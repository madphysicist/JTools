/*
 * SpinnerWrapListModel.java (Class: com.madphysicist.tools.swing.SpinnerWrapListModel)
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

import java.util.List;
import javax.swing.SpinnerListModel;

/**
 * Extends the swing {@code SpinnerListModel} class to allow wrapping of values
 * around the ends of the model list.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 11 Feb 2013
 * @since 1.0.0.0
 */
public class SpinnerWrapListModel extends SpinnerListModel
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
     * Constructs an empty {@code SpinnerWrapListModel}. Delegates to the
     * parent's {@linkplain SpinnerListModel#SpinnerListModel() empty
     * constructor}.
     *
     * @see SpinnerListModel#SpinnerListModel()
     * @since 1.0.0.0
     */
    public SpinnerWrapListModel()
    {
        super();
    }

    /**
     * Constructs a {@code SpinnerWrapListModel} based on the specified values.
     * All initialization is delegated to the parent's {@linkplain
     * SpinnerListModel#SpinnerListModel(java.lang.Object[]) corresponding
     * constructor}.
     *
     * @param values the values on which this model is initialized.
     * @see SpinnerListModel#SpinnerListModel(Object[])
     * @since 1.0.0.0
     */
    public SpinnerWrapListModel(Object[] values)
    {
        super(values);
    }

    /**
     * Constructs a {@code SpinnerWrapListModel} based on the specified values.
     * All initialization is delegated to the parent's {@linkplain
     * SpinnerListModel#SpinnerListModel(java.util.List) corresponding
     * constructor}.
     *
     * @param values the values on which this model is initialized.
     * @see SpinnerListModel#SpinnerListModel(List)
     * @since 1.0.0.0
     */
    public SpinnerWrapListModel(List<?> values)
    {
        super(values);
    }

    /**
     * Returns the next legal value in the underlying sequence. If the current
     * value is already the last element, returns the first value in the
     * sequence. Returns {@code null} if the sequence is empty.
     *
     * @return the next legal value of the underlying sequence, or the first
     * element of the sequence if the current value is already the last element.
     * @since 1.0.0.0
     */
    @Override public Object getNextValue()
    {
        List<?> list = getList();

        Object value = super.getNextValue();
        if(value == null && !list.isEmpty())
        {
            value = list.get(0);
        }
        return value;
    }

    /**
     * Returns the previous legal value in the underlying sequence. If the
     * current value is already the first element, returns the last value in the
     * sequence. Returns {@code null} if the sequence is empty.
     *
     * @return the previous legal value of the underlying sequence, or the last
     * element of the sequence if the current value is already the first
     * element.
     * @since 1.0.0.0
     */
    @Override public Object getPreviousValue()
    {
        List<?> list = getList();

        Object value = super.getPreviousValue();
        if(value == null && !list.isEmpty())
        {
            value = list.get(list.size() - 1);
        }
        return value;
    }
}
