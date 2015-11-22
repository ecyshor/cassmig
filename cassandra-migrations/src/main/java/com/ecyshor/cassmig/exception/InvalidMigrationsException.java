package com.ecyshor.cassmig.exception;

public class InvalidMigrationsException extends RuntimeException {

	public InvalidMigrationsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMigrationsException(String message) {
		super(message);
	}

	public InvalidMigrationsException(Throwable cause) {
		super(cause);
	}
}
