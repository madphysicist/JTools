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
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link com.madphysicist.tools.util.ReferenceIterator}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 Aug 2014 - J. Fox-Rabinovitz - Initial coding.
 * @since 1.0.0
 */
public class ReferenceIteratorTest
{
    /**
     * Checks that the {@code remove()} method of an iterator fails when invoked before {@code next()} method.
     * The expected failure is an {@code IllegalStateException}.
     *
     * @since 1.0.0
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void removeBeforeTest()
    {
        ReferenceIterator<ReferenceIteratorTest> iterator = new ReferenceIterator<ReferenceIteratorTest>(this);
        iterator.remove();
        iterator.next(); // This operation should not happen
        Assert.fail();
    }
}
