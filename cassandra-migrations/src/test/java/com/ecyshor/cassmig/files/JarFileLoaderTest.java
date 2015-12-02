package com.ecyshor.cassmig.files;

import com.ecyshor.cassmig.model.ExternalMigrationConfig;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

public class JarFileLoaderTest {

	private FileLoader<ExternalMigrationConfig> loader = new JarFileLoader();

	@Test
	public void shouldLoadCqlsFromAnotherJar() throws URISyntaxException {
		List<InputStream> results = loader.loadFiles(new ExternalMigrationConfig("com", "migrations/"));
	}

}
