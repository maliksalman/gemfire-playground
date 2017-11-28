package com.smalik.gemfire.jsoncache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemstone.gemfire.cache.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.SessionRepository;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonCacheSessionRepository implements SessionRepository<JsonCacheSession> {

	private static Logger LOGGER = LoggerFactory.getLogger(JsonCacheSessionRepository.class);

	private Region<String, String> region;
	private ObjectMapper mapper;

	public JsonCacheSessionRepository(Region region, ObjectMapper mapper) {
		this.region = region;
		this.mapper = mapper;
	}

	@Override
	public JsonCacheSession createSession() {
		JsonCacheSession session = new JsonCacheSession(mapper);
		LOGGER.trace("Created session: Id=" + session.getId());
		return session;
	}

	@Override
	public void save(JsonCacheSession session) {
		LOGGER.trace("Saving session: Id=" + session.getId() + ", Dirty=" + session.isDirty());
		if (session.isDirty()) {
			region.put(session.getId(), session.getJsonData());
			session.resetDirty();
		}
	}

	@Override
	public JsonCacheSession getSession(String id) {
		LOGGER.trace("Getting session: Id=" + id);
		String jsonData = region.get(id);
		if (!StringUtils.isEmpty(jsonData)) {
			try {
				return new JsonCacheSession(mapper, id, jsonData);
			} catch(IOException e) {
				LOGGER.warn("Failed to retrieve session for Id=" + id, e);
			}
		}

		return null;
	}

	@Override
	public void delete(String id) {
		LOGGER.trace("Deleting session: Id=" + id);
		region.remove(id);
	}
}
