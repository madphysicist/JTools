/*
 * ProcessInputEvent.java (Class: com.madphysicist.tools.proc.ProcessInputEvent)
 *
 * Mad Physicist JTools Project (System Process Utilities)
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

import java.util.EventObject;

/**
 * An event type that represents a line of input from a sub-process. These events are sent by {@code ProcessController}
 * for every line of input. Events can represent both standard output and standard error.
 *
 * @see ProcessInputListener
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 May 2014 - J. Fox-Rabinovitz: Created
 * @since 1.0
 */
public class ProcessInputEvent extends EventObject
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
     * The process that sent the specified event.
     *
     * @serial This field is not serializable.
     * @since 1.0.0
     */
    private transient Process process;

    /**
     * The line of process input that this event represents. This reference may be {@code null} to represent the end of
     * input.
     *
     * @serial
     * @since 1.0.0
     */
    private String line;

    /**
     * Constructs an event for the specified manager, process and line.
     *
     * @param manager the manager that ran the process that caused the event.
     * @param process the process that caused the event.
     * @param line the line of input that this event represents.
     * @since 1.0.0
     */
    public ProcessInputEvent(ProcessInputManager manager, Process process, String line)
    {
        super(manager);
        this.process = process;
        this.line = line;
    }

    /**
     * Returns the process manager which spawned the process from which this event originates.
     *
     * @return the process manager for this event.
     * @since 1.0.0
     */
    @Override public ProcessInputManager getSource()
    {
        return (ProcessInputManager)super.getSource();
    }

    /**
     * Returns the process from which this event originated.
     *
     * @return the process for this event.
     * @since 1.0.0
     */
    public Process getProcess()
    {
        return process;
    }

    /**
     * Returns the line which this event represents. Each line of output from the process generates a separate event.
     * If the line is {@code null}, the event represents the end of input from the process.
     *
     * @return the line represented by this event. The line may be {@code null}.
     * @since 1.0.0
     */
    public String getLine()
    {
        return line;
    }
}
