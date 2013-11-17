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
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.lang.ref.WeakReference;
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

/**
 * A swing button for displaying a color label which activates a color chooser.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 12 Jun 2013 - J. Fox-Rabinovitz - Created.
 * @version 1.0.1, 16 Nov 2013 - J. Fox-Rabinovitz - Made ColorIcon public main class.
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

    /**
     * The default color for an icon.
     *
     * @since 1.0.0
     */
    private static final Color DEFAULT_COLOR = Color.BLACK;

    /**
     * The default width for an icon in pixels.
     *
     * @since 1.0.0
     */
    private static final int DEFAULT_WIDTH = 32;

    /**
     * The default height for an icon in pixels.
     *
     * @since 1.0.0
     */
    private static final int DEFAULT_HEIGHT = 32;

    /**
     * The icon for this button. The icon holds the current state of the color,
     * and gets updated from the color chooser when a new selection is made.
     *
     * @serial
     * @since 1.0.0
     */
    private ColorIcon icon;

    /**
     * The color chooser that can be used to pick a new color. It is displayed
     * by a {@link JColorButton.DialogDisplay} {@code ActionListener} on this
     * button.
     *
     * @serial
     * @since 1.0.0
     */
    private JColorChooser chooser;

    /**
     * Changes the color of the icon when the current {@code JColorChooser}'s
     * {@code SelectionModel} receives an update. This listener is attached to
     * the current model whenever it is changed.
     *
     * @serial
     * @since 1.0.1
     */
    private final ColorUpdater colorUpdater;

    /**
     * Initializes a button with the default color containing and an icon with
     * the default dimensions.
     *
     * @since 1.0.0
     */
    public JColorButton()
    {
        this(DEFAULT_COLOR);
    }

    /**
     * Initializes a button with the specified color containing an icon with the
     * default dimensions.
     *
     * @param color the color of the icon.
     * @since 1.0.0
     */
    public JColorButton(Color color)
    {
        this(createColorChooser(color));
    }

    /**
     * Initializes a button with the default color containing an icon with the
     * specified dimensions.
     *
     * @param width the width of the icon.
     * @param height the height of the icon.
     * @since 1.0.0
     */
    public JColorButton(int width, int height)
    {
        this(DEFAULT_COLOR, width, height);
    }

    /**
     * Initializes a button with the specified color containing an icon with the
     * specified dimensions.
     *
     * @param color the color of the icon.
     * @param width the width of the icon.
     * @param height the height of the icon.
     * @since 1.0.0
     */
    public JColorButton(Color color, int width, int height)
    {
        this(createColorChooser(color), width, height);
    }

    /**
     * Initializes a button with the specified color chooser and an icon with
     * default dimensions. This allows multiple components to depend on the same
     * color chooser.
     *
     * @param chooser the chooser that will control the color of the icon.
     * @since 1.0.0
     */
    public JColorButton(JColorChooser chooser)
    {
        this(chooser, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Initializes a button with the specified color chooser and an icon with
     * the specified dimensions. This allows multiple components to depend on
     * the same color chooser.
     *
     * @param chooser the chooser that will control the color of the icon.
     * @param width the width of the icon.
     * @param height the height of the icon.
     * @since 1.0.0
     */
    public JColorButton(JColorChooser chooser, int width, int height)
    {
        colorUpdater = new ColorUpdater();

        checkChooser(chooser);
        this.chooser = chooser;
        this.icon = new ColorIcon(chooser.getColor(), width, height, true);
        setIcon(icon);

        chooser.getSelectionModel().addChangeListener(colorUpdater);
        addActionListener(new DialogDisplay());
    }

    /**
     * Retrieves the current color of the icon. This is the same as the color of
     * the underlying color chooser, except when the user is making a selection.
     *
     * @return the current color of the icon.
     * @since 1.0.0
     */
    public Color getColor()
    {
        return icon.getIconColor();
    }

    /**
     * Sets the color of the icon and updates the chooser to match. If the
     * current color and the new color are equal, no change is made.
     *
     * @param color the new color of the icon and color chooser.
     * @since 1.0.0
     */
    public void setColor(Color color)
    {
        if(icon.setIconColor(color)) {
            chooser.setColor(color);
            repaint();
        }
    }

    /**
     * Retrieves the current width of the icon.
     *
     * @return the width of the icon.
     * @since 1.0.0
     */
    public int getIconWidth()
    {
        return icon.getIconWidth();
    }

    /**
     * Sets the width of the icon. If the current width and the new width are
     * equal, no change is made.
     *
     * @param width the new width of the icon.
     * @since 1.0.0
     */
    public void setIconWidth(int width)
    {
        if(icon.setIconWidth(width)) {
            revalidate();
            repaint();
        }
    }

    /**
     * Retrieves the current height of the icon.
     *
     * @return the height of the icon.
     * @since 1.0.0
     */
    public int getIconHeight()
    {
        return icon.getIconHeight();
    }

    /**
     * Sets the height of the icon. If the current height and the new height are
     * equal, no change is made.
     *
     * @param height the new height of the icon.
     * @since 1.0.0
     */
    public void setIconHeight(int height)
    {
        if(icon.setIconHeight(height)) {
            revalidate();
            repaint();            
        }
    }

    /**
     * Retrieves the selection model of the color chooser.
     *
     * @return the selection model for the underlying color chooser.
     * @since 1.0.0
     */
    public ColorSelectionModel getSelectionModel()
    {
        return chooser.getSelectionModel();
    }

    /**
     * Changes the selection model of the color chooser. This will implicitly
     * change the listeners that respond to changes in the color of the icon.
     *
     * @param model the new selection model for the underlying color chooser.
     * @since 1.0.0
     */
    public void setSelectionModel(ColorSelectionModel model)
    {
        switchSelectionModel(model);
        chooser.setSelectionModel(model);
        setColor();
    }

    /**
     * Returns the underlying color chooser. Changing the color of the chooser
     * will also change the color of the icon. However, changing the {@code
     * SelectionModel} of the retrieved chooser will lead to incorrect behavior.
     *
     * @return the color chooser for this button.
     * @since 1.0.0
     */
    public JColorChooser getChooser()
    {
        return chooser;
    }

    /**
     * Changes the underlying color chooser of this button. External changes to
     * the new chooser will now be reflected by the icon.
     *
     * @param chooser the new chooser to set.
     * @since 1.0.0
     */
    public void setChooser(JColorChooser chooser)
    {
        checkChooser(chooser);
        if(!this.chooser.equals(chooser)) {
            switchSelectionModel(chooser.getSelectionModel());
            this.chooser = chooser;
            setColor();
        }
    }

    /**
     * Adds a listener to changes in the color to the current selection model.
     * This listener will not be transferred to a new model if either the model
     * or the color chooser are changed.
     *
     * @param listener the listener to add.
     * @since 1.0.0
     */
    public void addColorChangeListener(ChangeListener listener)
    {
        chooser.getSelectionModel().addChangeListener(listener);
    }

    /**
     * Removes a listener to changes in the color from the current selection
     * model.
     *
     * @param listener the listener to remove.
     * @since 1.0.0
     */
    public void removeColorChangeListener(ChangeListener listener)
    {
        chooser.getSelectionModel().removeChangeListener(listener);
    }

    /**
     * Updates the color of the icon with the currently selected color in the
     * chooser. If the update results in a change in the icon, the button is
     * repainted.
     *
     * @since 1.0.1
     */
    private void setColor()
    {
        if(icon.setIconColor(chooser.getColor())) {
            repaint();
        }
    }

    /**
     * Changes the {@code ChangeListener} that updates the icon from the
     * selection model. The listener is transferred from the model of the
     * current color chooser to the specified new model. This method should be
     * invoked just before the current color chooser or its model are changed.
     *
     * @param model the model that will receive the icon updater.
     * @see #colorUpdater
     * @since 1.0.1
     */
    private void switchSelectionModel(ColorSelectionModel model)
    {
        chooser.getSelectionModel().removeChangeListener(colorUpdater);
        model.addChangeListener(colorUpdater);
    }

    /**
     * Creates a default color chooser with the specified color. This method is
     * used when a {@code Color}-based constructor is invoked.
     *
     * @param color the color with which to initialize the chooser.
     * @return a new color chooser.
     * @since 1.0.0
     */
    private static JColorChooser createColorChooser(Color color)
    {
        JColorChooser colorChooser = new JColorChooser(color);
        return colorChooser;
    }

    /**
     * Verifies that the specified reference is non-null. Throws an exception if
     * it is.
     *
     * @param chooser the reference to check.
     * @throws NullPointerException if the specified chooser is {@code null}.
     * @since 1.0.0
     */
    private static void checkChooser(JColorChooser chooser) throws NullPointerException
    {
        if(chooser == null) {
            throw new NullPointerException("chooser");
        }
    }

    /**
     * Responds to changes in the color chooser's model by updating the icon.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 16 Nov 2013 - J. Fox-Rabinovitz - Created.
     * @since 1.0.1
     */
    private class ColorUpdater implements ChangeListener, Serializable
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

        @Override public void stateChanged(ChangeEvent evt)
        {
            setColor();
        }

    }

    /**
     * Responds to user clicks on the button by displaying a dialog with the
     * color chooser.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 18 Jun 2013 - J. Fox-Rabinovitz - Created.
     * @since 1.0.0
     */
    private class DialogDisplay implements ActionListener, Serializable
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

        private transient WeakReference<JDialog> dialog;
        private transient Color color;

        public DialogDisplay()
        {
            this.dialog = null;
            this.color = null;
        }

        @Override public void actionPerformed(ActionEvent e)
        {
            JDialog strongDialog = (this.dialog == null) ? null : this.dialog.get();
            if(strongDialog == null) {
                strongDialog = createDialog();
                this.dialog = new WeakReference<>(strongDialog);
            }

            this.color = getColor();
            strongDialog.setVisible(true);
        }

        private JDialog createDialog()
        {
            JDialog newDialog = new JDialog(
                SwingUtilities.windowForComponent(JColorButton.this),
                "Select Color", ModalityType.APPLICATION_MODAL);
            newDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            newDialog.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent arg0) {
                    exitFail();
                }
            });

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    destroyDialog();
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    exitFail();
                }
            });
            
            JPanel panel = new JPanel();
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

        private void exitFail()
        {
            destroyDialog();
            chooser.setColor(color);            
        }

        private void destroyDialog()
        {
            JDialog strongDialog = DialogDisplay.this.dialog.get();
            strongDialog.setVisible(false);
            strongDialog.dispose();
        }
    }

    /**
     * Runs a demo of this class. A {@code JColorButton} is displayed along with
     * a pair of {@code JSpinners} that allow the icon size to be changed
     * dynamically, as well as a LAF selector that allows the user to see the
     * button being rendered by any of the currently installed LAFs.
     *
     * @param args the command line arguments of the program, which are ignored.
     * @see javax.swing.JSpinner JSpinner
     * @see SwingUtilities#lookAndFeelSelector(java.awt.Component)
     * SwingUtilities.lookAndFeelSelector()
     * @since 1.0.0
     */
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
