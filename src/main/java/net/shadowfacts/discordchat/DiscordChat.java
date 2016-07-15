package net.shadowfacts.discordchat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.shadowfacts.discordchat.client.ClientSetupHandler;
import net.shadowfacts.discordchat.discord.DiscordThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author shadowfacts
 */
@Mod(modid = DiscordChat.modId, name = DiscordChat.modId, version = DiscordChat.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.10.2]", dependencies = "required-after:shadowmc;")
public class DiscordChat {

	public static final String modId = "DiscordChat";
	public static final String version = "@VERSION@";

	public static Logger log = LogManager.getLogger(modId);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		DCConfig.init(event.getModConfigurationDirectory());

		DCPrivateProps.init(event.getModConfigurationDirectory());

		if (DCConfig.enabled) {
			MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());

			if (!DCPrivateProps.setup && event.getSide() == Side.CLIENT) {
				MinecraftForge.EVENT_BUS.register(new ClientSetupHandler());
			}
		}
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		if (DCPrivateProps.setup) {
			log.info("Connecting to the Discord server...");

			DiscordThread.runThread();
		} else {
			log.error("DiscordChat has not been setup. Follow the instructions (https://git.io/vrGte) and change setup to true in config/shadowfacts/private.properties");
			DCConfig.enabled = false;
		}

	}

}
