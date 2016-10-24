package net.marcoreis.training;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveFrequentValues;

public class ClassificadorTextoWeka {
	private Instances documentos;
	private Instances dadosTreinamento;
	private Instances dadosTeste;

	public static void main(String[] args) {
		String arquivoDados = "/home/marco/Downloads/acordaos.txt";
		ClassificadorTextoWeka classificadorTextoWeka = new ClassificadorTextoWeka();
		classificadorTextoWeka.carregarDados(arquivoDados);
		classificadorTextoWeka.amostragem();
		classificadorTextoWeka.preProcessarText();
		Collection<Classifier> cls = new ArrayList<Classifier>();
		cls.add(new SMO());
		cls.add(new IBk(5));
		cls.add(new PART());
		cls.add(new J48());
		for (Classifier c : cls) {
			classificadorTextoWeka.treinar(c);
			break;
		}
	}

	private void classificar(Classifier classificador) throws Exception {
		while (dadosTeste.iterator().hasNext()) {
			Instance next = dadosTeste.iterator().next();
			double resultado = classificador.classifyInstance(next);
			System.out.println(resultado);
		}
	}

	private void amostragem() {
		try {
			documentos.randomize(new Random());
			//
			RemoveFrequentValues rfv = new RemoveFrequentValues();
			rfv.setAttributeIndex("1");
			rfv.setInputFormat(documentos);
			rfv.setModifyHeader(true);
			rfv.setNumValues(10);
			documentos = Filter.useFilter(documentos, rfv);
			//
			Resample filtro = new Resample();
			filtro.setInputFormat(documentos);
			filtro.setSampleSizePercent(5);
			documentos = Resample.useFilter(documentos, filtro);
			//
			//
			// RemovePercentage rp = new RemovePercentage();
			// rp.setInputFormat(documentos);
			// rp.setPercentage(70);
			// dadosTreinamento = Filter.useFilter(documentos, rp);
			// rp.setInvertSelection(true);
			// dadosTeste = Filter.useFilter(documentos, rp);
			//
			// Standardize s = new Standardize();
			// s.setInputFormat(documentos);
			// dadosTreinamento = Filter.useFilter(dadosTreinamento, s);
			// dadosTeste = Filter.useFilter(dadosTeste, s);
			// //
			// System.out.println(dadosTreinamento.attribute(1).numValues());
			// System.out.println(dadosTeste.attribute(1).numValues());
			// dadosTreinamento = documentos.trainCV(10, 1);
			// dadosTeste = documentos.testCV(10, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preProcessarText() {
		try {
			StringToWordVector filterString = new StringToWordVector();
			filterString.setInputFormat(documentos);
			filterString.setLowerCaseTokens(true);
			filterString.setIDFTransform(true);
			filterString.setTFTransform(true);
			filterString.setDebug(true);
			filterString.setWordsToKeep(10000);
			documentos = Filter.useFilter(documentos, filterString);
			//
			dadosTreinamento = documentos.trainCV(10, 1);
			dadosTeste = documentos.testCV(10, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void carregarDados(String arquivoDados) {
		try {
			long inicio = System.currentTimeMillis();
			CSVLoader loader = new CSVLoader();
			loader.setFile(new File(arquivoDados));
			loader.setFieldSeparator("^");
			loader.setBufferSize(2000);
			Instances data = loader.getDataSet();
			//
			NominalToString filterNT = new NominalToString();
			filterNT.setInputFormat(data);
			data = Filter.useFilter(data, filterNT);
			//
			Remove remove = new Remove();
			remove.setInputFormat(data);
			remove.setOptions(new String[] { "-R", "1,2,3" });
			documentos = Filter.useFilter(data, remove);
			documentos.setClassIndex(0);
			documentos.renameAttribute(0, "@@classe@@");
			data = null;
			System.out.println("Tempo de carregamento (s): " + (System.currentTimeMillis() - inicio) / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void treinar(Classifier classificador) {
		try {
			// Instances train = new Instances(dataset, 0, trainSize);
			// Instances test = new Instances(dataset, trainSize, testSize);
			//
			classificador.buildClassifier(dadosTreinamento);
			System.out.println(classificador.getClass());
			Evaluation eval = new Evaluation(dadosTreinamento);
			eval.crossValidateModel(classificador, dadosTreinamento, 10, new Random(1));
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			//
			testar(classificador);
			// classificar(classificador);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void testar(Classifier classificador) {
		try {
			// InputMappedClassifier imc = new InputMappedClassifier();
			Evaluation eval = new Evaluation(dadosTreinamento);
			eval.evaluateModel(classificador, dadosTeste);
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
