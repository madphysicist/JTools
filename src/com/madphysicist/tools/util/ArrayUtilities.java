/*
 * ArrayUtilities.java (Class: com.madphysicist.tools.util.ArrayUtilities)
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
 * @version 1.0.0, 20 Sep 2012 - J. Fox-Rabinovitz: Initial coding.
 * @version 1.0.1, 05 Feb 2014 - J. Fox-Rabinovitz: Added `truncate()` methods.
 * @version 1.0.2, 17 Feb 2015 - J. Fox-Rabinovitz: Added `toStringArray()` and
 *      `to<Primitive>Array()` methods.
 * @since 1.0
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
     * @brief Converts an array of bytes into an array of strings representing
     * the numerical values of corresponding elements.
     *
     * The conversion is done in base ten.
     *
     * @param anArray The array of bytes to convert.
     * @return An array of strings of the same length as the input, each one
     * containing a string representation of each element of the input.
     * @since 1.0.2
     */
    public static String[] toStringArray(byte[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of shorts into an array of strings representing
     * the numerical values of corresponding elements.
     *
     * The conversion is done in base ten.
     *
     * @param anArray The array of shorts to convert.
     * @return An array of strings of the same length as the input, each one
     * containing a string representation of each element of the input.
     * @since 1.0.2
     */
    public static String[] toStringArray(short[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of ints into an array of strings representing
     * the numerical values of corresponding elements.
     *
     * The conversion is done in base ten.
     *
     * @param anArray The array of ints to convert.
     * @return An array of strings of the same length as the input, each one
     * containing a string representation of each element of the input.
     * @since 1.0.2
     */
    public static String[] toStringArray(int[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of longs into an array of strings representing
     * the numerical values of corresponding elements.
     *
     * The conversion is done in base ten.
     *
     * @param anArray The array of longs to convert.
     * @return An array of strings of the same length as the input, each one
     * containing a string representation of each element of the input.
     * @since 1.0.2
     */
    public static String[] toStringArray(long[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of floats into an array of strings representing
     * the numerical values of corresponding elements.
     *
     * The conversion is done in base ten.
     *
     * @param anArray The array of floats to convert.
     * @return An array of strings of the same length as the input, each one
     * containing a string representation of each element of the input.
     * @since 1.0.2
     */
    public static String[] toStringArray(float[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of doubles into an array of strings representing
     * the numerical values of corresponding elements.
     *
     * The conversion is done in base ten.
     *
     * @param anArray The array of doubles to convert.
     * @return An array of strings of the same length as the input, each one
     * containing a string representation of each element of the input.
     * @since 1.0.2
     */
    public static String[] toStringArray(double[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of arbitrary objects into an array of strings
     * using the `toString()` method of the objects.
     *
     * `null` references in the array are converted to the string `"null"`. This
     * method does not throw an exception for such references.
     *
     * @param anArray The array of objects to convert.
     * @return An array of strings of the same length as the input, each one
     * containing the string representation of the corresponding element of the
     * input.
     * @since 1.0.2
     */
    public static String[] toStringArray(Object[] anArray)
    {
        String[] output = new String[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = String.valueOf(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of strings into an array of bytes.
     *
     * The strings are expected to be in base ten.
     *
     * @param anArray The array to convert.
     * @return An array of bytes of the same length as the input, each element
     * of which contains the decoded byte of the corresponding input element.
     * @throws NumberFormatException if any of the input elements can not be
     * parsed as a byte.
     * @since 1.0.2
     */
    public static byte[] toByteArray(String[] anArray) throws NumberFormatException
    {
        byte[] output = new byte[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = Byte.parseByte(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of strings into an array of shorts.
     *
     * The strings are expected to be in base ten.
     *
     * @param anArray The array to convert.
     * @return An array of shorts of the same length as the input, each element
     * of which contains the decoded short of the corresponding input element.
     * @throws NumberFormatException if any of the input elements can not be
     * parsed as a short.
     * @since 1.0.2
     */
    public static short[] toShortArray(String[] anArray) throws NumberFormatException
    {
        short[] output = new short[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = Short.parseShort(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of strings into an array of ints.
     *
     * The strings are expected to be in base ten.
     *
     * @param anArray The array to convert.
     * @return An array of ints of the same length as the input, each element
     * of which contains the decoded int of the corresponding input element.
     * @throws NumberFormatException if any of the input elements can not be
     * parsed as an int.
     * @since 1.0.2
     */
    public static int[] toIntArray(String[] anArray) throws NumberFormatException
    {
        int[] output = new int[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = Integer.parseInt(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of strings into an array of longs.
     *
     * The strings are expected to be in base ten.
     *
     * @param anArray The array to convert.
     * @return An array of longs of the same length as the input, each element
     * of which contains the decoded long of the corresponding input element.
     * @throws NumberFormatException if any of the input elements can not be
     * parsed as a long.
     * @since 1.0.2
     */
    public static long[] toLongArray(String[] anArray) throws NumberFormatException
    {
        long[] output = new long[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = Long.parseLong(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of strings into an array of floats.
     *
     * The strings are expected to be in base ten.
     *
     * @param anArray The array to convert.
     * @return An array of floats of the same length as the input, each element
     * of which contains the decoded float of the corresponding input element.
     * @throws NumberFormatException if any of the input elements can not be
     * parsed as a float.
     * @since 1.0.2
     */
    public static float[] toFloatArray(String[] anArray) throws NumberFormatException
    {
        float[] output = new float[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = Float.parseFloat(anArray[index]);
        return output;
    }

    /**
     * @brief Converts an array of strings into an array of doubles.
     *
     * The strings are expected to be in base ten.
     *
     * @param anArray The array to convert.
     * @return An array of doubles of the same length as the input, each element
     * of which contains the decoded double of the corresponding input element.
     * @throws NumberFormatException if any of the input elements can not be
     * parsed as a double.
     * @since 1.0.2
     */
    public static double[] toDoubleArray(String[] anArray) throws NumberFormatException
    {
        double[] output = new double[anArray.length];
        for(int index = 0; index < anArray.length; index++)
            output[index] = Double.parseDouble(anArray[index]);
        return output;
    }

    /**
     * Returns a subset of an array, starting from the beginning. The input
     * array is not actually modified in any way. The return value is always a
     * copy, even if the requested subset is the entire array.
     *
     * @param array the array to truncate.
     * @param length the length of the subset. The length may not be larger than
     * the length of the input array.
     * @return a new array of the specified length with the same component type
     * as the input, containing the specified subset of elements.
     * @throws IndexOutOfBoundsException if {@code length} does not meet the
     * restrictions imposed by the length of {@code array}.
     * @throws ArrayStoreException if {@code array} is not an array.
     * @since 1.0.1
     */
    public static Object truncate(Object array, int length)
            throws ArrayStoreException, IndexOutOfBoundsException
    {
        return truncate(array, 0, length);
    }

    /**
     * Returns a subset of an array. The input array is not actually modified
     * in any way. The return value is always a copy, even if the requested
     * subset is the entire array.
     *
     * @param array the array to truncate.
     * @param start the index to copy from within {@code array}. This number
     * must be between {@code 0} and {@code array.length - 1}.
     * @param length the length of the subset. {@code start + length} may not be
     * larger than the length of the input array.
     * @return a new array of the specified length with the same component type
     * as the input, containing the specified subset of elements.
     * @throws IndexOutOfBoundsException if {@code start} or {@code length} do
     * not meet the restrictions imposed by the length of {@code array}.
     * @throws ArrayStoreException if {@code array} is not an array.
     * @since 1.0.1
     */
    public static Object truncate(Object array, int start, int length)
            throws ArrayStoreException, IndexOutOfBoundsException
    {
        Class<?> componentType = array.getClass().getComponentType();
        if(componentType == null) {
            throw new ArrayStoreException();
        }
        Object copy = Array.newInstance(componentType, length);
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
