package com.ecyshor.cassmig;

import com.datastax.driver.core.Session;

public class CassandraMigrator {

	private Session session;

	public CassandraMigrator(Session session) {
		this.session = session;
	}

	public void migrate(String migrationFilesPath) {

	}
}
