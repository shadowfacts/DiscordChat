package net.shadowfacts.discordchat.api.permission;

/**
 * @author shadowfacts
 */
public enum Permission {

	GLOBAL,
	APPROVED,
	ADMIN;

	public boolean has(Permission other) {
		return ordinal() >= other.ordinal();
	}

}
