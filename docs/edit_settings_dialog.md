# "Edit settings" dialog

> *Note*  
> As more and more of Caztor's functionality is managed by the main user
> interface, there's less need to edit settings manually.  Incautious hacking
> on the configuration file can break things in non-obvious ways.

The settings editor allows for direct editing of the user settings
(configuration) file, and allows for changes that can't be made directly by the
Caztor user interface. To see the settings editor, use the 
_Tools|Settings|Edit_ menu command.

The settings file is usually `$HOME/.caztor/caztor.properties`.

If you prefer, you can edit the settings file with an ordinary text editor. If
you do this when Caztor is running, use the _Tools|Settings|Reload_ menu
command to make Caztor reload the file and update its internal state.

Please be aware that changes to some settings won't take effect unless you
restart Caztor.

For a full list of configuration settings, see the
[configuration_file](config_file.md) page.


[Documentation index](index.md)


