package com.ecyshor.cassmig.model;

import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import java.util.List;

public class MigrationFile extends BaseMigration {

	protected List<String> commands;
	protected String usedKeyspace;
	private boolean hasKeyspaceSet = false;

	public MigrationFile(int order, String description, List<String> commands, String usedKeyspace, boolean useKeyspace) {
		this.order = order;
		this.description = description;
		this.commands = commands;
		this.usedKeyspace = usedKeyspace;
		this.hasKeyspaceSet = useKeyspace;
		this.md5Sum = getMd5ForMigrationStatements();
	}

	public MigrationFile(int order, String description, List<String> commands, String usedKeyspace) {
		this(order, description, commands, usedKeyspace, true);
	}

	public MigrationFile(int order, String description, List<String> commands) {
		this.order = order;
		this.description = description;
		this.commands = commands;
		this.md5Sum = getMd5ForMigrationStatements();
	}

	public MigrationFile(int order, String description, String md5sum, List<String> commands) {
		this.order = order;
		this.description = description;
		this.commands = commands;
		this.md5Sum = md5sum;
	}

	public List<String> getCommands() {
		List<String> finalCommands = Lists.newArrayList(commands);
		if (hasKeyspaceSet) {
			finalCommands.add(0, "USE " + usedKeyspace.trim() + ";");
		}
		return finalCommands;
	}

	public String getUsedKeyspace() {
		return usedKeyspace;
	}

	private String getMd5ForMigrationStatements() {
		StringBuilder builder = new StringBuilder();
		for (String configurationLine : getCommands()) {
			builder.append(configurationLine);
		}
		return DigestUtils.md5Hex(builder.toString());

	}

	public AppliedMigration toAppliedMigration() {
		return new AppliedMigration(this.getOrder(), this.getMd5Sum(), DateTime.now());
	}

}
