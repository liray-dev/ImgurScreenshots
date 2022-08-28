package muwwy.screen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import muwwy.screen.packets.PacketReply;
import muwwy.screen.packets.PacketRequest;

@Mod(modid = "screen", name = "Screen", version = "1.0")
public final class ImgurScreenshots {

    @Mod.Instance("screen")
    public static ImgurScreenshots instance;

    public static final String client_id;
    public static final SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        ImgurScreenshots.network.registerMessage(PacketRequest.Handler.class, PacketRequest.class, 0, Side.CLIENT);
        ImgurScreenshots.network.registerMessage(PacketReply.Handler.class, PacketReply.class, 1, Side.SERVER);
    }

    @Mod.EventHandler
    public void startServer(final FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandScreen());
    }

    static {
        client_id = "fcc705d24e5ea3e";  // Please change the 'client-id' first
        network = NetworkRegistry.INSTANCE.newSimpleChannel("screen");
    }

}
