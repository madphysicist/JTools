/*
 * InputExceptionHandler.java
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

/**
 * A handler for {@code InputExceptions} that are thrown during the execution of {@code ProcessInputListener.input()}.
 * A {@code ProcessInputManager} has an exception handler registered.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 24 May 2014 - J. Fox-Rabinovitz: Initial coding
 * @since 2.0
 */
public interface InputExceptionHandler
{
    /**
     * Responds to an exception that occurs during processing of an input exception in an {@code ProcessInputListener}.
     * The exception contains a reference to the event which triggered it, which has references to both its originating
     * {@code ProcessInputManager} and {@code Process}.
     *
     * @param exception the exception to handle.
     * @since 1.0.0
     */
    public void exceptionOccurred(InputException exception);
}
