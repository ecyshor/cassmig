package com.ecyshor.cassmig;

import com.datastax.driver.core.Session;
import com.ecyshor.cassmig.exception.InvalidMigrationsException;
import com.ecyshor.cassmig.files.JarFileLoader;
import com.ecyshor.cassmig.files.ModuleFileLoader;
import com.ecyshor.cassmig.migration.MigrationExtractor;
import com.ecyshor.cassmig.migration.MigrationService;
import com.ecyshor.cassmig.model.ExternalMigrationConfig;
import com.ecyshor.cassmig.model.MigrationFile;
import com.ecyshor.cassmig.validation.MigrationValidator;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class CassandraMigrator {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraMigrator.class);

	private MigrationExtractor migrationExtractor;
	private MigrationService migrationService;

	public CassandraMigrator(Session session) {
		this(new MigrationExtractor(new MigrationFileTransformer(), new ModuleFileLoader(), new JarFileLoader()), new MigrationService(new MigrationValidator(), new MigrationDAO(session)));
	}

	CassandraMigrator(MigrationExtractor migrationExtractor, MigrationService migrationService) {
		DateTimeZone.setDefault(DateTimeZone.UTC);
		this.migrationExtractor = migrationExtractor;
		this.migrationService = migrationService;
	}

	/***
	 * @throws InvalidMigrationsException Something happend trying to migrate
	 */
	public void migrate(String migrationFilesPath) {
		try {
			LOGGER.info("Migrating internal migration for path {}", migrationFilesPath);
			List<MigrationFile> migrationFiles = migrationExtractor.getMigrationFiles(migrationFilesPath);
			migrationService.applyMigrations(migrationFiles);
		} catch (IOException e) {
			LOGGER.error("Exception while migrating schema.", e);
			throw new InvalidMigrationsException(e);
		} catch (URISyntaxException e) {
			LOGGER.error("Exception while migrating schema.", e);
			throw new InvalidMigrationsException(e);
		}
	}

	public void migrateExternal(ExternalMigrationConfig... externalMigrations) {
		LOGGER.info("Migrating external migrations for configurations {}", externalMigrations);
		for (ExternalMigrationConfig externalMigration : externalMigrations) {
			migrateExternalConfiguration(externalMigration);
		}
	}

	private void migrateExternalConfiguration(ExternalMigrationConfig externalMigration) {
		try {
			List<MigrationFile> migrationFiles = migrationExtractor.getMigrationFiles(externalMigration);
			migrationService.applyMigrations(migrationFiles);
		} catch (InvalidMigrationsException e) {
			LOGGER.error("Exception while migrating schema for external migration " + externalMigration, e);
			throw new InvalidMigrationsException(e);
		} catch (URISyntaxException e) {
			LOGGER.error("Exception while migrating schema for external migration " + externalMigration, e);
			throw new InvalidMigrationsException(e);
		}
	}
}
