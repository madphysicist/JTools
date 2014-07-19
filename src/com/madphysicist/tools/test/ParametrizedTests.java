/*
 * ParametrizedTests.java (Class: com.madphysicist.tools.test.ParametrizedTests)
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
import org.testng.Assert;

/**
 * Provides a framework for running simple cases of parametrized tests using
 * TestNG. It is very common to have a set of tests that follow a simple
 * pattern such as "plug in the input parameters and compare the results". This
 * class allows multiple such methods to be tested with a single data provider.
 * Note that when passing {@code Method} objects with a TestNG data provider,
 * the corresponding test argument must be annotated with {@code @NoInjection}.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 25 Oct 2012
 * @since 1.0.0.0
 * @see org.testng.annotations.NoInjection
 */
public class ParametrizedTests
{
    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0.0
     */
    private ParametrizedTests() {}

    /**
     * Invokes a method that is expected to throw an exception and makes the
     * appropriate assertion. If the method could not be invoked properly, or
     * does not throw an exception, the failure message will informative. Note
     * that this method does not impose any restrictions on the message of the
     * exception.
     *
     * @param label a label to include in the console output.
     * @param obj the object to which the method belongs. If the method is
     * static, this parameter will be ignored and can be set to {@code null}.
     * @param method the method to invoke. The method must be accessible for
     * invocation, or the test will fail.
     * @param cls the class of the expected exception. This method allows any
     * subclass of this type to pass the test. To test for an exact type, not
     * an "is a" relationship, use {@code testMethodExactException()}.
     * @param args the arguments to invoke the method with. If the arguments are
     * of incorrect type, the test will fail.
     * @see #testMethodExactException(java.lang.String, java.lang.Object,
     * java.lang.reflect.Method,java.lang.Class, java.lang.Object[])
     * testMethodExactException(String, Object, Method, Class, Object...)
     * @since 1.0.0.0
     */
    public static void testMethodException(String label, Object obj, Method method, Class<? extends Throwable> cls, Object... args)
    {
        System.out.println(method.getDeclaringClass().getSimpleName() + "." + method.getName() + " (" + label + ")");

        String errorString = "Expected exception of type " + cls.getName() + " in method " + method.toString();
        try {
            method.invoke(obj, args);
            Assert.fail(errorString);
        } catch(IllegalArgumentException iae) {
            Assert.fail("Bad arguments to method " + method.getName(), iae);
        } catch(IllegalAccessException iae) {
            Assert.fail("Unable to invoke method " + method.getName(), iae);
        } catch(InvocationTargetException ite) {
            Assert.assertTrue(cls.isAssignableFrom(ite.getCause().getClass()),
                              errorString + ": got " + ite.getCause().getClass().getName());
        }
    }

    /**
     * Invokes a method that is expected to throw an exception and makes the
     * appropriate assertion. If the method could not be invoked properly, or
     * does not throw an exception, the failure message will informative. Note
     * that this method does not impose any restrictions on the message of the
     * exception.
     *
     * @param label a label to include in the console output.
     * @param obj the object to which the method belongs. If the method is
     * static, this parameter will be ignored and can be set to {@code null}.
     * @param method the method to invoke. The method must be accessible for
     * invocation, or the test will fail.
     * @param cls the class of the expected exception. The class of the thrown
     * exception must match this class exactly. A subclass will cause this test
     * to fail. To apply a test that allows any "is a" relationship, use {@code
     * testMethodException()}.
     * @param args the arguments to invoke the method with. If the arguments are
     * of incorrect type, the test will fail.
     * @see #testMethodException(java.lang.String, java.lang.Object,
     * java.lang.reflect.Method,java.lang.Class, java.lang.Object[])
     * testMethodException(String, Object, Method, Class, Object...)
     * @since 1.0.0.0
     */
    public static void testMethodExactException(String label, Object obj, Method method, Class<? extends Throwable> cls, Object... args)
    {
        System.out.println(method.getDeclaringClass().getSimpleName() + "." + method.getName() + " (" + label + ")");

        String errorString = "Expected exception of type " + cls.getName() + " in method " + method.toString();
        try {
            method.invoke(obj, args);
            Assert.fail(errorString);
        } catch(IllegalArgumentException iae) {
            Assert.fail("Bad arguments to method " + method.getName(), iae);
        } catch(IllegalAccessException iae) {
            Assert.fail("Unable to invoke method " + method.getName(), iae);
        } catch(InvocationTargetException ite) {
            Assert.assertTrue(cls.equals(ite.getCause().getClass()),
                              errorString + ": got " + ite.getCause().getClass().getName());
        }
    }

    /**
     * Invokes a method that has a known expected result and makes the
     * appropriate assertion. If the method could not be invoked properly or
     * throws an exception, the failure message will informative.
     *
     * @param label a label to include in the console output.
     * @param obj the object to which the method belongs. If the method is
     * static, this parameter will be ignored and can be set to {@code null}.
     * @param method the method to invoke. The method must be accessible for
     * invocation, or the test will fail.
     * @param expected the expected result.
     * @param args the arguments to invoke the method with. If the arguments are
     * of incorrect type, the test will fail.
     * @since 1.0.0.0
     */
    public static void testMethodValue(String label, Object obj, Method method, Object expected, Object... args)
    {
        System.out.println(method.getDeclaringClass().getSimpleName() + "." + method.getName() + " (" + label + ")");

        try {
            Object actual = method.invoke(obj, args);
            Assert.assertEquals(actual, expected);
        } catch(NullPointerException npe) {
            Assert.fail("Attempting to call instance method " + method.getName() + " as static", npe);
        } catch(IllegalArgumentException iae) {
            Assert.fail("Bad arguments to method " + method.getName(), iae);
        } catch(IllegalAccessException iae) {
            Assert.fail("Unable to invoke method " + method.getName(), iae);
        } catch(InvocationTargetException ite) {
            Assert.fail("Method " + method.getName() + " threw an exception", ite);
        } // No catch block is necessary for raw AssertionError from assertEquals
    }
}
