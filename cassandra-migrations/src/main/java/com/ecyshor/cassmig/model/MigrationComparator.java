package com.ecyshor.cassmig.model;

import java.util.Comparator;

public class MigrationComparator implements Comparator<BaseMigration> {

	private static final MigrationComparator comparator = new MigrationComparator();

	private MigrationComparator() {

	}

	public int compare(BaseMigration first, BaseMigration second) {
		if (first.getOrder() != second.getOrder())
			return Integer.compare(first.getOrder(), second.getOrder());
		else
			return first.getMd5Sum().compareTo(second.getMd5Sum());
	}

	public static MigrationComparator getInstance() {
		return comparator;
	}
}
