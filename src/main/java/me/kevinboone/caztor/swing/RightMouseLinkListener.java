package me.kevinboone.caztor.swing;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.net.*;
import java.io.*;
import java.util.*;

/** The base class for anything that handles right mouse-clicks in
    the main document viewer. It's used to separate out some ugly
    logic for working out exactly what was clicked. At present,
    this class is only used by a single handler, which is an anonymous 
    inner in MainWindow, but at least it takes a little code
    out of the vast MainWindow class.
    */
abstract class RightMouseLinkListener extends MouseAdapter 
  {
  public abstract void clickedLink (String href, int x, int y);
  public abstract void clickedImage (String href, int x, int y);

  @Override
  public void mouseClicked (MouseEvent e) 
    {
    if (e.getButton() == MouseEvent.BUTTON3) 
      {
      Element h = getHyperlinkElement (e);
      if (h != null) 
        {
        String elementName = h.getName();
        if ("content".equals (elementName))
          {
          // The HTML viewer doesn't distinguish links from general
          //   content. But only links will have an "A" attribute
	  Object attribute = h.getAttributes().getAttribute (HTML.Tag.A);
	  if (attribute instanceof AttributeSet) 
	    {
	    AttributeSet set = (AttributeSet) attribute;
	    String href = (String) set.getAttribute (HTML.Attribute.HREF);
	    if (href != null) 
	      {
	      clickedLink (href, e.getX(), e.getY());
	      }
	    }
          }
        else if ("img".equals (elementName))
          {
	  AttributeSet as = h.getAttributes(); 
          Object o = as.getAttribute (HTML.Attribute.SRC);
          String src = o.toString(); 
	  clickedImage (src, e.getX(), e.getY());
          }
        }
      }
    }

  private Element getHyperlinkElement(MouseEvent event) 
    {
    JEditorPane editor = (JEditorPane) event.getSource();
    int pos = editor.getUI().viewToModel(editor, event.getPoint());
    if (pos >= 0 && editor.getDocument() instanceof HTMLDocument) 
      {
      HTMLDocument hdoc = (HTMLDocument) editor.getDocument();
      Element elem = hdoc.getCharacterElement (pos);
      //if (elem.getAttributes().getAttribute (HTML.Tag.A) != null)
        {
        return elem;
        }
      }
    return null;
    }
  }
