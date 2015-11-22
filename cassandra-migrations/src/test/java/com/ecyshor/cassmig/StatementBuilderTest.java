package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class StatementBuilderTest {

	@Test
	public void shouldBuildStatementWithoutEmptyLines() {
		List<String> rawStatements = Lists.newArrayList("", "create test", ";", "");
		List<String> statements = StatementBuilder.buildStatementsFromLines(rawStatements);
		assertThat(statements, hasSize(1));
		assertThat(statements.get(0), equalTo("create test;"));
	}

	@Test(expected = InvalidDataException.class)
	public void shouldThrowExceptionWhenStatementNotEnded() {
		List<String> rawStatements = Lists.newArrayList("correct statement;", "wrong statement");
		StatementBuilder.buildStatementsFromLines(rawStatements);
	}

}
