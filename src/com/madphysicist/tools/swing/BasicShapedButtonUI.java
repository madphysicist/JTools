/*
 * BasicShapedButtonUI.java (Class: com.madphysicist.tools.swing.BasicShapedButtonUI)
 *
 * Mad Physicist JTools Project (Swing Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2012 by Joseph Fox-Rabinovitz
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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 11 Dec 2012 - J. Fox-Rabinovitz - Initial Coding.
 * @since 1.0.0
 */
public class BasicShapedButtonUI extends BasicButtonUI
{
    private static final BasicShapedButtonUI UI_SINGLETON = new BasicShapedButtonUI();

    private Insets insetsBuffer;

    private BasicShapedButtonUI()
    {
        insetsBuffer = new Insets(0, 0, 0, 0);
    }

    @Override public void installUI(JComponent c)
    {
        super.installUI(c);
        c.setOpaque(false);
        c.setBorder(ShapedButtonBorder.getInstance());
    }

    @Override public boolean contains(JComponent c, int x, int y)
    {
        return c.contains(x, y);
    }

    @Override public void update(Graphics g, JComponent c)
    {
        System.out.println("Painting background");
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(c.getBackground());
        c.getInsets(insetsBuffer);
        g2.translate(insetsBuffer.left, insetsBuffer.top);
        g2.fill(((ShapedButton)c).getShape());
        paint(g, c);
    }

    public static ComponentUI createUI(JComponent c)
    {
        return UI_SINGLETON;
    }
}
