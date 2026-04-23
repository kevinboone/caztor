# Text prompts

None of the protocols that Caztor supports allow form-based data entry.  The
best they will accept is a single piece of text. In general, Caztor will pop up
a text entry dialog box when a particular site asks for text input.

For the Gemini protocol, the maximum length of text is reasonably well-defined:
the entire request URL, including the text being submitted, must be 2048 bytes
or less. For the other protocols the situation is less well-defined so, for
consistency, Caztor assumes 2048 bytes in all cases.

Since this 2048-byte limit includes the base URL, the amount of text you can
enter will be less than 2048 bytes, by some variable amount. So the text input
dialog box shows a count of bytes remaining. 

Other than for plain ASCII, each character will use more than one byte, the
exact number depending on several factors. Consequently, the byte count doesn't
equate to a character count, although the two will be similar with English
text. So you might find that the remaining bytes count will change in a
non-obvious way as you type.

Because the text sent to the server can legitimately include end-of-line
markers, the text entry dialog uses `ctrl+S` as the short-cut to submit the
data, not `Enter`.

If you do use multi-byte characters in your submission, be aware that they are
sent to the server using your platform's default encoding.  That's nearly
always UTF-8 these days. Gemini servers will be happy with this encoding, but
Gopher servers might not -- Gopher technology pre-dates Unicode. 

[Documentation index](index.md)

