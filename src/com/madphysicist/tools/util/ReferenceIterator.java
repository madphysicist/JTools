/*
 * ReferenceIterator.java (Class: com.madphysicist.tools.util.ReferenceIterator)
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
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

import java.util.Iterator;

/**
 * @brief A single-element iterator over a single object.
 *
 * This class is a much simpler version of doing something like
 *
 *     Arrays.asList(new Object[] {object}).iterator()}
 *
 * This iterator also provides a `reset()` method that allows it to be run
 * multiple times without having to be re-created.
 *
 * This class does not support the `remove()` method for obvious reasons.
 * However, because it can be reset, this iterator is not thread safe.
 *
 * @param <E> the element type of the iterator. This is just the class of the
 *            object that it is initialized with.
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 17 Jul 2014 - J. Fox-Rabinovitz - Initial coding
 * @since 1.3
 */
public class ReferenceIterator<E> implements Iterator<E>
{
    /**
     * @brief The object to iterate over.
     *
     * @since 1.0.0
     */
    private final E theObject;

    /**
     * @brief A flag indicating whether or not this iterator has a next element.
     *
     * This flag is set to `true` in the constructor and `reset()` method, and
     * `false` in the `next()` method.
     *
     * @since 1.0.0
     */
    private boolean theNextFlag;

    /**
     * @brief Constructs a new iterator over the specified object.
     *
     * @param anObject The object to iterate over.
     * @since 1.0.0
     */
    public ReferenceIterator(E anObject)
    {
        this.theObject = anObject;
        this.theNextFlag = true;
    }

    /**
     * @brief Checks if the object has been iterated over.
     *
     * @return `true` as long as `next()` has not been called.
     * @since 1.0.0
     */
    @Override public boolean hasNext()
    {
        return theNextFlag;
    }

    /**
     * @brief Returns the object being iterated over if it has not already been
     * returned.
     *
     * @return The object which this iterator encapsulates.
     * @throws IllegalStateException if this method was called a second time
     * before `reset()`.
     * @since 1.0.0 
     */
    @Override public E next()
    {
        if(this.theNextFlag) {
            this.theNextFlag = false;
            return theObject;
        }
        throw new IllegalStateException("No more elements in " + getClass().getSimpleName());
    }

    /**
     * @brief An unsupported operation required by the `Iterator` interface.
     *
     * This method always throws an exception.
     *
     * @throws UnsupportedOperationException always.
     * @since 1.0.0
     */
    @Override public void remove()
    {
        throw new UnsupportedOperationException("Deleting from SelfIterator");
    }

    /**
     * @brief Resets this iterator.
     *
     * Further calls to `hasNext()` will return `true` and `next()` can be
     * invoked after this method without throwing an exception. If the iterator
     * is already active or has been reset, this method has no effect.
     *
     * @since 1.0.0
     */
    public void reset()
    {
        this.theNextFlag = true;
    }
}
