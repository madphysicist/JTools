/*
 * ParametrizedTestsTest.java
 *
 * Mad Physicist JTools Project (TestNG Utilities)
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

package com.madphysicist.tools.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import java.util.TreeMap;
import com.madphysicist.tools.util.TextUtilities;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.NoInjection;
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link
 * com.madphysicist.tools.test.ParametrizedTests}. This class does not use
 * {@code ParametrizedTests} as a test harness, no matter how tempting it may
 * be.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 26 Oct 2012
 * @since 1.0.0.0
 */
public class ParametrizedTestsTest
{
    private final Object testObject;
    private final Method testMethod;
    private final Method testStaticMethod;
    private final String testBundleName;

    public ParametrizedTestsTest()
    {
        testBundleName = "joe.tools.test.ParametrizedTestsTest";
        testObject = ResourceBundle.getBundle(testBundleName);
        try {
            testMethod = testObject.getClass().getMethod("handleGetObject", String.class);
            testStaticMethod = testObject.getClass().getMethod("getBundle", String.class);
        } catch(NoSuchMethodException nsme) {
            throw new RuntimeException("Unable to initialize test class.", nsme);
        }
    }
/*
    @Test(dataProvider = "testTestMethodExceptionArgsDataProvider")
    public void testTestMethodExceptionArgs(String label, Object obj, @NoInjection Method method, Class<? extends Throwable> cls, Object[] args)
    {
        ParametrizedTests.testMethodException(label, obj, method, cls, args);
        // No need to assert. If the previous call did not fail, we're OK
    }
*/

    @Test(dataProvider = "testTestMethodByValueExceptionDataProvider")
    public void testTestMethodByValueException(String label, Object obj,
            @NoInjection Method method, Object expectedResult,
            Class<? extends Throwable> expectedException,
            Class<? extends Throwable> expectedCause, Object[] args)
    {
        try {
            ParametrizedTests.testMethodValue(label, obj, method, expectedResult, args);
            Assert.fail();
        } catch(Throwable t) {
            if(!expectedException.isAssignableFrom(t.getClass()))
                Assert.fail("Expected exception " + expectedException.getSimpleName() + " but got " + t.getClass().getSimpleName());
            Class<? extends Throwable> actualCause = (t.getCause() == null) ? null : t.getCause().getClass();
            if(expectedCause != actualCause && (expectedCause == null || actualCause == null || !expectedCause.isAssignableFrom(actualCause)))
                Assert.fail("Expected cause " + expectedCause.getSimpleName() + " but got " + actualCause.getSimpleName());
        }
    }

    /**
     * Checks {@code ParametrizedTests.testMethodValue} that allows successful
     * tests to pass correctly.
     *
     * @param label a label to use for output.
     * @param obj the object to call the test method on.
     * @param method the test method.
     * @param expectedResult the expected test result.
     * @param args the input arguments to the test method.
     */
    @Test(dataProvider = "testTestMethodByValueDataProvider")
    public void testTestMethodByValue(String label, Object obj, @NoInjection Method method, Object expectedResult, Object[] args)
    {
        ParametrizedTests.testMethodValue(label, obj, method, expectedResult, args);
        // No need to assert anything, we just don't want the call above to throw anything
    }

    /*
    @DataProvider(name = "testTestMethodExceptionArgsDataProvider")
    private Object[][] testTestMethodExceptionArgsDataProvider()
    {
        Object[][] result = new Object[][] {
            new Object[] {"null obj static", null, testStaticMethod, NullPointerException.class, new Object[] {null}},
            new Object[] {"non-null obj static", testObject, testStaticMethod, NullPointerException.class, new Object[] {null}},
            new Object[] {"non-null obj non-static", testObject, testMethod, NullPointerException.class, new Object[] {null}},
            new Object[] {"null obj non-static", null, testMethod, NullPointerException.class, new Object[] {null}},
        };
        return result;
    }
     */

