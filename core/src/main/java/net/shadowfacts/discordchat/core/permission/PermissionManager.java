package net.shadowfacts.discordchat.core.permission;

import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.util.Map;

/**
 * @author shadowfacts
 */
public class PermissionManager implements IPermissionManager {

	private Map<String, Permission> permissions;

	public PermissionManager(IDiscordChat discordChat) {
		permissions = discordChat.getConfig().getPermissions();
	}

	@Override
	public Permission get(User user) {
		return permissions.containsKey(user.getId()) ? permissions.get(user.getId()) : Permission.GLOBAL;
	}

	@Override
	public void set(User user, Permission permission) {
		permissions.put(user.getId(), permission);
	}

}
