package mx.redhat.ericsson.dtra.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
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
import mx.redhat.ericsson.dtra.resources.ConfiguredCacheManager;
import mx.redhat.ericsson.dtra.solver.Schedule;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.slf4j.Logger;

@Stateless
@Local(ScheduleSolver.class)
public class SchedulerSolverBean implements ScheduleSolver
{
	@Inject
	@ConfiguredCacheManager
	EmbeddedCacheManager defaultCacheManager;
	
	@EJB
	Executor executorService;

	@PersistenceContext(unitName = "dtra")
	EntityManager em;
	
	@Inject
	Logger log;
	
	@Override
	public List<Schedule> retrieveSolutions(String sessionId) 
	{
		List<Schedule> solutions = getSolutionCache().get(sessionId);
        if (solutions == null) {
//            URL unsolvedSolutionURL = getClass().getResource(IMPORT_DATASET);
//            solution = (VehicleRoutingSolution) new VehicleRoutingImporter(true)
//                    .readSolution(unsolvedSolutionURL);
        	solutions  = new ArrayList<Schedule>();
//            sessionSolutionMap.put(sessionId, solution);
        	getSolutionCache().put(sessionId, solutions);
        }
        return solutions;
	}
	
	@Override
	public boolean terminateEarly(String sessionId) 
	{
		Solver solver = getSolverCache().remove(sessionId);
        if (solver != null) {
            solver.terminateEarly();
            return true;
        } else {
            return false;
        }
	}
	
	@Override
	public boolean solve(final Schedule unsolvedSchedule, final String sessionId) 
	{
		final Solver solver = configureSolver();
		
		solver.addEventListener(new SolverEventListener<Schedule>() 
		{
            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent<Schedule> event) 
            {
                Schedule bestSolution = event.getNewBestSolution();
                synchronized (SchedulerSolverBean.this) 
                {
                	List<Schedule> solutions = getSolutionCache().get(sessionId);
                	if (solutions == null) {
                		solutions = new ArrayList<Schedule>();
                	}
                	
                	bestSolution.setTimestamp(new Timestamp(new Date().getTime()));
                	
                	solutions.add(bestSolution);
                	
                	getSolutionCache().put(sessionId, solutions);
                }
            }
        });
		
		if (getSolverCache().containsKey(sessionId)) {
            return false;
        }
		
		getSolverCache().put(sessionId, solver);
		
		Runnable solveTask = new Runnable() 
		{
            @Override
            public void run() 
            {
            	aggregateEngineerData(unsolvedSchedule);
            	
                // Solve the problem
                solver.solve(unsolvedSchedule);
                
                Schedule solvedSchedule = (Schedule) solver.getBestSolution();

                // Display the result
                toDisplayString(solvedSchedule);
                
                synchronized (SchedulerSolverBean.this) 
                {
                	List<Schedule> solutions = getSolutionCache().get(sessionId);
                	if (solutions == null) {
                		solutions = new ArrayList<Schedule>();
                	}
                	solutions.add(solvedSchedule);
                	
                	getSolutionCache().put(sessionId, solutions);
                    
                	getSolverCache().remove(sessionId);
                }
            }
        };
        
        executorService.execute(solveTask);
		
        return true;
	}

	protected void aggregateEngineerData(Schedule unsolvedSchedule) 
	{
		List<Engineer> engineers = new ArrayList<Engineer>();
		
		for (Engineer entity : unsolvedSchedule.getEngineerList()) 
		{
			entity = em.merge(entity);
			em.refresh(entity);
			engineers.add(entity);
		}
		unsolvedSchedule.setEngineerList(engineers);;
	}

	private Solver configureSolver() 
	{
        // Build the Solver
        SolverFactory solverFactory = SolverFactory.createFromXmlResource(
                "dtraScheduleSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        return solver;
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
	
	private Cache<String, List<Schedule>> getSolutionCache()
	{
		Cache<String, List<Schedule>> cache = defaultCacheManager.getCache("solutions-cache");
		
		return cache;
	}

	private Cache<String, Solver> getSolverCache()
	{
		Cache<String, Solver> cache = defaultCacheManager.getCache("solvers-cache");
		
		return cache;
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

	@Override
	public List<Engineer> loadEngineers() 
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Engineer> cq = cb.createQuery(Engineer.class);
		
		Root<Engineer> task = cq.from(Engineer.class);
		
		cq.select(task).where(cb.equal(task.get("calendar"), 50405377));
		
		TypedQuery<Engineer> tq = em.createQuery(cq);
		
//		tq.setMaxResults(5);
		
		List<Engineer> engineers = tq.getResultList();
		
		log.debug(String.format("Loaded engineers: %d", engineers.size()));
		
		return engineers;	
	}

	@PreDestroy
    public synchronized void destroy() 
	{
        for (Solver solver : getSolverCache().values()) {
            solver.terminateEarly();
        }
        defaultCacheManager.stop();
    }
}
