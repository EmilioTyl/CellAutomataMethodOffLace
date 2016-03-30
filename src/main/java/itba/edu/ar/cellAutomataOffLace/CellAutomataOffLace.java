package itba.edu.ar.cellAutomataOffLace;

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

public class CellAutomataOffLace {

	private List<Particle> particles;
	private int cellQuantity;
	private String staticPath;
	private String dynamicPath;
	private int timeStep = 0;
	private float interactionRadio;
	private float radio;
	private double length;
	private float noise;
	private Map<Particle, Double> newAngles = new HashMap<Particle, Double>();
	private double deltaTime;
	private List<CellAutomataOffLaceObserver> subscribers = new LinkedList<CellAutomataOffLaceObserver>();

	public CellAutomataOffLace(int cellQuantity, String staticPath, String dynamicPath, int timeStep,
			float interactionRadio, float radio, double length, float noise, double deltaTime) {
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

	public void simulate(int times) throws InstantiationException, IllegalAccessException, IOException {

		IndexMatrix indexMatrix = IndexMatrixBuilder.getIndexMatrix(staticPath, dynamicPath, cellQuantity, timeStep);
		particles = indexMatrix.getParticles();

		CellIndexMethod cellIndexMethod = new CellIndexMethod(indexMatrix,
				new OptimizedRoute(cellQuantity, true, length), interactionRadio, radio);

		notifyInitialState();
		
		for (int i = 0; i < times; i++) {

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
		for (CellAutomataOffLaceObserver subscriber : subscribers) {
			subscriber.endSimulation();
		}
	}

	private void notifyInitialState() {
		for (CellAutomataOffLaceObserver subscriber : subscribers) {
			subscriber.initialState(particles, timeStep);
		}		
	}

	private void notifyFinishedStep() {
		for (CellAutomataOffLaceObserver subscriber : subscribers) {
			subscriber.finishedStep(particles, timeStep);
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

		return new FloatPoint(Math.sin(angle), Math.cos(angle)).multiply(particle.getVelocityAbs()).multiply(deltaTime);
	}

	private void fillNewAngle(Particle particle) {
		double newAngle = getNewAngle(particle.getNeightbours());
		newAngles.put(particle, newAngle);
	}

	private double getNewAngle(Set<Particle> neightbours) {
		float sin = 0;
		float cos = 0;

		for (Particle neightbour : neightbours) {
			sin += Math.sin(neightbour.getAngle());
			cos += Math.cos(neightbour.getAngle());
		}

		return (Math.atan(sin / cos) + (Math.random() * noise - noise / 2));
	}

	public void subscribe(CellAutomataOffLaceObserver subscriber) {
		subscribers.add(subscriber);
	}

}
