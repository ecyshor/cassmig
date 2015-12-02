package com.ecyshor.cassmig.validation;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationFile;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class MigrationValidatorTest {

	private MigrationValidator migrationValidator;

	@Before
	public void setUp() {
		migrationValidator = new MigrationValidator();
	}

	@Test
	public void shouldValidateMigrationsThatWereApplied() {
		String md5sum8 = UUID.randomUUID().toString();
		String md5sum2 = UUID.randomUUID().toString();
		String md5sum5 = UUID.randomUUID().toString();
		String md5sum1 = UUID.randomUUID().toString();
		List<AppliedMigration> appliedMigrations = Lists.newArrayList(new AppliedMigration("primary", 8, md5sum8, DateTime.now()),
				new AppliedMigration("primary", 2, md5sum2, DateTime.now()),
				new AppliedMigration("primary", 5, md5sum5, DateTime.now()),
				new AppliedMigration("primary", 1, md5sum1, DateTime.now()));
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(5, "", md5sum5, Lists.<String>newArrayList()),
				new MigrationFile(1, "", md5sum1, Lists.<String>newArrayList()),
				new MigrationFile(2, "", md5sum2, Lists.<String>newArrayList()),
				new MigrationFile(8, "", md5sum8, Lists.<String>newArrayList()));
		migrationValidator.validateMigrations(appliedMigrations, migrationFiles);
	}

	@Test
	public void shouldValidateMigrationsThatWereNotYetApplied() {
		String md5sum8 = UUID.randomUUID().toString();
		String md5sum2 = UUID.randomUUID().toString();
		String md5sum5 = UUID.randomUUID().toString();
		String md5sum1 = UUID.randomUUID().toString();
		List<AppliedMigration> appliedMigrations = Lists.newArrayList(new AppliedMigration("primary", 8, md5sum8, DateTime.now()),
				new AppliedMigration("primary", 2, md5sum2, DateTime.now()),
				new AppliedMigration("primary", 5, md5sum5, DateTime.now()),
				new AppliedMigration("primary", 1, md5sum1, DateTime.now()));
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(5, "", md5sum5, Lists.<String>newArrayList()),
				new MigrationFile(1, "", md5sum1, Lists.<String>newArrayList()),
				new MigrationFile(2, "", md5sum2, Lists.<String>newArrayList()),
				new MigrationFile(12, "", md5sum2, Lists.<String>newArrayList()),
				new MigrationFile(8, "", md5sum8, Lists.<String>newArrayList()));
		migrationValidator.validateMigrations(appliedMigrations, migrationFiles);
	}

	@Test(expected = InvalidDataException.class)
	public void shouldInvalidateMigrationsThatHaveExtraMigrationFiles() {
		String md5sum8 = UUID.randomUUID().toString();
		String md5sum2 = UUID.randomUUID().toString();
		String md5sum5 = UUID.randomUUID().toString();
		String md5sum1 = UUID.randomUUID().toString();
		List<AppliedMigration> appliedMigrations = Lists.newArrayList(new AppliedMigration("", 8, md5sum8, DateTime.now()),
				new AppliedMigration("", 2, md5sum2, DateTime.now()),
				new AppliedMigration("", 1, md5sum1, DateTime.now()));
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(5, "", md5sum5, Lists.<String>newArrayList()),
				new MigrationFile(1, "", md5sum1, Lists.<String>newArrayList()),
				new MigrationFile(2, "", md5sum2, Lists.<String>newArrayList()),
				new MigrationFile(8, "", md5sum8, Lists.<String>newArrayList()));
		migrationValidator.validateMigrations(appliedMigrations, migrationFiles);
	}

	@Test(expected = InvalidDataException.class)
	public void shouldInvalidateMigrationsThatAreMissingMigrationFiles() {
		String md5sum8 = UUID.randomUUID().toString();
		String md5sum2 = UUID.randomUUID().toString();
		String md5sum5 = UUID.randomUUID().toString();
		String md5sum1 = UUID.randomUUID().toString();
		List<AppliedMigration> appliedMigrations = Lists.newArrayList(new AppliedMigration("", 8, md5sum8, DateTime.now()),
				new AppliedMigration("", 2, md5sum2, DateTime.now()),
				new AppliedMigration("", 5, md5sum5, DateTime.now()),
				new AppliedMigration("", 1, md5sum1, DateTime.now()));
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(5, "", md5sum5, Lists.<String>newArrayList()),
				new MigrationFile(2, "", md5sum2, Lists.<String>newArrayList()),
				new MigrationFile(8, "", md5sum8, Lists.<String>newArrayList()));
		migrationValidator.validateMigrations(appliedMigrations, migrationFiles);
	}

}
