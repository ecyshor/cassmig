package com.ecyshor;

import com.ecyshor.cassmig.CassandraMigrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bootstrap {

	@Autowired
	private CassandraMigrator migrator;

	@PostConstruct
	public void migrate() {
		migrator.migrate("migrations");
	}

}
