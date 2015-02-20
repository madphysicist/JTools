/*
 * TextUtilities.java (Class: com.madphysicist.tools.util.TextUtilities)
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @brief A utility library for performing and inverting operations on strings,
 * arrays of strings and maps of strings.
 *
 * This class contains utilities for converting maps of strings to strings and
 * back, the escaping and un-escaping of characters in a string, creating maps
 * from arrays of string pairs, retrieving all the keys in a map as an array of
 * strings, etc.
 *
 * <a name="Properties"><b>Property Lists.</b></a> This class provides a number
 * of methods for manipulating property lists such as by converting them to maps
 * and back. A property list is a string array with an even number of elements,
 * each pair of which represents a property name followed by its value. The even
 * indices in the array contain the names while the odd indices that follow them
 * contain the corresponding values. This is very similar to the arrangement
 * used in MATLAB to define object properties. Since they represent similar
 * concepts, a property list can be converted to and from a map using the
 * `propertiesToMap()` and `mapToProperties()` methods. These methods are useful
 * for creating copies of maps that can be safely modified without affecting the
 * original as well as quickly initializing maps from lists of strings.
 *
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 04 Mar 2012 - J. Fox-Rabinovitz: Initial coding.
 * @version 1.0.1, 04 Feb 2014 - J. Fox-Rabinovitz: Added countOccurences().
 * @version 2.0.0, 22 Jun 2014 - J. Fox-Rabinovitz: Moved nowStr(),
 *          simpleNowStr(), timeStr() and simpleTimeStr() into new class
 *          TimeUtilities.
 * @version 3.0.0, 11 Jan 2015 - J. Fox-Rabinovitz: Removed mapEquals() and
 *          added support for maps containing arrays.
 * @since 1.0
 */
public class TextUtilities
{
    /**
     * @brief The default `java.nio.Charset` of all the text operations.
     *
     * This character set is intended to be used for consistency in all I/O
     * operations.
     *
     * @since 1.0.0
     */
    public static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * @breif Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private TextUtilities() {}

