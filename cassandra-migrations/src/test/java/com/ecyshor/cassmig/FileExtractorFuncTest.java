package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidMigrationsException;
import com.ecyshor.cassmig.model.MigrationFile;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class FileExtractorFuncTest {

	private FileExtractor fileExtractor = new FileExtractor(new MigrationFileTransformer());

	@Test
	public void shouldExtractConfigurationLines() throws IOException, URISyntaxException {
		List<MigrationFile> migrationFiles = fileExtractor.getMigrationFiles("migrations/complete");
		assertThat(migrationFiles, hasSize(2));
		MigrationFile initFile = migrationFiles.get(0);
		assertThat(initFile.getOrder(), equalTo(-1));
	}

	@Test(expected = InvalidMigrationsException.class)
	public void shouldThrowExceptionWhenInitFileIsMissing() throws IOException, URISyntaxException {
		fileExtractor.getMigrationFiles("migrations/invalid");
	}

}
