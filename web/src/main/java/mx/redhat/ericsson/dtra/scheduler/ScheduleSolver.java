package mx.redhat.ericsson.dtra.scheduler;

import java.util.List;

import mx.redhat.ericsson.dtra.model.Engineer;
import mx.redhat.ericsson.dtra.model.Task;
import mx.redhat.ericsson.dtra.solver.Schedule;

public interface ScheduleSolver 
{
	List<Schedule> retrieveSolutions(String sessionId);

	boolean solve(Schedule schedule, String sessionId);
	
	boolean terminateEarly(String sessionId);

	List<Task> loadTasks(String wpg);

	List<Engineer> loadEngineers();

}
