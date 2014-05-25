/*
 * ProcessController.java
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

package com.madphysicist.tools.proc;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class wraps the ProcessBuilder and Process classes together. It provides functionality for managing the {@link
 * ProcessInputListener}s which listen to the sub-process output and error streams. This class does not supply any
 * direct mechanism for using the output streams to a process. However, finer control can be obtained by using the
 * {@link #fork()} method to execute processes rather than the {link #execute()} method. 
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 May 2014 - J. Fox-Rabinovitz: Initial coding.
 * @version 1.1.0, 24 May 2014 - J. Fox-Rabinovitz: Added overrideable event firing methods.
 * @version 2.0.0, 24 May 2014 - J. Fox-Rabinovitz: Added handling for exceptions from listeners
 * @version 2.0.1, 25 May 2014 - J. Fox-Rabinovitz: Added getter for the exception handler
 * @since 1.0
 */
public abstract class ProcessInputManager
{
    /**
     * Listeners registered for event coming from the standard input of the sub-process.
     *
     * @since 1.0.0
     */
    private List<ProcessInputListener> inputListeners;

    /**
     * Listeners registered for event coming from the error input of the sub-process.
     *
     * @since 1.0.0
     */
    private List<ProcessInputListener> errorListeners;

    /**
     * Handler for exceptions thrown by listeners during processing of standard input. The default handler ignores
     * exceptions silently.
     *
     * @since 2.0.0
     */
    private InputExceptionHandler inputExceptionHandler;

    /**
     * Handler for exceptions thrown by listeners during processing of error input. The default handler ignores
     * exceptions silently.
     *
     * @since 2.0.0
     */
    private InputExceptionHandler errorExceptionHandler;

    /**
     * Constructs a process input manager.
     *
     * @since 1.0.0
     */
    public ProcessInputManager()
    {
        this.inputListeners = new ArrayList<>();
        this.errorListeners = new ArrayList<>();

        this.inputExceptionHandler = new InputExceptionHandler() {
            @Override public void exceptionOccurred(InputException exception) {}
        };
        this.errorExceptionHandler = this.inputExceptionHandler;
    }

    /**
     * Adds a listener for standard input events. Listeners should execute quickly to allow the input buffer to empty
     * quickly and avoid deadlock.
     *
     * @param listener the listener to add.
     * @since 1.0.0
     */
    public void addInputListener(ProcessInputListener listener)
    {
        inputListeners.add(listener);
    }

    /**
     * Adds a listener for error input events. Listeners should execute quickly to allow the input buffer to empty
     * quickly and avoid deadlock.
     *
     * @param listener the listener to add.
     * @since 1.0.0
     */
    public void addErrorListener(ProcessInputListener listener)
    {
        errorListeners.add(listener);
    }

    /**
     * Adds a listener to both standard error and input events. This is a convenience method that is equivalent to
     * calling both {@link #addInputListener(ProcessInputListener)} and {@link #addInputListener(ProcessInputListener)}. 
     *
     * @param listener the listener to add.
     * @since 1.0.0
     */
    public void addListener(ProcessInputListener listener)
    {
        addInputListener(listener);
        addErrorListener(listener);
    }

    /**
     * Removes a listener for standard input events. Attempting to remove a listener that is not registered is a no-op.
     *
     * @param listener the listener to remove.
     * @since 1.0.0
     */
    public void removeInputListener(ProcessInputListener listener)
    {
        inputListeners.remove(listener);
    }

    /**
     * Removes a listener for error input events. Attempting to remove a listener that is not registered is a no-op.
     *
     * @param listener the listener to remove.
     * @since 1.0.0
     */
    public void removeErrorListener(ProcessInputListener listener)
    {
        errorListeners.remove(listener);
    }

    /**
     * Removes a listener for both standard error and input events. This is a convenience method that is equivalent to
     * calling both {@link #removeInputListener(ProcessInputListener)} and {@link
     * #removeInputListener(ProcessInputListener)}. A listener will only be removed from the type of event it is
     * registered for. 
     *
     * @param listener the listener to remove.
     * @since 1.0.0
     */
    public void removeListener(ProcessInputListener listener)
    {
        removeInputListener(listener);
        removeErrorListener(listener);
    }

    /**
     * Returns the current exception handler for standard input.
     *
     * @return The exception handler for the standard input stream.
     * @since 2.0.1
     */
    public InputExceptionHandler getInputExceptionHandler()
    {
        return this.inputExceptionHandler;
    }

