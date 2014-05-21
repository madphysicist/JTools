/*
 * CasinoSpinnerModel.java
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * Stores the state of a {@code CasinoSpinner}. The model records the position
 * of the spinner and whether or not it is spinning. The spinner may be
 * visualized as a drum with a series of strings arranged around it. When the
 * spinner is spinning, the string facing the viewer gradually changes. The
 * model is assumed to be displayed with a single vertical size shared by all
 * list elements. This assumption is implied by the fact that the spin rate,
 * acceleration and spin sigma properties use the "list element" as a unit of
 * distance. Multiple displays can be attached to the same model.
 * </p>
 * <p>
 * The model also has {@code ProperChangeListener}s that it can notify when a
 * setter is executed successfully. The properties of the model may not be
 * changed while it is in motion, so there are two types of setter for each
 * property. The different setters react to a spinning model differently. The
 * default set simply throws an {@code IllegalStateException} if the spinner is
 * active. The other will either return as a no-op if the spinner is active, or
 * block until it is finished, depending on the boolean flag passed in as a
 * second argument.
 * </p>
 *
 * @see CasinoSpinner
 * @see PropertyChangeListener
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 19 Feb 2013
 * @version 1.0.0.1, 8 Apr 2013 - Added spin direction property.
 * @since 1.0.0.0
 */
