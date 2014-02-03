/*
 * Closer.java
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

import java.io.Closeable;
import java.io.IOException;

/**
 * Provides a mechanism for guaranteeing that input and output will get closed
 * when the application exits normally. This class registers a call to a {@code
 * Closeable}'s {@code close()} method with the application's {@code Runtime}.
 * The {@code close()} method should be fast. It is not guaranteed to be called
 * if the application terminates abnormally.
 *
 * @see Runtime#addShutdownHook(java.lang.Thread)
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 9 Nov 2012
 * @since 1.0.0
 */
public class Closer extends Thread
{
    /**
     * The {@code Closeable} to be closed. This class will effectively do
     * nothing if the closeable has been closed before the application
     * terminates.
     *
     * @since 1.0.0
     */
    private Closeable closeable;

    /**
     * Constructs a {@code Closer} with the specified {@code Closeable}
     * reference. Note that the {@code Closeable} will not be registered until
     * the {@link #register()} method is called.
     *
     * @param closeable the object to be registered with the {@code Runtime} to
     * be closed when the application terminates normally.
     * @since 1.0.0
     */
    public Closer(Closeable closeable)
    {
        this.closeable = closeable;
    }

    /**
     * Registers the {@code Closeable} associated with this instance to be
     * closed by the {@code Runtime} on shutdown. In actuality, this instance is
     * registered as a shutdown hook with the application's {@code Runtime}.
     *
     * @see #run()
     * @see Runtime#addShutdownHook(java.lang.Thread)
     * @since 1.0.0
     */
    public void register()
    {
        Runtime.getRuntime().addShutdownHook(this);
    }

    /**
     * Executes the {@code close()} method of the associated {@code Closeable}
     * and traps any exceptions that are thrown.
     * @since 1.0.0
     */
    @Override public void run()
    {
        try {
            closeable.close();
        } catch(IOException ioe) {
            // This is a shutdown hook, do nothing
        }
    }
}
