/*=========================================================================
  
  Caztor

  Defaults 

  Copyright (c)2026 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor;
import me.kevinboone.caztor.base.Logger;

public interface Defaults
  {
  // Default home page
  public final static String DEFLT_URL_HOME = "about:/caztor_overview.md";

  // Settings file default values

  public final static int DEFLT_LOG_LEVEL = Logger.ERROR;
  public final static boolean DEFLT_EMOJI_STRIP_BOOKMARKS = false;
  public final static String DEFLT_CONNECT_TIMEOUT = "10"; // Sec
  public final static String DEFLT_FEEDS_MAX_AGE = "14"; // Days
  public final static String DEFLT_HISTORY_SIZE = "30";
  public final static String DEFLT_INLINE_IMAGE_WIDTH = "600";
  public final static String DEFLT_UI_CONTROL_FONT = "Sans 20; Emoji 20";
  public final static String DEFLT_UI_DOCUMENT_THEME = "light";
  public final static String DEFLT_UI_USER_FONT = "Sans 20; Emoji 20";
  public final static String DEFLT_UI_DOCUMENT_FONT_SIZE = "16";
  public final static String DEFLT_UI_NEW_WINDOW_MODE = "0";
  public final static String DEFLT_UI_ICON_SIZE = "24";
  public final static String DEFLT_REDIRECT_MAX = "6";
  public final static String DEFLT_URLBAR_SEARCH_URL = "gemini://tlgs.one/search";
  public final static String DEFLT_WINDOW_H = "900";
  public final static String DEFLT_WINDOW_W = "1200";
  public final static String DEFLT_STREAM_PLAYER = "vlc -";

  // File and directory names

  public final static String BOOKMARK_FILENAME = "bookmarks.gmi"; 
  public final static String FEEDS_FILENAME = "feeds.gmi"; 
  public final static String AGGREGATED_FEEDS_FILENAME = "aggregated_feeds.gmi"; 
  public final static String DOWNLOADS_DIRNAME = "downloads"; 
  public final static String HISTORY_FILENAME = "caztor.history";
  public final static String IDENTS_DIRNAME = "idents"; 
  public final static String PREFS_FILE = "caztor.properties"; 
  public final static String STATE_DIR_NAME = ".caztor"; 
  public final static String SYS_PREFS_FILE = "caztor.properties"; 

  // Sizes

  public static final int DIALOG_ROWS = 6;
  public static final int DIALOG_COLS = 25;
  }


