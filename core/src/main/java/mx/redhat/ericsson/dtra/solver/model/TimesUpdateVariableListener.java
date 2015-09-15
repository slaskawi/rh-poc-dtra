package mx.redhat.ericsson.dtra.solver.model;

import java.sql.Timestamp;
import java.util.Date;

import mx.redhat.ericsson.dtra.model.Engineer;
import mx.redhat.ericsson.dtra.model.EngineerOrTask;
import mx.redhat.ericsson.dtra.model.Task;







import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
//import org.apache.commons.lang.ObjectUtils;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class TimesUpdateVariableListener implements VariableListener<EngineerOrTask>{

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		if (entity instanceof Task) {
            updateTimes(scoreDirector, (Task) entity);
        }
	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		if (entity instanceof Task) {
            updateTimes(scoreDirector, (Task) entity);
        }
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	private void updateTimes(ScoreDirector scoreDirector, Task sourceTask) 
	{
		EngineerOrTask previousEngineerOrTask = sourceTask.getPreviousEngineerOrTask();
		
		Timestamp startTime = calculateStartTime(sourceTask, previousEngineerOrTask);
		
		Task shadowTask = sourceTask;
		
		while (shadowTask != null) 
		{
			scoreDirector.beforeVariableChanged(shadowTask, "startingTime");
            shadowTask.setStartingTime(startTime);
            scoreDirector.afterVariableChanged(shadowTask, "startingTime");

            Timestamp endTime = calculateEndTime(shadowTask, startTime);
            
            scoreDirector.beforeVariableChanged(shadowTask, "endingTime");
            shadowTask.setEndingTime(endTime);
            scoreDirector.afterVariableChanged(shadowTask, "endingTime");
            
            startTime = shadowTask.getEndingTime();
            
            shadowTask = shadowTask.getNextTask();
            
            endTime = calculateEndTime(shadowTask, startTime);            
		}
	}
	
	private Timestamp calculateStartTime(Task sourceTask, EngineerOrTask previousEngineerOrTask)
	{
		if (previousEngineerOrTask instanceof Task) {
			return ((Task)previousEngineerOrTask).getEndingTime();
		}
		
		Engineer engineer = sourceTask.getEngineer();
		
		if (engineer != null) 
		{
			Date scheduleDay = new LocalDate().toDateTimeAtStartOfDay().toDate();
			Date engineersStartTime = engineer.getCalendar().getStartingTime(scheduleDay);
			Timestamp scheduleDayStartTime = new Timestamp(scheduleDay.getTime() + engineersStartTime.getTime());

			return scheduleDayStartTime;
		}
		
		return null;
	}

	private Timestamp calculateEndTime(Task shadow, Timestamp previousEndTime) 
	{
		if (shadow == null) {
			return null;
		}
		
		Engineer engineer = shadow.getEngineer();
		
		if (previousEndTime == null) 
		{
			// check if not anchor (engineer)
			if (engineer == null) 
			{
				return null;//new Date( 0 + (shadow.getDuration() * 1000) );				
			}
			
			//TODO Does it gets here anyway?
			return new Timestamp( engineer.getCalendar().getStartingTime(new Date()).getTime() + shadow.getDuration() );			
		}
		
		Timestamp endTime = new Timestamp( previousEndTime.getTime() + shadow.getDuration() );
		
		DateTime endTimeDate = new LocalDate(endTime).toDateTimeAtStartOfDay();
		
		LocalTime endTimeTime = new DateTime(0).plus(new LocalTime(endTime).getMillisOfDay()).toLocalTime();

		LocalTime engineersLunchStart = new LocalTime(engineer.getCalendar().getLunchStart());
		LocalTime engineersLunchEnd = new LocalTime(engineer.getCalendar().getLunchEnd());
		
		// if between lunch add lunch break
		if (endTimeTime.isAfter(engineersLunchStart) && endTimeTime.isBefore(engineersLunchEnd)) 
		{
			Long lunchBreak = (long)engineersLunchEnd.getMillisOfDay() - engineersLunchStart.getMillisOfDay();

			endTime = new Timestamp(endTime.getTime() + lunchBreak);			
		}
		
		LocalTime engineersEndTime = new LocalTime(engineer.getCalendar().getEndingTime(endTimeDate.toDate()));
		
		// If after time add nightover
		if (endTimeTime.isAfter(engineersEndTime)) 
		{
			DateTime nextWorkingDay = endTimeDate.plusDays(1);
			Date engineersStartTime = engineer.getCalendar().getStartingTime(nextWorkingDay.toDate());
			
			Long nightOver = (long)endTimeTime.getMillisOfDay() - engineersEndTime.getMillisOfDay();
			
			endTime = new Timestamp(nextWorkingDay.plus(engineersStartTime.getTime()).plus(nightOver).toDate().getTime());
		}            	
		
		return endTime;
	}

}
