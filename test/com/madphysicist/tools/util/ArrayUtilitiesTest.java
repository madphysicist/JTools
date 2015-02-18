/*
 * ArrayUtilitiesTest.java (TestClass: com.madphysicist.tools.util.ArrayUtilitiesTest)
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2012 by Joseph Fox-Rabinovitz
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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link com.madphysicist.tools.util.ArrayUtilities}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 20 Sep 2012 - J. Fox-Rabinovitz - Initial coding.
 * @version 1.0.1, 17 Feb 2014 - J. Fox-Rabinovitz - Added tests for
 *      `toStringArray()` and `to<Primitive>Array()` methods.
 * @since 1.0.0
 */
public class ArrayUtilitiesTest
{
    /**
     * Indicates that {@code sort} should be called with {@code nullsFirst} set
     * to {@code true}. Intended for use in the array returned by
     * {@link #testSortDataProvider}.
     *
     * @since 1.0.0
     */
    private static final int SORT_NULLS_FIRST = -1;
    
    /**
     * Indicates that {@code sort} should be called with {@code nullsFirst} set
     * to {@code false}. Intended for use in the array returned by
     * {@link #testSortDataProvider}.
     *
     * @since 1.0.0
     */
    private static final int SORT_NULLS_LAST = 1;

    /**
     * Indicates that {@code sort} should be called twice with
     * {@code nullsFirst} set each of its possible values. Intended for use in
     * the array returned by {@link #testSortDataProvider}.
     *
     * @since 1.0.0
     */
    private static final int SORT_BOTH = 0;

    /**
     * A data provider for {@link #testSort}. Returns an array of input
     * parameters that exercise different scenarios.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null: Checks that null is returned if the input is null no matter how
     * the array is sorted.</li>
     * <li>Empty: Checks that an empty array is sorted correctly.</li>
     * <li>No Nulls: Checks that an array with no null elements is sorted
     * correctly.</li>
     * <li>Nulls First: Checks that null elements are moved to the beginning of
     * the array if {@code nullsFirst} is set to {@code true}</li>
     * <li>Nulls Last: Checks that null elements are moved to the end of the
     * array if {@code nullsFirst} is set to {@code false}</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the input array, a test flag and
     * the expected result.
     * @since 1.0.0
     */
    @DataProvider(name = "testSortDataProvider")
    private Object[][] testSortDataProvider()
    {
        String[] inputArray = new String[] {null, "B", null, "D", null, "A",
                                            null, "C", null};
        return new Object[][] {
            {"null", null, SORT_BOTH, null},
            {"empty", new String[0], SORT_BOTH, new String[0]},
            {"no nulls", new String[] {"B", "D", "A", "C"}, SORT_BOTH,
                         new String[] {"A", "B", "C", "D"}},
            {"nulls first", inputArray, SORT_NULLS_FIRST,
                            new String[] {null, null, null, null, null,
                                          "A", "B", "C", "D"}},
            {"nulls last", inputArray, SORT_NULLS_LAST,
                           new String[] {"A", "B", "C", "D",
                                         null, null, null, null, null}}
        };
    }

