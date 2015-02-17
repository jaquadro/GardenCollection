package com.jaquadro.minecraft.gardencore.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

public class EntitySteamFX extends EntitySmokeFX
{
    public EntitySteamFX (World world, double x, double y, double z) {
        super(world, x, y, z, 0, 0, 0);

        float color = .7f + (float)Math.random() * .3f;
        setRBGColorF(color, color, color);
    }

    public static EntityFX spawnParticle (World world, double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null) {
            int setting = mc.gameSettings.particleSetting;
            if (setting == 1 && mc.theWorld.rand.nextInt(3) == 0)
                setting = 2;

            double dx = mc.renderViewEntity.posX - x;
            double dy = mc.renderViewEntity.posY - y;
            double dz = mc.renderViewEntity.posZ - z;

            if (dx * dx + dy * dy + dz * dz > 16 * 16)
                return null;
            if (setting > 1)
                return null;

            EntityFX effect = new EntitySteamFX(world, x, y, z);
            mc.effectRenderer.addEffect(effect);

            return effect;
        }

        return null;
    }
}
