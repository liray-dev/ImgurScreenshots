package muwwy.screen.util;

import com.google.common.io.Closeables;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import muwwy.screen.ImgurScreenshots;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import org.apache.commons.codec.binary.Base64;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.IntBuffer;

@SideOnly(Side.CLIENT)
public final class ClientUtils {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static String uploadToImgur(final byte[] imageBytes) {
        String replyJson;
        try {
            final String reply = Base64.encodeBase64String(imageBytes);
            final String data = URLEncoder.encode("image", "UTF-8") + '=' + URLEncoder.encode(reply, "UTF-8");
            final URL url = new URL("https://api.imgur.com/3/image");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Client-ID " + ImgurScreenshots.client_id);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            final StringBuilder replyBuilder = new StringBuilder();
            try (final OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream())) {
                out.write(data);
                out.flush();
                try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        replyBuilder.append(line).append('\n');
                    }
                }
            } finally {
                connection.disconnect();
            }
            replyJson = replyBuilder.toString();
        } catch (IOException var14) {
            var14.printStackTrace();
            return null;
        }
        final ReplyJson reply2 = (ReplyJson) CommonUtils.GSON.fromJson(replyJson, (Class) ReplyJson.class);
        return reply2.success ? reply2.data.link : null;
    }

    public static final byte[] getScreenshot() {
        final Minecraft mc = Minecraft.getMinecraft();
        int width = mc.displayWidth;
        int height = mc.displayHeight;
        final Framebuffer frameBuffer = mc.getFramebuffer();
        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                width = frameBuffer.framebufferTextureWidth;
                height = frameBuffer.framebufferTextureHeight;
            }
            final int exception = width * height;
            if (ClientUtils.pixelBuffer == null || ClientUtils.pixelBuffer.capacity() < exception) {
                ClientUtils.pixelBuffer = BufferUtils.createIntBuffer(exception);
                ClientUtils.pixelValues = new int[exception];
            }
            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            ClientUtils.pixelBuffer.clear();
            if (OpenGlHelper.isFramebufferEnabled()) {
                GL11.glBindTexture(3553, frameBuffer.framebufferTexture);
                GL11.glGetTexImage(3553, 0, 32993, 33639, ClientUtils.pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, width, height, 32993, 33639, ClientUtils.pixelBuffer);
            }
            ClientUtils.pixelBuffer.get(ClientUtils.pixelValues);
            TextureUtil.func_147953_a(ClientUtils.pixelValues, width, height);
            BufferedImage image = null;
            if (OpenGlHelper.isFramebufferEnabled()) {
                image = new BufferedImage(frameBuffer.framebufferWidth, frameBuffer.framebufferHeight, 1);
                int imageOut;
                for (int out = imageOut = frameBuffer.framebufferTextureHeight - frameBuffer.framebufferHeight; imageOut < frameBuffer.framebufferTextureHeight; ++imageOut) {
                    for (int writer = 0; writer < frameBuffer.framebufferWidth; ++writer) {
                        image.setRGB(writer, imageOut - out, ClientUtils.pixelValues[imageOut * frameBuffer.framebufferTextureWidth + writer]);
                    }
                }
            } else {
                image = new BufferedImage(width, height, 1);
                image.setRGB(0, 0, width, height, ClientUtils.pixelValues, 0, width);
            }
            final ByteArrayOutputStream var11 = new ByteArrayOutputStream();
            final MemoryCacheImageOutputStream var12 = new MemoryCacheImageOutputStream(var11);
            final ImageWriter var13 = ImageIO.getImageWritersByFormatName("jpeg").next();
            final ImageWriteParam iwp = var13.getDefaultWriteParam();
            iwp.setCompressionMode(2);
            iwp.setCompressionQuality(0.7f);
            var13.setOutput(var12);
            var13.write(null, new IIOImage(image, null, null), iwp);
            var13.dispose();
            Closeables.close((Closeable) var12, true);
            return var11.toByteArray();
        } catch (Exception var14) {
            var14.printStackTrace();
            return null;
        }
    }

    private static final class ReplyJson {
        public ReplyJsonData data;
        public boolean success;
        public int status;

        private static final class ReplyJsonData {
            public String link;
        }
    }
}
