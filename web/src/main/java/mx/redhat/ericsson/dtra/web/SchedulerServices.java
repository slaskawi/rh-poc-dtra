package mx.redhat.ericsson.dtra.web;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mx.redhat.ericsson.dtra.model.Task;
import mx.redhat.ericsson.dtra.scheduler.Scheduler;

@Path("scheduler")
public class SchedulerServices 
{
	@EJB
	Scheduler service;
	
	@POST
	@Path("/execute")
	@Produces(MediaType.APPLICATION_JSON)
	public Response execute()
	{
		service.execute();
		
		return Response.ok().build();
	}

	@GET
	@Path("/tasks/wpg/{wpg}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> loadTasks(@PathParam(value="wpg")String wpg)
	{
		return service.loadTasks(wpg);
	}
}
