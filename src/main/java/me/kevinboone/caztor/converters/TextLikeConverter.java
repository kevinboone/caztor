/*=========================================================================
  
  Caztor

  TextLikeConverter

  Copyright (c)2026 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.converters;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import me.kevinboone.caztor.base.*;
import net.fellbaum.jemoji.*;

/** Base class for converters that basically handle text, but have a 
    specific "=&gt;" syntax for links. Gemtext and the Nex variety of plain
    text both use this format. Using this base class avoids a little
    code duplication.
*/
public class TextLikeConverter
  {
  private static final Config config = Config.getConfig();
  protected URL baseURL;
  protected String defltLink;

  public TextLikeConverter (URL baseURL)
    {
    this.baseURL = baseURL;
    defltLink = config.getDefltLink(); 
    }

  public static boolean isImageUri (String uri)
    {
    if (uri.endsWith(".gif") || uri.endsWith (".jpg") || 
        uri.endsWith (".png") || uri.endsWith (".jpeg"))
      return true;
    return false;
    }

  /** Convert common punctuation like &amp; into HTML-friendly forms. */
  protected static String escapeHtml (String gem)
    {
    String s = gem.replace (">", "£££gt;");
    s = s.replace ("<", "£££lt;");
    s = s.replace ("&", "&amp;");
    s = s.replace ("£££", "&");
    return s;
    }

  private String getLinkIcon (String link, String text)
    {
    if (text == null) return "";
    if (text.length() < 2) return "";
    if (EmojiManager.isEmoji(text.substring(0,2))) 
      return ""; // Don't decorate an emoji
    if (isImageUri (link))
      return "📷";
    if (link.startsWith ("http"))
      return "🌍";
    return defltLink;    }

  protected String writeLink (String uri, String title)
    {
    return writeLink (uri, title, false);
    }

  protected String writeLink (String uri, String title, boolean spartanPrompt)
    {
    Config config = Config.getConfig();
    if (isImageUri (uri) && config.getGemtextInlineImages())
      {
      return "<img width=\"" + config.getInlineImageWidth() + "\" src=\"" 
        + rewriteLink (uri) + "\">" + "<br/>" + "<a href=\"" 
           + rewriteLink (uri) + "\">" + getLinkIcon (uri, title) + " " 
             + escapeHtml (title) + "</a>"; 
      }
    return "<a href=\"" + rewriteLink (uri, spartanPrompt) + "\">" + 
      getLinkIcon (uri, title) + " " + escapeHtml (title) + "</a>"; 
    } 

  /** Parse and convert a Gemtext link line. */
  protected String parseLink (String gem)
    {
    return parseLink (gem, false);
    }

  /** Parse and convert a Gemtext link line. */
  protected String parseLink (String gem, boolean spartanPrompt)
    {
    String[] args = gem.split ("\\s+", 2);
    if (args.length >= 2)
      {
      return writeLink (args[0], args[1], spartanPrompt);
      }
    else if (args.length == 1)
      {
      return writeLink (args[0], args[0], spartanPrompt);
      }
    else
      {
      // Can not happen
      return "";
      }
    }

  protected String rewriteLink (String link)
    {
    return rewriteLink (link, false);
    }

  /** Given a link target, rewrite it to a complete link that can
      be parsed to a java.net.URL. This involves resolving it against the
      baseURL to allow for relative links, etc. */
  protected String rewriteLink (String link, boolean spartanPrompt)
    {
    Logger.in();
    Logger.log (getClass().getName(), Logger.DEBUG, "old link=" + link);
    try
      {
      // I'm still not 100% sure about this
      URI newUri =  new URI (baseURL.toString());
      newUri =  newUri.resolve (link); 
      boolean hasQuery = false;
      if (spartanPrompt)
        {
        String q = newUri.getQuery();
        if (q != null && q.length() > 0)
          hasQuery = true;
        }
      Logger.log (getClass().getName(), Logger.DEBUG, "new link=" + newUri);
      Logger.out();
      if (spartanPrompt) 
        { 
        if (hasQuery)
          return newUri.toString() + "&spartanprompt";
        else
          return newUri.toString() + "?spartanprompt";
        }
      else
        return newUri.toString();
      }
    catch (Exception e)
      {
      Logger.out();
      Logger.log (getClass().getName(), Logger.WARNING, e.toString());
      return link;
      }
    }



  }

