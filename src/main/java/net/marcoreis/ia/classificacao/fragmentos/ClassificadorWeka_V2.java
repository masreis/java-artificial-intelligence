package net.marcoreis.ia.classificacao.fragmentos;

import java.io.File;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class ClassificadorWeka_V2 {
	public static void main(String args[]) {
		// Classifier classificador = new J48();
		Classifier classificador = new IBk();
		String arquivoDados = "/home/marco/git/java-artificial-intelligence/src/main/resources/bank-marketing-data-set/bank.csv";
		//
		new ClassificadorWeka_V2().classificar(classificador, arquivoDados);
	}

	public void classificar(Classifier classificador, String arquivoDados) {
		try {
			CSVLoader loader = new CSVLoader();
			loader.setFile(new File(arquivoDados));
			loader.setFieldSeparator(";");
			Instances data = loader.getDataSet();
			data.setClassIndex(data.numAttributes() - 1);
			int folds = 10;
			Instances randData = new Instances(data);
			int seed = 1234;
			Random rand = new Random(seed);
			randData.randomize(rand);
			if (randData.classAttribute().isNominal()) {
				randData.stratify(folds);
			}
			if (randData.classAttribute().isNominal())
				randData.stratify(folds);
			Evaluation eval = new Evaluation(randData);
			for (int n = 0; n < folds; n++) {
				Instances train = randData.trainCV(folds, n);
				Instances test = randData.testCV(folds, n);
				Classifier clsCopy = AbstractClassifier.makeCopy(classificador);
				clsCopy.buildClassifier(train);
				eval.evaluateModel(clsCopy, test);
			}
			System.out.println("Classificador: " + classificador.getClass().getCanonicalName());
			System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
			System.out.println(eval.toMatrixString());

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
