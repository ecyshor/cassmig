package com.ecyshor.cassmig.model;

public class ModuleLoadingConfig extends LoadingConfig {

	private String path;

	public ModuleLoadingConfig(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	@Override public String toString() {
		return "ModuleLoadingConfig{" +
				"path='" + path + '\'' +
				'}';
	}
}
