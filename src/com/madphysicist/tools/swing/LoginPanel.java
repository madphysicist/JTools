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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * A panel in which the user can enter their user name and password. The user
 * name can optionally be selected from an editable dropdown combo box. This
 * class also provides a method for displaying a modal dialog for logging in.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0 17 Nov 2013 - J. Fox-Rabinovitz - Created
 * @since 1.0.0
 */
public class LoginPanel extends JPanel
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

    private static final String USER_NAME_STRING = "User Name:";
    private static final String PASSWORD_STRING = "Password:";
    private static final String DOMAIN_STRING = "Domain:";

    private JComboBox<String> userNameCombo;
    private SetListModel<String> userNameModel;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JComboBox<String> domainCombo;
    private SetListModel<String> domainModel;

    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel domainLabel;

    private String domainLabelText;

    public LoginPanel()
    {
        this(null);
    }

    public LoginPanel(Collection<String> userNames)
    {
        this(userNames, null);
    }

    public LoginPanel(Collection<String> userNames, Collection<String> domains)
    {
        super(new GridBagLayout());
        this.domainLabelText = DOMAIN_STRING;
        initComponents(userNames, domains);
    }

    private void initComponents(Collection<String> userNames, Collection<String> domains)
    {
        passwordField = new JPasswordField(20);

        userNameLabel = new JLabel(USER_NAME_STRING);
        passwordLabel = new JLabel(PASSWORD_STRING);
        passwordLabel.setLabelFor(passwordField);

        if(userNames == null || userNames.isEmpty()) {
            setUserNameField();
        } else {
            setUserNameCombo(userNames);
        }
        add(userNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                GridBagConstants.NORTHWEST, 0, 0));

        if(domains == null || domains.isEmpty()) {
            destroyDomainCombo(false);
        } else {
            createDomainCombo(domains, false);
        }
    }

    public boolean containsUserName(String userName)
    {
        return (userNameModel != null) && userNameModel.contains(userName);
    }

    public boolean containsDomain(String domain)
    {
        return (domainModel != null) && domainModel.contains(domain);
    }

    public boolean addUser(String userName)
    {
        if(userNameCombo == null) {
            setUserNameCombo(Arrays.asList(new String[] {userName}));
            return true;
        }
        return userNameModel.putElement(userName);
    }

    public boolean addDomain(String domain)
    {
        if(domainCombo == null) {
            createDomainCombo(Arrays.asList(new String[] {domain}), true);
            return true;
        }
        return domainModel.putElement(domain);
    }

    public void removeUser(String userName)
    {
        if(userNameModel != null) {
            userNameModel.removeElement(userName);
            if(userNameModel.isEmpty()) {
                setUserNameField();
            }
        }
    }

    public void removeDomain(String domain)
    {
        if(domainModel != null) {
            domainModel.removeElement(domain);
            if(domainModel.isEmpty()) {
                destroyDomainCombo(true);
            }
        }
    }

    public String getUserName()
    {
        if(userNameField != null) {
            return userNameField.getText();
        } else if(userNameCombo != null) {
            return userNameCombo.getSelectedItem().toString();
        } else {
            return null;
        }
    }

    public char[] getPassword()
    {
        if(passwordField != null) {
            return passwordField.getPassword();
        } else {
            return null;
        }
    }

    public String getDomain()
    {
        if(domainCombo != null) {
            return domainCombo.getSelectedItem().toString();
        } else {
            return null;
        }
    }

    /**
     * Sets the label text of the user name selector or text field.
     *
     * @param newLabel the new label text to set.
     * @since 1.0.0
     */
    public void setUserNameLabelText(String newLabel)
    {
        userNameLabel.setText(newLabel);
    }

    /**
     * Sets the label text of the password field.
     *
     * @param newLabel the new label text to set.
     * @since 1.0.0
     */
    public void setPasswordLabelText(String newLabel)
    {
        passwordLabel.setText(newLabel);
    }

    /**
     * Sets the label text of the domain selector. If the domain is not present,
     * this method records the new text for future use. The specified label will
     * show up later if domains are added to the selector.
     *
     * @param newLabel the new label text to set.
     * @since 1.0.0
     */
    public void setDomainLabelText(String newLabel)
    {
        this.domainLabelText = newLabel;
        if(domainLabel != null) {
            domainLabel.setText(newLabel);
        }
    }

    public JLabel getUserNameLabel()
    {
        return userNameLabel;
    }

    public JLabel getPasswordLabel()
    {
        return passwordLabel;
    }

    public JLabel getDomainLabel()
    {
        return domainLabel;
    }

    private void setUserNameCombo(Collection<String> userNames)
    {
        userNameModel = new SetListModel<>(userNames);
        userNameCombo = new JComboBox<>(userNameModel);
        userNameCombo.setEditable(true);
        addUserNameEditor(userNameCombo, userNameField);
        userNameField = null;
    }

    private void setUserNameField()
    {
        userNameField = new JTextField();
        addUserNameEditor(userNameField, userNameCombo);
        userNameCombo = null;
        userNameModel = null;
    }

    private void addUserNameEditor(JComponent userNameEditor, JComponent previousEditor)
    {
        boolean validateNeeded = (previousEditor != null);
        if(validateNeeded) {
            remove(previousEditor);
        }

        userNameLabel.setLabelFor(userNameEditor);
        add(userNameEditor, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                GridBagConstants.NORTHEAST, 0, 0));

        if(validateNeeded) {
            valipaint();
        }
    }

    private void createDomainCombo(Collection<String> domains, boolean validate)
    {
        domainModel = new SetListModel<>(domains);
        domainCombo = new JComboBox<>(domainModel);
        domainLabel = new JLabel(domainLabelText);
        domainLabel.setLabelFor(domainCombo);

        add(domainCombo, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                GridBagConstants.SOUTHEAST, 0, 0));
        add(domainLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                GridBagConstants.SOUTHWEST, 0, 0));

        replacePasswordInsets(GridBagConstants.EAST, GridBagConstants.WEST, validate);
    }

    private void destroyDomainCombo(boolean validate)
    {
        if(validate) {
            remove(domainCombo);
            remove(domainLabel);
        }

        domainModel = null;
        domainCombo = null;
        domainLabel = null;

        replacePasswordInsets(GridBagConstants.SOUTHEAST, GridBagConstants.SOUTHWEST, validate);
    }

    private void replacePasswordInsets(Insets editorInsets, Insets labelInsets, boolean validate)
    {
        if(validate) {
            remove(passwordField);
            remove(passwordLabel);
        }

        add(passwordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                editorInsets, 0, 0));
        add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                labelInsets, 0, 0));

        if(validate) {
            valipaint();
        }
    }

    private void valipaint()
    {
        validate();
        repaint();
    }

    /**
     * A panel that displays a {@link JTextField}, a {@link JList}, and two
     * {@link JButton}s to add and remove entries from either the user or domain
     * lists on a {@link LoginPanel}.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0 18 Nov 2013 - J. Fox-Rabinovitz - Created
     * @since 1.0.0
     */
    private class DemoPanel extends JPanel
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

        private final JTextField text;
        private final JList<String> list;
        private final SetListModel<String> model;
        private final JButton add;
        private final JButton remove;
        private final String type;

        public DemoPanel(String type)
        {
            super(new GridBagLayout());
            setBorder(new TitledBorder(type + " Names"));
            this.type = type;

            ActionListener addActionListener = new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    String entry = text.getText();
                    if(entry != null && !entry.isEmpty()) {
                        text.setText("");
                        model.putElement(entry);
                        try {
                            Method add = LoginPanel.class.getMethod("add" +
                                    DemoPanel.this.type, String.class);
                            add.invoke(LoginPanel.this, entry);
                        } catch(Exception ex) {
                            System.err.println("You messed up, clever user! (calling add" +
                                               DemoPanel.this.type + ")");
                            ex.printStackTrace();
                        }
                    }
                }
            };
            ActionListener removeActionListener = new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    int[] indices = list.getSelectedIndices();
                    // list is in ascending order
                    for(int i = indices.length - 1; i >= 0; i--) {
                        String entry = model.getElementAt(indices[i]);
                        model.removeElementAt(indices[i]);
                        try {
                            Method remove = LoginPanel.class.getMethod("remove" +
                                    DemoPanel.this.type, String.class);
                            remove.invoke(LoginPanel.this, entry);
                        } catch(Exception ex) {
                            System.err.println("You messed up, clever user! (calling remove" +
                                               DemoPanel.this.type + ")");
                            ex.printStackTrace();
                        }
                    }
                }
            };

            text = new JTextField(10);
            text.addActionListener(addActionListener);
            add(text, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    GridBagConstants.NORTHWEST, 0, 0));

            model = new SetListModel<>();
            list = new JList<>(model);
            add(list, new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    GridBagConstants.SOUTHWEST, 0, 0));

            add = new JButton("Add");
            add.addActionListener(addActionListener);
            add(add, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    GridBagConstants.NORTHEAST, 0, 0));

            remove = new JButton("Remove");
            remove.addActionListener(removeActionListener);
            add(remove, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    GridBagConstants.EAST, 0, 0));
        }
    }

    /**
     * Runs a demo of {@code LoginPanel}. The user is presented with a login
     * panel, a side panel that allows the editing of the login panel's
     * properties, and a look-and-feel selector that allows the components to be
     * viewed with any of the installed LAFs.
     *
     * @param args the command line arguments are always ignored.
     * @since 1.0.0
     */
    public static void main(String[] args)
    {
        LoginPanel loginPanel = new LoginPanel();
        loginPanel.setBorder(new TitledBorder("Login Panel"));

        JPanel sidePanel = new JPanel(new GridLayout(2, 1));
        sidePanel.setBorder(new TitledBorder("Edit Properties"));
        sidePanel.add(loginPanel.new DemoPanel("User"));
        sidePanel.add(loginPanel.new DemoPanel("Domain"));

        JFrame frame = new JFrame("LoginPanel Demo v1.0.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(loginPanel, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
        frame.add(SwingUtilities.lookAndFeelSelector(frame), BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }
}
