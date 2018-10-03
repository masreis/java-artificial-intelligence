package net.marcoreis.ai.stackoverflow.classification;

import java.io.File;
import java.util.Random;

import org.apache.log4j.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemoveFrequentValues;

/**
 * Esta classe faz a classificação de textos utilizando o Weka
 * 
 * @author ma@marcoreis.net
 *
 */
public class StackOverflowClassifier {
	private Instances documentos;
	private Instances dadosTreinamento;
	private Instances dadosTeste;
	private static final Logger logger =
			Logger.getLogger(StackOverflowClassifier.class);

	public void classificar(Classifier classificador,
			Instance instancia) {
		try {
			double resultado =
					classificador.classifyInstance(instancia);
			logger.info("Resultado: " + resultado);
		} catch (Exception e) {
			logger.error(e);
			// e.printStackTrace();
		}
	}

	public void criarDadosAmostragem(double percentual) {
		try {
			documentos.randomize(new Random());
			//
			RemoveFrequentValues rfv =
					new RemoveFrequentValues();
			rfv.setAttributeIndex("2");
			rfv.setInputFormat(documentos);
			rfv.setModifyHeader(true);
			rfv.setNumValues(10);
			documentos = Filter.useFilter(documentos, rfv);
			//
			Resample filtro = new Resample();
			filtro.setInputFormat(documentos);
			filtro.setSampleSizePercent(percentual);
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
			// dadosTreinamento = documentos.trainCV(10, 1);
			// dadosTeste = documentos.testCV(10, 1);
			logger.info(
					"Itens na amostra: " + documentos.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preProcessarTexto() {
		try {
			StringToWordVector filterString =
					new StringToWordVector();
			filterString.setInputFormat(documentos);
			filterString.setLowerCaseTokens(true);
			filterString.setIDFTransform(true);
			filterString.setTFTransform(true);
			filterString.setDebug(true);
			filterString.setWordsToKeep(10000);
			documentos =
					Filter.useFilter(documentos, filterString);
			//
			logger.info("Texto pré-processado");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gerarDadosTreinamentoETeste() {
		dadosTreinamento = documentos.trainCV(15, 5);
		dadosTeste = documentos.testCV(15, 3);
	}

	public void carregarDados(String arquivoDados) {
		try {
			long inicio = System.currentTimeMillis();
			CSVLoader loader = new CSVLoader();
			loader.setFieldSeparator("¬");
			loader.setFile(new File(arquivoDados));
			Instances data = loader.getDataSet();
			//
			NominalToString filterNT = new NominalToString();
			filterNT.setInputFormat(data);
			data = Filter.useFilter(data, filterNT);
			//
			Remove remove = new Remove();
			// Inverte a seleção, ou seja, inclui os campos removidos
			remove.setInvertSelection(true);
			// Adiciona os campos body, title e tags
			// 12 - body
			// 17 - title
			// 18 - tags
			remove.setAttributeIndices("17,18");
			remove.setInputFormat(data);
			documentos = Filter.useFilter(data, remove);
			// Seleciona o campo tag como o classificador
			documentos.setClassIndex(1);
			data = null;
			logger.info(
					"Itens no dataset: " + documentos.size());
			logger.info("Tempo de carregamento (s): "
					+ (System.currentTimeMillis() - inicio)
							/ 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Classifier treinar(Classifier classificador) {
		try {
			classificador.buildClassifier(dadosTreinamento);
			Evaluation eval = new Evaluation(dadosTreinamento);
			eval.crossValidateModel(classificador,
					dadosTreinamento, 10, new Random(1));
			//
			logger.info(
					"\n\n------------------TREINAMENTO------------------\n\n");
			logger.info("Classificador: "
					+ classificador.getClass());
			logger.info(eval.toSummaryString());
			logger.info(eval.toClassDetailsString());
			return classificador;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void avaliarModelo(Classifier classificador,
			boolean showSummary, boolean showDetail) {
		try {
			Evaluation eval = new Evaluation(dadosTreinamento);
			eval.evaluateModel(classificador, dadosTeste);
			logger.info(
					"\n\n------------------AVALIAÇÃO------------------\n\n");
			logger.info(eval.toSummaryString());
			logger.info(eval.toClassDetailsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
