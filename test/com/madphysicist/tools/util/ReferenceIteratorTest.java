/*
 * ReferenceIteratorTest.java (TestClass: com.madphysicist.tools.util.ReferenceIteratorTest)
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Joseph Fox-Rabinovitz
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link com.madphysicist.tools.util.ReferenceIterator}. Because of the simplicity of this
 * class, some of the tests are overlapping or redundant. This may come in handy for regression testing if more complex
 * functionality is added in the future.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 Aug 2014 - J. Fox-Rabinovitz - Initial coding.
 * @since 1.0.0
 */
public class ReferenceIteratorTest
{
    /**
     * The number of times checks are repeated to demonstrate repeatability of certain operations.
     *
     * @since 1.0.0
     */
    private static int REPEATS = 3;

    /**
     * An iterator wrapping this test object that is available to all tests. This reference is re-created for each
     * test method that is run by {@link #testMethodSetup()}. 
     *
     * @since 1.0.0
     */
    private ReferenceIterator<ReferenceIteratorTest> iterator;

    /**
     * Performs the common setup for each test method. This includes (re)initializing the {@link #iterator} field.
     *
     * @since 1.0.0
     */
    @BeforeMethod public void testMethodSetup()
    {
        this.iterator = new ReferenceIterator<>(this);
    }

    /**
     * Checks that the {@code hasNext()} method of an iterator returns true before {@code next()} is called, {@code
     * false} after {@code next}, and {@code true} again after {@code reset}. The check is run {@value #REPEATS} times
     * to demonstrate repeatability.
     *
     * @since 1.0.0
     */
    @Test
    public void hasNextTest()
    {
        Assert.assertTrue(iterator.hasNext());
        for(int i = 0; i < REPEATS; i++) {
            iterator.next();
            Assert.assertFalse(iterator.hasNext());
            iterator.reset();
            Assert.assertTrue(iterator.hasNext());
        }
    }

    /**
     * Checks that the {@code next()} method works when invoked once. The check is run {@value #REPEATS} times with a
     * call to {@code reset()} in between to demonstrate repeatability.
     *
     * @since 1.0.0
     */
    @Test
    public void nextResetTest()
    {
        for(int i = 0; i < REPEATS; i++) {
            Assert.assertSame(iterator.next(), this);
            iterator.reset();
        }
    }

    /**
     * Checks that the {@code next()} method fails when invoked twice consecutively without a reset in between. The
     * expected failure is an {@code IllegalStateException}. This method also checks that the failure of {@code next()}
     * is correlated with the return value of {@code hasNext()}.
     *
     * @throws IllegalStateException as expected.
     * @since 1.0.0
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void nextTwiceTest() throws IllegalStateException
    {
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        iterator.next(); // should fail here
        Assert.fail();
    }

    /**
     * Checks that the {@code next()} method can be called exactly once before {@code hasNext()} starts returning false.
     *
     * @since 1.0.0
     */
    @Test
    public void nextCountTest()
    {
        int count;
        for(count = 0; iterator.hasNext(); count++) {
            iterator.next();
        }
        Assert.assertEquals(count, 1);
    }

    /**
     * Checks that the {@code remove()} method of an iterator fails when invoked before the {@code next()} method.
     * The expected failure is an {@code UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException as expected.
     * @since 1.0.0
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void removeBeforeTest() throws UnsupportedOperationException
    {
        iterator.remove(); // should fail here
        iterator.next(); // this is just for show
        Assert.fail();
    }

    /**
     * Checks that the {@code remove()} method of an iterator fails when invoked after the {@code next()} method.
     * The expected failure is an {@code UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException as expected.
     * @since 1.0.0
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void removeAfterTest() throws UnsupportedOperationException
    {
        iterator.next();
        iterator.remove(); // should fail here
        Assert.fail();
    }

    /**
     * Checks that calling {@code reset()} repeatedly has no effect ({@code hasNext()} keeps returning {@code true}).
     * The check is done {@value #REPEATS} times.
     *
     * @since 1.0.0
     */
    @Test
    public void resetRepeatedTest()
    {
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        for(int i = 0; i < REPEATS; i++) {
            iterator.reset();
            Assert.assertTrue(iterator.hasNext());
        }
    }
}
