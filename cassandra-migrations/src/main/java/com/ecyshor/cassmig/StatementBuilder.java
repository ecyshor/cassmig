package com.ecyshor.cassmig;

import com.ecyshor.cassmig.exception.InvalidDataException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
		if (!completeStatement.isEmpty()) {
			throw new InvalidDataException("The statement " + completeStatement + "is not valid as it does not end in ;");
		}
		return statements;
	}
}
