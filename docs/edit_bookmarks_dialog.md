# "Edit bookmarks" dialog

Caztor's bookmarks file is an ordinary Gemtext document, and you can re-format
it (mostly) as you like. Every time you bookmark a page, Caztor appends a
Gemtext link to the bookmarks file. You can re-order the links, and add
headings and explanatory text if you wish.

The only stipulation is that the bookmarks file remain a valid Gemtext
document, and ends with an end-of-line character, so Caztor can add new
bookmarks links to the file. When you select _Bookmarks|Show all_, Caztor
simply renders the bookmarks file as if it were a Gemtext document.

If you prefer, you can edit the bookmarks file (usually
`$HOME/.caztor/bookmarks.gmi`) with an ordinary text editor. It's advisable to
do this when Caztor is not running, to prevent Caztor's internal
representation of the bookmarks document getting out of sync with the file.

[Documentation index](index.md)

