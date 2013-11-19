/*
 * ShapedButton.java
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
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

/**
 * So far, this is just a sandbox.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 11 Dec 2012
 * @since 1.0.0
 */
public class ShapedButton extends JButton
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when compatibility
     * is not compromised by a structural change (e.g. adding a new field with
     * a sensible default value), and the upper digits when the change makes
     * serialized versions of of the class incompatible with previous releases.
     * @since 1.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * The class ID that maps to the UI class for this component type. The ID is
     * {@value}, according to Swing convention.
     *
     * @since 1.0.0
     */
    private static final String UI_CLASS_ID = "ShapedButtonUI";

    /**
     * The shape of this button. This shape will be translated as necessary to
     * place it at the button's actual location. Its bounding box will be used
     * as the button's size.
     *
     * @serial
     * @since 1.0.0
     */
    private Shape shape;

    static {
        // register the default UI for this component class
        UIManager.put(UI_CLASS_ID, BasicShapedButtonUI.class.getName());
    }

    public ShapedButton(Shape shape) throws NullPointerException
    {
        super();
        setShape(shape);
    }

    public final void setShape(Shape shape) throws NullPointerException
    {
        if(!shape.equals(this.shape)) {
            this.shape = shape;
            Rectangle rect = shape.getBounds();
            setBounds(rect);
            setPreferredSize(new Dimension(rect.width, rect.height));
            invalidate();
        }
    }

    public Shape getShape()
    {
        return shape;
    }

    @Override public void setBorder(Border border)
    {
        if(border instanceof ShapedButtonBorder)
            super.setBorder(border);
    }

    /**
     * Determines whether this button contains the specified point. The point is
     * defined in the coordinate system of this component.
     *
     * @param x the x-coordinate of the test location.
     * @param y the y-coordinate of the test location.
     * @return {@code true} if the specified point lies within the shape of this
     * button, {@code false} otherwise}.
     */
    @Override public boolean contains(int x, int y)
    {
        return shape.contains(x, y);
    }

    @Override public String getUIClassID()
    {
        return UI_CLASS_ID;
    }

    public static void main(String[] args) throws ClassNotFoundException,
                                                  IllegalAccessException,
	  InstantiationException,
	  UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        ShapedButton shapedButton = new ShapedButton(
                new Polygon(new int[] {50, 150, 200, 200, 150, 50, 0, 0},
                            new int[] {0, 0, 50, 150, 200, 200, 150, 50}, 8));
        shapedButton.setBackground(Color.RED);
        shapedButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent me) {
                System.out.println("Clicked inside: (" + me.getX() + ", " + me.getY() + ")");
            }
        });
        shapedButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println(((ShapedButton)e.getSource()).getModel().isPressed());
            }
        });

        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(shapedButton);

        frame.pack();
        frame.setVisible(true);
    }
}
