/*============================================================================

  Caztor

  ResponseContent 

  A carrier for data received from a URL handler, including the exception,
  if any.

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.base;
import java.net.URL;

/** Holds all the information that came from an asynchronous file transfer.
    This includes the data and the MIME type, and any exception that was
    thrown. Transfers are asynchronous, so the background thread that does
    the transfer bundles up the content in single instance of this class,
    and passes it to the user interface when completed. */ 
public class ResponseContent 
  {
  private byte[] content;
  private long contentLength;
  private String mime;
  private Exception exception;
  private URL url;
  private String certinfo;
  private long lastRetrievedFromSource;
  private long lastUpdated;
  private long expires;

  public ResponseContent (URL url)
    {
    this.url = url;
    mime = null;
    exception = null;
    content = null;
    lastUpdated = -1L;
    lastRetrievedFromSource = -1L;
    contentLength = -1L;
    expires = -1L;
    }

  public String getCertinfo() { return certinfo; } // Might validly by null
  public byte[] getContent() { return content; }
  public long getContentLength() { return contentLength; } // Reported by server
  public Exception getException() { return exception; }
  public long getExpires () { return expires; } // Reported by server
  public long getLastUpdated() { return lastUpdated; } // Reported by server
  public long getLastRetrievedFromSource() { return lastRetrievedFromSource; } 
  public String getMime() { return mime; }
  public URL getURL() { return url; }

  public void setCertinfo (String certinfo) { this.certinfo = certinfo; }
  public void setContent (byte[] content) { this.content = content; }
  public void setContentLength (long l) { this.contentLength = l; }
  public void setExpires (long l) { this.expires = l; }
  public void setLastRetrievedFromSource (long l) { this.lastRetrievedFromSource = l; }
  public void setLastUpdated (long l) { this.lastUpdated = l; }
  public void setMime (String mime) { this.mime = mime; }
  public void setException (Exception exception) { this.exception = exception; }

  }
