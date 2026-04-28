/*=========================================================================
  
  Caztor

  Config 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.base;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import me.kevinboone.caztor.Defaults;
import me.kevinboone.caztor.Defaults;
import me.kevinboone.caztor.base.*;

/** Retrieves and manages application configuration. Use getConfig() to
    get the singleton instance of this class. */
public class Config extends Properties
  {
  private final static ResourceBundle messagesBundle = 
    ResourceBundle.getBundle ("me.kevinboone.caztor.bundles.Messages");

  private int logLevel = Logger.ERROR;
  private int bookmarkMaxMenu = 10;
  private boolean gemtextInlineImages = false;
  private boolean urlbarSearchEnabled = false;
  private boolean historyEnabled = false;
  private boolean emojiStripBookmarks = false;

  private Vector<ConfigChangeListener> listeners = new Vector<ConfigChangeListener>();

  private static Config instance = null;

/*=========================================================================
  
  addClientCert

=========================================================================*/
  /** Add a client certificate specification to the config file. Each
      specification has a keystore name and a keystore password, and
      is stored in the config file in the form 
      "clientcert.name=file password". */
  public void addClientCert (String name, String keystoreFile,
      String keystorePassword) 
    {
    setProperty (Strings.CLIENTCERT_TAG + name,
      keystoreFile + " " + keystorePassword);
    save();
    }

/*=========================================================================
  
  addConfigChangeListener

=========================================================================*/
  public void addConfigChangeListener (ConfigChangeListener l)
    {
    listeners.add (l);
    }

/*=========================================================================
  
  clearDefaultFileHandling 

=========================================================================*/
  public void clearDefaultFileHandling()
    {
    Enumeration keys = keys();
    while (keys.hasMoreElements())
      {
      String key = (String)keys.nextElement();
      if (key.startsWith (Strings.CONTENT_HANDLER_TAG))
        {
        remove (key);
        }
      }
    }

/*=========================================================================
  
  deriveProperties 

=========================================================================*/
  /* Calculate the values of the instance variables from the raw values
     read from the configuration file. */
  private void deriveProperties()
    {
    bookmarkMaxMenu = Integer.parseInt (getProperty 
      (Strings.BOOKMARK_MAX_MENU, "" + Defaults.DEFLT_BOOKMARK_MAX_MENU));

    logLevel = Integer.parseInt (getProperty (Strings.LOG_LEVEL, 
      "" + Defaults.DEFLT_LOG_LEVEL));
    Logger.setLevel (logLevel);

    gemtextInlineImages = getBooleanProperty 
      (Strings.GEMTEXT_INLINE_IMAGES, Defaults.DEFLT_GEMTEXT_INLINE_IMAGES);
   
    urlbarSearchEnabled = getBooleanProperty
      (Strings.URLBAR_SEARCH_ENABLED, Defaults.DEFLT_URLBAR_SEARCH_ENABLED); 

    historyEnabled = getBooleanProperty 
      (Strings.HISTORY_ENABLED, Defaults.DEFLT_HISTORY_ENABLED);

    emojiStripBookmarks = getBooleanProperty 
      (Strings.EMOJI_STRIP_BOOKMARKS, Defaults.DEFLT_EMOJI_STRIP_BOOKMARKS);
    }

/*=========================================================================
  
  getEmojiStripBookmarks

=========================================================================*/
  public boolean getEmojiStripBookmarks()
    {
    return emojiStripBookmarks;
    }

/*=========================================================================
  
  ensureAggregatedFeedsFileExists 

=========================================================================*/
  public void ensureAggregatedFeedsFileExists() throws IOException
    {
    String filename = getAggregatedFeedsFile();
    File file = new File (filename);
    if (file.exists()) return;
    file.createNewFile();
    FileUtil.appendStringToFile (filename, messagesBundle.getString 
      ("empty_aggregation_text"));
    }

/*=========================================================================
  
  ensureBookmarksFileExists 

=========================================================================*/
  public void ensureBookmarksFileExists() throws IOException
    {
    String filename = getBookmarksFile();
    File file = new File (filename);
    if (file.exists()) return;
    file.createNewFile();
    FileUtil.appendStringToFile (filename, "# " 
      + Strings.BOOKMARKS_COMMENTS + "\n");
    }

/*=========================================================================
  
  ensureFeedsFileExists 

=========================================================================*/
  public void ensureFeedsFileExists() throws IOException
    {
    String filename = getFeedsFile();
    File file = new File (filename);
    if (file.exists()) return;
    file.createNewFile();
    FileUtil.appendStringToFile (filename, "# " 
      + Strings.FEEDS_COMMENTS + "\n");
    }

/*=========================================================================
  
  ensureUserConfigFileExists 

=========================================================================*/
  public void ensureUserConfigFileExists() throws IOException
    {
    String filename = getUserConfigFilename();
    File file = new File (filename);
    if (file.exists()) return;

    // No config file. We need to retrieve, and then save,
    //   the template.
    InputStream is = getClass().getClassLoader().getResourceAsStream 
      ("templates/caztor.properties");
    String s = new BufferedReader (new InputStreamReader (is))
        .lines().collect (Collectors.joining("\n"));

    FileOutputStream fos = new FileOutputStream (file);
    PrintWriter pw = new PrintWriter (fos);
    pw.println (s);
    pw.flush();
    fos.close();
    is.close();
    }

/*=========================================================================
  
  getConfig 

=========================================================================*/
  public static Config getConfig()
    {
    if (instance == null)
      {
      instance = new Config();
      instance.load();
      }
    return instance;
    }

/*=========================================================================
  
  getContentHandlerAction

=========================================================================*/
  public int getContentHandlerAction (String mime)
    {
    String key = Strings.CONTENT_HANDLER_TAG + mime;
    String v = getProperty (key, null);
    if (v == null) return -1;
    return Integer.parseInt (v);
    }

/*=========================================================================
  
  setContentHandlerAction

=========================================================================*/
  public void setContentHandlerAction (String mime, int action)
    {
    String key = Strings.CONTENT_HANDLER_TAG + mime;
    setProperty (key, "" + action);
    }


/*=========================================================================
  
  getAggregatedFeedsFile

=========================================================================*/
  public String getAggregatedFeedsFile()
    {
    String feedsFile = getProperty (Strings.AGGREGATED_FEEDS_FILE, 
      Defaults.DEFLT_AGGREGATED_FEEDS_FILE);
    if (feedsFile == null)
      feedsFile = getStateDir() + File.separator 
        + Defaults.AGGREGATED_FEEDS_FILENAME;
    return feedsFile;
    }

/*=========================================================================
  
  getHomePage 

=========================================================================*/
  public String getHomePage()
    {
    Logger.in();
    String homePage = getProperty (Strings.URL_HOME, 
      Defaults.DEFLT_URL_HOME);
    Logger.log (getClass().getName(), Logger.INFO, 
      "Home page is " + homePage);
    Logger.out();
    return homePage;
    }

/*=========================================================================
  
  getHistoryFile

=========================================================================*/
  public String getHistoryFile()
    {
    String historyFile = getProperty (Strings.HISTORY_FILE, 
      Defaults.DEFLT_HISTORY_FILE);
    if (historyFile == null)
      historyFile = getStateDir() + File.separator + Defaults.HISTORY_FILENAME;
    return historyFile;
    }

/*=========================================================================
  
  getBookmarksFile

=========================================================================*/
  public String getBookmarksFile()
    {
    String bookmarkFile = getProperty (Strings.BOOKMARK_FILE, 
      Defaults.DEFLT_BOOKMARK_FILE);
    if (bookmarkFile == null)
      bookmarkFile = getStateDir() + File.separator 
        + Defaults.BOOKMARK_FILENAME;
    return bookmarkFile;
    }

/*=========================================================================
  
  getFeedsMaxAge

=========================================================================*/
  public int getFeedsMaxAge()
    {
    return Integer.parseInt (getProperty 
        (Strings.FEEDS_MAX_AGE, Defaults.DEFLT_FEEDS_MAX_AGE));
    }

/*=========================================================================
  
  getFeedsFile

=========================================================================*/
  public String getFeedsFile()
    {
    String feedsFile = getProperty (Strings.FEEDS_FILE, 
      Defaults.DEFLT_FEEDS_FILE);
    if (feedsFile == null)
      feedsFile = getStateDir() + File.separator 
        + Defaults.FEEDS_FILENAME;
    return feedsFile;
    }

/*=========================================================================
  
  getBookmarkMaxMenu

=========================================================================*/
  public int getBookmarkMaxMenu()
    {
    return bookmarkMaxMenu;
    }

/*=========================================================================
  
  getLogLevel 

=========================================================================*/
  public int getLogLevel()
    {
    return logLevel;
    }

/*=========================================================================
  
  getGemtextInlineImages 

=========================================================================*/
  public boolean getGemtextInlineImages()
    {
    return gemtextInlineImages;
    }

/*=========================================================================
  
  getGemtextLineAsPara

=========================================================================*/
  public boolean getGemtextLineAsPara()
    {
    return getBooleanProperty (Strings.GEMTEXT_LINEASPARA, 
      Defaults.DEFLT_GEMTEXT_LINEASPARA);
    }

/*=========================================================================
  
  getInlineImageWidth 

=========================================================================*/
  public int getInlineImageWidth()
    {
    return Integer.parseInt (getProperty 
        (Strings.INLINE_IMAGE_WIDTH, Defaults.DEFLT_INLINE_IMAGE_WIDTH));
    }

/*=========================================================================
  
  getNewWindowMode

=========================================================================*/
  public int getNewWindowMode ()
    {
    return Integer.parseInt (getProperty 
        (Strings.UI_NEW_WINDOW_MODE, Defaults.DEFLT_UI_NEW_WINDOW_MODE));
    }

/*=========================================================================
  
  getFeedsUpdateOnStartup

=========================================================================*/
  public boolean getFeedsUpdateOnStartup()
    {
    return getBooleanProperty (Strings.FEEDS_UPDATE_ON_STARTUP, 
      Defaults.DEFLT_FEEDS_UPDATE_ON_STARTUP);
    }

/*=========================================================================
  
  getIconsMono

=========================================================================*/
  public boolean getIconsMono()
    {
    return getBooleanProperty (Strings.UI_ICONS_MONO, 
        Defaults.DEFLT_UI_ICONS_MONO);
    }

/*=========================================================================
  
  getTimeoutSec

=========================================================================*/
  public int getConnectTimeout()
    {
    return Integer.parseInt (getProperty (Strings.CONNECT_TIMEOUT, 
      Defaults.DEFLT_CONNECT_TIMEOUT));
    }

/*=========================================================================
  
  getWindowWidth

=========================================================================*/
  public int getWindowWidth ()
    {
    return Integer.parseInt (getProperty (Strings.WINDOW_W, 
      Defaults.DEFLT_WINDOW_W));
    }

/*=========================================================================
  
  getWindowHeight

=========================================================================*/
  public int getWindowHeight ()
    {
    return Integer.parseInt (getProperty (Strings.WINDOW_H, 
      Defaults.DEFLT_WINDOW_H));
    }

/*=========================================================================
  
  getControlFont 

=========================================================================*/
  public String getControlFont()
    {
    return getProperty (Strings.UI_CONTROL_FONT, Defaults.
      DEFLT_UI_CONTROL_FONT);
    }

/*=========================================================================
  
  getUserFont

=========================================================================*/
  public String getUserFont()
    {
    return getProperty (Strings.UI_USER_FONT, 
      Defaults.DEFLT_UI_USER_FONT);
    }

/*=========================================================================
  
  getCustomCSSFile

=========================================================================*/
  public String getCustomCSSFile()
    {
    return getProperty (Strings.UI_DOCUMENT_CUSTOM_CSS, 
      Defaults.DEFLT_UI_DOCUMENT_CUSTOM_CSS);
    }

/*=========================================================================
  
  getTheme

=========================================================================*/
  public String getTheme()
    {
    return getProperty (Strings.UI_DOCUMENT_THEME, 
      Defaults.DEFLT_UI_DOCUMENT_THEME);
    }

/*=========================================================================
  
  getUrlbarSearchUrl

=========================================================================*/
  public String getUrlbarSearchUrl()
    {
    return getProperty (Strings.URLBAR_SEARCH_URL, 
      Defaults.DEFLT_URLBAR_SEARCH_URL);
    }

/*=========================================================================
  
  getDocumentBaseFontSize

=========================================================================*/
  public int getDocumentBaseFontSize()
    {
    String s = getProperty (Strings.UI_DOCUMENT_FONT_SIZE, 
      Defaults.DEFLT_UI_DOCUMENT_FONT_SIZE);
    return Integer.parseInt (s);
    }

/*=========================================================================
  
  getHistorySize

=========================================================================*/
  public int getHistorySize()
    {
    String s = getProperty (Strings.HISTORY_SIZE, 
      Defaults.DEFLT_HISTORY_SIZE);
    return Integer.parseInt (s);
    }

/*=========================================================================
  
  getIconSize

=========================================================================*/
  public int getIconSize()
    {
    String s = getProperty (Strings.UI_ICON_SIZE, 
      Defaults.DEFLT_UI_ICON_SIZE);
    return Integer.parseInt (s);
    }

/*=========================================================================
  
  getMaxRedirects

=========================================================================*/
  public int getMaxRedirects()
    {
    return Integer.parseInt (getProperty 
        (Strings.REDIRECT_MAX, Defaults.DEFLT_REDIRECT_MAX));
    }

/*=========================================================================
  
  getUrlbarSearchEnabled 

=========================================================================*/
  public boolean getUrlbarSearchEnabled()
    {
    return urlbarSearchEnabled;
    }

/*=========================================================================
  
  getHistoryEnabled 

=========================================================================*/
  public boolean getHistoryEnabled()
    {
    return historyEnabled;
    }

/*=========================================================================
  
  getBooleanProperty

=========================================================================*/
  public boolean getBooleanProperty (String name, boolean deflt)
    {
    String val = getProperty (name, deflt ? "1" : "0");
    if (val == null) return deflt;
    if (val.equals ("1")) return true;
    if (val.equals ("yes")) return true;
    if (val.equals ("true")) return true;
    if (val.equals ("on")) return true;
    return false;
    }

/*=========================================================================
  
  getStreamPlayer

=========================================================================*/
  public String getStreamPlayer()
    {
    String s = getProperty (Strings.STREAM_PLAYER, 
      Defaults.DEFLT_STREAM_PLAYER);
    return s; 
    }

/*=========================================================================
  
  fireSettingsChangedListeners 

=========================================================================*/
  public void fireSettingsChangedListeners (int ccMode)
    {
    Logger.in();
    int l = listeners.size();
    for (int i = 0; i < l; i++)
      listeners.elementAt(i).configChanged (ccMode);
    Logger.out();
    }

/*=========================================================================
  
  getIdents

=========================================================================*/
  public Set<String> getIdents()
    {
    HashSet<String> s = new HashSet<String>();

    Enumeration e = propertyNames();
    while (e.hasMoreElements())
      {
      String k = (String)e.nextElement();
      if (k.startsWith (Strings.CLIENTCERT_TAG))
        {
        String value = k.substring (Strings.CLIENTCERT_TAG.length());
        if (!value.equals ("any"))
          s.add (value);
        }
      }

    return s;
    }

/*=========================================================================
  
  getIdentssDir

=========================================================================*/
  public String getIdentsDir()
    {
    Logger.in();
    String identsDir = getStateDir() + File.separator 
      + Defaults.IDENTS_DIRNAME; 
    Logger.out();
    return identsDir;
    }

/*=========================================================================
  
  getDownloadsDir

=========================================================================*/
  public String getDownloadsDir()
    {
    Logger.in();
    String downloadsDir = getProperty (Strings.DOWNLOADS_DIR, 
      Defaults.DEFLT_DOWNLOADS_DIR);
    if (downloadsDir == null)
      downloadsDir = getStateDir() + File.separator 
        + Defaults.DOWNLOADS_DIRNAME; 
    Logger.out();
    return downloadsDir;
    }

/*=========================================================================
  
  getKeystoreSpecForIdent 

=========================================================================*/
public KeystoreSpec getKeystoreSpecForIdent (String ident)
    {
    String clientCertSpec = getProperty (Strings.CLIENTCERT_TAG + ident);
    if (clientCertSpec == null) return null;

    String[] tokens = clientCertSpec.trim().split ("\\s+");
    if (tokens.length != 2) 
      {
      Logger.log (getClass().getName(), Logger.WARNING, 
        "Bad client certificate specification: " + clientCertSpec);
      return null;
      }

    String clientCertKeyStoreFile = tokens[0];
    String clientCertKeyStorePassword = tokens[1];

    return new KeystoreSpec (clientCertKeyStoreFile, 
      clientCertKeyStorePassword);
    }


/*=========================================================================
  
  getStateDir

=========================================================================*/
  public String getStateDir()
    {
    Logger.in();
    String home = System.getProperty ("user.home");
    Logger.out();
    return home + File.separator + Defaults.STATE_DIR_NAME; 
    }

/*=========================================================================
  
  getUserConfigFilename

=========================================================================*/
  public String getUserConfigFilename()
    {
    return getStateDir() + File.separator + Defaults.PREFS_FILE;
    }

/*=========================================================================
  
  loadFromFile 

=========================================================================*/
  public void loadFromFile (String filename)
    {
    Logger.in();
    if (Logger.isDebug())
      Logger.log (getClass().getName(), Logger.INFO, 
         "Loading settings from " + filename);
    try (InputStream is = new FileInputStream (new File (filename)))
      {
      load (is);
      is.close();
      }
    catch (Exception e)
      {
      // This may not be an error
      Logger.log (getClass().getName(), Logger.DEBUG, e.toString());
      }
    deriveProperties();
    Logger.out();
    }

/*=========================================================================
  
  load

=========================================================================*/
  public void load()
    {
    // Make a new state directory. We have to do this somewhere,
    //   and it's best to do it early, before we try to save
    //   anything there.
    Logger.in();
    new File (getStateDir()).mkdir();
    new File (getIdentsDir()).mkdir();
    new File (getDownloadsDir()).mkdir();

    Logger.log (getClass().getName(), Logger.INFO, 
      "Loading system-wide configuration");
    // This won't work on Windows, but it won't do any harm.
    String sysPropsFile = "/etc/caztor/" + Defaults.SYS_PREFS_FILE; 
    loadFromFile (sysPropsFile);

    Logger.log (getClass().getName(), Logger.DEBUG, 
      "Loading user configuration");
    String propsFile = getUserConfigFilename(); 
    Logger.log (getClass().getName(), Logger.DEBUG, 
      "User properties file is " + propsFile);
    loadFromFile (propsFile);

    fireSettingsChangedListeners (ConfigChangeListener.CCMODE_REFRESH);
    Logger.out();
    }

/*=========================================================================
  
  removeConfigChangeListener 

=========================================================================*/
  public void removeConfigChangeListener (ConfigChangeListener l)
    {
    listeners.remove (l);
    }

/*=========================================================================
  
  removeIdent 

=========================================================================*/
  public void removeIdent (String hostname)
    {
    remove (Strings.IDENT_TAG + hostname);
    }

/*=========================================================================
  
  save 

=========================================================================*/
  public void save()
    {
    Logger.in();
    String propsFile = getUserConfigFilename(); 
    saveToFile (propsFile);
    Logger.out();
    }

/*=========================================================================
  
  saveToFile 

=========================================================================*/
  private void saveToFile (String filename)
    {
    Logger.in();
    if (Logger.isDebug())
      Logger.log (getClass().getName(), Logger.INFO, 
        "Saving properties to " + filename);
    try (OutputStream os = new FileOutputStream (new File (filename)))
      {
      store (os, Strings.PROPS_COMMENTS);
      os.close();
      }
    catch (Exception e)
      {
      // This may not be an error
      Logger.log (getClass().getName(), Logger.DEBUG, e.toString());
      }
    deriveProperties();
    Logger.out();
    }

/*=========================================================================
  
  setConnectTimeout

=========================================================================*/
  public void setConnectTimeout (int n)
    {
    setProperty (Strings.CONNECT_TIMEOUT, "" + n);
    }

/*=========================================================================
  
  setDocumentBaseFontSize 

=========================================================================*/
  public void setDocumentBaseFontSize (int px)
    {
    setProperty (Strings.UI_DOCUMENT_FONT_SIZE, "" + px);
    }

/*=========================================================================
  
  setDownloadsDir

=========================================================================*/
  public void setDownloadsDir (String d)
    {
    Logger.in();
    setProperty (Strings.DOWNLOADS_DIR, d);
    Logger.out();
    }

/*=========================================================================
  
  setEmojiStripBookmark 

=========================================================================*/
  public void setEmojiStripBookmarks (boolean f)
    {
    emojiStripBookmarks = f;
    setProperty (Strings.EMOJI_STRIP_BOOKMARKS, "" + f);
    }

/*=========================================================================
  
  setFeedsMaxAge

=========================================================================*/
  public void setFeedsMaxAge (int n)
    {
    setProperty (Strings.FEEDS_MAX_AGE, "" + n);
    }

/*=========================================================================
  
  setHomePage 

=========================================================================*/
  public void setHomePage (String uri)
    {
    if (Logger.isDebug())
      Logger.log (Logger.class, Logger.INFO, "setting home page to " + uri);
    setProperty (Strings.URL_HOME, uri);
    }

/*=========================================================================
  
  setCustomCssFile

=========================================================================*/
  public void setCustomCssFile (String file)
    {
    setProperty (Strings.UI_DOCUMENT_CUSTOM_CSS, file);
    }

/*=========================================================================
  
  setHistoryEnabled

=========================================================================*/
  public void setHistoryEnabled (boolean enabled)
    {
    historyEnabled = enabled;
    setProperty (Strings.HISTORY_ENABLED, "" + enabled);
    }

/*=========================================================================
  
  setIdentForHostname

  writes a property "ident.{hostname}={ident}"

  Note that "none" is a valid ident, but null is not. to remove an
  ident from a hostname, use removeIdent()

=========================================================================*/
  public void setIdentForHostname (String hostname, String ident)
    {
    Logger.in();
    if (Logger.isDebug())
      Logger.log (Logger.class, Logger.INFO, "setting ident for " + hostname
        + " to " + ident);
    setProperty (Strings.IDENT_TAG + hostname, ident);
    Logger.out();
    }

/*=========================================================================
  
  setTheme

=========================================================================*/
  public void setTheme (String theme)
    {
    setProperty (Strings.UI_DOCUMENT_THEME, theme);
    }

/*=========================================================================
  
  setGemtextInlineImages 

=========================================================================*/
  public void setGemtextInlineImages (boolean f)
    {
    gemtextInlineImages = f;
    setProperty (Strings.GEMTEXT_INLINE_IMAGES, "" + f);
    }

/*=========================================================================
  
  setGemtextLineAsPara

=========================================================================*/
  public void setGemtextLineAsPara (boolean f)
    {
    setProperty (Strings.GEMTEXT_LINEASPARA, "" + f);
    }

/*=========================================================================
  
  setHistorySize

=========================================================================*/
  public void setHistorySize (int n)
    {
    setProperty (Strings.HISTORY_SIZE, "" + n);
    }

/*=========================================================================
  
  setIconSize

=========================================================================*/
  public void setIconSize (int w)
    {
    setProperty (Strings.UI_ICON_SIZE, "" + w);
    }


/*=========================================================================
  
  setFeedsUpdateOnStartup

=========================================================================*/
  public void setFeedsUpdateOnStartup (boolean f)
    {
    setProperty (Strings.FEEDS_UPDATE_ON_STARTUP, "" + f);
    }

/*=========================================================================
  
  setIconsMono

=========================================================================*/
  public void setIconsMono (boolean f)
    {
    setProperty (Strings.UI_ICONS_MONO, "" + f);
    }

/*=========================================================================
  
  setInlineImageWidth 

=========================================================================*/
  public void setInlineImageWidth (int n)
    {
    setProperty (Strings.INLINE_IMAGE_WIDTH, "" + n);
    }

/*=========================================================================
  
  setUrlbarSearchEnabled 

=========================================================================*/
  public void setUrlbarSearchEnabled (boolean f)
    {
    urlbarSearchEnabled = f;
    setProperty (Strings.URLBAR_SEARCH_ENABLED, "" + f);
    }

/*=========================================================================
  
  setStreamPlayer 

=========================================================================*/
  public void setStreamPlayer (String s)
    {
    setProperty (Strings.STREAM_PLAYER, s);
    }

/*=========================================================================
  
  setUrlbarSearchUrl

=========================================================================*/
  public void setUrlbarSearchUrl (String url)
    {
    setProperty (Strings.URLBAR_SEARCH_URL, url);
    }

/*=========================================================================
  
  setWindowHeight

=========================================================================*/
  public void setWindowHeight (int h)
    {
    setProperty (Strings.WINDOW_H, "" + h);
    }

/*=========================================================================
  
  setWindowWidth

=========================================================================*/
  public void setWindowWidth (int w)
    {
    setProperty (Strings.WINDOW_W, "" + w);
    }


  }


