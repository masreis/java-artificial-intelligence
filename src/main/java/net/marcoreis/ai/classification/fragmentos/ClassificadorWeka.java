package net.marcoreis.ai.classification.fragmentos;

import java.io.File;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class ClassificadorWeka {
	public static void main(String args[]) {
		// Classifier classificador = new J48();
		Classifier classificador = new IBk();
		String arquivoDados =
				"/home/marco/git/java-artificial-intelligence/src/main/resources/bank-marketing-data-set/bank.csv";
		//
		new ClassificadorWeka().classificar(classificador,
				arquivoDados);
	}

	public void classificar(Classifier classificador,
			String arquivoDados) {
		try {
			long inicio = System.currentTimeMillis();
			CSVLoader loader = new CSVLoader();
			loader.setFile(new File(arquivoDados));
			loader.setFieldSeparator(";");
			Instances data = loader.getDataSet();
			data.setClassIndex(data.numAttributes() - 1);
			int folds = 10;
			//
			Instances randData = new Instances(data);
			int seed = 1234;
			Random rand = new Random(seed);
			randData.randomize(rand);
			if (randData.classAttribute().isNominal()) {
				randData.stratify(folds);
			}
			//
			Evaluation eval = new Evaluation(randData);
			for (int n = 0; n < folds; n++) {
				Instances trainingData = data.trainCV(folds, n);
				Instances testingData = data.testCV(folds, n);
				//
				// System.out.println("Treinamento: " + trainingData.size());
				// System.out.println("Teste: " + testingData.size());
				Classifier copiaClassificador =
						AbstractClassifier
								.makeCopy(classificador);
				copiaClassificador.buildClassifier(trainingData);
				eval.evaluateModel(copiaClassificador,
						testingData);
			}
			//
			System.out.println("\n\nClassificador: "
					+ classificador.getClass().getName());
			System.out.println(eval.toSummaryString(
					"\nResultados\n======", false));
			System.out.println("Tempo de processamento (s): "
					+ (System.currentTimeMillis() - inicio)
							/ 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
