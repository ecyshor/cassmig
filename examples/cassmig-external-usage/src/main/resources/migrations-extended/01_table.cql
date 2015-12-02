--order=1
--keyspace=marguerita2
--description=Create the first table in own keyspace
--start

CREATE TABLE drinks (
  salt bigint,
  column uuid,
  email varchar,
  PRIMARY KEY (salt, email)
);

--end
