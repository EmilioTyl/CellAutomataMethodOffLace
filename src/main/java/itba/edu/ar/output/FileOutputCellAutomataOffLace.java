package itba.edu.ar.output;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import itba.edu.ar.cellAutomataOffLace.CellAutomataOffLaceObserver;
import itba.edu.ar.cellIndexMethod.data.particle.Particle;

public class FileOutputCellAutomataOffLace implements CellAutomataOffLaceObserver {

	private static final Object SEPARATOR = " ";
	private String pathFolder;
	private List<String> fileContent = new LinkedList<String>();

	public FileOutputCellAutomataOffLace(String path) {
		this.pathFolder = path;
	}

	public void finishedStep(List<Particle> particles, int timeStep) {

		StringBuilder sb = new StringBuilder();
		fileContent.add(particles.size() + "");
		fileContent.add(timeStep + "");
		for (Particle particle : particles) {
			sb.append(particle.getId()).append(SEPARATOR).append(particle.getPosition().getX()).append(SEPARATOR)
					.append(particle.getPosition().getY());
			fileContent.add(sb.toString());
			sb = new StringBuilder();
		}
	}

	private String getFileName() {
		return "cellAutomataOffLace";
	}

	public void initialState(List<Particle> particles, int timeStep) {
		finishedStep(particles, timeStep);
	}

	public void endSimulation() {
		try {
			Files.write(Paths.get(pathFolder + getFileName()), fileContent, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new IllegalAccessError();
		}

	}

}
