/*============================================================================

  Caztor

  ZipfileConnection

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;
import me.kevinboone.caztor.base.*;
import me.kevinboone.caztor.swing.*;

/** A subclass of URLConnection that handles the Zipfile protocol. */
public class ZipfileConnection extends URLConnection
  {
  private static final int TYPE_GEMPUB = 0;
  private static final int TYPE_MBOOK = 1;
  private static final int TYPE_OTHER = -1;
  private String contentType = null;
  private static StatusHandler statusHandler = StatusHandler.getInstance();
  private static final ResourceBundle messagesBundle = 
    ResourceBundle.getBundle ("me.kevinboone.caztor.bundles.Messages");
  private static final Config config = Config.getConfig();
  private InputStream is = new ByteArrayInputStream ("hello".getBytes());
  private static Pattern pattern = Pattern.compile("\\/([^\\/]+\\.[^\\/]*)");
  private int type = TYPE_OTHER; 

  public ZipfileConnection (URL url) 
    {
    super (url);
    }

  @Override
  public void connect() 
      throws IOException 
    {
    String path = getURL().getPath();
    String query = getURL().getQuery();
    if (path.length() == 0)
      throw new IOException (messagesBundle.getString ("zipfile_no_path"));

    Matcher matcher = pattern.matcher (path);
    if (matcher.find())
      {
      if (matcher.end(1) >= path.length())
        throw new IOException 
           ("Zipfile filename must be followed by '/'");
 
      String subPath = path.substring (matcher.end(1) + 1);
      path = path.substring (0, matcher.end(1));

      Properties metadata = null; 
      metadata = getGemsubMetadata (path);
      if (metadata == null) 
        {
        metadata = getMBookMetadata (path);
        if (metadata == null)
          {
          }
        else
          type = TYPE_MBOOK;
        }
      else
        {
        type = TYPE_GEMPUB; 
        }

      if (type == TYPE_OTHER)
        throw new IOException ("This is not a Gemsub or MBook book"); // TODO
        
      String index = (String)metadata.get ("index");    
      if (index == null) index = "index.gmi"; 


      ZipFile zf = new ZipFile (path);
      Vector<GemLink> indexLinks = parseIndex (zf, index); 
   
      if (subPath.length() == 0 || subPath.equals ("/"))
	{
	// Make and show index
	contentType = "text/gemini"; 
        StringBuffer sb = new StringBuffer();
        for (GemLink link : indexLinks)
           {
System.out.println ("u=" + link.getUri() + " t=" + link.getText());
           sb.append ("=> " + link.getUri() + " " + link.getText() + "\n");
           }
        is = new ByteArrayInputStream (new String (sb).getBytes(StandardCharsets.UTF_8));
	}
      else
	{
	// Show content
	contentType = FileUtil.guessMimeTypeFromFilename (subPath); 
	ZipEntry ze = zf.getEntry (subPath);
	is = zf.getInputStream (ze);
        if (contentType.startsWith ("text/"))
          {
          String s = FileUtil.readInputStreamToString (is, StandardCharsets.UTF_8);
          s = s + makePostamble (indexLinks, path, subPath);
	
	  is.close(); 

          InputStream newIs = new ByteArrayInputStream (s.getBytes());
          is = newIs;
          }
	}
      //zf.close(); // Can't close while inputstream is still open
      }
    else
      throw new IOException ("No filename in zipfile path"); //TODO
    }

  public static String getBaseUrl (String path)
    {
    Matcher matcher = pattern.matcher (path);
    if (matcher.find())
      {
      if (matcher.end(1) >= path.length())
        return path;
      return path.substring (0, matcher.end(1));
      } 
    return path;
    }


  @Override
  public String getContentType()
    {
    return contentType;
    }
   
  @Override
  public Object getContent() 
      throws IOException 
    {
    Logger.in();
    connect();
    BufferedInputStream bis = new BufferedInputStream (is);
    Logger.out();
    return bis;
    }

  public static Properties getMetadata (String path)
      throws IOException
    {
    Properties metadata = getGemsubMetadata (path);
    if (metadata == null)
      metadata = getMBookMetadata (path);
    return metadata;
    }

  protected static Properties getGemsubMetadata (String path)
      throws IOException
    {
    ZipFile zf = null; 
    ZipEntry ze = null;
    InputStream is = null; 
    InputStreamReader isr = null;
    BufferedReader br = null;
    Properties ret = new Properties();
    try
      {
      zf = new ZipFile (path);
      ze = zf.getEntry ("metadata.txt");
      if (ze != null)
        {
        is = zf.getInputStream (ze);
        isr = new InputStreamReader (is);
        br = new BufferedReader (isr);
        String line;
        while ((line = br.readLine()) != null)
          {
          int p = line.indexOf (':');
          if (p > 0)
            {
            String key = line.substring (0, p).trim();
            String value = line.substring (p + 1).trim();
            ret.put (key, value);
            }
          }
        }
      else
        ret = null;
      }
    catch (IOException e)
      {
      if (br != null) br.close();
      if (isr != null) isr.close();
      if (is != null) is.close();
      if (zf != null) zf.close();
      throw e;
      }
    return ret;
    }

  protected static Properties getMBookMetadata (String path)
      throws IOException
    {
    ZipFile zf = null; 
    ZipEntry ze = null;
    InputStream is = null; 
    InputStreamReader isr = null;
    BufferedReader br = null;
    Properties ret = new Properties();
    try
      {
      zf = new ZipFile (path);
      ze = zf.getEntry ("mbook_metadata.txt");
      if (ze != null)
        {
	is = zf.getInputStream (ze);
	isr = new InputStreamReader (is);
	br = new BufferedReader (isr);
	String line;
	while ((line = br.readLine()) != null)
	  {
	  int p = line.indexOf (':');
	  if (p > 0)
	    {
	    String key = line.substring (0, p).trim();
	    String value = line.substring (p + 1).trim();
	    ret.put (key, value);
	    }
	  }
        }
      else
        ret = null;
      }
    catch (IOException e)
      {
      if (br != null) br.close();
      if (isr != null) isr.close();
      if (is != null) is.close();
      if (zf != null) zf.close();
      throw e;
      }
    return ret;
    }


  private String getNextPathFromIndex (Vector<GemLink> indexLinks, String subPath)
    {
    if (indexLinks == null) return null;
    int l = indexLinks.size();
    for (int i = 0; i < l; i++)
      {
      GemLink link = indexLinks.elementAt(i);
      String indexPath = link.getUri();
      if (indexPath.equals (subPath))
        {
        if (i < l - 1) return indexLinks.elementAt(i + 1).getUri();
        }
      }
    return null;
    }

  @Override
  public InputStream getInputStream() 
      throws IOException 
    {
    connect();
    return is;
    }

  private String makePostamble (Vector<GemLink> indexLinks, String path, 
       String subPath)
    {
    StringBuffer sb = new StringBuffer();
    sb.append ("\n");
    boolean isMarkdown = false;
    if (subPath.endsWith (".md")) isMarkdown = true;
    if (isMarkdown) 
      {
      String nextPath = getNextPathFromIndex (indexLinks, subPath);
      if (nextPath != null)
        sb.append ("[Next](" + path + "/" + nextPath + ")  \n");
      sb.append ("[Contents](" + path + "/)\n");
      }
    else
      {
      // Gemtext
      sb.append ("\n");
      String nextPath = getNextPathFromIndex (indexLinks, subPath);
      if (nextPath != null)
        sb.append ("=> " + path + "/" + nextPath + " Next\n"); 
      sb.append ("=> " + path + "/ Contents\n"); 
      }
    return new String (sb);
    }


  private Vector<GemLink> parseIndex (ZipFile zf, String index)
      throws IOException
    {
    Vector<GemLink> ret = new Vector<GemLink>();
    ZipEntry ze = zf.getEntry (index);
    InputStream is = zf.getInputStream (ze);
    String indexFile = FileUtil.readInputStreamToString 
       (is, StandardCharsets.UTF_8);
    String lines[] = indexFile.split ("\r*\n");
    for (String line : lines)
      {
      if (type == TYPE_MBOOK)
        {
        String toks[] = line.split (" ", 2);
	GemLink link = new GemLink (toks[0], toks[1]); 
	ret.add (link);
        }
      else if (type == TYPE_GEMPUB)
        {
	if (line.length() != 0)
	  {
	  GemLink link = GemLink.parse (line.substring(2).trim());
	  ret.add (link);
	  }
        }
      }
    is.close();
    return ret;
    }


  }



