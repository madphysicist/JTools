/*
 * JFileField.java
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 * An extended swing component that displays a {@code JTextField} and a {@code
 * JFileChooser} triggred by a {@code JButton}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 28 May 2013 - J. Fox-Rabinovitz - Created
 * @since 1.0.0
 */
public class JFileField extends JPanel
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

    public static final String EDITABLE_PROPERTY = "editable";

    /**
     * @serial
     * @since 1.0.0.0
     */
    private JTextField textField;

    /**
     * @serial
     * @since 1.0.0.0
     */
    private JButton button;

    /**
     * @serial
     * @since 1.0.0.0
     */
    private JFileChooser fileChooser;

    /**
     * @serial
     * @since 1.0.0.0
     */
    private GridBagConstraints buttonConstraints;

    /**
     * @serial
     * @since 1.0.0.0
     */
    private GridBagConstraints textFieldConstraints;

    public JFileField()
    {
        this(false, JFileChooser.FILES_ONLY);
    }

    /**
     * @param absolute
     * @param mode one of {@code JFileChooser.FILES_AND_DIRECTORIES}, {@code
     * JFileChooser.FILES_ONLY} or {@code JFileChooser.DIRECTORIES_ONLY}, to
     * determine the legal selection type of the file chooser.
     * @throws IllegalArgumentException if {@code mode} is not
     */
    public JFileField(boolean absolute, int mode) throws IllegalArgumentException
    {
        this.textField = new JTextField();
        this.button = new JButton("...");
        this.fileChooser = new JFileChooser(new File("."));

        initTextField();
        initButton();
        initFileChooser(mode);
        initLayout();
    }

    private void initTextField()
    {
        textField.setEditable(false);
    }

    private void initButton()
    {
        button.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if(fileChooser.showDialog(JFileField.this, null) == JFileChooser.APPROVE_OPTION)
                    textField.setText(fileChooser.getSelectedFile().getPath());
            }
        });
    }

    private void initFileChooser(int mode)
    {
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setApproveButtonText("Select");
        fileChooser.setControlButtonsAreShown(true);
        fileChooser.setDialogTitle("Select File");
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setMultiSelectionEnabled(false);
    }

    private void initLayout()
    {
        setLayout(new GridBagLayout());

        this.textFieldConstraints = new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        this.buttonConstraints = new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0);

        add(textField, textFieldConstraints);
        add(button, buttonConstraints);
    }

    public int getMode()
    {
        return fileChooser.getFileSelectionMode();
    }

    public void setMode(int mode)
    {
        fileChooser.setFileSelectionMode(mode);
    }

    public void addFileFilter(FileFilter filter)
    {
        fileChooser.addChoosableFileFilter(filter);
    }

    public boolean removeFileFilter(FileFilter filter)
    {
        return fileChooser.removeChoosableFileFilter(filter);
    }

    public File getCurrentDirectory()
    {
        return fileChooser.getCurrentDirectory();
    }

    public void setCurrentDirectory(File currentDirectory)
    {
        fileChooser.setCurrentDirectory(currentDirectory);
    }

    public File getAbsoluteFile()
    {
        return new File(textField.getText()).getAbsoluteFile();
    }

    public File getFile()
    {
        return new File(textField.getText());
    }

    public void setFile(File file)
    {
        textField.setText(file.getPath());
        fileChooser.setCurrentDirectory(file);
        fileChooser.setSelectedFile(file);
    }

    public void setText(String text)
    {
        textField.setText(text);
    }

    /**
     * Sets the editable property of the file field. If the file field is
     * editable, users can type into the text box to change the value of the
     * field. Otherwise the only ways to change the value of the file field are
     * through the file chooser or programatically.
     *
     * @param editable a flag indicating whether or not the file field should be
     * editable. A property change event will be fired to all registered
     * listeners if the property changes.
     * @see #isEditable()
     * @see #EDITABLE_PROPERTY
     * @since 1.0.0
     */
    public void setEditable(boolean editable)
    {
        if(editable != textField.isEditable()) {
            textField.setEditable(editable);
            firePropertyChange(EDITABLE_PROPERTY, !editable, editable);
        }
    }

    /**
     * Determines whether the file field is editable.
     *
     * @return {@code true} if the file field is editable, {@code false}
     * otherwise.
     * @see #setEditable(boolean)
     * @since 1.0.0
     */
    public boolean isEditable()
    {
        return textField.isEditable();
    }

    public String getButtonText()
    {
        return button.getText();
    }

    public void setButtonText(String text)
    {
        button.setText(text);
    }

    public boolean isButtonRight()
    {
        return buttonConstraints.gridx == 1;
    }

    public void setButtonRight(boolean buttonRight)
    {
        if(isButtonRight() != buttonRight) {
            if(buttonConstraints.gridx == 1) {
                textFieldConstraints.gridx = 1;
                textFieldConstraints.anchor = GridBagConstraints.WEST;
                buttonConstraints.gridx = 0;
            } else {
                textFieldConstraints.gridx = 0;
                textFieldConstraints.anchor = GridBagConstraints.EAST;
                buttonConstraints.gridx = 1;
            }
            remove(textField);
            remove(button);
            add(textField, textFieldConstraints);
            add(button, buttonConstraints);
            validate();
        }
    }

    public JTextField getTextField()
    {
        return textField;
    }

    public JButton getButton()
    {
        return button;
    }

    public JFileChooser getFileChooser()
    {
        return fileChooser;
    }

    @Override public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        textField.setEnabled(b);
        button.setEnabled(b);
    }

    /**
     * Runs a demo of {@code JFileField}. The user is presented with an editable
     * {@code JFileField}, a button to toggle the relative positions of the file
     * field's text field and browse buttons, as well as a look-and-feel
     * selector that allows the components to be viewed with any of the
     * installed LAFs.
     *
     * @param args the command line arguments are always ignored.
     * @since 1.0.0
     */
    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("Demo of JFileField v1.0.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        final JFileField demoField = new JFileField();

        final JButton toggleButton = new JButton("Toggle position of text and button");
        toggleButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                demoField.setButtonRight(!demoField.isButtonRight());
            }
        });

        frame.add(demoField, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 8, 2, 8), 0, 0));
        frame.add(toggleButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(2, 8, 5, 5), 20, 0));
        frame.add(SwingUtilities.lookAndFeelSelector(frame),
                new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 5, 5, 8), 0, 0));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
