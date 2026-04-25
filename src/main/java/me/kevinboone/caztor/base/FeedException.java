/*=========================================================================
  
  Caztor

  FeedException 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;

/** An exception thrown to indicate a problem with feed parsing.
*/
public class FeedException extends CaztorException
{
public FeedException (String s)
  {
  super (s);
  }

public FeedException (Exception e)
  {
  super ("Error parsing feed: " + e.toString());
  }
}


