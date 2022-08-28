package muwwy.screen.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import muwwy.screen.util.CommonUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.text.SimpleDateFormat;

public final class PacketReply implements IMessage {

    private int id;
    private String url;

    public PacketReply() {

    }

    public PacketReply(final int id, final String url) {
        this.id = id;
        this.url = url;
    }

    public void fromBytes(final ByteBuf buf) {
        this.id = buf.readInt();
        this.url = ByteBufUtils.readUTF8String(buf);
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.url);
    }

    public static final class Handler implements IMessageHandler<PacketReply, IMessage> {
        public IMessage onMessage(final PacketReply packet, final MessageContext ctx) {
            if (packet.id == 715) {
                System.out.println(packet.url);
                return null;
            }
            EntityPlayerMP playerMP = (EntityPlayerMP) MinecraftServer.getServer().getEntityWorld().getEntityByID(packet.id);

            ChatComponentText text = new ChatComponentText(packet.url);
            CommonUtils.setItalic(text, true);
            CommonUtils.setColor(text, EnumChatFormatting.GOLD);
            CommonUtils.setClickAction(text, ClickEvent.Action.OPEN_URL, packet.url);
            playerMP.addChatComponentMessage(text);

            return null;
        }
    }
}
