/*
 * StackedHandler.java (Class: com.madphysicist.tools.xml.StackedHandler)
 *
 * Mad Physicist JTools Project (XML Tools)
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
package com.madphysicist.tools.xml;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.madphysicist.tools.util.HashUtilities;

/**
 * A SAX handler that maintains a stack of the current location within the XML file. The elements of the stack define
 * the entire path of the current tag from the root element. A basic check is done to verify that the correct tags are
 * closed when appropriate.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 05 Jun 2013 - J. Fox-Rabinovitz: Initial coding.
 * @since 1.0.0
 */
public class StackedHandler extends DefaultHandler implements Iterable<StackedHandler.Element>
{
    /**
     * The stack of tags up to the current one. Stack elements are added via the {@link #startElement(String, String,
     * String, Attributes) startElement()} method. Stack elements are verified and popped via the {@link
     * #endElement(String, String, String) endElement()} method. The stack is created by the {@link #startDocument()}
     * method rather than the constructor. It is destroyed by the {@link #endDocument()} method.
     *
     * @since 1.0.0
     */
    protected Stack<Element> stack;

    /**
     * Constructs a handler. The stack is not created until a document is started.
     *
     * @since 1.0.0
     */
    public StackedHandler()
    {
        this.stack = null;
    }

    /**
     * Retrieves the last element from the stack. The element is retrieved using {@link Stack#peek()}.
     * 
     * @return the latest element for which {@link #startElement(String, String, String, Attributes) startElement()}
     * was invoked.
     * @since 1.0.0
     */
    public Element lastElement()
    {
        try {
            return stack.peek();
        } catch(EmptyStackException ese) {
            return null;
        }
    }

    /**
     * Starts processing a new document by creating a new stack.
     *
     * @since 1.0.0
     */
    @Override public void startDocument()
    {
        this.stack = new Stack<Element>();
    }

    /**
     * Ends a document by verifying and destroying the stack. If the stack is not empty, i.e., there are unclosed
     * tags remaining, an exception is thrown and the stack is not destroyed.
     *
     * @throws SAXException if there are unclosed tags at the end of the document. The stack will not be destroyed in
     * this case.
     * @since 1.0.0
     */
    @Override public void endDocument() throws SAXException
    {
        if(!stack.isEmpty())
            throw new SAXException("Unclosed tags at end of document.");
        destroyStack();
    }

    /**
     * Opens a tag by adding a new element to the stack.
     *
     * @param uri {@inheritDoc}
     * @param localName {@inheritDoc}
     * @param qualifiedName {@inheritDoc}
     * @param attributes {@inheritDoc}
     * @since 1.0.0
     */
    @Override public void startElement(String uri, String localName, String qualifiedName, Attributes attributes)
    {
        stack.push(new Element(uri, localName, qualifiedName));
    }

    /**
     * Closes a tag by verifying and popping an element from the stack. If the tag being closed does not match the last
     * open tag, an exception is thrown.
     *
     * @param uri {@inheritDoc}
     * @param localName {@inheritDoc}
     * @param qualifiedName {@inheritDoc}
     * @throws SAXException if the tag being closed does not match the last open tag, or if there are no open tags at
     * all. In the latter case, the cause of the exception will be a {@code EmptyStackException}.
     * @since 1.0.0
     */
    @Override public void endElement(String uri, String localName, String qualifiedName) throws SAXException
    {
        Element elem;
        try {
            elem = stack.pop();
        } catch(EmptyStackException ese) {
            throw new SAXException("Too many tags closed: " + localName, ese);
        }
        if(!elem.equals(uri, localName, qualifiedName))
            throw new SAXException("Start/close of element mismatch: " + localName + " != " + elem.localName);
    }

    /**
     * Returns an iterator over the elements of the stack in order of depth starting with the root element. This
     * iterator is not synchronized and will fail if the stack is modified concurrently.
     *
     * @return an iterator over the elements of the stack. The first element of the iteration will represent the root
     * tag of the XML file. The last element will correspond to the tag currently being parsed.
     * @since 1.0.0
     */
    @Override public Iterator<Element> iterator()
    {
        return stack.iterator();
    }

