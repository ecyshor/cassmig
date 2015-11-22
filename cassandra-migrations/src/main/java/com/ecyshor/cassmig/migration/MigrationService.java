package com.ecyshor.cassmig.migration;

import com.ecyshor.cassmig.MigrationDAO;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationComparator;
import com.ecyshor.cassmig.model.MigrationFile;
import com.ecyshor.cassmig.model.MigrationFiles;
import com.ecyshor.cassmig.validation.MigrationValidator;

import java.util.Collections;
import java.util.List;

public class MigrationService {

	private MigrationValidator migrationValidator;
	private MigrationDAO migrationDAO;

	public MigrationService(MigrationValidator migrationValidator, MigrationDAO migrationDAO) {
		this.migrationValidator = migrationValidator;
		this.migrationDAO = migrationDAO;
	}

	public void applyMigrations(List<MigrationFile> migrations) {
		MigrationFile initializationFile = MigrationFiles.getInitializationFile(migrations);
		List<AppliedMigration> appliedMigrations = migrationDAO.getAppliedMigrations(initializationFile.getUsedKeyspace());
		migrationValidator.validateMigrations(appliedMigrations, migrations);
		List<MigrationFile> migrationFiles = extractMigrationsThatHaveToBeApplied(appliedMigrations, migrations);
		for (MigrationFile migrationFile : migrationFiles) {
			applyMigration(migrationFile, initializationFile.getUsedKeyspace());
		}
	}

	public void applyMigration(MigrationFile migrationFile, String keyspace) {
		migrationDAO.applyMigration(migrationFile);
		migrationDAO.saveMigrationAsApplied(migrationFile.toAppliedMigration(), keyspace);
	}

	private List<MigrationFile> extractMigrationsThatHaveToBeApplied(List<AppliedMigration> appliedMigrations, List<MigrationFile> migrations) {
		Collections.sort(appliedMigrations, MigrationComparator.getInstance());
		Collections.sort(migrations, MigrationComparator.getInstance());
		return migrations.subList(appliedMigrations.size(), migrations.size());
	}

}
