package com.ecyshor.cassmig.migration;

import com.ecyshor.cassmig.MigrationFileTransformer;
import com.ecyshor.cassmig.files.FileLoader;
import com.ecyshor.cassmig.model.ExternalMigrationConfig;
import com.ecyshor.cassmig.model.MigrationComparator;
import com.ecyshor.cassmig.model.MigrationFile;
import com.ecyshor.cassmig.model.ModuleLoadingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class MigrationExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationExtractor.class);
	private MigrationFileTransformer migrationFileTransformer;
	private FileLoader<ModuleLoadingConfig> fileLoader;
	private FileLoader<ExternalMigrationConfig> externalFileLoader;

	public MigrationExtractor(MigrationFileTransformer migrationFileTransformer,
			FileLoader<ModuleLoadingConfig> fileLoader,
			FileLoader<ExternalMigrationConfig> externalFileLoader) {
		this.migrationFileTransformer = migrationFileTransformer;
		this.fileLoader = fileLoader;
		this.externalFileLoader = externalFileLoader;
	}

	public List<MigrationFile> getMigrationFiles(String path)
			throws IOException, URISyntaxException {
		return getMigrationFiles(new ModuleLoadingConfig(path));
	}

	public List<MigrationFile> getMigrationFiles(ModuleLoadingConfig config)
			throws IOException, URISyntaxException {
		LOGGER.info("Finding migration files in config {}.", config);
		List<InputStream> files = fileLoader.loadFiles(config);
		return getMigrationFiles(files);
	}

	public List<MigrationFile> getMigrationFiles(ExternalMigrationConfig externalMigration)
			throws URISyntaxException {
		LOGGER.info("Finding migration files in config {}.", externalMigration);
		List<InputStream> files = externalFileLoader.loadFiles(externalMigration);
		List<MigrationFile> migrationFiles = getMigrationFiles(files);
		addSchemaToMigrationFiles(migrationFiles, externalMigration);
		return migrationFiles;
	}

	private void addSchemaToMigrationFiles(List<MigrationFile> migrationFiles, ExternalMigrationConfig externalMigration) {
		String schema = externalMigration.getPackagePrefix() + externalMigration.getMigrationFilesPrefix();
		for (MigrationFile migrationFile : migrationFiles) {
			migrationFile.setSchema(schema);
		}
	}

	private List<MigrationFile> getMigrationFiles(List<InputStream> files) {
		List<MigrationFile> migrationFiles = migrationFileTransformer.transformFilesToMigrations(files);
		Collections.sort(migrationFiles, MigrationComparator.getInstance());
		return migrationFiles;
	}
}
