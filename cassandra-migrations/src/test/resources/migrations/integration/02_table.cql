--order=2
--keyspace=marguerita
--description=Create the first table
--start

CREATE TABLE drinks_2 (
  salt bigint,
  column uuid,
  email varchar
  PRIMARY KEY (salt, email)
);

--end
