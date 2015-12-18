--keyspace=marguerita2
--migration_init=public
--description=Initial keyspace creation for own keyspace
--start
    CREATE KEYSPACE marguerita2 WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
--end
