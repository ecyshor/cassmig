package com.ecyshor.cassmig;

import com.datastax.driver.core.Session;
import com.ecyshor.cassmig.exception.InvalidMigrationsException;
import com.ecyshor.cassmig.migration.MigrationService;
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

	private FileExtractor fileExtractor;
	private MigrationService migrationService;

	public CassandraMigrator(Session session) {
		this(new FileExtractor(new MigrationFileTransformer()), new MigrationService(new MigrationValidator(), new MigrationDAO(session)));
	}

	CassandraMigrator(FileExtractor fileExtractor, MigrationService migrationService) {
		DateTimeZone.setDefault(DateTimeZone.UTC);
		this.fileExtractor = fileExtractor;
		this.migrationService = migrationService;
	}

	/***
	 * @throws InvalidMigrationsException Something happend trying to migrate
	 */
	public void migrate(String migrationFilesPath) {
		try {
			List<MigrationFile> migrationFiles = fileExtractor.getMigrationFiles(migrationFilesPath);
			migrationService.applyMigrations(migrationFiles);
		} catch (IOException e) {
			LOGGER.error("Exception while migrating schema.", e);
			throw new InvalidMigrationsException(e);
		} catch (URISyntaxException e) {
			LOGGER.error("Exception while migrating schema.", e);
			throw new InvalidMigrationsException(e);
		}
	}
}
