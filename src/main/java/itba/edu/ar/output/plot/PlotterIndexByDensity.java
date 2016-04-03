package itba.edu.ar.output.plot;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public class PlotterIndexByDensity implements Plotter{

	private double noise;
	private Double density;
	private List<Double> densities = new LinkedList<Double>();
	private List<Double> indexes = new LinkedList<Double>();
	private String path;
	private double indexAverage=0;

	public PlotterIndexByDensity(String path) {
		this.path = path;
	}

	public void finishedStep(List<Particle> particles, int timeStep, double length) {
		// TODO Auto-generated method stub
	}

	public void initialState(List<Particle> particles, int timeStep, double length) {
		// TODO Auto-generated method stub
	}

	public void setParticleQuantity(Integer particleQuantity) {
	}

	public void startSimulation(double noise,double density) {
		this.noise = noise;
		this.density=density;
	}


	private double getConstantVelocityAbs(List<Particle> particles) {
		return particles.get(0).getVelocityAbs();
	}

	private double[] listToArray(List<Double> list) {
		double[] ans = new double[list.size()];
		int i = 0;
		for (Double value : list) {
			ans[i] = value.doubleValue();
			i++;
		}
		return ans;
	}

	public void plot() throws IOException {

		// Create Chart
		final XYChart chart = new XYChartBuilder().width(600).height(400).title("Order Index by Density")
				.xAxisTitle("Density").yAxisTitle("Order Index").build();

		// Customize Chart
		chart.getStyler().setLegendPosition(LegendPosition.OutsideE);

		// Series
		chart.addSeries("Noise=" + noise, listToArray(densities), listToArray(indexes));

		// Show it
		BitmapEncoder.saveBitmapWithDPI(chart, path + "IndexByDensity", BitmapFormat.PNG, 300);

	}

	public void endSimulationStep(List<Particle> particles) {
		FloatPoint sum = new FloatPoint(0, 0);

		for (Particle particle : particles)
			sum = sum.plus(particle.getVelocity());

		double abs = sum.abs();

		Double index = abs / (particles.size() * getConstantVelocityAbs(particles));

		indexAverage += index; 
		
	}

	public void endParticleQuantityStep() {
		// TODO Auto-generated method stub
		
	}

	public void endSimulation(int simulationTimes) {

		indexes.add(indexAverage/simulationTimes);
		densities.add(density);
		indexAverage=0;
	}

}