    /**
     * Input test of sort method of class ArrayUtilities. This test method
     * is parametrized with values from the {@link #testSortDataProvider()}
     * array. It ensures that arrays with and without {@code nulls} are sorted
     * correctly.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param testArray the input array.
     * @param testType an integer specifying how the test should be run:
     * {@link #SORT_NULLS_FIRST}, {@link #SORT_NULLS_LAST}, or
     * {@link #SORT_BOTH}.
     * @param expectedValue the expected result after sorting.
     * @since 1.0.0
     */
    @Test(dataProvider = "testSortDataProvider")
    public void testSort(String label, String[] testArray, int testType, String[] expectedValue)
    {
        System.out.println("sort (" + label + ")");
        if(testType == SORT_NULLS_FIRST || testType == SORT_BOTH) {
            String[] result = ArrayUtilities.sort(testArray, true);
            Assert.assertEquals(result, expectedValue);
        }
        if(testType == SORT_NULLS_LAST || testType == SORT_BOTH) {
            String[] result = ArrayUtilities.sort(testArray, false);
            Assert.assertEquals(result, expectedValue);
        }
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(byte[]) with an array
     * containing a varied selection of values.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayByte()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new byte[0]), new String[0]);

        // normal
        final byte[] byteArray = new byte[] {(byte)1, (byte)4, (byte)255, (byte)-255, (byte)-4};
        final String[] expectedArray = new String[] {"1", "4", "-1", "1", "-4"};

        String[] result = ArrayUtilities.toStringArray(byteArray);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(short[]) with an array
     * containing a varied selection of values.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayShort()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new short[0]), new String[0]);

        // normal
        final short[] shortArray = new short[] {(short)1, (short)4, (short)65536, (short)65537, (short)-65537, (short)-4};
        final String[] expectedArray = new String[] {"1", "4", "0", "1", "-1", "-4"};

        String[] result = ArrayUtilities.toStringArray(shortArray);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(int[]) with an array containing
     * a varied selection of values.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayInt()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new int[0]), new String[0]);

        // normal
        final int[] intArray = new int[] {1, 4, 897633, -897633, -4};
        final String[] expectedArray = new String[] {"1", "4", "897633", "-897633", "-4"};

        String[] result = ArrayUtilities.toStringArray(intArray);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(long[]) with an array
     * containing a varied selection of values.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayLong()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new long[0]), new String[0]);

        // normal
        final long[] longArray = new long[] {1L, 4L, 2147483648L, -2147483648L, -4L};
        final String[] expectedArray = new String[] {"1", "4", "2147483648", "-2147483648", "-4"};

        String[] result = ArrayUtilities.toStringArray(longArray);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(float[]) with an array
     * containing a varied selection of values.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayFloat()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new float[0]), new String[0]);

        // normal
        final float[] floatArray = new float[] {
                1.0f,
                -4.3f,
                Float.POSITIVE_INFINITY,
                0.0f,
                Float.NEGATIVE_INFINITY,
                Float.NaN};
        final String[] expectedArray = new String[] {
                "1.0",
                "-4.3",
                Float.toString(Float.POSITIVE_INFINITY),
                "0.0",
                Float.toString(Float.NEGATIVE_INFINITY),
                Float.toString(Float.NaN)};

        String[] result = ArrayUtilities.toStringArray(floatArray);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(double[]) with an array
     * containing a varied selection of values.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayDouble()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new double[0]), new String[0]);

        // normal
        final double[] doubleArray = new double[] {
                1.0,
                -4.3,
                Double.POSITIVE_INFINITY,
                0.0,
                Double.NEGATIVE_INFINITY,
                Double.NaN};
        final String[] expectedArray = new String[] {
                "1.0",
                "-4.3",
                Double.toString(Double.POSITIVE_INFINITY),
                "0.0",
                Double.toString(Double.NEGATIVE_INFINITY),
                Double.toString(Double.NaN)};

        String[] result = ArrayUtilities.toStringArray(doubleArray);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * @brief Tests ArrayUtilities.toStringArray(Object[]) with arrays
     * containing a varied selection of values, including `null`.
     *
     * @since 1.0.1
     */
    @Test public void testToStringArrayObject()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toStringArray(new Object[0]), new String[0]);
        Assert.assertEquals(ArrayUtilities.toStringArray(new String[0]), new String[0]);

        // string
        final String[] stringArray = new String[] { "a", "bc", "null", null};
        final String[] expectedStrings = new String[] {"a", "bc", "null", "null"};
        final String[] stringResult = ArrayUtilities.toStringArray(stringArray);
        Assert.assertEquals(stringResult, expectedStrings);

        // object
        final Object[] objectArray = new Object[] {new Object(), "a", null};
        final String[] expectedObjects = new String[] {objectArray[0].toString(), "a", "null"};
        final String[] objectResult = ArrayUtilities.toStringArray(objectArray);
        Assert.assertEquals(objectResult, expectedObjects);
    }

    /**
     * @brief Checks the conversion performed by ArrayUtilities.toByteArray()
     * with a selection of values, including an exception case.
     */
    @Test(expectedExceptions = NumberFormatException.class)
    public void testToByteArray()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toByteArray(new String[0]), new byte[0]);

        // normal
        final String[] stringArray = new String[] {"0", "1", "-7"};
        final byte[] expectedArray = new byte[] {(byte)0, (byte)1, (byte)-7};
        final byte[] bytes = ArrayUtilities.toByteArray(stringArray);
        Assert.assertEquals(bytes, expectedArray);

        // exception
        final String[] exceptionArray = new String[] {"65535"};
        ArrayUtilities.toByteArray(exceptionArray);
    }

    /**
     * @brief Checks the conversion performed by ArrayUtilities.toShortArray()
     * with a selection of values, including an exception case.
     */
    @Test(expectedExceptions = NumberFormatException.class)
    public void testToShortArray()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toShortArray(new String[0]), new short[0]);

        // normal
        final String[] stringArray = new String[] {"0", "1", "-7"};
        final short[] expectedArray = new short[] {(short)0, (short)1, (short)-7};
        final short[] shorts = ArrayUtilities.toShortArray(stringArray);
        Assert.assertEquals(shorts, expectedArray);

        // exception
        final String[] exceptionArray = new String[] {"65536"};
        ArrayUtilities.toShortArray(exceptionArray);
    }

    /**
     * @brief Checks the conversion performed by ArrayUtilities.toIntArray()
     * with a selection of values, including an exception case.
     */
    @Test(expectedExceptions = NumberFormatException.class)
    public void testToIntArray()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toIntArray(new String[0]), new int[0]);

        // normal
        final String[] stringArray = new String[] {"0", "1", "-700"};
        final int[] expectedArray = new int[] {0, 1, -700};
        final int[] ints = ArrayUtilities.toIntArray(stringArray);
        Assert.assertEquals(ints, expectedArray);

        // exception
        final String[] exceptionArray = new String[] {"12.0"};
        ArrayUtilities.toIntArray(exceptionArray);
    }

    /**
     * @brief Checks the conversion performed by ArrayUtilities.toLongArray()
     * with a selection of values, including an exception case.
     */
    @Test(expectedExceptions = NumberFormatException.class)
    public void testToLongArray()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toLongArray(new String[0]), new long[0]);

        // normal
        final String[] stringArray = new String[] {"0", "1", "-7"};
        final long[] expectedArray = new long[] {0L, 1L, -7L};
        final long[] longs = ArrayUtilities.toLongArray(stringArray);
        Assert.assertEquals(longs, expectedArray);

        // exception
        final String[] exceptionArray = new String[] {"12.0"};
        ArrayUtilities.toLongArray(exceptionArray);
    }

    /**
     * @brief Checks the conversion performed by ArrayUtilities.toFloatArray()
     * with a selection of values, including an exception case.
     */
    @Test(expectedExceptions = NumberFormatException.class)
    public void testToFloatArray()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toFloatArray(new String[0]), new float[0]);

        // normal
        final String[] stringArray = new String[] {"0", "1.1", "-7.21466324325",
                "NaN", "Inf", "-Inf"};
        final float[] expectedArray = new float[] {0.0f, 1.1f, -7.21466324f,
                Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY};
        final float[] floats = ArrayUtilities.toFloatArray(stringArray);
        Assert.assertEquals(floats, expectedArray);

        // exception
        final String[] exceptionArray = new String[] {"341f"};
        ArrayUtilities.toFloatArray(exceptionArray);
    }

    /**
     * @brief Checks the conversion performed by ArrayUtilities.toDoubleArray()
     * with a selection of values, including an exception case.
     */
    @Test(expectedExceptions = NumberFormatException.class)
    public void testToDoubleArray()
    {
        // empty
        Assert.assertEquals(ArrayUtilities.toShortArray(new String[0]), new double[0]);

        // normal
        final String[] stringArray = new String[] {"0.0", "1", "-7.4", "12.1244661253112",
                "NaN", "Inf", "-Inf"};
        final double[] expectedArray = new double[] {0.0, 1.0, -7.4, 12.1244661253112,
                Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        final double[] doubles = ArrayUtilities.toDoubleArray(stringArray);
        Assert.assertEquals(doubles, expectedArray);

        // exception
        final String[] exceptionArray = new String[] {"1e-7000"};
        ArrayUtilities.toDoubleArray(exceptionArray);
    }
}
