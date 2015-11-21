CREATE TABLE %s.migrations (
  schema varchar,
  order bigint,
  time_executed timestamp,
  md5sum text,
  PRIMARY KEY (schema, time_executed, order)
);
