package itba.edu.ar.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.output.plot.Plotter;
import itba.edu.ar.output.plot.PlotterIndexByDensity;

public class CellAutomataOffLaceOrderIndexByDensity implements CellAutomataOffLaceOrderIndexData {

	private static final int simlationTimes = 100;
	private static String path = System.getProperty("user.dir") + "/";
	private static double length = 20;
	private static int interactionRadio = 1;
	private static int frames = 4000;
	private static double fromNoise = 0.5;
	private static double stepNoise = 0.5;
	private static double toNoise = 1;
	private static double fromDensity =1;
	private static double stepDensity = 1;
	private static double toDensity = 11;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		indexByDensity();
	}

	public static void indexByDensity() throws InstantiationException, IllegalAccessException, IOException {

		CellAutomataOffLaceOrderIndexByDensity data = new CellAutomataOffLaceOrderIndexByDensity();

		List<Integer> particleQuantities = getParticleQuantities(fromDensity, stepDensity, toDensity, length);

		(new CellAutomataOffLaceOrderIndex( frames, fromNoise, stepNoise, toNoise,
				particleQuantities, data)).start();
	}

	private static List<Integer> getParticleQuantities(double fromDensity, double stepDensity, double toDensity,
			double length) {
		List<Integer> ans = new LinkedList<Integer>();
		for (double density = fromDensity; density < toDensity; density += stepDensity) {
			ans.add(getParticleQuantity(density, length));
		}
		return ans;
	}
	
	

	private static int getParticleQuantity(double density, double length) {
		return (int) Math.floor(density * Math.pow(length, 2));
	}

	public double getDensity(int particleQuantity) {
		return particleQuantity / Math.pow(length, 2);
	}

	public Plotter getPlotter() {
		return new PlotterIndexByDensity(path);
	}
	
	public double getLength(int particleQuantity){
		return length;
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
		return simlationTimes;
	}

}
