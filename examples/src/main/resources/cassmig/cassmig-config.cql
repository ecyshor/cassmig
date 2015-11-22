--keyspace=marguerita
--migration_init
--description=Initial keyspace creation
--start
    CREATE KEYSPACE IF NOT EXISTS marguerita WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
--end
