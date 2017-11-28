package com.smalik.gemfire.jsoncache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonCacheSessionData {

	private static Logger logger = LoggerFactory.getLogger(JsonCacheSession.class);

	private long creationTime;
	private long lastAccessedTime;
	private int maxInactiveIntervalInSeconds;

	private String id;
	private Map<String, String> types;
	private JsonNode data;
	private Map<String, Object> attributes;

	public JsonCacheSessionData() {
		this.attributes = new HashMap<>();
	}

	public JsonCacheSessionData(String id) {
		this.attributes = new HashMap<>();
		this.types = new HashMap<>();
		this.id = id;
		this.creationTime = System.currentTimeMillis();
		this.lastAccessedTime = this.creationTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getTypes() {
		return types;
	}

	public void setTypes(Map<String, String> types) {
		this.types = types;
	}

	public JsonNode getData() {
		return data;
	}

	public void setData(JsonNode data) {
		this.data = data;
	}

	@JsonIgnore
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void deserialize(ObjectMapper mapper) {
		// clear attributes from a previous deserialize op
		attributes.clear();

		// deserialize each attribute
		for (Iterator<String> fieldNames = getData().fieldNames(); fieldNames.hasNext(); ) {
			String fieldName = fieldNames.next();
			JsonNode fieldNode = getData().get(fieldName);

			try {
				Class fieldType = getFieldType(fieldName);
				attributes.put(fieldName, mapper.treeToValue(fieldNode, fieldType));

			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.warn("Unable to coerce field data: Session=" + getId() + " Field=" + fieldName + " Type=" + getTypes().get(fieldName) + " Data=" + fieldNode.toString(), e);
				} else {
					logger.warn("Unable to coerce field data: Session=" + getId() + " Field=" + fieldName + " Type=" + getTypes().get(fieldName), e);
				}
			}
		}
	}

	public void serialize(ObjectMapper mapper) {
		// clear the types object
		types.clear();

		// rebuild the data and types objects
		ObjectNode newData = mapper.createObjectNode();
		for (String fieldName: attributes.keySet()) {

			Object fieldObject = attributes.get(fieldName);
			JsonNode fieldNode = mapper.valueToTree(fieldObject);
			newData.set(fieldName, fieldNode);
			types.put(fieldName, fieldObject.getClass().getName());
		}

		this.data = newData;
	}


	private Class getFieldType(String fieldName) throws Exception {
		String className = getTypes().get(fieldName);
		return Class.forName(className);
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public int getMaxInactiveIntervalInSeconds() {
		return maxInactiveIntervalInSeconds;
	}

	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}
}
