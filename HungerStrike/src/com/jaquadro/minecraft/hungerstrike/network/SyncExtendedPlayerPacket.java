package com.jaquadro.minecraft.hungerstrike.network;

import com.jaquadro.minecraft.hungerstrike.ExtendedPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SyncExtendedPlayerPacket extends AbstractPacket
{
    private NBTTagCompound data;

    public SyncExtendedPlayerPacket () { }

    public SyncExtendedPlayerPacket (EntityPlayer player) {
        data = new NBTTagCompound();
        ExtendedPlayer ep = ExtendedPlayer.get(player);
        if (ep != null)
            ep.saveNBTDataSync(data);
    }

    @Override
    public void encodeInto (ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, data);
    }

    @Override
    public void decodeInto (ChannelHandlerContext ctx, ByteBuf buffer) {
        data = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void handleClientSide (EntityPlayer player) {
        ExtendedPlayer ep = ExtendedPlayer.get(player);
        if (ep != null)
            ep.loadNBTData(data);
    }

    @Override
    public void handleServerSide (EntityPlayer player) { }
}
