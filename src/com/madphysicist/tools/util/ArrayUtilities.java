/*
 * ArrayUtilities.java
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

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This class is a utility library for performing operations on arrays. It
 * contains utilities for sorting arrays with {@code null} elements, etc.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 20 Sep 2012 - J. Fox-Rabinovitz: Created
 * @version 1.0.1, 05 Feb 2014 - J. Fox-Rabinovitz: Added truncate() methods
 * @since 1.0.0
 */
public class ArrayUtilities
{
    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private ArrayUtilities() {}

    /**
     * Returns a subset of an array, starting from the beginning. The input
     * array is not actually modified in any way. The return value is always a
     * copy, even if the requested subset is the entire array.
     *
     * @param <T> The type of the input array.
     * @param array the array to truncate.
     * @param length the length of the subset. The length may not be larger than
     * the length of the input array.
     * @return a new array of the specified length with the same component type
     * as the input, containing the specified subset of elements.
     * @throws IndexOutOfBoundsException if {@code length} does not meet the
     * restrictions imposed by the length of {@code array}.
     * @since 1.0.1
     */
    public static <T> T[] truncate(T[] array, int length)
    {
        return truncate(array, 0, length);
    }

    /**
     * Returns a subset of an array. The input array is not actually modified
     * in any way. The return value is always a copy, even if the requested
     * subset is the entire array.
     *
     * @param <T> The type of the input array.
     * @param array the array to truncate.
     * @param start the index to copy from within {@code array}. This number
     * must be between {@code 0} and {@code array.length - 1}.
     * @param length the length of the subset. {@code start + length} may not be
     * larger than the length of the input array.
     * @return a new array of the specified length with the same component type
     * as the input, containing the specified subset of elements.
     * @throws IndexOutOfBoundsException if {@code start} or {@code length} do
     * not meet the restrictions imposed by the length of {@code array}.
     * @since 1.0.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] truncate(T[] array, int start, int length)
    {
        T[] copy = (T[])Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, start, copy, 0, length);
        return copy;
    }

    /**
     * Sorts an array with {@code null} elements according to the natural order
     * of the elements. {@code null} elements can be placed at the beginning or
     * the end of the sorted array.
     * 
     * @param <T> The type of objects to be sorted. All objects must be mutually
     * comparable.
     * @param array the array to be sorted.
     * @param nullsFirst if {@code true}, all {@code null} elements will be
     * grouped at the beginning of the array. Otherwise, they will be grouped at
     * the end.
     * @return the sorted array, or {@code null} if {@code array} is {@code
     * null}.
     * @since 1.0.0
     * @see java.util.Arrays#sort(Object[]) Arrays.sort()
     */
    public static <T> T[] sort(T[] array, boolean nullsFirst)
    {
        if(array == null)
            return null;
        int nullPos;
        if(nullsFirst) {
            nullPos = 0;
            // swap nulls to the beginning of the array
            for(int i = 0; i < array.length; i++) {
                if(array[i] == null) {
                    // swap with first non-null element
                    if(i != nullPos) {
                        array[i] = array[nullPos];
                        array[nullPos] = null;
                    }
                    nullPos++;
                }
            }
            Arrays.sort(array, nullPos, array.length);
        } else {
            nullPos = array.length;
            // swap nulls to the end of the array
            for(int i = array.length - 1; i >= 0; i--) {
                if(array[i] == null) {
                    nullPos--;
                    // swap with last non-null element
                    if(i != nullPos) {
                        array[i] = array[nullPos];
                        array[nullPos] = null;
                    }
                }
            }
            Arrays.sort(array, 0, nullPos);            
        }
        return array;
    }
}
