package mx.redhat.ericsson.dtra.web;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/application")
public class ApplicationActivator extends Application {

}