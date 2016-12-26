package net.shadowfacts.discordchat.api.permission;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.io.IOException;
import java.util.List;

/**
 * @author shadowfacts
 */
public interface IPermissionManager {

	Permission get(Role role);

	default Permission get(User user, Guild guild) {
		Permission max = Permission.GLOBAL;
		List<Role> roles = guild.getMember(user).getRoles();
		for (Role role : roles) {
			Permission permisison = get(role);
			if (permisison.ordinal() > max.ordinal()) {
				max = permisison;
			}
		}
		return max;
	}

	void set(Role role, Permission permission);

	default boolean has(Role role, Permission permission) {
		return get(role).has(permission);
	}

	default boolean has(User user, Guild guild, Permission permission) {
		return get(user, guild).has(permission);
	}

	void load() throws IOException;

	void save() throws IOException;

}
