package net.shadowfacts.discordchat.core;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.shadowlib.util.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shadowfacts
 */
public class Config implements IConfig {

	private File file;
	private com.typesafe.config.Config config;

	public Config(File file) throws IOException {
		this.file = file;
		load();
	}

	@Override
	public void load() throws IOException {
		config = ConfigFactory.parseFile(file).withFallback(ConfigFactory.load("assets/discordchat/default.conf"));

		// Migration handler
		if (config.hasPath("discordchat.discord.channel")) {
			String channel = config.getString("discordchat.discord.channel");
			config = config.withoutPath("discordchat.discord.channel");
			config = config.withValue("discordchat.discord.channels", ConfigValueFactory.fromAnyRef(Collections.singletonList(channel)));
		}

		save();
	}

	@Override
	public void save() throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		ConfigObject toRender = config.root().withOnlyKey("discordchat");
		String s = toRender.render(ConfigRenderOptions.defaults().setOriginComments(false).setJson(false));
		InputStream in = new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")));
		OutputStream out = new FileOutputStream(file);
		IOUtils.copy(in, out);
		in.close();
		out.close();

	}

	@Override
	public String getToken() {
		String conf = config.getString("discordchat.discord.token");
		if (conf.isEmpty()) {
			String prop = System.getProperty("discordchat.token");
			if (prop == null) {
				return System.getenv("DISCORDCHAT_TOKEN");
			} else {
				return prop;
			}
		} else {
			return conf;
		}
	}

	@Override
	public String getServerID() {
		String conf = config.getString("discordchat.discord.server");
		if (conf.isEmpty()) {
			String prop = System.getProperty("discordchat.server");
			if (prop == null) {
				return System.getenv("DISCORDCHAT_SERVER");
			} else {
				return prop;
			}
		} else {
			return conf;
		}
	}

	@Override
	public List<String> getChannels() {
		List<String> conf = config.getStringList("discordchat.discord.channels");
		if (conf.isEmpty() || conf.get(0).isEmpty()) {
			String prop = System.getProperty("discordchat.channels");
			String channels;
			if (prop == null) {
				channels = System.getenv("DISCORDCHAT_CHANNELS");
			} else {
				channels = prop;
			}
			return Arrays.asList(channels.split(","));
		} else {
			return conf;
		}
	}

	@Override
	public String getCommandPrefix() {
		return config.getString("discordchat.commands.prefix");
	}

	@Override
	public Permission getMinimumPermission(String command) {
		return Permission.valueOf(config.getString("discordchat.commands.permission." + command).toUpperCase());
	}

	@Override
	public FilterMode getMCMessageFilterMode() {
		return FilterMode.valueOf(config.getString("discordchat.filter.mode").toUpperCase());
	}

	@Override
	public String getMCMessageFilter() {
		return config.getString("discordchat.filter.filter");
	}

	@Override
	public boolean stripFilterPart() {
		return config.getBoolean("discordchat.filter.stripFilter");
	}

	@Override
	public boolean sendDeathMessages() {
		return config.getBoolean("discordchat.relay.deaths");
	}

	@Override
	public boolean sendAchievementMessages() {
		return config.getBoolean("discordchat.relay.achievements");
	}

	@Override
	public boolean sendJoinLeaveMessages() {
		return config.getBoolean("discordchat.relay.joinleave");
	}

	@Override
	public boolean sendServerOnlineOfflineMessages() {
		return config.getBoolean("discordchat.relay.onlineoffline");
	}

	@Override
	public String getDefaultColor() {
		return config.getString("discordchat.format.defaultColor");
	}

	@Override
	public String getFromMCFormat() {
		return config.getString("discordchat.format.fromMC");
	}

	@Override
	public String getFromDiscordFormat() {
		return config.getString("discordchat.format.fromDiscord");
	}

	@Override
	public String getFromMCPrivateFormat() {
		return config.getString("discordchat.format.fromMCPrivate");
	}

	@Override
	public String getFromDiscordPrivateFormat() {
		return config.getString("discordchat.format.fromDiscordPrivate");
	}

	@Override
	public String getDeathFormat() {
		return config.getString("discordchat.format.death");
	}

	@Override
	public String getAchievementFormat() {
		return config.getString("discordchat.format.achievement");
	}

	@Override
	public String getJoinFormat() {
		return config.getString("discordchat.format.join");
	}

	@Override
	public String getLeaveFormat() {
		return config.getString("discordchat.format.leave");
	}

	@Override
	public String getCommandFormat() {
		return config.getString("discordchat.format.command");
	}

}
