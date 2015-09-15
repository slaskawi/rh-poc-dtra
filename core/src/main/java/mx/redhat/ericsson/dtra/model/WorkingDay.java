package mx.redhat.ericsson.dtra.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonValue;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(schema = "public", name = "working_day")
public class WorkingDay 
{
	@Id
	@Column(name = "w6key")
	Long id;
	
	Integer day;
	Timestamp startTime;
	Timestamp endTime;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "calendar")
	EngineerCalendar calendar;
	
	public WorkingDay()
	{
		
	}
	
	public WorkingDay(Day day, Timestamp from, Timestamp to) {
	    this.day = day.getId();
	    this.startTime = from; // format time using 800 for 8:00am or 2300 for 23:00
	    this.endTime = to;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Day getDay() {
		return Day.getType(this.day);
	}

	public void setDay(Day day) {
		if (day == null) {
            this.day = null;
        } else {
            this.day = day.getId();
        }
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public EngineerCalendar getCalendar() {
		return calendar;
	}

	public void setCalendar(EngineerCalendar calendar) {
		this.calendar = calendar;
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum Day 
	{
		MONDAY(1, "Monday"), 
		TUESDAY(2, "Tuesday"), 
		WEDNESDAY(3, "Wednesday"), 
		THURSDAY(4, "Thursday"), 
		FRIDAY(5, "Friday"), 
		SATURDAY(6, "Saturday"), 
		SUNDAY(7, "Sunday");
		
		int id;
		String name;
		
		private Day(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public static Day getType(Integer id) 
		{
	        if (id == null) {
	            return null;
	        }
	 
	        for (Day position : Day.values()) {
	            if (id.equals(position.getId())) {
	                return position;
	            }
	        }
	        throw new IllegalArgumentException("No matching type for id " + id);
	    }
		
		public int getId() {
			return id;
		}
		@JsonValue
		public String getName() {
			return name;
		}
	}

}
