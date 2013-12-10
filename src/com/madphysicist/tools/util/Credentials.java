/*
 * Credentials.java
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

package com.madphysicist.tools.util;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents the set of password based login credentials. The password should
 * be replaced with zeroes when it is no longer needed. The {@link
 * #clearPassword()} method is suitable if no other copies of the password have
 * been made.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0 20 Nov 2013 - J. Fox-Rabinovitz - Created
 * @version 1.0.1  9 Dec 2013 - J. Fox-Rabinovitz - Added toString()
 * @since 1.0.0
 */
public class Credentials implements Serializable
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
     * The user name associated with these credentials.
     *
     * @see #getUserName()
     * @serial
     * @since 1.0.0
     */
    private final String userName;

    /**
     * The domain name associated with these credentials.
     *
     * @see #getDomain()
     * @serial
     * @since 1.0.0
     */
    private final String domain;

    /**
     * The password associated with these credentials. This is the only
     * mutable field of the class. It is cleared by {@link
     * #clearPassword()}. This field is not serializable.
     *
     * @since 1.0.0
     */
    private transient char[] password;

    /**
     * Constructs a set of credentials from the specified parameters.
     *
     * @param userName the user name.
     * @param domain the domain name. May be {@code null}.
     * @param password the password.
     * @since 1.0.0
     */
    public Credentials(String userName, String domain, char[] password)
    {
        this.userName = userName;
        this.domain = domain;
        this.password = password;
    }

    /**
     * Retrieves the user name.
     *
     * @return the user name.
     * @since 1.0.0
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Retrieves the domain name. This value will be {@code null} if it was not
     * set.
     *
     * @return the domain name.
     * @since 1.0.0
     */
    public String getDomain()
    {
        return domain;
    }

    /**
     * Retrieves the password. The password should be zeroed out to clear it
     * once it is no longer needed. This can be done with the {@link
     * #clearPassword()} method.
     *
     * @return the password, or {@code null} if it has already been cleared.
     * @since 1.0.0
     */
    public char[] getPassword()
    {
        return password;
    }

    /**
     * Zeroes out the password. This method should be invoked when the password
     * is no longer needed. {@link #getPassword()} will return {@code null} once
     * this method has been invoked.
     *
     * @since 1.0.0
     */
    public void clearPassword()
    {
        if(password != null) {
            Arrays.fill(password, (char)0);
            password = null;
        }
    }

    /**
     * Returns a {@code String} representation of this instance. The password is
     * never shown, even when it has not been cleared.
     *
     * @return a {@code String} representing this instance, without the actual
     * password.
     * @since 1.0.1
     */
    @Override public String toString()
    {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" [userName=").append(userName).append("; ");
        if(domain != null) {
            sb.append("domain=").append(domain).append("; ");
        }
        sb.append("password=");
        if(password == null) {
            sb.append("<CLEARED>");
        } else {
            sb.append("*****");
        }
        sb.append("]");
        return sb.toString();
    }
}

