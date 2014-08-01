/*
 * BackgroundThread.java (Class: com.madphysicist.tools.util.BackgroundThread)
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

import java.io.Serializable;

/**
 * This class provides an framework for running tasks in a background thread.
 * Extending classes must synchronize on the {@code this} reference when waiting
 * to ensure proper thread interruption. Threads are stopped by posting a
 * request through the {@code stop} method.
 * <p>
 * Note that objects of this type are restartable unless the {@code start()}
 * method is overridden.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 16 Mar 2012
 * @since 1.0
 */
public abstract class BackgroundThread implements Runnable, Serializable
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
     * The name of the underlying thread. Since the multiple instances of a
     * background thread are avoided pretty carefully, the same name will be
     * used for each thread generated.
     * 
     * @serial
     * @since 1.0.0
     */
    private String threadName;

    /**
     * Whether or not the underlying thread should be a daemon thread. It should
     * be by default. If not, it will keep the application alive even after all
     * other threads have died. This field is necessary to keep track of the
     * status when the thread has been stopped and set to {@code null}.
     * 
     * @serial
     * @since 1.0.0
     */
    private boolean daemon;

    /**
     * The underlying thread. The thread can be restarted if it is either deemed
     * dead or this reference is {@code null}.
     * 
     * @serial this field is always set to {@code null} on deserialization until
     * {@code start()} is called.
     * @since 1.0.0
     */
    private transient Thread thread;

    /**
     * A flag indicating whether or not a stop request has been posted for this
     * thread. The {@code run} method is responsible for checking this flag via
     * {@link #stopped()}.
     *
     * @serial this field is always set to {@code true} on deserialization until
     * {@code start()} is called.
     * @since 1.0.0
     */
    private transient boolean stopped;

    /**
     * The default constructor. The thread name will be the simple name of the
     * runtime class of this instance. The constructor does not start the
     * thread; use {@link #start()} to do that.
     *
     * @since 1.0.0
     */
    public BackgroundThread()
    {
        this.threadName = getClass().getSimpleName();
        init();
    }

    /**
     * Initializes an instance that will contain the specified index in the
     * thread name. This constructor is useful for constructing multiple threads
     * of the same subclass. The name is constructed from the simple name of the
     * runtime class of this instance and the specified index.
     *
     * @param index the index of the thread. There are no restrictions on this
     * parameter.
     * @since 1.0.0
     */
    public BackgroundThread(int index)
    {
        this.threadName = getClass().getSimpleName() + "-" + index;
        init();
    }

    /**
     * Initializes an instance with the specified thread name. The constructor
     * does not start the thread; use {@link #start()} to do that.
     *
     * @param threadName the name to use for the threads spawned by this
     * instance.
     * @since 1.0.0
     */
    public BackgroundThread(String threadName)
    {
        this.threadName = threadName;
        init();
    }

    private void init()
    {
        this.thread = null;
        this.stopped = true;
        this.daemon = true;
    }

    /**
     * Starts the background thread running. This method does nothing if a
     * background thread is already running or if this instance is not
     * restartable.
     * 
     * @since 1.0.0
     */
    public synchronized void start()
    {
        if(!running()) {
            stopped = false; // this must be done before thread.start()
            thread = new Thread(this, threadName);
            thread.setDaemon(daemon);
            thread.start();
        }
    }

    /**
     * Requests the background thread to stop. The {@code run} method is
     * responsible for exiting when it has been interrupted and all threads
     * waiting for this instance's monitor have been notified. The status of the
     * request can be checked by the {@link #stopped} and {@link #running}
     * methods.
     * 
     * @since 1.0.0
     */
    public synchronized void stop()
    {
        if(!stopped) {
            stopped = true;
            thread.interrupt();
            notifyAll(); // lets threads know that its time to reawaken
        }
    }

    /**
     * Determines if the background thread has been requested to stop. It is the
     * responsibility of the {@code run} method to respond to this status.
     * 
     * @return {@code true} if a stop request has been posted and the thread has
     * been notified, {@code false} otherwise.
     * @since 1.0.0
     */
    public synchronized boolean stopped()
    {
        return stopped;
    }

    /**
     * Determines if the background thread is alive and running.
     * 
     * @return {@code true} if the thread is still running, {@code false}
     * otherwise.
     * @since 1.0.0
     */
    public synchronized boolean running()
    {
        return thread != null && thread.isAlive();
    }

    /**
     * Returns the name of the thread.
     *
     * @return the name with which this instance was initialized.
     * @since 1.0.0
     */
    public String name()
    {
        return threadName;
    }

    /**
     * Checks if this thread is a daemon thread, that is, if it will die when
     * all other non-daemon threads have died. This is usually true, unless
     * explicitly set otherwise.
     * 
     * @return {@code true} if this thread is a daemon thread, {@code false}
     * otherwise.
     * @see Thread#isDaemon()
     * @since 1.0.0
     */
    public boolean isDaemon()
    {
        return daemon;
    }

    /**
     * Sets whether or not the underlying thread will be a daemon thread, that
     * is, if it will die when all other non-daemon threads have died. The
     * thread is a daemon thread by default, but can be explicitly set
     * otherwise. If no thread is currently running, this flag will apply to
     * future threads.
     *
     * @see Thread#setDaemon(boolean)
     * @param daemon {@code true} if this thread should die when all non-daemon
     * threads are dead, {@code false} otherwise.
     * @since 1.0.0
     */
    public void setDaemon(boolean daemon)
    {
        // ensure that the next restart will be with the correct state
        this.daemon = daemon;
        // check that a currently running thread gets its state updated
        if(thread != null)
            thread.setDaemon(daemon);
    }

    /**
     * A method that performs some task on the calling thread. It is intended to
     * be called on the background thread when {@link #start()} is called. This
     * method is responsible for stopping itself after {@link #stop()} has been
     * called by checking the status of {@link #stopped()}.
     *
     * @since 1.0.0
     */
    @Override public abstract void run();
}
