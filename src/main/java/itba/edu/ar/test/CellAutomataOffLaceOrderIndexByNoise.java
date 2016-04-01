package itba.edu.ar.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import itba.edu.ar.output.plot.PlotIndexByNoise;
import itba.edu.ar.output.plot.Plotter;

public class CellAutomataOffLaceOrderIndexByNoise implements CellAutomataOffLaceOrderIndexData {

	private static String path = System.getProperty("user.dir") + "/";
	private static double length = 20;
	private static int interactionRadio = 1;
	private static int simulationTimes = 400;
	private static double fromNoise = 0;
	private static double stepNoise = 0.5;
	private static double toNoise = 5;
	private static List<Integer> particleQuantities = Arrays.asList((new Integer[] { 40, 100 }));
	private static double density = 4;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		indexByNoise();
	}
	
	
	public static void indexByNoise() throws InstantiationException, IllegalAccessException, IOException {

		CellAutomataOffLaceOrderIndexData data = new CellAutomataOffLaceOrderIndexByNoise();
		
		(new CellAutomataOffLaceOrderIndex(simulationTimes, fromNoise, stepNoise,
				toNoise, particleQuantities,data)).start();

	}

	public double getDensity(int particleQuantity) {
		return density;
	}

	public Plotter getPlotter() {
		return new PlotIndexByNoise(path);
	}

	public double getLength(int particleQuantity) {
		return Math.sqrt(particleQuantity/density);
	}

	public double getVelocityAbs() {
		// TODO Auto-generated method stub
		return 0.03;
	}

	public double getRadio() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	public int getTimeStep() {
		// TODO Auto-generated method stub
		return 1;
	}

	public double getInteractionRadio() {
		// TODO Auto-generated method stub
		return 1;
	}

	public int getSimulationTimes() {
		return 3;
	}

}
