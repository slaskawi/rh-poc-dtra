package mx.redhat.ericsson.dtra.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import mx.redhat.ericsson.dtra.solver.model.NoopVariableListener;
import mx.redhat.ericsson.dtra.solver.model.TimesUpdateVariableListener;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

@Entity
@Table(schema = "public", name = "activity")
@PlanningEntity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task implements EngineerOrTask 
{
	@Id
	@Column(name = "w6key")
	Long id;
	String actname;
	@Formula(value = "duration * 1000")
	Long duration;
	Timestamp earlyStart;
	Timestamp lateStart;
	Timestamp dueDate;
	String workpackage;
	String workpackagegroup;
	
	// Planning variables: changes during planning, between score calculations.
	@Transient
	@JsonIgnore
	EngineerOrTask previousEngineerOrTask;
	
	// Shadow variables
	@Transient
	@JsonIgnore
	Engineer engineer;
	@Transient
	Task nextTask;
	
	@Transient
	Timestamp startingTime;	
	@Transient
	Timestamp endingTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActname() {
		return actname;
	}

	public void setActname(String actname) {
		this.actname = actname;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public String getWorkpackage() {
		return workpackage;
	}

	public void setWorkpackage(String workpackage) {
		this.workpackage = workpackage;
	}

	public String getWorkpackagegroup() {
		return workpackagegroup;
	}

	public void setWorkpackagegroup(String workpackagegroup) {
		this.workpackagegroup = workpackagegroup;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Timestamp getEarlyStart() {
		return earlyStart;
	}

	public void setEarlyStart(Timestamp earlyStart) {
		this.earlyStart = earlyStart;
	}

	public Timestamp getLateStart() {
		return lateStart;
	}

	public void setLateStart(Timestamp lateStart) {
		this.lateStart = lateStart;
	}

	@PlanningVariable(valueRangeProviderRefs = {"engineerRange", "taskRange"}, graphType = PlanningVariableGraphType.CHAINED)
	public EngineerOrTask getPreviousEngineerOrTask() {
		return previousEngineerOrTask;
	}

	public void setPreviousEngineerOrTask(EngineerOrTask previousEngineerOrTask) {
		this.previousEngineerOrTask = previousEngineerOrTask;
	}

	@AnchorShadowVariable(sourceVariableName = "previousEngineerOrTask")
	public Engineer getEngineer() {
		return engineer;
	}

	public void setEngineer(Engineer engineer) {
		this.engineer = engineer;
	}

	public Task getNextTask() {
		return nextTask;
	}

	public void setNextTask(Task nextTask) {
		this.nextTask = nextTask;
	}

	@CustomShadowVariable(sources = {@CustomShadowVariable.Source(variableName = "previousEngineerOrTask") }, variableListenerClass = TimesUpdateVariableListener.class)
	public Timestamp getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Timestamp startingTime) {
		this.startingTime = startingTime;
	}

	@CustomShadowVariable(sources = {@CustomShadowVariable.Source(variableName = "previousEngineerOrTask") }, variableListenerClass = NoopVariableListener.class)
	public Timestamp getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(Timestamp endingTime) {
		this.endingTime = endingTime;
	}

	public String toString() {
		if (getId() != null) {
			return String.valueOf(getId());
		}
		return super.toString();
	}
}
