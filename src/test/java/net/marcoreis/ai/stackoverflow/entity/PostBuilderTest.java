package net.marcoreis.ai.stackoverflow.entity;

import org.junit.Test;

public class PostBuilderTest {

	// @Test
	public void testMap() {
		PostBuilder builder = new PostBuilder();
		builder.bind();
	}

	// @Test
	public void testConvertToJSON() {
		PostBuilder builder = new PostBuilder();
		builder.convertToJSON(10);
	}
	
	@Test
	public void testConvertToCSV() {
		PostBuilder builder = new PostBuilder();
		builder.convertToCSV();
	}

}
