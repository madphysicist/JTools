/*
 * HashUtilities.java
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * This class is a utility library for performing various kinds of hash-related
 * operations. The algorithms provided by the {@code hashCode()} methods are
 * taken from Joshua Bloch's Effective Java (Addison-Wesley 2001), via
 * http://www.linuxtopia.org/online_books/programming_books/thinking_in_java/TIJ313_029.htm
 * and http://www.javapractices.com/topic/TopicAction.do?Id=28.
 * <p>
 * To compute a hash code of an object, it is possible to chain together
 * multiple calls to the various static {@code hashCode()} methods provided
 * here. Calls should be chained together using the {@code seed} parameter as
 * follows:
 * <pre>
 * public class Test
 * {
 *     private boolean bo;
 *     private byte by;
 *     private short sh;
 *     private char ch;
 *     private int in;
 *     private long lo;
 *     private float fl;
 *     private double do;
 *
 *     ...
 *
 *     public boolean equals(Object obj)
 *     {
 *         // equals depends on all the private fields
 *         ...
 *     }
 *
 *     public int hashCode()
 *     {
 *         int result = HashUtilities.hashCode(bo);
 *         result = HashUtilities.hashCode(result, by);
 *         result = HashUtilities.hashCode(result, sh);
 *         result = HashUtilities.hashCode(result, ch);
 *         result = HashUtilities.hashCode(result, in);
 *         result = HashUtilities.hashCode(result, lo);
 *         result = HashUtilities.hashCode(result, fl);
 *         result = HashUtilities.hashCode(result, do);
 *         return result;
 *     }
 * }
 * </pre>
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 11 Nov 2012
 * @since 1.0.0.0
 */
public class HashUtilities
{
    /**
     * A seed for the single-argument versions of {@code hashCode()} used to
     * produce decent distributions if hash values.
     *
     * @since 1.0.0.0
     */
    private static final int SEED = 17;

    /**
     * A prime multiple for the seed value for the {@code hashCode()} methods,
     * used to produce decent distributions of hash values.
     *
     * @since 1.0.0.0
     */
    private static final int PRIME = 37;

    /**
     * An MD5 hash generator. This object is used by the {@code hashPassword()}
     * method. The initialization of this parameter throws a checked exception,
     * so it must be done in a separate {@code static} block.
     *
     * @since 1.0.0.0
     */
    private static final MessageDigest obfuscator;

    static {
        try {
            // initialize the obfuscator
            obfuscator = MessageDigest.getInstance("MD5");
        } catch(NoSuchAlgorithmException nsae) {
            // this should never happen. NEVER!
            throw new InternalError("missing MD5 implementation");
        }
    }

    /**
     * A private constructor to prevent instantiation.
     * @since 1.0.0.0
     */
    private HashUtilities() {}

    /**
     * Computes the MD5 hash of a password string and returns a hexadecimal
     * representation of the result.
     *
     * @param password the string to hash.
     * @return a hexadecimal representation of the MD5 hash of the string. Each
     * character in the result represents 4-bits of the hash. Zeros are not
     * omitted from any part of the result.
     */
    public static String hashPassword(String password)
    {
        byte[] hash = obfuscator.digest(password.getBytes(TextUtilities.CHARSET));
        return TextUtilities.toHexString(hash);
    }

    /**
     * Computes the hash value of a boolean. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param b a boolean value.
     * @return the hash of {@code b}.
     * @since 1.0.0.0
     */
    public static int hashCode(boolean b)
    {
        return hashCode(SEED, b);
    }

    /**
     * Computes the hash value of a byte. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param b a byte value.
     * @return the hash of {@code b}.
     * @since 1.0.0.0
     */
    public static int hashCode(byte b)
    {
        return hashCode(SEED, b);
    }

    /**
     * Computes the hash value of a char. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param c a char value.
     * @return the hash of {@code c}.
     * @since 1.0.0.0
     */
    public static int hashCode(char c)
    {
        return hashCode(SEED, c);
    }

    /**
     * Computes the hash value of a short. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param s a short value.
     * @return the hash of {@code s}.
     * @since 1.0.0.0
     */
    public static int hashCode(short s)
    {
        return hashCode(SEED, s);
    }

    /**
     * Computes the hash value of an int. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param i an int value.
     * @return the hash of {@code i}.
     * @since 1.0.0.0
     */
    public static int hashCode(int i)
    {
        return hashCode(SEED, i);
    }

    /**
     * Computes the hash value of a long. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param l a long value.
     * @return the hash of {@code l}.
     * @since 1.0.0.0
     */
    public static int hashCode(long l)
    {
        return hashCode(SEED, l);
    }

    /**
     * Computes the hash value of a float. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param f a float value.
     * @return the hash of {@code f}.
     * @since 1.0.0.0
     */
    public static int hashCode(float f)
    {
        return hashCode(SEED, f);
    }

    /**
     * Computes the hash value of a double. This method can be used to compute
     * the first in a sequence of hash values that will be chained together to
     * obtain the hash value of an object. This value can be used by itself or
     * as the seed for further hash computations.
     *
     * @param d a double value.
     * @return the hash of {@code d}.
     * @since 1.0.0.0
     */
    public static int hashCode(double d)
    {
        return hashCode(SEED, d);
    }

