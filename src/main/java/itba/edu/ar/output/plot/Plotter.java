package itba.edu.ar.output.plot;

import java.io.IOException;

import itba.edu.ar.cellAutomataOffLattice.CellAutomataOffLatticeObserver;

public interface Plotter extends CellAutomataOffLatticeObserver{

	public void setParticleQuantity(Integer particleQuantity);

	public void startSimulation(double noise, double density);

	public void endParticleQuantityStep();

	public void plot() throws IOException;

	public void endSimulation(int simulationTimes);
}
