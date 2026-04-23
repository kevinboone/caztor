/*============================================================================

  Caztor

  DemarkusURLStreamHandler

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol; 

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** A URLStreamHandler that instantiates DemarkusConnection. 
*/
public class DemarkusURLStreamHandler extends URLStreamHandler
  {
  @Override
  protected URLConnection openConnection (URL url)
  throws IOException
    {
    return new DemarkusConnection (url);
    }

  @Override
  protected int getDefaultPort ()
    {
    return DemarkusConnection.DEFLT_PORT; 
    }
  }

