package itba.edu.ar.test;

import itba.edu.ar.output.plot.Plotter;

public interface CellAutomataOffLatticeOrderIndexData {

	public double getDensity(int particleQuantity) ;
	
	public Plotter getPlotter();
	
	public double getLength(int particleQuantity);

	public double getVelocityAbs();

	public double getRadio();

	public String getPath();

	public int getTimeStep();

	public double getInteractionRadio();

	public int getSimulationTimes();
	
}
