package com.ecyshor;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.ecyshor.cassmig.CassandraMigrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfiguration {

	@Autowired
	private String cassandraHost;

	@Bean(destroyMethod = "close")
	public Cluster cassandraCluster() {
		return Cluster.builder().addContactPoints(cassandraHost).build();
	}

	@Bean
	public Session cassandraSession(Cluster cluster) throws InterruptedException {
		try {
			System.out.println("Connecting to cluster " + cluster.getConfiguration().toString());
			return cluster.connect();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			Thread.sleep(5000);
			return cassandraSession(cluster);
		}
	}

	@Bean
	public CassandraMigrator cassandraMigrator(Session session) {
		return new CassandraMigrator(session);
	}

}
