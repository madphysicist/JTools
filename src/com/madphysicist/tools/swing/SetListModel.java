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
import javax.swing.MutableComboBoxModel;

/**
 * Provides a model for {@link JList}s and {@code JComboBox}es that sorts items
 * and only allows one instance of each item. This model does not allow {@code
 * null} items in the list.
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
     * index-based access.
     *
     * @serial
     * @since 1.0.0
     */
    private List<E> data;

    private int selectedIndex;

    public SetListModel()
    {
        this((Collection)null);
    }

    public SetListModel(E[] data) throws NullPointerException
    {
        this(Arrays.asList(data));
    }

    public SetListModel(Collection<? extends E> data) throws NullPointerException
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

    @Override public int getSize()
    {
        return data.size();
    }

    @Override public E getElementAt(int index)
    {
        if(index < 0 || index >= data.size()) {
            return null;
        }
        return data.get(index);
    }

    public int getIndexOf(E element)
    {
        if(element != null) {
            int index = Collections.binarySearch(data, element);
            if(index >= 0) {
                return index;
            }
        }
        return -1;
    }

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

    @Override public void addElement(E element)
    {
        putElement(element);
    }

    @Override public void insertElementAt(E element, int index)
    {
        putElement(element);
    }

    @Override public void removeElement(Object obj)
    {
        int index = Collections.binarySearch(data, (E)obj);
        if(index >= 0) {
            data.remove(index);
            fireIntervalRemoved(this, index, index);
        }
    }

    @Override public void removeElementAt(int index)
    {
        data.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public void removeAllElements()
    {
        if(!data.isEmpty()) {
            int end = data.size() - 1;
            data.clear();
            fireIntervalRemoved(this, 0, end);
        }
    }

    @Override public void setSelectedItem(Object obj) throws ClassCastException
    {
        this.selectedIndex = getIndexOf((E)obj);
    }

    @Override public Object getSelectedItem()
    {
        if(selectedIndex >= 0 && selectedIndex < data.size()) {
            return data.get(selectedIndex);
        }
        return null;
    }

    public boolean contains(E element)
    {
        if(element != null) {
            int index = Collections.binarySearch(data, element);
            return index >= 0;
        }
        return false;
    }

    public boolean isEmpty()
    {
        return data.isEmpty();
    }
}
