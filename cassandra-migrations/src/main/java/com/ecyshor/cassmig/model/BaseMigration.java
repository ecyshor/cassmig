package com.ecyshor.cassmig.model;

public abstract class BaseMigration {
	protected String schema = "default";
	protected String md5Sum;
	protected int order;
	protected String description;

	public String getMd5Sum() {
		return md5Sum;
	}

	public int getOrder() {
		return order;
	}

	public String getSchema() {
		return schema;
	}

	public String getDescription() {
		return description;
	}

	@Override public String toString() {
		return "BaseMigration{" +
				"schema='" + schema + '\'' +
				", md5Sum='" + md5Sum + '\'' +
				", order=" + order +
				", description='" + description + '\'' +
				'}';
	}
}
