package com.ecyshor.cassmig.validation;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationComparator;
import com.ecyshor.cassmig.model.MigrationFile;

import java.util.Collections;
import java.util.List;

public class MigrationValidator {

	public void validateMigrations(List<AppliedMigration> appliedMigrations, List<MigrationFile> migrationsFiles) {
		Collections.sort(appliedMigrations, MigrationComparator.getInstance());
		Collections.sort(migrationsFiles, MigrationComparator.getInstance());
		checkForMissingFiles(appliedMigrations, migrationsFiles);
		checkForInconsistencyInFiles(appliedMigrations, migrationsFiles);
	}

	private void checkForMissingFiles(List<AppliedMigration> appliedMigrations, List<MigrationFile> migrationsFiles) {
		if (appliedMigrations.size() > migrationsFiles.size()) {
			throw new InvalidDataException("We found more applied migrations than existing files.");
		}
	}

	private void checkForInconsistencyInFiles(List<AppliedMigration> appliedMigrations, List<MigrationFile> migrationsFiles) {
		for (int index = 0; index < appliedMigrations.size(); index++) {
			AppliedMigration appliedMigration = appliedMigrations.get(index);
			MigrationFile migrationFile = migrationsFiles.get(index);
			verifyConsistencyOfMigrationFile(appliedMigration, migrationFile);
		}
	}

	private void verifyConsistencyOfMigrationFile(AppliedMigration appliedMigration, MigrationFile migrationFile) {
		if (appliedMigration.getOrder() != migrationFile.getOrder() || !appliedMigration.getMd5Sum().equals(migrationFile.getMd5Sum())
				|| !appliedMigration.getSchema().equals(migrationFile.getSchema())) {
			throw new InvalidDataException("The applied migration " + appliedMigration +
					" is not consisntent to what we found in the files " + migrationFile);
		}
	}
}
