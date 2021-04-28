package me.focusvity.powernbt.util;

import java.util.*;

public class Tokenizer
{

    private final String lineComment;
    private final String openComment;
    private final String closeComment;
    private Set<Character> quotes = new HashSet<>();
    private Set<Character> singleCharacters = new HashSet<>();
    private Set<Character> delimiters = new HashSet<>();

    public Tokenizer(String lineComment, String openComment, String closeComment,
                     Collection<Character> quotes, Collection<Character> singleCharacters, Collection<Character> delimiters)
    {
        this.lineComment = lineComment;
        this.openComment = openComment;
        this.closeComment = closeComment;
        if (quotes != null)
        {
            this.quotes = new HashSet<>(quotes);
        }
        if (singleCharacters != null)
        {
            this.singleCharacters = new HashSet<>(singleCharacters);
        }
        if (delimiters != null)
        {
            this.delimiters = new HashSet<>(delimiters);
        }
    }

    private boolean isQuote(char c)
    {
        return quotes.contains(c);
    }

    private boolean isSingleCharacter(char c)
    {
        return singleCharacters.contains(c);
    }

    private boolean isDelimiter(char c)
    {
        return delimiters.contains(c);
    }

    private boolean isLineComment(String s)
    {
        return lineComment != null && lineComment.equals(s);
    }

    private boolean isLineComment(String s, char c)
    {
        return isLineComment(s + c);
    }

    private boolean isOpenComment(String s)
    {
        return openComment != null && openComment.equals(s);
    }

    private boolean isOpenComment(String s, char c)
    {
        return isOpenComment(s + c);
    }

    private boolean isCloseComment(String s)
    {
        return closeComment != null && closeComment.equals(s);
    }

    private boolean isCloseComment(String s, char c)
    {
        return isCloseComment(s + c);
    }

    public TreeMap<Integer, String> tokenize(String string)
    {
        VarCharInputStream input = new VarCharInputStream(string);
        TreeMap<Integer, String> tokens = new TreeMap<>();
        Mode mode = Mode.OPERAND;
        VarStringBuffer buffer = new VarStringBuffer();
        char quote = 0;
        tokenizer:
        while (true)
        {
            int position = input.getPosition();
            Character c = input.read();
            switch (mode)
            {
                case OPERAND:
                {
                    if (c == null)
                    {
                        if (buffer.isNotEmpty())
                        {
                            tokens.put(position - buffer.length(), buffer.toString());
                        }
                        break tokenizer;
                    }
                    else if (isOpenComment(buffer.toString(), c))
                    {
                        buffer.clear();
                        mode = Mode.FULL_COMMENT;
                    }
                    else if (isLineComment(buffer.toString(), c))
                    {
                        buffer.clear();
                        mode = Mode.LINE_COMMENT;
                    }
                    else if (isDelimiter(c))
                    {
                        if (buffer.isNotEmpty())
                        {
                            tokens.put(position - buffer.length(), buffer.toString());
                        }
                        buffer.clear();
                    }
                    else if (isQuote(c))
                    {
                        buffer.append(c);
                        quote = c;
                        mode = Mode.TEXT;
                    }
                    else if (isSingleCharacter(c))
                    {
                        if (buffer.isNotEmpty())
                        {
                            tokens.put(position - buffer.length(), buffer.toString());
                        }
                        tokens.put(position - 1, c.toString());
                        buffer.clear();
                    }
                    else
                    {
                        buffer.append(c);
                    }
                    break;
                }
                case TEXT:
                {
                    if (c == null)
                    {
                        throw new RuntimeException("Missing " + quote + ": " + buffer);
                    }
                    if (c == '\\')
                    {
                        buffer.append(c);
                        buffer.append(input.read());
                    }
                    else if (c == quote)
                    {
                        buffer.append(c);
                        mode = Mode.OPERAND;
                    }
                    else
                    {
                        buffer.append(c);
                    }
                    break;
                }
                case FULL_COMMENT:
                {
                    VarStringBuffer buf = new VarStringBuffer();
                    while (true)
                    {
                        Character t = input.read();
                        if (t == null)
                        {
                            throw new RuntimeException("Missing " + closeComment);
                        }
                        else if (isDelimiter(t))
                        {
                            if (isCloseComment(buf.toString()))
                            {
                                break;
                            }
                            buf.clear();
                        }
                        else
                        {
                            buf.append(t);
                        }
                    }
                    mode = Mode.OPERAND;
                    break;
                }
                case LINE_COMMENT:
                {
                    while (true)
                    {
                        Character t = input.read();
                        if (t == null || t == '\n')
                        {
                            mode = Mode.OPERAND;
                            break;
                        }
                    }
                    break;
                }
                default:
                {
                    throw new RuntimeException("Unexpected mode: " + mode);
                }
            }
        }
        return tokens;
    }

    private enum Mode
    {
        OPERAND, LINE_COMMENT, FULL_COMMENT, TEXT
    }

    private class VarCharInputStream
    {

        private final char[] c;
        private int p = 0;

        public VarCharInputStream(String s)
        {
            c = s.toCharArray();
        }

        public int getPosition()
        {
            return p;
        }

        public Character read()
        {
            Character x = null;
            if (p < c.length)
            {
                x = c[p];
            }
            p++;
            return x;
        }
    }

    private class VarStringBuffer
    {

        private final ArrayList<Character> a = new ArrayList<>();

        public void append(Character c)
        {
            a.add(c);
        }

        public int length()
        {
            return a.size();
        }

        public void clear()
        {
            a.clear();
        }

        public boolean isNotEmpty()
        {
            return !a.isEmpty();
        }

        @Override
        public String toString()
        {
            int s = a.size();
            char[] c = new char[s];
            int i = 0;
            while (i < s)
            {
                c[i] = a.get(i++);
            }
            return new String(c);
        }
    }
}
