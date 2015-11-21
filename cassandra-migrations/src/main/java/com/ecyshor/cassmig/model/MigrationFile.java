package com.ecyshor.cassmig.model;

import java.util.List;

public class MigrationFile extends BaseMigration {

	protected List<String> commands;
	protected String usedKeyspace;
	private boolean hasKeyspaceSet = false;

	public MigrationFile(int order, String md5sum, List<String> commands, String usedKeyspace) {
		this.order = order;
		this.md5Sum = md5sum;
		this.commands = commands;
		this.usedKeyspace = usedKeyspace;
		this.hasKeyspaceSet = true;
	}

	public MigrationFile(int order, String md5sum, List<String> commands) {
		this.order = order;
		this.md5Sum = md5sum;
		this.commands = commands;
	}

	public List<String> getCommands() {
		if (hasKeyspaceSet) {
			commands.add(0, "USE " + usedKeyspace.trim() + ";");
		}
		return commands;
	}
}
