package newsReader.client.Service;

import java.util.List;

import newsReader.client.DataModel.NewsHeading;
import newsReader.client.DataModel.NewsSource;
import newsReader.client.DataModel.NewsContent;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NewsSourceServiceAsync {
	void getNewsSource(AsyncCallback<List<NewsSource>> callback) throws IllegalArgumentException;
	void getNewsHeadings(String newsSourceName, AsyncCallback<List<NewsHeading>> callback) throws IllegalArgumentException;
	void getNewsContent(String newsSourceName, String newsHeading, AsyncCallback<NewsContent> callback) throws IllegalArgumentException;
}
