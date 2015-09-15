package mx.redhat.ericsson.dtra.scheduler.model;

import org.codehaus.jackson.annotate.JsonValue;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Message 
{
	String text;
	Type type = Type.SUCCESS;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum Type 
	{
    	SUCCESS(1, "success"),
    	DANGER(2, "danger"),
    	WARNING(3, "warning");
    	
    	int id;
    	String name;
    	
    	private Type(final int id, final String name) {
    		this.id = id;
    		this.name = name;
    	}
    	
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		@JsonValue
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    }
}
