# Gempub and MBook support 

Caztor 1.0.3 has preliminary support for Gempub and MBook e-books. It displays
books whose text is not only in Gemtext format -- as described in the original
Gempub specification -- but also in the Commonmark Markdown that MBook uses,
and which potentially offers richer text formatting.

Caztor will treat as Gempub any content to which the server assigns the MIME type
`application/gpub+zip`, or whose path has a name ending in `.gpub`. MBook
is signalled by content type `application/mbook+zip`, or a path ending in 
`.mbook`.

As well as following a link to a Gempub or MBook document, you can open a
`.gpub` or `.mbook` file from the command line:

    java -jar caztor.jar /path/to/book.gpub

This only works for files whose names have these specific endings -- Caztor won't inspect
the contents of the file, to find out what it is.

When Caztor is showing a book, the _Go/Site root_ menu command reloads the contents page.
There will also be a link to the contents page, as well as the the next chapter,
at the end of each chapter.

In the current implementation, Caztor initially downloads the entire document
into memory, and then saves it as a temporary file. Thereafter it reads
specific chapters from the saved temporary file. To reduce privacy concerns,
Caztor will try to delete any such files it created when it exits. 

Internally, Caztor handles these books as zipfiles, using a specific URL whose
protocol is `zipfile:`. However, at present, Caztor doesn't handle any other
kind of zipfile. A `zipfile` URI has the form

    zipfile:///path/to/filename.extension/path/to/item
 
The URL for the index page _must_ end with a forward slash (`/`), because the
URL resolver won't work properly otherwise. 

Caztor generally hides these URL details from users, but you'll see the
`zipfile` URLs in the URL bar. It's possible to bookmark a Gempub chapter using
this URL, if you wish. 

[Documentation index](index.md)


