package muwwy.screen;

import muwwy.screen.packets.PacketRequest;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandScreen extends CommandBase {

    @Override
    public String getCommandName() {
        return "screen";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/screen [player name]";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        EntityPlayerMP player = (EntityPlayerMP) MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(strings[0]);
        EntityPlayerMP admin = (EntityPlayerMP) MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(iCommandSender.getCommandSenderName());
        if (player == null) {
            getCommandUsage(iCommandSender);
            return;
        }
        if (admin == null) {
            ImgurScreenshots.network.sendTo(new PacketRequest(715), player);
        } else
            ImgurScreenshots.network.sendTo(new PacketRequest(admin.getEntityId()), player);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_addTabCompletionOptions_1_, String[] strings) {
        return (strings.length == 1) ? getListOfStringsFromIterableMatchingLastWord(strings,  Arrays.stream(MinecraftServer.getServer().getAllUsernames()).collect(Collectors.toList())) : null;
    }
}
