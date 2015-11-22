--order=1
--keyspace=marguerita
--description=Create the first table
--start

CREATE TABLE drinks (
  salt bigint,
  column uuid,
  email varchar
  PRIMARY KEY (salt, email)
);

--end
