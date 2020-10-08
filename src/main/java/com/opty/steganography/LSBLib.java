package com.opty.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.stream.IntStream;

public class LSBLib {

    public static ByteBuffer getRandomKey(BufferedImage pic, int msgBytes) throws Exception {
        int picBytes = getPicBytes(pic);
        if (msgBytes * 8 > picBytes) throw new Exception("Msg too Long!");

        ByteBuffer keyBuf = ByteBuffer.allocate(getKeyBytes(msgBytes));
        getRandomsInRange(picBytes, msgBytes * 8).forEach(keyBuf::putInt);
        return keyBuf;
    }

    public static IntStream getRandomsInRange(int range, int n) {
        return new Random().ints(0, range).distinct().limit(n);
    }

    public static int getPicBytes(BufferedImage pic) {
        return pic.getWidth() * pic.getHeight() * 4;
    }

    public static int getKeyBytes(int msgBytes) {
        return msgBytes * 8 * Integer.BYTES;
    }

    public static int getMsgBytes(int keyBytes) {
        return keyBytes / (8 * Integer.BYTES);
    }

    public static int getMsgMaxBytes(BufferedImage pic) {
        return getPicBytes(pic) / 8;
    }

    public static int readFile(String pn, byte[] buf) throws Exception {
        int bufBytes = new FileInputStream(new File(pn)).read(buf);
        if (bufBytes < 0) throw new Exception(String.format("Cannot Read '%s'!", pn));
        return bufBytes;
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
