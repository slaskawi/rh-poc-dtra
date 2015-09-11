package mx.redhat.ericsson.dtra.scheduler;

import java.util.List;

import mx.redhat.ericsson.dtra.model.Task;

public interface Scheduler {

	void execute();

	List<Task> loadTasks(String wpg);

}
