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

import com.madphysicist.tools.util.Credentials;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * A panel in which the user can enter their user name and password. The user
 * name can optionally be selected from an editable dropdown combo box. If the
 * panel is not initialized with any user names, the user name input is a text
 * field. If a list of domains is specified, a domain may also be selected from
 * a dropdown combo box. This combo box is not visible if there are no domains
 * to choose from. This class also has a method for displaying a modal dialog
 * for logging in.
 * <p>
 * This class is basically a configurable GUI editor for the {@link
 * com.madphysicist.tools.util.Credentials} class.
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

    /**
     * The default text of {@link #userNameLabel}.
     *
     * @since 1.0.0
     */
    private static final String USER_NAME_STRING = "User Name:";

    /**
     * The default text of {@link #passwordLabel}.
     *
     * @since 1.0.0
     */
    private static final String PASSWORD_STRING = "Password:";

    /**
     * The default text of {@link #domainLabel}.
     *
     * @since 1.0.0
     */
    private static final String DOMAIN_STRING = "Domain:";

    /**
     * The editable combo-box used to display and edit user names. The model of
     * this combo box is always set to a {@link SetListModel}, and used to store
     * the preconfigured user names. If there are no preconfigured user names,
     * this reference is {@code null}, and the user name is entered through
     * {@link #userNameField} instead.
     *
     * @see #userNameModel
     * @serial
     * @since 1.0.0
     */
    private JComboBox<String> userNameCombo = null;

    /**
     * A reference to the model of {@link #userNameCombo}. This field is {@code
     * null} if {@code userNameCombo} is {@code null}. This reference is
     * maintained to avoid constant casting whenever access to the model is
     * required.
     *
     * @serial
     * @since 1.0.0
     */
    private SetListModel<String> userNameModel = null;

    /**
     * The text field used to display and edit user names. If there are
     * preconfigured user names, they are displayed and edited by {@link
     * #userNameCombo} and this reference is set to {@code null}.
     *
     * @serial
     * @since 1.0.0
     */
    private JTextField userNameField = null;

    /**
     * The text field used to display and edit the password. The password is
     * never displayed as plain text. This reference is never {@code null} past
     * the constructor.
     *
     * @serial
     * @since 1.0.0
     */
    private JPasswordField passwordField = null;

    /**
     * The combo box used to display and select the domain. The model of this
     * combo box is always set to a {@link SetListModel}, and used to store the
     * preconfigured domains. If there are no preconfigured domains, this
     * reference is set to {@code null}.
     *
     * @see #domainModel
     * @serial
     * @since 1.0.0
     */
    private JComboBox<String> domainCombo = null;

    /**
     * A reference to the model of {@link #domainCombo}. This field is {@code
     * null} if {@code domainCombo} is {@code null}. This reference is
     * maintained to avoid constant casting whenever access to the model is
     * required.
     *
     * @serial
     * @since 1.0.0
     */
    private SetListModel<String> domainModel = null;

    /**
     * The label for the user name editor. The text of the label is configurable
     * via the {@link #setUserNameLabelText(String)}. If additional
     * configuration is required, the label itself may be retrieved using the
     * {@link #getUserNameLabel()} method. This reference is never {@code null}
     * past the constructor, although the component that it is a label for may
     * change.
     *
     * @serial
     * @since 1.0.0
     */
    private JLabel userNameLabel = null;

    /**
     * The label for the password text field. The text of the label is
     * configurable via the {@link #setPasswordLabelText(String)}. If additional
     * configuration is required, the label itself may be retrieved using the
     * {@link #getPasswordLabel()} method. This reference is never {@code null}
     * past the constructor.
     *
     * @serial
     * @since 1.0.0
     */
    private JLabel passwordLabel = null;

    /**
     * The label for the domain editor. The text of the label is configurable
     * via the {@link #setDomainLabelText(String)}. If additional configuration
     * is required, the label itself may be retrieved using the {@link
     * #getDomainLabel()} method. This reference is never {@code null} past the
     * constuctor, even if {@link #domainCombo} is set to {@code null}.
     *
     * @serial
     * @since 1.0.0
     */
    private JLabel domainLabel = null;

    /**
     * Constructs a panel with no preconfigured user names or domain names. This
     * will display a simple text field for the user name and a password field.
     * There will be no way to edit the domain name.
     *
     * @since 1.0.0
     */
    public LoginPanel()
    {
        this(null);
    }

    /**
     * Constructs a panel with no preconfigured domain names. This will display
     * an editable combo box to select user names if there are preconfigured
     * values. The password will be entered through a password field. There will
     * be no way to edit the domain name.
     *
     * @param userNames the preconfigured user names. The user names will be
     * displayed in a sorted editable combo box. If the collection is {@code
     * null} or empty, the user name input will revert to a text field.
     * @since 1.0.0
     */
    public LoginPanel(Collection<String> userNames)
    {
        this(userNames, null);
    }

    /**
     * Constructs a panel with preconfigured user names and domain names. This
     * will display an editable combo box to select user names if there are
     * preconfigured values. The password will be entered through a password
     * field. There will be a non-editable combo box to select the domain name,
     * if there are preconfigured values.
     *
     * @param userNames the preconfigured user names. The user names will be
     * displayed in a sorted editable combo box. If the collection is {@code
     * null} or empty, the user name input will revert to a text field.
     * @param domains the preconfigured domain names. The domain names will be
     * displayed in a sorted, non-editable combo box. If the collection is
     * {@code null} or empty, the domain name input will not be displayed at
     * all.
     * @since 1.0.0
     */
    public LoginPanel(Collection<String> userNames, Collection<String> domains)
    {
        super(new GridBagLayout());
        initComponents(userNames, domains);
    }

    /**
     * Initializes the sub-components of this panel. The user name is editor is
     * either a text field or a combo box. The password is manipulated through a
     * password field. The domain name is optionally selected from a
     * non-editable combo box.
     *
     * @param userNames the user names to configure this panel with. If this
     * collection contains values, the user name will be displayed and edited
     * through an editable combo box. Otherwise it will be in a text field.
     * @param domains the domain names to configure this panel with. If this
     * collection contains values, the domain name will be displayed and
     * selected through a non-editable combo box. Otherwise, the combo box will
     * not be displayed at all.
     * @since 1.0.0
     */
    private void initComponents(Collection<String> userNames, Collection<String> domains)
    {
        userNameLabel = new JLabel(USER_NAME_STRING);
        passwordLabel = new JLabel(PASSWORD_STRING);
        domainLabel = new JLabel(DOMAIN_STRING);

        passwordField = new JPasswordField(20);
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

    /**
     * Checks if the configured list of user names contains the specified value.
     *
     * @param userName the user name to check for.
     * @return {@code false} if there are no configured user names or if the
     * list does not contain the specified value, {@code true} otherwise.
     * @since 1.0.0
     */
    public boolean containsUserName(String userName)
    {
        return (userNameModel != null) && userNameModel.contains(userName);
    }

    /**
     * Checks if the configured list of domains contains the specified value.
     *
     * @param domain the domain to check for.
     * @return {@code false} if there are no configured domains or if the list
     * does not contain the specified value, {@code true} otherwise.
     * @since 1.0.0
     */
    public boolean containsDomain(String domain)
    {
        return (domainModel != null) && domainModel.contains(domain);
    }

    /**
     * Adds the specified user name to the configured list. If this is the first
     * user name to be configured, the text box editor will be converted to an
     * editable combo box to display the configured user name. Otherwise the
     * name will be added to the dropdown list of the existing combo box in
     * alphabetical order. Duplicate user names are ignored.
     *
     * @param userName the user name to add to the list of configured names.
     * @return {@code true} if the name was added to the list of preconfigured
     * user names, {@code false} if it was already in the list.
     * @since 1.0.0
     */
    public boolean addUserName(String userName)
    {
        if(userNameCombo == null) {
            setUserNameCombo(Arrays.asList(new String[] {userName}));
            return true;
        }
        return userNameModel.putElement(userName);
    }

    /**
     * Adds the specified domain to the configured list. If this is the first
     * domain to be configured, a non-editable combo box will be added to
     * display the configured domain. Otherwise the domain will be added to the
     * dropdown list of the existing combo box in alphabetical order. Duplicate
     * domains are ignored.
     *
     * @param domain the domain to add to the list of configured domains.
     * @return {@code true} if the domain was added to the list of preconfigured
     * domains, {@code false} if it was already in the list.
     * @since 1.0.0
     */
    public boolean addDomain(String domain)
    {
        if(domainCombo == null) {
            createDomainCombo(Arrays.asList(new String[] {domain}), true);
            return true;
        }
        return domainModel.putElement(domain);
    }

    /**
     * Removes the specified user name from the list of preconfigured list. If
     * the last user name in the list is removed, the editor is converted from a
     * combo box into a text field.
     *
     * @param userName the user name to remove.
     * @return {@code true} if the specified user name was removed from the
     * list, {@code false} if the list is empty or the user name was not found
     * in it.
     * @since 1.0.0
     */
    public boolean removeUserName(String userName)
    {
        if(userNameModel != null) {
            int size = userNameModel.getSize();

            userNameModel.removeElement(userName);
            if(userNameModel.isEmpty()) {
                setUserNameField();
                return true;
            }

            return (size != userNameModel.getSize());
        }
        return false;
    }

    /**
     * Removes the specified domain from the list of preconfigured list. If the
     * last domain in the list is removed, the combo box displaying the list is
     * removed entirely from the panel.
     *
     * @param domain the domain to remove.
     * @return {@code true} if the specified domain was removed from the list,
     * {@code false} if the list is empty or the domain was not found in it.
     * @since 1.0.0
     */
    public boolean removeDomain(String domain)
    {
        if(domainModel != null) {
            int size = domainModel.getSize();

            domainModel.removeElement(domain);
            if(domainModel.isEmpty()) {
                destroyDomainCombo(true);
                return true;
            }

            return (size != domainModel.getSize());
        }
        return false;
    }

    /**
     * Removes all preconfigured user names. The user name editor is converted
     * from a combo box to a text field.
     *
     * @since 1.0.0
     */
    public void clearUserNames()
    {
        if(userNameModel != null) {
            userNameModel.removeAllElements();
            setUserNameField();
        }
    }

    /**
     * Removes all preconfigured domains. The domain combo box is removed
     * entirely from this panel.
     *
     * @since 1.0.0
     */
    public void clearDomains()
    {
        if(domainModel != null) {
            domainModel.removeAllElements();
            destroyDomainCombo(true);
        }
    }

    /**
     * Retrieves the user name currently entered in the text field or combo box
     * of this panel.
     *
     * @return the current user name. May be an empty string.
     * @since 1.0.0
     */
    public String getUserName()
    {
        if(userNameField != null) {
            return userNameField.getText();
        } else if(userNameCombo != null) {
            return userNameCombo.getEditor().getItem().toString();
        } else {
            throw new IllegalStateException("UserName editor not initialized");
        }
    }

    /**
     * Returns the password currently entered int eh password field. This value
     * should be zeroed out once it is no longer needed.
     *
     * @return the current password. May be an empty array.
     * @since 1.0.0
     */
    public char[] getPassword()
    {
        if(passwordField != null) {
            return passwordField.getPassword();
        } else {
            throw new IllegalStateException("Password editor not initialized");
        }
    }

    /**
     * Returns the domain currently selected from the combo box on this panel.
     * Returns {@code null} if there are no preconfigured domains to display.
     *
     * @return the current domain. If the domain combo box is not displayed,
     * the return value is always {@code null}.
     * @since 1.0.0
     */
    public String getDomain()
    {
        if(domainCombo != null) {
            return domainCombo.getSelectedItem().toString();
        } else {
            return null;
        }
    }

    /**
     * Returns the user name, domain and password currently entered into the
     * form. The user name and password may be empty and the domain name may be
     * {@code null}.
     *
     * @return the current credentials.
     * @since 1.0.0
     */
    public Credentials getCredentials()
    {
        return new Credentials(getUserName(), getDomain(), getPassword());
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
     * Sets the label text of the domain selector. If the domain selector is not
     * shown, this method records the new text for future use. The specified
     * label will show up later if domains are added to the selector.
     *
     * @param newLabel the new label text to set.
     * @since 1.0.0
     */
    public void setDomainLabelText(String newLabel)
    {
        domainLabel.setText(newLabel);
    }

    /**
     * Retrieves the label of the user name editor in case additional
     * customization is required.
     *
     * @return the label for the user name editor.
     * @since 1.0.0
     */
    public JLabel getUserNameLabel()
    {
        return userNameLabel;
    }

    /**
     * Retrieves the label of the password field in case additional
     * customization is required.
     *
     * @return the label for the password field.
     * @since 1.0.0
     */
    public JLabel getPasswordLabel()
    {
        return passwordLabel;
    }

    /**
     * Retrieves the label of the domain selector in case additional
     * customization is required. Note that this reference will exist even if
     * the domain selector is not displayed.
     *
     * @return the label for the domain combo box.
     * @since 1.0.0
     */
    public JLabel getDomainLabel()
    {
        return domainLabel;
    }

    /**
     * Shows this panel on an appliation modal dialog and blocks until the
     * dialog closes. The dialog will have "OK" and "Cancel" buttons. If the
     * user clicks "OK", the entered credentials are returned. If the user
     * clicks "Cancel", the return value is {@code null}.
     *
     * @return either the entered credentials if the user clicks "OK", or {@code
     * null} if the user clicks "Cancel".
     * @since 1.0.0
     */
    public Credentials displayDialog()
    {
        return displayDialog(null);
    }

    /**
     * Shows this panel on an appliation modal dialog and blocks until the
     * dialog closes. The dialog will have "OK" and "Cancel" buttons. If the
     * user clicks "OK", the entered credentials are returned. If the user
     * clicks "Cancel", the return value is {@code null}.
     *
     * @param icons an icon set to use for the displayed dialog;
     * @return either the entered credentials if the user clicks "OK", or {@code
     * null} if the user clicks "Cancel".
     * @since 1.0.0
     */
    public Credentials displayDialog(List<? extends Image> icons)
    {
        LoginDialog dialog = new LoginDialog(icons);
        dialog.setVisible(true);
        return dialog.getCredentials();
    }

    /**
     * A convenience method for showing a dialog without explicitly
     * instantiating this class. This method is an alias for
     * <pre>showDialog(userNames, null, null)</pre>
     * The panel in the dialog will not display a domain selector.
     *
     * @param userNames the user names to show in the editable combo box where
     * the user enters their user name. If this reference is {@code null} or
     * empty, the user name will be entered through a simple text field.
     * @return the credentials set by the user in the dialog if the user clicks
     * "OK", or {@code null} if the user clicks "Cancel".
     * @see #showDialog(Collection, Collection, List)
     * @since 1.0.0
     */
    public static Credentials showDialog(Collection<String> userNames)
    {
        return showDialog(userNames, null, null);
    }

    /**
     * A convenience method for showing a dialog without explicitly
     * instantiating this class. This method is an alias for
     * <pre>new LoginPanel(userNames, domains).displayDialog()</pre>
     * 
     * @param userNames the user names to show in the editable combo box where
     * the user enters their user name. If this reference is {@code null} or
     * empty, the user name will be entered through a simple text field.
     * @param domains the domains to show in the non-editable combo box from
     * which the user selects their domain. If this reference is {@code null} or
     * empty, the domain combo box will not be shown at all.
     * @return the credentials set by the user in the dialog if the user clicks
     * "OK", or {@code null} if the user clicks "Cancel".
     * @see #displayDialog()
     * @since 1.0.0
     */
    public static Credentials showDialog(Collection<String> userNames, Collection<String> domains)
    {
        return new LoginPanel(userNames, domains).displayDialog();
    }

    /**
     * A convenience method for showing a dialog without explicitly
     * instantiating this class. This method is an alias for
     * <pre>new LoginPanel(userNames, domains).displayDialog(icons)</pre>
     * 
     * @param userNames the user names to show in the editable combo box where
     * the user enters their user name. If this reference is {@code null} or
     * empty, the user name will be entered through a simple text field.
     * @param domains the domains to show in the non-editable combo box from
     * which the user selects their domain. If this reference is {@code null} or
     * empty, the domain combo box will not be shown at all.
     * @param icons the icon that will be displayed on the dialog. This may be
     * {@code null}.
     * @return the credentials set by the user in the dialog if the user clicks
     * "OK", or {@code null} if the user clicks "Cancel".
     * @see #displayDialog(List)
     * @since 1.0.0
     */
    public static Credentials showDialog(Collection<String> userNames,
                                         Collection<String> domains,
                                         List<? extends Image> icons)
    {
        return new LoginPanel(userNames, domains).displayDialog(icons);
    }

    /**
     * Sets the user name editor to a combo box containing the specified values.
     * The values are assumed to be a non-empty collection. The model of {@link
     * #userNameCombo} is set to a {@link SetListModel}, referenced by {@link
     * #userNameModel}. {@link #userNameField} is set to {@code null} after
     * being removed from the pabel. The combo box is added in its place. {@link
     * #userNameLabel} is set as the label for the combo box.
     * <p>
     * The panel will be validated and repainted if the previous editor was a
     * text field. If the previous editor was {@code null}, we are still in the
     * constructor and should not validate yet.
     *
     * @param userNames the user names to add to the new combo box.
     * @see #addUserNameEditor(JComponent, JComponent)
     * @since 1.0.0
     */
    private void setUserNameCombo(Collection<String> userNames)
    {
        userNameModel = new SetListModel<>(userNames);
        userNameCombo = new JComboBox<>(userNameModel);
        userNameCombo.setEditable(true);
        addUserNameEditor(userNameCombo, userNameField);
        userNameField = null;
    }

    /**
     * Sets the user name editor to a text field. {@link #userNameCombo} and
     * {@link #userNameModel} are set to {@code null}, after being removed from
     * the panel. {@link #userNameField} is created and added in as the new
     * editor. {@link #userNameLabel} is set as the label for the text field.
     * <p>
     * The panel will be validated and repainted if the previous editor was a
     * combo box . If the previous editor was {@code null}, we are still in the
     * constructor and should not validate yet.
     *
     * @see #addUserNameEditor(JComponent, JComponent)
     * @since 1.0.0
     */
    private void setUserNameField()
    {
        userNameField = new JTextField();
        addUserNameEditor(userNameField, userNameCombo);
        userNameCombo = null;
        userNameModel = null;
    }

    /**
     * Adds the newly created component as an editor for user names in place of
     * the previous comonent. The new component may be a text field or editable
     * combo box, depending on whether there are preconfigured user names or
     * not. If the previous editor is {@code null}, the panel is not validated
     * or repainted because we are still in the constructor. The new editor is
     * added to the layout with the proper {@code GridBagConstraints} to
     * maintain the layout from {@link #initComponents(Collection, Collection)}.
     *
     * @param userNameEditor the new editor.
     * @param previousEditor either the previous editor component or {@code
     * null} if this method is invoked from the constructor. If it is not {@code
     * null}, the previous component is explicitly removed from this panel
     * before the new one is added, and the panel is validated and repainted
     * afterwards.
     * @see #valipaint()
     * @since 1.0.0
     */
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

    /**
     * Creates and adds the domain name combo box when the first domain name is
     * configured. The domain is specified as a collection so that the
     * constructor can add many domains simultaneously. The new combo box is
     * added to the layout with the proper {@code GridBagConstraints} to
     * maintain the layout from {@link #initComponents(Collection, Collection)}.
     * The insets of the components above it, the password field and label, are
     * also adjusted.
     *
     * @param domains the domains to show in the combo box. This collection is
     * assumed to be non-empty.
     * @param validate {@code true} if this panel needs to be validated and
     * repainted, {@code false} otherwise. The only time a validation is not
     * required is when this method is called from the constructor.
     * @see #replacePasswordInsets(Insets, Insets, boolean)
     * @since 1.0.0
     */
    private void createDomainCombo(Collection<String> domains, boolean validate)
    {
        domainModel = new SetListModel<>(domains);
        domainCombo = new JComboBox<>(domainModel);
        domainLabel.setLabelFor(domainCombo);

        add(domainCombo, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                GridBagConstants.SOUTHEAST, 0, 0));
        add(domainLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                GridBagConstants.SOUTHWEST, 0, 0));

        replacePasswordInsets(GridBagConstants.EAST, GridBagConstants.WEST, validate);
    }

    /**
     * Removes the domain name combo box when the last domain name is removed
     * from the configured list. To preserve the layout format from {@link
     * #initComponents(Collection, Collection)}, the insets of the components
     * above the combo box, the password field and label, are adjusted to
     * reflect that they are now at the bottom of the panel.
     *
     * @param validate {@code true} if this panel needs to be validated and
     * repainted, {@code false} otherwise. The only time a validation is not
     * required is when this method is called from the constructor.
     * @see #replacePasswordInsets(Insets, Insets, boolean)
     * @since 1.0.0
     */
    private void destroyDomainCombo(boolean validate)
    {
        if(validate) {
            remove(domainCombo);
            remove(domainLabel);
        }

        domainModel = null;
        domainCombo = null;

        replacePasswordInsets(GridBagConstants.SOUTHEAST, GridBagConstants.SOUTHWEST, validate);
    }

    /**
     * Replaces the {@code Inset}s used to display the password label and field
     * with the specified values. This should be done whenever a component is
     * added or subtracted from below the fields so that they move towards or
     * away from the bottom edge.
     *
     * @param editorInsets the insets of the editor field, which appears to the
     * right.
     * @param labelInsets the insets of the label, which appears to the left.
     * @param validate {@code true} if the component needs to be validated and
     * repainted after the insets are changed. The only time the component
     * should not be validated is when this method is called within the
     * constructor. If this parameter is {@code true}, the password field and
     * label will first be removed from the panel before being added back.
     * Otherwise, this method is being executed in the constructor and there is
     * nothing to remove.
     * @see #valipaint()
     * @since 1.0.0
     */
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

    /**
     * Calls {@link #validate()} and {@link #repaint()} on this component, in
     * that order. This method is provided purely for convenience.
     *
     * @since 1.0.0
     */
    private void valipaint()
    {
        validate();
        repaint();
    }

    /**
     * The dialog displayed by the {@code displayDialog()} and static {@code
     * showDialog()} methods of the parent class. This class records the user's
     * selection when one is made.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0 19 Nov 2013 - J. Fox-Rabinovitz - Created
     * @since 1.0.0
     */
    private class LoginDialog extends JDialog
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
         * The current credentials. This reference is always {@code null} until
         * the user presses the OK button, when the state of the panel at that
         * time is retrieved.
         *
         * @serial
         * @since 1.0.0
         */
        private Credentials credentials;

        public LoginDialog(List<? extends Image> icons)
        {
            super(null, "Login Credentials", ModalityType.APPLICATION_MODAL);
            this.credentials = null;

            JButton approveButton = new JButton(new AbstractAction("OK") {
                private static final long serialVersionUID = 1000L;
                @Override public void actionPerformed(ActionEvent e) {
                    destroy(LoginPanel.this.getCredentials());
                }
            });
            JButton rejectButton = new JButton(new AbstractAction("Cancel") {
                private static final long serialVersionUID = 1000L;
                @Override public void actionPerformed(ActionEvent e) { destroy(null); }
            });

            setLayout(new GridBagLayout());
            add(LoginPanel.this, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    GridBagConstants.FILL_HORIZONTAL_NORTH, 0, 0));
            add(rejectButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                    GridBagConstants.SOUTH, 0, 0));
            add(approveButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                    GridBagConstants.SOUTHEAST, 0, 0));

            setIconImages(icons);
            getRootPane().setDefaultButton(approveButton);
            pack();
            setResizable(false);
            setLocationRelativeTo(null);

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                 @Override public void windowClosing(WindowEvent e) { destroy(null); }
            });
        }

        public Credentials getCredentials()
        {
            return credentials;
        }

        /**
         * Destroys the dialog GUI. The visibility is set to {@code false} so
         * that any methods blocking until the dialog disappears can continue.
         * The {@code LoginPanel} is removed from the dialog and system
         * resources are freed. This method also sets {@link #credentials}.
         *
         * @param credentials the credentials to return. This value should be
         * {@code null} if this method is called when the "Cancel" button is
         * clicked. Otherwise, the credentials should come from the panel being
         * displayed.
         * @since 1.0.0
         */
        private void destroy(Credentials credentials)
        {
            this.credentials = credentials;
            setVisible(false);
            remove(LoginPanel.this);
            dispose();
        }
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
                @SuppressWarnings({"UseSpecificCatch", "CallToThreadDumpStack", "UseOfSystemOutOrSystemErr"})
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
                @SuppressWarnings({"UseSpecificCatch", "CallToThreadDumpStack", "UseOfSystemOutOrSystemErr"})
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
            add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                    new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0,
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
        if(args.length > 0 && args[0].equalsIgnoreCase("Dialog")) {
            Credentials credentials = showDialog(
                    Arrays.asList("user", "admin", "guest"),
                    Arrays.asList("WORKGROUP", "NET_DOMAIN"));

            if(credentials != null) {
                JOptionPane.showMessageDialog(null, "<html>" +
                        "UserName: " + credentials.getUserName() + "<br/>" +
                        "Password: " + new String(credentials.getPassword()) + "<br/>" +
                        "Domain: " + credentials.getDomain() + "</html>",
                        "Credentials Approved", JOptionPane.INFORMATION_MESSAGE);
                credentials.clearPassword();
            } else {
                JOptionPane.showMessageDialog(null,
                        "You chose to cancel your selection.",
                        "Credentials Rejected", JOptionPane.WARNING_MESSAGE);
            }
        } else {
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
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
