package mx.redhat.ericsson.dtra.resources;

import java.util.concurrent.Executor;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

@Stateless(name="Executor")
public class ExecutorBean implements Executor {

    @Override
    @Asynchronous
    public void execute(Runnable command) {
        command.run();
    }
}