/*
 * SwingUtilities.java (Class: com.madphysicist.tools.swing.SwingUtilities)
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

/**
 * Provides utilities for performing common tasks on members of the {@code
 * javax.swing} packages.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 25 Feb 2013 - J. Fox-Rabinovitz: Initial coding.
 * @version 1.1.0, 13 Jun 2013 - J. Fox-Rabinovitz: Added {@code setEnabled()} method.
 * @version 1.1.1, 13 Jun 2013 - J. Fox-Rabinovitz: Added {@code setGlobalAccelerator()} method.
 * @version 1.1.2,  9 Dec 2013 - J. Fox-Rabinovitz: Added {@code createCutCopyPastePopup()} method.
 * @version 2.0.0, 24 May 2014 - J. Fox-Rabinovitz: Removed {@code lookAndFeelSelector()} into {@code LAFUtilities}
 * @since 1.0
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
     * @since 1.1.0
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
     * Sets an accelerator for all subcomponents of the specified {@code
     * JComponent}. This method can be run on the {@code RootPane} of a window
     * to set the accelerator globally for the window. Note that this method
     * can override the default behavior of the current look-and-feel, in which
     * case the accelerator it sets may be deactivated when the look-and-feel
     * changes.
     *
     * @param component the base component to set the accelerator for. If any of
     * the components in the subtree of this one have a mapping for the
     * accelerator, the mapping is removed from their user {@code InputMap} and
     * parent {@code InputMap} (set by the LAF).
     * @param accelerator the keystroke that activates the accelerator.
     * @param action the action that is performed when the keystroke activates
     * it.
     * @since 1.1.1
     */
    public static void setGlobalAccelerator(JComponent component, KeyStroke accelerator, Action action)
    {
        Object actionCommand = action.getValue(Action.ACTION_COMMAND_KEY);
        if(actionCommand == null) {
            actionCommand = action.getValue(Action.NAME);
        }
        component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(accelerator, actionCommand);
        component.getActionMap().put(actionCommand, action);
        removeAcceleratorFromChildren(component, accelerator);
    }

    /**
     * Removes the specified keystroke form the input mappings of all {@code
     * JComponent} subcomponents of the specified container. This method is
     * recursive: all subcomponents that are also {@code Container}s are
     * processed.
     *
     * @param container the container whose children will have the accelerator
     * removed. The container itself does not lose the mappings it has for the
     * accelerator.
     * @param accelerator the accelerator to remove.
     * @since 1.1.1
     */
    private static void removeAcceleratorFromChildren(Container container, KeyStroke accelerator)
    {
        for(Component child : container.getComponents()) {
            if(child instanceof JComponent) {
                removeAcceleratorFromComponent((JComponent)child, accelerator);
            }
            if(child instanceof Container) {
                removeAcceleratorFromChildren((Container)child, accelerator);
            }
        }
    }

    /**
     * Removes the specified key mapping from a component. The mapping is
     * removed from all input maps and parent input maps. This affects the
     * behavior of the look-and-feel in some cases. The parent mappings will be
     * reinstated if the look-and-feel changes after this method is invoked.
     *
     * @param component the component to remove the mapping from.
     * @param accelerator the keystroke to remove.
     * @since 1.1.1
     */
    private static void removeAcceleratorFromComponent(JComponent component, KeyStroke accelerator)
    {
        removeAcceleratorFromMap(component.getInputMap(JComponent.WHEN_FOCUSED), accelerator);
        removeAcceleratorFromMap(component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), accelerator);
        removeAcceleratorFromMap(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), accelerator);
    }

    /**
     * Removes a keystroke from an input map and its parent map.
     *
     * @param map the map to remove the keystroke from.
     * @param accelerator the keystroke to remove.
     * @since 1.1.1
     */
    private static void removeAcceleratorFromMap(InputMap map, KeyStroke accelerator)
    {
        map.remove(accelerator);
        InputMap parent = map.getParent();
        if(parent != null) {
            parent.remove(accelerator);
        }
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
     * Creates a menu for a text component with select, copy and optionally cut
     * and paste actions.
     *
     * @param text the component to create the popup for.
     * @param includeModifying whether or not to include the modifying actions
     * (cut and paste) in the popup.
     * @return a popup menu that has actions for the component.
     * @since 1.1.2
     */
    public static JPopupMenu createActionMenu(JTextComponent text, boolean includeModifying)
    {
        JPopupMenu menu = new JPopupMenu();
        Action[] actions = text.getActions();
        Map<String, Action> actionMap = new HashMap<>(actions.length);
        for(Action action : actions) {
            actionMap.put((String)action.getValue(Action.NAME), action);
        }
        menu.add(setActionName("Select All", actionMap.get(DefaultEditorKit.selectAllAction)));
        menu.add(setActionName("Copy", actionMap.get(DefaultEditorKit.copyAction)));
        if(includeModifying) {
            menu.add(setActionName("Cut", actionMap.get(DefaultEditorKit.cutAction)));
            menu.add(setActionName("Paste", actionMap.get(DefaultEditorKit.pasteAction)));
        }
        return menu;
    }

    /**
     * Changes the name but not the command of an action. Use this method with
     * care.
     *
     * @param name the new name to assign to the action.
     * @param action the action to change the name of.
     * @return the input action.
     * @since 1.1.2
     */
    private static Action setActionName(String name, Action action)
    {
        action.putValue(Action.NAME, name);
        return action;
    }

    /**
     * Runs a small demo of the hyperlink labels from this class and the look-and-feel selector from {@link
     * LAFUtilities}.
     *
     * @param args command-line arguments, wich will be ignored.
     * @since 1.0.0
     */
    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("Demo of HyperlinkLabel & LAFSelector v1.1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JLabel googleLink = createHyperlinkLabel(null, "http://google.com");
        JLabel yahooLink = createHyperlinkLabel("Yahoo!", "http://yahoo.com");
        JLabel wikipediaLink = createHyperlinkLabel("This will take you to English Wikipedia", "http://en.wikipedia.com");

        JComboBox<?> lafSelector = LAFUtilities.lookAndFeelSelector(frame);        

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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
