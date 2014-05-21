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
import java.awt.Cursor;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Displays information about an exception. The exception stack trace is shown
 * in a non-editable but selectable text area below the message. This area can
 * be expanded or collapsed by pressing a button. This class can show a dialog
 * as well.
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

    /**
     * The text that appears on the button notifying the user that the panel can
     * be expanded.
     *
     * @since 1.0.0
     */
    private static final String EXPAND_TEXT = "Details >>";

    /**
     * The text that appears on the button notifying the user that the panel can
     * be collapsed.
     *
     * @since 1.0.0
     */
    private static final String COLLAPSE_TEXT = "Details <<";

    /**
     * The exception that is displayed by this panel. The stack trace of the
     * exception is shown if the user expands the panel.
     *
     * @serial
     * @since 1.0.0
     */
    private Throwable exception;

    /**
     * The message that is displayed by the panel. The message appears in the
     * part of the panel that is always visible. The default value for the
     * message is just the exception message.
     *
     * @serial
     * @since 1.0.0
     */
    private String message;

    /**
     * The label that displays the message. The label is always in the visible
     * part of the component.
     *
     * @serial
     * @since 1.0.0
     */
    private JLabel messageLabel;

    /**
     * The button that triggers the expansion or collapse of the detailed
     * description of the exception. The text of the button is either {@link
     * #EXPAND_TEXT} if the detail area is collapsed, or {@link #COLLAPSE_TEXT}
     * if the area is expanded. The button is placed differently if this panel
     * appears in a dialog.
     *
     * @serial
     * @since 1.0.0
     */
    private JButton expandButton;

    /**
     * The text area containing the detailed exception information. The stack
     * trace of the exception is printed into this area.
     *
     * @serial
     * @since 1.0.0
     */
    private JTextArea details;

    /**
     * The scroll pane containing the text area. The visibility of this
     * component is altered to collapse or expand the details pane.
     *
     * @serial
     * @since 1.0.0
     */
    private JScrollPane collapsePane;

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
        messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.LEADING);

        expandButton = new JButton(new AbstractAction(EXPAND_TEXT) {
            private static final long serialVersionUID = 1000L;
            @Override public void actionPerformed(ActionEvent e) {
                if(collapsePane.isVisible()) {
                    collapsePane.setVisible(false);
                    expandButton.setText(EXPAND_TEXT);
                } else {
                    collapsePane.setVisible(true);
                    expandButton.setText(COLLAPSE_TEXT);
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
        details.setLineWrap(false);
        details.setTabSize(4);
        details.setRows(20);
        details.setFont(Font.decode(Font.MONOSPACED));
        details.setComponentPopupMenu(SwingUtilities.createActionMenu(details, false));

        collapsePane = new JScrollPane(details);
        collapsePane.setVisible(false);
        collapsePane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        setLayout(new GridBagLayout());
        add(messageLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                GridBagConstants.FILL_HORIZONTAL_NORTH, 0, 0));
        addExpandComponent(expandButton);
        add(collapsePane, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
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

        /**
         * The panel that replaces {@link ExceptionPanel#expandButton} in the
         * parent panel's layout. This panel contains the original button as
         * well as an additional "OK" button that closes that dialog. When the
         * dialog is closed, this panel is removed from the parent and replaced
         * with the original button.
         *
         * @serial
         * @since 1.0.0
         */
        private JPanel expandPanel;

        public ExceptionDialog(Window parent, String title)
        {
            super(parent, title, ModalityType.APPLICATION_MODAL);

            JButton okButton = new JButton(new AbstractAction("OK") {
                private static final long serialVersionUID = 1000L;
                @Override public void actionPerformed(ActionEvent e) { destroy(); }
            });

            ExceptionPanel.this.remove(expandButton);
            expandPanel = new JPanel(new GridLayout(1, 2,
                    GridBagConstants.HORIZONTAL_INSET,
                    GridBagConstants.VERTICAL_INSET));
            expandPanel.add(expandButton);
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
            ExceptionPanel.this.addExpandComponent(expandButton);
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
