package com.ecyshor.cassmig.mappings;

import com.datastax.driver.core.Row;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MigrationMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationMapper.class);

	public static List<AppliedMigration> map(List<Row> rows) {
		LOGGER.debug("Mapping casssandra rows to applied migrations.");
		List<AppliedMigration> appliedMigrations = Lists.newArrayList();
		for (Row row : rows) {
			appliedMigrations.add(new AppliedMigration((int) row.getLong("migration_order"), row.getString("md5sum"),
					row.getDate("time_executed")));
		}
		return appliedMigrations;
	}

}
