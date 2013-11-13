/*
 * Motion1DModel.java
 *
 * Mad Physicist JTools Project (Swing Utilities)
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

package com.madphysicist.tools.swing;

import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeListener;

/**
 * An interface for models that describe motion in a single dimension in
 * real-time. The model notifies listeners of changes to its state via {@code
 * ChangeListener}s and to changes to its properties via {@code
 * PropertyChangeListener}s. {@code ChangeListener}s are used to support
 * real-time processing since they require the listener to query the model for
 * up-to-date state information. This model type implicitly supports meta data
 * via {@code PropertyChangeListener}s. Unlike the state, {@code
 * PropertyChangeEvent}s encapsulate the actual values being changed. This
 * interface is part of the swing GUI package.
 *
 * @see ChangeListener
 * @see PropertyChangeListener
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 20 Feb 2013
 * @since 1.0.0.0
 */
public interface Motion1DModel
{
    /**
     * Adds a listener that responds to changes in the state of the model.
     *
     * @param listener the listener to add.
     * @since 1.0.0.0
     */
    public void addChangeListener(ChangeListener listener);

    /**
     * Adds a listener that responds to changes in any of the model's
     * properties.
     *
     * @param listener the listener to add.
     * @since 1.0.0.0
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Adds a listener that responds to changes in a specific property of the
     * model. This method throws a {@code NullPointerException} if the
     * property name is {@code null}.
     *
     * @param propertyName the property to listen for changes on.
     * @param listener the listener to add.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     * @since 1.0.0.0
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * Removes a listener that responds to changes in the state of the the
     * model.
     *
     * @param listener the listener to remove.
     * @since 1.0.0.0
     */
    public void removeChangeListener(ChangeListener listener);

    /**
     * Removes a listener that responds to changes in the model's properties.
     *
     * @param listener the listener to remove.
     * @since 1.0.0.0
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Queries the one-dimensional state of the model. Change listeners should
     * invoke this method to get the state once they have been notified.
     *
     * @return the current state of the model.
     * @since 1.0.0.0
     */
    public double getState();
}
