package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardenapi.api.component.ILanternSource;
import com.jaquadro.minecraft.gardenapi.api.component.IRedstoneSource;
import com.jaquadro.minecraft.gardenapi.internal.Api;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardencore.util.BindingStack;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.integration.ColoredLightsIntegration;
import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;
import com.jaquadro.minecraft.gardenstuff.item.ItemLantern;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.crafting.IInfusionStabiliser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Optional.Interface(iface="thaumcraft.api.crafting.IInfusionStabiliser", modid="Thaumcraft", striprefs=true)
public class BlockLantern extends BlockContainer implements IInfusionStabiliser
{
    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;
    @SideOnly(Side.CLIENT)
    private IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconTopCross;
    @SideOnly(Side.CLIENT)
    private IIcon iconGlass;
    @SideOnly(Side.CLIENT)
    private IIcon iconCandle;

    public BlockLantern (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setTickRandomly(true);
        setHardness(2.5f);
        setResistance(5);
        setLightLevel(1);
        setBlockTextureName(GardenStuff.MOD_ID + ":lantern");
        setCreativeTab(ModCreativeTabs.tabGardenCore);

        setBlockBoundsForItemRender();
    }

    public boolean isGlass (ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("glass"))
                return tag.getBoolean("glass");
        }

        return false;
    }

    public String getLightSource (ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("src", Constants.NBT.TAG_STRING))
                return tag.getString("src");
        }

        return null;
    }

    public int getLightSourceMeta (ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("srcMeta"))
                return tag.getShort("srcMeta");
        }

        return 0;
    }

    @Override
    public String getItemIconName () {
        return GardenStuff.MOD_ID + ":lantern";
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.lanternRenderID;
    }

    @Override
    public int getRenderBlockPass () {
        return 1;
    }

    @Override
    public boolean canRenderInPass (int pass) {
        ClientProxy.lanternRenderer.renderPass = pass;
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        setBlockBoundsForItemRender();
    }

    @Override
    public void setBlockBoundsForItemRender () {
        setBlockBounds(.125f, 0, .125f, .875f, .875f, .875f);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public int damageDropped (int meta) {
        return meta;
    }

    @Override
    public boolean removedByPlayer (World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        if (willHarvest)
            return true;

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void harvestBlock (World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                boolean glass = false;
                String source = null;
                int sourceMeta = 0;

                if (tile != null) {
                    glass = tile.hasGlass();
                    source = tile.getLightSource();
                    sourceMeta = tile.getLightSourceMeta();
                }

                ItemStack stack = ((ItemLantern)Item.getItemFromBlock(this)).makeItemStack(1, metadata, glass, source, sourceMeta);
                ret.add(stack);
            }
        }
        return ret;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        if (tile == null)
            return false;

        ItemStack item = player.inventory.getCurrentItem();

        if (item == null && player.isSneaking() && tile.getLightSource() != null) {
            ILanternSource lanternSource = Api.instance.registries().lanternSources().getLanternSource(tile.getLightSource());
            if (lanternSource != null)
                dropBlockAsItem(world, x, y, z, lanternSource.getRemovedItem(tile.getLightSourceMeta()));

            tile.setLightSource((String)null);
            world.markBlockForUpdate(x, y, z);
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            tile.markDirty();
            return true;
        }
        else if (tile.getLightSource() == null && item != null) {
            for (ILanternSource lanternSource : Api.instance.registries.lanternSources.getAllLanternSources()) {
                if (lanternSource.isValidSourceItem(item)) {
                    tile.setLightSource(lanternSource.getSourceID());
                    tile.setLightSourceMeta(lanternSource.getSourceMeta(item));

                    if (player != null && !player.capabilities.isCreativeMode) {
                        if (--item.stackSize <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }

                    world.markBlockForUpdate(x, y, z);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                    world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
                    tile.markDirty();
                    return true;
                }
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        if (tile == null)
            return;

        ILanternSource lanternSource = Api.instance.registries.lanternSources.getLanternSource(tile.getLightSource());
        if (lanternSource != null)
            lanternSource.renderParticle(world, x, y, z, rand, tile.getLightSourceMeta());
    }

    @Override
    public boolean canHarvestBlock (EntityPlayer player, int meta) {
        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        if (getRedstoneSource(world, x, y, z) != null) {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    /*private void notifyPowerChange (World world, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x, y, z, this);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this);

        for (int i = 1; i < )
    }*/

    @Override
    public void getSubBlocks (Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));

        NBTTagCompound glassTag = new NBTTagCompound();
        glassTag.setBoolean("glass", true);

        for (int i = 0; i < 16; i++) {
            ItemStack entry = new ItemStack(item, 1, i);
            entry.setTagCompound(glassTag);
            list.add(entry);
        }
    }

    @Override
    public int getLightValue (IBlockAccess world, int x, int y, int z) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        if (tile == null)
            return 0;

        if (tile.hasGlass() && ColoredLightsIntegration.isInitialized())
            return ColoredLightsIntegration.getPackedColor(world.getBlockMetadata(x, y, z));

        ILanternSource lanternSource = Api.instance.registries.lanternSources.getLanternSource(tile.getLightSource());
        if (lanternSource != null)
            return lanternSource.getLightLevel(tile.getLightSourceMeta());

        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int meta) {
        if (side == 0)
            return iconBottom;
        if (side == 1)
            return iconTop;

        return iconSide;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconCandle () {
        return iconCandle;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconTopCross () {
        return iconTopCross;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconGlass (int meta) {
        return iconGlass;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconStainedGlass (int meta) {
        return Blocks.stained_glass_pane.getIcon(0, meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister register) {
        iconBottom = register.registerIcon(getTextureName() + "_bottom");
        iconSide = register.registerIcon(getTextureName());
        iconGlass = register.registerIcon(getTextureName() + "_glass");
        iconTop = register.registerIcon(getTextureName() + "_top");
        iconTopCross = register.registerIcon(getTextureName() + "_top_cross");
        iconCandle = register.registerIcon(GardenStuff.MOD_ID + ":candle");
    }

    @Override
    public TileEntity createNewTileEntity (World world, int meta) {
        return new TileEntityLantern();
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess world, int x, int y, int z, int side) {
        IRedstoneSource source = getRedstoneSource(world, x, y, z);
        return (side == 1 && source != null) ? source.strongPowerValue(getTileEntity(world, x, y, z).getLightSourceMeta()) : 0;
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess world, int x, int y, int z, int side) {
        IRedstoneSource source = getRedstoneSource(world, x, y, z);
        return (source != null) ? source.weakPowerValue(getTileEntity(world, x, y, z).getLightSourceMeta()) : 0;
    }

    @Override
    public boolean canProvidePower () {
        return true;
    }

    private IRedstoneSource getRedstoneSource (IBlockAccess world, int x, int y, int z) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        if (tile != null) {
            ILanternSource source = Api.instance.registries.lanternSources.getLanternSource(tile.getLightSource());
            if (source instanceof IRedstoneSource)
                return (IRedstoneSource)source;
        }

        return null;
    }

    public TileEntityLantern getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityLantern) ? (TileEntityLantern) te : null;
    }

    @Override
    @Optional.Method(modid = "Thaumcraft")
    public boolean canStabaliseInfusion (World world, int x, int y, int z) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        if (tile == null)
            return false;

        ILanternSource lanternSource = Api.instance.registries().lanternSources().getLanternSource(tile.getLightSource());
        if (lanternSource == null)
            return false;

        return lanternSource.getSourceID().equals("thaumcraftCandle");
    }
}
