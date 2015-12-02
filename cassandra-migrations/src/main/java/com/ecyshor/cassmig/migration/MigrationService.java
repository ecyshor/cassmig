package com.ecyshor.cassmig.migration;

import com.ecyshor.cassmig.MigrationDAO;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationComparator;
import com.ecyshor.cassmig.model.MigrationFile;
import com.ecyshor.cassmig.model.MigrationFiles;
import com.ecyshor.cassmig.validation.MigrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class MigrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationService.class);
	private MigrationValidator migrationValidator;
	private MigrationDAO migrationDAO;

	public MigrationService(MigrationValidator migrationValidator, MigrationDAO migrationDAO) {
		this.migrationValidator = migrationValidator;
		this.migrationDAO = migrationDAO;
	}

	public void applyMigrations(List<MigrationFile> migrations) {
		LOGGER.debug("Applying migrations {}.", migrations);
		MigrationFile initializationFile = MigrationFiles.getInitializationFile(migrations);
		List<AppliedMigration> appliedMigrations = migrationDAO.getAppliedMigrations(initializationFile.getUsedKeyspace(), initializationFile.getSchema());
		LOGGER.debug("The migrations {} already have been applied.", appliedMigrations);
		migrationValidator.validateMigrations(appliedMigrations, migrations);
		List<MigrationFile> migrationFiles = extractMigrationsThatHaveToBeApplied(appliedMigrations, migrations);
		LOGGER.info("A number of {} migrations have to be applied.", migrationFiles.size());
		for (MigrationFile migrationFile : migrationFiles) {
			LOGGER.info("Applying migration {}.", migrationFile);
			applyMigration(migrationFile, initializationFile.getUsedKeyspace());
		}
	}

	public void applyMigration(MigrationFile migrationFile, String keyspace) {
		migrationDAO.applyMigration(migrationFile);
		LOGGER.debug("Sacing migration {} as applied.", migrationFile);
		migrationDAO.saveMigrationAsApplied(migrationFile.toAppliedMigration(), keyspace);
	}

	private List<MigrationFile> extractMigrationsThatHaveToBeApplied(List<AppliedMigration> appliedMigrations, List<MigrationFile> migrations) {
		Collections.sort(appliedMigrations, MigrationComparator.getInstance());
		Collections.sort(migrations, MigrationComparator.getInstance());
		return migrations.subList(appliedMigrations.size(), migrations.size());
	}

}
