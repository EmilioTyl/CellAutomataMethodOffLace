package itba.edu.ar.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.cellAutomataOffLattice.CellAutomataOffLattice;
import itba.edu.ar.input.file.CellIndexMethodFileGenerator;
import itba.edu.ar.output.plot.Plotter;

public class CellAutomataOffLatticeOrderIndex {

	private double deltaTime;
	private int frames;

	private double fromNoise;
	private double stepNoise;
	private double toNoise;

	private List<Integer> particleQuantities;
	private CellAutomataOffLatticeOrderIndexData data;

	public CellAutomataOffLatticeOrderIndex(int frames, double fromNoise,
			double stepNoise, double toNoise, List<Integer> particleQuantities,
			CellAutomataOffLatticeOrderIndexData data) {
		super();
		this.deltaTime = 0;
		this.frames = frames;
		this.fromNoise = fromNoise;
		this.stepNoise = stepNoise;
		this.toNoise = toNoise;
		this.particleQuantities = particleQuantities;
		this.data = data;
	}

	private int getCellquantity(double length) {
		return (int) (length / (data.getInteractionRadio() + 2 * data.getRadio())) - 1;
	}

	public void start() throws InstantiationException, IllegalAccessException, IOException {

		List<String> staticPaths = new LinkedList<String>();
		List<String> dynamicPaths = new LinkedList<String>();

		Plotter plotter = data.getPlotter();

		for (Integer particleQuantity : particleQuantities) {

			plotter.setParticleQuantity(particleQuantity);

			double density = data.getDensity(particleQuantity);
			double length = data.getLength(particleQuantity);
			double interactionRadio = data.getInteractionRadio();

			CellIndexMethodFileGenerator cmfg = new CellIndexMethodFileGenerator(length, particleQuantity, data.getRadio(), data.getPath(),
					data.getTimeStep(),data.getVelocityAbs());
			cmfg.generate(staticPaths, dynamicPaths);

			for (double noise = fromNoise; noise < toNoise; noise += stepNoise) {
				CellAutomataOffLattice caol = new CellAutomataOffLattice(getCellquantity(length), staticPaths.get(0),
						dynamicPaths.get(0), data.getTimeStep(), interactionRadio, data.getRadio(), length, noise, deltaTime);
				plotter.startSimulation(noise, density);

				caol.subscribe(plotter);
				
				for(int i=0;i<data.getSimulationTimes();i++){
					caol.simulate(frames);
				}
				plotter.endSimulation(data.getSimulationTimes());

			}

			plotter.endParticleQuantityStep();

			staticPaths.clear();
			dynamicPaths.clear();

		}

		plotter.plot();
	}


}
