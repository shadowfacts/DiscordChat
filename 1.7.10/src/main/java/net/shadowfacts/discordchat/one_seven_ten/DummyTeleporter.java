package net.shadowfacts.discordchat.one_seven_ten;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * @author shadowfacts
 */
public class DummyTeleporter extends Teleporter {

	public DummyTeleporter(WorldServer world) {
		super(world);
	}

	@Override
	public void placeInPortal(Entity entity, double p_77185_2_, double p_77185_4_, double p_77185_6_, float p_77185_8_) {
		entity.motionX = 0;
		entity.motionY = 0;
		entity.motionZ = 0;
		entity.fallDistance = 0;
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_) {
		entity.motionX = 0;
		entity.motionY = 0;
		entity.motionZ = 0;
		entity.fallDistance = 0;
		return true;
	}

	@Override
	public boolean makePortal(Entity entity) {
		return true;
	}

	@Override
	public void removeStalePortalLocations(long worldTime) {

	}

}
