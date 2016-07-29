package net.shadowfacts.discordchat;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class DCCommands {

	private static Map<String, Handler> commands = new HashMap<>();

	static {
		register("online", (channel, sender, args) -> {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			int online = server.getCurrentPlayerCount();
			channel.sendMessage(String.format("There %s %d player%s online", online == 1 ? "is" : "are", online, online == 1 ? "" : "s"));
			if (online != 0) {
				String players = String.join(", ", server.getPlayerList().getAllUsernames());
				channel.sendMessage("Online: " + players);
			}
		});
	}

	public static void register(String name, Handler handler) {
		commands.put(name, handler);
	}

	public static void handle(TextChannel channel, User sender, String command) {
		String[] bits = command.split(" ");
		if (bits.length <= 0) return;
		String name = bits[0];
		if (commands.containsKey(name)) {
			String[] args = Arrays.copyOfRange(bits, 1, bits.length);
			commands.get(name).handle(channel, sender, args);
		}
	}

	public interface Handler {
		void handle(TextChannel channel, User sender, String[] args);
	}

}
