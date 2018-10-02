package net.marcoreis.ai.entity.stackoverflow;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

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
}
