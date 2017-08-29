package de.szut.dqi.vererlinf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

/**
 * The Parser parses the website given in the config file
 */
public class Parser {
    Elements data;


    public Parser(Config conf) throws IOException {

        System.setProperty("jsse.enableSNIExtension", "false");

        Logger.loggedPrint("Parsing website...");

        data = getRows(conf.modifiedUrl, 0);
    }

    /**
     * Get every tableRow of every page that exists with the current filters recursively
     */
    private Elements getRows(String url, int skip) throws IOException {
        final Document root_document = Jsoup.connect(url + "&skip=" + skip).get();

        final Elements table = root_document.select(".bildboxtable");

        // break condition for recursive function
        if (table.isEmpty()) {
            Logger.loggedPrint("Parsing finished at page " + skip / 10);
            // return empty Elements-object to not cause a nullPointerException for the addAll method for tempRows
            return new Elements();
        }

        Logger.loggedPrint("Parsing page " + skip / 10 + " ...");

        Elements tempRows = new Elements();

        for(Element tableRow : table.select("tr")) {
            // skip the table head
            if (Objects.equals(tableRow.id(), "t1")) {
                continue;
            }
            tempRows.add(tableRow);
        }

        // recursively go through every page until the table doesn't exist anymore (see break condition above)
        tempRows.addAll(getRows(url, skip + 10));
        return tempRows;
    }
}
