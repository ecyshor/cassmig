package com.ecyshor.cassmig;

import com.ecyshor.cassmig.model.MigrationFile;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class MigrationFileTransformerTest extends MigrationFileTransformer {

	@Test
	public void shouldReturnInitializationFile() throws URISyntaxException {
		URL resource = this.getClass().getClassLoader().getResource("migrations/test_init_file.cql");
		assert resource != null;
		File file = new File(resource.toURI());
		MigrationFile initFile = transformMigrationFileToMigration(file);
		assertThat(initFile.getOrder(), equalTo(-1));
		assertThat(initFile.getCommands(), hasSize(2));
		String initCommandsReceived = initFile.getCommands().get(0);
		String schemaForMigrationTable = initFile.getCommands().get(1);
		assertThat(initCommandsReceived, equalTo("CREATE KEYSPACE test_keyspace;"));
		assertThat(schemaForMigrationTable, equalTo("CREATE TABLE test_keyspace.migrations (  schema varchar,  order bigint,  time_executed timestamp,  md5sum text,  PRIMARY KEY (schema, time_executed, order));"));
	}

}
