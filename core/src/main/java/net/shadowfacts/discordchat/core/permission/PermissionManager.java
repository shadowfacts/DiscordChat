package net.shadowfacts.discordchat.core.permission;

import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.IOException;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class PermissionManager implements IPermissionManager {

	private IConfig config;
	private Map<String, Permission> permissions;

	public PermissionManager(IDiscordChat discordChat) {
		config = discordChat.getConfig();
	}

	@Override
	public Permission get(User user) {
		return permissions.containsKey(user.getId()) ? permissions.get(user.getId()) : Permission.GLOBAL;
	}

	@Override
	public void set(User user, Permission permission) {
		permissions.put(user.getId(), permission);
	}

	@Override
	public void load() {
		permissions = config.getPermissions();
	}

	@Override
	public void save() throws IOException {
		config.setPermissions(permissions);
		config.save();
	}

}