    /**
     * Computes the hash value of an object. If the object is {@code null}, the
     * hash value will only depend on the default seed value. If the object is
     * an array, the hash code will be computed based on each element of the
     * array. If the object is a normal object, its {@code hashCode()} will be
     * used. Note that this method is <b>not</b> recursive, even for arrays.
     * <p>
     * This method can be used to compute the first in a sequence of hash values
     * that will be chained together to obtain the hash value of an object. This
     * value can be used by itself or as the seed for further hash computations.
     *
     * @param o an object, possibly an array.
     * @return the hash of {@code b}.
     * @see Arrays#hashCode(boolean[])
     * @see Arrays#hashCode(byte[])
     * @see Arrays#hashCode(char[])
     * @see Arrays#hashCode(short[])
     * @see Arrays#hashCode(int[])
     * @see Arrays#hashCode(long[])
     * @see Arrays#hashCode(float[])
     * @see Arrays#hashCode(double[])
     * @see Arrays#deepHashCode(java.lang.Object[])
     * @since 1.0.0.0
     */
    public static int hashCode(Object o)
    {
        return hashCode(SEED, o);
    }

    /**
     * Computes the hash value of a boolean with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param b a boolean value.
     * @return the hash of {@code b}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, boolean b)
    {
        return reSeed(seed) + (b ? 1 : 0);
    }

    /**
     * Computes the hash value of a byte with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param b a byte value.
     * @return the hash of {@code b}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, byte b)
    {
        return reSeed(seed) + (int)b;
    }

    /**
     * Computes the hash value of a char with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param c a char value.
     * @return the hash of {@code c}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, char c)
    {
        return reSeed(seed) + (int)c;
    }

    /**
     * Computes the hash value of a short with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param s a short value.
     * @return the hash of {@code s}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, short s)
    {
        return reSeed(seed) + (int)s;
    }

    /**
     * Computes the hash value of an int with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param i an int value.
     * @return the hash of {@code i}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, int i)
    {
        return reSeed(seed) + i;
    }

    /**
     * Computes the hash value of a long with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param l a long value.
     * @return the hash of {@code l}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, long l)
    {
        return reSeed(seed) + (int)(l ^ (l >>> 32));
    }

    /**
     * Computes the hash value of a float with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param f a float value.
     * @return the hash of {@code f}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, float f)
    {
        return hashCode(seed, Float.floatToIntBits(f));
    }

    /**
     * Computes the hash value of a double with a given seed. The seed value
     * can be used to incorporate previous computations into this hash value.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param d a double value.
     * @return the hash of {@code d}.
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, double d)
    {
        return hashCode(seed, Double.doubleToLongBits(d));
    }

    /**
     * Computes the hash value of an object. If the object is {@code null}, the
     * hash value will only depend on the seed value. If the object is an array,
     * the hash code will be computed based on each element of the array. If the
     * object is a normal object, its {@code hashCode()} will be used. Note that
     * this method is <b>not</b> recursive, even for arrays.
     * <p>
     * This method can be used to compute the first in a sequence of hash values
     * that will be chained together to obtain the hash value of an object. This
     * value can be used by itself or as the seed for further hash computations.
     *
     * @param seed the value to seed the hash comuptation with. This value can
     * be used to incorporate previous hash computations into the result.
     * @param o an object, possibly an array.
     * @return the hash of {@code b}.
     * @see Arrays#hashCode(boolean[])
     * @see Arrays#hashCode(byte[])
     * @see Arrays#hashCode(char[])
     * @see Arrays#hashCode(short[])
     * @see Arrays#hashCode(int[])
     * @see Arrays#hashCode(long[])
     * @see Arrays#hashCode(float[])
     * @see Arrays#hashCode(double[])
     * @see Arrays#deepHashCode(java.lang.Object[])
     * @since 1.0.0.0
     */
    public static int hashCode(int seed, Object o)
    {
        int hash;
        if(o == null) {
            hash = 0;
        } else if(o.getClass().isArray()) {
            Class<?> cls = o.getClass().getComponentType();
            if(cls == Boolean.TYPE)
                hash = Arrays.hashCode((boolean[]) o);
            else if(cls == Byte.TYPE)
                hash = Arrays.hashCode((byte[]) o);
            else if(cls == Character.TYPE)
                hash = Arrays.hashCode((char[]) o);
            else if(cls == Short.TYPE)
                hash = Arrays.hashCode((short[]) o);
            else if(cls == Integer.TYPE)
                hash = Arrays.hashCode((int[]) o);
            else if(cls == Long.TYPE)
                hash = Arrays.hashCode((long[]) o);
            else if(cls == Float.TYPE)
                hash = Arrays.hashCode((float[]) o);
            else if(cls == Double.TYPE)
                hash = Arrays.hashCode((double[]) o);
            else
                hash = Arrays.deepHashCode((Object[]) o);
        } else {
            hash = o.hashCode();
        }
        return hashCode(seed, hash);
    }

    /**
     * Computes the part of a hash value that comes from the seed. This will be
     * added to the hash of the item being processed by one of the two-argument
     * {@code hashCode()} methods.
     *
     * @param seed a seed value.
     * @return {@code seed} multiplied by {@link #PRIME}.
     * @since 1.0.0.0
     */
    private static int reSeed(int seed)
    {
        return PRIME * seed;
    }
}
