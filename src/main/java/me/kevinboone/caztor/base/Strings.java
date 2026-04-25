/*=========================================================================
  
  Caztor

  Strings 

  Copyright (c)2026 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;

public interface Strings
  {
  // Header of user properties file
  public final static String PROPS_COMMENTS = "Caztor user configuration"; 
  // Header of bookmarks file
  public final static String BOOKMARKS_COMMENTS = "Caztor bookmarks"; 
  // Header of feeds file
  public final static String FEEDS_COMMENTS = "Caztor subscriptions"; 

  // Settings file tags

  public final static String CLIENTCERT_TAG = "clientcert.";
  public final static String IDENT_TAG = "ident.";
  public final static String NONE_IDENT_TAG = "none";
  public final static String CONTENT_HANDLER_TAG = "handler.";

  // Settings file keys

  public final static String BOOKMARK_FILE = "bookmark.file";
  public final static String BOOKMARK_MAX_MENU = "bookmark.max.menu";
  public final static String CONNECT_TIMEOUT = "connect.timeout";
  public final static String DOWNLOADS_DIR = "downloads.directory";
  public final static String EMOJI_STRIP_BOOKMARKS = "emoji.strip.bookmarks";
  public final static String FEEDS_FILE = "feeds.file";
  public final static String FEEDS_MAX_AGE = "feeds.max_age";
  public final static String FEEDS_UPDATE_ON_STARTUP = "feeds.update_on_startup";
  public final static String AGGREGATED_FEEDS_FILE = "feeds.aggregated.file";
  public final static String GEMTEXT_INLINE_IMAGES = "gemtext.inline.images";
  public final static String LOG_LEVEL = "log.level";
  public final static String HISTORY_FILE = "history.file";
  public final static String HISTORY_SIZE = "history.size";
  public final static String HISTORY_ENABLED = "history.enabled";
  public final static String REDIRECT_MAX = "redirect.max";
  public final static String INLINE_IMAGE_WIDTH = "inline.image.width";
  public final static String STREAM_PLAYER = "stream.player";
  public final static String UI_CONTROL_FONT = "ui.control_font"; 
  public final static String UI_DOCUMENT_CUSTOM_CSS = "ui.document.custom.css";
  public final static String UI_DOCUMENT_THEME = "ui.document.theme";
  public final static String UI_USER_FONT = "ui.user_font"; 
  public final static String UI_DOCUMENT_FONT_SIZE = "ui.document.font.size";
  public final static String UI_ICON_SIZE = "ui.icon.size";
  public final static String UI_ICONS_MONO = "ui.icons.mono";
  public final static String UI_NEW_WINDOW_MODE = "ui.new_window";
  public final static String URLBAR_SEARCH_ENABLED = "urlbar.search.enabled";
  public final static String URLBAR_SEARCH_URL = "urlbar.search.url";
  public final static String URL_HOME = "url.home";
  public final static String WINDOW_W = "window.w";
  public final static String WINDOW_H = "window.h";

  // URLs
  
  // "New identity in new keystore" dialog
  public final static String DOC_EDIT_BOOKMARKS = 
    "about:/edit_bookmarks_dialog.md"; 
  public final static String DOC_EDIT_SETTINGS = 
    "about:/edit_settings_dialog.md"; 
  public final static String DOC_INDEX = 
    "about:/index.md"; 
  public final static String DOC_RELEASE_NOTES = 
    "about:/release_notes.md"; 
  public final static String DOC_NEW_IDENT_DIALOG = 
    "about:/new_identity_dialog.md"; 
  // "Attach identity to keystore" dialog
  public final static String DOC_ATTACH_IDENT_DIALOG = 
    "about:/attach_identity_dialog.md"; 
  public final static String DOC_SET_IDENT_DIALOG = 
    "about:/set_identity_dialog.md"; 
  public final static String DOC_SELECT_ACTION_DIALOG = 
    "about:/select_action_dialog.md"; 
  public final static String DOC_THEME_DIALOG = 
    "about:/theme_dialog.md"; 
  public final static String DOC_SETTINGS_DIALOG = 
    "about:/settings_dialog.md"; 
  public final static String DOC_DOWNLOADS_DIALOG = 
    "about:/downloads_dialog.md"; 
  public final static String DOC_FEED_MANAGER_DIALOG = 
    "about:/feed_aggregator_dialog.md"; 

  }


