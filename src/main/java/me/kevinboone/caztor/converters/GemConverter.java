/*=========================================================================
  
  Caztor

  GemConverter

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.converters;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import me.kevinboone.caztor.base.*;
import net.fellbaum.jemoji.*;

/** A class for converting GemText to HTML. Most of the conversion is
    done line-by-line using regular expressions. It's not elegant,
    but it mostly seems to work.
*/
public class GemConverter extends TextLikeConverter implements Converter
  {
  private static final int MODE_NORMAL = 0;
  private static final int MODE_VERBATIM = 1;
  private static final int MODE_QUOTE = 2;
  private static final int MODE_LIST = 3;

  public static final int START_OTHER = 0;  
  public static final int START_PREFORMAT = 1;  
  public static final int START_TITLE = 2;  
  public static final int START_LIST = 3;  
  public static final int START_QUOTE = 4;  
  public static final int START_LINK = 5;  
  public static final int START_EOF = 6;  

  private int mode;
  private boolean lineAsPara;

  /** Construct a GemConverter, supplying a base URL. We need the URL so
      we can construct proper links. */
  public GemConverter (URL baseUrl)
    {
    super (baseUrl);
    mode = MODE_NORMAL;
    }

  /** Convert a single (maybe long) Gemtext line to HTML. */
  private String lineToHtml (String gem)
    {
    final int startCode;
    String trimmed;
    if (gem == null)
      trimmed = "";
    else
      trimmed = gem.trim();

    if (gem == null)
      startCode = START_EOF;
    else if (gem.startsWith ("```"))
      startCode = START_PREFORMAT;
    else if (gem.startsWith ("#"))
      startCode = START_TITLE;
    else if (gem.startsWith (">"))
      startCode = START_QUOTE;
    else if (gem.startsWith ("*"))
      startCode = START_LIST;
    else if (gem.startsWith ("=>"))
      startCode = START_LINK;
    else
      startCode = START_OTHER;

    switch (mode)
      {
      case MODE_NORMAL:
        switch (startCode)
          {
          case START_EOF:
            return "";
          case START_PREFORMAT:
            mode = MODE_VERBATIM;
            return "<pre>\n";
          case START_TITLE:
	    if (gem.startsWith ("###"))
	      return "<h3>" + escapeHtml(gem.substring(3)) + "</h3>\n";
	    else if (gem.startsWith ("##"))
	      return "<h2>" + escapeHtml(gem.substring(2)) + "</h2>\n";
	    else if (gem.startsWith ("#"))
              return "<h1>" + escapeHtml(gem.substring(1)) + "</h1>\n";
            else return escapeHtml (gem); 
          case START_QUOTE:
            mode = MODE_QUOTE;
            if (lineAsPara)
              return "<blockquote><p>❝ " + escapeHtml(gem.substring(1)) + "\n";
            else
              return "<blockquote>❝" + escapeHtml(gem.substring(1)) + "\n";
          case START_LIST:
            mode = MODE_LIST;
            return "<ul><li>&nbsp;" + escapeHtml(gem.substring(2)) + "\n";
          case START_LINK:
            if (lineAsPara)
              return "<p>" + parseLink (gem.substring(2).trim()) + "</p>\n";
            else
              return parseLink (gem.substring(2).trim()) + "<br/>\n";
          default:
            if (gem.length() == 0) 
              return "<br/>\n";
            else
              {
              if (lineAsPara)
                return "<p>" + escapeHtml(gem) + "</p>\n";
              else
                return escapeHtml(gem) + "<br/>\n";
              }
          }
      case MODE_VERBATIM:
        switch (startCode)
          {
          case START_PREFORMAT:
            mode = MODE_NORMAL;
            return "</pre>\n";
          case START_EOF:
            return "</pre>\n";
          default:
            return escapeHtml (gem) + "\n";
          }
      case MODE_LIST:
        switch (startCode)
          {
          case START_LIST:
            return "</li>\n<li>&nbsp;" + gem.substring(1).trim(); 
          case START_EOF:
            return "</li></ul>\n";
          default:
            mode = MODE_NORMAL;
            return "</li></ul>\n" + lineToHtml (gem);
          }
      case MODE_QUOTE:
        switch (startCode)
          {
          case START_QUOTE:
            if (trimmed.length() < 2) // Empty quote line
              {
              if (lineAsPara)
                return "</p><p></p><p>\n";
              else
                return "<p>&nbsp;</p>\n";
              }
            return gem.substring(1); 
          case START_EOF:
            if (lineAsPara)
              return "</p></blockquote>\n";
            else
              return "</blockquote>\n";
          default:
            mode = MODE_NORMAL;
            if (lineAsPara)
              return "</p></blockquote>\n" + lineToHtml (gem);
            else
              return "</blockquote>\n" + lineToHtml (gem);
          }
      default:
        return "ERROR"; // It should be impossible to get here
      }
    }

  /** Convert the Gemtext file to HTML. */ 
  @Override
  public String toHtml (String gem)
    {
    mode = MODE_NORMAL;
    lineAsPara = Config.getConfig().getGemtextLineAsPara();
    StringBuffer sb = new StringBuffer();
    String lines[] = gem.split ("\n");
    sb.append ("<html><head><body>\n");

    for (int i = 0; i < lines.length; i++)
      {
      String line = lines[i];
      String htmlLine = lineToHtml (line);
      sb.append (htmlLine);
      }
    String htmlLine = lineToHtml (null); // Mark EOF
    sb.append (htmlLine);

    sb.append ("</body></html>\n");
    //System.out.println (sb);
    return new String (sb);
    }
  }


