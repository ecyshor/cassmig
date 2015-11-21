package com.ecyshor.cassmig.exception;

public class InvalidMigrations extends RuntimeException {

	public InvalidMigrations(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMigrations(String message) {
		super(message);
	}
}
