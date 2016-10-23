package newsReader.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DisclosurePanel;
import java.util.List;
import newsReader.client.Service.NewsSourceServiceAsync;
import newsReader.client.Service.NewsSourceService;
import newsReader.client.DataModel.NewsSource;
import newsReader.client.DataModel.NewsHeading;
import newsReader.client.DataModel.NewsContent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import newsReader.client.HTMLEx;
import com.google.gwt.user.client.ui.ScrollPanel;
import java.lang.StringBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
//import com.google.gwt.user.client.rpc.ServiceDefTarget; 

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewsReader implements EntryPoint {
	
	List<NewsSource> newsSourceList = null;
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final NewsSourceServiceAsync newsSourceService = GWT
			.create(NewsSourceService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		//((ServiceDefTarget)newsSourceService).setServiceEntryPoint("/"); 

		newsSourceService.getNewsSource(
			new AsyncCallback<List<NewsSource>>()
			{
				public void onFailure(Throwable caught)
				{
					System.err.println("Error: " + caught.getMessage());
				}

				public void onSuccess(List<NewsSource> result)
				{
					newsSourceList = result;
					
					RootPanel rootPanel = RootPanel.get("newsReaderContainer");
					rootPanel.setSize("100%", "100%");
					rootPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);

					DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
					//HTML htmlContact = new HTML("<div align=\"right\"><small style=\"color:blue\">Contact: admin@slnewsreader.com</small></div>");
					//htmlContact.setWidth("100%");
					//rootPanel.add(htmlContact);
					rootPanel.add(new Image("images/Title.png"), 0, 0);
					rootPanel.add(dockLayoutPanel, 0, 50);
					dockLayoutPanel.setSize("100%", "100%");
					
					//right most panel
					FlowPanel spacerPanel = new FlowPanel();
					dockLayoutPanel.addEast(spacerPanel, 2.0);
					
					//left most panel
					FlowPanel buttonPanel = new FlowPanel();
					dockLayoutPanel.addWest(buttonPanel, 10.0);
					buttonPanel.setWidth("100%");
					
					//middle panel
					final VerticalPanel newsPanel = new VerticalPanel();
					dockLayoutPanel.add(new ScrollPanel(newsPanel));
					newsPanel.setSize("100%", "");
					
					HTML htmlClue = new HTML("<CENTER>&larr; Select a news source to your left.</CENTER>");
					htmlClue.setWidth("100%");
					newsPanel.add(htmlClue);
					
					Button button;
					for (final NewsSource newsSource : newsSourceList)
					{
						StringBuilder newsSourceNameBuilder = new StringBuilder("<div");
						if (newsSource.getFontFace().isEmpty())
							newsSourceNameBuilder.append(">");
						else
							newsSourceNameBuilder.append(" style=\"font-family:" + newsSource.getFontFace() + "\">");
						newsSourceNameBuilder.append(HTMLEx.escapeHtml(newsSource.getSourceDisplayName()) + "</div>");
						
						String newsSourceName = newsSourceNameBuilder.toString();
						
						button = new Button(newsSourceName);
						button.setSize("90%", "");

						button.addClickHandler(new ClickHandler()
						{
							public void onClick(ClickEvent event)
							{
								newsPanel.clear();
								newsSourceService.getNewsHeadings(newsSource.getSourceName(),
								new AsyncCallback<List<NewsHeading>>()
								{
									public void onFailure(Throwable caught)
									{
										System.err.println("Error: " + caught.getMessage());
									}

									public void onSuccess(List<NewsHeading> result)
									{
										if (result == null || result.isEmpty())
											return;
										
										for (final NewsHeading nh : result)
										{
											final DisclosurePanel disclosurePanel = new DisclosurePanel();
											disclosurePanel.addOpenHandler(new OpenHandler<DisclosurePanel>()
										    {
												@Override
												public void onOpen(OpenEvent<DisclosurePanel> event)
												{			
													String header = BuildHeading(newsSource.getFontFace(), HTMLEx.escapeHtml(nh.getNewsHeading()), "\u25BC");

													disclosurePanel.setHeader(new HTML(header));
													if (disclosurePanel.getContent() == null)
													{
														newsSourceService.getNewsContent(newsSource.getSourceName(), nh.getNewsHeading(),
														new AsyncCallback<NewsContent>()
														{
															public void onFailure(Throwable caught)
															{
																System.err.println("Error: " + caught.getMessage());
															}

															public void onSuccess(NewsContent result)
															{
																StringBuilder contentBuilder = new StringBuilder("<pre");
																if (newsSource.getFontFace().isEmpty())
																	contentBuilder.append(">");
																else
																	contentBuilder.append(" style=\"font-family:" + newsSource.getFontFace() + "\">");
																if (!result.getNewsImage().isEmpty())
																	contentBuilder.append("<img src = " + result.getNewsImage() + " align = \"right\" height = \"80\">");
																contentBuilder.append(HTMLEx.escapeHtml(result.getNewsContent()));
																contentBuilder.append("</pre><font face = \"Arial\"><a href=\"" + result.getNewsURL() + "\" target=\"_blank\" align = \"right\">source>></a></font>");
																disclosurePanel.setContent(new HTMLEx(contentBuilder.toString()));
															}
														});
													}
												}
										    });
										    
											disclosurePanel.addCloseHandler(new CloseHandler<DisclosurePanel>()
										    {
												@Override
												public void onClose(CloseEvent<DisclosurePanel> event)
												{
													String header = BuildHeading(newsSource.getFontFace(), HTMLEx.escapeHtml(nh.getNewsHeading()), "\u25BA");
													disclosurePanel.setHeader(new HTML(header));																	
												}
										    });

											String header = BuildHeading(newsSource.getFontFace(), HTMLEx.escapeHtml(nh.getNewsHeading()), "\u25BA");
											disclosurePanel.setHeader(new HTML(header));
											newsPanel.add(disclosurePanel);
											disclosurePanel.setWidth("100%");
										}
									}
								});
							}
						});
						buttonPanel.add(button);
					}
					buttonPanel.add(new HTML(" More news soon..."));
				}
			});
	}
	
	private String BuildHeading(String fontFace, String heading, String bullet)
	{
		StringBuilder headerBuilder = new StringBuilder("<p");
		if (fontFace.isEmpty())
			headerBuilder.append(" style=\"color:blue\">");
		else
			headerBuilder.append(" style=\"font-family: " + fontFace + ";color:blue\">");
		headerBuilder.append("<font face = \"Arial\">" + bullet + "</font>" + heading + "</p>");
		return headerBuilder.toString();
	}
}
