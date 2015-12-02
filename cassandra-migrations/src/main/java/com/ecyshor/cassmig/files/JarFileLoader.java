package com.ecyshor.cassmig.files;

import com.ecyshor.cassmig.model.ExternalMigrationConfig;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class JarFileLoader implements FileLoader<ExternalMigrationConfig> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JarFileLoader.class);

	public List<InputStream> loadFiles(@Nullable final ExternalMigrationConfig config)
			throws URISyntaxException {
		assert config != null;
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forPackage(config.getPackagePrefix()))
				.setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));
		Set<String> resources = reflections.getResources(Pattern.compile(".*\\.cql"));
		Iterable<String> filteredResources = Iterables.filter(resources, new Predicate<String>() {
			public boolean apply(@Nullable String resource) {
				assert resource != null;
				return resource.startsWith(config.getMigrationFilesPrefix());
			}
		});
		LOGGER.info("Found a number of {} migration files for configuration {}.", Iterables.size(filteredResources), config);
		return Lists.newArrayList(Iterables.transform(filteredResources, new Function<String, InputStream>() {
			@Nullable public InputStream apply(@Nullable String resource) {
				assert resource != null;
				return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			}
		}));
	}
}
