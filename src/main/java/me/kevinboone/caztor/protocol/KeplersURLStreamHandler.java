/*============================================================================

  Caztor

  KeplersURLStreamHandler

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol; 

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** A URLStreamHandler that instantiates KeplersConnection. 
*/
public class KeplersURLStreamHandler extends URLStreamHandler
  {
  public static final int DEFAULT_PORT = 10009;

  @Override
  protected URLConnection openConnection (URL url)
  throws IOException
    {
    return new KeplersConnection (url);
    }

  @Override
  protected int getDefaultPort ()
    {
    return DEFAULT_PORT;
    }
  }
