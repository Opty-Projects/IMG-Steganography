package com.opty.steganography;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class LSBUnwrap {

    private final static String TXT_FILE = "UnwrappedMsg.txt";

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Missing Arg(s)!");
            System.out.printf("%s Args:%n", LSBUnwrap.class.getName());
            System.out.println("> Wrapper File");
            System.out.println("> Key File");
            System.out.printf("> Output Pathname of '%s'%n", TXT_FILE);
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.printf("args[%d]='%s'%n", i, args[i]);
        }

        try {
            BufferedImage pic = ImageIO.read(new File(args[0]));
            byte[] key = new byte[LSBLib.getKeyBytes(LSBLib.getMsgMaxBytes(pic))];
            int keyBytes = LSBLib.readFile(args[1], key);
            byte[] msg = new byte[LSBLib.getMsgBytes(keyBytes)];

            unwrapMsg(pic, msg, ByteBuffer.wrap(key, 0, keyBytes));
            new FileOutputStream(new File(args[2], TXT_FILE)).write(msg);
            System.out.println("Success!");

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid Key!");

        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
    }

    public static void unwrapMsg(BufferedImage pic, byte[] msg, ByteBuffer keyBuf) {

        int[] xyz = new int[3];
        int[] rgba = new int[4];
        int i = 0;

        keyBuf.rewind();
        while (keyBuf.hasRemaining()) {
            LSBLib.posToXYZ(pic, keyBuf.getInt(), xyz);
            LSBLib.colorToRGBA(pic.getRGB(xyz[0], xyz[1]), rgba);
            msg[i / 8] <<= 1;
            msg[i++ / 8] |= rgba[xyz[2]] & 1;
        }
    }
}
