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
 * <p>
 * An {@code Icon} that paints itself in a single solid color, with an optional border. The color, width and height are
 * dynamically modifiable. The border is painted with the inverse of the icon color be default if it is enabled.
 * </p>
 * <p>
 * As with many other swing elements, this class is not properly synchronized.
 * </p>
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 12 Jun 2013 - J. Fox-Rabinovitz - Iniaial coding.
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
     * The default width of the border when it is shown.
     *
     * @since 1.1.0
     */
    private static final int DEFAULT_BORDER_WIDTH = 1;

    /**
     * The width of the icon in pixels.
     *
     * @serial
     * @since 1.0.0
     */
    private int width;

    /**
     * The height of the icon in pixels.
     *
     * @serial
     * @since 1.0.0
     */
    private int height;

    /**
     * The color of the icon.
     *
     * @serial
     * @since 1.0.1
     */
    private Color color;

    /**
     * Whether or not to display the border.
     *
     * @serial
     * @since 1.0.1
     */
    private boolean border;

    /**
     * The width of the border in pixels.
     *
     * @serial
     * @since 1.1.0
     */
    private int borderWidth;

    /**
     * The color of the border. This may be a {@code null} reference to indicate the default color, which is the inverse
     * of the icon color.
     *
     * @see ColorUtilities#inverse(Color)
     * @serial
     * @since 1.1.0
     */
    private Color borderColor;

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
     * @param borderEnabled {@code true} if the icon is to have a border, {@code false}
     * otherwise.
     * @since 1.0.1
     */
    public ColorIcon(Color color, int width, int height, boolean borderEnabled)
    {
        this.color = color;
        this.width = width;
        this.height = height;
        this.border = borderEnabled;
        this.borderWidth = DEFAULT_BORDER_WIDTH;
        this.borderColor = null;
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
            // draw a border
            copy.setColor(getBorderColor());
            if(borderWidth == 1)
                copy.drawRect(x, y, iconWidth, iconHeight);
            else
                copy.fillRect(x, y, iconWidth + 1, iconHeight + 1);
        }

        // fill the color
        copy.setColor(color);
        if(border)
            copy.fillRect(x + borderWidth, y + borderWidth, iconWidth - 2 * borderWidth + 1, iconHeight - 2 * borderWidth + 1);
        else
            copy.fillRect(x, y, iconWidth + 1, iconHeight + 1);
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
     * Returns the display state of the icon border.
     *
     * @return {@code true} if the icon border will be drawn, {@code false} otherwise.
     * @since 1.1.0
     */
    public boolean isBorderEnabled()
    {
        return border;
    }

    /**
     * Returns the border width of the icon in pixels.
     *
     * @return the width of the icon border, when it is enabled.
     * @since 1.1.0
     */
    public int getBorderWidth()
    {
        return this.borderWidth;
    }

    /**
     * Returns the current border color of the icon. If the default color is being used, this will be the inverse of the
     * current icon color.
     *
     * @see #getIconColor()
     * @see ColorUtilities#inverse(Color)
     * @return the color with which the border is displayed, when it is enabled.
     * @since 1.1.0
     */
    public Color getBorderColor()
    {
        return (this.borderColor == null) ? ColorUtilities.inverse(this.color) : this.borderColor;
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

    /**
     * Changes whether or not the border of the icon is displayed.
     *
     * @param borderEnabled the new status of the border.
     * @return {@code true} if the status changed, {@code false} if the new display status is the same as the old. This
     * is done so that components that display the icon can immediately tell if an update is necessary after a status
     * change.
     * @since 1.1.0
     */
    public boolean setBorderEnabled(boolean borderEnabled)
    {
        if(borderEnabled != this.border) {
            this.border = borderEnabled;
            return true;
        }

        return false;
    }

    /**
     * Changes the border width of the icon in pixels. The border width is configurable even when the border itself is
     * not being displayed.
     *
     * @param borderWidth the new border width in pixels.
     * @return {@code true} if the border width changed and the border is being displayed, {@code false} if the new
     * border width is the same as the old, or if the border is not being displayed. This is done so that components
     * that display the icon can immediately tell if an update is necessary after a change.
     * @since 1.1.0
     */
    public boolean setBorderWidth(int borderWidth)
    {
        if(this.borderWidth != borderWidth) {
            this.borderWidth = borderWidth;
            return border;
        }

        return false;
    }

    /**
     * Changes the border color of the icon. The border width is configurable even when the border itself is not being
     * displayed. If the new color is {@code null}, the border color defaults to the dynamic inverse of the icon color.
     *
     * @param borderColor the new border color.
     * @return {@code true} if the border color changed and the border is being displayed, {@code false} if the new
     * border color is the same as the old, or if the border is not being displayed. This is done so that components
     * that display the icon can immediately tell if an update is necessary after a change. Note that explicitly setting
     * the color to a value equal to the default when the current border color has been set to {@code null} or setting
     * the border color to {@code null} when it has been explicitly set to a value that equals the default will cause
     * a return value of {@code false} since no update is necessary to correctly display the icon. 
     * @since 1.1.0
     */
    public boolean setBorderColor(Color borderColor)
    {
        if((borderColor == null && this.borderColor != null) || (borderColor != null && !borderColor.equals(this.borderColor))) {
            Color oldColor = getBorderColor();
            this.borderColor = borderColor;
            return border && !oldColor.equals(getBorderColor());
        }

        return false;
    }
}
