/*=========================================================================
  
  Caztor

  CaztorException 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;

/** This exception is thrown after receiving a redirect response,
    when there are too many redirects already. This mechanism is
    to protect against redirect loops. 
*/
public class TooManyRedirectsException extends CaztorException
{
int redirects;
public TooManyRedirectsException (int n)
  {
  super ("Too many redirects");
  redirects = n;
  }

public int getRedirects()
  {
  return redirects;
  }
}


