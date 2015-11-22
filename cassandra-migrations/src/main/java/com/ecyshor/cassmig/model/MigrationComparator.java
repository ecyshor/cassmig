package com.ecyshor.cassmig.model;

import java.util.Comparator;

public class MigrationComparator implements Comparator<BaseMigration> {

	private static final MigrationComparator comparator = new MigrationComparator();

	private MigrationComparator() {

	}

	public int compare(BaseMigration first, BaseMigration second) {
		return Integer.compare(first.getOrder(), second.getOrder());
	}

	public static MigrationComparator getInstance() {
		return comparator;
	}
}
