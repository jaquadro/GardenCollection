package com.jaquadro.minecraft.gardenstuff.integration;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.integration.twilightforest.EntityFireflyWrapper;
import com.jaquadro.minecraft.gardenstuff.integration.twilightforest.RenderFireflyWrapper;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Random;

public class TwilightForestIntegration
{
    public static final String MOD_ID = "TwilightForest";

    static Class classEntityFirefly;
    static Class classRenderFirefly;

    public static Constructor constEntityFirefly;
    public static Constructor constRenderFirefly;

    private static boolean initialized;

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        try {
            classEntityFirefly = Class.forName("twilightforest.entity.passive.EntityTFTinyFirefly");
            classRenderFirefly = Class.forName("twilightforest.client.renderer.entity.RenderTFTinyFirefly");

            constEntityFirefly = classEntityFirefly.getConstructor(World.class, double.class, double.class, double.class);
            constRenderFirefly = classRenderFirefly.getConstructor();

            if (GardenStuff.proxy instanceof ClientProxy)
                registerEntity();

            initialized = true;
        }
        catch (Throwable t) { }
    }

    @SideOnly(Side.CLIENT)
    private static void registerEntity () {
        RenderingRegistry.registerEntityRenderingHandler(EntityFireflyWrapper.class, new RenderFireflyWrapper());
    }

    public static boolean isLoaded () {
        return initialized;
    }

    public static void doFireflyEffect (World world, int x, int y, int z, Random rand) {
        try {
            EntityWeatherEffect tinyfly;
            double dx, dy, dz;

            for (int i = 0; i < 2; i++) {
                dx = x + ((rand.nextFloat() - rand.nextFloat()) * 0.3F + 0.5F);
                dy = y + ((rand.nextFloat() - rand.nextFloat()) * 0.4F - .05f);
                dz = z + ((rand.nextFloat() - rand.nextFloat()) * 0.3F + 0.5F);

                tinyfly = (EntityWeatherEffect) constEntityFirefly.newInstance(world, dx, dy, dz);
                //tinyfly = new EntityFireflyWrapper(world, dx, dy, dz);
                world.spawnEntityInWorld(tinyfly);
            }
        }
        catch (Throwable t) {

        }
    }
}