    /**
     * Returns the current exception handler for standard error.
     *
     * @return The exception handler for the standard error stream.
     * @since 2.0.1
     */
    public InputExceptionHandler getErrorExceptionHandler()
    {
        return this.errorExceptionHandler;
    }

    /**
     * Sets the specified exception handler for the standard input stream. Returns the previous handler. The default
     * exception handler ignores exceptions silently. The new handler may not be {@code null}, although it will not
     * cause a problem until an exception is thrown by one of the listeners.
     *
     * @param newHandler the new handler to assign. May not be {@code null}.
     * @return the previous handler used by this manager.
     * @since 2.0.0
     */
    public InputExceptionHandler setInputExceptionHandler(InputExceptionHandler newHandler)
    {
        InputExceptionHandler oldHandler = this.inputExceptionHandler;
        this.inputExceptionHandler = newHandler;
        return oldHandler;
    }

    /**
     * Sets the specified exception handler for the error input stream. Returns the previous handler. The default
     * exception handler ignores exceptions silently. The new handler may not be {@code null}, although it will not
     * cause a problem until an exception is thrown by one of the listeners.
     *
     * @param newHandler the new handler to assign. May not be {@code null}.
     * @return the previous handler used by this manager.
     * @since 2.0.0
     */
    public InputExceptionHandler setErrorExceptionHandler(InputExceptionHandler newHandler)
    {
        InputExceptionHandler oldHandler = this.errorExceptionHandler;
        this.errorExceptionHandler = newHandler;
        return oldHandler;
    }

    /**
     * Executes the a process and fires all {@code ProcessInputEvent}s it generates to registered listeners. The process
     * is constructed by the builder returned by {@link #getProcessBuilder()}. This method is useful for short processes
     * that produce a result without any user output. For processes that require output from the user or the parent
     * process, see the {@link #fork()} method.
     *
     * @return the return value of the process on exit. {@code 0} traditionally indicates success.
     * @throws IOException if any error occurs during process execution.
     * @since 1.0.0
     */
    public int execute() throws IOException
    {
        int returnValue = 0;

        Process process = getProcessBuilder().start();

        // Note here that the InputThread class is self-closing
        @SuppressWarnings("resource") InputThread inputHandler = new InputThread("InputParserExec", process, false);
        @SuppressWarnings("resource") InputThread errorHandler = new InputThread("ErrorParserExec", process, true);

        inputHandler.start();
        errorHandler.start();

        while(true) {
            try {
                returnValue = process.waitFor();
                break;
            } catch(InterruptedException ie) {
                continue;
            }
        }

        while(true) {
            try {
                inputHandler.join();
                errorHandler.join();
                break;
            } catch(InterruptedException ie) {
                continue;
            }
        }

        return returnValue;
    }

    /**
     * Starts a sub-process and returns immediately after registering listeners with it. The process is constructed by
     * the builder returned by {@link #getProcessBuilder()}. This method is useful if any sort of redirection or output
     * stream control is required over the process.
     *
     * @return the process started by this method.
     * @throws IOException if the process can not be started, or any of the input streams can not be acquired.
     * @since 1.0.0
     */
    public Process fork() throws IOException
    {
        Process process = getProcessBuilder().start();

        // Note here that the InputThread class is self-closing
        @SuppressWarnings("resource") InputThread inputHandler = new InputThread("InputParserFork", process, false);
        @SuppressWarnings("resource") InputThread errorHandler = new InputThread("ErrorParserFork", process, true);

        inputHandler.start();
        errorHandler.start();

        return process;
    }

    /**
     * Constructs or retrieves a process builder for this controller. Whether this method returns the same reference
     * between invocations or not is left entirely up to the implementation. This method is called once per {@link
     * #execute()} and {@code fork()}.
     *
     * @return the process builder that will construct the process which will be executed next.
     * @since 1.0.0
     */
    public abstract ProcessBuilder getProcessBuilder();

    /**
     * Fires an event to registered standard input listeners.
     *
     * @param process the process sending the event. This may be {@code null} to indicate a startup error, or a {@code
     * Process} reference returned by {@link #fork()}.
     * @param line the line of input that this event represents.
     * @since 1.1.0
     */
    protected void fireInputEvent(Process process, String line)
    {
        fireEvent(process, line, inputListeners, inputExceptionHandler);
    }

    /**
     * Fires an event to registered error input listeners.
     *
     * @param process the process sending the event. This may be {@code null} to indicate a startup error, or a {@code
     * Process} reference returned by {@link #fork()}.
     * @param line the line of input that this event represents.
     * @since 1.1.0
     */
    protected void fireErrorEvent(Process process, String line)
    {
        fireEvent(process, line, errorListeners, errorExceptionHandler);
    }

