/*
 * ProcessInputListener.java
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Joseph Fox-Rabinovitz
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

package com.madphysicist.tools.proc;

import java.util.EventListener;

/**
 * An interface for processing events from a sub-process input pipe. Intended for use with {@link ProcessInputManager}.
 *
 * @see ProcessInputManager
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 May 2014 - J. Fox-Rabinovitz: Initial coding.
 * @version 2.0.0, 24 May 2014 - J. Fox-Rabinovitz: Added possibility for exception.
 * @since 1.0
 */
public interface ProcessInputListener extends EventListener
{
    /**
     * Reacts to input from the process. Each event represents a line. The event will not be {@code null}, but the line
     * it represents may be.
     *
     * @param event the event to react to.
     * @since 1.0.0
     */
    public void input(ProcessInputEvent event) throws InputException;
}
