#!/bin/bash
# Creates a Caztor configuration $HOME/.caztor, based on an existing
#  JGemini configuration $HOME/.jgemini. This script assumes no
#  existing Caztor settings, and it only works with configuration
#  created by the JGemini user interface. Some custom changes
#  probably won't convert correctly.

JG_SETTINGS_DIR="$HOME/.jgemini"
CZ_SETTINGS_DIR="$HOME/.caztor"
JG_IDENTS_DIR="$JG_SETTINGS_DIR/idents"
CZ_IDENTS_DIR="$CZ_SETTINGS_DIR/idents"

if [ -d "$CZ_SETTINGS_DIR" ] ; then
  echo A Caztor settings directory already exists: $CZ_SETTINGS_DIR
  echo This script "can't" merge settings: sorry
  exit
fi

replace_in_properties()
 {
 file="$1"
 sed -i s/.jgemini/.caztor/ "$file" 
 }
 
mkdir -p "$CZ_IDENTS_DIR"
cp -p "$JG_IDENTS_DIR"/* "$CZ_IDENTS_DIR"/
cp -p "$JG_SETTINGS_DIR/aggregated_feeds.gmi" "$CZ_SETTINGS_DIR"/
cp -p "$JG_SETTINGS_DIR/feeds.gmi" "$CZ_SETTINGS_DIR"/
cp -p "$JG_SETTINGS_DIR/jgemini.history" "$CZ_SETTINGS_DIR"/caztor.history
cp -p "$JG_SETTINGS_DIR/jgemini.properties" "$CZ_SETTINGS_DIR"/caztor.properties
cp -p "$JG_SETTINGS_DIR/bookmarks.gmi" "$CZ_SETTINGS_DIR"/
replace_in_properties "$CZ_SETTINGS_DIR"/caztor.properties

