package com.ecyshor.cassmig.model;

public class ExternalMigrationConfig extends LoadingConfig {

	public String migrationFilesPrefix;
	public String packagePrefix;

	public ExternalMigrationConfig(String packagePrefix, String migrationFilesPrefix) {
		this.packagePrefix = packagePrefix;
		this.migrationFilesPrefix = migrationFilesPrefix;
	}

	public String getMigrationFilesPrefix() {
		return migrationFilesPrefix;
	}

	public String getPackagePrefix() {
		return packagePrefix;
	}

	public static ExternalMigrationConfig forConfiguration(String packagePrefix, String migrationFilesPrefix) {
		return new ExternalMigrationConfig(packagePrefix, migrationFilesPrefix);
	}

	@Override public String toString() {
		return "ExternalMigrationConfig{" +
				"migrationFilesPrefix='" + migrationFilesPrefix + '\'' +
				", packagePrefix='" + packagePrefix + '\'' +
				'}';
	}
}
