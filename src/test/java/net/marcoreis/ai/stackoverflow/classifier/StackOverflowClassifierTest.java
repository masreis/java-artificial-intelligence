package net.marcoreis.ai.stackoverflow.classifier;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import net.marcoreis.ai.stackoverflow.classification.StackOverflowClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

public class StackOverflowClassifierTest {

	@Test
	public void testLoadJSON() {
		StackOverflowClassifier classifier =
				new StackOverflowClassifier();
		String inputFile = System.getProperty("input.file");
		classifier.carregarDados(inputFile);
		classifier.criarDadosAmostragem(100);
		classifier.preProcessarTexto();
		classifier.gerarDadosTreinamentoETeste();
		//
		Collection<Classifier> cls = new ArrayList<Classifier>();
		// cls.add(new NaiveBayesMultinomialText());
		// cls.add(new NaiveBayes());
		cls.add(new IBk()); // knn
		// cls.add(new PART());
		// cls.add(new LinearRegression());
		// cls.add(new RandomTree()); // Precisa de muita memória
		// cls.add(new J48());
		for (Classifier c : cls) {
			Classifier modelo = classifier.treinar(c);
			classifier.avaliarModelo(modelo, true, false);
		}

	}

}
