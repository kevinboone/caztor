/*=========================================================================
  
  Caztor

  DemarkusConnection 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.protocol;

import me.kevinboone.caztor.base.*;
import tech.kwik.core.*;

import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.cert.X509Certificate;
import java.time.Duration;

public class DemarkusConnection extends URLConnection
  {
  public static final int DEFLT_PORT = 6309;
  public static final int DEMARKUS_MAX_HEADER = 1024; // TODO
  public static final int DEMARKUS_MAX_LINE = 1024; // TODO
  public static final int DEMARKUS_MAX_RESPONSE_HEADER_LINES = 20; // TODO
  public static final String DEMARKUS_APID = "mark";

  private final static ResourceBundle messagesBundle = 
    ResourceBundle.getBundle ("me.kevinboone.caztor.bundles.Messages");

  private QuicClientConnection quicConnection = null;
  private InputStream is = null;
  private String contentType = null;
  private String certinfo = null;

/*============================================================================

  Constructor

============================================================================*/
  public DemarkusConnection (URL url) 
    {
    super (url);
    Logger.in();
    Logger.out();
    }

/*============================================================================

  connect 

============================================================================*/
  @Override
  public void connect() 
      throws IOException 
    {
    Logger.in();
    if (quicConnection != null) 
      {
      Logger.out(); 
      return; 
      }

    quicConnection = QuicClientConnection.newBuilder()
	.host (url.getHost())
	.port (url.getPort() > 0 ? url.getPort() : DEFLT_PORT)
	.applicationProtocol (DEMARKUS_APID)
	.build();
    quicConnection.connect();
    QuicStream quicStream = quicConnection.createStream (true);
    OutputStream output = quicStream.getOutputStream();
    is = quicStream.getInputStream();
    PrintStream pos = new PrintStream (output);
    pos.print("FETCH ");
    String path = url.getPath();
    if (path == null || path.length() == 0)
      pos.print ("/");
    else
      pos.print (url.getPath());
    pos.print ("\r\n");
    pos.close();
    output.close();
    String line = readLine (is);
    if (!"---".equals (line))
      throw new IOException ("Response does not begin \"---\"");
    int headerLines = 0;
    do
      {
      line = readLine (is);
      // Add present, we don't even need to parse the metadata. The server will
      //   send back its own error page if there's something wrong.
      headerLines++;
      } while (!"---".equals (line) && headerLines < DEMARKUS_MAX_RESPONSE_HEADER_LINES);

    contentType = "text/markdown";

    // If we got this far, we must have had a reasonable SSL handshake.
    // So let's save the certificate information for future use.
    StringBuffer sb = new StringBuffer();
    List<X509Certificate> certs = quicConnection.getServerCertificateChain();
    int i = 0;
    for (X509Certificate xcert : certs)
      {
      sb.append ("" + i);
      sb.append (":\nSubject: ");
      sb.append (xcert.getSubjectDN().toString());
      sb.append ("\nIssuer: ");
      sb.append (xcert.getIssuerDN().toString());
      sb.append ("\nExpires: ");
      sb.append (xcert.getNotAfter().toString());
      sb.append ("\nAlgorithm: ");
      sb.append (xcert.getSigAlgName());
      sb.append ("\n");
      i++;
      }
    certinfo = new String (sb); 

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

  getInputStream

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

  getRequestProperty

  We override this method so client classes can retrieve certificate
  information from this connection. It's a bit ugly, but there aren't
  many alternatives, when we embed our protocol handling into the
  JVM's infrastructure.  

============================================================================*/
  @Override
  public String getRequestProperty (String key)
    {
    if ("certinfo".equals (key)) return certinfo;
    return super.getRequestProperty (key);
    }


/*============================================================================

  readLine 

============================================================================*/
  private String readLine (InputStream is)
      throws IOException
    {
    int len = 0;
    char c;
    String line = "";
    do 
      {
      c = (char)is.read(); 
      len++;
      if (c == '\n')
           break; 
      if (c != '\r') line += c + "";
      } while (c != -1 && len <= DEMARKUS_MAX_LINE);
    if (len >= DEMARKUS_MAX_LINE)
      throw new IOException ("Response header line too long"); 
    return line;
    }


  }

