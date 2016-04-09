package itba.edu.ar.cellAutomataOffLattice;

import java.util.List;

import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public interface CellAutomataOffLatticeObserver {

	public void finishedStep(List<Particle> particles,int timeStep, double length);

	public void initialState(List<Particle> particles, int timeStep, double length);

	public void endSimulationStep(List<Particle> particles);
	
}
