package mx.redhat.ericsson.dtra.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@Table(schema = "public", name = "engineer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Engineer implements EngineerOrTask
{
	@Id
	@Column(name = "w6key")	
	Long id;
	
	String gsc;
	String unit;
	String subunit;
	String region;
	String disctrict;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "calendar", referencedColumnName = "w6key")
	EngineerCalendar calendar;
	
	// Shadow variables
	@Transient
    Task nextTask;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGsc() {
		return gsc;
	}

	public void setGsc(String gsc) {
		this.gsc = gsc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSubunit() {
		return subunit;
	}

	public void setSubunit(String subunit) {
		this.subunit = subunit;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDisctrict() {
		return disctrict;
	}

	public void setDisctrict(String disctrict) {
		this.disctrict = disctrict;
	}

	public EngineerCalendar getCalendar() {
		return calendar;
	}

	public void setCalendar(EngineerCalendar calendar) {
		this.calendar = calendar;
	}

	// ************************************************************************
    // Complex methods
    // ************************************************************************
	@Override
	@JsonIgnore
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
