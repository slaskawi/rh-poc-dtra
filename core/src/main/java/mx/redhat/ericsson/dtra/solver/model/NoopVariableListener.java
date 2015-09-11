package mx.redhat.ericsson.dtra.solver.model;

import mx.redhat.ericsson.dtra.model.EngineerOrTask;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class NoopVariableListener implements VariableListener<EngineerOrTask>{

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector,
			EngineerOrTask entity) {
		// Do nothing
	}

}
