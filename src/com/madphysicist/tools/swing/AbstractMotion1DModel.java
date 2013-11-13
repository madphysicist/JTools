/*
 * AbstractMotion1DModel.java
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Implements the listener book-keeping specified in {@code Motion1DModel}. This
 * class places no restrictions on the underlying motion model. It is intended
 * only to create the underlying event and property change infrastructure. The
 * infrastructure can be accessed via the protected {@code fireStateChage()} and
 * {@code firePropertyChange()} methods. This class does not support vetoable
 * changes.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 20 Feb 2013
 * @since 1.0.0.0
 */
public abstract class AbstractMotion1DModel implements Motion1DModel, Serializable
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when compatibility
     * is not compromised by a structural change (e.g. adding a new field with
     * a sensible default value), and the upper digits when the change makes
     * serialized versions of of the class incompatible with previous releases.
     * @since 1.0.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * A mapping of property names to {@code Set}s of {@code
     * PropertyChangeListener}s which listen to them. A listener can only be
     * registered once per named property or once for all properties. Either
     * way, when a {@code PropertyChangeEvent} is fired, a given listener will
     * only be invoked once.
     *
     * @serial
     * @since 1.0.0.0
     */
    private Map<String, Set<PropertyChangeListener>> namedPropertyListeners;

    /**
     * A set of global property listeners. When a listener is added to this set,
     * all refrences to it are removed from {@link #namedPropertyListeners}, and
     * further references will not be added by {@link
     * #addPropertyChangeListener(String, PropertyChangeListener)}.
     *
     * @serial
     * @since 1.0.0.0
     */
    private Set<PropertyChangeListener> globalPropertyListeners;

    /**
     * A set of interested {@code ChangeListeners} that will be updated when
     * the model's state changes.
     *
     * @serial
     * @since 1.0.0.0
     */
    private Set<ChangeListener> changeListeners;

    /**
     * The {@code ChangeEvent} used to notify all registered {@code
     * ChangeListener}s. Since a change event only has a source and the source
     * does not change within a given model instatance, the same event can be
     * safely shared.
     *
     * @see #fireStateChange()
     * @serial
     * @since 1.0.0.0
     */
    private ChangeEvent changeEvent;

    /**
     * Constructs and initializes an empty instance of this class
     *
     * @since 1.0.0.0
     */
    public AbstractMotion1DModel()
    {
        this.namedPropertyListeners = new HashMap<>();
        this.globalPropertyListeners = new HashSet<>();
        this.changeListeners = new HashSet<>();

        this.changeEvent = new ChangeEvent(this);
    }

    /**
     * {@inheritDoc}
     * A change listener can only be added once to this instance. All attempts
     * to add a listener after the first one will be ignored.
     *
     * @param listener {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public void addChangeListener(ChangeListener listener)
    {
        changeListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     * A global property change listener can be added only once to this
     * instance. All attempts to add a listener after the first one will be
     * ignored. Adding a listener globally will force its removal from specific
     * properties.
     *
     * @param listener {@inheritDoc}
     * @throws NullPointerException if {@code listener} is {@code null}.
     * @since 1.0.0.0
     */
    @Override public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if(listener == null)
            throw new NullPointerException("listener");

        if(!globalPropertyListeners.contains(listener)) {
            for(Set<PropertyChangeListener> listenerSet : this.namedPropertyListeners.values())
                if(listenerSet.contains(listener))
                    listenerSet.remove(listener);

            globalPropertyListeners.add(listener);
        }
    }

    /**
     * {@inheritDoc}
     * A listener may only be registered once per property name. Calling this
     * method multiple times for the same {@code propertyName}-{@code listener}
     * combination will do nothing. A listener that is already registered
     * globally can not be added to a specific property and will be ignored as
     * well.
     *
     * @param propertyName {@inheritDoc}
     * @param listener {@inheritDoc}
     * @throws NullPointerException if either {@code propertyName} or {@code
     * listener} are null.
     * @since 1.0.0.0
     */
    @Override public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        if(propertyName == null)
            throw new NullPointerException("propertyName");
        if(listener == null)
            throw new NullPointerException("listener");
        if(!this.globalPropertyListeners.contains(listener))
        {
            if(this.namedPropertyListeners.containsKey(propertyName)) {
                this.namedPropertyListeners.get(propertyName).add(listener);
            } else {
                HashSet<PropertyChangeListener> listenerSet = new HashSet<>();
                listenerSet.add(listener);
                this.namedPropertyListeners.put(propertyName, listenerSet);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param listener {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public void removeChangeListener(ChangeListener listener)
    {
        this.changeListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     * The specified listener will be removed from all properties that it is
     * registered for. All registered instances of the listener are removed,
     * both from listening to all properties and from individual properties.
     *
     * @param listener {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        if(!this.globalPropertyListeners.remove(listener))
        {
            for(Set<PropertyChangeListener> listeners : this.namedPropertyListeners.values())
                listeners.remove(listener);
        }
    }

    /**
     * {@inheritDoc}
     * It is up to the child implementation to provide the functionality of this
     * method.
     *
     * @return {@inheritDoc}
     * @since 1.0.0.0
     */
    @Override public abstract double getState();

    /**
     * Notifies all interested listeners that the state of the model has
     * changed. The state may then be accessed through the {@link #getState()}
     * method.
     *
     * @since 1.0.0.0
     */
    protected void fireStateChange()
    {
        for(ChangeListener listener : this.changeListeners)
            listener.stateChanged(this.changeEvent);
    }

    /**
     * Sends a property {@code PropertyChangeEvent} to all bound {@code
     * PropertyChangeListeners}. This method should be called whenever a bean
     * property is updated.
     *
     * @param propertyName the name of the property that was changed.
     * @param oldValue the old value of the property.
     * @param newValue the new value of the property.
     * @since 1.0.0.0
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        Set<PropertyChangeListener> namedListeners = namedPropertyListeners.get(propertyName);
        if(namedListeners != null) {
            for(PropertyChangeListener listener : namedListeners)
                listener.propertyChange(event);
        }
        for(PropertyChangeListener listener : this.globalPropertyListeners)
            listener.propertyChange(event);
    }
}
