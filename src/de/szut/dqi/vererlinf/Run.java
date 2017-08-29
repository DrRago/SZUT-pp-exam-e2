package de.szut.dqi.vererlinf;

import java.io.IOException;

/**
 * The basic run class
 */
public class Run {
    public static void main(String[] args) throws IOException {
        Config conf = new Config("config.properties");

        Logger.init(conf.logPath);
        try {
            Parser parser = new Parser(conf);

            AtomWriter.write(parser.data, conf.rawUrl, conf.atomPath);

        } catch (Exception e) {
            Logger.logException(e);
        }
        Logger.exit();

    }
}
