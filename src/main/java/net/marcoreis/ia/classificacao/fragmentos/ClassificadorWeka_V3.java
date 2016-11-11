package net.marcoreis.ia.classificacao.fragmentos;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class ClassificadorWeka_V3 {
	public static void main(String args[]) {
		// Classifier classificador = new J48();
		String arquivoDados = "/home/marco/git/java-artificial-intelligence/src/main/resources/bank-marketing-data-set/bank.csv";
		//
		new ClassificadorWeka_V3().classificar(arquivoDados);
	}

	public void classificar(String arquivoDados) {
		try {
			// Classifier cls[] = { new J48(), new PART(), new SMO(), new IBk(),
			// new NaiveBayes(), new LinearRegression(),
			// new M5P(), new LWL(), new RegressionByDiscretization() };
			CSVLoader loader = new CSVLoader();
			loader.setFile(new File(arquivoDados));
			loader.setFieldSeparator(";");
			Instances data = loader.getDataSet();
			data.setClassIndex(data.numAttributes() - 1);
			int folds = 10;
			Instances randData = new Instances(data);
			Collection<Classifier> cls = new ArrayList<Classifier>();
			cls.add(new IBk());
			int n = 0;
			for (int i = 0; n < folds; i++) {
				for (Classifier c : cls) {
					Instances trainingData = data.trainCV(3, 0);
					Instances testingData = data.testCV(3, 1);

					long inicio = System.currentTimeMillis();
					c.buildClassifier(data);
					Evaluation eval = new Evaluation(trainingData);
					eval.crossValidateModel(c, data, 10, new Random(1));
					// eval.evaluateModel(c, testingData);
					System.out.println("\n\nClassificador: " + c.getClass().getName());
					System.out.println(eval.toSummaryString("\nResultados\n======\n", false));
					System.out.println("Tempo de processamento (s): " + (System.currentTimeMillis() - inicio) / 1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
