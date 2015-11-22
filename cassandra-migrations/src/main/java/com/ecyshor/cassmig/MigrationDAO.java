package com.ecyshor.cassmig;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.ecyshor.cassmig.mappings.MigrationMapper;
import com.ecyshor.cassmig.model.AppliedMigration;

import java.util.List;

public class MigrationDAO {

	private Session session;
	private String keyspace;

	public MigrationDAO(Session session, String keyspace) {
		this.session = session;
		this.keyspace = keyspace;
	}

	public List<AppliedMigration> getAppliedMigrations() {
		Select migrationQuery = QueryBuilder.select().from(keyspace, "migrations");
		List<Row> allMigrations = session.execute(migrationQuery).all();
		return MigrationMapper.map(allMigrations);
	}

}
