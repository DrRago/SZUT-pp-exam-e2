package de.szut.dqi.vererlinf;

import com.rometools.rome.feed.atom.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedOutput;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Writes the data parsed from the website into a atom file
 */
public class AtomWriter {
    public static void write(Elements data, String baseURL, String path) throws IOException {
        // Feed
        Feed feed = new Feed();
        feed.setFeedType("atom_1.0");

        feed.setTitle("Verfügungen, Erlasse und Infoschreiben der Senatorin für Bildung");
        feed.setUpdated(Calendar.getInstance().getTime() /*now*/);

        Content feedSubtitle = new Content();
        feedSubtitle.setValue("Verfügungen, Elasse und Infoschreiben");
        feed.setSubtitle(feedSubtitle);

        Person author = new Person();
        author.setName("Leonhard Gahr");
        author.setEmail("leonhard.gahr@gmail.com");
        feed.setAuthors(Collections.singletonList(author));

        // Link auf das Feeddokument
        Link feedLinkSelf = new Link();
        feedLinkSelf.setHref("http://example.com/myfeed.atom");
        feedLinkSelf.setRel("self");
        feed.setOtherLinks(Collections.singletonList(feedLinkSelf));

        // Link auf die zugehörige Website
        Link feedLinkAlternate = new Link();
        feedLinkAlternate.setHref("https://www.bildung.bremen.de/verfuegungen__erlasse__infoschreiben-4566");
        feedLinkAlternate.setRel("alternate");
        feed.setAlternateLinks(Collections.singletonList(feedLinkAlternate));

        feed.setId("Veröffentlichungen");
        feed.setLanguage("de-de");

        Content description = new Content();
        description.setType("text/plain");
        description.setValue("Veröffentlichungen der Senatoring für Bildung");
        feed.setInfo(description);

        List<Entry> entries = new ArrayList<>();

        int id = 0;

        for (Element writeData : data) {
            Elements tableData = writeData.select("td");
            Calendar published = Calendar.getInstance();

            String date = tableData.get(0).text();
            int year = Integer.parseInt(date.substring(6, 10));
            int month = Integer.parseInt(date.substring(3, 5));
            int day = Integer.parseInt(date.substring(0, 2));

            published.set(year, month, day);

            Element entryDescrition = tableData.get(1).select("a").first();
            String entryTitle = entryDescrition.html().substring(0, entryDescrition.html().indexOf("<br>"));

            // Entry
            Entry entry = new Entry();
            entry.setPublished(published.getTime());
            entry.setTitle(entryTitle);
            entry.setId("FeedEntry" + id);

            List<Link> linkList = new ArrayList<>();

            Element attachmentElement = tableData.get(2).select("a").first();

            String attachmentHref = "";

            if (!attachmentElement.attr("href").equals("Keine")) {
                attachmentHref = baseURL + attachmentElement.attr("href");
                Link entryLinkEnclosure = new Link();
                entryLinkEnclosure.setHref(attachmentHref);
                entryLinkEnclosure.setType("file/pdf");
                entryLinkEnclosure.setRel("enclosure");

                linkList.add(entryLinkEnclosure);
            }

            String articleURL = baseURL + entryDescrition.attr("href");

            Link articleLink = new Link();
            articleLink.setHref(articleURL);
            articleLink.setTitle("file/pdf");
            articleLink.setRel("article");

            linkList.add(articleLink);

            entry.setOtherLinks(linkList);

            Content entrySummary = new Content();
            entrySummary.setValue("Dokument: " + articleURL + ", Anhang: " + attachmentHref);
            entry.setSummary(entrySummary);

            entries.add(entry);

            id ++;
        }

        feed.setEntries(entries);

        // Write
        File doc = new File(path);
        try {
            if (doc.exists()) {
                doc.delete();
            }
            doc.createNewFile();
            WireFeedOutput wfo = new WireFeedOutput();
            wfo.output(feed, doc);
        } catch (IOException | FeedException e) {
           Logger.logException(e);
        }
    }
}
