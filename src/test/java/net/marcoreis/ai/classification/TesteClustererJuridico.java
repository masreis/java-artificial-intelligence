package net.marcoreis.ai.classification;

import org.junit.BeforeClass;
import org.junit.Test;

import net.marcoreis.ai.legal.clustering.ClustererJuridico;
import net.marcoreis.ai.util.UtilML;
import weka.core.Instances;

public class TesteClustererJuridico {

	private static ClustererJuridico clustererJuridico;
	private static String arquivoDados = "/home/marco/Downloads/vef/";

	@BeforeClass
	public static void setUp() throws Exception {
		Instances instancias = new UtilML()
				.carregarInstanciasVEF(arquivoDados);
		clustererJuridico.setDocumentos(instancias);
	}

	@Test
	public void testCluster() throws Exception {
		clustererJuridico.cluster();
	}
}
