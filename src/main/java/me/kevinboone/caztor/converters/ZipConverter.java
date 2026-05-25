/*=========================================================================
  
  Caztor

  ZipConverter

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.converters;
import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.*;
import java.util.regex.Pattern;
import me.kevinboone.caztor.bundles.Messages;
import me.kevinboone.caztor.base.*;
import me.kevinboone.caztor.protocol.ZipfileConnection;

/** A class for converting a Zipfile index to HTML. 
*/
public class ZipConverter extends TextLikeConverter implements Converter
  {
  private final static ResourceBundle messagesBundle = 
    Messages.getBundle ("me.kevinboone.caztor.bundles.Messages");

  /** Construct a ZipConverter, supplying a base URL. We need the URL so
      we can construct proper links. */
  public ZipConverter (URL baseUrl)
    {
    super (baseUrl);
    }


  /** Convert the Ziptext file to HTML. */ 
  @Override
  public String toHtml (byte[] content, Charset charset)
    {
    StringBuffer sb = new StringBuffer();

    sb.append ("<html><head><body>\n");

    try
      {
      File tempFile = File.createTempFile ("zip-", ".zip");
      tempFile.deleteOnExit();

      FileOutputStream fos = new FileOutputStream (tempFile);
      fos.write (content);
      fos.close();

      Properties metadata = ZipfileConnection.getMetadata (tempFile.toString());
      if (metadata == null)
        {
        sb.append ("<p>");
        sb.append (messagesBundle.getString ("zipfile_format_unknown"));
        sb.append (".</p>\n");
        sb.append ("<p>");
        sb.append ("<a href=\"");
        sb.append ("zipfile://");
        sb.append (tempFile);
        sb.append ("/\">");
        sb.append (messagesBundle.getString ("open_anyway"));
        sb.append ("</a>\n");
        sb.append ("</p>");
        }
      else
        {
        String title = (String)metadata.get ("title");
        if (title != null)
          {
          sb.append ("<p>");
          sb.append (messagesBundle.getString ("title"));
          sb.append (": ");
          sb.append (title.trim());
          sb.append ("</p>");
          }
        String author = (String)metadata.get ("author");
        if (author != null)
          {
          sb.append ("<p>");
          sb.append (messagesBundle.getString ("author"));
          sb.append (": ");
          sb.append (author.trim());
          sb.append ("</p>");
          }
        String description = (String)metadata.get ("description");
        if (description != null)
          {
          sb.append ("<p><i>");
          sb.append (escapeHtml (description.trim()));
          sb.append ("</i></p>");
          }
        sb.append ("<p>");
        sb.append ("<a href=\"");
        sb.append ("zipfile://");
        sb.append (tempFile);
        sb.append ("/\">"); 
        sb.append (messagesBundle.getString ("open_book"));
        sb.append ("</a>"); 
        sb.append ("</p>\n");
        }
      }
    catch (Exception e)
      {
      sb.append ("Can't process zipfile: " + e.toString());
      }

    sb.append ("</body></html>\n");
    //System.out.println (sb);
    return new String (sb);
    }
  }



