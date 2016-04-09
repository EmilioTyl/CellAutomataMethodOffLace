package itba.edu.ar.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import itba.edu.ar.output.plot.PlotIndexByNoise;
import itba.edu.ar.output.plot.Plotter;

public class CellAutomataOffLatticeOrderIndexByNoise implements CellAutomataOffLatticeOrderIndexData {

	private static final int _simulationTimes = 20;
	private static String path = System.getProperty("user.dir") + "/";
	private static int interactionRadio = 1;
	private static int frames = 4000;
	private static double fromNoise = 0;
	private static double stepNoise = 0.5;
	private static double toNoise = 5.5;
	private static List<Integer> particleQuantities = Arrays.asList((new Integer[] { 40, 100, 400, 4000, 10000 }));
	private static double density = 4;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		indexByNoise();
	}

	public static void indexByNoise() throws InstantiationException, IllegalAccessException, IOException {

		CellAutomataOffLatticeOrderIndexData data = new CellAutomataOffLatticeOrderIndexByNoise();

		(new CellAutomataOffLatticeOrderIndex(frames, fromNoise, stepNoise, toNoise, particleQuantities, data)).start();

	}

	public double getDensity(int particleQuantity) {
		return density;
	}

	public Plotter getPlotter() {
		return new PlotIndexByNoise(path);
	}

	public double getLength(int particleQuantity) {
		return Math.sqrt(particleQuantity / density);
	}

	public double getVelocityAbs() {
		return 0.03;
	}

	public double getRadio() {
		return 0;
	}

	public String getPath() {
		return path;
	}

	public int getTimeStep() {
		return 1;
	}

	public double getInteractionRadio() {
		return interactionRadio;
	}

	public int getSimulationTimes() {
		return _simulationTimes;
	}

}
