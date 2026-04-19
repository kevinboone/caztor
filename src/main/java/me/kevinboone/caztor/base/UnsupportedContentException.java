/*============================================================================

  Caztor

  HandleDifferentlyException 

  Copyright (c)2021-2026 Kevin Boone, GPLv3.0

============================================================================*/
package me.kevinboone.caztor.base;

/** 
  Thrown by the ContentFetcher class to indicate that it received
  a kind of document it can't process.
*/
public class UnsupportedContentException extends CaztorException 
  {
  public UnsupportedContentException (String url)
    {
    super ("Unsupported content: " + url); // TODO
    }
  }




