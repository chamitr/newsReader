package newsReader.client.DataModel;

import java.io.Serializable;

public class NewsHeading implements  Serializable {
	private static final long serialVersionUID = 1L;
	
	private String newsHeading;
	private Integer key;
	
	public NewsHeading()
	{
		newsHeading = "";
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
	
	public Integer getKey()
	{
		return key;
	}
	
	public void setKey(Integer key)
	{
		this.key = key;
	}
}