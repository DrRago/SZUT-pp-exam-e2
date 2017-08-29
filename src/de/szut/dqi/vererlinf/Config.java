package de.szut.dqi.vererlinf;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.StrictConfigurationComparator;

import java.io.IOException;
import java.util.List;

/**
 * Reads and saves the configuration
 */
public class Config {
    public String rawUrl;
    public String modifiedUrl;

    private boolean filterPrimar;
    private boolean filterSek1;
    private boolean filterSek2a;
    private boolean filterSek2b;
    private String filterCategory;

    public String logPath;

    public String atomPath;

    public Config(String path) throws IOException {
        try {
            Configuration config = new PropertiesConfiguration(path);

            rawUrl = config.getString("url");

            filterPrimar = config.getBoolean("primar");
            filterSek1 = config.getBoolean("sek1");
            filterSek2a = config.getBoolean("sek2a");
            filterSek2b = config.getBoolean("sek2b");
            filterCategory = config.getString("category");

            modifiedUrl = addUrlModifiers(rawUrl);

            logPath = config.getString("logpath");
            atomPath = config.getString("atompath");

        } catch (ConfigurationException e) {
            Logger.logException(e);
        }
    }

    private String addUrlModifiers(String rawUrl) {
        String url = rawUrl + "?";

        if (filterPrimar) {
            url += "blnprimar=on&";
        }
        if (filterSek1) {
            url += "blnsek1=on&";
        }

        if (filterSek2a) {
            url += "blnsek2a=on&";
        }

        if (filterSek2b) {
            url += "blnsek2b=on&";
        }

        url += "Kategorie=" + filterCategory;

        return url;
    }
}
