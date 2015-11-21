package com.ecyshor.cassmig.model;

public abstract class BaseMigration {
	protected String schema = "default";
	protected String md5Sum;
	protected int order;

	public String getMd5Sum() {
		return md5Sum;
	}

	public int getOrder() {
		return order;
	}

	public String getSchema() {
		return schema;
	}
}
