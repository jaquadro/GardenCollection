package com.jaquadro.minecraft.gardenstuff.integration.lantern;

import com.jaquadro.minecraft.gardenapi.api.component.StandardLanternSource;
import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.Random;

public class FireflyLanternSource extends StandardLanternSource
{
    private Block blockFirefly;

    public FireflyLanternSource (Block blockFirefly) {
        super(new LanternSourceInfo("firefly", Item.getItemFromBlock(blockFirefly), blockFirefly.getLightValue()));
        this.blockFirefly = blockFirefly;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticle (World world, int x, int y, int z, Random rand, int meta) {
        TwilightForestIntegration.doFireflyEffect(world, x, y, z, rand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInPass (int pass) {
        return false;
    }
}
