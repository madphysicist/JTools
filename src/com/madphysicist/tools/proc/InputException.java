/*
 * InputException.java
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
 * Indicates that an error occurred when responding to a {@code ProcessInputEvent}. This exception may be used to wrap a
 * root cause. It can be further processed by the exception handler registered with the parent {@code
 * ProcessInputManager}. 
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 24 May 2014 - J. Fox-Rabinovitz: Initial coding.
 * @since 2.0
 */
public class InputException extends Exception
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
     * The event that caused this exception. The event contains a reference to the originating {@code 
     * ProcessInputManager} and {@code Process}, if any. This reference can be obtained through {@link #getEvent()}.
     *
     * @serial
     * @since 1.0.0
     */
    private ProcessInputEvent event;

    /**
     * Constructs a new exception with the specified event.
     *
     * @param event the event that triggered this exception.
     * @since 1.0.0
     */
    public InputException(ProcessInputEvent event)
    {
        super();
        this.event = event;
    }

    /**
     * Constructs a new exception with the specified event and message.
     *
     * @param event the event that triggered this exception.
     * @param msg the message to use for this exception.
     * @since 1.0.0
     */
    public InputException(ProcessInputEvent event, String msg)
    {
        super(msg);
        this.event = event;
    }

    /**
     * Constructs a new exception with the specified event and cause.
     *
     * @param event the event that triggered this exception.
     * @param cause the underlying cause of this exception.
     * @since 1.0.0
     */
    public InputException(ProcessInputEvent event, Throwable cause)
    {
        super(cause);
        this.event = event;
    }

    /**
     * Constructs a new exception with the specified event, message and cause.
     *
     * @param event the event that triggered this exception.
     * @param msg the message to use for this exception.
     * @param cause the underlying cause of this exception.
     * @since 1.0.0
     */
    public InputException(ProcessInputEvent event, String msg, Throwable cause)
    {
        super(msg, cause);
        this.event = event;
    }

    /**
     * Returns the event that caused this exception. The event contains more information about the conditions that
     * triggered the exception. It can be used to obtain references to the {@code ProcessInputManager} and {@code
     * Process} that originated the exception.
     *
     * @return the event that triggered this exception.
     * @since 1.0.0
     */
    public ProcessInputEvent getEvent()
    {
        return event;
    }
}
