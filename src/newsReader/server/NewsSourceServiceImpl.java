package newsReader.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import newsReader.client.Service.NewsSourceService;
import newsReader.client.DataModel.NewsHeading;
import newsReader.client.DataModel.NewsSource;
import newsReader.client.DataModel.NewsContent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import javax.servlet.ServletException;

public class NewsSourceServiceImpl extends RemoteServiceServlet implements NewsSourceService
{
	private static final long serialVersionUID = 1L;
	private HashMap<String, NewsStore> mapNewsStore = new HashMap<String, NewsStore>();
	private HashMap<String, NewsSource> mapNewsSource = new HashMap<String, NewsSource>();
    private Connection conn = null;
    private String url  = null;
    private String user = null;
    private String pass = null;
    private Timer timer;
   
    public void init() throws ServletException { 
        super.init(); 
        url = getServletConfig().getInitParameter("dbUrl"); 
        user = getServletConfig().getInitParameter("dbUser"); 
        pass = getServletConfig().getInitParameter("dbPass"); 
      }
    
    class DataLoaderTask extends TimerTask
    {
    	private Calendar cal;

    	public DataLoaderTask()
    	{
    	}
    	
    	public void run (  )
        {
        	cal = Calendar.getInstance();
        	int minute = cal.get(Calendar.MINUTE);
        	if (minute > 4)
        		return;
        	//Every hour
        	
        	//do this hourly
        	HashMap<String, NewsStore> mapNewsStoreTemp = new HashMap<String, NewsStore>();
        	HashMap<String, NewsSource> mapNewsSourceTemp = new HashMap<String, NewsSource>();
        	
			LoadData(mapNewsStoreTemp, mapNewsSourceTemp);

			if (!mapNewsStoreTemp.isEmpty() && !mapNewsSourceTemp.isEmpty())
			{
				mapNewsStore = mapNewsStoreTemp;
				mapNewsSource = mapNewsSourceTemp;
			}
        }
    }
    
	public NewsSourceServiceImpl()
	{
	    timer = new Timer (  ) ;
	    timer.schedule ( new DataLoaderTask() , 0, 5*60*1000 ) ; //every 5 minute
	}
	
	public List<NewsSource> getNewsSource() throws IllegalArgumentException
	{
		if (mapNewsStore.isEmpty())
		{
			LoadData(mapNewsStore, mapNewsSource);
		}
		
		//create list from the map and send
		List<NewsSource> listNewsSources = new ArrayList<NewsSource>();
		Iterator<Entry<String, NewsSource>> iterator = mapNewsSource.entrySet().iterator();
        while(iterator.hasNext())
        {   
        	listNewsSources.add(iterator.next().getValue());
        }
        return listNewsSources;

	}

	public void LoadData(HashMap<String, NewsStore> mapNewsStore, HashMap<String, NewsSource> mapNewsSource)
	{	    
	    try
        {
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection(url, user, pass);
            
            PreparedStatement ps = conn.prepareStatement("select * from NewsSource order by pos");                        
            ResultSet result = ps.executeQuery();	            
            NewsStore newsStore;
            NewsSource newsSource;
            String newsSourceName;
            PreparedStatement psNewsContent;
            ResultSet resultNewsContent;
            NewsArticle newsArticle;
                    
            //news sources
            while (result.next())
            {
            	newsSourceName = result.getString("NewsSourceName");
            	newsSource = new NewsSource();
            	newsSource.setSourceName(newsSourceName);
            	newsSource.setSourceDisplayName(result.getString("NewsSourceDisplayName"));
            	newsSource.setFontFace(result.getString("FontFace"));
            	mapNewsSource.put(newsSourceName, newsSource);

            	newsStore = new NewsStore();
            	String sql = "select * from " + newsSourceName + " order by " + newsSourceName + "_key";
	            psNewsContent = conn.prepareStatement(sql);                        
	            resultNewsContent = psNewsContent.executeQuery();

	            //no of content columns
            	int nContentCount = 1;
            	try
            	{
            		while (resultNewsContent.findColumn("Content"+nContentCount) > -1)
            			nContentCount++;
            	}
            	catch (Exception e)
            	{
            	}
            	
            	--nContentCount;//actual no of columns

            	//news articles of source
	            while (resultNewsContent.next())
	            {
	            	String heading = resultNewsContent.getString("Heading").trim();
	            	
	            	if ((heading == null) || (heading.isEmpty()))
	            		continue;
	            	
	            	//if we have the article just add content.
	            	newsArticle = newsStore.getArticle(heading);
	            	if (newsArticle != null)
	            	{
		            	String[] contentExtra = new String[nContentCount];
		            	for (int i = 0; i < nContentCount; i++)
		            	{
		            		contentExtra[i] = resultNewsContent.getString("Content"+ (i + 1));
		            	}
		            	
		            	newsArticle.addNewsContent(contentExtra);
	            		continue;
	            	}
	            	
	            	newsArticle = new NewsArticle();
	            	
	            	newsArticle.setNewsHeading(heading);
	            	newsArticle.setNewsURL(resultNewsContent.getString("Link"));
	            	newsArticle.setNewsImage(resultNewsContent.getString("Image"));
	            	newsArticle.setKey(resultNewsContent.getInt(newsSourceName + "_key"));
	            	
	            	String date = resultNewsContent.getString("Date");

	            	//set "subheading"
	            	
	            	//set "Content*"		            	
	            	String[] content = new String[nContentCount];
	            	for (int i = 0; i < nContentCount; i++)
	            	{
	            		
	            		if (date != null && !date.isEmpty() && i == 0)
	            			content[i] = "(" + date + ") " + resultNewsContent.getString("Content"+ (i + 1));
	            		else
	            			content[i] = resultNewsContent.getString("Content"+ (i + 1));
	            	}
	            	
	            	newsArticle.addNewsContent(content);
	            	
	            	newsStore.addNewsArticle(newsArticle);
	            }
	            
	            resultNewsContent.close();
	            psNewsContent.close();

            	mapNewsStore.put(newsSourceName, newsStore);		
            }

            result.close();
            ps.close();
        }
        catch (Exception e)
        {
			//Catch exception if any
			e.printStackTrace();
        }
   	}
	
	public List<NewsHeading> getNewsHeadings(String newsSourceName) throws IllegalArgumentException
	{
		NewsStore newsStore = mapNewsStore.get(newsSourceName);
		
		if (newsStore == null)
			return null;
		
		return newsStore.getHeadings();
	}
	
	public NewsContent getNewsContent(String newsSourceName, String newsHeading) throws IllegalArgumentException
	{
		NewsStore newsStore = mapNewsStore.get(newsSourceName);
		
		if (newsStore == null)
			return null;
		
		return newsStore.getContent(newsHeading);
	}
}
