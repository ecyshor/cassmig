package com.ecyshor;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CassandraConfiguration.class, TestConfig.class})
public class SpringMigrationIntTest {

	@Autowired
	private Session session;

	@Test
	public void shouldHaveTheKeySpaceInitialized() {
		Metadata metadata = session.getCluster().getMetadata();
		KeyspaceMetadata marguerita = metadata.getKeyspace("marguerita");
		assertThat(marguerita, notNullValue());
	}

	@Test
	public void shouldHaveTheMigrationsApplied() {
		int count = (int) session.execute("SELECT COUNT(*) FROM marguerita.migrations;").one().getLong(0);
		assertThat(count, equalTo(4));
	}

}
