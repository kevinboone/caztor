/*=========================================================================
  
  Caztor

  CaztorException 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;

/** This exception was supposed to have been the base for many different
    application exceptions. In practice, it isn't used all that much.
*/
public class CaztorException extends Exception
{
public CaztorException (String s)
  {
  super (s);
  }
}

