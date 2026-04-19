# Styling Caztor's document window

You can style Caztor's main document window using CSS stylesheets. Be aware,
though, that Java's CSS support is pretty ancient, and only a limited range of
styles are useable. In addition, "modern" features, like being able to set
sizes in "em" units, aren't supported.

Caztor has four built-in themes, called 'light' (the default) 'dark',
'light\_pastel' and 'dark\_pastel'. 
To select one of these, use _Theme_ tab of the _Settings_ dialog, or 
apply this setting in the 
[configuration file](config_file,md):

    ui.document.theme=name

To change the font sizes with the built-in themes, use a setting such as

    ui.document.font.size=14

This size is in pixels, and is for ordinary, unformatted text. Elements
like headings will have sizes based on this figure, scaled appropriately.

The only other supported theme setting is `custom`. If you use this setting, you
must also supply a stylesheet as a CSS file. Again, you can set this filename
using the _Settings_ dialog, or in the configuration file:

    ui.document.custom.css=/path/to/stylesheet.css

The CSS files for the built-in themes are in the `samples/` directory in the
source code bundle. You could use these as the basis for customization.  Note
that the built-in themes have place-holders for sizes, so multiple sizes
can be set using one configuration entry. Custom CSS themes can use these
place-holders, too. Of course, you could just the size of each element
independently, if you prefer, but then run-time changes to the text
size wouldn't work.

Caztor uses the following styleable elements: 

- "a" -- links 
- "blockquote" -- block quotes
- "body" -- background and layout
- "code" -- technical terms, 
- "h1...h3" -- headings 
- "p" -- ordinary text
- "pre" -- pre-formatted text
- "ul" -- lists

The fonts that can be applied to these styles are JVM fonts, which may be named
differently to platform fonts (and, in some cases, are more extensive). To get
a list all JVM fonts, run Caztor like this: 

    java -Dcaztor.dumpfonts -jar caztor-1.0.jar

You can test changes to a custom stylesheet by refreshing the display (ctrl+R).

[Documentation index](index.md)

