package net.marcoreis.ia.classificacao;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import net.marcoreis.ia.classificacao.ClassificadorTextoWeka;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.core.Instance;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteClassificadorTextoWeka {
	private static ClassificadorTextoWeka classificadorTextoWeka;
	private static String arquivoDados = "/home/marco/Downloads/acordaos.txt";

	@BeforeClass
	public static void iniciar() {
		arquivoDados = "/home/marco/Dropbox/mestrado/ppca/mineracao-de-dados-e-textos/acordaos-part.txt";
		classificadorTextoWeka = new ClassificadorTextoWeka();
	}

	@Test
	public void teste1CarregarDados() {
		classificadorTextoWeka.carregarDados(arquivoDados);
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
			Classifier modelo = classificadorTextoWeka.treinar(c);
			classificadorTextoWeka.avaliarModelo(modelo);
			Instance instancia = classificadorTextoWeka.criarInstancia(criarTextoExemplo());
//			classificadorTextoWeka.classificar(modelo, instancia);
		}

	}

	private String criarTextoExemplo() {
		return "CONSTITUCIONAL E ESTATUTO DA CRIANCA E DO ADOLESCENTE. INOVACAO RECURSAL. NAO CONHECIMENTO. ATENDIMENTO ESPECIAL A ALUNO PORTADOR DE ATRASO MENTAL E EPILEPSIA. DESIGNACAO DE MONITOR.  NAO SE CONHECE DA PARTE DO RECURSO QUE NAO FOI ANALISADA NO PRIMEIRO GRAU DE JURISDICAO, SOB PENA DE SUPRESSAO DE INSTANCIA E AFRONTA AO PRINCIPIO DO DUPLO GRAU DE JURISDICAO.  TRATANDO-SE DE ALUNO PORTADOR DE ATRASO MENTAL E EPILEPSIA, COM NOTORIAS DIFICULDADES DEVIDO A DEFICIT INTELECTUAL, BEM COMO TRANSTORNOS ESPECIFICOS DO DESENVOLVIMENTO DA FALA E DA LINGUAGEM, DEVE A REDE PUBLICA DE ENSINO FORNECER ATENDIMENTO EDUCACIONAL ESPECIALIZADO, DESTINADO A SUPRIR AS PECULIARIDADES QUE ENVOLVEM O CASO.  INEXISTINDO NORMA QUE OBRIGUE O ENTE ESTATAL A DESIGNAR  MONITOR EXCLUSIVO A ALUNO EXCEPCIONAL MATRICULADO EM ENSINO REGULAR, CABE AO JUDICIARIO PROCEDER AO EXAME DA HIPOTESE MEDIANTE INTERPRETACAO SISTEMATICA DO ORDENAMENTO JURIDICO PERTINENTE, A LUZ DOS DIREITOS E GARANTIAS CONSTITUCIONALMENTE PREVISTOS.  A FALTA OU INSUFICIENCIA DE MONITORES EM ESCOLAS PUBLICAS DO DISTRITO FEDERAL ACARRETA OFENSA AOS DIREITOS DA CRIANCA ESPECIAL QUE NECESSITA DE EDUCACAO COM ATENDIMENTO ESPECIALIZADO. O PRINCIPIO DA SEPARACAO DOS PODERES NAO PODE SER UTILIZADO PARA IMPEDIR O PODER JUDICIARIO DE DECIDIR ANTE A INERCIA GOVERNAMENTAL.  RECURSO CONHECIDO E IMPROVIDO. ";
	}

}
