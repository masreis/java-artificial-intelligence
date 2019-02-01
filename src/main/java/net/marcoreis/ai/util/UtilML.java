package net.marcoreis.ai.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ListIterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;

public class UtilML {
	private static final Logger logger =
			Logger.getLogger(UtilML.class);
	private static final int QUANTIDADE_PALAVRAS = 2000;

	public void salvaModelo(String caminhoModelo, Object modelo)
			throws Exception {
		weka.core.SerializationHelper.write(caminhoModelo,
				modelo);
	}

	public Object carregaModelo(String caminhoModelo)
			throws Exception {
		return weka.core.SerializationHelper.read(caminhoModelo);
	}

	public Instances carregarInstanciasEmenta(
			String arquivoDados) throws Exception {
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
		Instances documentos = Filter.useFilter(data, remove);
		documentos.setClassIndex(0);
		documentos.renameAttribute(0, "@@classe@@");
		data = null;
		logger.info("Quantidade de itens carregados: "
				+ documentos.size());
		logger.info("Tempo de carregamento (s): "
				+ (System.currentTimeMillis() - inicio) / 1000);
		return documentos;
	}

	public Instances carregarInstanciasVEF(String diretorioDados)
			throws Exception {
		long inicio = System.currentTimeMillis();
		Instances data = null;
		File diretorioVEF = new File(diretorioDados);
		for (File file : diretorioVEF.listFiles()) {
			CSVLoader loader = new CSVLoader();
			String text = recuperaTextoPuro(file);
			loader.setSource(
					new ByteArrayInputStream(text.getBytes()));
			loader.setBufferSize(QUANTIDADE_PALAVRAS);
			if (data == null) {
				data = loader.getDataSet();
			} else {
				Instances dataSet = loader.getDataSet();
				ListIterator<Instance> listIterator =
						dataSet.listIterator();
				while (listIterator.hasNext()) {
					Instance next =
							dataSet.listIterator().next();
					data.add(next);
				}
			}
		}
		//
		NominalToString filterNT = new NominalToString();
		filterNT.setInputFormat(data);
		data = Filter.useFilter(data, filterNT);
		//
		data.renameAttribute(0, "@@texto@@");
		logger.info("Quantidade de itens carregados: "
				+ data.size());
		logger.info("Tempo de carregamento (s): "
				+ (System.currentTimeMillis() - inicio) / 1000);
		return data;
	}

	private String recuperaTextoPuro(File file)
			throws IOException {
		String text = FileUtils.readFileToString(file);
		text = Normalizer.normalize(text, Normalizer.Form.NFD);
		// Pattern pattern = Pattern
		// .compile("\\p{InCombiningDiacriticalMarks}+");
		// text = text.replaceAll("[^\\p{Alpha}]", " ");
		text = text.replaceAll("[\\|\"|\\/|(|)|'|*|”|,|-]", " ");
		// text = text.replaceAll("[^\\p{ASCII}]", " ");
		text = StringUtils.normalizeSpace(text);
		text = StringUtils.stripAccents(text);
		text = "@@texto@@" + "\n" + text.toLowerCase();
		return text;
	}

}
