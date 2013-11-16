/*
 * TagAdapter.java
 *
 * Mad Physicist JTools Project (Javadoc Utilities)
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

package com.madphysicist.tools.javadoc;

import com.sun.javadoc.Doc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;

/**
 * This provides a trivial implementation of the {@code Tags} interface. It
 * is intended for use with the {@code LogTagletTest} cases. Only the
 * {@link #text()} is implemented in a useful way.
 * <p>
 * This is a helper class used for testing. It does not contain any tests.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 22 Sept 2012
 * @since 1.0.0.0
 */
public class TagAdapter implements Tag
{
    /**
     * The text of this tag. This text can be configured by the constructor
     * and retrieved by the {@code text()} method.
     *
     * @since 1.1.1.0
     */
    private String text;

    /**
     * Initializes an instance with the specified text.
     *
     * @param text the raw text of the tag.
     * @since 1.0.0.0
     */
    public TagAdapter(String text)
    {
        this.text = text;
    }

    /**
     * {@inheritDoc}
     * @return a {@code String} representing the name of the class.
     * @since 1.0.0.0
     */
    @Override public String name()
    {
        return this.getClass().getName();
    }

    /**
     * {@inheritDoc}
     * @return {@code null}.
     * @since 1.0.0.0
     */
    @Override public Doc holder()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return an empty string.
     * @since 1.0.0.0
     */
    @Override public String kind()
    {
        return "";
    }

    /**
     * {@inheritDoc}
     * @return the text with which this instance was initialized.
     * @since 1.0.0.0
     */
    @Override public String text()
    {
        return text;
    }

    /**
     * {@inheritDoc}
     * @return {@code null}.
     * @since 1.0.0.0
     */
    @Override public Tag[] inlineTags()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return {@code null}.
     * @since 1.0.0.0
     */
    @Override public Tag[] firstSentenceTags()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return {@code null}.
     * @since 1.0.0.0
     */
    @Override public SourcePosition position()
    {
        return null;
    }
}
