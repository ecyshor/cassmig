package com.ecyshor.cassmig;

import com.ecyshor.cassmig.model.MigrationFile;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.filefilter.FileFileFilter.FILE;

public class FileExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileExtractor.class);
	private MigrationFileTransformer migrationFileTransformer;

	public FileExtractor(MigrationFileTransformer migrationFileTransformer) {
		this.migrationFileTransformer = migrationFileTransformer;
	}

	public List<MigrationFile> getMigrationFiles(String path)
			throws IOException, URISyntaxException {
		URL resource = this.getClass().getClassLoader().getResource(path);
		if (resource != null) {
			File file = new File(resource.toURI());
			Collection<File> files = FileUtils.listFiles(file, new AndFileFilter(Lists.asList(CanReadFileFilter.CAN_READ,
					new SuffixFileFilter(".cql"), new IOFileFilter[] {FILE})), FalseFileFilter.FALSE);
			return migrationFileTransformer.transformFilesToMigrations(files);
		}
		throw new IllegalArgumentException("The configured folder " + path + " for the migration is not correct.");
	}

}
