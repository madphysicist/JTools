/*
 * LAFUtilities.java
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

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Provides utilities for performing common tasks on the look-and-feel of an UI.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 24 May 2014 - J. Fox-Rabinovitz: Initial Coding (from SwingUtilities).
 * @since 1.1
 */
public class LAFUtilities
{
    /**
     * The name of the Metal look-and-feel base class. This field is used by the {@link #setMetalLAF()} method.
     *
     * @since 1.0.0
     */
    private static final String METAL_LAF_CLASS = "javax.swing.plaf.metal.MetalLookAndFeel";

    /**
     * The name of the Nimbus look-and-feel base class. This field is used by the {@link #setNimbusLAF()} method.
     *
     * @since 1.0.0
     */
    private static final String NIMBUS_LAF_CLASS = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

    /**
     * Private constructor to prevent instantiation.
     *
     * @since 1.0.0
     */
    private LAFUtilities() {}

    /**
     * Attempts to set the default look-and-feel to Metal. If an exception occurs while setting the LAF, this method
     * fails silently and leaves the LAF as before.
     *
     * @since 1.0.0
     */
    public static void setMetalLAF()
    {
        setLAF(METAL_LAF_CLASS);
    }

    /**
     * Attempts to set the default look-and-feel to Nimbus. If an exception occurs while setting the LAF, this method
     * fails silently and leaves the LAF as before.
     *
     * @since 1.0.0
     */
    public static void setNimbusLAF()
    {
        setLAF(NIMBUS_LAF_CLASS);
    }

    /**
     * Attempts to set the default look-and-feel to the default cross-platform LAF specified by Java. If an exception
     * occurs while setting the LAF, this method fails silently and leaves the LAF as before.
     *
     * @since 1.0.0
     */
    public static void setCrossPlatformLAF()
    {
        setLAF(UIManager.getCrossPlatformLookAndFeelClassName());
    }

    /**
     * Attempts to set the default look-and-feel to the default LAF of the current system. If an exception occurs while
     * setting the LAF, this method fails silently and leaves the LAF as before.
     *
     * @since 1.0.0
     */
    public static void setSystemLAF()
    {
        setLAF(UIManager.getSystemLookAndFeelClassName());
    }

    /**
     * Attempts to set the look and feel the specified class. If an exception occurs while setting the LAF, this method
     * fails silently and leaves the LAF as before.
     *
     * @param className the name of the LAF class to set.
     * @since 1.0.0
     */
    public static void setLAF(String className)
    {
        try {
            UIManager.setLookAndFeel(className);
        } catch(ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex) {
            // Do nothing, just use the default LAF
        }
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
                    Window window = SwingUtilities.windowForComponent(base);
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
     * @version 1.0.0, 13 June 2013 - J. Fox-Rabinovitz - Created
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
}
