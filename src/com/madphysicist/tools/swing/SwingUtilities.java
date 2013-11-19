/*
 * SwingUtilities.java
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
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * Provides utilities for performing common tasks on members of the {@code
 * javax.swing} packages.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 25 Feb 2013
 * @version 1.0.1, 13 June 2013: Added {@code setEnabled()} method.
 * @since 1.0.0
 */
public class SwingUtilities
{
    /**
     * Private constructor to prevent instantiation.
     *
     * @since 1.0.0
     */
    private SwingUtilities() {}

    /**
     * Recursively sets the enabled state of a component and all of its
     * sub-components if it is a {@code Container}.
     *
     * @param comp the component to enable or disable.
     * @param b the enabled state to set for {@code comp} and possible children.
     * @since 1.0.1
     */
    public static void setEnabled(Component comp, boolean b)
    {
        comp.setEnabled(b);
        if(comp instanceof Container) {
            for(Component child : ((Container)comp).getComponents())
                setEnabled(child, b);
        }
    }

    /**
     * Returns the top level window containing a component. If the component is
     * itself a window, it is returned.
     *
     * @param comp the component to check.
     * @return the window for the selected component, or {@code null} if it has
     * not been added to a window.
     * @since 1.0.0
     */
    public static Window windowForComponent(Component comp)
    {
        if(comp instanceof Window)
            return (Window)comp;
        return javax.swing.SwingUtilities.getWindowAncestor(comp);
    }

    /**
     * Creates a {@code JLabel} that acts as a clickable hyperlink. This
     * component is initialized with underlined blue text which changes to
     * purple when the link is clicked. The hyperlink is opened in the default
     * system browser. The supplied address must be a valid URI string for the
     * hyperlink to work. Web links must begin with "http://". Any errors
     * encountered in opening the link are silently ignored.
     *
     * @param text the text to display. If {@code null}, the address string is
     * displayed instead.
     * @param address the address to open. Must be parseable as a valid URI.
     * @return a clickable JLabel that behaves like a hyperlink.
     * @since 1.0.0
     */
    public static JLabel createHyperlinkLabel(final String text, final String address)
    {
        String textStr = (text == null) ? address : text;
        JLabel linkLabel = new JLabel("<html><u>" + textStr + "</u></html>");
        linkLabel.setForeground(Color.BLUE);
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent event) {
                try {
                    Desktop.getDesktop().browse(new URI(address));
                    event.getComponent().setForeground(new Color(128, 0, 128));
                } catch(IOException | URISyntaxException ex) {}
            }
        });
        return linkLabel;
    }

    /**
     * Creates a {@code JComboBox} that allows the user to select the
     * look-and-feel for the specified component. The entries in the combo box
     * are the names of registered looks and feels. Whenever the selection
     * changes, the specified component gets its look-and-feel updated. If the
     * component has been added to a window, the window is repacked.
     *
     * @param base the component to modify in response to changes in the combo
     * box.
     * @return a combo box listing all of the currently registered
     * look-and-feels.
     * @since 1.0.0
     */
    public static JComboBox<?> lookAndFeelSelector(final Component base)
    {
        UIManager.LookAndFeelInfo[] infoList = UIManager.getInstalledLookAndFeels();
        LAFNode[] items = new LAFNode[infoList.length];
        for(int i = 0; i < items.length; i++) {
            items[i] = new LAFNode(infoList[i]);
        }

        JComboBox<LAFNode> comboBox = new JComboBox<>(items);
        comboBox.addItemListener(new ItemListener() {
            @Override public void itemStateChanged(ItemEvent e) {
                try {
                    UIManager.setLookAndFeel(((LAFNode)e.getItem()).info.getClassName());
                    javax.swing.SwingUtilities.updateComponentTreeUI(base);
                    Window window = windowForComponent(base);
                    if(window != null) window.pack();
                } catch(ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
                    // Silent ignore: do nothing in case of an error
                }
            }
        });

        return comboBox;
    }

    /**
     * Wraps a {@code UIManager.LookAndFeelInfo} object to provide the look and
     * feel name as its string representation. This allows the look and feel to
     * be displayed in a combo box with the default renderer.
     *
     * @see #lookAndFeelSelector(Component)
     * @see javax.swing.UIManager.LookAndFeelInfo
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 13 June 2013
     * @since 1.0.0
     */
    private static class LAFNode implements Serializable
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
         * The {@code LookAndFeelInfo} object wrapped by this class.
         *
         * @since 1.0.0
         */
        public final UIManager.LookAndFeelInfo info;

        /**
         * Constructs a new node with the specified info object.
         *
         * @param info the object to wrap.
         * @since 1.0.0
         */
        public LAFNode(UIManager.LookAndFeelInfo info)
        {
            this.info = info;
        }

        /**
         * Returns the name of the wrapped {@code LookAndFeelInfo} object. This
         * allows the object to be displayed as its name by the default combo
         * box renderer.
         *
         * @return the name of the underlying look and feel info.
         * @since 1.0.0
         */
        @Override public String toString()
        {
            return info.getName();
        }
    }

    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("Demo of HyperlinkLabel & LAFSelector v1.0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JLabel googleLink = createHyperlinkLabel(null, "http://google.com");
        JLabel yahooLink = createHyperlinkLabel("Yahoo!", "http://yahoo.com");
        JLabel wikipediaLink = createHyperlinkLabel("This will take you to English Wikipedia", "http://en.wikipedia.com");

        JComboBox<?> lafSelector = lookAndFeelSelector(frame);        

        frame.add(googleLink, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(5, 8, 2, 8), 0, 0));
        frame.add(yahooLink, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.NONE,
                  new Insets(2, 8, 2, 8), 0, 0));
        frame.add(wikipediaLink, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(2, 8, 2, 8), 0, 0));
        frame.add(lafSelector, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.NONE,
                  new Insets(2, 8, 5, 8), 0, 0));

        frame.pack();
        frame.setVisible(true);
    }
}
