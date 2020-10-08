package com.opty.steganography;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class LSBWrap {

    private final static String PNG_FILE = "Wrapper.png";
    private final static String KEY_FILE = "LSB.key";

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Missing Arg(s)!");
            System.out.printf("%s Args:%n", LSBWrap.class.getName());
            System.out.println("> Img File");
            System.out.println("> Msg File");
            System.out.printf("> Output Pathname of '%s' & '%s'%n", PNG_FILE, KEY_FILE);
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.printf("args[%d]='%s'%n", i, args[i]);
        }

        try {
            BufferedImage pic = ImageIO.read(new File(args[0]));
            byte[] msg = new byte[LSBLib.getMsgMaxBytes(pic)];
            int msgBytes = LSBLib.readFile(args[1], msg);
            ByteBuffer keyBuf = LSBLib.getRandomKey(pic, msgBytes);

            BufferedImage newPic = wrapMsg(pic, msg, keyBuf);
            ImageIO.write(newPic, "png", new File(args[2], PNG_FILE));
            new FileOutputStream(new File(args[2], KEY_FILE)).write(keyBuf.array());
            System.out.println("Success!");

        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
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
            LSBLib.posToXYZ(newPic, keyBuf.getInt(), xyz);
            LSBLib.colorToRGBA(newPic.getRGB(xyz[0], xyz[1]), rgba);
            rgba[xyz[2]] &= 0xFE;
            rgba[xyz[2]] |= (msg[i / 8] >>> 7) & 1;
            newPic.setRGB(xyz[0], xyz[1], LSBLib.RGBAToColor(rgba));
            msg[i++ / 8] <<= 1;
        }
        return newPic;
    }
}
