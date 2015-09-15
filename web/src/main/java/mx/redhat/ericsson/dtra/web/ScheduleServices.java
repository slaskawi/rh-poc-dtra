package mx.redhat.ericsson.dtra.web;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import mx.redhat.ericsson.dtra.model.Engineer;
import mx.redhat.ericsson.dtra.model.Task;
import mx.redhat.ericsson.dtra.scheduler.ScheduleSolver;
import mx.redhat.ericsson.dtra.scheduler.model.Message;
import mx.redhat.ericsson.dtra.solver.Schedule;

@Path("scheduler")
public class ScheduleServices 
{
	@EJB
	ScheduleSolver service;
	
	@Context
    HttpServletRequest request;
	
	@GET
    @Path("/solutions")
    @Produces("application/json")
    public List<Schedule> getSolutions() 
	{
        return service.retrieveSolutions(request.getSession().getId());
    }
	
	@POST
	@Path("/solve")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message execute(Schedule schedule)
	{
		schedule.setId(new Date().getTime() + (long)(Math.random() * 1000));
		boolean success = service.solve(schedule, request.getSession().getId());
		return success ? new Message("Solving started.") : new Message("Solver was already running.", Message.Type.WARNING);
	}
	
	@POST
    @Path("/terminateEarly")
    @Produces("application/json")
    public Message terminateEarly() {
        boolean success = service.terminateEarly(request.getSession().getId());
        return new Message(success ? "Solver terminating early." : "Solver was already terminated.", Message.Type.DANGER);
    }

	@GET
	@Path("/tasks/wpg/{wpg}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> loadTasks(@PathParam(value="wpg")String wpg)
	{
		return service.loadTasks(wpg);
	}

	@GET
	@Path("/engineers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Engineer> loadEngineers()
	{
		return service.loadEngineers();
	}
}
