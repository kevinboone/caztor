/*============================================================================

  Caztor

  NotChangedException 

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol;

import java.io.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.net.*;

/** An Exception that indicates that a requested document has not changed
    since the time specified by the client. Only the kepler protocol
    uses this.
*/
public class NotChangedException extends IOException 
  {
  URL url;
  int status;

  public NotChangedException (URL url, int status)
    {
    super ("Not changed -- TODO");
    this.url = url;
    this.status = status;
    }

  public URL getURL() { return url; }
  public int getStatus() { return status; }
  }



