package itba.edu.ar.output.plot;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

import itba.edu.ar.cellAutomataOffLattice.CellAutomataOffLatticeObserver;
import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public class PlotIndexByNoise implements Plotter {

	private List<IndexByNoiseSerie> series = new LinkedList<PlotIndexByNoise.IndexByNoiseSerie>();
	private Double noise;
	private List<Double> noises = new LinkedList<Double>();
	private List<Double> indexes = new LinkedList<Double>();
	private String path;
	private int particleQuantity;
	private double indexAverage=0;

	public PlotIndexByNoise(String path) {
		this.path = path;
	}

	public void finishedStep(List<Particle> particles, int timeStep, double length) {
		// TODO Auto-generated method stub
	}

	public void initialState(List<Particle> particles, int timeStep, double length) {
		// TODO Auto-generated method stub
	}

	public void endSimulationStep(List<Particle> particles) {
		FloatPoint sum = new FloatPoint(0, 0);

		for (Particle particle : particles)
			sum = sum.plus(particle.getVelocity());

		double abs = sum.abs();

		Double index = abs / (particles.size() * getConstantVelocityAbs(particles));

		indexAverage += index;
	}

	public void setNoise(double noise) {
		this.noise = noise;
	}

	public void setParticleQuantity(Integer particleQuantity) {
		this.particleQuantity = particleQuantity;
	}

	public void startSimulation(double noise,double density) {
		this.noise = noise;
	}

	//Create a new serie
	public void endParticleQuantityStep() {
		series.add(new IndexByNoiseSerie(noises, indexes, particleQuantity));
		this.indexes = new LinkedList<Double>();
		this.noises = new LinkedList<Double>();
	}

	private double getConstantVelocityAbs(List<Particle> particles) {
		return particles.get(0).getVelocityAbs();
	}

	public void plot() throws IOException {

		// Create Chart
		final XYChart chart = new XYChartBuilder().width(600).height(400).title("Order Index by Nois")
				.xAxisTitle("Noise").yAxisTitle("Order Index").build();

		// Customize Chart
		chart.getStyler().setLegendPosition(LegendPosition.InsideNE);

		// Series
		for (IndexByNoiseSerie serie : series) {

			chart.addSeries("N=" + serie.getParticleQuantity(), serie.getNoises(), serie.getIndexes());
		}

		// Show it
		BitmapEncoder.saveBitmapWithDPI(chart, path + "IndexByNoise", BitmapFormat.PNG, 300);

	}

	private class IndexByNoiseSerie {

		private List<Double> noises;
		private List<Double> indexes;
		private Integer particleQuantity;

		public IndexByNoiseSerie(List<Double> noises, List<Double> indexes, int particleQuantity) {
			super();
			this.noises = noises;
			this.indexes = indexes;
			this.particleQuantity = particleQuantity;
		}

		public double[] getNoises() {
			return listToArray(noises);
		}

		public double[] getIndexes() {
			return listToArray(indexes);
		}

		public Integer getParticleQuantity() {
			return particleQuantity;
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

	}

	public void endSimulation(int simulationTimes) {
		indexes.add(indexAverage/simulationTimes);
		noises.add(noise);
		indexAverage=0;
	}

	

}
