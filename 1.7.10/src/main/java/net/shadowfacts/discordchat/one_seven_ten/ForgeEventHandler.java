package net.shadowfacts.discordchat.one_seven_ten;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void onServerChat(ServerChatEvent event) {
		if (!(event.entityLiving instanceof FakePlayer)) {
		OneSevenTenMod.discordChat.sendMessage(OneSevenTenMod.discordChat.getFormatter().fromMC(event.player.getDisplayName(), event.message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (OneSevenTenMod.config.sendDeathMessages() && event.entityLiving instanceof EntityPlayer && !(event.entityLiving instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			OneSevenTenMod.discordChat.sendMessage(OneSevenTenMod.discordChat.getFormatter().death(player.getDisplayName(), player.getCombatTracker().func_151521_b().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerReceiveAchievement(AchievementEvent event) {
		if (OneSevenTenMod.config.sendAchievementMessages() && event.entityPlayer instanceof EntityPlayerMP) {
			if (((EntityPlayerMP) event.entityPlayer).getStatFile().hasAchievementUnlocked(event.achievement)) {
				return;
			}
			if (!((EntityPlayerMP) event.entityPlayer).getStatFile().canUnlockAchievement(event.achievement)) {
				return;
			}
			IChatComponent achievementComponent = event.achievement.getStatName();
			IChatComponent achievementText = new ChatComponentText("[").appendSibling(achievementComponent).appendText("]");
			OneSevenTenMod.discordChat.sendMessage(OneSevenTenMod.discordChat.getFormatter().achievement(event.entityPlayer.getDisplayName(), achievementText.getUnformattedText()));
		}
	}

}
