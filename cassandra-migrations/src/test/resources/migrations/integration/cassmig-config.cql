--keyspace=marguerita
--migration_init=public
--description=Initial keyspace creation
--start
    CREATE KEYSPACE marguerita WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
--end
