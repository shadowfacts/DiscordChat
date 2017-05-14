package net.shadowfacts.discordchat.one_eight;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void onServerChat(ServerChatEvent event) {
		String message = OneEightMod.discordChat.filterMCMessage(event.message);
		if (message != null) {
			OneEightMod.discordChat.sendMessage(OneEightMod.discordChat.getFormatter().fromMC(event.player.getDisplayNameString(), message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneEightMod.config.sendDeathMessages() && event.entityLiving instanceof EntityPlayer && !(event.entityLiving instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			OneEightMod.discordChat.sendMessage(OneEightMod.discordChat.getFormatter().death(player.getDisplayNameString(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerReceiveAchievement(AchievementEvent event) {
		if (OneEightMod.config.sendAchievementMessages() && event.entityPlayer instanceof EntityPlayerMP) {
			if (((EntityPlayerMP) event.entityPlayer).getStatFile().hasAchievementUnlocked(event.achievement)) {
				return;
			}
			if (!((EntityPlayerMP) event.entityPlayer).getStatFile().canUnlockAchievement(event.achievement)) {
				return;
			}
			IChatComponent achievementComponent = event.achievement.getStatName();
			IChatComponent achievementText = new ChatComponentText("[").appendSibling(achievementComponent).appendText("]");
			OneEightMod.discordChat.sendMessage(OneEightMod.discordChat.getFormatter().achievement(event.entityPlayer.getDisplayNameString(), achievementText.getUnformattedText()));
		}
	}

}
