package net.shadowfacts.discordchat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.shadowfacts.discordchat.discord.DiscordThread;
import net.shadowfacts.shadowmc.util.LogHelper;

/**
 * @author shadowfacts
 */
@Mod(modid = DiscordChat.modId, name = DiscordChat.modId, version = DiscordChat.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.8.8,1.8.9]")
public class DiscordChat {

	public static final String modId = "DiscordChat";
	public static final String version = "1.0.6";

	public static LogHelper log = new LogHelper(modId);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		DCConfig.init(event.getModConfigurationDirectory());

		if (DCConfig.enabled) {
			MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		}
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		DiscordThread.runThread();
	}

}
