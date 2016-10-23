package newsReader.client;

import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.Element;

public class HTMLEx extends HTML implements MouseOverHandler, MouseOutHandler
{
	public HTMLEx(String html)
	{
	    super(html);
		DOM.setStyleAttribute(getElement(), "border", "2px solid #F8F8F8");
	    addMouseOverHandler(this);
	    addMouseOutHandler(this);
	}
	
    public void onMouseOver(MouseOverEvent event)
    {
    	DOM.setStyleAttribute(getElement(), "borderColor", "#0000CC");
    }

    public void onMouseOut(MouseOutEvent event)
    {
        DOM.setStyleAttribute(getElement(), "borderColor", "#F8F8F8");
    }
    
    public static String escapeHtml(String maybeHtml)
    {
        final Element div = DOM.createDiv();
        DOM.setInnerText(div, maybeHtml);
        return DOM.getInnerHTML(div);
     }
}