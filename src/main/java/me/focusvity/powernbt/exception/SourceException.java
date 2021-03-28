package me.focusvity.powernbt.exception;

import org.bukkit.ChatColor;

public abstract class SourceException extends RuntimeException
{

    protected String source;
    protected int row, col;

    public SourceException(String source, int row, int col, Throwable reason)
    {
        super(reason);
        this.source = source;
        this.row = row;
        this.col = col;
    }

    public SourceException(String source, int row, int col, String reason)
    {
        super(reason);
        this.source = source;
        this.row = row;
        this.col = col;
    }

    public String getErrorString()
    {
        if (source == null)
        {
            return ChatColor.translateAlternateColorCodes('&', "&c[No Source]&r");
        }
        String[] lines = source.split("\n");
        if (lines.length == row)
        {
            return ChatColor.translateAlternateColorCodes('&', "&e[End of Source]&r");
        }
        else if (lines.length < row)
        {
            return ChatColor.translateAlternateColorCodes('&', "&c[Unknown Source]&r");
        }
        String line = lines[row];
        if (col == line.length())
        {
            return ChatColor.translateAlternateColorCodes('&', "&e[End of Line]&r ") + line;
        }
        if (col > line.length())
        {
            return ChatColor.translateAlternateColorCodes('&', "&c[Unknown Position]&r ") + line;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(line.substring(0, col));
        builder.append(ChatColor.RED);
        builder.append(line.charAt(col));
        builder.append(ChatColor.RESET);
        builder.append(line.substring(col + 1));
        return builder.toString();
    }
}

