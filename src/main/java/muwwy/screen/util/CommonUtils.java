package muwwy.screen.util;

import com.google.gson.Gson;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public final class CommonUtils {
    public static final Gson GSON;
    private static String hostIp;

    public static ChatComponentText setColor(final String text, final EnumChatFormatting color) {
        return (ChatComponentText) setColor((IChatComponent) new ChatComponentText(text), color);
    }

    public static IChatComponent setColor(final IChatComponent chatComponent, final EnumChatFormatting color) {
        chatComponent.getChatStyle().setColor(color);
        return chatComponent;
    }

    public static IChatComponent setItalic(final IChatComponent chatComponent, final boolean italic) {
        chatComponent.getChatStyle().setItalic(italic);
        return chatComponent;
    }

    public static IChatComponent setClickAction(final IChatComponent chatComponent, final ClickEvent.Action action, final String value) {
        chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(action, value));
        return chatComponent;
    }

    public static final IChatComponent setHoverAction(final IChatComponent chatComponent, final HoverEvent.Action action, final IChatComponent value) {
        chatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(action, value));
        return chatComponent;
    }

    public static String getHostIp() throws Throwable {
        if (CommonUtils.hostIp == null) {
        }
        return CommonUtils.hostIp;
    }

    static {
        GSON = new Gson();
    }
}
