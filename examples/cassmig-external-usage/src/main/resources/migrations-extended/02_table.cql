--order=2
--keyspace=marguerita2
--description=Create the first table in own keyspace
--start

CREATE TABLE drinks_2 (
  salt bigint,
  column uuid,
  email varchar,
  PRIMARY KEY (salt, email)
);

--end
