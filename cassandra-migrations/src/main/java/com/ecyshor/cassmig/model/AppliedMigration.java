package com.ecyshor.cassmig.model;

import org.joda.time.DateTime;

import java.util.Date;

public class AppliedMigration extends BaseMigration {
	private DateTime timeExecuted;

	public AppliedMigration(int order, String md5sum, Date timeExecuted) {
		this(order, md5sum, new DateTime(timeExecuted));
	}

	public AppliedMigration(int order, String md5sum, DateTime timeExecuted) {
		this.order = order;
		this.md5Sum = md5sum;
		this.timeExecuted = timeExecuted;
	}

	public DateTime getTimeExecuted() {
		return timeExecuted;
	}

	public static AppliedMigration fromMigrationFile(MigrationFile migrationFile) {
		return new AppliedMigration(migrationFile.getOrder(), migrationFile.getMd5Sum(), DateTime.now());
	}

	public Date getTimeExecutedAsJavaDate() {
		return timeExecuted.toDate();
	}
}
