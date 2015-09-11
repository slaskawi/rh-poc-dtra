package mx.redhat.ericsson.dtra.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public interface EngineerOrTask 
{
	Engineer getEngineer();
	
	@InverseRelationShadowVariable(sourceVariableName = "previousEngineerOrTask")
    Task getNextTask();
	
    void setNextTask(Task nextTask);
}
