package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModBlocks;
import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.config.PatternConfig;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockLargePot extends BlockContainer
{
    public static final String[] subTypes = new String[] { "default", "raw" };

    public enum Direction {
        North (1 << 0),
        East (1 << 1),
        South (1 << 2),
        West (1 << 3),
        NorthWest (1 << 4),
        NorthEast (1 << 5),
        SouthEast (1 << 6),
        SouthWest (1 << 7);

        private final int flag;

        Direction (int flag) {
            this.flag = flag;
        }

        public int getFlag () {
            return this.flag;
        }

        public static boolean isSet (int bitflags, Direction direction) {
            return (bitflags & direction.flag) != 0;
        }

        public static int set (int bitflags, Direction direction) {
            return bitflags | direction.flag;
        }

        public static int clear (int bitflags, Direction direction) {
            return bitflags & ~direction.flag;
        }

        public static int setOrClear (int bitflags, Direction direction, boolean set) {
            return set ? set(bitflags, direction) : clear(bitflags, direction);
        }
    }

    private boolean colored;

    @SideOnly(Side.CLIENT)
    private IIcon iconSide;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconOverlayArray;

    // Scratch
    private int scratchDropMetadata;

    public BlockLargePot (String blockName, boolean colored) {
        super(Material.clay);

        this.colored = colored;

        setCreativeTab(ModularPots.tabModularPots);
        setHardness(.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeStone);
        setBlockName(blockName);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        float dim = .0625f;

        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te == null || te.getSubstrate() == null || !isSubstrateSolid(te.getSubstrate()))
            setBlockBounds(0, 0, 0, 1, dim, 1);
        else
            setBlockBounds(0, 0, 0, 1, 1 - dim, 1);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        if (!isCompatibleNeighbor(world, x, y, z, -1, 0)) {
            setBlockBounds(0, 0, 0, dim, 1, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (!isCompatibleNeighbor(world, x, y, z, 0, -1)) {
            setBlockBounds(0, 0, 0, 1, 1, dim);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (!isCompatibleNeighbor(world, x, y, z, 1, 0)) {
            setBlockBounds(1 - dim, 0, 0, 1, 1, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (!isCompatibleNeighbor(world, x, y, z, 0, 1)) {
            setBlockBounds(0, 0, 1 - dim, 1, 1, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        setBlockBoundsForItemRender();
    }

    @Override
    public void setBlockBoundsForItemRender () {
        setBlockBounds(0, 0, 0, 1, 1, 1);
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
        return ClientProxy.largePotRenderID;
    }

    @Override
    public boolean canRenderInPass (int pass) {
        ClientProxy.renderPass = pass;
        return true;
    }

    @Override
    public int getRenderBlockPass () {
        return 1;
    }

    @Override
    public boolean isSideSolid (IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess blockAccess, int x, int y, int z, int side) {
        int nx = x;
        int nz = z;

        switch (side) {
            case 0:
                y++;
                break;
            case 1:
                y--;
                break;
            case 2:
                z++;
                break;
            case 3:
                z--;
                break;
            case 4:
                x++;
                break;
            case 5:
                x--;
                break;
        }

        if (side >= 2 && side < 6)
            return !isCompatibleNeighbor(blockAccess, x, y, z, nx - x, nz - z);

        return side != 1;
    }

    @Override
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te == null || te.getSubstrate() == null)
            return false;

        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        Block plant = plantable.getPlant(world, x, y + 1, z);
        Block substrate = Block.getBlockFromItem(te.getSubstrate());

        if (plant == Blocks.cactus)
            return substrate == Blocks.sand;

        return plantType == EnumPlantType.Crop && substrate == Blocks.farmland;
    }

    @Override
    public boolean isFertile (World world, int x, int y, int z) {
        return true;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (world.isRemote)
            return;

        if (y >= world.getHeight() - 1)
            return;

        Block overBlock = world.getBlock(x, y + 1, z);
        if (overBlock.isAir(world, x, y, z)) {
            TileEntityLargePot tileEntity = getTileEntity(world, x, y, z);
            if (tileEntity != null) {
                tileEntity.setItem(null, 0);
                tileEntity.markDirty();
            }
        }
    }

    public boolean isCompatibleNeighbor (IBlockAccess world, int x, int y, int z, int dx, int dz) {
        Block block = world.getBlock(x + dx, y, z + dz);
        if (block != this)
            return false;

        int meta = world.getBlockMetadata(x, y, z);
        int metaComp = world.getBlockMetadata(x + dx, y, z + dz);
        if (meta != metaComp)
            return false;

        if (!colored && meta == 1)
            return false;

        BlockLargePot pot = (BlockLargePot) block;
        TileEntityLargePot teThis = getTileEntity(world, x, y, z);
        TileEntityLargePot teThat = getTileEntity(world, x + dx, y, z + dz);
        if (teThis == null || teThat == null)
            return false;

        if (teThis.getSubstrate() != teThat.getSubstrate() || teThis.getSubstrateData() != teThat.getSubstrateData())
            return false;

        return true;
    }

    @Override
    public void onBlockHarvested (World world, int x, int y, int z, int p_149681_5_, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, p_149681_5_, player);

        if (player.capabilities.isCreativeMode) {
            TileEntityLargePot te = getTileEntity(world, x, y, z);
            if (te != null) {
                te.setSubstrate(null, 0);
                te.setItem(null, 0);
                te.markDirty();
            }
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);

        if (te != null && te.getSubstrate() != null) {
            Block substrate = Block.getBlockFromItem(te.getSubstrate());
            if (substrate != Blocks.water) {
                if (substrate == Blocks.farmland)
                    substrate = Blocks.dirt;

                ItemStack item = new ItemStack(substrate, 1, te.getSubstrateOriginalData());
                dropBlockAsItem(world, x, y, z, item);
            }
        }

        if (te != null && te.getFlowerPotItem() != null) {
            ItemStack item = new ItemStack(te.getFlowerPotItem(), 1, te.getFlowerPotData());
            dropBlockAsItem(world, x, y, z, item);
        }

        if (te != null)
            scratchDropMetadata = te.getCarving() << 8;

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
                items.add(new ItemStack(item, 1, metadata | scratchDropMetadata));
        }

        return items;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        if (side != 1)
            return false;

        ItemStack itemStack = player.inventory.getCurrentItem();
        if (itemStack == null)
            return false;

        TileEntityLargePot tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity == null) {
            tileEntity = new TileEntityLargePot();
            world.setTileEntity(x, y, z, tileEntity);
        }

        IPlantable plantable = null;
        Item item = itemStack.getItem();
        if (item instanceof IPlantable)
            plantable = (IPlantable) item;
        else if (item instanceof ItemBlock) {
            Block itemBlock = Block.getBlockFromItem(item);
            if (itemBlock instanceof IPlantable)
                plantable = (IPlantable) itemBlock;
        }

        if (tileEntity.getSubstrate() == null && isValidSubstrate(item))
            applySubstrate(world, x, y, z, tileEntity, player);
        else if (tileEntity.getSubstrate() != null && canApplyItemToSubstrate(tileEntity, itemStack))
            applyItemToSubstrate(world, x, y, z, tileEntity, player);
        else if (plantable != null && canSustainPlantActivated(world, x, y, z, plantable)) {
            if (!enoughAirAbove(world, x, y, z, plantable))
                return false;
            applyPlantable(world, x, y, z, tileEntity, player, plantable);
        }
        else
            return false;

        return true;
    }

    protected void applySubstrate (World world, int x, int y, int z, TileEntityLargePot tile, EntityPlayer player) {
        ItemStack substrate = player.inventory.getCurrentItem();

        if (!colored && world.getBlockMetadata(x, y, z) == 1) {
            world.setBlockToAir(x, y, z);
            world.playSoundAtEntity(player, "dig.sand", 1.0f, 1.0f);
            for (int i = 0; i < 4; i++)
                dropBlockAsItem(world, x, y, z, new ItemStack(Items.clay_ball));
            return;
        }

        if (substrate.getItem() == Items.water_bucket) {
            tile.setSubstrate(Item.getItemFromBlock(Blocks.water), 0);
            tile.markDirty();
            if (!player.capabilities.isCreativeMode)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
        }
        else {
            tile.setSubstrate(substrate.getItem(), substrate.getItemDamage());
            tile.markDirty();
            if (!player.capabilities.isCreativeMode && --substrate.stackSize <= 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
        world.markBlockForUpdate(x, y, z);
    }

    protected boolean canApplyItemToSubstrate (TileEntityLargePot tile, ItemStack itemStack) {
        return itemStack.getItem() == Items.water_bucket || itemStack.getItem() == ModularPots.soilTestKitUsed;
    }

    protected void applyItemToSubstrate (World world, int x, int y, int z, TileEntityLargePot tile, EntityPlayer player) {
        ItemStack item = player.inventory.getCurrentItem();
        if (item.getItem() == Items.water_bucket)
            applyWaterToSubstrate(world, x, y, z, tile, player);
        else if (item.getItem() == ModularPots.soilTestKitUsed)
            applyTestKit(world, x, y, z, item);
    }

    protected void applyWaterToSubstrate (World world, int x, int y, int z, TileEntityLargePot tile, EntityPlayer player) {
        if (Block.getBlockFromItem(tile.getSubstrate()) == Blocks.dirt) {
            tile.setSubstrate(Item.getItemFromBlock(Blocks.farmland), 1, tile.getSubstrateData());
            tile.markDirty();

            world.markBlockForUpdate(x, y, z);
        }
    }

    public boolean applyTestKit (World world, int x, int y, int z, ItemStack testKit) {
        if (testKit.getItem() != ModularPots.soilTestKitUsed)
            return false;

        TileEntityLargePot tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity == null)
            return false;

        Block substrate = Block.getBlockFromItem(tileEntity.getSubstrate());
        if (substrate != Blocks.dirt && substrate != Blocks.grass && substrate != Blocks.farmland)
            return false;

        tileEntity.setBiomeData(testKit.getItemDamage());
        world.markBlockForUpdate(x, y, z);

        for (int i = 0; i < 5; i++) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            world.spawnParticle("happyVillager", x + world.rand.nextFloat(), y + .5f + world.rand.nextFloat(), z + world.rand.nextFloat(), d0, d1, d2);
        }

        return true;
    }

    protected void applyPlantable (World world, int x, int y, int z, TileEntityLargePot tile, EntityPlayer player, IPlantable plantable) {
        ItemStack itemStack = player.inventory.getCurrentItem();

        // TODO: Non-compliant IPlantable, use config
        Block itemBlock = plantable.getPlant(world, x, y, z);
        if (itemBlock == null && plantable instanceof Block)
            itemBlock = (Block) plantable;

        world.setBlock(x, y + 1, z, ModBlocks.largePotPlantProxy, itemStack.getItemDamage(), 3);
        if (itemBlock instanceof BlockDoublePlant || itemBlock.getRenderType() == 40)
            world.setBlock(x, y + 2, z, ModBlocks.largePotPlantProxy, itemStack.getItemDamage() | 8, 3);

        tile.setItem(itemStack.getItem(), itemStack.getItemDamage());
        tile.markDirty();

        if (!player.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
    }

    private boolean enoughAirAbove (IBlockAccess world, int x, int y, int z, IPlantable plant) {
        Block plantBlock = plant.getPlant(world, x, y + 1, z);
        int height = (plantBlock instanceof BlockDoublePlant) ? 2 : 1;

        boolean enough = true;
        for (int i = 0; i < height; i++)
            enough &= world.isAirBlock(x, y + 1 + i, z);

        return enough;
    }

    protected boolean isValidSubstrate (Item item) {
        if (item == Items.water_bucket)
            return true;

        Block block = Block.getBlockFromItem(item);
        if (block == null)
            return false;

        return block == Blocks.dirt
            || block == Blocks.sand
            || block == Blocks.gravel
            || block == Blocks.soul_sand
            || block == Blocks.grass
            || block == Blocks.water;
    }

    private boolean isSubstrateSolid (Item item) {
        Block block = Block.getBlockFromItem(item);
        return block != Blocks.water;
    }

    protected boolean canSustainPlantActivated (IBlockAccess world, int x, int y, int z, IPlantable plantable) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te == null || te.getSubstrate() == null)
            return false;

        Block substrate = Block.getBlockFromItem(te.getSubstrate());
        if (substrate == null)
            return false;

        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        Block plant = plantable.getPlant(world, x, y + 1, z);

        // TODO: Non-compliant IPlantable, use config
        if (plantType == null)
            plantType = EnumPlantType.Plains;

        if (plant == Blocks.cactus)
            return false;

        switch (plantType) {
            case Desert:
                return substrate == Blocks.sand;
            case Nether:
                return substrate == Blocks.soul_sand;
            //case Crop:
            //    return substrate == Blocks.dirt || substrate == Blocks.farmland;
            case Cave:
                return true;
            case Plains:
                return substrate == Blocks.grass || substrate == Blocks.dirt;
            case Beach:
                return substrate == Blocks.grass || substrate == Blocks.dirt || substrate == Blocks.sand;
            case Water:
                return substrate == Blocks.water;
            default:
                return false;
        }
    }

    public TileEntityLargePot getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return (tileEntity != null && tileEntity instanceof  TileEntityLargePot) ? (TileEntityLargePot) tileEntity : null;
    }

    @Override
    public TileEntity createNewTileEntity (World world, int data) {
        return new TileEntityLargePot();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int data) {
        if (colored)
            return iconArray[data & 15];

        switch (data) {
            case 1:
                return Blocks.clay.getIcon(side, 0);
            default:
                return iconSide;
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getBottomIcon (int data) {
        if (!colored && data == 1)
            return Blocks.clay.getIcon(0, 0);
        else
            return iconSide;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon (int data) {
        if (iconOverlayArray[data] != null)
            return iconOverlayArray[data];

        return null;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        if (colored) {
            for (int i = 0; i < 16; i++)
                blockList.add(new ItemStack(item, 1, i));
        }
        else {
            blockList.add(new ItemStack(item, 1, 0));
            blockList.add(new ItemStack(item, 1, 1));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        iconSide = iconRegister.registerIcon(ModularPots.MOD_ID + ":large_pot");

        if (colored) {
            iconArray = new IIcon[16];
            for (int i = 0; i < 16; i++) {
                String colorName = ItemDye.field_150921_b[getBlockFromDye(i)];
                iconArray[i] = iconRegister.registerIcon(ModularPots.MOD_ID + ":large_pot_" + colorName);
            }
        }

        iconOverlayArray = new IIcon[256];
        for (int i = 1; i < iconOverlayArray.length; i++) {
            PatternConfig pattern = ModularPots.config.getPattern(i);
            if (pattern != null && pattern.getOverlay() != null)
                iconOverlayArray[i] = iconRegister.registerIcon(ModularPots.MOD_ID + ":" + pattern.getOverlay());
        }
    }

    public static int getBlockFromDye (int index) {
        return index & 15;
    }
}
