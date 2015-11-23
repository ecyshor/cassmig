package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.ecyshor.cassmig.exception.InvalidMigrationsException;
import com.ecyshor.cassmig.exception.MissingRequiredConfiguration;
import com.ecyshor.cassmig.model.MigrationFile;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class MigrationFileTransformerTest extends MigrationFileTransformer {

	@Test
	public void shouldReturnInitializationFile() throws URISyntaxException {
		URL resource = this.getClass().getClassLoader().getResource("migrations/test_init_file.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		MigrationFile initFile = transformMigrationFileToMigration(file);
		assertThat(initFile.getOrder(), equalTo(-1));
		assertThat(initFile.getCommands(), hasSize(2));
		String initCommandsReceived = initFile.getCommands().get(0);
		String schemaForMigrationTable = initFile.getCommands().get(1);
		assertThat(initCommandsReceived, equalTo("    CREATE KEYSPACE test_keyspace;"));
		assertThat(schemaForMigrationTable, equalTo("CREATE TABLE test_keyspace.migrations(  migration_schema varchar,  migration_order bigint,  time_executed timestamp,  md5sum text,  PRIMARY KEY (migration_schema, time_executed, migration_order));"));
	}

	@Test
	public void shouldReturnNormalMigrationFile() throws URISyntaxException {
		URL resource = this.getClass().getClassLoader().getResource("migrations/example_migration_file.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		MigrationFile migrationFile = transformMigrationFileToMigration(file);
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
		URL resource = this.getClass().getClassLoader().getResource("migrations/file_without_order.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		try {
			transformMigrationFileToMigration(file);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingValueForConfigurationInFile() throws Throwable {
		URL resource = this.getClass().getClassLoader().getResource("migrations/file_with_empty_order.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		try {
			transformMigrationFileToMigration(file);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingMigrationStart() throws Throwable {
		URL resource = this.getClass().getClassLoader().getResource("migrations/file_without_start_migration.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		try {
			transformMigrationFileToMigration(file);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = MissingRequiredConfiguration.class)
	public void shouldThrowExceptionWhenMissingMigrationEnd() throws Throwable {
		URL resource = this.getClass().getClassLoader().getResource("migrations/file_without_end_migration.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		try {
			transformMigrationFileToMigration(file);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Test(expected = InvalidDataException.class)
	public void shouldThrowExceptionWhenOrderHasNegativeValue() throws Throwable {
		URL resource = this.getClass().getClassLoader().getResource("migrations/file_with_negative_order.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		try {
			transformMigrationFileToMigration(file);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

}
