/*=========================================================================
  
  Caztor

  General bundle 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.bundles;
import java.util.*;

public class General extends ListResourceBundle 
  {
  private Object[][] contents = 
    {
    {"empty_window_text", "Caztor: a browser for small net protocols"},
    {"version_lc", "version"},
    {"version_uc", "Version"}
    };

  @Override
  public Object[][] getContents() 
     {
     return contents;
     }
  }

