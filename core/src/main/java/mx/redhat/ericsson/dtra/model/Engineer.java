package mx.redhat.ericsson.dtra.model;

import java.sql.Timestamp;


public class Engineer implements EngineerOrTask
{
	Long id;
	Timestamp startTime;
	
	// Shadow variables
    Task nextTask;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	// ************************************************************************
    // Complex methods
    // ************************************************************************
	@Override
	public Engineer getEngineer() {
		return this;
	}

	@Override
	public Task getNextTask() {
		return nextTask;
	}

	@Override
	public void setNextTask(Task nextTask) {
		this.nextTask = nextTask;
	}

	public String toString() {
		if (getId() != null) {
			return String.valueOf(getId());
		}
		return super.toString();
	}

}
