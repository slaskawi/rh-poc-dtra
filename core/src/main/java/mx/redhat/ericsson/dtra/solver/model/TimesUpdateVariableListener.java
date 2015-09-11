package mx.redhat.ericsson.dtra.solver.model;

import java.sql.Timestamp;

import mx.redhat.ericsson.dtra.model.EngineerOrTask;
import mx.redhat.ericsson.dtra.model.Task;



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
            // If after time add nightover
            LocalTime endLocalTime = new LocalTime(endTime);
            if (new LocalTime(18,0).isBefore(endLocalTime)) {
            	endTime = new Timestamp(endTime.getTime() + (14 * 60 * 60 * 1000));
            }
            
            scoreDirector.beforeVariableChanged(shadowTask, "endingTime");
            shadowTask.setEndingTime(endTime);
            scoreDirector.afterVariableChanged(shadowTask, "endingTime");
            
            startTime = shadowTask.getEndingTime();
            
            shadowTask = shadowTask.getNextTask();
            
            endTime = calculateEndTime(shadowTask, startTime);
            // If after time add nightover
            endLocalTime = new LocalTime(endTime);
            if (new LocalTime(18,0).isBefore(endLocalTime)) {
            	endTime = new Timestamp(endTime.getTime() + (14 * 60 * 60 * 1000));
            }
		}
	}
	
	private Timestamp calculateStartTime(Task sourceTask, EngineerOrTask previousEngineerOrTask)
	{
		if (previousEngineerOrTask instanceof Task) {
			return ((Task)previousEngineerOrTask).getEndingTime();
		}
		
		if (sourceTask.getEngineer() != null) {
			return sourceTask.getEngineer().getStartTime();
		}
		
		return null;
	}

	private Timestamp calculateEndTime(Task shadow, Timestamp previousEndTime) 
	{
		if (shadow == null) {
			return null;
		}
		
		if (previousEndTime == null) 
		{
			// check if not anchor (engineer)
			if (shadow.getEngineer() == null) 
			{
				return null;//new Date( 0 + (shadow.getDuration() * 1000) );				
			}
			
			return new Timestamp( shadow.getEngineer().getStartTime().getTime() + (shadow.getDuration() * 1000) );			
		}
		
		return new Timestamp( previousEndTime.getTime() + (shadow.getDuration() * 1000) );
	}

}
