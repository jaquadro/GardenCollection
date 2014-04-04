package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.item.ItemUsedSoilKit;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

import static com.jaquadro.minecraft.modularpots.block.LargePot.Direction.*;

public class LargePot extends BlockContainer
{
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
    private Icon iconSide;

    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;

    public LargePot (int id, boolean colored) {
        super(id, Material.clay);

        this.colored = colored;
        this.setCreativeTab(ModularPots.tabModularPots);
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
    public boolean isBlockSolidOnSide (World world, int x, int y, int z, ForgeDirection side) {
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
    public boolean canSustainPlant (World world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te == null || te.getSubstrate() == null)
            return false;

        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        Block plant = Block.blocksList[plantable.getPlantID(world, x, y + 1, z)];
        Block substrate = Block.blocksList[te.getSubstrate().itemID];

        if (plant == Block.cactus)
            return substrate == Block.sand;

        return plantType == EnumPlantType.Crop && substrate == Block.tilledField;
    }

    @Override
    public boolean isFertile (World world, int x, int y, int z) {
        return true;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, int blockId) {
        if (world.isRemote)
            return;

        if (y >= world.getHeight() - 1)
            return;

        if (world.isAirBlock(x, y, z)) {
            TileEntityLargePot tileEntity = getTileEntity(world, x, y, z);
            if (tileEntity != null) {
                tileEntity.setItem(null, 0);
                tileEntity.invalidate();
            }
        }
    }

    public boolean isCompatibleNeighbor (IBlockAccess world, int x, int y, int z, int dx, int dz) {
        Block block = Block.blocksList[world.getBlockId(x + dx, y, z + dz)];
        if (block != this)
            return false;
        if (world.getBlockMetadata(x, y, z) != world.getBlockMetadata(x + dx, y, z + dz))
            return false;

        LargePot pot = (LargePot) block;
        TileEntityLargePot teThis = getTileEntity(world, x, y, z);
        TileEntityLargePot teThat = getTileEntity(world, x + dx, y, z + dz);
        if (teThis == null || teThat == null)
            return false;

        if (teThis.getSubstrate() != teThat.getSubstrate() || teThis.getSubstrateData() != teThat.getSubstrateData())
            return false;

        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, int blockID, int data) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te != null && te.getSubstrate() != null) {
            Block substrate = Block.blocksList[te.getSubstrate().itemID];
            if (substrate != Block.waterStill) {
                if (substrate == Block.tilledField)
                    substrate = Block.dirt;

                ItemStack item = new ItemStack(substrate, 1, te.getSubstrateOriginalData());
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, item));
            }
        }

        if (te != null && te.getFlowerPotItem() != null) {
            ItemStack item = new ItemStack(te.getFlowerPotItem(), 1, te.getFlowerPotData());
            world.spawnEntityInWorld(new EntityItem(world, x, y, z, item));
        }

        super.breakBlock(world, x, y, z, blockID, data);
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
            world.setBlockTileEntity(x, y, z, tileEntity);
        }

        IPlantable plantable = null;
        Item item = itemStack.getItem();
        if (item instanceof IPlantable)
            plantable = (IPlantable) item;
        else if (item instanceof ItemBlock) {
            Block itemBlock = Block.blocksList[((ItemBlock) item).getBlockID()];
            if (itemBlock instanceof IPlantable)
                plantable = (IPlantable) itemBlock;
        }

        if (tileEntity.getSubstrate() == null && isValidSubstrate(item)) {
            if (item == Item.bucketWater) {
                addSubstrate(tileEntity, Item.itemsList[Block.waterStill.blockID], 0);
                if (!player.capabilities.isCreativeMode)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Item.bucketWater));
            }
            else {
                addSubstrate(tileEntity, itemStack.getItem(), itemStack.getItemDamage());
                if (!player.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            world.markBlockForUpdate(x, y, z);
        }
        else if (tileEntity.getSubstrate() != null && item == Item.bucketWater) {
            if (tileEntity.getSubstrate() instanceof ItemBlock && Block.blocksList[((ItemBlock) tileEntity.getSubstrate()).getBlockID()] == Block.dirt) {
                tileEntity.setSubstrate(Item.itemsList[Block.tilledField.blockID], 1, tileEntity.getSubstrateData());
                world.markBlockForUpdate(x, y, z);
            }
        }
        else if (tileEntity.getSubstrate() != null && item == ModularPots.soilTestKitUsed) {
            applyTestKit(world, x, y, z, itemStack);
        }
        else if (plantable != null && canSustainPlantActivated(world, x, y, z, plantable)) {
            if (!enoughAirAbove(world, x, y, z, plantable))
                return false;

            // TODO: Non-compliant IPlantable, use config
            Block itemBlock = Block.blocksList[plantable.getPlantID(world, x, y, z)];
            if (itemBlock == null && plantable instanceof Block)
                itemBlock = (Block) plantable;

            world.setBlock(x, y + 1, z, ModularPots.largePotPlantProxy.blockID, itemStack.getItemDamage(), 3);

            tileEntity.setItem(itemStack.getItem(), itemStack.getItemDamage());
            world.markBlockForUpdate(x, y, z);

            if (!player.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
        else
            return false;

        return true;
    }

    public boolean applyTestKit (World world, int x, int y, int z, ItemStack testKit) {
        if (testKit.getItem() != ModularPots.soilTestKitUsed)
            return false;

        TileEntityLargePot tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity == null)
            return false;

        Block substrate = Block.blocksList[tileEntity.getSubstrate().itemID];
        if (substrate != Block.dirt && substrate != Block.grass && substrate != Block.tilledField)
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

    @Override
    public int getRenderBlockPass () {
        return 0;
    }

    private boolean enoughAirAbove (World world, int x, int y, int z, IPlantable plant) {
        Block plantBlock = Block.blocksList[plant.getPlantID(world, x, y + 1, z)];
        int height = 1;

        boolean enough = true;
        for (int i = 0; i < height; i++)
            enough &= world.isAirBlock(x, y + 1 + i, z);

        return enough;
    }

    private void addSubstrate (TileEntityLargePot tileEntity, Item item, int data) {
        tileEntity.setSubstrate(item, data);
        //tileEntity.invalidate();
    }

    private boolean isValidSubstrate (Item item) {
        if (item == Item.bucketWater)
            return true;

        if (item.itemID >= Block.blocksList.length)
            return false;

        Block block = Block.blocksList[item.itemID];
        if (block == null)
            return false;

        return block == Block.dirt
            || block == Block.sand
            || block == Block.gravel
            || block == Block.slowSand
            || block == Block.grass
            || block == Block.waterStill;
    }

    private boolean isSubstrateSolid (Item item) {
        Block block = Block.blocksList[item.itemID];
        return block != Block.waterStill;
    }

    private boolean canSustainPlantActivated (World world, int x, int y, int z, IPlantable plantable) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te == null || te.getSubstrate() == null)
            return false;

        Block substrate = Block.blocksList[te.getSubstrate().itemID];
        if (substrate == null)
            return false;

        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        Block plant = Block.blocksList[plantable.getPlantID(world, x, y + 1, z)];

        // TODO: Non-compliant IPlantable, use config
        if (plantType == null)
            plantType = EnumPlantType.Plains;

        if (plant == Block.cactus)
            return false;

        switch (plantType) {
            case Desert:
                return substrate == Block.sand;
            case Nether:
                return substrate == Block.slowSand;
            //case Crop:
            //    return substrate == Blocks.dirt || substrate == Blocks.farmland;
            case Cave:
                return true;
            case Plains:
                return substrate == Block.grass || substrate == Block.dirt;
            case Beach:
                return substrate == Block.grass || substrate == Block.dirt || substrate == Block.sand;
            case Water:
                return substrate == Block.waterStill;
            default:
                return false;
        }
    }

    public TileEntityLargePot getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        return (tileEntity != null && tileEntity instanceof  TileEntityLargePot) ? (TileEntityLargePot) tileEntity : null;
    }

    @Override
    public TileEntity createNewTileEntity (World world) {
        return new TileEntityLargePot();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon (int side, int data) {
        if (colored)
            return iconArray[data % 16];

        return iconSide;
    }

    @Override
    public void getSubBlocks (int itemId, CreativeTabs creativeTabs, List blockList) {
        if (colored) {
            for (int i = 0; i < 16; i++)
                blockList.add(new ItemStack(itemId, 1, i));
        }
        else
            blockList.add(new ItemStack(itemId, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IconRegister iconRegister) {
        iconSide = iconRegister.registerIcon(ModularPots.MOD_ID + ":large_pot");

        if (colored) {
            iconArray = new Icon[16];
            for (int i = 0; i < 16; i++) {
                String colorName = ItemDye.dyeItemNames[getBlockFromDye(i)];
                iconArray[i] = iconRegister.registerIcon(ModularPots.MOD_ID + ":large_pot_" + colorName);
            }
        }
    }

    public static int getBlockFromDye (int index) {
        return index & 15;
    }
}
