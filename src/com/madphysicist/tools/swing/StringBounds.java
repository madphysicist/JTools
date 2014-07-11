/*
 * StringBounds.java
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Joseph Fox-Rabinovitz
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

package com.madphysicist.tools.swing;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Stores the dimensions of a string when rendered with a specific font. This class returns only approximate results,
 * although it is usually a very good approximation. This class is immutable.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 16 Feb 2013 - J. Fox-Rabinovitz - Initial coding
 * @version 2.0.0, 24 Jun 2014 - J. Fox-Rabinovitz - Turned into public class
 * @since 1.2
 */
public class StringBounds implements Serializable
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when
     * compatibility is not compromised by a structural change (e.g. adding
     * a new field with a sensible default value), and the upper digits when
     * the change makes serialized versions of of the class incompatible
     * with previous releases.
     * @since 1.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * The width of the string in pixels for the current font and rendering context.
     *
     * @serial
     * @since 1.0.0
     */
    private final float width;

    /**
     * The height of the string in pixels for the current font and rendering context. The height is the sum of the
     * ascent (part of the string above the baseline) and descent (the part below the baseline) for the specified font.
     *
     * @serial
     * @since 1.0.0
     */
    private final float height;

    /**
     * The base line of the string in pixels measured from the top for the current font and rendering context. The base
     * line is given by the distance from the top of the ascent to the base line for the current font.
     *
     * @serial
     * @since 1.0.0
     */
    private final float baseLine;

    /**
     * The string which this object encapsulates. This is the only field that is font- and rendering
     * context-independent.
     *
     * @serial
     * @since 1.0.0
     */
    private final String str;

    /**
     * The font for which the bounds were computed.
     *
     * @serial
     * @since 2.0.0
     */
    private final Font font;

    /**
     * Constructs a bounds object for the specified string at the specified font. The width, height and base line fields
     * are computed immediately since this class is immutable. An entirely new object must be created for each font.
     *
     * @param str the string for which the bounds are to be computed.
     * @param font the font for which the bounds are computed.
     * @since 2.0.0
     */
    public StringBounds(String str, Font font)
    {
        this.str = str;
        this.font = font;

        FontRenderContext renderer = new FontRenderContext(font.getTransform(), true, true);

        Rectangle2D rect = font.getStringBounds(str, renderer);
        LineMetrics metrics = font.getLineMetrics(str, renderer);

        this.width = (float)rect.getWidth();
        this.height = metrics.getAscent() + metrics.getDescent();
        this.baseLine = metrics.getAscent();
    }

    /**
     * The string described by these bounds.
     *
     * @return the string with which this object was initialized.
     * @since 2.0.0
     */
    public String getString()
    {
        return str;
    }

    /**
     * The font used to compute these bounds.
     *
     * @return the font with which this object was initialized.
     * @since 2.0.0
     */
    public Font getFont()
    {
        return font;
    }

    /**
     * The width of the string given the specified font.
     *
     * @return the width of the string in pixels.
     * @since 1.0.0
     */
    public float getWidth()
    {
        return width;
    }

    /**
     * The height of the string given the specified font. The height is the sum of the ascent and the descent of the
     * font.
     *
     * @return the total height of the string in pixels.
     * @since 1.0.0
     */
    public float getHeight()
    {
        return height;
    }

    /**
     * The distance from the ascent of the font to the baseline for the current font.
     *
     * @return the distance from the top of the bounds to the baseline in pixels.
     * @since 1.0.0
     */
    public float getBaseLine()
    {
        return baseLine;
    }

    /**
     * Returns an informative string representation of this object. The resulting string will contain information about
     * this object and its properties. This method is useful for debugging and should not be used as a formal
     * description of this object since the contents of the result may change at any time.
     *
     * @return a {@code String} representation of this object.
     * @since 1.0.0
     */
    @Override public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [str=").append(this.str);
        sb.append(", font=").append(this.font);
        sb.append(", width=").append(this.width);
        sb.append(", height=").append(this.height);
        sb.append(", baseLine=").append(this.baseLine);
        sb.append("]");
        return sb.toString();
    }
}

