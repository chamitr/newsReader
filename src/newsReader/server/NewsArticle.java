package newsReader.server;

public class NewsArticle {
	
	private String newsHeading;
	private String newsURL;
	private String newsImage;
	private String newsContent;
	private Integer key;
	
	public NewsArticle()
	{
		newsHeading = "";
		newsURL = "";
		newsImage = "";
		newsContent = "";
		key = 0;
	}
	
	public String getNewsHeading()
	{
		return newsHeading;
	}
	
	public void setNewsHeading(String newsHeading)
	{
		this.newsHeading = newsHeading;
	}
	
	public String getNewsURL()
	{
		return newsURL;
	}
	
	public void setNewsURL(String newsURL)
	{
		this.newsURL = newsURL;
	}
	
	public String getNewsImage()
	{
		return newsImage;
	}
	
	public void setNewsImage(String newsImage)
	{
		this.newsImage = newsImage;
	}

	public String getNewsContent()
	{
		return newsContent;
	}
	
	public void addNewsContent(String[] saNewsContent)
	{
		StringBuilder contentBuilder = new StringBuilder(newsContent);
		for (int i = 0; i < saNewsContent.length; i++)
		{
			if (contentBuilder.length() != 0)
				contentBuilder.append('\n');
				
			contentBuilder.append(saNewsContent[i]);
		}
		newsContent = contentBuilder.toString();
	}
	
	public Integer getKey()
	{
		return key;
	}
	
	public void setKey(Integer key)
	{
		this.key = key;
	}
}
