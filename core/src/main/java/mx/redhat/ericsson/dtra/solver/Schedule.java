package mx.redhat.ericsson.dtra.solver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mx.redhat.ericsson.dtra.model.Engineer;
import mx.redhat.ericsson.dtra.model.Task;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Schedule implements Solution<HardSoftScore> 
{
	Long id;
	Timestamp timestamp;
	
	// Problem facts
	List<Engineer> engineerList;
	
	// Planning entities
	List<Task> taskList;
	
	HardSoftScore score;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "engineerRange")
    public List<Engineer> getEngineerList() {
        return engineerList;
    }

    public void setEngineerList(List<Engineer> engineerList) {
        this.engineerList = engineerList;
    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "taskRange")
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	@Override
	public HardSoftScore getScore() {
		return score;
	}

	@Override
	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	@Override
	public Collection<? extends Object> getProblemFacts() 
	{
		List<Object> facts = new ArrayList<Object>();
		return facts;
	}

}
