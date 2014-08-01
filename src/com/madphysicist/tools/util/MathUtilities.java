/*
 * MathUtilities.java (Class: com.madphysicist.tools.util.MathUtilities)
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A utility library containing math-related functions.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 01 Aug 2014 - J. Fox-Rabinovitz: Initial coding.
 * @since 1.5
 */
public class MathUtilities
{
    /**
     * An interface used by {@link MathUtilities#generatePrimes(PrimeListener)} to report that a new prime has been
     * found in the sequence.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 01 Aug 2014 - J. Fox-Rabinovitz: Initial coding.
     * @since 1.0.0
     */
    public static interface PrimeListener
    {
        /**
         * Processes the latest prime. A list of all primes found is supplied for reference as well.
         *
         * @param currentPrime the prime being reported. This is a convenience refrence to the last element of  {@code
         * primesToDate}.
         * @param primesToDate an unmodifiable list of all the primes that have been found up to this point, in
         * ascending order.
         * @since 1.0.0
         */
        public void primeFound(BigInteger currentPrime, List<BigInteger> primesToDate);
    }

    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private MathUtilities() {}

    /**
     * Determines whether each number from 1 to {@code max} is a prime. The results are returned as a series of bit
     * flags, with each byte containing the flags for eight numbers. To determine if a number N is prime, use the
     * following expression:
     * <pre>
     * (array[(N - 1) / 8] & ((byte)1 << ((N - 1) % 8))) != 0
     * </pre>
     * Values for {@code N > max} are undefined, even if the number of bits in the array is greater than {@code max}.
     * This method uses a sieve of Eratosthenes to find primes. It has a fixed memory footprint. 
     *
     * @param max the largest number to search up to (inclusive). This does not necessarily have to be a multiple of
     * eight.
     * @return an array of at least {@code max} bit flags determining which numbers are prime and which are not. Bits
     * set to 1 indicate prime numbers. 
     * @since 1.0.0
     */
    public static byte[] generatePrimes(int max)
    {
        byte[] flags = new byte[max / 8 + ((max % 8) == 0 ? 0 : 1)];
        Arrays.fill(flags, (byte)0xFF);
        flags[0] &= ~(byte)1;
        for(int p = 2; p <= max; p++) {
            int q = p - 1;
            if((flags[q / 8] & ((byte)1 << (q % 8))) == 0) {
                continue;
            }
            for(int s = q + p; s < max; s += p) {
                flags[s / 8] &= ~(byte)((byte)1 << (s % 8));
            }
        }
        return flags;
    }

    /**
     * Continuously generates primes until this thread is interrupted. New primes are reported to the specified
     * listener. This method uses an inverse sieve of Eratosthenes. Every successive number is checked for divisibility
     * by each of the primes found so far. This method is not the fastest or most efficient way to find primes, and it
     * has a memory footprint that increases linearly with the number of primes found. The only advantage it has is that
     * it can continue indefinitely. The only practical limits are the size of available memory and the largest linked
     * list size supported in Java. 
     *
     * @param listener a listener that is invoked whenever a prime number is discovered.
     * @since 1.0.0
     */
    public static void generatePrimes(PrimeListener listener)
    {
        List<BigInteger> internalPrimes = new LinkedList<>();
        List<BigInteger> externalPrimes = Collections.unmodifiableList(internalPrimes);

        BigInteger currentPrime = BigInteger.ONE;
        boolean isPrime;
        while(true) {
            currentPrime = currentPrime.add(BigInteger.ONE);
            isPrime = true;
            for(BigInteger pastPrime : internalPrimes) {
                if(currentPrime.mod(pastPrime).equals(BigInteger.ZERO)) {
                    isPrime = false;
                    break;
                }
            }
            if(isPrime) {
                internalPrimes.add(currentPrime);
                listener.primeFound(currentPrime, externalPrimes);
            }
        }
    }

    /**
     * Runs a demo of the {@code generatePrimes()} methods. If the first command-line argument exists and is parseable
     * as an integer, {@link #generatePrimes(int)} is invoked with the argument as the limit. In all other cases, {@link
     * #generatePrimes(PrimeListener)} is invoked with a listener that prints the primes as they are found. In this
     * later case, the program will run indefinitely until it runs out of resources or is terminated externally.
     *
     * @param args the command line arguments. If there is at least one argument and the first one is an integer, it
     * will be interpreted as the upper limit on the primes to generate. Otherwise, the program will run until it is
     * terminated or runs out of resources.
     * @since 1.0.0
     */
    public static void main(String[] args)
    {
        int max;

        if(args.length == 0) {
            max = 0;
        } else {
            try {
                max = Integer.parseInt(args[0]);
            } catch(NumberFormatException nfe) {
                System.out.println("Unable to parse " + args[0] + " as integer.");
                max = 0;
            }
        }

        if(max > 0) {
            System.out.println("Generating primes up to " + max);
            byte[] values = generatePrimes(max);
            System.out.println("Allocated " + values.length + " bytes (" + (values.length * 8 - max) + " extra bits)");
            for(int i = 0; i < max; i++) {
                int flag = values[i / 8] & ((byte)1 << (i % 8));
                if(flag != 0)
                    System.out.println("Found prime: " + (i + 1));
            }
        } else {
            System.out.println("Generating primes indefinitely");
            generatePrimes(new PrimeListener() {
                @Override public void primeFound(BigInteger currentPrime, List<BigInteger> primesToDate) {
                    System.out.println("Found new prime: " + currentPrime);
                }
            });
        }
    }
}
