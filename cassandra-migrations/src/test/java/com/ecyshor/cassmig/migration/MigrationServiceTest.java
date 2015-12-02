package com.ecyshor.cassmig.migration;

import com.ecyshor.cassmig.MigrationDAO;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.ecyshor.cassmig.model.MigrationFile;
import com.ecyshor.cassmig.validation.MigrationValidator;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class MigrationServiceTest {

	private MigrationService migrationService;
	private MigrationDAO migrationDAO;
	private MigrationValidator migrationValidator;

	@Before
	public void setUp() {
		migrationDAO = mock(MigrationDAO.class);
		migrationValidator = mock(MigrationValidator.class);
		migrationService = new MigrationService(migrationValidator, migrationDAO);
	}

	@Test
	public void shouldValidateTheMigrations() {
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(-100, ",", Lists.<String>newArrayList(), "test"));
		ArrayList<AppliedMigration> appliedMigrations = Lists.newArrayList();
		when(migrationDAO.getAppliedMigrations("test", "primary")).thenReturn(appliedMigrations);
		migrationService.applyMigrations(migrationFiles);
		verify(migrationDAO).getAppliedMigrations("test", "primary");
		verify(migrationValidator).validateMigrations(appliedMigrations, migrationFiles);
	}

	@Test
	public void shouldApplyAllTheMigrationAtFirst() {
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(-100, ",", Lists.<String>newArrayList(), "test"),
				new MigrationFile(8, ",", Lists.<String>newArrayList()));
		ArrayList<AppliedMigration> appliedMigrations = Lists.newArrayList();
		when(migrationDAO.getAppliedMigrations("test", "primary")).thenReturn(appliedMigrations);
		migrationService.applyMigrations(migrationFiles);
		verify(migrationDAO, times(2)).applyMigration(Matchers.<MigrationFile>any());
		verify(migrationDAO, times(2)).saveMigrationAsApplied(Matchers.<AppliedMigration>any(),
				eq("test"));
	}

	@Test
	public void shouldApplyOnlyTheMissingMigrations() {
		ArgumentCaptor<MigrationFile> migrationFileArgumentCaptor = ArgumentCaptor.forClass(MigrationFile.class);
		List<MigrationFile> migrationFiles = Lists.newArrayList(new MigrationFile(-100, ",", Lists.<String>newArrayList(), "test"),
				new MigrationFile(3, ",", Lists.<String>newArrayList()),
				new MigrationFile(5, ",", Lists.<String>newArrayList()),
				new MigrationFile(8, ",", Lists.<String>newArrayList()));
		ArrayList<AppliedMigration> appliedMigrations = Lists.newArrayList(new AppliedMigration("", -100, "", DateTime.now()),
				new AppliedMigration("", 3, "", DateTime.now()));
		when(migrationDAO.getAppliedMigrations("test", "primary")).thenReturn(appliedMigrations);
		migrationService.applyMigrations(migrationFiles);
		verify(migrationDAO, times(2)).applyMigration(migrationFileArgumentCaptor.capture());
		verify(migrationDAO, times(2)).saveMigrationAsApplied(Matchers.<AppliedMigration>any(),
				eq("test"));
		List<MigrationFile> allValues = migrationFileArgumentCaptor.getAllValues();
		MigrationFile migrationFile = allValues.get(0);
		MigrationFile secondMigrationFile = allValues.get(1);
		assertThat(migrationFile.getOrder(), equalTo(5));
		assertThat(secondMigrationFile.getOrder(), equalTo(8));

	}

}
