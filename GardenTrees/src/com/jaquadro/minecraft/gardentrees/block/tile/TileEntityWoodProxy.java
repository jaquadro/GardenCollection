package com.jaquadro.minecraft.gardentrees.block.tile;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityWoodProxy extends TileEntity
{
    private Block protoBlock;
    private int protoMeta;

    public Block getProtoBlock () {
        return protoBlock;
    }

    public int getProtoMeta () {
        return protoMeta;
    }

    public void setProtoBlock (Block block, int meta) {
        protoBlock = block;
        protoMeta = meta;
    }

    public static int composeMetadata (Block block, int meta) {
        int id = GameData.blockRegistry.getId(block);
        return (id & 0xFFF) | ((meta & 0xF) << 12);
    }

    public static Block getBlockFromComposedMetadata (int metadata) {
        if (metadata >= 16)
            return GameData.blockRegistry.get(metadata & 0xFFF);
        else if (metadata < 4)
            return Blocks.log;
        else if (metadata < 8)
            return Blocks.log2;
        else
            return null;
    }

    public static int getMetaFromComposedMetadata (int metadata) {
        if (metadata >= 16)
            return (metadata >> 12) & 0xF;
        else
            return metadata % 4;
    }

    public static void syncTileEntityWithData (World world, int x, int y, int z, int metadata) {
        if (metadata < 16) {
            world.removeTileEntity(x, y, z);
            return;
        }

        TileEntityWoodProxy te = (TileEntityWoodProxy) world.getTileEntity(x, y, z);
        if (te == null) {
            te = new TileEntityWoodProxy();
            world.setTileEntity(x, y, z, te);
        }

        Block block = getBlockFromComposedMetadata(metadata);
        int protoMeta = getMetaFromComposedMetadata(metadata);

        if (block != null)
            te.setProtoBlock(block, protoMeta);

        te.markDirty();
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (protoBlock != null) {
            tag.setInteger("P", getUnifiedProtoData());
        }
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("P"))
            unpackUnifiedProtoData(tag.getInteger("P"));
    }

    @Override
    public Packet getDescriptionPacket () {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 5, tag);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
        getWorldObj().func_147479_m(xCoord, yCoord, zCoord); // markBlockForRenderUpdate
    }

    private int getUnifiedProtoData () {
        return (Block.getIdFromBlock(protoBlock) & 0xFFFF) | ((protoMeta & 0xFFFF) << 16);
    }

    private void unpackUnifiedProtoData (int protoData) {
        protoBlock = Block.getBlockById(protoData & 0xFFFF);
        protoMeta = (protoData >> 16) & 0xFFFF;
    }
}
