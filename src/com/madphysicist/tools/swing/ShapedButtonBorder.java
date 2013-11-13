/*
 * ShapedButtonBorder.java
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import javax.swing.border.Border;

/**
 * So far, this is just a sandbox
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 11 Dec 2012
 * @since 1.0.0.0
 */
class ShapedButtonBorder implements Border
{
    private static final ShapedButtonBorder SINGLETON = new ShapedButtonBorder();

    private ShapedButtonBorder() {}

    @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        System.out.println("Painting border");
        if(c instanceof ShapedButton) {
            Graphics2D g2 = (Graphics2D)g.create();
            ShapedButton b = (ShapedButton)c;
            Shape s = b.getShape();
            g2.setColor(Color.RED);
            g2.translate(x, y);
            g2.draw(s);
            g2.translate(1, 1);
            g2.scale(((double)(width - 2)) / width, ((double)(height - 2)) / height);
            g2.setColor(Color.BLUE);
            g2.draw(s);
        }
    }

    @Override public Insets getBorderInsets(Component c)
    {
        return new Insets(3, 3, 3, 3);
    }

    @Override public boolean isBorderOpaque()
    {
        return false;
    }

    public static ShapedButtonBorder getInstance()
    {
        return SINGLETON;
    }
}

