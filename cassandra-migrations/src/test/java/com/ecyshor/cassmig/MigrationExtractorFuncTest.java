package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidMigrationsException;
import com.ecyshor.cassmig.files.JarFileLoader;
import com.ecyshor.cassmig.files.ModuleFileLoader;
import com.ecyshor.cassmig.migration.MigrationExtractor;
import com.ecyshor.cassmig.model.MigrationFile;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class MigrationExtractorFuncTest {

	private MigrationExtractor migrationExtractor = new MigrationExtractor(new MigrationFileTransformer(), new ModuleFileLoader(), new JarFileLoader());

	@Test
	public void shouldExtractConfigurationLines() throws IOException, URISyntaxException {
		List<MigrationFile> migrationFiles = migrationExtractor.getMigrationFiles("migrations/complete");
		assertThat(migrationFiles, hasSize(2));
		MigrationFile initFile = migrationFiles.get(0);
		assertThat(initFile.getOrder(), equalTo(-100));
	}

	@Test(expected = InvalidMigrationsException.class)
	public void shouldThrowExceptionWhenInitFileIsMissing() throws IOException, URISyntaxException {
		migrationExtractor.getMigrationFiles("migrations/invalid");
	}

}
