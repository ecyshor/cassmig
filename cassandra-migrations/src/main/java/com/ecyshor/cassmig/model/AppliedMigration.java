package com.ecyshor.cassmig.model;

import org.joda.time.DateTime;

import java.util.Date;

public class AppliedMigration extends BaseMigration {
	private DateTime timeExecuted;

	public AppliedMigration(String schema, int order, String md5sum, Date timeExecuted) {
		this(schema, order, md5sum, new DateTime(timeExecuted));
	}

	public AppliedMigration(String schema, int order, String md5sum, DateTime timeExecuted) {
		this.schema = schema;
		this.order = order;
		this.md5Sum = md5sum;
		this.timeExecuted = timeExecuted;
	}

	public DateTime getTimeExecuted() {
		return timeExecuted;
	}

	public static AppliedMigration fromMigrationFile(MigrationFile migrationFile) {
		return new AppliedMigration(migrationFile.getSchema(), migrationFile.getOrder(), migrationFile.getMd5Sum(), DateTime.now());
	}

	public Date getTimeExecutedAsJavaDate() {
		return timeExecuted.toDate();
	}
}
