package net.shadowfacts.discordchat.api.permission;

import net.dv8tion.jda.core.entities.User;

/**
 * @author shadowfacts
 */
public interface IPermissionManager {

	Permission get(User user);

	void set(User user, Permission permission);

	default boolean has(User user, Permission permission) {
		return get(user).has(permission);
	}

}
