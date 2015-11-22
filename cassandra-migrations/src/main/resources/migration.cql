CREATE TABLE %s.migrations(
  migration_schema varchar,
  migration_order bigint,
  time_executed timestamp,
  md5sum text,
  PRIMARY KEY (migration_schema, time_executed, migration_order)
);