    /**
     * Destroys the stack. This method can be used to clean up if an exception occurs during the parsing process.
     *
     * @since 1.0.0
     */
    public void destroyStack()
    {
        this.stack = null;
    }

    /**
     * An element representing an XML tag. This class contains all of the distinguishing information about a tag as well
     * as a comparison method. 
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 05 Jun 2013 - J. Fox-Rabinovitz: Initial coding.
     * @version 1.0.1, 13 Mar 2015 - J. Fox-Rabinovitz: Added `hashCode()` to
     *          avoid compiler warnings.
     * @since 1.0.0
     */
    public static class Element implements Serializable
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
        private static final long serialVersionUID = 10000L;

        /**
         * The namespace URI. May be {@code null} or an empty string if the element has no namespace URI, or if
         * namespace processing is not being performed.
         *
         * @serial
         * @since 1.0.0
         */
        public final String uri;

        /**
         * The local name (without a prefix). May be {@code null} or an empty string if namespace processing is not
         * being performed.
         *
         * @serial
         * @since 1.0.0
         */
        public final String localName;

        /**
         * The qualified name (with a prefix). May be {@code null} or an empty string if qualified names are not
         * available.
         *
         * @serial
         * @since 1.0.0
         */
        public final String qualifiedName;

        /**
         * Constructs an element with the specified properties. Any of the properties may be {@code null} or empty.
         *
         * @param uri the namespace URI.
         * @param localName the local name (without a prefix).
         * @param qualifiedName the qualified name (with a prefix).
         * @since 1.0.0
         */
        public Element(String uri, String localName, String qualifiedName)
        {
            this.uri = uri;
            this.localName = localName;
            this.qualifiedName = qualifiedName;
        }

        /**
         * @brief Computes the hash code for this object.
         *
         * The hash code of two equal objects is guaranteed to be equal. The
         * hash is computed based on the `uri`, `localName` and `qualifiedName`
         * fields. Empty and `null` strings generate the same hash.
         *
         * @since 1.0.1
         */
        @Override public int hashCode()
        {
            int hashCode = HashUtilities.hashCode(this.uri.isEmpty() ? null : this.uri);
            hashCode = HashUtilities.hashCode(hashCode, this.localName);
            hashCode = HashUtilities.hashCode(hashCode, this.qualifiedName);
            return hashCode;
        }

        /**
         * Determines if this element is equal to the specified object.
         *
         * @return {@code true} if the other object is also an {@code Element},
         * and has the same {@code uri}, {@code localName} and {@code qualifiedName}.
         * @see #equals(String, String, String)
         * @since 1.0.0
         */
        @Override public boolean equals(Object o)
        {
            if(o instanceof Element) {
                Element elem = (Element)o;
                return equals(elem.uri, elem.localName, elem.qualifiedName);
            }
            return false;
        }

        /**
         * Determines if this element is equal to an element having the specified properties. Any of the properties may
         * be {@code null} or empty, both of which are considered to be the same thing.
         *
         * @param uri the namespace URI.
         * @param localName the local name (without a prefix).
         * @param qualifiedName the qualified name (with a prefix).
         * @return {@code true} if the specified URI, local name and qualified name are the same as the properties of
         * this element, {@code false} otherwise. Strings are compared as usual, with the exception that {@code null}
         * and empty strings are considered to be equal.
         * @since 1.0.0
         */
        public boolean equals(String uri, String localName, String qualifiedName)
        {
            return comp(this.uri, uri) && comp(this.localName, localName) && comp(this.qualifiedName, qualifiedName);
        }

        /**
         * Compares two strings such that {@code null} and empty strings are equal. All other cases are compared using
         * the regular {@code String.equals()} method.
         *
         * @param str1 the first string to compare.
         * @param str2 the second string to compare.
         * @return {@code true} if both strings are either {@code null} or empty values or they represent the same
         * sequence of characters.
         * @since 1.0.0
         */
        private static boolean comp(String str1, String str2)
        {
            if(str1 == null || str1.isEmpty())
                return (str2 == null || str2.isEmpty());
            return str1.equals(str2);
        }
    }
}
