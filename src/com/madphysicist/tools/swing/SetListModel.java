/*
 * SetListModel.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.MutableComboBoxModel;

/**
 * Provides a model for {@link JList}s and {@link JComboBox}es that sorts items
 * and only allows one instance of each item. This model is mutable. It does not
 * allow {@code null} items in the list.
 *
 * @param <E> The type of data stored in the list or combo box.
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0 18 Nov 2013 - J. Fox-Rabinovitz - Created
 * @since 1.0.0
 */
public class SetListModel<E extends Comparable<? super E>>
             extends AbstractListModel<E>
             implements MutableComboBoxModel<E>, Serializable
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
     * The data of the combo box or list. Data is maintained in a sorted list
     * rather than a set because Java {@link SortedSet}s do not provide
     * index-based access. This field is never {@code null} after the
     * constructor completes successfully, although it may be empty.
     *
     * @serial
     * @since 1.0.0
     */
    private List<E> data;

    /**
     * Maintains the currently selected index for the methods of the {@link
     * ComboBoxModel} interface.
     *
     * @serial
     * @since 1.0.0
     */
    private int selectedIndex;

    /**
     * Creates an empty model. Elements may be added later.
     *
     * @since 1.0.0
     */
    public SetListModel()
    {
        this((Collection<E>)null);
    }

    /**
     * Creates a model with the specified list of elements.
     *
     * @param data the elements to initialize the model with.
     * @since 1.0.0
     */
    public SetListModel(E[] data)
    {
        this(Arrays.asList(data));
    }

    /**
     * Creates a model with the specified collection of elements.
     *
     * @param data the elements to initialize the model with. May be {@code
     * null}, but may not contain {@code null} elements.
     * @throws NullPointerException if any of the data elements are {@code
     * null}. Note that the collection itself may be {@code null}.
     * @since 1.0.0
     */
    public SetListModel(Collection<? extends E> data)
    {
        this.data = new ArrayList<>();

        if(data == null || data.isEmpty()) {
            this.selectedIndex = -1;
        } else {
            for(E item : data) {
                if(item == null) {
                    throw new NullPointerException("Null data");
                }
            }
            this.data.addAll(data);
            Collections.sort(this.data);
            this.selectedIndex = 0;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 1.0.0
     */
    @Override public int getSize()
    {
        return data.size();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}. Returns {@code null} if the specified zero-based
     * index is not within the bounds of the data.
     * @since 1.0.0
     */
    @Override public E getElementAt(int index)
    {
        if(index < 0 || index >= data.size()) {
            return null;
        }
        return data.get(index);
    }

    /**
     * Finds the location of the specified element with the dataset.
     *
     * @param obj the element to find.
     * @return the zero-based index of the element within the data, or {@code
     * -1} if the element could not be found.
     * @throws ClassCastException if obj is not {@code Comparable} and
     * compatible with the type of the model.
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public int getIndexOf(Object obj) throws ClassCastException
    {
        if(obj != null && obj instanceof Comparable) {
            int index = Collections.binarySearch(data, (E)obj);
            if(index >= 0) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Places the specified element within the dataset. Since the dataset is
     * always sorted, this method is the only way to put data into the dataset.
     * There is no way to insert data into a specific location. This method does
     * not allow duplicate elements to be inserted.
     *
     * @param element the element to add.
     * @return {@code true} if the element was added to the dataset
     * successfully, {@code false} if it was already contained.
     * @since 1.0.0
     */
    public boolean putElement(E element)
    {
        int index = Collections.binarySearch(data, element);
        if(index >= 0) {
            return false;
        }
        index = -(index + 1);
        data.add(index, element);
        fireIntervalAdded(this, index, index);
        return true;
    }

    /**
     * {@inheritDoc}. In actuality, this method is an alias for {@link
     * #putElement(Comparable)}. The item is not added to the end, but rather
     * into whatever location is specified by the sorting. The element is not
     * added at all if it is a duplicate.
     *
     * @param element {@inheritDoc}
     * @since 1.0.0
     */
    @Override public void addElement(E element)
    {
        putElement(element);
    }

    /**
     * {@inheritDoc}. In actuality, this method is an alias for {@link
     * #putElement(Comparable)}. The item is not inserted at the specified
     * index, but rather into whatever location is specified by the sorting. The
     * element is not added at all if it is a duplicate.
     *
     * @param element {@inheritDoc}
     * @param index {@inheritDoc}. This parameter is ignored.
     * @since 1.0.0
     */
    @Override public void insertElementAt(E element, int index)
    {
        putElement(element);
    }

    /**
     * {@inheritDoc}.
     *
     * @param obj {@inheritDoc}
     * @throws ClassCastException if obj is not {@code Comparable} and
     * compatible with the type of the model.
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override public void removeElement(Object obj) throws ClassCastException
    {
        if(obj instanceof Comparable) {
            int index = Collections.binarySearch(data, (E)obj);
            if(index >= 0) {
                data.remove(index);
                fireIntervalRemoved(this, index, index);
            }
        }
    }

    /**
     * {@inheritDoc}.
     *
     * @param index {@inheritDoc}
     * @since 1.0.0
     */
    @Override public void removeElementAt(int index)
    {
        data.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    /**
     * Clears all of the elements in this model.
     *
     * @since 1.0.0
     */
    public void removeAllElements()
    {
        if(!data.isEmpty()) {
            int end = data.size() - 1;
            data.clear();
            fireIntervalRemoved(this, 0, end);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param obj {@inheritDoc}
     * @since 1.0.0
     */
    @Override public void setSelectedItem(Object obj)
    {
        this.selectedIndex = getIndexOf(obj);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override public Object getSelectedItem()
    {
        if(selectedIndex >= 0 && selectedIndex < data.size()) {
            return data.get(selectedIndex);
        }
        return null;
    }

    /**
     * Checks if this model's dataset contains the specified element.
     *
     * @param element the element to check for.
     * @return {@code true} if the requested element is contained in the
     * dataset, {@code false} otherwise.
     * @since 1.0.0
     */
    public boolean contains(E element)
    {
        if(element != null) {
            int index = Collections.binarySearch(data, element);
            return index >= 0;
        }
        return false;
    }

    /**
     * Checks if this model's dataset is empty or not.
     *
     * @return {@code true} if the dataset is empty, {@code false} if it has a
     * non-zero number of elements.
     * @since 1.0.0
     */
    public boolean isEmpty()
    {
        return data.isEmpty();
    }
}
