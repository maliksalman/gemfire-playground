package com.smalik.gemfire.jsoncache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.*;

public class JsonCacheSessionTest {

	@Test
	public void testSetAndGet() {
		ObjectMapper mapper = new ObjectMapper();
		JsonCacheSession session = new JsonCacheSession(mapper);
		session.setAttribute("hundred", Integer.valueOf(100));
		assertEquals(Integer.valueOf(100), session.getAttribute("hundred"));
	}

	@Test
	public void testSetAndRemove() {
		ObjectMapper mapper = new ObjectMapper();
		JsonCacheSession session = new JsonCacheSession(mapper);
		session.setAttribute("hundred", Integer.valueOf(100));
		session.removeAttribute("hundred");
		assertNull(session.getAttribute("hundred"));
	}

	@Test
	public void testIsDirtyWhenEmpty() {
		ObjectMapper mapper = new ObjectMapper();
		JsonCacheSession session = new JsonCacheSession(mapper);
		assertFalse(session.isDirty());
	}

	@Test
	public void testSetAndIsDirty() {
		ObjectMapper mapper = new ObjectMapper();
		JsonCacheSession session = new JsonCacheSession(mapper);
		session.setAttribute("hundred", Integer.valueOf(100));
		assertTrue(session.isDirty());
	}

	@Test
	public void testSimpleSerialize() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonCacheSession session = new JsonCacheSession(mapper);
		session.setAttribute("hundred", Integer.valueOf(100));

		String expectedJson = "{ \"id\":\"" + session.getId() + "\", \"types\":{\"hundred\":\"java.lang.Integer\"}, \"data\":{\"hundred\":100}}";
		JSONAssert.assertEquals(expectedJson, session.getJsonData(), false);
	}
}