public class CasinoSpinnerModel extends AbstractMotion1DModel
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
     * A spin direction constant that indicates that only positive spins are
     * allowed. This means that the position of the model is only allowed to
     * increase. Random spins will always be positive and the sign of spin
     * increments will be disregarded (set to their absolute value).
     *
     * @see #getSpinDirection()
     * @see #setSpinDirection(int)
     * @see #setSpinDirection(int, boolean)
     * @since 1.0.0.1
     */
    public static final int SPIN_POSITIVE = +1;

    /**
     * A spin direction constant that indicates that only negative spins are
     * allowed. This means that the position of the model is only allowed to
     * decrease. Random spins will always be negative and the sign of spin
     * increments will be disregarded (set to their negative absolute value).
     *
     * @see #getSpinDirection()
     * @see #setSpinDirection(int)
     * @see #setSpinDirection(int, boolean)
     * @since 1.0.0.1
     */
    public static final int SPIN_NEGATIVE = -1;

    /**
     * A spin direction constant that indicates that spin is allowed in both
     * directions. This means that the position of the model is only allowed to
     * either increase or decrease. Random spins will have a Gaussian
     * distribution, and the sign of spin increments will not be disregarded.
     *
     * @see #getSpinDirection()
     * @see #setSpinDirection(int)
     * @see #setSpinDirection(int, boolean)
     * @since 1.0.0.1
     */
    public static final int SPIN_BOTH = 0;

    /**
     * The name of the data property. This can be used to register a {@code
     * PropertyChangeListener} on changes to the data.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getData()
     * @see #setData(List)
     * @see #setData(List, boolean)
     * @since 1.0.0.0
     */
    public static final String DATA_PROPERTY = "data";

    /**
     * The name of the spin rate property. This can be used to register a {@code
     * PropertyChangeListener} on changes to the spin rate.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getSpinRate()
     * @see #setSpinRate(double)
     * @see #setSpinRate(double, boolean)
     * @since 1.0.0.0
     */
    public static final String SPIN_RATE_PROPERTY = "spinRate";

    /**
     * The name of the acceleration property. This can be used to register a
     * {@code PropertyChangeListener} on changes to the acceleration.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getAcceleration()
     * @see #setAcceleration(double)
     * @see #setAcceleration(double, boolean)
     * @since 1.0.0.0
     */
    public static final String ACCELERATION_PROPERTY = "acceleration";

    /**
     * The name of the refresh property. This can be used to register a {@code
     * PropertyChangeListener} on changes to the refresh rate and time. Note
     * that {@code PropertyChangeEvent}s regarding this property will always
     * give the old and new value of the refresh rate, not the refresh time.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getRefreshRate()
     * @see #getRefreshTime()
     * @see #setRefreshRate(int)
     * @see #setRefreshTime(long)
     * @see #setRefreshRate(int, boolean)
     * @see #setRefreshTime(long, boolean)
     * @since 1.0.0.0
     */
    public static final String REFRESH_PROPERTY = "refresh";

    /**
     * The name of the spin sigma property. This can be used to register a
     * {@code PropertyChangeListener} on changes to the spin sigma.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getSpinSigma()
     * @see #setSpinSigma(double)
     * @see #setSpinSigma(double, boolean)
     * @since 1.0.0.0
     */
    public static final String SPIN_SIGMA_PROPERTY = "spinSigma";

    /**
     * The name of the spin direction property. This can be used to register a
     * {@code PropertyChangeListener} on changes to the spin direction.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getSpinDirection()
     * @see #setSpinDirection(int)
     * @see #setSpinDirection(int, boolean)
     * @since 1.0.0.1
     */
    public static final String SPIN_DIRECTION_PROPERTY = "spinDirection";

    /**
     * The default full-speed rate of spin in objects per second. The object may
     * not reach its maximum speed, depending on the duration of the spin and
     * the acceleration. This value is a magnitude and will be applied to spins
     * in either direction.
     *
     * @since 1.0.0.0
     */
    private static final double DEFAULT_SPIN_RATE = 20.0;

    /**
     * The default acceleration and deceleration rate for the spin in objects
     * per second per second. This is the rate of change of the velocity until
     * it reaches either its maximum or the rotation is at its midpoint,
     * whichever comes first. It is also the rate at which the spin subsequently
     * slows down.  This value is a magnitude and will be applied to spins in
     * either direction.
     *
     * @since 1.0.0.0
     */
    private static final double DEFAULT_ACCELERATION = 10.0;

    /**
     * The default refresh rate of the animation in frames per second. This
     * number will be approximated as closely as possible because the actual
     * time between frames must be a whole number of milliseconds.
     *
     * @since 1.0.0.0
     */
    private static final int DEFAULT_REFRESH_RATE = 24;

    /**
     * The default standard deviation of number of objects rotated through on
     * each randomly generated spin. This means that approximately 67% of all
     * random spins will traverse at most this many objects from the list.
     *
     * @since 1.0.0.0
     */
    private static final double DEFAULT_SPIN_SIGMA = 50.0;

    /**
     * The default spin direction. The default is for the model to be allowed to
     * spin both ways.
     *
     * @since 1.0.0.1
     */
    private static final int DEFAULT_SPIN_DIRECTION = SPIN_BOTH;

    /**
     * The random number generator used to compute random spins. This object is
     * shared between all instances of {@code CasinoSpinnerModel}. It uses the
     * default seed, which is set when this class is loaded.
     *
     * @since 1.0.0.0
     */
    private static final Random RANDOM = new Random();

    /**
     * The timer that is used to run tasks for all objects of this type. Every
     * spin is executed by a new {@link TimerTask} which is scheduled at the
     * beginning of the spin to execute with the current frame rate. The task is
     * cancelled and removed at the end of a spin. This timer is a deamon, so
     * that it will not interfere with the graceful termination of the Virtual
     * Machine.
     *
     * @since 1.0.0.0
     */
    private static final Timer TIMER = new Timer("CasinoSpinnerModelTimer", true);

    /**
     * The current position of the spinner. If the spinner is at a standstill,
     * the position will be an integer eqivalent to the index of the element
     * currently being shown. When the spinner is spinning, this field is the
     * fractional position between elements. This field is re-initialized to
     * zero whenever a new non-{@code null} and non-empty data list is set. It
     * is set to {@code Double.NaN} if the data is {@code null} or empty.
     *
     * @serial
     * @since 1.0.0.0
     */
    private double position;

    /**
     * A flag to indicate whether or not this model is currently spinning. As
     * long as this flag is {@code true}, none of the setters will work and the
     * {@code spin} methods will do nothing. There is no way to set this flag
     * directly except through the {@code spin()} methods. All internal access
     * to this flag should be synchronized on the {@link #spinLock} object.
     *
     * @see #spin(int)
     * @see #spin()
     * @serial
     * @since 1.0.0.0
     */
    private boolean spinning;

    /**
     * The list of items managed by this model. This reference is an
     * unmodifiable list, so it can be passed outside the class freely. The
     * model never uses the contents of the data directly except to query its
     * size. The reason for maintaining the refrence is so that multiple clients
     * can have access to the same data-set and to the change notifications it
     * generates. The data may be set to {@code null}, in which case the model
     * will not spin. The data list must never be empty, so it is set to {@code
     * null} when an empty list is passed to the setters.
     *
     * @see #DATA_PROPERTY
     * @see Collections#unmodifiableList(List)
     * @serial
     * @since 1.0.0.0
     */
    private List<String> data;

    /**
     * The size of the {@link #data} property, as a {@code double}. This field
     * is set along with the data list whenever it is modified to elininate the
     * need for a repeated cast when the model is spinning. If the data is set
     * to {@code null}, this field is set to {@code Double.NaN}.
     *
     * @serial
     * @since 1.0.0.0
     */
    private double dataSize;

    /**
     * The maximum speed that the spinner can attain. The units are objects per
     * second, i.e., how many strings pass the starting point every second, not
     * the angular rate of the spinner if we imaging it to be a drum. When
     * spinning begins, the spinner speeds up at a rate determined by {@link
     * #acceleration}. It will stop accelerating when it reaches the maximum
     * spin rate. For spins that are short enough, the maximum spin rate may not
     * be attained at all before the spinner decelerates. The default spin rate
     * is {@value #DEFAULT_SPIN_RATE} objects per second.
     *
     * @see #SPIN_RATE_PROPERTY
     * @serial
     * @since 1.0.0.0
     */
    private double spinRate;

    /**
     * The acceleration with which the spin rate increases to and decreases from
     * its maximum value. The units are objects per second per second. The
     * default acceleration is {@value #DEFAULT_ACCELERATION}.
     *
     * @see #ACCELERATION_PROPERTY
     * @serial
     * @since 1.0.0.0
     */
    private double acceleration;

    /**
     * The rate at which the position is updated when the model is spinning. The
     * units are Herz. This number is honored as closely as possible, given that
     * the time between updates must be a whole number of milliseconds. The
     * default refresh rate is {@value #DEFAULT_REFRESH_RATE}. This number is
     * updated automatically with the rounded reciprocal of {@code refreshTime}
     * in seconds whenever {@code refreshTime} is modified.
     *
     * @see #refreshTime
     * @see #REFRESH_PROPERTY
     * @serial
     * @since 1.0.0.0
     */
    private int refreshRate;

    /**
     * The time between successive animation frames. The units are milliseconds.
     * This number is updated to the rounded reciprocal of {@code refreshRate}
     * in Kiloherz whenever {@code refreshRate} is modified. The defualt value
     * of this field is determined by the default for {@code refreshRate}.
     *
     * @see #refreshRate
     * @see #REFRESH_PROPERTY
     * @serial
     * @since 1.0.0.0
     */
    private long refreshTime;

    /**
     * The standard deviation of number of objects rotated through on each
     * randomly generated spin. This means that approximately 67% of all random
     * spins will traverse at most this many objects from the list. This field
     * is only used in the random {@link #spin()} method. The default sigma is
     * {@value #DEFAULT_SPIN_SIGMA}.
     *
     * @see #SPIN_SIGMA_PROPERTY
     * @serial
     * @since 1.0.0.0
     */
    private double spinSigma;

    /**
     * A flag indicating which direction the spinner is allowed to spin in. It
     * will always have one of the values {@link #SPIN_POSITIVE}, {@link
     * #SPIN_NEGATIVE} or {@link #SPIN_BOTH}. The default value is {@code
     * SPIN_BOTH}.
     *
     * @serial
     * @since 1.0.0.1
     */
    private int spinDirection;

    /**
     * A lock which is used to synchronize all access to the {@link #spinning}
     * flag. This allows setters and the {@code spin()} methods to operate
     * without interfering with each other. If the state of the flag is changed
     * in a block synchronized on this lock, at least one thread must be
     * released using either {@link Object#notify() spinLock.notify()} or {@link
     * Object#notifyAll() spinLock.notifyAll()}.
     *
     * @since 1.0.0.0
     */
    private final transient Object spinLock;

    /**
     * Constructs an empty model with {@code null} data and all other properties
     * set to their default values. This model will not spin until a non-{@code
     * null} data list is assigned to it.
     *
     * @since 1.0.0.0
     */
    public CasinoSpinnerModel()
    {
        this(null);
    }

    /**
     * Constructs a spinner model with the specified data list. The data is
     * copied so changes to the original list will not be reflected by this
     * instance. All other properties will be set to default values.
     *
     * @param data the data to construct this model with.
     * @since 1.0.0.0
     */
    public CasinoSpinnerModel(List<String> data)
    {
        this.spinning = false;
        this.spinLock = new Object();
        synchronized(this.spinLock) {
            setDataInternal(data);
            setDefaults();
        }
    }

    /**
     * Sets the intial values of all named properties except {@code data} to
     * default values. The primary purpose of this method is to perform part of
     * the initialization done in the consutructor. This method fires property
     * changes if any listeners are registered by the time it is invoked.
     * Invocations of this method must be performed within a block synchronized
     * on {@link #spinLock}.
     *
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired.
     * @since 1.0.0.0
     */
    private void setDefaults()
    {
        setSpinRateInternal(DEFAULT_SPIN_RATE);
        setAccelerationInternal(DEFAULT_ACCELERATION);
        setRefreshRateInternal(DEFAULT_REFRESH_RATE);
        setSpinSigmaInternal(DEFAULT_SPIN_SIGMA);
        setSpinDirectionInternal(DEFAULT_SPIN_DIRECTION);
    }

    /**
     * Spins the model by the specified number of list elements. The spin
     * consists of acceleration, cruising and deceleration phases. If the
     * requested spin is too short or the acceleration is too low to attain the
     * maximum speed, there will be no cruising phase. The maximum rotation
     * rate, acceleration magnitude and frame rate at which the state is
     * updated are all configurable properties. The direction of the spin
     * depends on the allowable directions. This method is a no-op if the model
     * is already spinning or if the data is {@code null}.
     *
     * @param increment the number of objects to rotate through. The sign will
     * be ignored unless the spin direction is set to {@link #SPIN_BOTH}.
     * @since 1.0.0.0
     */
    public void spin(int increment)
    {
        synchronized(this.spinLock) {
            if(!spinning && data != null) {
                // block the state
                spinning = true;
                // check the direction
                if(this.spinDirection != SPIN_BOTH) {
                    // this assumes that spinDirection = +/- 1
                    increment = Math.abs(increment) * this.spinDirection;
                }
                // set up to run
                TIMER.schedule(new SpinTask(increment), 0L, this.refreshTime);
            }
        }
    }

    /**
     * Performs a random spin. The number of full rotations to be performed is
     * selected from a random pseudo-Gaussian distribution with a width
     * specified by the {@code spinSigma} property. This method is a no-op if
     * the model is already spinning or if the data is {@code null}.
     * Approximately 67% of all randomly generated spins will rotate through
     * {@code spinSigma} or fewer list elements. The distribution may be folded
     * over on itself depending on the allowed spin directions.
     *
     * @see #getSpinSigma()
     * @see #setSpinSigma(double)
     * @see #setSpinSigma(double, boolean)
     * @since 1.0.0.0
     */
    public void spin()
    {
        // randomly select a number of full rotations to perform
        double rotations = RANDOM.nextGaussian() * DEFAULT_SPIN_SIGMA;
        // normalize to the number of objects to be traversed
        int increments = (int)(Math.signum(rotations) * Math.ceil(Math.abs(rotations)));
        // spin
        spin(increments);
    }

    /**
     * Returns the current position of the model. If the spinner is at a
     * standstill, the position will be an integer eqivalent to the index of the
     * element currently being shown. When the spinner is spinning, this field
     * is the fractional position between elements.
     *
     * @return the position of the spinner, in units of array indices.
     * @since 1.0.0.0
     */
    @Override public double getState()
    {
        return this.position;
    }

    /**
     * Returns the element currently being displayed. If the spinner is
     * spinning, the element closest to the center is returned. If the data is
     * {@code null}, {@code null} is returned.
     *
     * @return the element of the data closest to the center of the display.
     * @since 1.0.0.0
     */
    public String getCurrentElement()
    {
        return (this.data == null) ? null : this.data.get(getCurrentIndex());
    }

    /**
     * Returns the index of the element currently being displayed. If the
     * spinner is spinning, the index of the element closest to the center is
     * returned. If the data is {@code null}, {@code -1} is returned.
     *
     * @return the index of the element of the data closest to the center of
     * the display, or {@code -1} if there are no elements.
     * @since 1.0.0.0
     */
    public int getCurrentIndex()
    {
        return (this.data == null) ? -1 : normalize((int)Math.round(position));
    }

    /**
     * Retrieves the number of elements in the data list. This is a convenience
     * method that is equivalent to calling {@code getData().size()} if the data
     * is not {@code null}. If the data is {@code null}, this method returns
     * {@code -1}.
     *
     * @return the size of the data list, or {@code -1} if the list is {@code
     * null}.
     * @since 1.0.0.0
     */
    public int getDataSize()
    {
        return (this.data == null) ? -1 : this.data.size();
    }

    /**
     * Returns the underlying data array of the model. The returned list is
     * unmodifiable.
     *
     * @return an immutable list of this model's elements. This value may be
     * {@code null}.
     * @since 1.0.0.0
     */
    public List<String> getData()
    {
        return this.data;
    }

    /**
     * Sets the data list for this model if it is not spinning. If the model is
     * spinning, an {@code IllegalStateException} is thrown. For a version of
     * this method that does not throw an exception, see {@link #setData(List,
     * boolean)}. A copy of the data is made, so further changes to the list
     * being passed will not be reflected in the model. If the list is empty,
     * subsequent calls {@link #getData()} will return {@code null} rather than
     * an empty list. The position of this model will be reset to zero if the
     * new data is not {@code null} or empty. If the data is {@code null} or
     * empty, the position will be set to {@link Double#NaN} and the spinner
     * will not be able to spin. This method fires both state change and
     * property change events to listeners. The name of the data property is
     * given by the {@link #DATA_PROPERTY} constant of this class.
     *
     * @param data the new data to set. A copy of this list will be made for
     * internal storage. The internal list will be {@code null} if this
     * parameter is {@code null} or empty.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getState()
     * @since 1.0.0.0
     */
    public void setData(List<String> data) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setDataInternal(data))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the data list for this model if it is not spinning. If the model is
     * spinning, this method will either return immediately or block until the
     * end of the spin, depending on the second argument. For a version of
     * this method that throws an exception if the model is spinning, see {@link
     * #setData(List)}. A copy of the data is made, so further changes to the
     * list being passed will not be reflected in the model. If the list is
     * empty, subsequent calls {@link #getData()} will return {@code null}
     * rather than an empty list. The position of this model will be reset to
     * zero if the new data is not {@code null} or empty. If the data is {@code
     * null} or empty, the position will be set to {@link Double#NaN} and the
     * spinner will not be able to ssetDataInternalpin. This method fires both state change and
     * property change events to listeners. The name of the data property is
     * given by the {@link #DATA_PROPERTY} constant of this class.
     *
     * @param data the new data to set. A copy of this list will be made for
     * internal storage. The internal list will be {@code null} if this
     * parameter is {@code null} or empty.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getState()
     * @since 1.0.0.0
     */
    public void setData(List<String> data, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setDataInternal(data);
        }
    }

    /**
     * Sets the data list for this model if it is not spinning. If the model is
     * spinning, this method will return {@code false}. This method provides the
     * underlying functionality for the public {@code setData()} methods. The
     * data is copied into an internal unmodifyable buffer, so further changes
     * to the list being passed will not be reflected in the model. If the list
     * is empty, subsequent calls {@link #getData()} will return {@code null}
     * rather than an empty list. The position of this model will be reset to
     * zero if the new data is not {@code null} or empty. If the data is {@code
     * null} or empty, the position will be set to {@link Double#NaN} and the
     * spinner will not be able to spin. This method fires both state change and
     * property change events to listeners. The name of the data property is
     * given by the {@link #DATA_PROPERTY} constant of this class. Invocations
     * of this method must be performed within a block synchronized on {@link
     * #spinLock}.
     *
     * @param data the new data to set. A copy of this list will be made for
     * internal storage. The internal list will be cleared if this parameter is
     * {@code null} or empty.
     * @return {@code true} if the data was set, {@code false} otherwise. This
     * is exactly the inverse of the spinning flag when this method is called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @since 1.0.0.0
     */
    private boolean setDataInternal(List<String> data)
    {
        if(this.spinning)
            return false;
        List<String> oldData = getData();
        if(data == null || data.isEmpty()) {
            this.data = null;
            this.dataSize = Double.NaN;
            this.position = Double.NaN;
        } else {
            List<String> copy = new ArrayList<>(data);
            this.data = Collections.unmodifiableList(copy);
            this.dataSize = (double)data.size();
            this.position = 0.0;
        }
        fireStateChange();
        fireSyncedPropertyChange(DATA_PROPERTY, oldData, this.data);
        return true;
    }

    /**
     * Returns the maximum magnitude of this model's spin rate, in objects per
     * second. The model will spin with at this rate during the cruising phase
     * only. The model will be moving slower during the acceleration and
     * deceleration phases, and spins that are short enough may never attain
     * this rate at all. This value is a magnitude: the model may spin in either
     * direction.
     *
     * @return the maximum possible spin rate of this model.
     * @since 1.0.0.0
     */
    public double getSpinRate()
    {
        return this.spinRate;
    }

    /**
     * Sets the spin rate property for this model if it is not spinning. The
     * units are objects per second. If the model is spinning, an {@code
     * IllegalStateException} is thrown. For a version of this method that does
     * not throw an exception, see {@link #setSpinRate(double, boolean)}. This
     * method fires a property change event to listeners. The name of the spin
     * rate property is given by the {@link #SPIN_RATE_PROPERTY} constant of
     * this class.
     *
     * @param spinRate the new spin rate to set.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getSpinRate()
     * @since 1.0.0.0
     */
    public void setSpinRate(double spinRate) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setSpinRateInternal(spinRate))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the spin rate property for this model if it is not spinning. The
     * units are objects per second. If the model is spinning, this method will
     * either return immediately or block until the end of the spin, depending
     * on the second argument. For a version of this method that throws an
     * exception if the model is spinning, see {@link #setSpinRate(double)}.
     * This method fires a property change event to listeners. The name of the
     * spin rate property is given by the {@link #SPIN_RATE_PROPERTY} constant
     * of this class.
     *
     * @param spinRate the new spin rate to set.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getSpinRate()
     * @since 1.0.0.0
     */
    public void setSpinRate(double spinRate, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setSpinRateInternal(spinRate);
        }
    }

    /**
     * Sets the spin rate property for this model if it is not spinning. The
     * units are objects per second. If the model is spinning, this method will
     * return {@code false}. This method provides the underlying functionality
     * for the public {@code setSpinRate()} methods. This method fires a
     * property change event to listeners. The name of the spin rate property is
     * given by the {@link #SPIN_RATE_PROPERTY} constant of this class.
     * Invocations of this method must be performed within a block synchronized
     * on {@link #spinLock}.
     *
     * @param spinRate the new spin rate to set.
     * @return {@code true} if the spin rate was set, {@code false} otherwise.
     * This is exactly the inverse of the spinning flag when this method is
     * called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @see #getSpinRate()
     * @since 1.0.0.0
     */
    private boolean setSpinRateInternal(double spinRate)
    {
        if(this.spinning)
            return false;
        double oldRate = this.spinRate;
        this.spinRate = spinRate;
        fireSyncedPropertyChange(SPIN_RATE_PROPERTY, oldRate, this.spinRate);
        return true;
    }

    /**
     * Returns the magnitude of the acceleration of the model, in objects per
     * second per second. The model will speed up and slow down with this
     * acceleration. If there is a cruising phase, the model will not accelerate
     * while it is cruising. Otherwise, the acceletration will switch sign
     * half way through the spin without going to zero at all.
     *
     * @return the acceleration of this model.
     * @since 1.0.0.0
     */
    public double getAcceleration()
    {
        return this.acceleration;
    }

    /**
     * Sets the acceleration property for this model if it is not spinning. The
     * units are objects per second per second. If the model is spinning, an
     * {@code IllegalStateException} is thrown. For a version of this method
     * that does not throw an exception, see {@link #setAcceleration(double,
     * boolean)}. This method fires a property change event to listeners. The
     * name of the spin rate property is given by the {@link
     * #ACCELERATION_PROPERTY} constant of this class.
     *
     * @param acceleration the acceleration to set.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getAcceleration()
     * @since 1.0.0.0
     */
    public void setAcceleration(double acceleration) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setAccelerationInternal(acceleration))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the acceleration property for this model if it is not spinning. The
     * units are objects per second per second. If the model is spinning, this
     * method will either return immediately or block until the end of the spin,
     * depending on the second argument. For a version of this method that
     * throws an exception if the model is spinning, see {@link
     * #setAcceleration(double)}. This method fires a property change event to
     * listeners. The name of the spin rate property is given by the {@link
     * #ACCELERATION_PROPERTY} constant of this class.
     *
     * @param acceleration the new acceleration to set.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getAcceleration()
     * @since 1.0.0.0
     */
    public void setAcceleration(double acceleration, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setAccelerationInternal(acceleration);
        }
    }

    /**
     * Sets the acceleration property for this model if it is not spinning. The
     * units are objects per second per second. If the model is spinning, this
     * method will return {@code false}. This method provides the underlying
     * functionality for the public {@code setAcceleration()} methods. This
     * method fires a property change event to listeners. The name of the
     * acceleration property is given by the {@link #ACCELERATION_PROPERTY}
     * constant of this class. Invocations of this method must be performed
     * within a block synchronized on {@link #spinLock}.
     *
     * @param acceleration the new acceleration to set.
     * @return {@code true} if the acceleration was set, {@code false}
     * otherwise. This is exactly the inverse of the spinning flag when this
     * method is called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @see #getAcceleration()
     * @since 1.0.0.0
     */
    private boolean setAccelerationInternal(double acceleration)
    {
        if(this.spinning)
            return false;
        double oldAcceleration = this.acceleration;
        this.acceleration = acceleration;
        fireSyncedPropertyChange(ACCELERATION_PROPERTY, oldAcceleration, this.acceleration);
        return true;
    }

    /**
     * Returns the approximate refresh rate of the model, in Herz or frames per
     * second (FPS). The refresh rate is how often the state of the model will be updated when it
     * is spinning. The actual refresh happens a whole number of milliseconds
     * apart, so this rate is only approximate. A state change is fired every
     * time the model is refreshed.
     *
     * @return the refresh rate of the model when it is spinning.
     * @since 1.0.0.0
     */
    public int getRefreshRate()
    {
        return this.refreshRate;
    }

    /**
     * Sets the refresh rate property for this model if it is not spinning. The
     * units are Herz. If the model is spinning, an {@code
     * IllegalStateException} is thrown. For a version of this method that does
     * not throw an exception, see {@link #setRefreshRate(int, boolean)}.
     * This method fires a property change event to listeners. The name of the
     * refresh property is given by the {@link #REFRESH_PROPERTY} constant of
     * this class. {@link #setRefreshTime(long)} changes the same property.
     *
     * @param refreshRate the new refresh rate to set. The actual refresh time
     * will be set to the closest number of milliseconds per frame to this rate.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getRefreshRate()
     * @since 1.0.0.0
     */
    public void setRefreshRate(int refreshRate) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setRefreshRateInternal(refreshRate))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the refresh rate property for this model if it is not spinning. The
     * units are Herz. If the model is spinning, this method will either return
     * immediately or block until the end of the spin, depending on the second
     * argument. For a version of this method that throws an exception if the
     * model is spinning, see {@link #setRefreshRate(int)}. This method fires a
     * property change event to listeners. The name of the refresh property is
     * given by the {@link #REFRESH_PROPERTY} constant of this class. {@link
     * #setRefreshTime(long, boolean)} changes the same property.
     *
     * @param refreshRate the new refresh rate to set. The actual refresh time
     * will be set to the closest number of milliseconds per frame to this rate.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getRefreshRate()
     * @since 1.0.0.0
     */
    public void setRefreshRate(int refreshRate, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setRefreshRateInternal(refreshRate);
        }
    }

    /**
     * Sets the refresh rate property for this model if it is not spinning. The
     * units are Herz. If the model is spinning, this method will return {@code
     * false}. This method provides the underlying functionality for the public
     * {@code setRefreshRate()} methods. This method fires a property change
     * event to listeners. The name of the refresh property is given by the
     * {@link #REFRESH_PROPERTY} constant of this class. {@link
     * #setRefreshTimeInternal(long)} changes the same property. Invocations of
     * this method must be performed within a block synchronized on {@link
     * #spinLock}.
     *
     * @param refreshRate the new refresh rate to set. The actual refresh time
     * will be set to the closest number of milliseconds per frame to this rate.
     * @return {@code true} if the refresh rate was set, {@code false}
     * otherwise. This is exactly the inverse of the spinning flag when this
     * method is called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @see #getRefreshRate()
     * @since 1.0.0.0
     */
    private boolean setRefreshRateInternal(int refreshRate)
    {
        if(this.spinning)
            return false;
        int oldRate = this.refreshRate;
        this.refreshRate = refreshRate;
        this.refreshTime = Math.round(1000.0 / refreshRate);
        fireSyncedPropertyChange(REFRESH_PROPERTY, oldRate, this.refreshRate);
        return true;
    }

    /**
     * Returns the actual refresh time of the model, in milliseconds. The
     * refresh time is the time between successive updates to the state when
     * the model is spinning. The refresh time may be set directly or through
     * the refresh rate property. A state change is fired every time the model
     * is refreshed.
     *
     * @return the number of milliseconds between updates to the state of the
     * model when it is spinning.
     * @since 1.0.0.0
     */
    public long getRefreshTime()
    {
        return this.refreshTime;
    }

    /**
     * Sets the refresh time property for this model if it is not spinning. The
     * units are milliseconds. If the model is spinning, an {@code
     * IllegalStateException} is thrown. For a version of this method that does
     * not throw an exception, see {@link #setRefreshTime(long, boolean)}.
     * This method fires a property change event to listeners, containing the
     * old and new values of the refresh rate, not the time. The name of the
     * refresh property is given by the {@link #REFRESH_PROPERTY} constant of
     * this class. {@link #setRefreshRate(int)} changes the same property.
     *
     * @param refreshTime the new refresh time to set.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getRefreshTime()
     * @since 1.0.0.0
     */
    public void setRefreshTime(long refreshTime) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setRefreshTimeInternal(refreshTime))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the refresh time property for this model if it is not spinning. The
     * units are milliseconds. If the model is spinning, this method will either
     * return immediately or block until the end of the spin, depending on the
     * second argument. For a version of this method that throws an exception if
     * the model is spinning, see {@link #setRefreshTime(long)}. This method
     * fires a property change event to listeners, containing the old and new
     * values of the refresh rate, not the time. The name of the refresh
     * property is given by the {@link #REFRESH_PROPERTY} constant of this
     * class. {@link #setRefreshRate(int, boolean)} changes the same property.
     *
     * @param refreshTime the new refresh time to set.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getRefreshTime()
     * @since 1.0.0.0
     */
    public void setRefreshTime(long refreshTime, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setRefreshTimeInternal(refreshTime);
        }
    }

    /**
     * Sets the refresh time property for this model if it is not spinning. The
     * units are milliseconds. If the model is spinning, this method will return
     * {@code false}. This method provides the underlying functionality for the
     * public {@code setRefreshTime()} methods. This method fires a property
     * change event to listeners, containing the old and new values of the
     * refresh rate, not the time. The name of the refresh property is given by
     * the {@link #REFRESH_PROPERTY} constant of this class. {@link
     * #setRefreshRateInternal(int)} changes the same property. Invocations of
     * this method must be performed within a block synchronized on {@link
     * #spinLock}.
     *
     * @param refreshTime the new refresh time to set.
     * @return {@code true} if the refresh time was set, {@code false}
     * otherwise. This is exactly the inverse of the spinning flag when this
     * method is called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @see #getRefreshTime()
     * @since 1.0.0.0
     */
    private boolean setRefreshTimeInternal(long refreshTime)
    {
        if(this.spinning)
            return false;
        int oldRate = this.refreshRate;
        this.refreshRate = Math.round(1000.0f / refreshTime);
        this.refreshTime = refreshTime;
        fireSyncedPropertyChange(REFRESH_PROPERTY, oldRate, this.refreshRate);
        return true;
    }

    /**
     * Returns the standard deviation of the distribution used to generate
     * random spins, in units of objects. When the {@link #spin()} method is
     * invoked, this property determines the spread of the generated spin
     * increments. Approximately two thirds of all randomly generated spins will
     * rotate through a number of objects between {@code -spinSigma} to {@code
     * +spinSigma}. This property only affects the random generation of spins.
     *
     * @return the standard deviation of the distribution used to generate
     * random spins.
     * @since 1.0.0.0
     */
    public double getSpinSigma()
    {
        return this.spinSigma;
    }

    /**
     * Sets the standard deviation of the distribution used to generate random
     * spins, if the model is not spinning. The units are objects. If the model
     * is spinning, an {@code IllegalStateException} is thrown. For a version of
     * this method that does not throw an exception, see {@link
     * #setSpinSigma(double, boolean)}. This method fires a property change
     * event to listeners. The name of the spin sigma property is given by the
     * {@link #SPIN_SIGMA_PROPERTY} constant of this class.
     *
     * @param spinSigma the new spin sigma to set.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getSpinSigma()
     * @since 1.0.0.0
     */
    public void setSpinSigma(double spinSigma) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setSpinSigmaInternal(spinSigma))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the standard deviation of the distribution used to generate random
     * spins, if the model is not spinning. The units are objects. If the model
     * is spinning, this method will either return immediately or block until
     * the end of the spin, depending on the second argument. For a version of
     * this method that throws an exception if the model is spinning, see {@link
     * #setSpinSigma(double)}. This method fires a property change event to
     * listeners. The name of the spin sigma property is given by the {@link
     * #SPIN_SIGMA_PROPERTY} constant of this class.
     *
     * @param spinSigma the new spin sigma to set.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getSpinSigma()
     * @since 1.0.0.0
     */
    public void setSpinSigma(double spinSigma, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setSpinSigmaInternal(spinSigma);
        }
    }

    /**
     * Sets the standard deviation of the distribution used to generate random
     * spins, if the model is not spinning. The units are objects. If the model
     * is spinning, this method will return {@code false}. This method provides
     * the underlying functionality for the public {@code setSpinSigma()}
     * methods. This method fires a property change event to listeners. The name
     * of the spin sigma property is given by the {@link #SPIN_SIGMA_PROPERTY}
     * constant of this class. Invocations of this method must be performed
     * within a block synchronized on {@link #spinLock}.
     *
     * @param spinSigma the new spin sigma to set.
     * @return {@code true} if the spin sigma was set, {@code false} otherwise.
     * This is exactly the inverse of the spinning flag when this method is
     * called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @see #getSpinSigma()
     * @since 1.0.0.0
     */
    private boolean setSpinSigmaInternal(double spinSigma)
    {
        if(this.spinning)
            return false;
        double oldSigma = this.spinSigma;
        this.spinSigma = spinSigma;
        fireSyncedPropertyChange(SPIN_SIGMA_PROPERTY, oldSigma, this.spinSigma);
        return true;
    }

    /**
     * Returns the spin direction flag. This flag indicates the allowed
     * direction of spin for both the {@link #spin()} and {@link #spin(int)}
     * methods.
     *
     * @return one of the {@link #SPIN_POSITIVE}, {@link #SPIN_NEGATIVE} or
     * {@link #SPIN_BOTH} flags that indicates which way the spinner is allowed
     * to spin.
     * @since 1.0.0.1
     */
    public int getSpinDirection()
    {
        return this.spinDirection;
    }

    /**
     * Sets the flag indicating allowed spin directions, if the model is not
     * spinning. If the model is spinning, an {@code IllegalStateException} is
     * thrown. For a version of this method that does not throw an exception,
     * see {@link #setSpinDirection(int, boolean)}. This method fires a property
     * change event to listeners. The name of the spin direction property is
     * given by the {@link #SPIN_DIRECTION_PROPERTY} constant of this class.
     *
     * @param spinDirection the new spin direction flag to set. This should be
     * one of the constants {@link #SPIN_POSITIVE}, {@link #SPIN_NEGATIVE} or
     * {@link #SPIN_BOTH}.
     * @throws IllegalStateException if the model is spinning when this method
     * is called.
     * @see #getSpinDirection()
     * @since 1.0.0.1
     */
    public void setSpinDirection(int spinDirection) throws IllegalStateException
    {
        synchronized(this.spinLock) {
            if(!setSpinDirectionInternal(spinDirection))
                throw new IllegalStateException("spinning");
        }
    }

    /**
     * Sets the flag indicating allowed spin directions, if the model is not
     * spinning. If the model is spinning, this method will either return
     * immediately or block until the end of the spin, depending on the second
     * argument. For a version of this method that throws an exception if the
     * model is spinning, see {@link #setSpinDirection(int)}. This method fires
     * a property change event to listeners. The name of the spin direction
     * property is given by the {@link #SPIN_DIRECTION_PROPERTY} constant of
     * this class.
     *
     * @param spinDirection the new spin direction flag to set. This should be
     * one of the constants {@link #SPIN_POSITIVE}, {@link #SPIN_NEGATIVE} or
     * {@link #SPIN_BOTH}.
     * @param block if {@code true}, this method will wait for a spin to stop
     * before proceeding. If {@code false}, this method will return immediately
     * without taking any action if the model is spinning. If the model is not
     * spinning, this parameter is ignored.
     * @see #getSpinDirection()
     * @since 1.0.0.1
     */
    public void setSpinDirection(int spinDirection, boolean block)
    {
        synchronized(this.spinLock) {
            checkSpin(block);
            setSpinDirectionInternal(spinDirection);
        }
    }

    /**
     * Sets the flag indicating allowed spin directions, if the model is not
     * spinning. If the model is spinning, this method will return {@code
     * false}. This method provides the underlying functionality for the public
     * {@code setSpinDirection()} methods. This method fires a property change
     * event to listeners. The name of the spin direction property is given by
     * the {@link #SPIN_DIRECTION_PROPERTY} constant of this class. Invocations
     * of this method must be performed within a block synchronized on {@link
     * #spinLock}.
     *
     * @param spinDirection a flag indicating the new spin direction. All
     * positive values will be interpreted as {@link #SPIN_POSITIVE}, all
     * negative values will be interpreted as {@link #SPIN_NEGATIVE}, while zero
     * will be interpreted as {@link #SPIN_BOTH}.
     * @return {@code true} if the spin direction was set, {@code false}
     * otherwise. This is exactly the inverse of the spinning flag when this
     * method is called.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired to notify waiting threads after firing
     * property changes.
     * @see #getSpinDirection()
     * @since 1.0.0.1
     */
    private boolean setSpinDirectionInternal(int spinDirection)
    {
        if(this.spinning)
            return false;
        int oldDirection = this.spinDirection;
        if(spinDirection > 0)
            this.spinDirection = SPIN_POSITIVE;
        else if(spinDirection < 0)
            this.spinDirection = SPIN_NEGATIVE;
        else
            this.spinDirection = SPIN_BOTH;
        fireSyncedPropertyChange(SPIN_DIRECTION_PROPERTY, oldDirection, this.spinDirection);
        return true;
    }

    /**
     * Returns an informative string representation of this model. The resulting
     * string will contain information about all of the object's properties.
     * This method is useful for debugging and should not be used as a formal
     * description of this object since the contents of the string may change at
     * any time.
     *
     * @return a {@code String} representation of this object.
     * @since 1.0.0.0
     */
    @Override public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [position=").append(this.position);
        if(this.spinning)
            sb.append(" (spinning)");
        else
            sb.append(" (not spinning)");
        sb.append(", spinRate=").append(this.spinRate);
        sb.append(", acceleration=").append(this.acceleration);
        sb.append(", refreshRate=").append(this.refreshRate);
        sb.append(", refreshTime=").append(this.refreshTime);
        sb.append(", spinSigma=").append(this.spinSigma);
        sb.append(", spinDirection=");
        switch(this.spinDirection) {
            case SPIN_POSITIVE:
                sb.append("SPIN_POSITIVE");
                break;
            case SPIN_NEGATIVE:
                sb.append("SPIN_NEGATIVE");
                break;
            case SPIN_BOTH:
                sb.append("SPIN_BOTH");
                break;
            default:
                sb.append("<unknown>");
                break;
        }
        sb.append(", dataSize=").append(this.dataSize);
        sb.append(", data=").append(this.data.toString());
        sb.append(']');
        return sb.toString();
    }

    /**
     * Normalizes a position to the size of the list. All values greater than
     * the size of the list or less than zero will be "wrapped" to the
     * corresponding position within the list.
     *
     * @param position a position, possibly outside the list.
     * @return a position in [{@code 0}, {@code data.size() - 1}).
     * @since 1.0.0.0
     */
    protected double normalize(double position)
    {
        position %= this.dataSize;
        if(position < 0)
            position += this.dataSize;
        return position;
    }

    /**
     * Normalizes an index to the size of the list. All values greater than the
     * size of the list or less than zero will be "wrapped" to the corresponding
     * index within the list.
     *
     * @param index an index, possibly outside the list.
     * @return an index in [{@code 0}, {@code data.size() - 1}).
     * @since 1.0.0.0
     */
    protected int normalize(int index)
    {
        index %= this.data.size();
        if(index < 0)
            index += this.data.size();
        return index;
    }

    /**
     * Fires a property change and notifies the next waiting thread that the
     * intrinsic lock of {@link #spinLock} is available. This method must be
     * invoked in a block synchronized on {@link #spinLock}.
     *
     * @param propertyName the name of the property that was changed.
     * @param oldValue the old value of the property.
     * @param newValue the new value of the property.
     * @throws IllegalMonitorStateException if the intrinsic lock of {@link
     * #spinLock} could not be acquired.
     * @since 1.0.0.0
     */
    private void fireSyncedPropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        firePropertyChange(propertyName, oldValue, newValue);
        spinLock.notify();
    }

    /**
     * Checks if this model is spinning, optionally waiting for it to stop. This
     * method must be invoked in a block synchronized on {@link #spinLock} if
     * {@code block} is {@code true}.
     *
     * @param block whether or not to block the thread until the current spin
     * finishes. If this parameter is {@code false}, this method will return
     * immediately no matter what the state of the spinner is.
     * @throws IllegalMonitorStateException if {@code block} is {@code true},
     * the spinner is spinning and the intrinsic lock of {@link #spinLock} can
     * not be acquired.
     * @since 1.0.0.0
     */
    private void checkSpin(boolean block)
    {
        while(this.spinning && block) {
            try {
                this.spinLock.wait();
            } catch(InterruptedException ie) { /* Ignore the exception */ }
        }
    }

    /**
     * Animates the spinner model in real time. When the model's {@link #spin()}
     * or {@link #spin(int)} method is invoked, this task is run on the
     * {@linkplain #TIMER internal timer} at fixed intervals until the spin
     * ends. The {@code position} field is updated as each object rotates
     * through the display. The task shuts itself down and sets the {@code
     * spinning} flag to {@code false} when the desired final position has been
     * reached and the spin is completed. It is important to note that this task
     * updates the position based only on the time, not on the current position.
     * This has the added advantage of allowing the posision to be normalized to
     * the size of the data list without throwing off the calculation.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0.0, 13 Feb 2013
     * @since 1.0.0.0
     */
    private class SpinTask extends TimerTask implements Serializable
    {
        /**
         * The version ID for serialization.
         *
         * @serial Increment the least significant three digits when
         * compatibility is not compromised by a structural change (e.g. adding
         * a new field with a sensible default value), and the upper digits when
         * the change makes serialized versions of of the class incompatible
         * with previous releases.
         * @since 1.0.0.0
         */
        private static final long serialVersionUID = 1000L;

        /**
         * A flag indicating whether or not this task has been run at least
         * once. If this flag is {@code true}, {@code t0} will be a valid value.
         * Otherwise, the task has been created but not yet been initialized.
         *
         * @serial
         * @since 1.0.0.0
         */
        private boolean started;

        /**
         * The starting time of this task, in milliseconds. This value will be
         * subtracted off of the current time during task execution. It is set
         * by the first iteration of the task. This value should only be
         * considered valid if the {@link #started} flag is {@code true}.
         *
         * @serial
         * @since 1.0.0.0
         */
        private double t0;

        /**
         * The time of the first inflection point in the position, relative to
         * {@link #t0}, in milliseconds. This is also the time at which the spin
         * rate reaches its maximum value and the acceleration stops. If the
         * entire spin is short enough that the spinner never reaches its
         * maximum configured speed, this time will be equal to {@link #tB}, and
         * the acceleration will switch sign immediately instead of first going
         * to zero for a cruising stretch. This value is precomputed as soon as
         * the final position is known.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double tA;

        /**
         * The time of the second inflection point in the position, relative to
         * {@link #t0}, in milliseconds. This is also the time at which the
         * deceleration starts and the spin rate begins to decline. If the
         * entire spin is short enough that the spinner never reaches its
         * maximum configured speed, this time will be equal to {@link #tA}.
         * This value is precomputed as soon as the final position is known.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double tB;

        /**
         * The time of the end of the spin, relative to {@link #t0}, in
         * milliseconds. The spin is finished when this much time has elapsed,
         * the position of the spinner will have have been incremented by
         * exactly the desired amount, and the task will cancel itself. This
         * value is precomputed as soon as the final position is known.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double tF;

        /**
         * The initial position of the spinner at time {@code #t0}, in units of
         * objects. This is exactly {@link #position} at the beginning of the
         * spin.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double x0;

        /**
         * The position of the first inflection point, occuring at time {@link
         * #tA}, in units of objects. This is where the spin rate reaches its
         * maximum value and the acceleration stops. If the entire spin is so
         * short that the spinner never reaches its maximum configured spin
         * rate, this position will be equal to {@link #xB}, and the
         * acceleration will switch sign immediately instead of first going to
         * zero for a cruising stretch. This value is precomputed as soon as the
         * final position is known.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double xA;

        /**
         * The position of the second inflection point, occuring at time {@link
         * #tB}, in units of objects. This is where the spin rate begins to
         * decrease and the deceleration starts. If the entire spin is so short
         * that the spinner never reaches its maximum configured spin rate, this
         * position will be equal to {@link #xA}. This value is precomputed as
         * soon as the final position is known.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double xB;

        /**
         * The final position of the spinner when it comes to a stop, in units
         * of objects. This number is an integer indicating the index of the
         * object at the end of the spin. The index is just {@link #x0} offset
         * by the desired increment.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double xF;

        /**
         * The actual maximum spin rate that this model will attain, in units of
         * objects per millisecond. Generally, this will be a conversion of the
         * value of {@link #spinRate} into the appropriate units. However, if
         * either the acceleration or the desired spin increment are small
         * enough, the actual maximum spin rate that will be attained will be
         * smaller than the preconfigured value. In that case, the acceleration
         * will switch sign immediately in the middle of the spin rather than
         * turning off for a time to allow the spinner to cruise. This value
         * includes both a direction and a magnitude.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double vMax;

        /**
         * The absolute value of the acceleration of the spin, in units of
         * objects per millisecond per millisecond. This is a conversion of the
         * value of {@link #acceleration}. The spinner accelerates with this
         * value and decelerates with its inverse. A constant acceleration
         * allows us to apply the equations of kinematics to the speed-up,
         * cruise, and slow-down phases of the spin. This value includes both a
         * direction and a magnitude.
         *
         * @serial
         * @since 1.0.0.0
         */
        private final double aMax;

        /**
         * Constructs a new task that will move the spinner's position by the
         * desired increment. The positions and times of every discontinuity of
         * the spin are computed so that the task can update the position with
         * minimal computation later. The acceleration is always fixed. The
         * maximum allowed velocity may or may not be attained during the spin,
         * depending on the size of the increment and the acceleration. If the
         * top speed is not the maximum allowed speed, there will not be a
         * cruising phase where the spinner spins at a constant rate.
         *
         * @param increment the number of list elements that this spin should
         * move by.
         * @since 1.0.0.0
         */
        public SpinTask(int increment)
        {
            /*
             * Compute the positions using kinematics in units of objects.
             * Conversion from objects to pixels will be done by whoever wants
             * to draw the model.
             */
            double direction = Math.signum(increment);

            // Initial position
            this.x0 = CasinoSpinnerModel.this.position;
            this.xF = x0 + increment;

            // acceleration
            this.aMax = direction * 1e-6 * CasinoSpinnerModel.this.acceleration;

            // compute the displacement to the beginning of the cruise phase
            double dx = direction * 0.5 * CasinoSpinnerModel.this.spinRate
                                        * CasinoSpinnerModel.this.spinRate
                                        / CasinoSpinnerModel.this.acceleration;
            // Position where max velocity is reached and acceleration cuts off
            double xA = x0 + dx;
            // Position where negative acceleration cuts back in
            double xB = xF - dx;

            /*
             * The next section does the following:
             * 1) Computes time of each transition in units of milliseconds
             *    based on positions.
             * 2) Computes maximum spin rate that will actually be acheived in
             *    units of objects per millisecond.
             */

            /*
             * if the xA->xB 1D vector points in the opposite direction from the
             * displacement, then there is no cruising phase because the
             * traversal will be shorter than required to attain max velocity.
             */
            if(Math.signum(xB - xA) != direction) {
                this.xA = 0.5 * (x0 + xF);
                this.xB = this.xA;
                this.vMax = direction * Math.sqrt(this.aMax * (double)increment);
            } else { // normal traversal: constant velocity in the middle
                this.xA = xA;
                this.xB = xB;
                this.vMax = direction * CasinoSpinnerModel.this.spinRate / 1000.0;
            }

            this.tA = this.vMax / this.aMax;
            this.tB = this.tA + (this.xB - this.xA) / this.vMax;
            this.tF = this.tA + this.tB;

            this.started = false;
        }

        /**
         * Updates the current position based on the offset of the current time
         * from the start of the task. During the first iteration, the start
         * time is recorded. If the end time has been exceeded or reached, the
         * position is set to its final integer value, the {@link #spinning}
         * flag is turned off and this task is canceled. During normal
         * operation, the position is updated according to whether the spinner
         * is accelerating, cruising, or decelerating. The position is always
         * normalized to a value between zero (inclusive) and the number of
         * objects (exclusive).
         *
         * @since 1.0.0.0
         */
        @Override public void run()
        {
            double currentTime = (double)System.currentTimeMillis();

            // do not change position on first iteration
            if(!this.started) {
                this.t0 = currentTime;
                this.started = true;
                return;
            }

            currentTime -= this.t0;

            if(currentTime >= this.tF) {                                        // The spin has ended
                // truncate to a number b/w 0 and the size of the list
                CasinoSpinnerModel.this.position = normalize(xF);
                synchronized(CasinoSpinnerModel.this.spinLock) {
                    // the next spin and property changes can begin immediately after the current position is updated
                    CasinoSpinnerModel.this.spinning = false;
                    // let the first waiting thread know it can acquire the lock
                    CasinoSpinnerModel.this.spinLock.notify();
                }
                // the thread can take its time dying because it will not interfere with anything now
                cancel();
            } else if(currentTime <= this.tA) {                                 // The spinner is speeding up
                CasinoSpinnerModel.this.position = normalize(this.x0 + 0.5 * currentTime * currentTime * this.aMax);
            } else if(currentTime >= this.tB) {                                 // The spinner is slowing down
                double deltaTime = currentTime - this.tB;
                CasinoSpinnerModel.this.position = normalize(this.xB + this.vMax * deltaTime - 0.5 * this.aMax * deltaTime * deltaTime);
            } else {                                                            // The spinner is coasting
                double deltaTime = currentTime - this.tA;
                CasinoSpinnerModel.this.position = normalize(this.xA + this.vMax * deltaTime);
            }

            // notify listeners that the model has been updated
            fireStateChange();
        }

        /**
         * Returns an informative string representation of this model. The
         * resulting string will contain information about all of the task's
         * properties. This method is useful for debugging and should not be
         * used as a formal description of this object since the contents of the
         * string may change at any time.
         *
         * @return a {@code String} representation of this object.
         * @since 1.0.0.0
         */
        @Override public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" [");
            if(this.started) {
                sb.append("t0=").append(this.t0);
                sb.append("(started)");
            } else {
                sb.append("t0=undefined (waiting)");
            }
            sb.append(", tA=").append(this.tA);
            sb.append(", tB=").append(this.tB);
            sb.append(", tF=").append(this.tF);
            sb.append(", x0=").append(this.x0);
            sb.append(", xA=").append(this.xA);
            sb.append(", xB=").append(this.xB);
            sb.append(", xF=").append(this.xF);
            sb.append(", vMax=").append(this.vMax);
            sb.append(", aMax=").append(this.aMax);
            sb.append("]");
            return sb.toString();
        }
    }
}
