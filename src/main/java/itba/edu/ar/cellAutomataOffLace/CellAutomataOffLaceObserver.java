package itba.edu.ar.cellAutomataOffLace;

import java.util.List;

import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public interface CellAutomataOffLaceObserver {

	public void finishedStep(List<Particle> particles,int timeStep, double length);

	public void initialState(List<Particle> particles, int timeStep, double length);

	public void endSimulationStep(List<Particle> particles);
	
}
