package com.ecyshor.cassmig;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

public class StatementBuilder {

	public static List<String> buildStatementsFromLines(List<String> statementLines) {
		List<String> statements = Lists.newArrayList();
		String completeStatement = "";
		for (String statementLine : statementLines) {
			if (StringUtils.isNotBlank(statementLine)) {
				completeStatement += statementLine;
				if (completeStatement.contains(";")) {
					statements.add(completeStatement);
					completeStatement = "";
				}
			}
		}
		return statements;
	}
}
