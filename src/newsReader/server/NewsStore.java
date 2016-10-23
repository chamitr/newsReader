package newsReader.server;

import java.util.HashMap;
import java.util.List;
import newsReader.client.DataModel.NewsHeading;
import newsReader.client.DataModel.NewsContent;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map.Entry;

public class NewsStore
{
	private HashMap<String, NewsArticle> mapArticles;

	public NewsStore()
	{
		mapArticles = new HashMap<String, NewsArticle>();
	}
	
	public void addNewsArticle(NewsArticle newsArticle)
	{
		String key = newsArticle.getNewsHeading();
		mapArticles.put(key, newsArticle);
	}
	
	public List<NewsHeading> getHeadings()
	{
		List<NewsHeading> listHeadings = new ArrayList<NewsHeading>();
		Iterator<Entry<String, NewsArticle>> iterator = mapArticles.entrySet().iterator();
		NewsHeading newsHeading;
		NewsArticle newsArticle;
        while(iterator. hasNext())
        {   
        	newsHeading = new NewsHeading();
        	newsArticle = iterator.next().getValue();
        	newsHeading.setNewsHeading(newsArticle.getNewsHeading());
        	newsHeading.setKey(newsArticle.getKey());
        	listHeadings.add(newsHeading);
        }
        return sortHeadings(listHeadings);
	}
	
	public NewsArticle getArticle(String key)
	{
		 return mapArticles.get(key);
	}
	
	public NewsContent getContent(String key)
	{
		NewsArticle newsArticle = mapArticles.get(key);
		
		if (newsArticle == null)
			return null;
		
		NewsContent newsContent = new NewsContent();
		
		newsContent.setNewsContent(newsArticle.getNewsContent());
		newsContent.setNewsURL(newsArticle.getNewsURL());
		newsContent.setNewsImage(newsArticle.getNewsImage());
		
		return newsContent;
	}
	
	public List<NewsHeading> sortHeadings(List<NewsHeading> headingsList)
	{
		List<NewsHeading> ret = new ArrayList<NewsHeading>();
		HashMap<Integer, NewsHeading> mapSort = new HashMap<Integer, NewsHeading>();
		for (NewsHeading heading : headingsList)
		{
			mapSort.put(heading.getKey(), heading);
		}
		
		Iterator<Entry<Integer, NewsHeading>> iterator = mapSort.entrySet().iterator();
        while(iterator.hasNext())
        {   
        	ret.add(iterator.next().getValue());
        }
		
        return ret;
	}
}
