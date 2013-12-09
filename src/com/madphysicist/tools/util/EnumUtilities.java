/*
 * EnumUtilities.java
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
 * A utility library for manipulating enums.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 20 June 2013
 * @since 1.0.0
 */
public class EnumUtilities
{
    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private EnumUtilities() {}


    /**
     * Converts an enum name into a more user friendly representation. This
     * method assumes that the enum name follows the all-caps with underscores
     * naming convention for enums. All underscores are replaced with spaces and
     * all letters except the first letter of every resulting word are converted
     * to lowercase.
     *
     * @param <E> the type of {@code enum} being passed in.
     * @param enm the enum whose name is to be to converted.
     * @return the formatted user-friendly version of the enum's name.
     * @since 1.0.0
     */
    public static <E extends Enum<?>> String nameToString(E enm)
    {
        return nameToString(enm.name());
    }

    /**
     * Converts an enum name into a more user friendly representation. This
     * method assumes that the enum name follows the all-caps with underscores
     * naming convention for enums. All underscores are replaced with spaces and
     * all letters except the first letter of every resulting word are converted
     * to lowercase.
     *
     * @param name the enum name to convert.
     * @return the formatted user-friendly version of the name.
     * @since 1.0.0
     */
    public static String nameToString(String name)
    {
        char[] chars = name.replace('_', ' ').toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(i > 0 && chars[i - 1] != ' ')
                chars[i] = Character.toLowerCase(chars[i]);
        }
        return new String(chars);
    }
}
