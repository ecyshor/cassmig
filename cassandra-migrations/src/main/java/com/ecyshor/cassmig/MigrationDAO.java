package com.ecyshor.cassmig;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.ecyshor.cassmig.mappings.MigrationMapper;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationFile;
import com.google.common.collect.Lists;

import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class MigrationDAO {

	public static final String MIGRATIONS_TABLE = "migrations";

	private Session session;

	public MigrationDAO(Session session) {
		this.session = session;
	}

	public List<AppliedMigration> getAppliedMigrations(String keyspace, String schema) {
		KeyspaceMetadata keyspaceMetadata = session.getCluster().getMetadata().getKeyspace(keyspace);
		if (keyspaceMetadata != null) {
			if (keyspaceMetadata.getTable(MIGRATIONS_TABLE) != null) {
				Select.Where migrationQuery = QueryBuilder.select().from(keyspace, MIGRATIONS_TABLE).where(eq("migration_schema", schema));
				List<Row> allMigrations = session.execute(migrationQuery).all();
				return MigrationMapper.map(allMigrations);
			}
		}
		return Lists.newArrayList();
	}

	public void applyMigration(MigrationFile migrationFile) {
		List<String> commands = migrationFile.getCommands();
		for (String command : commands) {
			session.execute(command);
		}
	}

	public void saveMigrationAsApplied(AppliedMigration appliedMigration, String keyspace) {
		Insert migrationQuery = QueryBuilder.insertInto(keyspace, MIGRATIONS_TABLE).value("migration_schema", appliedMigration.getSchema())
				.value("migration_order", appliedMigration.getOrder()).value("time_executed", appliedMigration.getTimeExecutedAsJavaDate())
				.value("md5sum", appliedMigration.getMd5Sum());
		session.execute(migrationQuery);
	}

}
