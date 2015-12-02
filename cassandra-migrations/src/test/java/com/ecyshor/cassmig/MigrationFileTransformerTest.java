package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.ecyshor.cassmig.exception.MissingRequiredConfiguration;
import com.ecyshor.cassmig.model.MigrationFile;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class MigrationFileTransformerTest extends MigrationFileTransformer {

	@Test
	public void shouldReturnInitializationFile() throws URISyntaxException {
		InputStream resource = getResourceAsStream("migrations/test_init_file.cql");
		MigrationFile initFile = transformMigrationFileToMigration(resource);
		assertThat(initFile.getOrder(), equalTo(-100));
		assertThat(initFile.getCommands(), hasSize(2));
		String initCommandsReceived = initFile.getCommands().get(0);
		String schemaForMigrationTable = initFile.getCommands().get(1);
		assertThat(initCommandsReceived, equalTo("    CREATE KEYSPACE test_keyspace;"));
		assertThat(schemaForMigrationTable, equalTo("CREATE TABLE test_keyspace.migrations(  migration_schema varchar,  migration_order bigint,  time_executed timestamp,  md5sum text,  PRIMARY KEY (migration_schema, time_executed, migration_order));"));
	}

	@Test
	public void shouldReturnNormalMigrationFile() throws URISyntaxException {
		InputStream resource = getResourceAsStream("migrations/example_migration_file.cql");
		MigrationFile migrationFile = transformMigrationFileToMigration(resource);
		assertThat(migrationFile.getOrder(), equalTo(1));
		assertThat(migrationFile.getCommands(), hasSize(2));
		assertThat(migrationFile.getDescription(), equalTo("Description"));
		String keyspaceCommand = migrationFile.getCommands().get(0);
		String migration = migrationFile.getCommands().get(1);
		assertThat(keyspaceCommand, equalTo("USE test;"));
		assertThat(migration, equalTo("migrate this is a test;"));
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingConfigurationInFile() throws Throwable {
		InputStream resource = getResourceAsStream("migrations/file_without_order.cql");
		try {
			transformMigrationFileToMigration(resource);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingValueForConfigurationInFile() throws Throwable {
		InputStream resource = getResourceAsStream("migrations/file_with_empty_order.cql");
		try {
			transformMigrationFileToMigration(resource);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingMigrationStart() throws Throwable {
		InputStream resource = getResourceAsStream("migrations/file_without_start_migration.cql");
		assert resource != null;
		try {
			transformMigrationFileToMigration(resource);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingMigrationEnd() throws Throwable {
		InputStream resource = getResourceAsStream("migrations/file_without_end_migration.cql");
		try {
			transformMigrationFileToMigration(resource);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = InvalidDataException.class)
	public void shouldThrowExceptionWhenOrderHasNegativeValue() throws Throwable {
		InputStream resource = getResourceAsStream("migrations/file_with_negative_order.cql");
		try {
			transformMigrationFileToMigration(resource);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	private InputStream getResourceAsStream(String filePath) {
		return this.getClass().getClassLoader().getResourceAsStream(filePath);
	}

}
