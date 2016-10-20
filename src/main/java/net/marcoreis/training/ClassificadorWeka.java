package net.marcoreis.training;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

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
			Collection<Classifier> cls = new ArrayList<Classifier>();
			cls.add(new IBk());
			int folds = 5;
			int n = 3;
			//
			for (int i = 0; n < folds; i++) {
				for (Classifier c : cls) {
					Instances trainingData = data.trainCV(3, 0);
					Instances testingData = data.testCV(3, 1);

					long inicio = System.currentTimeMillis();
					c.buildClassifier(trainingData);
					Evaluation eval = new Evaluation(trainingData);
					// eval.crossValidateModel(c, trainingData, 10, new
					// Random(1));
					eval.evaluateModel(c, testingData);
					System.out.println("\n\nClassificador: " + c.getClass().getName());
					System.out.println(eval.toSummaryString("\nResultados\n======\n", false));
					System.out.println("Tempo de processamento (s): " + (System.currentTimeMillis() - inicio) / 1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];

		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}

		return split;
	}

}
