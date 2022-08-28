// 
// Decompiled by Procyon v0.5.36
// 

package muwwy.screen.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import muwwy.screen.ImgurScreenshots;
import muwwy.screen.util.ClientUtils;

public final class PacketRequest implements IMessage {

    private int id;

    public PacketRequest() {
    }

    public PacketRequest(final int id) {
        this.id = id;
    }

    public void fromBytes(final ByteBuf buf) {
        this.id = buf.readInt();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.id);
    }

    public static final class Handler implements IMessageHandler<PacketRequest, IMessage> {
        public PacketReply onMessage(final PacketRequest packet, final MessageContext ctx) {
            final byte[] bytes = ClientUtils.getScreenshot();
            final String url = ClientUtils.uploadToImgur(bytes);
            if (url != null) {
                ImgurScreenshots.network.sendToServer(new PacketReply(packet.id, url));
            }
            return null;
        }
    }
}
