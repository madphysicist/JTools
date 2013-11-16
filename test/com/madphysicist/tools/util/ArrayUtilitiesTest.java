/*
 * ArrayUtilitiesTest.java
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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link
 * com.madphysicist.tools.util.ArrayUtilities}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 20 Sept 2012
 * @since 1.0.0.0
 */
public class ArrayUtilitiesTest
{
    /**
     * Indicates that {@code sort} should be called with {@code nullsFirst} set
     * to {@code true}. Intended for use in the array returned by
     * {@link #testSortDataProvider}.
     */
    private static final int SORT_NULLS_FIRST = -1;
    
    /**
     * Indicates that {@code sort} should be called with {@code nullsFirst} set
     * to {@code false}. Intended for use in the array returned by
     * {@link #testSortDataProvider}.
     */
    private static final int SORT_NULLS_LAST = 1;

    /**
     * Indicates that {@code sort} should be called twice with
     * {@code nullsFirst} set each of its possible values. Intended for use in
     * the array returned by {@link #testSortDataProvider}.
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
     * @since 1.0.0.0
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
     * @since 1.0.0.0
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
}
