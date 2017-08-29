package de.szut.dqi.vererlinf;

import java.io.*;

/**
 * Logs the activity of the program if the methods are called
 */
public class Logger {
    private static FileOutputStream fos;
    public static void init(String fileName) throws IOException {
        fos = new FileOutputStream(fileName);
    }

    public static void loggedPrint(Object e) throws IOException {
        fos.write(e.toString().getBytes());
        fos.write(System.getProperty("line.separator").getBytes());
        System.out.println(e.toString());
    }

    public static void logException(Exception e) throws IOException {
        e.printStackTrace();
        fos.write(e.getClass().toString().getBytes());
        for (int i = 0; i < e.getStackTrace().length; i ++) {
            fos.write(System.getProperty("line.separator").getBytes());
            fos.write("\t".getBytes());
            fos.write(e.getStackTrace()[i].toString().getBytes());
        }
        System.exit(-1);
    }

    public static void exit() throws IOException {
        fos.close();
    }
}
