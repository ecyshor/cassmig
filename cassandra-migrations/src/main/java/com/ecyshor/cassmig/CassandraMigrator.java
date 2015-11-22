package com.ecyshor.cassmig;

import com.datastax.driver.core.Session;
import org.joda.time.DateTimeZone;

public class CassandraMigrator {

	private Session session;

	public CassandraMigrator(Session session) {
		DateTimeZone.setDefault(DateTimeZone.UTC);
		this.session = session;
	}

	public void migrate(String migrationFilesPath) {

	}
}
