package itba.edu.ar.cellAutomataOffLattice;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import itba.edu.ar.cellIndexMethod.CellIndexMethod;
import itba.edu.ar.cellIndexMethod.IndexMatrix;
import itba.edu.ar.cellIndexMethod.IndexMatrixBuilder;
import itba.edu.ar.cellIndexMethod.data.particle.FloatPoint;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;
import itba.edu.ar.cellIndexMethod.route.routeImpl.OptimizedRoute;

public class CellAutomataOffLattice {

	private List<Particle> particles;
	private int cellQuantity;
	private String staticPath;
	private String dynamicPath;
	private int timeStep = 0;
	private double interactionRadio;
	private double radio;
	private double length;
	private double noise;
	private Map<Particle, Double> newAngles = new HashMap<Particle, Double>();
	private double deltaTime;
	private List<CellAutomataOffLatticeObserver> subscribers = new LinkedList<CellAutomataOffLatticeObserver>();

	public CellAutomataOffLattice(int cellQuantity, String staticPath, String dynamicPath, int timeStep,
			double interactionRadio, double radio, double length, double noise, double deltaTime) {
		super();
		this.cellQuantity = cellQuantity;
		this.staticPath = staticPath;
		this.dynamicPath = dynamicPath;
		this.timeStep = timeStep;
		this.interactionRadio = interactionRadio;
		this.radio = radio;
		this.length = length;
		this.noise = noise;
		this.deltaTime = deltaTime;
	}

	public void simulate(int frames) throws InstantiationException, IllegalAccessException, IOException {

		IndexMatrix indexMatrix = IndexMatrixBuilder.getIndexMatrix(staticPath, dynamicPath, cellQuantity, timeStep);
		particles = indexMatrix.getParticles();

		CellIndexMethod cellIndexMethod = new CellIndexMethod(indexMatrix,
				new OptimizedRoute(cellQuantity, true, length), interactionRadio, radio);

		notifyInitialState();

		for (int i = 0; i < frames; i++) {

			cellIndexMethod.execute();

			calculateNextStep();

			timeStep++;
			notifyFinishedStep();

			indexMatrix.clear();
			indexMatrix.addParticles(particles);
		}

		notifyEndSimulation();

	}

	private void notifyEndSimulation() {
		for (CellAutomataOffLatticeObserver subscriber : subscribers) {
			subscriber.endSimulationStep(particles);
		}
	}

	private void notifyInitialState() {
		for (CellAutomataOffLatticeObserver subscriber : subscribers) {
			subscriber.initialState(particles, timeStep, length);
		}
	}

	private void notifyFinishedStep() {
		for (CellAutomataOffLatticeObserver subscriber : subscribers) {
			subscriber.finishedStep(particles, timeStep, length);
		}
	}

	private void calculateNextStep() {

		for (Particle particle : particles) {
			fillNewAngle(particle);
		}

		for (Particle particle : particles) {
			FloatPoint newPosition = getNewPosition(particle);
			particle.setPosition(newPosition);
		}

		for (Particle particle : particles) {
			particle.setAngle(newAngles.get(particle));
		}

	}

	private FloatPoint getNewPosition(Particle particle) {
		return particle.getPosition().plus(getDeltaFloatPoint(particle)).mod(length).plus(length).mod(length);
	}

	private FloatPoint getDeltaFloatPoint(Particle particle) {
		double angle = particle.getAngle();

		return new FloatPoint(Math.cos(angle),Math.sin(angle)).multiply(particle.getVelocityAbs()).multiply(deltaTime);
	}

	private void fillNewAngle(Particle particle) {
		double newAngle = getNewAngle(particle);
		newAngles.put(particle, newAngle);
	}

	private double getNewAngle(Particle particle) {
		Set<Particle> neightbours = particle.getNeightbours();
		float sin = 0;
		float cos = 0;

		for (Particle neightbour : neightbours) {
			sin += Math.sin(neightbour.getAngle());
			cos += Math.cos(neightbour.getAngle());
		}
		
		sin += Math.sin(particle.getAngle());
		cos += Math.cos(particle.getAngle());
	

		return (Math.atan2(sin, cos) + (Math.random() * noise - noise / 2));
	}

	public void subscribe(CellAutomataOffLatticeObserver subscriber) {
		subscribers.add(subscriber);
	}

}
