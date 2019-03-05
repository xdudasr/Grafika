package cv1.util;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL21.GL_SRGB;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureFactory {
    public static int loadCubeMap(String[] filenames) throws IOException {
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);

        for (int i = 0; i < filenames.length; i++) {
            InputStream is = TextureFactory.class.getResourceAsStream(filenames[i]);
            BufferedImage image = ImageIO.read(is);
            byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

            int internalFormat;
            int format;
            switch (image.getType()) {
                case BufferedImage.TYPE_3BYTE_BGR:
                    internalFormat = GL_SRGB;
                    format = GL_BGR;
                    break;
                case BufferedImage.TYPE_4BYTE_ABGR:
                    internalFormat = GL_SRGB_ALPHA;
                    format = GL_BGRA;
                    pixels = toBGRA(pixels);
                    break;
                default:
                    throw new IOException("Unknown image type: " + image.getType());
            }
            ByteBuffer textureData = BufferUtils.createByteBuffer(pixels.length);
            textureData.put(pixels);
            textureData.rewind();

            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, internalFormat, image.getWidth(),
                    image.getHeight(), 0, format, GL_UNSIGNED_BYTE, textureData);
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        return textureId;
    }

    public static int loadTexture(String filename) throws IOException {
        InputStream is = TextureFactory.class.getResourceAsStream(filename);
        BufferedImage image = ImageIO.read(is);
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        int internalFormat;
        int format;
        switch (image.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
                internalFormat = GL_SRGB;
                format = GL_BGR;
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                internalFormat = GL_SRGB_ALPHA;
                format = GL_BGRA;
                pixels = toBGRA(pixels);
                break;
            default:
                throw new IOException("Unknown image type: " + image.getType());
        }

        ByteBuffer textureData = BufferUtils.createByteBuffer(pixels.length);
        textureData.put(pixels);
        textureData.rewind();

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, image.getWidth(), image.getHeight(), 0, format, GL_UNSIGNED_BYTE, textureData);
        glGenerateMipmap(GL_TEXTURE_2D);


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//         unbind texture
        glBindTexture(GL_TEXTURE_2D, 0);

        return texture;
    }

    private static byte[] toBGRA(byte[] abgr) {
        byte[] bgra = new byte[abgr.length];
        for (int i = 0; i < abgr.length; i += 4) {
            bgra[i] = abgr[i + 1];
            bgra[i + 1] = abgr[i + 2];
            bgra[i + 2] = abgr[i + 3];
            bgra[i + 3] = abgr[i];
        }
        return bgra;
    }
}
