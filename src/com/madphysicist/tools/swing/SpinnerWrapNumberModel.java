/*
 * SpinnerWrapNumberModel.java (Class: com.madphysicist.tools.swing.SpinnerWrapNumberModel)
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

import javax.swing.SpinnerNumberModel;

/**
 * @brief Extends the swing {@code SpinnerNumberModel} class to allow wrapping
 * of values around the ends of the list.
 *
 * This is handy for things like angles, which can be defined in the domain
 * `[0, 360)` or `(-180, 180]` and wrap around cyclically.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 13 Mar 2015 - J. Fox-Rabinovitz - Initial coding.
 * @since 1.3
 */
public class SpinnerWrapNumberModel extends SpinnerNumberModel
{
    /**
     * @brief The version ID for serialization.
     *
     * @since 1.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * @brief Constructs an instance with no bounds, a step size of one and an
     * initial value of zero.
     *
     * Delegates to the parent's empty constructor.
     *
     * @since 1.0.0
     */
    public SpinnerWrapNumberModel()
    {
        super();
    }

    /**
     * @brief Constructs an instance with the specified initial value, limits
     * and step size.
     *
     * All initialization is delegated to the parent's corresponding
     * constructor.
     *
     * @param aValue The initial value.
     * @param aMinimum The lower bound of model values.
     * @param aMaximum The upper bound of model values.
     * @param aStep The step size between successive values.
     * @throws IllegalArgumentException if the upper and lower bounds are not in
     * the correct order or if the value is not between them. This is the same
     * condition that causes an exception in the corresponding
     * super-constructor.
     * @since 1.0.0
     */
    public SpinnerWrapNumberModel(double aValue, double aMinimum, double aMaximum, double aStep)
    {
        super(aValue, aMinimum, aMaximum, aStep);
    }

    /**
     * @brief Constructs an instance with the specified initial value, limits
     * and step size.
     *
     * All initialization is delegated to the parent's corresponding
     * constructor.
     *
     * @param aValue The initial value.
     * @param aMinimum The lower bound of model values.
     * @param aMaximum The upper bound of model values.
     * @param aStep The step size between successive values.
     * @throws IllegalArgumentException if the upper and lower bounds are not in
     * the correct order or if the value is not between them. This is the same
     * condition that causes an exception in the corresponding
     * super-constructor.
     * @since 1.0.0
     */
    public SpinnerWrapNumberModel(int aValue, int aMinimum, int aMaximum, int aStep)
    {
        super(aValue, aMinimum, aMaximum, aStep);
    }

    /**
     * @brief Constructs an instance with the specified initial value, limits
     * and step size.
     *
     * All initialization is delegated to the parent's corresponding
     * constructor.
     *
     * @param aValue The initial value.
     * @param aMinimum The lower bound of model values. May be `null` to
     * indicate no bound.
     * @param aMaximum The upper bound of model values. May be `null` to
     * indicate no bound.
     * @param aStep The step size between successive values.
     * @throws IllegalArgumentException if the initial value or step size are
     * `null`, or the upper and lower bounds are not in the correct order, or
     * if the value is not between them. This is the same condition that causes
     * an exception in the corresponding super-constructor.
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public SpinnerWrapNumberModel(Number aValue, Comparable aMinimum, Comparable aMaximum, Number aStep)
    {
        super(aValue, aMinimum, aMaximum, aStep);
    }

    /**
     * @brief Returns the next number in the sequence.
     *
     * Normally, the next value is the current value plus the step size. If the
     * value would exceed the maximum, returns the minimum. Note that the
     * minimum may be `null`, to indicate that there is no lower bound.
     *
     * @return The next number in the sequence, or the minimum bound if the next
     * value would exceed the maximum.
     * @since 1.0.0
     */
    @Override public Object getNextValue()
    {
        Object value = super.getNextValue();
        if(value == null) value = getMinimum();
        return value;
    }

    /**
     * @brief Returns the previous number in the sequence.
     *
     * Normally, the previous value is the current value minus the step size. If
     * the value would fall below the minimum, returns the maximum. Note that
     * the maximum may be `null`, to indicate that there is no upper bound.
     *
     * @return The previous number in the sequence, or the maximum bound if the
     * previous value would fall below the minimum.
     * @since 1.0.0
     */
    @Override public Object getPreviousValue()
    {
        Object value = super.getPreviousValue();
        if(value == null) value = getMaximum();
        return value;
    }
}
