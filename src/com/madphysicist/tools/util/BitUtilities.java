/*
 * BitUtilities.java
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

/**
 * A utility library for doing bit manipulations on integers.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 22 May 2013
 * @since 1.0.0
 */
public class BitUtilities
{
    public static final int QWORD_BITS = 64;
    public static final int DWORD_BITS = 32;
    public static final int WORD_BITS = 16;
    public static final int BYTE_BITS = 8;

    public static final long LONG_DWORD_MASK = 0xFFFFFFFFL;
    public static final long LONG_WORD_MASK = 0xFFFFL;
    public static final long LONG_BYTE_MASK = 0xFFL;

    public static final int INT_WORD_MASK = 0xFFFF;
    public static final int INT_BYTE_MASK = 0xFF;

    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private BitUtilities() {}

    public static String binaryString(byte aByte)
    {
        return binaryString(aByte, null);
    }

    public static String binaryString(byte aByte, boolean prefix)
    {
        return binaryString(aByte, prefix ? "b0" : null);
    }

    public static String binaryString(byte aByte, String prefix)
    {
        StringBuilder sb = new StringBuilder(BYTE_BITS);
        do {
            sb.append((aByte & 1) == 1 ? "1" : "0");
            aByte = (byte)((aByte & INT_BYTE_MASK) >>> 1);
        } while(aByte != 0);
        sb.reverse();
        if(prefix != null) sb.insert(0, prefix);
        return sb.toString();
    }

    public static String binaryString(short aWord)
    {
        return binaryString(aWord, null);
    }

    public static String binaryString(short aWord, boolean prefix)
    {
        return binaryString(aWord, prefix ? "b0" : null);
    }

    public static String binaryString(short aWord, String prefix)
    {
        StringBuilder sb = new StringBuilder(WORD_BITS);
        do {
            sb.append((aWord & 1) == 1 ? "1" : "0");
            aWord = (short)((aWord & INT_WORD_MASK) >>> 1);
        } while(aWord != 0);
        sb.reverse();
        if(prefix != null) sb.insert(0, prefix);
        return sb.toString();
    }

    public static String binaryString(int aDWord)
    {
        return binaryString(aDWord, null);
    }

    public static String binaryString(int aDWord, boolean prefix)
    {
        return binaryString(aDWord, prefix ? "b0" : null);
    }

    public static String binaryString(int aDWord, String prefix)
    {
        StringBuilder sb = new StringBuilder(DWORD_BITS);
        do {
            sb.append((aDWord & 1) == 1 ? "1" : "0");
            aDWord >>>= 1;
        } while(aDWord != 0);
        sb.reverse();
        if(prefix != null) sb.insert(0, prefix);
        return sb.toString();
    }

    public static String binaryString(long aQWord)
    {
        return binaryString(aQWord, null);
    }

    public static String binaryString(long aQWord, boolean prefix)
    {
        return binaryString(aQWord, prefix ? "b0" : null);
    }

    public static String binaryString(long aQWord, String prefix)
    {
        StringBuilder sb = new StringBuilder(QWORD_BITS);
        do {
            System.out.println("" + aQWord + " " + (aQWord >> 1) + " " + (aQWord >>> 1));
            sb.append((aQWord & 1L) == 1L ? "1" : "0");
            aQWord >>>= 1;
        } while(aQWord != 0);
        sb.reverse();
        if(prefix != null) sb.insert(0, prefix);
        return sb.toString();
    }

    public static int getByte(int n, int byteInex) throws IndexOutOfBoundsException
    {
        if(byteInex < 0 || byteInex >= 4) {
            throw new IndexOutOfBoundsException("Invalid BYTE: " + byteInex);
        }

        return (n >> (BYTE_BITS * byteInex)) & INT_BYTE_MASK;
    }

    public static long getByte(long n, int byteInex) throws IndexOutOfBoundsException
    {
        if(byteInex < 0 || byteInex >= 8) {
            throw new IndexOutOfBoundsException("Invalid BYTE: " + byteInex);
        }

        return (n >> (BYTE_BITS * byteInex)) & LONG_BYTE_MASK;
    }

    public static long getWord(long n, int wordIndex) throws IndexOutOfBoundsException
    {
        if(wordIndex < 0 || wordIndex >= 4) {
            throw new IndexOutOfBoundsException("Invalid WORD: " + wordIndex);
        }

        return (n >> (WORD_BITS * wordIndex)) & LONG_WORD_MASK;
    }

    public static int getWord(int n, int wordIndex) throws IndexOutOfBoundsException
    {
        if(wordIndex < 0 || wordIndex >= 2) {
            throw new IndexOutOfBoundsException("Invalid WORD: " + wordIndex);
        }

        return (n >> (WORD_BITS * wordIndex)) & INT_WORD_MASK;
    }

    public static long getDWord(long n, int dWordIndex) throws IndexOutOfBoundsException
    {
        if(dWordIndex < 0 || dWordIndex >= 2) {
            throw new IndexOutOfBoundsException("Invalid DWORD: " + dWordIndex);
        }

        return (n >> (DWORD_BITS * dWordIndex)) & LONG_DWORD_MASK;
    }

    public static long highBit(long n)
    {
        n |= (n >>>  1);
        n |= (n >>>  2);
        n |= (n >>>  4);
        n |= (n >>>  8);
        n |= (n >>> 16);
        n |= (n >>> 32);
        return n - (n >>> 1);
    }

    public static int highBit(int n)
    {
        n |= (n >>>  1);
        n |= (n >>>  2);
        n |= (n >>>  4);
        n |= (n >>>  8);
        n |= (n >>> 16);
        return n - (n >>> 1);
    }

    public static short highBit(short n)
    {
        n |= (n >>>  1);
        n |= (n >>>  2);
        n |= (n >>>  4);
        n |= (n >>>  8);
        return (short)(n - (n >>> 1));
    }

    public static byte highBit(byte n)
    {
        n |= (n >>>  1);
        n |= (n >>>  2);
        n |= (n >>>  4);
        return (byte)(n - (n >>> 1));
    }
}
