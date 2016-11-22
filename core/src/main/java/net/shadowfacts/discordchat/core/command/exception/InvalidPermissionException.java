package net.shadowfacts.discordchat.core.command.exception;

import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;

/**
 * @author shadowfacts
 */
public class InvalidPermissionException extends CommandException {

	public InvalidPermissionException(Permission required, Permission actual, String _for) {
		super(String.format("Permission %s is required for %s but user has only %s", required, _for, actual));
	}

}
