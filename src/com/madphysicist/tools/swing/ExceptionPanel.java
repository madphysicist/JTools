/*
 * LoginPanel.java
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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Displays information about an exception. This class can show a standard
 * dialog as well.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0 21 Nov 2013 - J. Fox-Rabinovitz - Created
 * @since 1.0.0
 */

public class ExceptionPanel extends JPanel
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

    private static final String EXPAND_TEXT = "Details >>";
    private static final String COLLAPSE_TEXT = "Details <<";

    private Throwable exception;
    private String message;

    private JLabel notice;
    private JButton expand;
    private JTextArea details;

    public ExceptionPanel(Throwable exception)
    {
        this(exception, exception.getMessage());
    }

    public ExceptionPanel(Throwable exception, String message)
    {
        this.exception = exception;
        this.message = message;

        initComponents();
    }

    private void initComponents()
    {
        notice = new JLabel(message);
        notice.setHorizontalAlignment(SwingConstants.LEADING);

        expand = new JButton(new AbstractAction(EXPAND_TEXT) {
            private static final long serialVersionUID = 1000L;
            @Override public void actionPerformed(ActionEvent e) {
                if(details.isVisible()) {
                    details.setVisible(false);
                    expand.setText(EXPAND_TEXT);
                } else {
                    details.setVisible(true);
                    expand.setText(COLLAPSE_TEXT);
                }
                Window window = SwingUtilities.windowForComponent(ExceptionPanel.this);
                if(window != null) {
                    window.pack();
                }
            }
        });

        details = new JTextArea(stackTraceToString());
        details.setBorder(new BevelBorder(BevelBorder.LOWERED));
        details.setEditable(false);
        details.setEnabled(true);
        details.setFont(Font.decode(Font.MONOSPACED));
        details.setVisible(false);

        setLayout(new GridBagLayout());
        add(notice, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                GridBagConstants.FILL_HORIZONTAL_NORTH, 0, 0));
        addExpandComponent(expand);
        add(details, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                GridBagConstants.FILL_BOTH, 0, 0));
    }

    public String getMessage()
    {
        return message;
    }

    public Throwable getException()
    {
        return exception;
    }

    public void displayDialog(Window parent, String title)
    {
        new ExceptionDialog(parent, title).setVisible(true);
    }

    public static void showDialog(Throwable exception, Window parent, String title)
    {
        new ExceptionPanel(exception).displayDialog(parent, title);
    }

    public static void showDialog(Throwable exception, Window parent, String title, String message)
    {
        new ExceptionPanel(exception, message).displayDialog(parent, title);
    }

    private void addExpandComponent(Component component)
    {
        add(component, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                GridBagConstants.SOUTHEAST, 0, 0));
    }

    @SuppressWarnings("ConvertToTryWithResources")
    private String stackTraceToString()
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    /**
     * The dialog displayed by the {@code displayDialog()} and static {@code
     * showDialog()} methods of the parent class. This dialog adds an extra "OK"
     * button to the panel which is removed once the dialog is closed.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0 19 Nov 2013 - J. Fox-Rabinovitz - Created
     * @since 1.0.0
     */
    private class ExceptionDialog extends JDialog
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

        private JPanel expandPanel;

        public ExceptionDialog(Window parent, String title)
        {
            super(parent, title, ModalityType.APPLICATION_MODAL);

            JButton okButton = new JButton(new AbstractAction("OK") {
                private static final long serialVersionUID = 1000L;
                @Override public void actionPerformed(ActionEvent e) { destroy(); }
            });

            ExceptionPanel.this.remove(expand);
            expandPanel = new JPanel(new GridLayout(1, 2,
                    GridBagConstants.HORIZONTAL_INSET,
                    GridBagConstants.VERTICAL_INSET));
            expandPanel.add(expand);
            expandPanel.add(okButton);
            addExpandComponent(expandPanel);

            setLayout(new BorderLayout());
            add(ExceptionPanel.this, BorderLayout.CENTER);

            getRootPane().setDefaultButton(okButton);
            pack();
            setResizable(true);
            setLocationRelativeTo(parent);

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                 @Override public void windowClosing(WindowEvent e) { destroy(); }
            });
        }

        /**
         * Destroys the dialog GUI. The visibility is set to {@code false} so
         * that any methods blocking until the dialog disappears can continue.
         * The extra "OK" button is removed from the panel once it is no longer
         * displayed. The {@code ExceptionPanel} is removed from the dialog and
         * system resources are freed.
         *
         * @since 1.0.0
         */
        private void destroy()
        {
            setVisible(false);
            ExceptionPanel.this.remove(expandPanel);
            ExceptionPanel.this.addExpandComponent(expand);
            remove(ExceptionPanel.this);
            dispose();
        }
    }

    /**
     * Shows a small demo of this class with a {@code NullPointerException}.
     *
     * @param args the command line arguments, which are always ignored.
     */
    public static void main(String[] args)
    {
        try {
            Double.parseDouble(null);
        } catch(Exception ex) {
            showDialog(ex, null, "Demo of ExceptionPanel v1.0.0", "There has been an accident.");
        }
    }
}
