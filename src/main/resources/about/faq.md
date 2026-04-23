# Caztor FAQ

## Installation

_How do I install Caztor?_

You don't need to do anything in particular. Just copy the program's JAR file
`caztor-1.0.jar` to any convenient directory, and run it using `java -jar
caztor-1.0.jar`. Or, on Windows, just double-click the JAR file in the
file manager.

You may prefer a more integrated installation; for more information, see the
[page on installation](installing_and_running.md).

_What Java version do I need?_

Any Java 11 or later should be fine. Because the Gemini protocol is based on
TLS, you might find old, or cryptographically limited, JVMs don't work
properly. Caztor does run under Java 8, but the limited TLS support 
might be problematic. 

_Why can't Caztor provide its own emoji fonts?_

Installing an emoji font is a slight nuisance, but it should be a one-time
task. There are redistributable emoji fonts that Caztor could bundle, but
loading a font explicitly doesn't make the document viewer use it. 

Caztor's document viewer is based on Java's built-in HTML renderer. Converting
all incoming documents to HTML is flexible, and allows for future expansion.
After all, pretty much anything that can be viewed can be converted to HTML.
Unfortunately, the built-in document viewer won't use anything except platform
fonts, so bundling emoji fonts wouldn't be helpful. 

_Why "Caztor"?_

"Castor" is one of the stars in the Gemini constellation.  Changing the "s" to
a "z" gives a name that isn't widely used. All the other names associated with
the Gemini protocol are so widely used, that its difficult to find anything with
a web search.

_Why won't Caztor import settings from JGemini?_

Because too many names have changed, and I'm not sure it's worth the
effort of adding code to convert everything. For Linux users, there's
a script in the `samples/` directory of the source code bundle that
will create a new Caztor configuration from an existing JGemini
configuration -- provided you haven't changed too many of the stock
file locations. 

## Basic features

_How do I set the home page?_

If the relevant page is in the viewer, select _Edit|Settings_ from the
menu to open the [Settings dialog](settings_dialog.md), and then select
the _Home_ tab. Alternatively, add an entry `url.home` to the [configuration
file](config_file.md). 

_Why does Caztor not store my URL history between sessions?_

People who use Gemini and similar systems are often quite concerned about
privacy. Storing browsing history, etc., is a potential privacy hazard.
Although there are ways to mitigate this hazard, it's easier,
and safer, if Caztor simply didn't store it at all, unless you 
expressly ask it to. 

To allow Caztor to store URL history, go to the _History_ tab in 
the [Settings dialog](settings_dialog), and check the box. 
Alternatively add `history.enabled=1` to the 
[configuration file](config_file.md). 

_How do I clear my URL history?_

Go to the _History_ tab in 
the [Settings dialog](settings_dialog), and click "Clear history".
Unless you disable the saving of URL history, Caztor will continue
to add new URLs to the history after clearing existing ones. 

_How do I stop slow connections timing out?_

The default connection timeout, for all protocols, is ten seconds.
You can change this in the [configuration file](config_file.md),
by adding a setting:

    connect.timeout=20

or whatever time in seconds you prefer.

_How do I open a local .gmi file?_

From the command line, just run

    java -jar /path/to/caztor-1.0.jar [filename]

Depending on how you installed Caztor, you might simply be able to click on
a `.gmi` file in a file manager.

_How does Caztor handle images?_

With Markdown documents, images are in-lined into the text by default. With
Gemtext documents, in-lining behaviour is controlled by the configuration file
property `gemtext.inline.images`, which takes values `yes` or `no`. You
can also use the _Images_ tab of the _Settings_ dialog to control this
behaviour.

If the images are in-lined, they are all displayed with the same width,
controlled by the property `inline.image.width`. This is because Gemtext
provides no way to set a particular image size, and it often isn't helpful to
let the image display at its full size.

_How do I see an image at full size?_

Right-click the caption or URI under the image, and select _Open_ 
or _Open in new window_ from the menu. This will display the image without size
constraints.

_How do I download an image?_

Right-click the caption or URI under the image, or the link to the image, and
select _Download_.  The _File|Save_ menu won't work on a window that contains
only an image, for tedious technical reasons.

_How can I get back the monochrome toolbar icons from previous releases?_

Set `ui.icons.mono=true` in the [configuration file](config_file.md). 

_Why can't I see a progress indicator when downloading a large document or image?_

The [Downloads dialog](downloads_dialog.md) shows the amount of data received
from the server, but it doesn't show how close to complete the transfer is.
None of the protocols Caztor supports provide any indication of the size of
the file, so there's no way for Caztor to know this. 

_What happens if I follow a non-Gemini link in a Gemtext page?_

If it's a link to a protocol that Java understands, like `http:`, then Caztor
will pass the link to the desktop, which will usually do something useful. If
it's not a recognized protocol, like `telnet:`, nothing much will happen. This
is a limitation in Java's HTML renderer -- it's not easy to fix in Caztor,
without reimplementing the whole renderer. 

_What document types can Caztor display?_

Gemtext, plain text, Markdown, and gophermaps, along with images in
GIF, PNG, and JPEG format.