    @DataProvider(name = "testTestMethodByValueExceptionDataProvider")
    public Object[][] testTestMethodByValueExceptionDataProvider()
    {
        return new Object[][] {
            {"static null object", null, testMethod, "value1",
                AssertionError.class, NullPointerException.class, new Object[] {"key1"}},
            {"wrong class object", "bad", testMethod, "value1",
                AssertionError.class, IllegalArgumentException.class, new Object[] {"key1"}},
            {"null method", testObject, null, null, NullPointerException.class, null, new Object[] {}},
            {"static null method", null, null, null, NullPointerException.class, null, new Object[] {}},
            {"inaccessible method", null, TestMap.getPrivateMethod(), null,
                AssertionError.class, IllegalAccessException.class, new Object[]{"getHashMap"}},
            {"bad null expectedValue", testObject, testMethod, null,
                AssertionError.class, null, new Object[] {"key3"}},
            {"wrong expectedValue", testObject, testMethod, "value2",
                AssertionError.class, null, new Object[] {"key1"}},
            {"wrong class expectedValue", testObject, testMethod, new String[] {"value2"},
                AssertionError.class, null, new Object[] {"key1"}},
            {"no args", testObject, testMethod, "value3",
                AssertionError.class, IllegalArgumentException.class, new Object[] {}},
            {"bad number args", testObject, testMethod, "value1",
                AssertionError.class, IllegalArgumentException.class, new Object[] {"key1", "key2"}},
            {"bad class args", testObject, testMethod, "value1",
                AssertionError.class, IllegalArgumentException.class, new Object[] {new String[] {"key1"}}},
            {"method throws", testObject, testMethod, null,
                AssertionError.class, InvocationTargetException.class, new Object[] {null}},
            {"static method throws", null, testStaticMethod, null,
                AssertionError.class, InvocationTargetException.class, new Object[] {testBundleName + "blah"}},
        };
    }

    @DataProvider(name = "testTestMethodByValueDataProvider")
    public Object[][] testTestMethodByValueDataProvider()
    {
        return new Object[][] {
            {null, testObject, testMethod, "value1", new Object[] {"key1"}},
            {"", testObject, testMethod, "value2", new Object[] {"key2"}},
            {"null expectedValue", testObject, testMethod, null, new Object[] {"key4"}},
            {"string expectedValue", testObject, testMethod, "value2", new Object[] {"key2"}},
            {"static string expectedValue", null, testStaticMethod, testObject, new Object[] {testBundleName}},
            {"map expectedValue", null, TestMap.getHashMapMethod(), TestMap.getTreeMap(), new Object[] {}},
        };
    }

    /**
     * Creates maps of diffrent types with identical key-value mappings for
     * testing.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0.0, 26 Oct 2012
     * @since 1.0.0.0
     */
    private static class TestMap
    {
        private static String[] properties = new String[] {"A", "a", "B", "b", "C", "c"};

        public static TreeMap<String, String> getTreeMap()
        {
            return new TreeMap<>(TextUtilities.propertiesToMap(properties));
        }
/*
        public static HashMap<String, String> getHashMap()
        {
            return new HashMap<>(TextUtilities.propertiesToMap(properties));
        }

        public static Method getTreeMapMethod()
        {
            return getMethod("getTreeMap");
        }
*/
        public static Method getHashMapMethod()
        {
            return getMethod("getHashMap");
        }

        public static Method getPrivateMethod()
        {
            try {
                return TestMap.class.getDeclaredMethod("getMethod", String.class);
            } catch(NoSuchMethodException nsme) {
                throw new RuntimeException("The impossible has happened.", nsme);
            }
        }

        private static Method getMethod(String name)
        {
            try {
                return TestMap.class.getMethod(name);
            } catch(NoSuchMethodException nsme) {
                throw new RuntimeException("The impossible has happened.", nsme);
            }
        }
    }
}
