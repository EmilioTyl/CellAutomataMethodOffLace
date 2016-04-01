package itba.edu.ar.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.cellAutomataOffLace.CellAutomataOffLace;
import itba.edu.ar.input.file.CellIndexMethodFileGenerator;
import itba.edu.ar.output.FileOutputCellAutomataOffLace;

public class CellAutomataOffLaceTest {

	private double length;
	private int particleQuantity;
	private float radio;
	private String path;
	private int timeStep;
	private float interactionRadio;
	private double noise;
	private double deltaTime;
	private int simulationTimes;

	public CellAutomataOffLaceTest(double length, int particleQuantity, String path, float interactionRadio,
			double noise, int simulationTimes) {
		super();
		this.length = length;
		this.particleQuantity = particleQuantity;
		this.radio = 0;
		this.path = path;
		this.timeStep = 0;
		this.interactionRadio = interactionRadio;
		this.noise = noise;
		this.deltaTime = 1;
		this.simulationTimes = simulationTimes;
	}

	private int getCellquantity() {
		return (int) (length / (interactionRadio + 2 * radio)) - 1;
	}

	public void start() throws InstantiationException, IllegalAccessException, IOException {

		List<String> staticPaths = new LinkedList<String>();
		List<String> dynamicPaths = new LinkedList<String>();

		CellIndexMethodFileGenerator cmfg = new CellIndexMethodFileGenerator(length, particleQuantity, radio, path,
				timeStep,0.03);
		cmfg.generate(staticPaths, dynamicPaths);

		CellAutomataOffLace caol = new CellAutomataOffLace(getCellquantity(), staticPaths.get(0), dynamicPaths.get(0), timeStep,
				interactionRadio, radio, length, noise, deltaTime);
		caol.subscribe(new FileOutputCellAutomataOffLace(path));
		caol.simulate(simulationTimes);

	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		String path = System.getProperty("user.dir")+"/";

		(new CellAutomataOffLaceTest((float) 20, 400, path, 1, 0.5, 400)).start();
	}

}
