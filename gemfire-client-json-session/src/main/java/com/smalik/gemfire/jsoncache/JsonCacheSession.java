package com.smalik.gemfire.jsoncache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.session.ExpiringSession;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JsonCacheSession implements ExpiringSession {

	private JsonCacheSessionData data;
	private boolean dirty;
	private ObjectMapper mapper;

	public JsonCacheSession(ObjectMapper mapper, String id, String jsonData) throws IOException {
		this.mapper = mapper;
		data = mapper.readValue(jsonData, JsonCacheSessionData.class);
		data.deserialize(mapper);
	}

	public JsonCacheSession(ObjectMapper mapper) {
		this.mapper = mapper;
		data = new JsonCacheSessionData(UUID.randomUUID().toString());
	}

	@Override
	public String getId() {
		return data.getId();
	}

	@Override
	public <T> T getAttribute(String attributeName) {
		return (T) data.getAttributes().get(attributeName);
	}

	@Override
	public Set<String> getAttributeNames() {
		return data.getAttributes().keySet();
	}

	@Override
	public void setAttribute(String attributeName, Object attributeValue) {
		data.getAttributes().put(attributeName, attributeValue);
		dirty = true;
	}

	@Override
	public void removeAttribute(String attributeName) {
		data.getAttributes().remove(attributeName);
		dirty = true;
	}

	public String getJsonData() {
		data.serialize(mapper);
		try {
			return mapper.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	public void resetDirty() {
		this.dirty = false;
	}

	@Override
	public long getCreationTime() {
		return data.getCreationTime();
	}

	@Override
	public void setLastAccessedTime(long lastAccessedTime) {
		data.setLastAccessedTime(lastAccessedTime);
		dirty = true;
	}

	@Override
	public long getLastAccessedTime() {
		return data.getLastAccessedTime();
	}

	@Override
	public void setMaxInactiveIntervalInSeconds(int interval) {
		data.setMaxInactiveIntervalInSeconds(interval);
		dirty = true;
	}

	@Override
	public int getMaxInactiveIntervalInSeconds() {
		return data.getMaxInactiveIntervalInSeconds();
	}

	@Override
	public boolean isExpired() {
		long maxInactiveIntervalInSeconds = getMaxInactiveIntervalInSeconds();
		long idleTimeMillis = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(maxInactiveIntervalInSeconds);

		return maxInactiveIntervalInSeconds >= 0
				&& (idleTimeMillis >= getLastAccessedTime());
	}
}
