/*
 * ColorIcon.java (Class: com.madphysicist.tools.swing.ColorIcon)
 *
 * Mad Physicist JTools Project (Swing Utilities)
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
package com.madphysicist.tools.swing;

import com.madphysicist.tools.util.ColorUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;

/**
 * An {@code Icon} that paints itself in a single solid color, with an optional border. The color, width and height are
 * dynamically modifiable. The border is painted with the inverse of the icon color be default if it is enabled.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 12 Jun 2013 - J. Fox-Rabinovitz - Created.
 * @version 1.0.1, 16 Nov 2013 - J. Fox-Rabinovitz - Made border display configurable.
 * @version 1.1.0, 18 Jul 2014 - J. Fox-Rabinovitz - Added color and width configuration to border.
 * @since 1.0.0
 */
public class ColorIcon implements Icon, Serializable
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
     * The width of the icon in pixels.
     *
     * @since 1.0.0
     * @serial
     */
    private int width;

    /**
     * The height of the icon in pixels.
     *
     * @since 1.0.0
     * @serial
     */
    private int height;

    /**
     * Whether or not to display the border.
     *
     * @since 1.0.1
     * @serial
     */
    private boolean border;

    /**
     * The color of the icon.
     *
     * @since 1.0.1
     * @serial
     */
    private Color color;

    /**
     * Constructs an icon with the specified color, width and height, and no
     * border.
     *
     * @param color the desired color of the icon.
     * @param width the desired width of the icon.
     * @param height the desired height of the icon.
     * @since 1.0.0
     */
    public ColorIcon(Color color, int width, int height)
    {
        this(color, width, height, false);
    }

    /**
     * Constructs an icon with the specified color, width and height, and an
     * optional border.
     *
     * @param color the desired color of the icon.
     * @param width the desired width of the icon.
     * @param height the desired height of the icon.
     * @param border {@code true} if the icon is to have a border, {@code false}
     * otherwise.
     * @since 1.0.1
     */
    public ColorIcon(Color color, int width, int height, boolean border)
    {
        this.color = color;
        this.width = width;
        this.height = height;
        this.border = border;
    }

    /**
     * {@inheritDoc}
     *
     * @param c {@inheritDoc}
     * @param g {@inheritDoc}
     * @param x {@inheritDoc}
     * @param y {@inheritDoc}
     * @since 1.0.0
     */
    @Override public void paintIcon(Component c, Graphics g, int x, int y)
    {
        Graphics copy = g.create();
        int iconWidth = width - 1;
        int iconHeight = height - 1;

        if(border) {
            // draw an inverse border
            copy.setColor(ColorUtilities.inverse(color));
            copy.drawLine(x, y, x + iconWidth, y);                           // top
            copy.drawLine(x + iconWidth, y, x + iconWidth, y + iconHeight);  // right
            copy.drawLine(x + iconWidth, y + iconHeight, x, y + iconHeight); // bottom
            copy.drawLine(x, y + iconHeight, x, y);                          // left
        }

        // fill the color
        copy.setColor(color);
        copy.fillRect(x + 1, y + 1, iconWidth - 1, iconHeight - 1);
    }

    /**
     * @since 1.0.0
     */
    @Override public int getIconWidth()
    {
        return this.width;
    }

    /**
     * @since 1.0.0
     */
    @Override public int getIconHeight()
    {
        return this.height;
    }

    /**
     * Returns the current color of the icon.
     *
     * @return the color of the icon.
     * @since 1.0.0
     */
    public Color getIconColor()
    {
        return this.color;
    }

    /**
     * Changes the width of the icon.
     *
     * @param width the new width of the icon in pixels.
     * @return {@code true} if the width changed, {@code false} if the new width is the same as the old. This is done so
     * that components that display the icon can immediately tell if an update is necessary after a resize.
     * @since 1.0.0
     */
    public boolean setIconWidth(int width)
    {
        if(width != this.width) {
            this.width = width;
            return true;
        }

        return false;
    }

    /**
     * Changes the height of the icon.
     *
     * @param height the new height of the icon in pixels.
     * @return {@code true} if the height changed, {@code false} if the new height is the same as the old. This is done
     * so that components that display the icon can immediately tell if an update is necessary after a resize.
     * @since 1.0.0
     */
    public boolean setIconHeight(int height)
    {
        if(height != this.height) {
            this.height = height;
            return true;
        }

        return false;
    }

    /**
     * Changes the color of the icon.
     *
     * @param color the new color of the icon.
     * @return {@code true} if the color changed, {@code false} if the new color is the same as the old. This is done
     * so that components that display the icon can immediately tell if an update is necessary after a color change.
     * @since 1.0.0
     */
    public boolean setIconColor(Color color)
    {
        if(!color.equals(this.color)) {
            this.color = color;
            return true;
        }

        return false;
    }
}
