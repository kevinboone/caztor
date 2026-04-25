/*=========================================================================
  
  Caztor

  IdentException 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;

/** This exception is thrown from methods in the IdentUtil class, to
    indicate that an identity name selected by the user is invalid in
    some way.
*/
public class IdentException extends CaztorException 
{
public IdentException (String s)
  {
  super (s);
  }
}


