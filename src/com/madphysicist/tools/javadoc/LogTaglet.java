/*
 * LogTaglet.java (Class: com.madphysicist.tools.javadoc.LogTaglet)
 *
 * Mad Physicist JTools Project (Javadoc Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 by Joseph Fox-Rabinovitz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.madphysicist.tools.javadoc;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;
import java.util.Map;
import com.madphysicist.tools.util.TextUtilities;

/**
 * Implements a {@code Taglet} that adds functionality to include the
 * {@literal @}joe.log block tag to doc comments in the standard HTML doclet.
 * This taglet can be used for methods and constructors to list the messages
 * that they log. The tag is block-only (not inline), with the following format:
 * <pre>
 * {@literal @}joe.log &lt;Type&gt; &lt;["]Message["]&gt; &lt;Reason&gt;
 * </pre>
 * {@code Type} is always interpreted as the first word delimited by a space
 * character not escaped by a backslash. It indicates the type or level of the
 * log message. {@code Message} is the message that is logged. If it contains
 * space characters, the whole thing must either be surrounded by double quotes
 * or the spaces must be escaped with backslashes. Quotes can also appear within
 * the message if they are escaped by a backslash. Note that the opening quote
 * must be the first character of a message if it is to be interpreted
 * correctly. The remainder of the tag is processed as the optional
 * {@code Reason} or condition under which the log message is written. Whereas
 * text in the type and message is transformed as though it is surrounded by the
 * {@literal {@literal}} tag, the {@code Reason} text is interpreted as HTML.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 4 Mar 2012 - J. Fox-Rabinovitz - Created
 * @since 1.0.0.0
 */
public class LogTaglet implements Taglet
{
    /**
     * Registers this taglet with the doclet.
     * 
     * @param map the map to add this taglet to.
     * @since 1.0.0.0
     */
    public static void register(Map<String, Taglet> map)
    {
        LogTaglet taglet = new LogTaglet();
        map.put(taglet.getName(), taglet);
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code false}.
     * @since 1.0.0.0
     */
    @Override public boolean inField()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code true}.
     * @since 1.0.0.0
     */
    @Override public boolean inConstructor()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code true}.
     * @since 1.0.0.0
     */
    @Override public boolean inMethod()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code false}.
     * @since 1.0.0.0
     */
    @Override public boolean inOverview()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code false}.
     * @since 1.0.0.0
     */
    @Override public boolean inPackage()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code false}.
     * @since 1.0.0.0
     */
    @Override public boolean inType()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code false}.
     * @since 1.0.0.0
     */
    @Override public boolean isInlineTag()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * @return always returns {@code "joe.log"}.
     * @since 1.0.0.0
     */
    @Override public String getName()
    {
        return "joe.log";
    }

    /**
     * {@inheritDoc}
     * @param tag the tag to convert to an HTML string.
     * @return {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public String toString(Tag tag)
    {
        if(tag == null)
            return null;
        String lineNo = (tag.position() == null) ? "unknown line" : "line " + tag.position().line();
        String text = tag.text();
        if(text == null) {
            System.err.println("LogTaglet: Null @" + getName() + " tag on " + lineNo + ".");
            return text;
        }
        text = text.trim();
        if(text.isEmpty()) {
            System.err.println("LogTaglet: Empty @" + getName() + " tag on " + lineNo + ".");
            return text;
        }
        String type, message, description;
        int spaceIndex = TextUtilities.nextIndexOf(text, " ", 0, " ", '\\');
        if(spaceIndex < 0) {
            System.err.println("LogTaglet: @" + getName() + " tag only contains type on " + lineNo + ".");
            type = TextUtilities.unescapeString(text, " ", '\\');
            message = "";
            description = "";
        } else {
            int quoteIndex;
            type = TextUtilities.unescapeString(text.substring(0, spaceIndex), " ", '\\');
            text = text.substring(spaceIndex).trim();
            spaceIndex = 0;
            if(text.charAt(spaceIndex) == '\"') {
                spaceIndex++;
                quoteIndex = TextUtilities.nextIndexOf(text, "\"", spaceIndex + 1, null, '\\');
                if(quoteIndex < 0)
                    System.err.println("LogTaglet: @" + getName() + " tag missing closing quote (\") on " + lineNo + ".");
            } else
                quoteIndex = TextUtilities.nextIndexOf(text, " ", spaceIndex, null, '\\');
            if(quoteIndex < 0) {
                message = text.substring(spaceIndex).trim();
                description = "";
            } else {
                message = text.substring(spaceIndex, quoteIndex);
                description = (quoteIndex == text.length() - 1) ? "" : text.substring(quoteIndex + 1).trim();
            }
        }
        String tagStr = "<dd><dl>";
        if(message.isEmpty()) {
            tagStr += "<dt><b>" + TextUtilities.htmlEncode(type) + "</b></dt>";
        } else {
            tagStr += "<dt><b>" + TextUtilities.htmlEncode(type) +
                    ":</b>&nbsp;<code>" + TextUtilities.htmlEncode(
                    TextUtilities.unescapeString(message, null, '\\')) + "</code></dt>";            
        }
        if(!description.isEmpty())
            tagStr += "<dd>" + description + "</dd>";
        tagStr += "</dl></dd>";
        return tagStr;
    }

    /**
     * {@inheritDoc}
     * @param tags the tags to convert to HTML.
     * @return {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public String toString(Tag[] tags)
    {
        if(tags == null || tags.length == 0)
            return null;
        String result = "<dt><b>Log Messages:</b></dt>";
        for(Tag tag : tags) {
            String tagStr = toString(tag);
            if(tagStr != null)
                result += tagStr;
        }
        return result;
    }
}
