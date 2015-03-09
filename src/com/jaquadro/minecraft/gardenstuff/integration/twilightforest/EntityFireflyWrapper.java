package com.jaquadro.minecraft.gardenstuff.integration.twilightforest;

import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFireflyWrapper extends EntityWeatherEffect
{
    public EntityWeatherEffect entity;

    public EntityFireflyWrapper (World world, double x, double y, double z) {
        super(world);

        try {
            entity = (EntityWeatherEffect) TwilightForestIntegration.constEntityFirefly.newInstance(world, x, y, z);

            setPositionAndRotation(x, y, z, 0, 0);

        }
        catch (Throwable t) { }
    }

    @Override
    public void onUpdate () {
        super.onUpdate();

        if (entity != null) {
            entity.onUpdate();
            if (!entity.isEntityAlive())
                setDead();
        }
        else
            setDead();
    }

    @Override
    protected void entityInit () { }

    @Override
    protected void readEntityFromNBT (NBTTagCompound p_70037_1_) { }

    @Override
    protected void writeEntityToNBT (NBTTagCompound p_70014_1_) { }
}
