/*
 * JColorButton.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.madphysicist.tools.util.ColorUtilities;

/**
 * A swing button for displaying a color label which activates a color chooser.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 12 June 2013
 * @since 1.0.0
 */
public class JColorButton extends JButton
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

    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    private ColorIcon icon;
    private JColorChooser chooser;

    public JColorButton()
    {
        this(DEFAULT_COLOR);
    }

    public JColorButton(Color color)
    {
        this(createColorChooser(color));
    }

    public JColorButton(int width, int height)
    {
        this(DEFAULT_COLOR, width, height);
    }

    public JColorButton(Color color, int width, int height)
    {
        this(createColorChooser(color), width, height);
    }

     public JColorButton(JColorChooser chooser)
    {
        this(chooser, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public JColorButton(JColorChooser chooser, int width, int height)
    {
        checkChooser(chooser);
        this.chooser = chooser;
        this.icon = new ColorIcon(width, height);
        setIcon(icon);
        addActionListener(new DialogDisplay());
    }

    public Color getColor()
    {
        return chooser.getColor();
    }

    public void setColor(Color color)
    {
        if(!color.equals(getColor())) {
            chooser.setColor(color);
            repaint();
        }
    }

    public int getIconWidth()
    {
        return icon.getIconWidth();
    }

    public void setIconWidth(int width)
    {
        icon.setIconWidth(width);
    }

    public int getIconHeight()
    {
        return icon.getIconHeight();
    }

    public void setIconHeight(int height)
    {
        icon.setIconHeight(height);
    }

    public ColorSelectionModel getSelectionModel()
    {
        return chooser.getSelectionModel();
    }

    public void setSelectionModel(ColorSelectionModel model)
    {
        chooser.setSelectionModel(model);
        repaint();
    }

    public JColorChooser getChooser()
    {
        return chooser;
    }

    public void setChooser(JColorChooser chooser)
    {
        checkChooser(chooser);
        if(!this.chooser.equals(chooser)) {
            this.chooser = chooser;
            repaint();
        }
    }

    public void addColorChangeListener(ChangeListener listener)
    {
        chooser.getSelectionModel().addChangeListener(listener);
    }

    public void removeColorChangeListener(ChangeListener listener)
    {
        chooser.getSelectionModel().removeChangeListener(listener);
    }

    private static JColorChooser createColorChooser(Color color)
    {
        JColorChooser colorChooser = new JColorChooser(color);
        return colorChooser;
    }

    private static void checkChooser(JColorChooser chooser) throws NullPointerException
    {
        if(chooser == null)
            throw new NullPointerException("chooser");
    }

    /**
     * An icon that paints itself in a single color.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 12 June 2013
     * @since 1.0.0
     */
    private class ColorIcon implements Icon
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

        private int width;
        private int height;

        public ColorIcon(int width, int height)
        {
            this.width = width;
            this.height = height;
        }
        
        @Override public void paintIcon(Component c, Graphics g, int x, int y)
        {
            Graphics copy = g.create();
            Color color = chooser.getColor();
            int iconWidth = width - 1;
            int iconHeight = height - 1;

            // draw an inverse border
            copy.setColor(ColorUtilities.inverse(color));
            copy.drawLine(x, y, x + iconWidth, y);                           // top
            copy.drawLine(x + iconWidth, y, x + iconWidth, y + iconHeight);  // right
            copy.drawLine(x + iconWidth, y + iconHeight, x, y + iconHeight); // bottom
            copy.drawLine(x, y + iconHeight, x, y);                          // left

            // fill the color
            copy.setColor(color);
            copy.fillRect(x + 1, y + 1, iconWidth - 1, iconHeight - 1);
        }

        @Override public int getIconWidth()
        {
            return this.width;
        }

        @Override public int getIconHeight()
        {
            return this.height;
        }

        public void setIconWidth(int width)
        {
            if(width != this.width) {
                this.width = width;
                revalidate();
                repaint();
            }
        }

        public void setIconHeight(int height)
        {
            if(height != this.height) {
                this.height = height;
                revalidate();
                repaint();
            }
        }
    }

    /**
     * Responds to user clicks on the button by displaying a dialog with the
     * color chooser.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 18 June 2013
     * @since 1.0.0
     */
    private class DialogDisplay implements ActionListener
    {
        private WeakReference<JDialog> dialog;
        private int counter;
        private Color color;

        public DialogDisplay()
        {
            this.counter
                    =
                    0;
            this.dialog = null;
            this.color = null;
        }

        @Override public void actionPerformed(ActionEvent e)
        {
            this.color = chooser.getColor();

            JDialog strongDialog = (this.dialog == null) ? null : this.dialog.get();
            if(strongDialog == null) {
                strongDialog = createDialog();
                this.dialog = new WeakReference<>(strongDialog);
            }
            strongDialog.setVisible(true);
            color = null;
            repaint();
        }

        private JDialog createDialog()
        {
            JDialog newDialog = new JDialog(
                SwingUtilities.windowForComponent(JColorButton.this),
                "Select Color", ModalityType.APPLICATION_MODAL);
            JPanel panel = new JPanel();
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");

            okButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    DialogDisplay.this.dialog.get().setVisible(false);
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    DialogDisplay.this.dialog.get().setVisible(false);
                    chooser.setColor(color);
                }
            });

            panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            panel.add(cancelButton);
            panel.add(okButton);

            newDialog.setLayout(new BorderLayout());
            newDialog.add(chooser, BorderLayout.CENTER);
            newDialog.add(panel, BorderLayout.SOUTH);
            newDialog.setResizable(false);
            newDialog.pack();

            return newDialog;
        }
    }

    public static void main(String[] args)
    {
        final JColorButton colorButton = new JColorButton();
        final JSpinner heightSpinner = new JSpinner();
        final JSpinner widthSpinner = new JSpinner();

        colorButton.setColor(Color.GREEN);
        heightSpinner.setValue(colorButton.getIconHeight());
        heightSpinner.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                colorButton.setIconHeight(((Number)heightSpinner.getValue()).intValue());
            }
        });
        widthSpinner.setValue(colorButton.getIconWidth());
        widthSpinner.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                colorButton.setIconWidth(((Number)widthSpinner.getValue()).intValue());
            }
        });

        JFrame frame = new JFrame("Demo of JColorButton v1.0");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.add(colorButton, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 8, 2, 8), 0, 0));
        frame.add(new JLabel("Height"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 8, 2, 5), 0, 0));
        frame.add(heightSpinner, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 5, 2, 8), 0, 0));
        frame.add(new JLabel("Width"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 8, 2, 5), 0, 0));
        frame.add(widthSpinner, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 5, 2, 8), 0, 0));
        frame.add(new JLabel("Look-and-Feel"), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(2, 8, 5, 5), 0, 0));
        frame.add(SwingUtilities.lookAndFeelSelector(frame), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 5, 5, 8), 0, 0));
        frame.pack();
        frame.setVisible(true);
    }
}
