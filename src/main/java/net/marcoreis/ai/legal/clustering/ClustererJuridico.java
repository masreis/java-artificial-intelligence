package net.marcoreis.ai.legal.clustering;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.ManhattanDistance;

public class ClustererJuridico {
	private Instances documentos;

	public void cluster() throws Exception {
		SimpleKMeans model = new SimpleKMeans();
		model.setNumClusters(2);
		model.setDistanceFunction(new ManhattanDistance());
		// model.setDistanceFunction(new EuclideanDistance());
		//
		model.setSeed(10);
		model.setPreserveInstancesOrder(true);
		model.setNumClusters(2);
		model.buildClusterer(documentos);
		int[] assignments = model.getAssignments();
		int i = 0;
		for (int clusterNum : assignments) {
			System.out.printf("Instance %d -> Cluster %d\n", i,
					clusterNum);
			i++;
		}
		//
		System.out.println(model);
	}

	public void setDocumentos(Instances documentos) {
		this.documentos = documentos;
	}
}
