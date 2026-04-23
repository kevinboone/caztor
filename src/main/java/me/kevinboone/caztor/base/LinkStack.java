/*=========================================================================
  
  Caztor

  LinkStack  

  Copyright (c)2021-6 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;
import java.util.*;
import java.net.*;


public class LinkStack
  {
  Vector<URL> links = new Vector<URL>();
  int current = -1;
  int top = -1;

  public URL advance()
    {
    //System.out.println ("advance current=" + current + " top=" + top);
    if (current < top)
      {
      current++;
      URL ret = links.elementAt (current);
      return ret;
      }
    else
      return null;
    }

  public boolean isAtTop()
    {
    return current >= top;
    }

  public URL peek()
    {
    if (current < 0) return null;
    URL ret = links.elementAt (current);
    return ret;
    }

  public URL pop()
    {
    if (current < 0) return null;
    URL ret = links.elementAt (current);
    current--;
    return ret;
    }

  public void push (URL url)
    {
    current++;
    if (links.size() < (current + 1)) links.setSize (current + 1);
    links.setElementAt (url, current);
    if (current > top) top = current;
    //System.out.println ("push current=" + current + " top=" + top);
    }

  public void setTopToCurrent()
    {
    top = current;
    }

  public int size()
    {
    return current + 1;
    }
  }

