package net.marcoreis.ai.stackoverflow.entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class PostBuilder {

	public void bind() {
		ObjectMapper mapper = new XmlMapper();
		mapper.configure(
				MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
				true);
		String inputFile = System.getProperty("input.file");
		try {
			InputStream is = new FileInputStream(inputFile);
			List<Post> posts = mapper.readValue(is,
					new TypeReference<List<Post>>() {
					});
			for (Post post : posts) {
				System.out.println(post.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void convertToJSON() {
		ObjectMapper mapper = new XmlMapper();
		ObjectMapper mapperOutput = new ObjectMapper();
		mapper.configure(
				MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
				true);
		String inputFile = System.getProperty("input.file");
		try {
			InputStream is = new FileInputStream(inputFile);
			List<Post> posts = mapper.readValue(is,
					new TypeReference<List<Post>>() {
					});
			String outputFile =
					System.getProperty("output.file");
			mapperOutput.writerWithDefaultPrettyPrinter()
					.writeValue(new File(outputFile), posts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void convertToJSON(int top) {
		ObjectMapper mapper = new XmlMapper();
		ObjectMapper mapperOutput = new ObjectMapper();
		mapper.configure(
				MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
				true);
		String inputFile = System.getProperty("input.file");
		try {
			InputStream is = new FileInputStream(inputFile);
			List<Post> posts = mapper.readValue(is,
					new TypeReference<List<Post>>() {
					});
			List<Post> postsTop = posts.subList(0, top - 1);
			String outputFile =
					System.getProperty("output.file");
			mapperOutput.writerWithDefaultPrettyPrinter()
					.writeValue(new File(outputFile), postsTop);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * O delimitador é ¬
	 * 
	 */
	public void convertToCSV() {
		ObjectMapper mapper = new XmlMapper();
		mapper.configure(
				MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
				true);
		String inputFile = System.getProperty("input.file");
		try {
			InputStream is = new FileInputStream(inputFile);
			List<Post> posts = mapper.readValue(is,
					new TypeReference<List<Post>>() {
					});
			String outputFile =
					System.getProperty("output.file");
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outputFile));
			bw.write("id¬" + "parentId¬" + "ownerDisplayName¬"
					+ "closedDate¬" + "communityOwnedDate¬"
					+ "lastEditorDisplayName¬" + "postTypeId¬"
					+ "acceptedAnswerId¬" + "creationDate¬"
					+ "score¬" + "viewCount¬" + "body¬"
					+ "ownerUserId¬" + "lastEditorUserId¬"
					+ "lastEditDate¬" + "lastActivityDate¬"
					+ "title¬" + "tags¬" + "answerCount¬"
					+ "commentCount¬" + "favoriteCount\n");
			String SEPARADOR = "¬";
			StringBuilder sb = new StringBuilder();
			for (Post post : posts) {
				String tag = analyseTags(post.getTags());
				if (tag == null) {
					continue;
				}
				sb.append(post.getId()).append(SEPARADOR)
						.append(post.getParentId())
						.append(SEPARADOR)
						.append(checkString(
								post.getOwnerDisplayName()))
						.append(SEPARADOR)
						.append(post.getClosedDate())
						.append(SEPARADOR)
						.append(checkString(
								post.getCommunityOwnedDate()))
						.append(SEPARADOR)
						.append(checkString(
								post.getLastEditorDisplayName()))
						.append(SEPARADOR)
						.append(post.getPostTypeId())
						.append(SEPARADOR)
						.append(post.getAcceptedAnswerId())
						.append(SEPARADOR)
						.append(post.getCreationDate())
						.append(SEPARADOR)
						.append(post.getScore())
						.append(SEPARADOR)
						.append(post.getViewCount())
						.append(SEPARADOR)
						.append(checkString(post.getBody()))
						.append(SEPARADOR)
						.append(post.getOwnerUserId())
						.append(SEPARADOR)
						.append(post.getLastEditorUserId())
						.append(SEPARADOR)
						.append(post.getLastEditDate())
						.append(SEPARADOR)
						.append(post.getLastActivityDate())
						.append(SEPARADOR)
						.append(checkString(post.getTitle()))
						.append(SEPARADOR).append(tag)
						.append(SEPARADOR)
						.append(post.getAnswerCount())
						.append(SEPARADOR)
						.append(post.getCommentCount())
						.append(SEPARADOR)
						.append(post.getFavoriteCount())
						.append("\n");
				String line =
						sb.toString().replaceAll("¬null", "¬");
				bw.write(line);
				sb.setLength(0);
			}
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Inicialmente, para testar, vamos selecionar apenas a primeira tag
	 * 
	 */

	private String analyseTags(String tags) {
		if (tags == null) {
			return null;
		}
		String[] arrayTags = tags.split("><");
		return arrayTags[0].replaceAll("<", "").replaceAll(">",
				"");
	}

	private String checkString(String value) {
		if (value == null) {
			return null;
		}
		String ret = "\"" + value.replaceAll("¬", "")
				.replaceAll("\n", " ").replaceAll("\"", " ")
				.replaceAll("\\?", " ").replaceAll("\\*", " ")
				.replaceAll("\r", " ").replaceAll("\\|", " ")
				.replaceAll("\\\\", "").replaceAll("'", " ")
				+ "\"";
		return ret;
	}

	public void convertToInstance() {
		ObjectMapper mapper = new XmlMapper();
		mapper.configure(
				MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
				true);
		String inputFile = System.getProperty("input.file");
		try {
			InputStream is = new FileInputStream(inputFile);
			List<Post> posts = mapper.readValue(is,
					new TypeReference<List<Post>>() {
					});
			String outputFile =
					System.getProperty("output.file");
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(outputFile));
			Vector<String> attributes = new Vector<String>();
			attributes.add("id");
			attributes.add("parentId");
			attributes.add("ownerDisplayName");
			attributes.add("closedDate");
			attributes.add("communityOwnedDate");
			attributes.add("lastEditorDisplayName");
			attributes.add("postTypeId");
			attributes.add("acceptedAnswerId");
			attributes.add("creationDate");
			attributes.add("score");
			attributes.add("viewCount");
			attributes.add("body");
			attributes.add("ownerUserId");
			attributes.add("lastEditorUserId");
			attributes.add("lastEditDate");
			attributes.add("lastActivityDate");
			attributes.add("title");
			attributes.add("tags");
			attributes.add("answerCount");
			attributes.add("commentCount");
			attributes.add("favoriteCount");
			String SEPARADOR = "¬";
			StringBuilder sb = new StringBuilder();
			for (Post post : posts) {
				sb.append(post.getId()).append(SEPARADOR)
						.append(post.getParentId())
						.append(SEPARADOR)
						.append(checkString(
								post.getOwnerDisplayName()))
						.append(SEPARADOR)
						.append(post.getClosedDate())
						.append(SEPARADOR)
						.append(post.getCommunityOwnedDate())
						.append(SEPARADOR)
						.append(post.getLastEditorDisplayName())
						.append(SEPARADOR)
						.append(post.getPostTypeId())
						.append(SEPARADOR)
						.append(post.getAcceptedAnswerId())
						.append(SEPARADOR)
						.append(post.getCreationDate())
						.append(SEPARADOR)
						.append(post.getScore())
						.append(SEPARADOR)
						.append(post.getViewCount())
						.append(SEPARADOR).append(post.getBody())
						.append(SEPARADOR)
						.append(post.getOwnerUserId())
						.append(SEPARADOR)
						.append(post.getLastEditorUserId())
						.append(SEPARADOR)
						.append(post.getLastEditDate())
						.append(SEPARADOR)
						.append(post.getLastActivityDate())
						.append(SEPARADOR)
						.append(post.getTitle())
						.append(SEPARADOR).append(post.getTags())
						.append(SEPARADOR)
						.append(post.getAnswerCount())
						.append(SEPARADOR)
						.append(post.getCommentCount())
						.append(SEPARADOR)
						.append(post.getFavoriteCount())
						.append("\n");
				bw.write(sb.toString());
				sb.setLength(0);
			}
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