In a client-server interaction, the file type is determined by the response
from the server, with protocols that provide that information. For local files,
it's just based on the filename extension. The same is true for protocols like
Gopher, which don't give detailed file type information.

_How do I go to the top-level directory of a Gemini capsule?_

Use the _Go|Site root_ menu command.  Caztor tries to do the same as the Lagrange
browser, and use a reasonable guess at what the "root" path amounts to. In many
cases it will just be `/`. However, in a URI that contains a username
(`~fred`), then it's more useful to interpret the top level of the user's
directory as the root, rather than that of the service as a whole. 

_Why don't the Home/End keys work?_

Some keystrokes only work when the input focus is in the document viewer window
(and not, for example, in the URL bar). Just click the document.

_How do I disable the use of the URL bar for searching?_

In the configuration file, set `urlbar.search.enabled=0`, or got to the
_Search_ tab of the _Settings_ dialog.

_Where does Caztor save downloaded documents?_

If you use the _Download_ menu command from the right-click menu, Caztor will
always prompt for a filename. If you left-click a link, Caztor will ask
whether you want to specify a filename or not. If you choose not to, Caztor
will store the file in the default downloads directory, which is usually
`$HOME/.caztor/downloads`.

If you tell Caztor to hand off a file to the desktop, Caztor will download to
a file in the platform's temporary directory. The location and naming of these
files is, perhaps, unimportant, as Caztor will delete them when it exits.

_Does Caztor include a media player?_

No, but you can configure it to send media to an external player, either as a
complete file, or as a stream that is never stored on disk. For more
information, see the page on 
[media and streaming](media_and_streaming.md).

_Can I have feed subscriptions updated automatically,_ 
_without having to start the process manually?_

The relevant setting is in the _Feeds_ tab of the _Settings_ dialog box.  This
feature is disabled by default, because it's best to ensure that feed
aggregation is working properly, before letting it happen automatically. 

## Configuration

_How do I change the document fonts?_

You'll have to provide a custom stylesheet in CSS format. See the
page on [styling](styling.md). 

_How do I find out what fonts are available?_
 
    java -jar caztor-1.0.jar -Dcaztor.dumpfonts 

_The screen font is too small. What should I do?_

You can increase or decrease the base font size at runtime using ctrl-[ and
ctrl-].  To make a permanent change, use the _Appearance_ tab of the _Settings_
dialog, or edit the `ui.document.font.size` setting in the user configuration
file.

## Bookmarks

_In what format are bookmarks shown in the bookmarks editor?_

Bookmarks are stored in the local bookmarks file as links in a Gemtext document. 
Every time you bookmark a page, Caztor just appends a new link for the
bookmark. You can edit the bookmarks however you wish, so long as it remains
a valid Gemtext document, and you don't mind having new links added to the
end. 

Therefore, bookmarks are just Gemtext links, with the format:

    => {URL} {text} 

_Where are bookmarks stored?_

Unless you change this in the configuration file, they are stored in a single
file in the `$HOME/.caztor` directory called `bookmarks.gmi`.

## TLS and security

_How do I see information about the server's certificate?_

Use the menu command _Tools|Server certificate info_.

_How do I send a client certificate with my requests?_

Please see the page on [identity](identity.md) to get started. 

_What does the error message "Badly formatted directory" mean in the_
New identity _dialog box?_

You've entered an invalid name in the _Name for certificate_ field.  A
"directory" here refers to a _user_ directory, and has nothing to do with files
and folders. The name that goes into the client certificate has strict
formatting rules but, for Gemini purposes, a name of the form "CN=My name" will
almost always be sufficient.

_How do I import an identity from another browser like Lagrange?_

At present, this is a rather fiddly, manual process, because most applications
that handle TLS certificates store them in the "PEM" format, while Caztor
requires PKCS12 (or Java's proprietary 'JKS' format).

See the section "Importing a certificate from another application" in the
[client certificates](client_certs.md) page.

_Does Caztor expose keystore passwords?_

Yes. This is a clear security limitation, but it's no worse -- and perhaps
slightly better -- than those Gemini clients which don't use any password
protection on certificates at all.

## Emoji support 

_Why aren't emojis displayed properly in the document viewer?_

You need the appropriate fonts and configuration -- not all fonts contain
glyphs for the emoji characters. `Segoe UI Emoji` is a workable choice. 

Note that Java Swing is pretty fussy about which fonts it's prepared to use.
However, once there is a suitable font in place, you don't have to tell Caztor
to use it in the document viewer -- the viewer is smart enough to find a font
that contains the relevant glyphs if the main display font does not have them.

_Why aren't emojis displayed properly in the bookmark editor and menu?_

Unfortunately, the main Java Swing user interface is not as clever as the document
viewer. It isn't sufficient just to install the font -- you might also have to
tell the user interface to use it. For more information, see the separate page
on [emoji support](emoji_support.md).


## Debugging

_How do I enable full debug logging?_

In the [configuration file](config_file.md), add the setting

    log.level=3

Please be aware that this will slow Caztor down considerably.

_Where does debug logging go?_

It goes to the standard error stream. You'll probably have to run Caztor from
a command prompt to see and collect the information.

[Documentation index](index.md)


