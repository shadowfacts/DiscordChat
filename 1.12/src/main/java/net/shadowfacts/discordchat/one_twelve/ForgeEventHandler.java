package net.shadowfacts.discordchat.one_twelve;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void onServerChat(ServerChatEvent event) {
		String message = OneTwelveMod.discordChat.filterMCMessage(event.getMessage());
		if (message != null) {
			OneTwelveMod.discordChat.sendMessage(OneTwelveMod.discordChat.getFormatter().fromMC(event.getPlayer().getName(), message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneTwelveMod.config.sendDeathMessages() && event.getEntityLiving() instanceof EntityPlayer && !(event.getEntityLiving() instanceof FakePlayer) && !event.getEntity().world.isRemote) {
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			OneTwelveMod.discordChat.sendMessage(OneTwelveMod.discordChat.getFormatter().death(player.getName(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneTwelveMod.config.sendJoinLeaveMessages()) {
			OneTwelveMod.discordChat.sendMessage(OneTwelveMod.discordChat.getFormatter().join(event.player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneTwelveMod.config.sendJoinLeaveMessages()) {
			OneTwelveMod.discordChat.sendMessage(OneTwelveMod.discordChat.getFormatter().leave(event.player.getName()));
		}
	}

}
