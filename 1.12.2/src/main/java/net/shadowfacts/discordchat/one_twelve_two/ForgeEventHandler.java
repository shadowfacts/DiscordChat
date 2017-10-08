package net.shadowfacts.discordchat.one_twelve_two;

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
		String message = OneTwelveTwoMod.discordChat.filterMCMessage(event.getMessage());
		if (message != null) {
			OneTwelveTwoMod.discordChat.sendMessage(OneTwelveTwoMod.discordChat.getFormatter().fromMC(event.getPlayer().getName(), message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneTwelveTwoMod.config.sendDeathMessages() && event.getEntityLiving() instanceof EntityPlayer && !(event.getEntityLiving() instanceof FakePlayer) && !event.getEntity().world.isRemote) {
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			OneTwelveTwoMod.discordChat.sendMessage(OneTwelveTwoMod.discordChat.getFormatter().death(player.getName(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (OneTwelveTwoMod.config.sendJoinLeaveMessages()) {
			OneTwelveTwoMod.discordChat.sendMessage(OneTwelveTwoMod.discordChat.getFormatter().join(event.player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (OneTwelveTwoMod.config.sendJoinLeaveMessages()) {
			OneTwelveTwoMod.discordChat.sendMessage(OneTwelveTwoMod.discordChat.getFormatter().leave(event.player.getName()));
		}
	}

}
