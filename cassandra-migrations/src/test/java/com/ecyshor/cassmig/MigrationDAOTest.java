package com.ecyshor.cassmig;

import com.datastax.driver.core.*;
import com.ecyshor.cassmig.model.AppliedMigration;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MigrationDAOTest {
	@Mock
	private Session session;
	@Mock
	private Cluster cluster;
	@Mock
	private Metadata metadata;
	@Mock
	private KeyspaceMetadata metadataMock;
	@Mock
	private TableMetadata tableMetadataMock;
	private MigrationDAO migrationDAO;
	private static final String KEYSPACE = "test";

	@Before
	public void setUp() throws Exception {
		migrationDAO = new MigrationDAO(session);
		when(session.getCluster()).thenReturn(cluster);
		when(cluster.getMetadata()).thenReturn(metadata);
		when(metadata.getKeyspace(KEYSPACE)).thenReturn(metadataMock);
		when(metadataMock.getTable(anyString())).thenReturn(tableMetadataMock);
	}

	@Test
	public void shouldReturnAppliedMigrations() {
		Row mockRow = mock(Row.class);
		ResultSet resultSet = mock(ResultSet.class);
		when(session.execute(Matchers.<Statement>any())).thenReturn(resultSet);
		when(resultSet.all()).thenReturn(Lists.newArrayList(mockRow));
		long order = 1L;
		when(mockRow.getLong("migration_order")).thenReturn(order);
		String md5sum = "lalalalala";
		when(mockRow.getString("md5sum")).thenReturn(md5sum);
		Date executed = new Date();
		when(mockRow.getDate("time_executed")).thenReturn(executed);
		List<AppliedMigration> appliedMigrations = migrationDAO.getAppliedMigrations(KEYSPACE);
		assertThat(appliedMigrations, hasSize(1));
		AppliedMigration appliedMigration = appliedMigrations.get(0);
		assertThat(appliedMigration.getTimeExecutedAsJavaDate(), equalTo(executed));
		assertThat(appliedMigration.getOrder(), equalTo((int) order));
		assertThat(appliedMigration.getMd5Sum(), equalTo(md5sum));
	}
}
