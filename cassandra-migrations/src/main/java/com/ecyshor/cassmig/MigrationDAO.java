package com.ecyshor.cassmig;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.ecyshor.cassmig.mappings.MigrationMapper;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationFile;

import java.util.List;

public class MigrationDAO {

	public static final String MIGRATIONS_TABLE = "migrations";

	private Session session;

	public MigrationDAO(Session session) {
		this.session = session;
	}

	public List<AppliedMigration> getAppliedMigrations(String keyspace) {
		Select migrationQuery = QueryBuilder.select().from(keyspace, MIGRATIONS_TABLE);
		List<Row> allMigrations = session.execute(migrationQuery).all();
		return MigrationMapper.map(allMigrations);
	}

	public void applyMigration(MigrationFile migrationFile) {
		List<String> commands = migrationFile.getCommands();
		for (String command : commands) {
			session.execute(command);
		}
	}

	public void saveMigrationAsApplied(AppliedMigration appliedMigration, String keyspace) {
		QueryBuilder.insertInto(keyspace, MIGRATIONS_TABLE).value("schema", appliedMigration.getSchema())
				.value("order", appliedMigration.getOrder()).value("time_executed", appliedMigration.getTimeExecutedAsJavaDate())
				.value("md5sum", appliedMigration.getMd5Sum());
	}

}
