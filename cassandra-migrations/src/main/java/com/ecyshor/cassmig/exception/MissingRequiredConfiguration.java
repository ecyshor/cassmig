package com.ecyshor.cassmig.exception;

import org.omg.SendingContext.RunTime;

public class MissingRequiredConfiguration extends Exception {

	public MissingRequiredConfiguration(String message) {
		super(message);
	}
}
