package com.jaquadro.minecraft.gardencore.item;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.event.UseTrowelEvent;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.BlockGardenProxy;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardencore.core.handlers.GuiHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ItemTrowel extends Item
{
    protected ToolMaterial toolMaterial;

    public ItemTrowel (String name, ToolMaterial toolMaterial) {
        setUnlocalizedName(name);
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setTextureName(GardenCore.MOD_ID + ":garden_trowel");
        setMaxStackSize(1);
        setMaxDamage(toolMaterial.getMaxUses());

        this.toolMaterial = toolMaterial;
    }

    @Override
    public boolean onItemUse (ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(x, y, z, side, itemStack))
            return false;

        UseTrowelEvent event = new UseTrowelEvent(player, itemStack, world, x, y, z);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;

        if (event.getResult() == Event.Result.ALLOW) {
            itemStack.damageItem(1, player);
            return true;
        }

        if (side == 0)
            return false;

        Block block = world.getBlock(x, y, z);

        if (block instanceof BlockGarden) {
            player.openGui(GardenCore.instance, GuiHandler.gardenLayoutGuiID, world, x, y, z);
            return true;
        }
        else if (block instanceof BlockGardenProxy) {
            BlockGardenProxy proxy = (BlockGardenProxy) block;
            TileEntityGarden te = proxy.getGardenEntity(world, x, y, z);

            if (te != null) {
                player.openGui(GardenCore.instance, GuiHandler.gardenLayoutGuiID, world, te.xCoord, te.yCoord, te.zCoord);
                return true;
            }
        }

        /*if (world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
            Block.SoundType stepSound = ModBlocks.gardenSoil.stepSound;
            world.playSoundEffect( + .5f, y + .5f, z + .5f, stepSound.getStepResourcePath(), (stepSound.getVolume() + .5f) / 2, stepSound.getPitch() * .8f);

            if (!world.isRemote) {
                world.setBlock(x, y, z, ModBlocks.gardenSoil);
                itemStack.damageItem(1, player);
            }

            return true;
        }*/

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D () {
        return true;
    }
}
