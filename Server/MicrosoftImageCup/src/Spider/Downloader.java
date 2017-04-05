package Spider;

import org.apache.http.HttpStatus;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;


public class Downloader {
    

    private final Parser parser;
    private final PageFetcher pageFetcher;

    public Downloader() throws InstantiationException, IllegalAccessException {
        CrawlConfig config = new CrawlConfig(); // init crawler's config
        config.setUserAgentString("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        parser = new Parser(config); // init parser
        pageFetcher = new PageFetcher(config); // init page fetcher
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Downloader downloader = new Downloader();
        downloader.processUrl("https://book.douban.com/subject/26953532/");
        
    }

    public void processUrl(String url) {
        Page page = download(url);
        if (page != null) {
            ParseData parseData = page.getParseData(); // get Parse data
            if (parseData != null) {
                if (parseData instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) parseData;
                    System.out.println("BookName: "+htmlParseData.getTitle());
                    String html = htmlParseData.getHtml();
                    System.out.println("html:  "+html);
                    //System.out.println("标题: " + htmlParseData.getTitle());
                    //logger.debug("Title: {}", htmlParseData.getTitle());
                    //logger.debug("Text length: {}", htmlParseData.getText().length());
                    //logger.debug("Html length: {}", htmlParseData.getHtml().length());
                }
            } else {
            	System.out.println("Couldn't parse the content of the page.");
            }
        } else {
        	System.out.println("Couldn't fetch the content of the page.");
        }
    }

    private Page download(String url) {
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        PageFetchResult fetchResult = null;
        try {
            fetchResult = pageFetcher.fetchPage(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                Page page = new Page(curURL);
                fetchResult.fetchContent(page); //pageFetcher.getConfig().getMaxDownloadSize()
                parser.parse(page, curURL.getURL());
                return page;
            }
        } catch (Exception e) {
           System.out.println("Error occurred while fetching url: " + curURL.getURL()+", "+e);
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        return null;
    }
}



