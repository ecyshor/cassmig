package com.ecyshor.cassmig;

import com.ecyshor.cassmig.model.ExternalMigrationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bootstrap {

	@Autowired
	private CassandraMigrator migrator;

	@PostConstruct
	public void migrate() {
		migrator.migrate("migrations-extended");
		migrator.migrateExternal(ExternalMigrationConfig.forConfiguration("com.ecyshor", "migrations/"));
	}

}
