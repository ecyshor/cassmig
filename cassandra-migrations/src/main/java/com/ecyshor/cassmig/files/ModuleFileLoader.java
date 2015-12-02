package com.ecyshor.cassmig.files;

import com.ecyshor.cassmig.model.ModuleLoadingConfig;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.filefilter.FileFileFilter.FILE;

public class ModuleFileLoader implements FileLoader<ModuleLoadingConfig> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ModuleFileLoader.class);

	public List<InputStream> loadFiles(ModuleLoadingConfig config) throws URISyntaxException {
		URL resource = Thread.currentThread().getContextClassLoader().getResource(config.getPath());
		if (resource != null) {
			File file = new File(resource.toURI());
			Collection<File> files = FileUtils.listFiles(file, new AndFileFilter(Lists.asList(CanReadFileFilter.CAN_READ,
					new SuffixFileFilter(".cql"), new IOFileFilter[] {FILE})), FalseFileFilter.FALSE);
			return Lists.newArrayList(Iterables.transform(files, new Function<File, InputStream>() {
				@Nullable public InputStream apply(@Nullable File file) {
					try {
						assert file != null;
						return new FileInputStream(file);
					} catch (FileNotFoundException e) {
						String error = "Failure to open migration file. File not found for file " + file.getName();
						LOGGER.error(error, e);
						throw new RuntimeException(error);
					}
				}
			}));
		}
		throw new IllegalArgumentException("The configured folder " + config.getPath() + " for the migration is not correct.");
	}

}
