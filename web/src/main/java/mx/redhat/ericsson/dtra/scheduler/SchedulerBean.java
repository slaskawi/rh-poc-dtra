package mx.redhat.ericsson.dtra.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import mx.redhat.ericsson.dtra.model.Engineer;
import mx.redhat.ericsson.dtra.model.Task;
import mx.redhat.ericsson.dtra.solver.Schedule;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;

@Stateless
@Local(Scheduler.class)
public class SchedulerBean implements Scheduler
{
	@PersistenceContext(unitName = "dtra")
	EntityManager em;
	
	@Inject
	Logger log;

	@Override
	public void execute() 
	{
		Solver solver = configureSolver();
		
        // Load a problem
        Schedule unsolvedSchedule = createSchedule();

        // Solve the problem
        solver.solve(unsolvedSchedule);
        
        Schedule solvedSchedule = (Schedule) solver.getBestSolution();

        // Display the result
        toDisplayString(solvedSchedule);
	}

	private Solver configureSolver() 
	{
        // Build the Solver
        SolverFactory solverFactory = SolverFactory.createFromXmlResource(
                "dtraScheduleSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        return solver;
	}

	public Schedule createSchedule() 
	{
		List<Engineer> engineerList = createEngineers();
		
		List<Task> activityList = createTasks();
		
		Schedule schedule = new Schedule();
		
		schedule.setTaskList(activityList);
		schedule.setEngineerList(engineerList);
		
		return schedule;
	}

	private List<Engineer> createEngineers() 
	{
		List<Engineer> engineerList = new ArrayList<Engineer>();

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, Calendar.AUGUST);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Engineer e1 = new Engineer();
		e1.setId(10l);
		e1.setStartTime(new Timestamp(calendar.getTimeInMillis()));

		engineerList.add(e1);

		e1 = new Engineer();
		e1.setId(20l);
		e1.setStartTime(new Timestamp(calendar.getTimeInMillis()));

		engineerList.add(e1);
		
		return engineerList;
	}

	private List<Task> createTasks() 
	{
		TypedQuery<Task> tq = em.createQuery("from " + Task.class.getCanonicalName() + " where workpackagegroup = :wpg", Task.class);
		tq.setParameter("wpg", "7471264-2-M102");
		
		List<Task> tasks = tq.getResultList();
		
		System.out.println(tasks.size());
		
		return tasks;
	}

	private void toDisplayString(Schedule solvedSchedule) 
	{
		log.info("Solved with score: " + solvedSchedule.getScore()  + "");
		
		for (Engineer a : solvedSchedule.getEngineerList()) 
		{
			if (a.getNextTask() == null) {
				log.info("Engineer " + a.getId() + " has no tasks.");
			}
			else 
			{
				log.info("Engineer " + a.getId() + " has this tasks:");

				displayNextTasks(a.getNextTask());
			}
		}

		return;
	}

	private void displayNextTasks(Task task) 
	{
		if (task == null) {
			return;
		}
		
		log.info("\tTask " + task.getId() + " starts at " + task.getStartingTime() + " ends at " + task.getEndingTime() + " for Engineer: " + task.getEngineer());
		
		displayNextTasks(task.getNextTask());
	}

	@Override
	public List<Task> loadTasks(String wpg) 
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Task> cq = cb.createQuery(Task.class);
		
		Root<Task> task = cq.from(Task.class);
		
		ParameterExpression<String> p = cb.parameter(String.class);
		
		cq.select(task).where(cb.equal(task.get("workpackagegroup"), p));
		
		TypedQuery<Task> tq = em.createQuery(cq);
		
		tq.setParameter(p, wpg);
		
		List<Task> tasks = tq.getResultList();
		
		log.debug(String.format("Loaded tasks: %d", tasks.size()));
		
		return tasks;	
	}

}
