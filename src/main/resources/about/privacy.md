# Caztor privacy issues

## Network privacy issues

All the "small net" protocols that Caztor supports, apart from Gemini,
are unencrypted. Your requests and responses are visible to anybody
who can see your network traffic. 

On the positive side, however, none of these protocols is very traceable,
because they don't carry much information from the client. There's no realistic
chance of "browser fingerprinting", which is now ubiquitous on the mainstream
web. If you use a VPN, you're essentially anonymous unless you deliberately
identify yourself. 

Things are slightly different if you're using Gemini, because it supports
TLS encryption, and authentication using a client certificate.

If you send the same client certificate to all servers it's possible, in theory,
for server administrators to collude to track you. There's no reason to 
think this is happening, or ever will. However, if it's a concern, you should
use a different client certificate for each site that requires one.

## Saving local state

Caztor does not cache incoming documents -- there's no point, since none of
the protocols it supports carry any time-stamp information. In fact, it
deliberately stores very little state.

Caztor might store the following on disk.

- Files you explicitly download
- Files Caztor has to download, to pass to the desktop for handling. 
  Caztor will try to delete these files when it exits, whether the
  application has finished with them or not. If you choose to save a 
  file in whatever application handled it then, of course, it will be
  saved permanently
- Your URL history, but only if you have explicitly enabled this
  Otherwise, history is only stored in memory, for the duration of the session.
- Bookmarks you explicitly save
- Subscription feeds you explicitly save
- The page of post titles and links from aggregated feeds
- User interface settings, like the window size.

By default, all local state is saved in the `$HOME/.caztor` directory.


[Documentation index](index.md)



