package mx.redhat.ericsson.dtra.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import mx.redhat.ericsson.dtra.model.WorkingDay.Day;

@Entity
@Table(schema = "public", name = "calendar")
public class EngineerCalendar 
{
	@Id
	@Column(name = "w6key")	
	Long id;

	Timestamp lunchStart;
	Timestamp lunchEnd;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "calendar")
	@MapKey(name = "day")
	Map<Day, WorkingDay> workingDays;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getLunchStart() {
		return lunchStart;
	}

	public void setLunchStart(Timestamp lunchStart) {
		this.lunchStart = lunchStart;
	}

	public Timestamp getLunchEnd() {
		return lunchEnd;
	}

	public void setLunchEnd(Timestamp lunchEnd) {
		this.lunchEnd = lunchEnd;
	}

	public Map<Day, WorkingDay> getWorkingDays() {
		if (workingDays == null) {
			workingDays = new HashMap<Day, WorkingDay>();
		}
		return workingDays;
	}

	public void setWorkingDays(Map<Day, WorkingDay> workingDays) {
		this.workingDays = workingDays;
	}

	// ************************************************************************
    // Complex methods
    // ************************************************************************
	public Timestamp getStartingTime(Date date) {
		Integer day = LocalDate.fromDateFields(date).getDayOfWeek();
		return getStartingTime(day);
	}

	public Timestamp getEndingTime(Date date) {
		Integer day = LocalDate.fromDateFields(date).getDayOfWeek();
		return getEndingTime(day);
	}
	
	public Timestamp getStartingTime(Integer dayOfWeek) 
	{
		// TODO Use Day enum
		WorkingDay wDay = workingDays.get(dayOfWeek);//Day.getType(day));
		if (wDay == null) {
			// not working day, return next working date
			if (dayOfWeek > 7) {
				return getStartingTime(1);
			}
			return getStartingTime(++dayOfWeek);
		}
		return wDay.getStartTime();
	}

	public Timestamp getEndingTime(Integer dayOfWeek) 
	{
		// TODO Use Day enum
		WorkingDay wDay = workingDays.get(dayOfWeek);//Day.getType(day));
		if (wDay == null) {
			// TODO create default calendar day
			return null;
		}
		return wDay.getEndTime();
	}
}
