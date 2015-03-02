package com.jaquadro.minecraft.gardencore.block.tile;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBlockMateralProxy extends TileEntity
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

    public int composeMetadata (Block block, int meta) {
        int id = GameData.getBlockRegistry().getId(block);
        return ((id & 0xFFF) << 4) | (meta & 0xF);
    }

    public Block getBlockFromComposedMetadata (int metadata) {
        if (metadata >= 16)
            return GameData.getBlockRegistry().getObjectById((metadata >> 4) & 0xFFF);

        return getBlockFromStandardMetadata(metadata);
    }

    protected Block getBlockFromStandardMetadata (int metadata) {
        return null;
    }

    public int getMetaFromComposedMetadata (int metadata) {
        if (metadata >= 16)
            return metadata & 0xF;

        return getMetaFromStandardMetadata(metadata);
    }

    protected int getMetaFromStandardMetadata (int metadata) {
        return metadata;
    }

    public void syncTileEntityWithData (World world, int x, int y, int z, int metadata) {
        if (metadata < 16) {
            world.removeTileEntity(x, y, z);
            return;
        }

        TileEntityBlockMateralProxy te = (TileEntityBlockMateralProxy) world.getTileEntity(x, y, z);
        if (te == null) {
            te = createTileEntity();
            world.setTileEntity(x, y, z, te);
        }

        Block block = getBlockFromComposedMetadata(metadata);
        int protoMeta = getMetaFromComposedMetadata(metadata);

        if (block != null)
            te.setProtoBlock(block, protoMeta);

        te.markDirty();
    }

    protected TileEntityBlockMateralProxy createTileEntity () {
        return new TileEntityBlockMateralProxy();
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
