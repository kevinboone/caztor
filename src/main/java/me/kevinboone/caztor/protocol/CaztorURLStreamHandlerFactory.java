/*============================================================================

  Caztor

  CaztorURLStreamHandlerFactory

  Creates a stream handler for each protocol we support. The interface
  to the JVM platform is to pass an instance of this class to
  URL.setURLStreamHandlerFactory

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.protocol;
import java.net.*;
import java.io.*;

/** A factory for creating our customer URLStreamHandler instances,
    according to the URL protocol. This class is registered with the
    JVM by calling setURLStreamHandlerFactory().
*/ 
public class CaztorURLStreamHandlerFactory implements URLStreamHandlerFactory 
  {
  @Override
  public URLStreamHandler createURLStreamHandler(String protocol) 
    {
    if ("gemini".equals(protocol)) 
      {
      return new GeminiURLStreamHandler();
      }
    else if ("nex".equals(protocol)) 
      {
      return new NexURLStreamHandler();
      }
    else if ("spartan".equals(protocol)) 
      {
      return new SpartanURLStreamHandler();
      }
    else if ("gopher".equals(protocol)) 
      {
      return new GopherURLStreamHandler();
      }
    else if ("about".equals(protocol)) 
      {
      return new AboutURLStreamHandler();
      }
    else if ("mark".equals(protocol)) 
      {
      return new DemarkusURLStreamHandler();
      }
    return null;
    }
  }

