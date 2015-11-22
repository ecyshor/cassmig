package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.ecyshor.cassmig.exception.InvalidMigrationsException;
import com.ecyshor.cassmig.exception.MissingRequiredConfiguration;
import com.ecyshor.cassmig.model.MigrationFile;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.beanutils.ConvertUtils.convert;

public class MigrationFileTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileExtractor.class);
	private static final String MIGRATION_START_MARK = "start";
	private static final String MIGRATION_END_MARK = "end";
	private static final String CONFIGURATION_OPTION_PREFIX = "--";
	private static final String MIGRATION_INIT_KEY = "migration_init";
	private static final String KEYSPACE_KEY = "keyspace";
	private static final String DESCRIPTION_KEY = "description";
	private static final String ORDER_KEY = "order";
	private static final String VALUE_SEPARATOR = "=";
	private boolean foundInitFile = false;

	public List<MigrationFile> transformFilesToMigrations(Collection<File> files) {
		List<MigrationFile> migrations = new LinkedList<MigrationFile>();
		for (File migrationFile : files) {
			MigrationFile migration = transformMigrationFileToMigration(migrationFile);
			migrations.add(migration);
		}
		if (!foundInitFile) {
			throw new InvalidMigrationsException("The initialization file was not provided. Please provide it.");
		}
		return migrations;
	}

	public MigrationFile transformMigrationFileToMigration(File migrationFile) {
		try {
			List<String> lines = readLinesForFile(migrationFile);
			List<String> configurationLines = extractConfigurationLines(lines);
			List<String> migrationLines = extractMigrationLines(lines);
			return transformFileContentToMigration(configurationLines, migrationLines);
		} catch (MissingRequiredConfiguration missingRequiredConfiguration) {
			String exceptionMessage = "Invalid file " + migrationFile.getName() + " for migration";
			LOGGER.error(exceptionMessage, missingRequiredConfiguration);
			throw new InvalidMigrationsException(exceptionMessage, missingRequiredConfiguration);
		} catch (FileNotFoundException e) {
			throw Throwables.propagate(e);
		} catch (InvalidDataException ex) {
			String exceptionMessage = "Invalid file " + migrationFile.getName() + " for migration";
			LOGGER.error(exceptionMessage, ex);
			throw new InvalidMigrationsException(exceptionMessage, ex);
		}
	}

	private List<String> extractMigrationLines(List<String> lines) {
		List<String> migrationLines = Lists.newArrayList();
		boolean startedMigrationLines = false;
		boolean migrationEnded = false;
		for (String line : lines) {
			String trimmedLine = line.trim();
			migrationEnded = trimmedLine.equalsIgnoreCase(CONFIGURATION_OPTION_PREFIX + MIGRATION_END_MARK);
			if (migrationEnded) {
				break;
			}
			if (startedMigrationLines) {
				migrationLines.add(line);
			}
			if (trimmedLine.equalsIgnoreCase(CONFIGURATION_OPTION_PREFIX + MIGRATION_START_MARK)) {
				startedMigrationLines = true;
			}
		}
		if (!startedMigrationLines) {
			throw new MissingRequiredConfiguration("The migration starting flag was not found, no migration configured in file.");
		}
		if (!migrationEnded) {
			throw new MissingRequiredConfiguration("The migration end flag was not found, migration misconfigured in file.");
		}
		return migrationLines;
	}

	private List<String> extractConfigurationLines(List<String> lines) {
		List<String> configurationLines = Lists.newArrayList();
		for (String line : lines) {
			String trimmedLine = line.trim();
			if (trimmedLine.startsWith(CONFIGURATION_OPTION_PREFIX)) {
				String configuration = trimmedLine.replace(CONFIGURATION_OPTION_PREFIX, "");
				if (!configuration.equalsIgnoreCase(MIGRATION_START_MARK) && !configuration.equalsIgnoreCase(MIGRATION_END_MARK)) {
					configurationLines.add(configuration);
				}
			}
		}
		return configurationLines;
	}

	private List<String> readLinesForFile(File migrationFile) throws FileNotFoundException {
		FileInputStream input = new FileInputStream(migrationFile);
		try {
			return IOUtils.readLines(input);
		} catch (IOException e) {
			String exceptionMessage = "Exception while reading migration file " + migrationFile.getName() + ". The migrations will be aborted.";
			LOGGER.error(exceptionMessage, e);
			throw Throwables.propagate(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	private MigrationFile transformFileContentToMigration(List<String> configurationLines, List<String> migrationLines)
			throws MissingRequiredConfiguration {
		if (configurationLines.contains(MIGRATION_INIT_KEY)) {
			return transformFileContentToInitialization(configurationLines, migrationLines);
		} else {
			return transformFileContentToNormalMigrationFile(configurationLines, migrationLines);
		}
	}

	private MigrationFile transformFileContentToInitialization(List<String> configurationLines, List<String> migrationLines)
			throws MissingRequiredConfiguration {
		foundInitFile = true;
		InputStream initializationSchema = this.getClass().getClassLoader().getResourceAsStream("migration.cql");
		try {
			List<String> migrationTable = IOUtils.readLines(initializationSchema);
			List<String> migrations = StatementBuilder.buildStatementsFromLines(migrationLines);
			List<String> migrationTableStatements = StatementBuilder.buildStatementsFromLines(migrationTable);
			String keyspace = findValueForKey(configurationLines, KEYSPACE_KEY);
			String description = findValueForKey(configurationLines, DESCRIPTION_KEY);
			migrationTableStatements.set(0, String.format(migrationTableStatements.get(0), keyspace));
			migrations.addAll(migrationTableStatements);
			return new MigrationFile(-1, description, migrations, keyspace, false);
		} catch (IOException e) {
			String exceptionMessage = "Exception while trying to read the schema for the migration table. The migrations will be aborted.";
			LOGGER.error(exceptionMessage, e);
			throw Throwables.propagate(e);
		}
	}

	private MigrationFile transformFileContentToNormalMigrationFile(List<String> configurationLines, List<String> migrationLines)
			throws MissingRequiredConfiguration {
		List<String> statements = StatementBuilder.buildStatementsFromLines(migrationLines);
		int order = findValueForKey(configurationLines, ORDER_KEY, Integer.class);
		String keyspace = findValueForKey(configurationLines, KEYSPACE_KEY);
		String description = findValueForKey(configurationLines, DESCRIPTION_KEY);
		if (order < 0)
			throw new InvalidDataException("The value " + order + " is not a valid order for the file. This must be a positive integer.");
		return new MigrationFile(order, description, statements, keyspace);
	}

	@SuppressWarnings("unchecked")
	private <T> T findValueForKey(List<String> configurationLines, String key, Class<T> clazz)
			throws MissingRequiredConfiguration {
		return (T) convert(findValueForKey(configurationLines, key), clazz);
	}

	private String findValueForKey(List<String> configurationLines, String key)
			throws MissingRequiredConfiguration {
		for (String configurationLine : configurationLines) {
			String completeKey = key + VALUE_SEPARATOR;
			if (configurationLine.startsWith(completeKey)) {
				String valueForKey = configurationLine.replace(completeKey, "").trim();
				if (org.apache.commons.lang3.StringUtils.isEmpty(valueForKey)) {
					throw new MissingRequiredConfiguration("The key " + key + " is provided but does not have a value.");
				}
				configurationLines.remove(configurationLine);
				return valueForKey;
			}
		}
		throw new MissingRequiredConfiguration("The key " + key + " is not provided");
	}

}
