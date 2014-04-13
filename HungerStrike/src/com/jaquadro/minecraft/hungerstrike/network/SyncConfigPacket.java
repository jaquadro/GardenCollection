package com.jaquadro.minecraft.hungerstrike.network;

import com.jaquadro.minecraft.hungerstrike.ConfigManager;
import com.jaquadro.minecraft.hungerstrike.HungerStrike;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

public class SyncConfigPacket extends AbstractPacket
{
    private NBTTagCompound data;

    public SyncConfigPacket () {
        data = new NBTTagCompound();
        data.setTag("mode", new NBTTagString(HungerStrike.config.getMode().toString()));
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
        if (data.hasKey("mode")) {
            String mode = data.getString("mode");
            HungerStrike.config.setModeSoft(ConfigManager.Mode.valueOf(mode));
        }
    }

    @Override
    public void handleServerSide (EntityPlayer player) { }
}
