/*
 * TogglePanel.java
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
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Creates a container with a checkbox that enables or disables all of the other
 * components. The content pane and the checkbox may be modified by retrieving
 * them via {@code getContentPane()} and {@code getCheckBox()}, respectively.
 * Many of the content pane's {@code Container} methods are available as
 * convenience methods with the word "Content" in their name.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 13 June 2013
 * @since 1.0.0
 */
public class TogglePanel extends JPanel
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

    private JPanel panel;
    private JCheckBox checkBox;

    public TogglePanel()
    {
        this(null);
    }

    public TogglePanel(LayoutManager layout)
    {
        this.panel = new JPanel();
        this.checkBox = new JCheckBox();

        this.checkBox.setSelected(true);
        this.checkBox.addItemListener(new ItemListener() {
            @Override public void itemStateChanged(ItemEvent e) {
                SwingUtilities.setEnabled(panel, e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        if(!(getLayout() instanceof BorderLayout))
            setLayout(new BorderLayout());
        add(checkBox, BorderLayout.WEST, 0);
        add(panel, BorderLayout.CENTER, 1);

        if(layout != null)
            panel.setLayout(layout);
    }

    public Container getContentPane()
    {
        return panel;
    }
    
    public JCheckBox getCheckBox()
    {
        return checkBox;
    }

    public void addContent(Component comp)
    {
        panel.add(comp);
    }

    public void addContent(Component comp, Object constraints)
    {
        panel.add(comp, constraints);
    }

    public void addContent(Component comp, int index)
    {
        panel.add(comp, index);
    }

    public void addContent(Component comp, Object constraints, int index)
    {
        panel.add(comp, constraints, index);
    }

    public void addContent(String name, Component comp)
    {
        panel.add(name, comp);
    }

    public void removeContent(Component comp)
    {
        panel.remove(comp);
    }

    public void removeContent(int index)
    {
        panel.remove(index);
    }

    public void removeAllContent()
    {
        panel.removeAll();
    }

    public LayoutManager getContentLayout()
    {
        return panel.getLayout();
    }

    public void setContentLayout(LayoutManager layout)
    {
        panel.setLayout(layout);
    }

    public void doContentLayout()
    {
        panel.doLayout();
    }

    public Border getContentBorder()
    {
        return panel.getBorder();
    }

    public void setContentBorder(Border border)
    {
        panel.setBorder(border);
    }

    public Insets getContentInsets()
    {
        return panel.getInsets();
    }

    public Insets getContentInsets(Insets insets)
    {
        return panel.getInsets(insets);
    }

    @Override public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        checkBox.setEnabled(b);
    }

    public static void main(String[] args)
    {
        TogglePanel panel = new TogglePanel();

        panel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "TogglePanel",
                                         TitledBorder.CENTER, TitledBorder.CENTER));

        panel.setContentLayout(new GridBagLayout());
        panel.setContentBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "ContentPane",
                                                TitledBorder.CENTER, TitledBorder.CENTER));

        panel.addContent(new JLabel("Sample Text:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 8, 2, 5), 0, 0));
        panel.addContent(new JTextField("Demo Sample Text"), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 2, 8), 0, 0));
        panel.addContent(new JLabel("Sample Combo Box:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 8, 2, 5), 0, 0));
        panel.addContent(new JComboBox<>(new String[] {"Sample", "Demo"}), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 8), 0, 0));
        panel.addContent(new JLabel("Sample Button:"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 8, 5, 5), 0, 0));
        panel.addContent(new JButton("Demo Button"), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 5, 5, 8), 0, 0));

        JFrame frame = new JFrame("TogglePanel Demo v1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(panel, BorderLayout.CENTER);
        frame.add(SwingUtilities.lookAndFeelSelector(frame), BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
