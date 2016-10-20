package net.marcoreis.training;

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
		try {
			String nomeArquivoTreinamento = "/home/marco/git/java-artificial-intelligence/src/main/resources/bank-marketing-data-set/bank.csv";
			CSVLoader loader = new CSVLoader();
			loader.setFile(new File(nomeArquivoTreinamento));
			loader.setFieldSeparator(";");
			Instances data = loader.getDataSet();
			// Random rand = new Random(1234);
			data.setClassIndex(data.numAttributes() - 1);
			// Instances trainingData = data.trainCV(3, 0);
			// Instances testingData = data.testCV(3, 1);
			//
			// Instances[][] split = crossValidationSplit(data, 2);
			// Instances[] trainingData = split[0];
			// Instances[] testingData = split[1];
			//
			// Classifier cls[] = { new J48(), new PART(), new SMO(), new IBk(),
			// new NaiveBayes(), new LinearRegression(),
			// new M5P(), new LWL(), new RegressionByDiscretization() };
			// Collection<Classifier> cls = new ArrayList<Classifier>();
			// cls.add(new IBk());
			int folds = 10;
			//
			// for (int i = 0; n < folds; i++) {
			// for (Classifier c : cls) {
			// Instances trainingData = data.trainCV(3, 0);
			// Instances testingData = data.testCV(3, 1);
			//
			// long inicio = System.currentTimeMillis();
			// c.buildClassifier(data);
			// Evaluation eval = new Evaluation(trainingData);
			// eval.crossValidateModel(c, data, 10, new Random(1));
			// // eval.evaluateModel(c, testingData);
			// System.out.println("\n\nClassificador: " +
			// c.getClass().getName());
			// System.out.println(eval.toSummaryString("\nResultados\n======\n",
			// false));
			// System.out.println("Tempo de processamento (s): " +
			// (System.currentTimeMillis() - inicio) / 1000);
			// }
			int seed = 4321;

			// randomize data
			Random rand = new Random(seed);
			Instances randData = new Instances(data);
			randData.randomize(rand);
			if (randData.classAttribute().isNominal())
				randData.stratify(folds);
			Evaluation eval = new Evaluation(randData);
			Classifier classificador = new IBk();
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
