package newsReader.client.DataModel;

import java.io.Serializable;

public class NewsSource implements  Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sourceName;
	private String sourceDisplayName;
	private String fontFace;
	
	public NewsSource()
	{
		sourceName = "";
		sourceDisplayName = "";
		fontFace = "";
	}
	
	public String getSourceName()
	{
		return sourceName;
	}
	
	public void setSourceName(String sourceName)
	{
		this.sourceName = sourceName;
	}

	public String getSourceDisplayName()
	{
		return sourceDisplayName;
	}
	
	public void setSourceDisplayName(String sourceDisplayName)
	{
		this.sourceDisplayName = sourceDisplayName;
	}

	public String getFontFace()
	{
		return fontFace;
	}
	
	public void setFontFace(String fontFace)
	{
		this.fontFace = fontFace;
	}
}
