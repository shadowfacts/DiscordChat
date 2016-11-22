package net.shadowfacts.discordchat.core.command.exception;

import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;

/**
 * @author shadowfacts
 */
public class InvalidUsageException extends CommandException {

	public InvalidUsageException(ICommand command) {
		super("Invalid use of " + command.getName() + ". Usage: " + command.getName() + " " + command.getUsage());
	}

}
