package net.marcoreis.ai.classification;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import net.marcoreis.ai.legal.classification.ClassificadorTextoWeka;
import net.marcoreis.ai.util.UtilML;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteClassificadorTextoWeka {
	private static ClassificadorTextoWeka classificadorTextoWeka;
	private static String arquivoDados =
			System.getProperty("arquivo.entrada");

	@BeforeClass
	public static void iniciar() {
		classificadorTextoWeka = new ClassificadorTextoWeka();
	}

	@Test
	public void teste1CarregarDados() throws Exception {
		Instances instancias = new UtilML()
				.carregarInstanciasEmenta(arquivoDados);
		classificadorTextoWeka.setDocumentos(instancias);
	}

	@Test
	public void teste2Amostragem() {
		classificadorTextoWeka.criarDadosAmostragem(50);
	}

	@Test
	public void teste3PreProcessarTexto() {
		classificadorTextoWeka.preProcessarTexto();
	}

	@Test
	public void teste4TreinarETestarModelos() {
		Collection<Classifier> cls = new ArrayList<Classifier>();
		// O SMO
		cls.add(new SMO());
		// cls.add(new IBk());
		// cls.add(new PART());
		// cls.add(new J48());
		for (Classifier c : cls) {
			Classifier modelo =
					classificadorTextoWeka.treinar(c);
			classificadorTextoWeka.avaliarModelo(modelo);
			Instance instancia = classificadorTextoWeka
					.criarInstancia(criarTextoExemplo());
			// classificadorTextoWeka.classificar(modelo, instancia);
		}

	}

	private String criarTextoExemplo() {
		return System.getProperty("texto.exemplo");
	}

}
