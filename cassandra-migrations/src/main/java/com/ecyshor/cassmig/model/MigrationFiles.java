package com.ecyshor.cassmig.model;

import com.ecyshor.cassmig.exception.InvalidDataException;

import java.util.Collections;
import java.util.List;

public class MigrationFiles {

	public static MigrationFile getInitializationFile(List<MigrationFile> migrationFiles) {
		Collections.sort(migrationFiles, MigrationComparator.getInstance());
		for (MigrationFile migrationFile : migrationFiles) {
			if (migrationFile.getOrder() == -100) {
				return migrationFile;
			}
		}
		throw new InvalidDataException("Could not find initialization file from the migration files.");
	}

}
