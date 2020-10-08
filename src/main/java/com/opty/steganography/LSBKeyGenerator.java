package com.opty.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.stream.IntStream;

public class LSBKeyGenerator {

    public static void main(String[] args) {

        if (args.length < 4) {
            System.out.println("Missing Arg(s)!");
            System.out.printf("%s Args:%n", LSBKeyGenerator.class.getName());
            System.out.println("> Min Img Width");
            System.out.println("> Min Img Height");
            System.out.println("> Number of Bytes in Msg");
            System.out.println("> Output Key Pathname");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.printf("args[%d]='%s'%n", i, args[i]);
        }

        try {
            int picBytes = getPicBytes(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            int msgBytes = Integer.parseInt(args[2]);
            if (msgBytes * 8 > picBytes) throw new Exception("Msg too Long!");

            ByteBuffer keyBuf = ByteBuffer.allocate(getKeyBytes(msgBytes));
            getRandomsInRange(picBytes, msgBytes * 8).forEach(keyBuf::putInt);
            new FileOutputStream(new File(args[3], "LSB.key")).write(keyBuf.array());
            System.out.println("Success!");

        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
    }

    public static int getPicBytes(int w, int h) {
        return w * h * 4;
    }

    public static int getMsgMaxBytes(BufferedImage pic) {
        return getPicBytes(pic.getWidth(), pic.getHeight()) / 8;
    }

    public static int getKeyBytes(int msgBytes) {
        return msgBytes * 8 * Integer.BYTES;
    }

    public static int getMsgBytes(int keyBytes) {
        return keyBytes / (8 * Integer.BYTES);
    }

    public static IntStream getRandomsInRange(int range, int n) {
        return new Random().ints(0, range).distinct().limit(n);
    }
}
