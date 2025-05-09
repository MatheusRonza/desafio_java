package com.desafio.authorization;

public record Authorization (
	String message) {
	public boolean isAuthorized() {
		return message.equals("Autorizado");
	}
}