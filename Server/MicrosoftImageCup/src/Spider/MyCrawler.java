package Spider;

import java.util.Set;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private Set<String> urls = new HashSet<String>();
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp3|zip|gz))$");

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
         String href = url.getURL().toLowerCase();
         return !FILTERS.matcher(href).matches() && href.startsWith("https://book.douban.com/");//https://book.douban.com/
         //return !FILTERS.matcher(href).matches() && href.contains("sports.sina.com.cn/");
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         System.out.println("URL: " + url);

         if (page.getParseData() instanceof HtmlParseData) {
             //HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             //String text = htmlParseData.getText();
             //String html = htmlParseData.getHtml();
             //Set<WebURL> links = htmlParseData.getOutgoingUrls();
             
             //System.out.println("Text length111111: " + text.length());
             //System.out.println("Html length: " + html.length());
             //System.out.println("Number of outgoing links: " + links.size());
             urls.add(url);
         }
    }
     
     @Override
     public void onBeforeExit() {
         Set<String> rsSet = bookUrlFilter(urls);
         if(rsSet.size()!=0){
        	 for(String each:rsSet){
            	 System.out.println("Result "+each);
            	 try {
            		 
    				JsoupParseUrl.parseUrl(each);
    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
             }
         }
         
         
         
     }
     
    // URL filter function that get books' url
    public Set<String> bookUrlFilter(Set<String> initUrls){
    	Set<String> rsSet = new HashSet<String>();
    	for(String each:initUrls){
    		//System.out.println("这个是在urlFilter里："+ each);
    		// "https://book.douban.com/subject/[1-9]{1,}/"  
    		//"http://sports.sina.com.cn/z/[a-zA-Z_]{1,}/$"
    		//"https://book.douban.com/subject/[1-9]{1,}/(\\?icn=index\\-[a-z]{1,}\\-subject){0,1}"
    		
    		
    		String regex = "^(https://book.douban.com/subject/[1-9]{1,}/).*"; 
    		
    		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(each);
    		boolean result = matcher.matches();
    		//System.out.println(result + "  "+ each);
    		
    		if(result){
    			rsSet.add(each);
    		}else{
    			
    		}
    	}
    	
    	return rsSet;
    }
     
}