    /**
     * Fires an event to listeners from a specific list. All listeners will receive the same event per each invocation
     * of this method.
     *
     * @param process the process sending the event. This may be {@code null} to indicate a startup error, a {@code
     * Process} reference returned by {@link #fork()}, or the internal reference used by {@link #execute()}.
     * @param line the line of input that this event represents.
     * @param listeners the list of listeners that the event will be sent to. Listeners should execute quickly to avoid
     * deadlock around the input buffer.
     * @param handler the exception handler to invoke if something goes wrong in the listener.
     * @since 1.1.0
     */
    private void fireEvent(Process process, String line, List<ProcessInputListener> listeners, InputExceptionHandler handler)
    {
        ProcessInputEvent event = new ProcessInputEvent(this, process, line);
        for(ProcessInputListener listener : listeners) {
            try {
                listener.input(event);
            } catch(InputException ie) {
                handler.exceptionOccurred(ie);
            }
        }
    }

    /**
     * Allows asynchronous input from a sub-process input stream. This class runs in the background as a daemon. It can
     * be terminated prematurely with the {link #close()} method.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 22 May 2014 - J. Fox-Rabinovitz: Initial coding
     * @since 1.0.0
     */
    private class InputThread extends Thread implements Closeable
    {
        /**
         * The list of listeners that this thread fires events to. This will be one of {@link
         * ProcessInputManager#inputListeners} or {@link ProcessInputManager#errorListeners}.
         *
         * @since 1.0.0
         */
        private final List<ProcessInputListener> listeners;

        /**
         * The reader for this instance. Every line read by the reader will generate a new {@link ProcessInputEvent}.
         * The reader is wrapped around one of process's input streams. It can be closed using the {@link #close()}
         * method.
         *
         * @since 1.0.0
         */
        private final BufferedReader reader;

        /**
         * The process that generated the output that this thread listens to.
         *
         * @since 1.0.0
         */
        private final Process process;

        /**
         * Constructs a thread to listen to either the standard or error input stream of a process. Events generated
         * by this thread will be passed to the relevant listener type registered with the process controller.
         * Generally, each process should have two input threads, one for the standard and one for the error input.
         *
         * @param name the name to assign to this thread.
         * @param process the process that this thread will listen to.
         * @param error if {@code true}, this thread will listen to the error stream. If {@code false}, this thread will
         *      listen to the standard stream.
         * @since 1.0.0  
         */
        public InputThread(String name, Process process, boolean error)
        {
            super(name);
            setDaemon(true);

            this.process = process;
            InputStream stream;
            if(error) {
                this.listeners = ProcessInputManager.this.errorListeners;
                stream = process.getErrorStream();
            } else {
                this.listeners = ProcessInputManager.this.inputListeners;
                stream = process.getInputStream();
            }
            this.reader = new BufferedReader(new InputStreamReader(stream));
        }

        /**
         * Sends input from the relevant process input stream to registered listeners. Since listeners are run on the
         * input thread, they should execute quickly to avoid possible backup and deadlock. The last event fired always
         * has a {@code null} line associated with it.
         *
         *  @since 1.0.0
         */
        @Override public void run()
        {
            String line;
            do {
                try {
                    line = reader.readLine();
                } catch(IOException ioe) {
                    line = null;
                }
                fireEvent(line);
            } while(line != null);

            try {
                close();
            } catch(IOException ioe) {}
        }

        /**
         * Sends an event representing the specified line to all listeners registered with the process controller. Only
         * one event object is allocated per line. All registered listeners get a reference to the same event during an
         * invocation of this method.
         * <p>
         * A note to developers: The decision as to which exception handler to use from the enclosing class is made
         * every time an event is fired. This is done so that the thread gets the latest version of the handler in case
         * it is changed while the process is running. 
         *
         * @param line the line read by this event. A {@code null} line represents the end of input. The event fired
         * will not be {@code null}.
         * @since 1.0.0
         */
        private void fireEvent(String line)
        {
            ProcessInputManager.this.fireEvent(process, line, listeners, (listeners == inputListeners) ? inputExceptionHandler
                                                                                                       : errorExceptionHandler);
        }

        /**
         * Closes the reader associated with this input thread. Closing the reader that is already closed has no effect.
         * Closing an active thread will terminate it.
         *
         * @since 1.0.0
         */
        @Override public void close() throws IOException
        {
            reader.close();
        }
    }
}
