package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardencore.util.BindingStack;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.integration.ColoredLightsIntegration;
import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;
import com.jaquadro.minecraft.gardenstuff.item.ItemLantern;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockLantern extends BlockContainer
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

    public BindingStack binding = new BindingStack();

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

    public TileEntityLantern.LightSource getLightSource (ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("src")) {
                int srcVal = tag.getByte("src");
                if (srcVal >= 0 && srcVal < TileEntityLantern.LightSource.values().length)
                    return TileEntityLantern.LightSource.values()[srcVal];
            }
        }

        return TileEntityLantern.LightSource.NONE;
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
                TileEntityLantern.LightSource source = TileEntityLantern.LightSource.NONE;

                if (tile != null) {
                    glass = tile.hasGlass();
                    source = tile.getLightSource();
                }

                ItemStack stack = ((ItemLantern)Item.getItemFromBlock(this)).makeItemStack(1, metadata, glass, source);
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

        //if (!player.isSneaking())
        //    return false;

        ItemStack item = player.inventory.getCurrentItem();

        if (item == null && player.isSneaking() && tile.getLightSource() != TileEntityLantern.LightSource.NONE) {
            switch (tile.getLightSource()) {
                case TORCH:
                    dropBlockAsItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(Blocks.torch)));
                    break;
                case REDSTONE_TORCH:
                    dropBlockAsItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(Blocks.redstone_torch)));
                    break;
                case GLOWSTONE:
                    dropBlockAsItem(world, x, y, z, new ItemStack(Items.glowstone_dust));
                    break;
                case FIREFLY:
                    if (TwilightForestIntegration.isLoaded()) {
                        Block firefly = Block.getBlockFromName(TwilightForestIntegration.MOD_ID + ":tile.TFFirefly");
                        if (firefly != null)
                            dropBlockAsItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(firefly)));
                    }
                    break;
                default:
                    return false;
            }

            tile.setLightSource(TileEntityLantern.LightSource.NONE);
            world.markBlockForUpdate(x, y, z);
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            tile.markDirty();
            return true;
        }
        else if (tile.getLightSource() == TileEntityLantern.LightSource.NONE && item != null) {
            if (item.getItem() instanceof ItemBlock) {
                Block block = Block.getBlockFromItem(item.getItem());
                if (block == Blocks.torch)
                    tile.setLightSource(TileEntityLantern.LightSource.TORCH);
                else if (block == Blocks.redstone_torch) {
                    tile.setLightSource(TileEntityLantern.LightSource.REDSTONE_TORCH);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                    world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
                }
                else if (TwilightForestIntegration.isLoaded() && block == Block.getBlockFromName(TwilightForestIntegration.MOD_ID + ":tile.TFFirefly"))
                    tile.setLightSource(TileEntityLantern.LightSource.FIREFLY);
                else
                    return false;

                if (player != null && !player.capabilities.isCreativeMode) {
                    if (--item.stackSize <= 0)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }

                world.markBlockForUpdate(x, y, z);
                tile.markDirty();
                return true;
            }
            else {
                if (item.getItem() == Items.glowstone_dust)
                    tile.setLightSource(TileEntityLantern.LightSource.GLOWSTONE);
                else
                    return false;

                if (player != null && !player.capabilities.isCreativeMode) {
                    if (--item.stackSize <= 0)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }

                world.markBlockForUpdate(x, y, z);
                tile.markDirty();
                return true;
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);

        double px = x + 0.5F;
        double py = y + 0.6F;
        double pz = z + 0.5F;

        switch (tile.getLightSource()) {
            case TORCH:
            case CANDLE:
                if (tile.getLightSource() == TileEntityLantern.LightSource.TORCH)
                    py += 0.1F;

                if (tile == null || !tile.hasGlass())
                    world.spawnParticle("smoke", px, py, pz, 0.0D, 0.0D, 0.0D);

                world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
                break;
            case REDSTONE_TORCH:
                px += (rand.nextFloat() - 0.5F) * 0.2D;
                py += (rand.nextFloat() - 0.5F) * 0.2D + 0.1F;
                pz += (rand.nextFloat() - 0.5F) * 0.2D;

                world.spawnParticle("reddust", px, py, pz, 0.0D, 0.0D, 0.0D);
                break;
            case FIREFLY:
                if (TwilightForestIntegration.isLoaded())
                    TwilightForestIntegration.doFireflyEffect(world, x, y, z, rand);
                break;
        }
    }

    @Override
    public boolean canHarvestBlock (EntityPlayer player, int meta) {
        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        if (hasRedstoneTorch(world, x, y, z)) {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        }

        super.breakBlock(world, x, y, z, block, data);
    }

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
        if (tile != null && tile.hasGlass() && ColoredLightsIntegration.isInitialized())
            return ColoredLightsIntegration.getPackedColor(world.getBlockMetadata(x, y, z));

        switch (tile.getLightSource()) {
            case TORCH:
            case CANDLE:
            case GLOWSTONE:
            case FIREFLY:
                return getLightValue();
            case REDSTONE_TORCH:
                return Blocks.redstone_torch.getLightValue();
            default:
                return 0;
        }
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
        return (side == 1 && hasRedstoneTorch(world, x, y, z)) ? 15 : 0;
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess world, int x, int y, int z, int side) {
        return hasRedstoneTorch(world, x, y, z) ? 15 : 0;
    }

    @Override
    public boolean canProvidePower () {
        return true;
    }

    private boolean hasRedstoneTorch (IBlockAccess world, int x, int y, int z) {
        TileEntityLantern tile = getTileEntity(world, x, y, z);
        if (tile != null)
            return tile.getLightSource() == TileEntityLantern.LightSource.REDSTONE_TORCH;

        return false;
    }

    public TileEntityLantern getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityLantern) ? (TileEntityLantern) te : null;
    }
}
