package com.ecyshor.cassmig;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.BeforeClass;
import org.junit.Test;

public class CassandraMigratorIntTest {

	private static final String CASSANDRA_HOST = "localhost";

	private static Session session;
	private static CassandraMigrator migrator;

	@BeforeClass
	public static void setUp() {
		session = Cluster.builder().addContactPoints(CASSANDRA_HOST)
				.build().connect();
		migrator = new CassandraMigrator(session);
	}

	@Test
	public void shouldMigrateTheSchema() {
		migrator.migrate("migrations/integration");
		session.execute("SELECT pepper from marguerita.drinks_2");
		session.execute("SELECT * FROM marguerita.drinks");
	}

	@Test
	public void shouldDoNothingWhenMigratingTheSecondTime() {
		migrator.migrate("migrations/integration");
	}

	@Test
	public void shouldRunOnlyTheNewMigration(){
		migrator.migrate("migrations/integration2");
	}

}
