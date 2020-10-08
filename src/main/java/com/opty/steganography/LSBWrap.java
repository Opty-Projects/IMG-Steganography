package com.opty.steganography;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class LSBWrap {

    public static void main(String[] args) {

        if (args.length < 4) {
            System.out.println("Missing Arg(s)!");
            System.out.printf("%s Args:%n", LSBWrap.class.getName());
            System.out.println("> Img File");
            System.out.println("> Msg File");
            System.out.println("> Key File");
            System.out.println("> Output Wrapper Img Pathname");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.printf("args[%d]='%s'%n", i, args[i]);
        }

        try {
            BufferedImage pic = ImageIO.read(new File(args[0]));
            byte[] msg = new byte[LSBKeyGenerator.getMsgMaxBytes(pic)];
            int msgBytes = readFile(args[1], msg);
            byte[] key = new byte[LSBKeyGenerator.getKeyBytes(msgBytes)];
            int keyBytes = readFile(args[2], key);
            if (keyBytes < key.length) throw new Exception("Invalid Key!");

            BufferedImage newPic = wrapMsg(pic, msg, ByteBuffer.wrap(key));
            ImageIO.write(newPic, "png", new File(args[3], "Wrapper.png"));
            System.out.println("Success!");

        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
    }

    public static int readFile(String pn, byte[] buf) throws Exception {
        int bufBytes = new FileInputStream(new File(pn)).read(buf);
        if (bufBytes < 0) throw new Exception(String.format("Cannot Read '%s'!", pn));
        return bufBytes;
    }

    public static BufferedImage wrapMsg(BufferedImage pic, byte[] msg, ByteBuffer keyBuf) {

        BufferedImage newPic = new BufferedImage(pic.getWidth(), pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int[] buf = pic.getRGB(0, 0, pic.getWidth(), pic.getHeight(), null, 0, pic.getWidth());
        newPic.setRGB(0, 0, pic.getWidth(), pic.getHeight(), buf, 0, pic.getWidth());

        int[] xyz = new int[3];
        int[] rgba = new int[4];
        int i = 0;

        keyBuf.rewind();
        while (keyBuf.hasRemaining()) {
            posToXYZ(newPic, keyBuf.getInt(), xyz);
            colorToRGBA(newPic.getRGB(xyz[0], xyz[1]), rgba);
            rgba[xyz[2]] &= 0xFE;
            rgba[xyz[2]] |= (msg[i / 8] >>> 7) & 1;
            newPic.setRGB(xyz[0], xyz[1], RGBAToColor(rgba));
            msg[i / 8] <<= 1;
            i++;
        }
        return newPic;
    }

    public static void posToXYZ(BufferedImage pic, int pos, int[] xyz) {
        xyz[0] = pos % (pic.getWidth() * 4);
        xyz[1] = pos / (pic.getWidth() * 4);
        xyz[2] = xyz[0] % 4;
        xyz[0] /= 4;
    }

    public static void colorToRGBA(int color, int[] rgba) {
        rgba[0] = color & 0xFF;
        rgba[1] = (color >>> 8) & 0xFF;
        rgba[2] = (color >>> 16) & 0xFF;
        rgba[3] = (color >>> 24) & 0xFF;
    }

    public static int RGBAToColor(int[] rgba) {
        return rgba[3] << 24 | rgba[2] << 16 | rgba[1] << 8 | rgba[0];
    }
}
