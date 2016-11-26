package net.shadowfacts.discordchat.core.permission;

import com.google.gson.*;
import net.dv8tion.jda.core.entities.Role;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.ILogger;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.shadowlib.util.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author shadowfacts
 */
public class PermissionManager implements IPermissionManager {

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(PermissionStore.class, new Deserializer()).setPrettyPrinting().create();

	private static ILogger logger;

	private File file;
	private PermissionStore permissions;

	public PermissionManager(IDiscordChat discordChat, File file) throws IOException {
		logger = discordChat.getLogger();
		this.file = file;
		load();
	}

	@Override
	public Permission get(Role role) {
		return permissions.containsKey(role.getId()) ? permissions.get(role.getId()) : Permission.GLOBAL;
	}

	@Override
	public void set(Role role, Permission permission) {
		permissions.put(role.getId(), permission);
	}

	@Override
	public void load() throws IOException {
		if (file.exists()) {
			permissions = GSON.fromJson(new FileReader(file), PermissionStore.class);
		} else {
			permissions = new PermissionStore();
			save();
		}
	}

	@Override
	public void save() throws IOException {
		String s = new Gson().toJson(permissions);
		InputStream in = new ByteArrayInputStream(s.getBytes());
		OutputStream out = new FileOutputStream(file);
		IOUtils.copy(in, out);
		in.close();
		out.close();
	}

	private static class PermissionStore extends HashMap<String, Permission> {

	}

	private static class Deserializer implements JsonDeserializer<PermissionStore> {

		@Override
		public PermissionStore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			PermissionStore store = new PermissionStore();
			JsonObject obj = json.getAsJsonObject();
			obj.entrySet().forEach(e -> {
				try {
					store.put(e.getKey(), Permission.valueOf(e.getValue().getAsString().toUpperCase()));
				} catch (IllegalArgumentException ex) {
					logger.warn(ex, "Invalid permission '" + e.getValue().getAsString() + "' for user '" + e.getKey() + "'");
				}
			});
			return store;
		}

	}

}
