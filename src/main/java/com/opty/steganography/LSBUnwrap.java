package com.opty.steganography;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class LSBUnwrap {

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Missing Arg(s)!");
            System.out.printf("%s Args:%n", LSBUnwrap.class.getName());
            System.out.println("> Wrapper Img File");
            System.out.println("> Key File");
            System.out.println("> Output Msg Pathname");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.printf("args[%d]='%s'%n", i, args[i]);
        }

        try {
            BufferedImage pic = ImageIO.read(new File(args[0]));
            byte[] key = new byte[LSBKeyGenerator.getKeyBytes(LSBKeyGenerator.getMsgMaxBytes(pic))];
            int keyBytes = LSBWrap.readFile(args[1], key);
            byte[] msg = new byte[LSBKeyGenerator.getMsgBytes(keyBytes)];
            Arrays.fill(msg, (byte)0);

            unwrapMsg(pic, msg, ByteBuffer.wrap(key, 0, keyBytes));
            new FileOutputStream(new File(args[2], "UnwrappedMsg.txt")).write(msg);
            System.out.println("Success!");

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
            LSBWrap.posToXYZ(pic, keyBuf.getInt(), xyz);
            LSBWrap.colorToRGBA(pic.getRGB(xyz[0], xyz[1]), rgba);
            msg[i / 8] <<= 1;
            msg[i / 8] |= rgba[xyz[2]] & 1;
            i++;
        }
    }
}
