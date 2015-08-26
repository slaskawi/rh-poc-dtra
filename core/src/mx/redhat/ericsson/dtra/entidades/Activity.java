package mx.redhat.ericsson.dtra.entidades;

// Generated 27/08/2015 12:50:18 PM by Hibernate Tools 4.3.1

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Activity generated by hbm2java
 */
@Entity
@Table(name = "activity", schema = "public")
public class Activity implements java.io.Serializable {

	private long activityId;
	private String activityName;
	private Integer activityNormHours;
	private Integer activityLeadTime;
	private Integer activityIntensity;
	private String timeDependencyType;
	private Long lagTime;
	private Long timeDependentActivity;
	private Integer engineerDependency;
	private Integer engineerDependentActivity;
	private Serializable competenceProfiles;
	private Integer priority;
	private Serializable preferredEngineers;
	private Serializable requiredEngnineers;
	private Integer deliveryUnit;
	private Integer unit;
	private Date earlyStart;
	private Date lateStart;
	private Date dueDate;

	public Activity() {
	}

	public Activity(long activityId) {
		this.activityId = activityId;
	}

	public Activity(long activityId, String activityName,
			Integer activityNormHours, Integer activityLeadTime,
			Integer activityIntensity, String timeDependencyType, Long lagTime,
			Long timeDependentActivity, Integer engineerDependency,
			Integer engineerDependentActivity, Serializable competenceProfiles,
			Integer priority, Serializable preferredEngineers,
			Serializable requiredEngnineers, Integer deliveryUnit,
			Integer unit, Date earlyStart, Date lateStart, Date dueDate) {
		this.activityId = activityId;
		this.activityName = activityName;
		this.activityNormHours = activityNormHours;
		this.activityLeadTime = activityLeadTime;
		this.activityIntensity = activityIntensity;
		this.timeDependencyType = timeDependencyType;
		this.lagTime = lagTime;
		this.timeDependentActivity = timeDependentActivity;
		this.engineerDependency = engineerDependency;
		this.engineerDependentActivity = engineerDependentActivity;
		this.competenceProfiles = competenceProfiles;
		this.priority = priority;
		this.preferredEngineers = preferredEngineers;
		this.requiredEngnineers = requiredEngnineers;
		this.deliveryUnit = deliveryUnit;
		this.unit = unit;
		this.earlyStart = earlyStart;
		this.lateStart = lateStart;
		this.dueDate = dueDate;
	}

	@Id
	@Column(name = "activity_id", unique = true, nullable = false)
	public long getActivityId() {
		return this.activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	@Column(name = "activity_name", length = 100)
	public String getActivityName() {
		return this.activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Column(name = "activity_norm_hours")
	public Integer getActivityNormHours() {
		return this.activityNormHours;
	}

	public void setActivityNormHours(Integer activityNormHours) {
		this.activityNormHours = activityNormHours;
	}

	@Column(name = "activity_lead_time")
	public Integer getActivityLeadTime() {
		return this.activityLeadTime;
	}

	public void setActivityLeadTime(Integer activityLeadTime) {
		this.activityLeadTime = activityLeadTime;
	}

	@Column(name = "activity_intensity")
	public Integer getActivityIntensity() {
		return this.activityIntensity;
	}

	public void setActivityIntensity(Integer activityIntensity) {
		this.activityIntensity = activityIntensity;
	}

	@Column(name = "time_dependency_type", length = 2)
	public String getTimeDependencyType() {
		return this.timeDependencyType;
	}

	public void setTimeDependencyType(String timeDependencyType) {
		this.timeDependencyType = timeDependencyType;
	}

	@Column(name = "lag_time")
	public Long getLagTime() {
		return this.lagTime;
	}

	public void setLagTime(Long lagTime) {
		this.lagTime = lagTime;
	}

	@Column(name = "time_dependent_activity")
	public Long getTimeDependentActivity() {
		return this.timeDependentActivity;
	}

	public void setTimeDependentActivity(Long timeDependentActivity) {
		this.timeDependentActivity = timeDependentActivity;
	}

	@Column(name = "engineer_dependency")
	public Integer getEngineerDependency() {
		return this.engineerDependency;
	}

	public void setEngineerDependency(Integer engineerDependency) {
		this.engineerDependency = engineerDependency;
	}

	@Column(name = "engineer_dependent_activity")
	public Integer getEngineerDependentActivity() {
		return this.engineerDependentActivity;
	}

	public void setEngineerDependentActivity(Integer engineerDependentActivity) {
		this.engineerDependentActivity = engineerDependentActivity;
	}

	@Column(name = "competence_profiles")
	public Serializable getCompetenceProfiles() {
		return this.competenceProfiles;
	}

	public void setCompetenceProfiles(Serializable competenceProfiles) {
		this.competenceProfiles = competenceProfiles;
	}

	@Column(name = "priority")
	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "preferred_engineers")
	public Serializable getPreferredEngineers() {
		return this.preferredEngineers;
	}

	public void setPreferredEngineers(Serializable preferredEngineers) {
		this.preferredEngineers = preferredEngineers;
	}

	@Column(name = "required_engnineers")
	public Serializable getRequiredEngnineers() {
		return this.requiredEngnineers;
	}

	public void setRequiredEngnineers(Serializable requiredEngnineers) {
		this.requiredEngnineers = requiredEngnineers;
	}

	@Column(name = "delivery_unit")
	public Integer getDeliveryUnit() {
		return this.deliveryUnit;
	}

	public void setDeliveryUnit(Integer deliveryUnit) {
		this.deliveryUnit = deliveryUnit;
	}

	@Column(name = "unit")
	public Integer getUnit() {
		return this.unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "early_start", length = 13)
	public Date getEarlyStart() {
		return this.earlyStart;
	}

	public void setEarlyStart(Date earlyStart) {
		this.earlyStart = earlyStart;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "late_start", length = 13)
	public Date getLateStart() {
		return this.lateStart;
	}

	public void setLateStart(Date lateStart) {
		this.lateStart = lateStart;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "due_date", length = 13)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

}
