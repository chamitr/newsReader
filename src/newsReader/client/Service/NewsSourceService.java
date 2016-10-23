package newsReader.client.Service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import newsReader.client.DataModel.NewsSource;
import newsReader.client.DataModel.NewsHeading;
import newsReader.client.DataModel.NewsContent;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("newsSources")
public interface NewsSourceService extends RemoteService {
	List<NewsSource> getNewsSource() throws IllegalArgumentException;
	List<NewsHeading> getNewsHeadings(String newsSourceName) throws IllegalArgumentException;
	NewsContent getNewsContent(String newsSourceName, String newsHeading) throws IllegalArgumentException;
}
