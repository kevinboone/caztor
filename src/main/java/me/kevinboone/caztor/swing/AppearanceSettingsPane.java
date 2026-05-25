/*=========================================================================
  
  Caztor

  AppearanceSettingsPane 

  Copyright (c)2021 Kevin Boone, GPLv3.0 

=========================================================================*/
package me.kevinboone.caztor.swing;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.text.*;
import me.kevinboone.caztor.base.*;

/** Implements the "Appearance" tab of the SettingsDialog.
*/
public class AppearanceSettingsPane extends SettingsPane
  {
  private MainWindow mainWindow;
  private JTextField documentBaseFontSize;
  private JCheckBox emojiStripBookmarks; 
  private JCheckBox gemtextLineAsPara; 
  private JComboBox linkCharacter; 
  private int oldDocumentBaseFontSize;
  private boolean oldEmojiStripBookmarks;
  private boolean oldGemtextLineAsPara;
  private int oldLinkCharacter;

/*=========================================================================
  
  Constructor

=========================================================================*/
  protected AppearanceSettingsPane (MainWindow mainWindow)
    {
    super ("appearance_settings_pane");

    this.mainWindow = mainWindow;
    oldDocumentBaseFontSize = config.getDocumentBaseFontSize();
    oldEmojiStripBookmarks = config.getEmojiStripBookmarks();
    oldGemtextLineAsPara = config.getGemtextLineAsPara();
    oldLinkCharacter = (int)(config.getDefltLink().charAt(0));

    GridBagLayout gl = new GridBagLayout ();
    setLayout (gl);
    setBorder(new EmptyBorder (10, 10, 10, 10));

    // Row 0, col 0
    GridBagConstraints gbc00 = new GridBagConstraints();
    gbc00.insets = new Insets (5, 5, 5, 5);
    gbc00.gridy = 0;
    gbc00.gridx = 0;
    gbc00.anchor = gbc00.EAST;
    JLabel documentBaseFontSizeLabel = createLabel 
      ("appearance_settings_pane_base_font_size");
    add (documentBaseFontSizeLabel, gbc00);

    // Row 0, col 1
    GridBagConstraints gbc01 = new GridBagConstraints();
    gbc01.insets = new Insets (5, 5, 5, 5);
    gbc01.gridy = 0;
    gbc01.gridx = 1;
    gbc01.anchor = gbc01.WEST;
    documentBaseFontSize = new JTextField (5);
    documentBaseFontSizeLabel.setLabelFor (documentBaseFontSize); 
    add (documentBaseFontSize, gbc01);

    // Row 1, col 0
    GridBagConstraints gbc10 = new GridBagConstraints();
    gbc10.insets = new Insets (5, 5, 5, 5);
    gbc10.gridy = 1;
    gbc10.gridx = 0;
    gbc10.anchor = gbc10.EAST;
    JLabel emojiStripBookmarksLabel = createLabel 
      ("appearance_settings_pane_emoji_strip_bookmarks");
    add (emojiStripBookmarksLabel, gbc10);

    // Row 1, col 1
    GridBagConstraints gbc11 = new GridBagConstraints();
    gbc11.insets = new Insets (5, 5, 5, 5);
    gbc11.gridy = 1;
    gbc11.gridx = 1;
    emojiStripBookmarks = new JCheckBox();
    emojiStripBookmarksLabel.setLabelFor (emojiStripBookmarks); 
    add (emojiStripBookmarks, gbc11);

    // Row 2, col 0
    GridBagConstraints gbc20 = new GridBagConstraints();
    gbc20.insets = new Insets (5, 5, 5, 5);
    gbc20.gridy = 2;
    gbc20.gridx = 0;
    gbc20.anchor = gbc20.EAST;
    JLabel gemtextLineAsParaLabel = createLabel 
      ("appearance_settings_pane_gemtext_lineaspara");
    add (gemtextLineAsParaLabel, gbc20);

    // Row 2, col 1
    GridBagConstraints gbc21 = new GridBagConstraints();
    gbc21.insets = new Insets (5, 5, 5, 5);
    gbc21.gridy = 2;
    gbc21.gridx = 1;
    gemtextLineAsPara = new JCheckBox();
    gemtextLineAsParaLabel.setLabelFor (gemtextLineAsPara); 
    add (gemtextLineAsPara, gbc21);

    // Row 3, col 0
    GridBagConstraints gbc30 = new GridBagConstraints();
    gbc30.insets = new Insets (5, 5, 5, 5);
    gbc30.gridy = 3;
    gbc30.gridx = 0;
    gbc30.anchor = gbc30.EAST;
    JLabel linkCharacterLabel = createLabel 
      ("appearance_settings_pane_link_character");
    add (linkCharacterLabel, gbc30);

    // Row 2, col 1
    GridBagConstraints gbc31 = new GridBagConstraints();
    gbc31.insets = new Insets (5, 5, 5, 5);
    gbc31.gridy = 3;
    gbc31.gridx = 1;
    linkCharacter = new JComboBox();
    linkCharacter.addItem (">");
    linkCharacter.addItem ("→");
    linkCharacter.addItem ("↠");
    linkCharacter.addItem ("↣");
    linkCharacter.addItem ("↦");
    linkCharacter.addItem ("⇀");
    linkCharacter.addItem ("⇒");
    linkCharacter.addItem ("⇨");
    linkCharacter.addItem ("→");
    linkCharacter.addItem ("➜");
    linkCharacter.addItem ("➝");
    linkCharacter.addItem ("➡");
    linkCharacter.addItem ("➣");
    linkCharacter.addItem ("➤");
    linkCharacter.addItem ("➨");
    linkCharacter.addItem ("➩");
    linkCharacter.addItem ("➱");
    linkCharacter.addItem ("➸");
    linkCharacter.addItem ("🢂");
    linkCharacter.addItem ("🡺");
    linkCharacter.addItem ("🡆");
    linkCharacter.addItem ("🠲");
    linkCharacter.setSelectedItem ("" + (char)oldLinkCharacter);
    linkCharacterLabel.setLabelFor (linkCharacter); 
    add (linkCharacter, gbc31);

    // Set digits only
    documentBaseFontSize.setDocument (new PlainDocument() 
      {
      public void insertString (int offs, String str, AttributeSet a) 
         throws BadLocationException 
        {
        if (str == null) return;
        if (str.matches("[0-9]+")) 
          {  
          super.insertString(offs, str, a);
          }
        }
      });

    documentBaseFontSize.setText ("" + oldDocumentBaseFontSize);
    emojiStripBookmarks.setSelected (oldEmojiStripBookmarks);
    gemtextLineAsPara.setSelected (oldGemtextLineAsPara);
    }

/*=========================================================================
  
  submit 

=========================================================================*/
  @Override
  protected void submit()
    {
    ccMode = ConfigChangeListener.CCMODE_NOUPDATE; 
    error = false;
   
    int newDocumentBaseFontSize;

    try
      {
      newDocumentBaseFontSize = Integer.parseInt 
        (documentBaseFontSize.getText());
      }
    catch (NumberFormatException e)
      {
      newDocumentBaseFontSize = oldDocumentBaseFontSize;
      }

    if (newDocumentBaseFontSize != oldDocumentBaseFontSize)
      {
      config.setDocumentBaseFontSize (newDocumentBaseFontSize);
      ccMode = ConfigChangeListener.CCMODE_REFRESH; 
      }

    boolean newEmojiStripBookmarks = emojiStripBookmarks.isSelected();
    if (newEmojiStripBookmarks != oldEmojiStripBookmarks)
      {
      config.setEmojiStripBookmarks (newEmojiStripBookmarks);
      ccMode = ConfigChangeListener.CCMODE_REFRESH; 
      }

    boolean newGemtextLineAsPara = gemtextLineAsPara.isSelected();
    if (newGemtextLineAsPara != oldGemtextLineAsPara)
      {
      config.setGemtextLineAsPara (newGemtextLineAsPara);
      ccMode = ConfigChangeListener.CCMODE_RELOAD; 
      }

    String s = (String)linkCharacter.getSelectedItem();
    if (s != null)
      {
      int newLinkCharacter = (int)(s.charAt(0)); 
      if (newLinkCharacter != oldLinkCharacter)
	{
	config.setDefltLinkCode (newLinkCharacter);
	ccMode = ConfigChangeListener.CCMODE_RELOAD; 
	} 
      }
    }
  }





