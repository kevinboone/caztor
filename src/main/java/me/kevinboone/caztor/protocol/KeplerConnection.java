/*============================================================================

  Caztor

  KeplerConnection

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Collections;
import me.kevinboone.caztor.base.*;

/** A subclass of URLConnection that handles the Kepler protocol.
*/
public class KeplerConnection extends URLConnection
  {
  public static final int KEPLER_MAX_HEADER = 1024;
  private Socket s = null;
  private String contentType = null;
  private InputStream is = null;
  private String meta = null;
  private static StatusHandler statusHandler = StatusHandler.getInstance();
  private final static ResourceBundle messagesBundle = 
    ResourceBundle.getBundle ("me.kevinboone.caztor.bundles.Messages");
  private String certinfo = null;
  private static Config config = Config.getConfig();
  private int contentLength = -1;
  private int updated = -1;
  private int expires = -1;

/*============================================================================

  Constructor

============================================================================*/
  public KeplerConnection (URL url) 
    {
    super (url);
    Logger.in();
    Logger.out();
    }

/*============================================================================

  getRequestProperty

  We override this method so client classes can retrieve certificate
  information and other things from this connection. It's a bit ugly, but there
  aren't many alternatives, when we embed our protocol handling into the JVM's
  infrastructure.  

============================================================================*/
  @Override
  public String getRequestProperty (String key)
    {
    if ("certinfo".equals (key)) return certinfo;
    if ("last-updated".equals (key)) return "" + updated;
    if ("content-length".equals (key)) return "" + contentLength;
    if ("expires".equals (key)) return "" + expires;
    return super.getRequestProperty (key);
    }

/*============================================================================

  parseStatus

============================================================================*/
  private static int parseStatus (String line)
    {
    Logger.in();
    String[] args = line.split ("\\s+", 2); 
    try 
      {
      int status = Integer.parseInt (args[0]);
      Logger.out();
      return status;
      }
    catch (NumberFormatException e) 
      {
      Logger.out();
      return -1;
      }
    }

/*============================================================================

  connect 

============================================================================*/
  @Override
  public void connect() 
      throws IOException 
    {
    Logger.in();
    if (s != null && s.isConnected()) return;

    String host = getURL().getHost();
    String path = getURL().getPath();
    int port = getURL().getPort();
    if (port == -1) port = KeplerURLStreamHandler.DEFAULT_PORT;

    int timeoutMs = config.getConnectTimeout() * 1000;

    s = new Socket(); 
    s.setSoTimeout (timeoutMs);
    s.connect(new InetSocketAddress(host, port), timeoutMs);
    is = s.getInputStream();
    OutputStream os = s.getOutputStream();
    PrintStream pos = new PrintStream (os);

    if (Logger.isDebug())
      Logger.log (getClass().getName(), Logger.DEBUG, "Sending request: " + getURL().toString());
    pos.print (getURL().toString());
    pos.print (" ");
    pos.print ("0"); // TODO -- we need to accept a value from the cache manager
    pos.print (" ");
    pos.print (config.getUserLanguage()); 
    pos.print ("\r\n"); 
    pos.flush();

    char c; 
    String line = ""; 
    int len = 0;
    do 
      {
      c = (char)is.read(); 
      len++;
      if (c == '\n')
           break; 
      if (c != '\r') line += c + "";
      } while (c != -1 && len <= KEPLER_MAX_HEADER);

    if (len >= KEPLER_MAX_HEADER)
      {
      String tooLong = messagesBundle.getString ("status_line_too_long");
      Logger.log (getClass().getName(), Logger.WARNING, 
          tooLong + ", URI=" +  getURL());
      throw new IOException (tooLong + ", URI=" +  getURL());
      }

    int status = parseStatus (line);
    Logger.log (getClass().getName(), Logger.DEBUG, "Got status code " + status);
      
    if (status >= 20 && status < 30)
      {
      String args[] = line.split ("\\s+", 5);
      if (args.length != 5)
        throw new IOException 
          ("Incorrect number of data elements in response: expected 5, got " 
             + args.length);
      contentType = args[4]; 
        
      int length = -1;
      int updated = -1;
      int expires = -1;
      try
        {
        length = Integer.parseInt (args[1]);
        updated = Integer.parseInt (args[2]);
        expires = Integer.parseInt (args[3]);
        }
      catch (NumberFormatException e)
        {
        throw new IOException ("Bad numeric value in response");
        }

      this.updated = updated;
      this.contentLength = length;
      this.expires = expires;

      // Nothing more to do -- input stream is now positioned
      //  to read data
      }
    else
      {
      s.close();
      s = null;
      meta = parseMeta (line);
      if (status < 10) 
        {
        throw new ErrorResponseException (getURL(), status, 
          "Invalid status code in response");
        }
      if (status >= 10 && status < 20)
        {
        throw new RetryWithInputException (getURL(), status == 11, meta);
        }
      else if (status >= 30 && status < 40)
        {
        Logger.log (getClass().getName(), Logger.DEBUG, "Throwing a redirect to " + meta);
        throw new RedirectedException (new URL(getURL(), meta));
        }
      else if (status >= 40 && status < 70)
        {
        // There are some codes in this range we really ought to be
        //   implementing
        throw new ErrorResponseException (getURL(), status, meta);
        }
      else if (status >= 70 && status < 80)
        {
        // These code only used by kepler protocol
        throw new NotChangedException (getURL(), status);
        }
      }
    Logger.out();
    }

/*============================================================================

  getContentType 

============================================================================*/
  @Override
  public String getContentType()
    {
    return contentType;
    }
   
/*============================================================================

  getContent

============================================================================*/
  @Override
  public Object getContent() 
      throws IOException 
    {
    Logger.in();
    connect();
    BufferedInputStream bis = new BufferedInputStream (getInputStream());
    Logger.out();
    return bis;
    }

/*============================================================================

  getInputStream

  This method forces a connection and the initial request. If the request
    results in an "OK" response, the input stream should be positioned
    at the start of the real content, ready to read.

============================================================================*/
  @Override
  public InputStream getInputStream() 
      throws IOException 
    {
    Logger.in();
    connect();
    Logger.out();
    return is;
    }

/*============================================================================

  parseMeta

============================================================================*/
  private static String parseMeta (String line)
    {
    String[] args = line.split ("\\s+", 2); 
    if (args.length > 1)
      return args[1]; 
    else
      return "";
    }

  }