    /**
     * @brief Returns the keys in a string map in an array.
     *
     * If the map is `null`, `null` is returned. No guarantee is made about the
     * order of the returned array.
     *
     * @param aMap A map keyed by strings.
     * @return The keys in the map, or `null` if `aMap` is `null`.
     * @since 1.0.0
     */
    public static String[] keyList(Map<String, ?> aMap)
    {
        if(aMap == null) return null;
        Set<String> keys = aMap.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * @brief Converts a map into a string.
     *
     * The map may contain strings and arrays. All other value types (including
     * non-string array elements) will be converted to strings using their
     * `toString()` method. This method does not currently support primitive or
     * nested arrays.
     *
     * The return value has the following format:
     *
     *      <name><prefix><escaped key><keyValueSeparator><escaped value><entrySeparator> ... <suffix>
     *
     * The entry separator does not appear after the last key-value element.
     * Array values have the following format:
     *
     *      <arrayPrefix><escaped element><arraySeparator> ... <arraySuffix>
     *
     * The array separator does not appear after the last element.
     *
     * The name, keys and values will have all characters that appear in
     * `anEscapeChars` escaped by preceding them with the escape symbol. The
     * prefix, suffix, key-value separator and entry separator will not be
     * escaped. The recommended use of this method is to add the symbols in the
     * prefix, suffix, array delimiters and separators to the escape characters
     * so that they can be identified unambiguously.
     *
     * @param aName The name of the map. If `null`, no name is prefixed.
     * @param aMap The map to convert. If `null` or empty, only the name, prefix
     * and suffix are concatenated.
     * @param aPrefix The prefix to insert before listing keys and values.
     * Omitted entirely if `null`.
     * @param aSuffix The suffix to append to the end of the string. Omitted
     * entirely if `null`.
     * @param aKeyValueSeparator The character sequence to separate keys from
     * values in the result. Defaults to a single space (' ') if `null`.
     * @param anEntrySeparator The character sequence to separate key-value
     * pairs from other entries in the result. Defaults to a single space (' ')
     * if `null`.
     * @param anArrayPrefix The character sequence that defines the beginning of
     * an array value. Omitted entirely if `null`.
     * @param anArraySuffix The character sequence that defines the end of an
     * array value. Omitted entirely if `null`.
     * @param anArraySeparator The character sequence to separate elements in an
     * array value. Defaults to a single space (' ') if `null`.
     * @param anEscapeChars A string containing the characters that need to be
     * escaped in the name, key and value strings. Note that characters in
     * `aKeyValueSeparator` and `entrySeparator` are not escaped. No escaping is
     * done if `null` or empty.
     * @param anEscapeSymbol The symbol to be inserted before characters that
     * need to be escaped. May be used to escape itself.
     * @return A string representation of the input map. This value is never
     * `null`, although it may be empty if the map, name, prefix and suffix are
     * all empty or `null`.
     * @see #arrayToString()
     * @see #propertiesToString()
     * @see #stringToMap()
     * @since 1.0.0
     */
    public static String mapToString(String aName, Map<String, Object> aMap,
                                     String aPrefix, String aSuffix,
                                     String aKeyValueSeparator,
                                     String anEntrySeparator,
                                     String anArrayPrefix, String anArraySuffix,
                                     String anArraySeparator,
                                     String anEscapeChars, char anEscapeSymbol)
    {
        StringBuilder sb = new StringBuilder();
        if(aName != null) {
            sb.append(escapeString(aName, anEscapeChars, anEscapeSymbol));
        }
        if(aPrefix != null) {
            sb.append(aPrefix);
        }
        if(aMap != null && !aMap.isEmpty()) {
            if(aKeyValueSeparator == null)  aKeyValueSeparator = " ";
            if(anEntrySeparator == null)    anEntrySeparator = " ";

            // indicates that the loop is starting
            boolean first = true;
            for(Map.Entry<String, Object> entry : aMap.entrySet()) {
                if(first) {
                    // once the loop has started, the next iteration won't be first
                    first = false;
                } else {
                    // if not on the first iteration, prepend a separator
                    sb.append(anEntrySeparator);
                }
                sb.append(escapeString(entry.getKey(), anEscapeChars,
                                       anEscapeSymbol));
                sb.append(aKeyValueSeparator);
                Object value = entry.getValue();
                if(value instanceof Object[]) {
                    sb.append(arrayToString((Object[])value,
                            anArrayPrefix, anArraySuffix, anArraySeparator,
                            anEscapeChars, anEscapeSymbol));
                } else {
                    sb.append(escapeString(value.toString(),
                            anEscapeChars, anEscapeSymbol));
                }
            }
        }
        if(aSuffix != null) {
            sb.append(aSuffix);
        }
        return sb.toString();
    }

    /**
     * @brief Parses a map encoded with `mapToString()` or equivalent.
     *
     * Special attention is paid to elements with escape characters
     * in them. The expected format of the string is:
     *
     *      <aName><aMapPrefix><escaped key><aKeyValueSeparator><escaped value><anEntrySeparator> ... <aMapSuffix>
     *
     * Note that if keys are repeated, the values will be silently replaced. The
     * case in which there are no characters between `aMapPrefix` and
     * `aMapSuffix` is treated specially as an empty map.
     *
     * @param aString The string to decode. This parameter may not be `null`.
     * It may only be empty is both `mapPrefix` and `mapSuffix` are empty.
     * @param aMap The map to fill with the decoded values. The map does not
     * have to be empty, but duplicate keys will be silently replaced. If this
     * parameter is `null`, only the name is extracted from the string and
     * returned. This map must be modifiable and will only have `String` and
     * `String[]` values added to it.
     * @param aMapPrefix The map prefix string. Note that escape characters do
     * **not** appear in the prefix. This string may not be `null`. If the
     * prefix is empty, the map will have an empty name string.
     * @param aMapSuffix The map suffix string. Note that escape characters do
     * **not** appear in the suffix. This string may not be `null` but may be
     * empty. The input string must end with this sequence exactly.
     * @param aKeyValueSeparator The string that separates keys from values.
     * This string may be neither `null` nor empty.
     * @param anEntrySeparator The string that separates successive key-value
     * pairs. This string may be neither `null` nor empty.
     * @param anEscapeChars A string containing a list of characters that may be
     * escaped in the input. This parameter may be `null` to indicate that all
     * characters preceded by `anEscapeSymbol` are escaped, or empty to indicate
     * that there are no escape characters.
     * @param anEscapeSymbol A symbol used to escape special characters, e.g.
     * the prefix, suffix and separators within the keys and values.
     * @return The name of the map, or an empty `String` if none is specified.
     * @throws NullPointerException if any of the arguments except `aMap` or
     * `anEscapeChars` is `null`.
     * @throws IllegalArgumentException if either `aString`,
     * `aKeyValueSeparator` or `anEntrySeparator` is empty.
     * @throws StringIndexOutOfBoundsException if `aMapPrefix` is not found,
     * `aString` does not end with `aMapSuffix`, or an entry does not have a
     * valid key-value separator. The message indicates which condition caused
     * the exception.
     * @see mapToString()
     * @since 1.0.0
     */
    public static String stringToMap(String aString, Map<String, String> aMap,
                                     String aMapPrefix, String aMapSuffix,
                                     String aKeyValueSeparator,
                                     String anEntrySeparator,
                                     String anEscapeChars, char anEscapeSymbol)
    {
        // preprocess (calling isEmpty on null will handle throwing the NPEs)
        if(aString.isEmpty()) {
            if(aMapPrefix.isEmpty() && aMapSuffix.isEmpty())
                return new String();
            throw new IllegalArgumentException("map string empty");
        }
        if(anEntrySeparator.isEmpty()) {
            throw new IllegalArgumentException("entry separator empty");
        }
        if(aKeyValueSeparator.isEmpty()) {
            throw new IllegalArgumentException("key-value separator empty");
        }
        // find prefix
        int prefixIndex = nextIndexOf(aString, aMapPrefix, 0, anEscapeChars, anEscapeSymbol);
        if(prefixIndex < 0) {
            throw new StringIndexOutOfBoundsException("map prefix missing");
        }
        // check for suffix
        int suffixIndex = (aMapSuffix.isEmpty()) ? aString.length() :
                          nextIndexOf(aString, aMapSuffix, aString.length() - aMapSuffix.length(),
                                      anEscapeChars, anEscapeSymbol);
        if(suffixIndex < 0) {
            throw new StringIndexOutOfBoundsException("map suffix missing");
        }
        String name = aString.substring(0, prefixIndex);
        if(aMap != null) {
            aString = aString.substring(prefixIndex + aMapPrefix.length(), suffixIndex);
            // only process if the string contains a map between the prefix and suffix
            if(!aString.isEmpty()) {
                int prev = 0;
                for(int index = 0; (index = nextIndexOf(aString, anEntrySeparator, prev, anEscapeChars, anEscapeSymbol)) >= prev;
                    prev = index + anEntrySeparator.length()) {
                    addMapEntry(aString.substring(prev, index), aMap, aKeyValueSeparator, anEscapeChars, anEscapeSymbol);
                }
                addMapEntry(aString.substring(prev), aMap, aKeyValueSeparator, anEscapeChars, anEscapeSymbol);
            }
        }
        return unescapeString(name, anEscapeChars, anEscapeSymbol);
    }

    /**
     * @brief Parses a map encoded with `mapToString()` or equivalent.
     *
     * Special attention is paid to elements with escape characters
     * in them. The expected format of the string is:
     *
     *      <name><mapPrefix><escaped key><keyValueSeparator><escaped value><entrySeparator> ... <mapSuffix>
     *
     * Note that if keys are repeated, the values will be silently replaced. The
     * case in which there are no characters between `mapPrefix` and `mapSuffix`
     * is treated specially as an empty map. While keys are always interpreted
     * as strings, values may be interpreted as either strings or arrays of
     * strings. Array values must follow the following format:
     *
     *      <arrayPrefix><escaped element><arraySeparator> ... <arraySuffix>
     *
     * `arraySeparator` does not appear after the last element. Empty arrays
     * are allowed, as long as they begin with `arrayPrefix`, end with
     * `arraySuffix`, and contain nothing in between. If `arrayPrefix` and
     * `arraySuffix` are empty, empty values will not be treated as arrays.
     *
     * @param aString The string to decode. This parameter may not be `null`.
     * It may only be empty is both `mapPrefix` and `mapSuffix` are empty.
     * @param aMap The map to fill with the decoded values. The map does not
     * have to be empty, but duplicate keys will be silently replaced. If this
     * parameter is `null`, only the name is extracted from the string and
     * returned. This map must be modifiable and will only have `String` and
     * `String[]` values added to it.
     * @param aMapPrefix The map prefix string. Note that escape characters do
     * **not** appear in the prefix. This string may not be `null`. If the
     * prefix is empty, the map will have an empty name string.
     * @param aMapSuffix The map suffix string. Note that escape characters do
     * **not** appear in the suffix. This string may not be `null` but may be
     * empty. The input string must end with this sequence exactly.
     * @param aKeyValueSeparator The string that separates keys from values.
     * This string may be neither `null` nor empty.
     * @param anEntrySeparator The string that separates successive key-value
     * pairs. This string may be neither `null` nor empty.
     * @param anArrayPrefix The character sequence that defines the beginning of
     * an array value. May be empty but not `null`.
     * @param anArraySuffix The character sequence that defines the end of an
     * array value. May be empty but not `null`.
     * @param anArraySeparator The character sequence to separate elements in an
     * array value. May not be `null` or empty.
     * @param anEscapeChars A string containing a list of characters that may be
     * escaped in the input. This parameter may be `null` to indicate that all
     * characters preceded by `anEscapeSymbol` are escaped, or empty to indicate
     * that there are no escape characters.
     * @param anEscapeSymbol A symbol used to escape special characters, e.g.
     * the prefix, suffix and separators within the keys and values.
     * @return The name of the map, or an empty `String` if none is specified.
     * @throws NullPointerException if any of the arguments except `aMap` or
     * `anEscapeChars` is `null`.
     * @throws IllegalArgumentException if either `aString`,
     * `aKeyValueSeparator` or `anEntrySeparator` is empty. `anArraySeparator`
     * may not be empty either, but the exception is only thrown if arrays are
     * actually found in `aString`.
     * @throws StringIndexOutOfBoundsException if `aMapPrefix` is not found,
     * `aString` does not end with `aMapSuffix`, or an entry does not have a
     * valid key-value separator. The message indicates which condition caused
     * the exception.
     * @see mapToString()
     * @since 3.0.0
     */
    public static String stringToMap(String aString, Map<String, Object> aMap,
                                     String aMapPrefix, String aMapSuffix,
                                     String aKeyValueSeparator, String anEntrySeparator,
                                     String anArrayPrefix, String anArraySuffix,
                                     String anArraySeparator,
                                     String anEscapeChars, char anEscapeSymbol)
    {
        // preprocess (calling isEmpty on null will handle throwing the NPEs)
        if (aString.isEmpty()) {
            if (aMapPrefix.isEmpty() && aMapSuffix.isEmpty())
                return new String();
            throw new IllegalArgumentException("map string empty");
        }
        if (anEntrySeparator.isEmpty()) {
            throw new IllegalArgumentException("entry separator empty");
        }
        if (aKeyValueSeparator.isEmpty()) {
            throw new IllegalArgumentException("key-value separator empty");
        }

        // find prefix
        int prefixIndex = nextIndexOf(aString, aMapPrefix, 0, anEscapeChars, anEscapeSymbol);
        if (prefixIndex < 0) {
            throw new StringIndexOutOfBoundsException("map prefix missing");
        }
        // check for suffix
        int suffixIndex = (aMapSuffix.isEmpty()) ? aString.length() : nextIndexOf(aString, aMapSuffix, aString.length()
                - aMapSuffix.length(), anEscapeChars, anEscapeSymbol);
        if (suffixIndex < 0) {
            throw new StringIndexOutOfBoundsException("map suffix missing");
        }
        String name = aString.substring(0, prefixIndex);
        if (aMap != null) {
            aString = aString.substring(prefixIndex + aMapPrefix.length(), suffixIndex);
            // only process if the string contains a map between the prefix and
            // suffix
            if (!aString.isEmpty()) {
                int prev = 0;
                for (int index = 0; (index = nextIndexOf(aString, anEntrySeparator,
                                                         prev, anEscapeChars, anEscapeSymbol)) >= prev;
                                    prev = index + anEntrySeparator.length()) {
                    addMapEntry(aString.substring(prev, index),
                                aMap, aKeyValueSeparator,
                                anArrayPrefix, anArraySuffix, anArraySeparator,
                                anEscapeChars, anEscapeSymbol);
                }
                addMapEntry(aString.substring(prev), aMap, aKeyValueSeparator,
                            anArrayPrefix, anArraySuffix, anArraySeparator,
                            anEscapeChars, anEscapeSymbol);
            }
        }
        return unescapeString(name, anEscapeChars, anEscapeSymbol);
    }

    /**
     * @brief Converts an array of objects into a string.
     *
     * Non-string elements will be converted to strings using their `toString()`
     * method. This method does not currently support primitive or nested
     * arrays.
     *
     * The return value has the following format:
     *
     *      <arrayPrefix><escaped element><arraySeparator> ... <arraySuffix>
     *
     * The array separator does not appear after the last element.
     *
     * Elements will have all characters that appear in `anEscapeChars` escaped
     * by preceding them with the escape symbol. The prefix, suffix and
     * separator and entry separator will not be escaped. The recommended use of
     * this method is to add the symbols in the prefix, suffix and separator to
     * the escape characters so that they can be identified unambiguously.
     *
     * To convert primitive arrays to arrays of string objects that can be
     * processed by this method, use the appropriate methods of the
     * `ArrayUtilities` class.
     *
     * @param anArray The array to convert. If `null` or empty, only the prefix
     * and suffix are concatenated.
     * @param aPrefix The prefix to delimit the beginning of the array with.
     * Omitted entirely if `null`.
     * @param aSuffix The suffix to delimit the end of the array with. Omitted
     * entirely if `null`.
     * @param aSeparator The character sequence to separate elements in the
     * array with. Defaults to a single space (' ') if `null`.
     * @param anEscapeChars A string containing the characters that need to be
     * escaped in the elements. Note that characters in the prefix, suffix and
     * separator are not escaped. No escaping is done if `null` or empty.
     * @param anEscapeSymbol The symbol to be inserted before characters that
     * need to be escaped. May be used to escape itself.
     * @return A string representation of the array. This value is never `null`,
     * although it may be empty if the array, prefix and suffix are empty.
     * @see ArrayUtilities
     * @since 3.0.0
     */
    public static String arrayToString(Object[] anArray,
            String aPrefix, String aSuffix, String aSeparator,
            String anEscapeChars, char anEscapeSymbol)
    {
        if(aPrefix    == null)    aPrefix = "";
        if(aSeparator == null) aSeparator = " ";
        if(aSuffix    == null)    aSuffix = "";

        if(anArray == null || anArray.length == 0) return aPrefix + aSuffix;

        StringBuilder sb = new StringBuilder();

        sb.append(aPrefix);
        for(int i = 0; i < anArray.length; i++) {
            if(i > 0) sb.append(aSeparator);
            sb.append(escapeString(anArray[i].toString(), anEscapeChars, anEscapeSymbol));
        }
        sb.append(aSuffix);
        return sb.toString();
    }

    /**
     * @brief Converts a string containing delimited array elements into an
     * array of strings.
     *
     * This method is the approximate inverse of `arrayToString()`. The expected
     * string format is
     *
     *     <arrayPrefix><escaped element><arraySeparator> ... <arraySuffix>
     *
     * Special attention is paid to elements with escape characters in them. The
     * case in which there are no characters between `arrayPrefix` and
     * `arraySuffix` is treated specially as an empty array.
     *
     * To convert the result into primitive arrays, use the appropriate
     * methods of the `ArrayUtilities` class.
     * 
     * @param aString The string to decode. This parameter may not be `null`. It
     * may only be empty is both `aPrefix` and `aSuffix` are empty.
     * @param aPrefix The prefix string. Escape characters do **not** appear in
     * the prefix. This string may not be `null`, but may be empty.
     * @param aSuffix The suffix string. Escape characters do **not** appear in
     * the suffix. This string may not be `null` but may be empty.
     * @param aSeparator The string that separates successive elements in the
     * array. This string may not be `null` or empty.
     * @param anEscapeChars A string containing a list of characters that may be
     * escaped in the input. This parameter may be `null` to indicate that all
     * characters preceded by an escape symbol are escaped, or empty to indicate
     * that there are no escape characters.
     * @param anEscapeSymbol A symbol used to escape special characters. If
     * `anEscapeChars` is empty, this parameter is ignored.
     * @return The input string split into an array of stings with the prefix,
     * suffix and separators discarded. The return value may be empty, but will
     * never be `null`.
     * @throws NullPointerException if any of the arguments except {@code map}
     * or {@code escapeChars} is {@code null}.
     * @throws IllegalArgumentException if either `aString` or `aSeparator` is
     * empty.
     * @throws StringIndexOutOfBoundsException if no valid prefix is found or no
     * valid suffix is found. The message indicates which condition caused the
     * exception.
     * @see arrayToString()
     * @since 3.0.0
     */
    public static String[] stringToArray(String aString,
                            String aPrefix, String aSuffix, String aSeparator,
                            String anEscapeChars, char anEscapeSymbol)
    {
        // pre-process (calling isEmpty on null will handle throwing the NPEs)
        if(aString.isEmpty()) {
            if(aPrefix.isEmpty() && aSuffix.isEmpty())
                return new String[0];
            throw new IllegalArgumentException("array string empty");
        }
        if(aSeparator.isEmpty()) {
            throw new IllegalArgumentException("array separator empty");
        }
        // find prefix
        int prefixIndex = nextIndexOf(aString, aPrefix, 0, anEscapeChars, anEscapeSymbol);
        if(prefixIndex != 0) {
            throw new StringIndexOutOfBoundsException("array prefix missing");
        }
        // check for suffix
        int suffixIndex = (aSuffix.isEmpty()) ? aString.length() :
                          nextIndexOf(aString, aSuffix, aString.length() - aSuffix.length(),
                                      anEscapeChars, anEscapeSymbol);
        if(suffixIndex < 0) {
            throw new StringIndexOutOfBoundsException("array suffix missing");
        }
        String string = aString.substring(aPrefix.length(), suffixIndex);

        // only process if the string contains a map between the prefix and suffix
        if(string.isEmpty()) return new String[0];
        List<String> list = new ArrayList<>();
        int prev = 0;
        for(int index = 0; (index = nextIndexOf(string, aSeparator, prev, anEscapeChars, anEscapeSymbol)) >= prev;
                           prev = index + aSeparator.length()) {
            list.add(unescapeString(string.substring(prev, index), anEscapeChars, anEscapeSymbol));
        }
        list.add(unescapeString(string.substring(prev), anEscapeChars, anEscapeSymbol));
        return list.toArray(new String[list.size()]);
    }

    /**
     * Converts a property list into a string. This is useful for sending {@code
     * Message} objects across a network. The returned string has the format
     * {@code
     * <name><prefix><escaped key><keyValueSeparator><escaped value><entrySeparator>
     * ... <suffix>}. The entry separator does not appear after the last
     * key-value element. Keys and values will have all characters that appear
     * in {@code escapeChars} escaped by preceding them with the escape symbol.
     * The property list must contain an even number of elements. See the
     * <a href="#Properties">class description</a> for more about property lists.
     *
     * @param name the name of the property list. If null, no name is prefixed.
     * @param properties the properties list to convert. If null or empty, only
     * the name, prefix and suffix are concatenated.
     * @param prefix the prefix to insert before listing property names and
     * values. Omitted entirely if null.
     * @param suffix the suffix to append to the end of the string. Omitted
     * entirely if null.
     * @param keyValueSeparator the character sequence to separate property
     * names from values in the result. Defaults to a single space (' ') if null.
     * @param entrySeparator the character sequence to separate property pairs
     * from other properties in the result. Defaults to a single space (' ') if
     * null.
     * @param escapeChars the characters that need to be escaped in the list
     * name, property name and property value strings. Note that characters in
     * {@code keyValueSeparator} and {@code entrySeparator} are not escaped. No
     * escaping is done if null or empty.
     * @param escapeSymbol the symbol to be inserted before characters that
     * need to be escaped.
     * @return a string representation of the input map. This value is never
     * null.
     * @throws ArrayIndexOutOfBoundsException if the property list does not have
     * an even number of elements.
     * @see #mapToString
     * @since 1.0.0
     */
    public static String propertiesToString(String name, String[] properties,
                                            String prefix, String suffix,
                                            String keyValueSeparator,
                                            String entrySeparator,
                                            String escapeChars, char escapeSymbol)
    {
        if(properties != null && properties.length % 2 != 0) {
            throw new ArrayIndexOutOfBoundsException("" + properties.length +
                                                     " % 2 != 0");
        }
        StringBuilder sb = new StringBuilder();
        if(name != null) {
            sb.append(escapeString(name, escapeChars, escapeSymbol));
        }
        if(prefix != null) {
            sb.append(prefix);
        }
        if(properties != null && properties.length > 0) {
            if(keyValueSeparator == null) {
                keyValueSeparator = " ";
            }
            if(entrySeparator == null) {
                entrySeparator = " ";
            }
            for(int i = 0; i < properties.length; i += 2) {
                if(i > 0)
                    sb.append(entrySeparator);
                sb.append(escapeString(properties[i], escapeChars, escapeSymbol));
                sb.append(keyValueSeparator);
                sb.append(escapeString(properties[i + 1], escapeChars, escapeSymbol));
            }
        }
        if(suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * Escapes the specified characters of a string by prepending an escape
     * symbol. Only one pass is made through the string, so that the escape
     * symbol may safely be present in the list of characters to escape.
     *
     * @param string the string to escape.
     * @param escapeChars the characters to escape. Every occurrence of any of
     * the characters in this string is prepended with the escape symbol. If
     * this argument is empty or null, this method does nothing.
     * @param escapeSymbol a symbol used to escape the required characters.
     * @return a string similar to the input but with all of the characters in
     * {@code escapeChars} preceded by an escape sequence
     * @since 1.0.0
     */
    public static String escapeString(String string,
                                      String escapeChars,
                                      char escapeSymbol)
    {
        if(string == null || string.isEmpty() ||
                escapeChars == null || escapeChars.isEmpty()) {
            return string;
        }
        StringBuilder sb = new StringBuilder(string.length());
        for(int index = 0; index < string.length(); index++) {
            char ch = string.charAt(index);
            if(escapeChars.indexOf(ch) >= 0)
                sb.append(escapeSymbol);
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Removes occurrences of an escape character from a string. If a list of
     * escape characters is specified, only the characters in that list are
     * regarded as escape sequences when preceded by an escape symbol.
     *
     * @param string the string to process.
     * @param escapeChars the characters that are to be unescaped. If null, all
     * characters preceded by the escape symbol will be escaped. If empty, no
     * escape characters are unescaped.
     * @param escapeSymbol the symbol that appears before escaped characters.
     * @return the string with all escape sequences eliminated.
     * @since 1.0.0
     */
    public static String unescapeString(String string,
                                        String escapeChars,
                                        char escapeSymbol)
    {
        if(string == null || string.isEmpty() ||
                (escapeChars != null && escapeChars.isEmpty())) {
            return string;
        }
        StringBuilder sb = new StringBuilder(string.length());
        for(int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if(c == escapeSymbol && i < string.length() - 1) {
                char c2 = string.charAt(i + 1);
                if(escapeChars == null || escapeChars.indexOf(c2) >= 0) {
                    c = c2;
                    i++;
                }
            }
            sb.append(c);
        }
        string = sb.toString();
        return sb.toString();
    }

    /**
     * Converts a property list into a map. The array is required to have
     * an even number of elements. Each sequential pair of elements is assumed
     * to be a key followed by a value, similarly to property lists in MATLAB.
     * See the <a href="#Properties">class description</a> for more about
     * property lists. The map will be null if the input is null, and empty if
     * the input is empty.
     *
     * @param properties an array of strings to convert
     * @return a map in which the keys are the elements in the array with even
     * indices and the values are the elements with odd indices, or null if
     * {@code properties} is null.
     * @throws ArrayIndexOutOfBoundsException if the array does not have an even
     * number of elements
     * @throws NullPointerException if a key in the property list is {@code
     * null}. Values on the other hand may be {@code null}.
     * @since 1.0.0
     */
    public static SortedMap<String, String> propertiesToMap(String... properties)
    {
        if(properties == null) {
            return null;
        }
        if(properties.length % 2 != 0) {
            throw new ArrayIndexOutOfBoundsException("" + properties.length +
                                                     " % 2 != 0");
        }
        TreeMap<String, String> map = new TreeMap<>();
        for(int i = 0; i < properties.length; i += 2) {
            if(properties[i] == null)
                throw new NullPointerException("null key (value=" + properties[i + 1] + ")");
            map.put(properties[i], properties[i + 1]);
        }
        return map;
    }

    /**
     * Converts a map into an array of strings. The array will be a sequence of
     * pair of strings, each consisting of a map key followed by its value. The
     * result is similar to a MATLAB property list. See the
     * <a href="#Properties">class description</a> for more about property lists.
     * The array will be null if the map is null, and empty if the input is
     * empty. The returned property list is not guaranteed to be sorted in any
     * particular order.
     *
     * @param map the map to convert.
     * @return an array representation of the map with keys occupying even
     * indices in the array and corresponding values occupying the successive
     * odd indices.
     * @since 1.0.0
     */
    public static String[] mapToProperties(Map<String, String> map)
    {
        if(map == null)
            return null;
        String[] properties = new String[map.size() * 2];
        int counter = 0;
        for(Map.Entry<String, String> entry : map.entrySet()) {
            properties[counter++] = entry.getKey();
            properties[counter++] = entry.getValue();
        }
        return properties;
    }

    /**
     * <p>
     * Finds the next occurrence of a key in the template string barring escaped
     * characters. This method is similar to {@link String#indexOf(String, int)}
     * except that escaped characters are not considered to be part of the
     * key, even if they occur at the beginning of a matching sequence in the
     * template.
     * </p>
     * <p>
     * For example, if the key is {@code "ABC"} and the template is
     * {@code "\\ABC"} (in java notation, printed as {@code "\ABC"}), the
     * following table illustrates the outcomes:
     * <table border="1">
     * <tr><td><b>Input</b></td><td><b>Result</b></td></tr>
     * <tr><td>{@code start = }anything<br />
     *         {@code escapeChars == null}<br />
     *         {@code escapeSymbol == '\'}</td>
     *     <td>{@code -1}</td></tr>
     * <tr><td>{@code start = }anything<br />
     *         {@code escapeChars != null}, includes {@code 'A'}<br />
     *         {@code escapeSymbol == '\'}</td>
     *     <td>{@code -1}</td></tr>
     * <tr><td>{@code 0 <= start <= 1}<br />
     *         {@code escapeChars != null}, does not include {@code 'A'}<br />
     *         {@code escapeSymbol == '\'}</td>
     *     <td>{@code 1}</td></tr>
     * <tr><td>{@code 0 <= start <= 1}<br />
     *         {@code escapeChars = }anything<br />
     *         {@code escapeSymbol != '\'}</td>
     *     <td>{@code 1}</td></tr>
     * </table>
     * </p>
     *
     * @param template the string to search in. This parameter may not be {@code
     * null}.
     * @param key the string to search for. This parameter may not be {@code
     * null}.
     * @param start the index to start searching from. If a match is found, it
     * will be at an index that is greater than or equal to this value. If this
     * index is greater than the length of the string minus
     * the length of the key, the return value will be {@code -1}.
     * @param escapeChars the characters that can be escaped. If this is null,
     * all characters are escaped if preceded by the escape symbol. If empty,
     * there are no escape characters.
     * @param escapeSymbol the symbol that escapes characters.
     * @return the index of the beginning of the first occurrence of the key in
     * the template after the start index, or {@code -1} if not found.
     * @throws NullPointerException if the template or the key are {@code null}.
     * @since 1.0.0
     */
    public static int nextIndexOf(String template, String key, int start, String escapeChars, char escapeSymbol)
    {
        if(template == null || key == null)
            throw new NullPointerException("null " + (template == null ? "template" : "key"));
        if(start < 0)
            start = 0;
        while(start >= 0) {
            start = template.indexOf(key, start);
            if(start <= 0 || template.charAt(start - 1) != escapeSymbol || // key not found or theres no escape symbol
               (escapeChars != null && escapeChars.indexOf(key.charAt(0)) < 0) || // the first char of key is not an escapable char
               ((escapeChars == null || escapeChars.indexOf(escapeSymbol) >= 0) && // the escape symbol  can be escaped
                    countEscapesBack(template, start - 1, escapeSymbol) % 2 == 0)) { // there are an even number of escape symbols
                break;
            } else {
                start++;
            }
        }
        return start;
    }

    /**
     * Finds the next occurrence of a key in the template string barring escaped
     * characters.  This method is similar to {@link #nextIndexOf(java.lang.String,
     * java.lang.String, int, java.lang.String, char)}, except that it searches for
     * a single character that may be escaped. In this case, the character is
     * assumed to be escapable. The escape symbol can be escapable if required
     * too.
     *
     * @param template the string to search in. This parameter may not be {@code
     * null}.
     * @param key the character to search for.
     * @param start the index to start searching from. If a match is found, it
     * will be at an index that is greater than or equal to this value. If the
     * index is negative, search begins at the start of the template.
     * @param escapeSymbol the symbol that escapes characters.
     * @param symbolEscaped determines whether the escape symbol can be escaped
     * too. If so, an even number of escape characters do not affect what
     * follows them. If not, only the last in any sequence of escape characters
     * will be regarded.
     * @return the index of the first occurrence of the key in
     * the template after the start index, or {@code -1} if not found.
     * @throws NullPointerException if the template is {@code null}.
     * @since 1.0.0
     */
    public static int nextIndexOf(String template, char key, int start, char escapeSymbol, boolean symbolEscaped)
    {
        if(template == null)
            throw new NullPointerException("null template");
        while(true) {
            int index = template.indexOf(key, start);
            if(index < 0)
                return index;
            // check for escapes
            int escapeCount = 0;
            for(int pos = index - 1; pos >= 0 && template.charAt(pos) == escapeSymbol; pos--) {
                escapeCount++;
                // if the escape symbol can not be escaped, exit after the first iteraton
                if(!symbolEscaped)
                    break;
            }
            if(escapeCount % 2 == 0)
                return index;
            start = index + 1;
        }
    }

    /**
     * Escapes all characters that may cause a problem if a string is used
     * directly in HTML. Characters are replaced by their equivalent numerical
     * entities. For example, ampersand ({@code &}) becomes {@code &#038;}
     * rather than {@code &amp;}. The escaped characters are single quote
     * ({@code '}), double quote ({@code "}), ampersand ({@code &}), less than
     * ({@code <}), greater than ({@code >}), pound ({@code #}), semicolon
     * ({@code ;}), and any character whose code is greater than 127.
     *
     * @param string the string to encode.
     * @return the string with all single and double quotes, ampersands, greater
     * than and less than symbols, pound symbols, semicolons and non-ASCII
     * characters transformed into their equivalent HTML numerical entities.
     */
    public static String htmlEncode(String string)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if(c > 127 || c == '\"' || c == '\'' || c == '&' || c == '<' || c == '>' || c == '#' || c == ';')
               sb.append("&#").append((int)c).append(';');
            else
                sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Converts the specified array to a hex string. This method is similar to
     * {@link Integer#toHexString(int)}. Every two digits represent a successive
     * byte. This method is an exact inverse of {@link #fromHexString(String)}.
     * Calling {@code fromHexString(toHexString(bytes))} will return the
     * original sequence of numbers.
     *
     * @param bytes the byte array to encode.
     * @return a hex represenetation of the specified bytes. The length of the
     * string is always exactly twice the length of the input array.
     * @since 1.0.0
     */
    public static String toHexString(byte[] bytes)
    {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for(int i = 0; i < bytes.length; i++) {
            hex.append(String.format("%02X", (bytes[i] & 0xFF)));
        }
        return hex.toString();
    }

    /**
     * Converts the hex string to a byte array. The string must have an even
     * number of digits, each of which must be a valid hexadecimal digit. Every
     * two digits represent a successive byte. This method is an almost exact
     * inverse of {@link #toHexString(byte[])}. Calling {@code
     * toHexString(fromHexString(hexString))} will return the original string,
     * up to the case of the hexadecimal digits.
     *
     * @param hexString the string to decode. Must have an even number of
     * characters, each pair of which represents a hexadecimal byte.
     * @return a numerical representation of the input string.
     * @throws NumberFormatException if the string can not be parsed as a
     * sequence of hexadecimal digits for any reason.
     * @since 1.0.0
     */
    public static byte[] fromHexString(String hexString) throws NumberFormatException
    {
        if(hexString.length() % 2 != 0) {
            throw new NumberFormatException("odd number of digits: " + hexString);
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for(int i = 0; i < bytes.length; i++) {
            char d1 = hexString.charAt(i * 2);
            char d2 = hexString.charAt(i * 2 + 1);
            if(Character.digit(d1, 16) < 0 || Character.digit(d2, 16) < 0) {
                throw new NumberFormatException("not a hex string" + hexString);
            }
            bytes[i] = (byte)(Integer.parseInt("" + d1 + d2, 16) & 0xFF);
        }
        return bytes;
    }

    /**
     * Counts the number of times the specified character appears in the
     * specified string.
     *
     * @param string the string to search in.
     * @param character the character to search for.
     * @return the number of times that the specified character appears in the
     * string. The number will be between zero and the length of the string.
     * @since 1.0.1
     */
    public static int countOccurrences(String string, char character)
    {
        int count = 0;
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == character) {
                count++;
            }
        }
        return count;
    }

    /**
     * @brief Adds an entry to a map by splitting the entry string around the
     * separator.
     *
     * If an non-escaped separator string is not found, an exception
     * is thrown. Only the first non-escaped instance of the separator string
     * will be processed. This means that the value may contain multiple
     * non-escaped instances of the separator string.
     *
     * @param anEntry A string representation of the map entry consisting of a
     * key-value pair delimited by a separator.
     * @param aMap The map to add the entry to.
     * @param aSeparator The separator that delimits the boundary between keys
     * and values. At least one non-escaped occurrence of this string must
     * appear in the entry.
     * @param anEscapeChars The characters that can be escaped by the escape
     * symbol. A `null` string indicates that all characters preceded by
     * `anEscapeSymbol` are escaped. An empty string indicates that there are no
     * escape sequences present, and `anEscapeSymbol` will be ignored.
     * @param anEscapeSymbol A symbol that indicates the start of escape
     * sequences.
     * @throws StringIndexOutOfBoundsException if `aSeparator` does not appear
     * in `anEntry`.
     * @since 1.0.0
     */
    private static void addMapEntry(String anEntry, Map<String, String> aMap, String aSeparator,
                                    String anEscapeChars, char anEscapeSymbol)
    {
        int separatorIndex = nextIndexOf(anEntry, aSeparator, 0, anEscapeChars, anEscapeSymbol);
        if(separatorIndex < 0) {
            throw new StringIndexOutOfBoundsException("missing key-value separator");
        }
        aMap.put(unescapeString(anEntry.substring(0, separatorIndex), anEscapeChars, anEscapeSymbol),
                 unescapeString(anEntry.substring(separatorIndex + aSeparator.length()),
                                anEscapeChars, anEscapeSymbol));
    }

    /**
     * @brief Adds an entry to a map by splitting the entry string around the
     * separator.
     *
     * If an non-escaped separator string is not found, an exception
     * is thrown. Only the first non-escaped instance of the separator string
     * will be processed. This means that the value may contain multiple
     * non-escaped instances of the separator string.
     *
     * If the value begins with `anArrayPrefix` and ends with `anArraySuffix`,
     * it will be parsed as an array. If the value is empty, it will be parsed
     * as an empty string, even if `anArrayPrefix` and `anArraySuffix` are both
     * empty.
     *
     * @param anEntry A string representation of the map entry consisting of a
     * key-value pair delimited by a separator.
     * @param aMap The map to add the entry to.
     * @param aSeparator The separator that delimits the boundary between keys
     * and values. At least one non-escaped occurrence of this string must
     * appear in the entry.
     * @param anArrayPrefix A string that delimits the beginning of an array
     * value. Values that begin with this sequence (unescaped) and end with
     * `anArraySuffix` will be parsed as arrays. This string may be empty but
     * not `null`.
     * @param anArraySuffix A string that delimits the end of an array value.
     * Values that end with this sequence (unescaped) and begin with
     * `anArrayPrefix` will be parsed as arrays. This string may be empty but
     * not `null`.
     * @param anArraySeparator The separator between elements in array values.
     * This string is only used if the value is determined to be an array. In
     * that case, it may not be empty or `null`.
     * @param anEscapeChars The characters that can be escaped by the escape
     * symbol. A `null` string indicates that all characters preceded by
     * `anEscapeSymbol` are escaped. An empty string indicates that there are no
     * escape sequences present, and `anEscapeSymbol` will be ignored.
     * @param anEscapeSymbol A symbol that indicates the start of escape
     * sequences.
     * @throws IllegalArgumentException if an array is encountered and
     * `anArraySeparator` is empty and the value is an array.
     * @throws StringIndexOutOfBoundsException if `aSeparator` does not appear
     * in `anEntry`.
     * @throws NullPointerException if any of the arguments except
     * `anEscapeChars` are `null`. An exception may not be thrown for
     * `anArraySuffix` if the entry does not contain an array.
     * @since 3.0.0
     */
    private static void addMapEntry(String anEntry, Map<String, Object> aMap, String aSeparator,
                                    String anArrayPrefix, String anArraySuffix, String anArraySeparator,
                                    String anEscapeChars, char anEscapeSymbol)
    {
        int separatorIndex = nextIndexOf(anEntry, aSeparator, 0, anEscapeChars, anEscapeSymbol);
        if(separatorIndex < 0) {
            throw new StringIndexOutOfBoundsException("missing key-value separator");
        }
        String key = unescapeString(anEntry.substring(0, separatorIndex), anEscapeChars, anEscapeSymbol);
        String valueString = anEntry.substring(separatorIndex + aSeparator.length());
        Object value;
        if(!valueString.isEmpty() && valueString.startsWith(anArrayPrefix) &&
           nextIndexOf(valueString, anArraySuffix, valueString.length() - anArraySuffix.length(),
                       anEscapeChars, anEscapeSymbol) >= 0)
        {
            value = stringToArray(valueString,
                                  anArrayPrefix, anArraySuffix, anArraySeparator,
                                  anEscapeChars, anEscapeSymbol);
        } else {
            value = unescapeString(valueString, anEscapeChars, anEscapeSymbol);
        }
        aMap.put(key, value);
    }

    /**
     * Finds the number of occurrences of an escape character starting from the
     * specified position. An even number generally indicates that whatever
     * follows the characters (at index start + 1) is not escaped.
     *
     * @param string the string to search.
     * @param start the index to start searching with. This is the index of the
     * last escape character, not the index of the first non-escape character.
     * @param escape the escape character to search for.
     * @return the number of escape characters found. If the character at the
     * start index is not an escape character, this will be zero.
     * @throws NullPointerException if string is null.
     * @since 1.0.0
     */
    private static int countEscapesBack(String string, int start, char escape)
    {
        int pos = start;
        while(pos >= 0 && string.charAt(pos) == escape) {
            pos--;
        }
        return start - pos;
    }
}
