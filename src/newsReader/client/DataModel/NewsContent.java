package newsReader.client.DataModel;

import java.io.Serializable;

public class NewsContent implements  Serializable {
	private static final long serialVersionUID = 1L;
	
	private String newsContent;
	private String newsURL;
	private String newsImage;
	
	public NewsContent()
	{
		newsContent = "";
		newsURL = "";
		newsImage = "";
	}
	
	public String getNewsContent()
	{
		return newsContent;
	}
	
	public void setNewsContent(String newsContent)
	{
		this.newsContent = newsContent;
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
}