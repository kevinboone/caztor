/*============================================================================

  Caztor

  KeplerURLStreamHandler

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol; 

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** A URLStreamHandler that instantiates KeplerConnection. 
*/
public class KeplerURLStreamHandler extends URLStreamHandler
  {
  public static final int DEFAULT_PORT = 2009;

  @Override
  protected URLConnection openConnection (URL url)
  throws IOException
    {
    return new KeplerConnection (url);
    }

  @Override
  protected int getDefaultPort ()
    {
    return DEFAULT_PORT;
    }
  }
