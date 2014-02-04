/*
 * TextUtilities.java
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

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class is a utility library for performing and inverting operations on
 * strings, arrays of strings and maps of strings. It contains utilities for
 * converting maps of strings to strings and back, the escaping and un-escaping
 * of characters in a string, creating maps from arrays of string pairs,
 * retrieving all the keys in a map as an array of strings, creating and
 * printing time strings, etc.
 * <p>
 * <a name="Properties"><b>Property Lists.</b></a> This class provides a number
 * of methods for manipulating property lists such as by converting them to maps
 * and back. A property list is a string array with an even number of elements,
 * each pair of which represents a property name followed by its value. The even
 * indices in the array contain the names while the odd indices that follow them
 * contain the corresponding values. This is very similar to the arrangement
 * used in MATLAB to define object properties. Since they represent similar
 * concepts, a property list can be converted to and from a map using the
 * {@link #propertiesToMap} and {@link #mapToProperties} methods. These methods
 * are useful for creating copies of maps that can be safely modified without
 * affecting the original as well as quickly initializing maps from lists of
 * strings.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 04 Mar 2012 - J. Fox-Rabinovitz: Created
 * @version 1.0.1, 04 Feb 2014 - J. Fox-Rabinovitz: Added countOccurences()
 * @since 1.0.0
 */
public class TextUtilities
{
    /**
     * The default {@link java.nio.Charset Charset} of all the text operations.
     * This charset is intended to be used for consistence in all I/O
     * operations.
     *
     * @since 1.0.0.0
     */
    public static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0.0
     */
    private TextUtilities() {}

    /**
     * Performs a deep equality check of two string maps. To be considered
     * equal, both maps must either be {@code null} or have the same set of
     * keys mapping to the same values. The internal arrangement and 
     * types of the maps need not be the same.
     *
     * @param map1 the first map to compare.
     * @param map2 the second map to compare.
     * @return {@code true} if both maps are {@code null} or all of their keys
     * and values are equal, {@code false} otherwise.
     * @since 1.0.0.0
     */
    public static boolean mapEquals(Map<String, String> map1,
                                    Map<String, String> map2)
    {
        if(map1 == null && map2 == null)
            return true;
        if(map1 == null || map2 == null)
            return false;
        if(map1.size() != map2.size())
            return false;
        for(Map.Entry<String, String> entry : map1.entrySet()) {
            if(!map2.containsKey(entry.getKey()) || !entry.getValue().equals(map2.get(entry.getKey())))
                return false;
        }
        return true;
    }

    /**
     * Returns the keys in a string map in an array. If the map is {@code null},
     * {@code null} is returned. No guarantee is made about the order of the
     * returned array.
     *
     * @param map a map keyed by strings.
     * @return the keys in the map, or {@code null} if {@code map} is {@code
     * null}.
     * @since 1.0.0.0
     */
    public static String[] keyList(Map<String, ?> map)
    {
        if(map == null)
            return null;
        Set<String> keys = map.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * Converts a map into a string. The returned string has the format {@code
     * <name><prefix><escaped key><keyValueSeparator><escaped value><entrySeparator>
     * ... <suffix>}. The entry separator does not appear after the last
     * key-value element. The name, keys and values will have all characters
     * that appear in {@code escapeChars} escaped by preceding them with the
     * escape symbol. The prefix, suffix, key-value separator and entry
     * separator will not be escaped. The recommended use of this method is to
     * add the symbols in the prefix, suffix and separators to the escape
     * characters so that they can later be easily identified.
     *
     * @param name the name of the map. If null, no name is prefixed.
     * @param map the map to convert. If null or empty, only the name, prefix
     * and suffix are concatenated.
     * @param prefix the prefix to insert before listing keys and values.
     * Omitted entirely if null.
     * @param suffix the suffix to append to the end of the string. Omitted
     * entirely if null.
     * @param keyValueSeparator the character sequence to separate keys from
     * values in the result. Defaults to a single space (' ') if null.
     * @param entrySeparator the character sequence to separate key value pairs
     * from other entries in the result. Defaults to a single space (' ') if
     * null.
     * @param escapeChars the characters that need to be escaped in the name,
     * key and value strings. Note that characters in {@code keyValueSeparator}
     * and {@code entrySeparator} are not escaped. No escaping is done if null
     * or empty.
     * @param escapeSymbol the symbol to be inserted before characters that
     * need to be escaped.
     * @return a string representation of the input map. This value is never
     * {@code null}, although it may be empty if the map, name, prefix and
     * suffix are all empty or {@code null}.
     * @see #propertiesToString
     * @see #stringToMap
     * @since 1.0.0.0
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    public static String mapToString(String name, Map<String, String> map,
                                     String prefix, String suffix,
                                     String keyValueSeparator,
                                     String entrySeparator,
                                     String escapeChars, char escapeSymbol)
    {
        StringBuilder sb = new StringBuilder();
        if(name != null) {
            sb.append(escapeString(name, escapeChars, escapeSymbol));
        }
        if(prefix != null) {
            sb.append(prefix);
        }
        if(map != null && !map.isEmpty()) {
            if(keyValueSeparator == null) {
                keyValueSeparator = " ";
            }
            if(entrySeparator == null) {
                entrySeparator = " ";
            }
            // indicates that the loop is starting
            boolean first = true;
            for(Map.Entry<String, String> entry : map.entrySet()) {
                if(first) {
                    // once the loop has started, the next iteration won't be first
                    first = false;
                } else {
                    // if not on the first iteration, prepend a separator
                    sb.append(entrySeparator);
                }
                sb.append(escapeString(entry.getKey(), escapeChars,
                                       escapeSymbol));
                sb.append(keyValueSeparator);
                sb.append(escapeString(entry.getValue(), escapeChars,
                                       escapeSymbol));
            }
        }
        if(suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * Parses a map encoded with {@link #mapToString mapToString()} or
     * equivalent. Special attention is paid to elements with escape characters
     * in them. The expected format is
     *  {@code
     * <name><prefix><escaped key><keyValueSeparator><escaped value><entrySeparator>
     * ... <suffix>}.
     * Note that if keys are repeated, the values will be silently replaced. The
     * case in which there are no characters between {@code prefix} and {@code
     * suffix} is treated specially as an empty map.
     *
     * @param string the string to decode. This parameter may not be
     * {@code null}. It may only be empty is both prefix and suffix are empty.
     * @param map the map to fill with the decoded values. The map does not have
     * to be empty, but duplicate keys will be silently replaced. If this
     * parameter is {@code null}, only the name is extracted and returned.
     * @param prefix the prefix string. Note that escape characters do
     * <b>not</b> appear in the prefix. This string may not be {@code null}.
     * If the prefix is empty, the map will have an empty name string.
     * @param suffix the suffix string. Note that escape characters do
     * <b>not</b> appear in the suffix. This string may not be {@code null}.
     * @param keyValueSeparator the string that separates keys from values.
     * This string may not be {@code null} or empty.
     * @param entrySeparator the string that separates successive key-value
     * entries. This string may not be {@code null} or empty.
     * @param escapeChars a string containing a list of characters that may be
     * escaped in the input. This parameter may be {@code null} to indicate that
     * all characters preceded by an escape symbol are escaped, or empty to
     * indicate that there are no escape characters.
     * @param escapeSymbol a symbol used to escape special characters.
     * @return the name of the map, or an empty String if none is specified.
     * @throws NullPointerException if any of the arguments except {@code map}
     * or {@code escapeChars} is {@code null}.
     * @throws IllegalArgumentException if either {@code string}, {@code
     * keyValueSeparator} or {@code entrySeparator} is empty.
     * @throws StringIndexOutOfBoundsException if no valid prefix is found, no
     * valid suffix is found, or an entry does not have a valid key-value
     * separator. The message indicates which condition caused the exception.
     * @see #mapToString
     * @since 1.0.0.0
     */
    @SuppressWarnings({"AssignmentToMethodParameter", "NestedAssignment"})
    public static String stringToMap(String string, Map<String, String> map,
                                     String prefix, String suffix,
                                     String keyValueSeparator,
                                     String entrySeparator,
                                     String escapeChars, char escapeSymbol)
    {
        // preprocess (calling isEmpty on null will handle throwing the NPEs)
        if(string.isEmpty()) {
            if(prefix.isEmpty() && suffix.isEmpty())
                return new String();
            throw new IllegalArgumentException("string empty");
        }
        if(entrySeparator.isEmpty()) {
            throw new IllegalArgumentException("entry separator empty");
        }
        if(keyValueSeparator.isEmpty()) {
            throw new IllegalArgumentException("key-value separator empty");
        }
        // find prefix
        int prefixIndex = nextIndexOf(string, prefix, 0, escapeChars, escapeSymbol);
        if(prefixIndex < 0) {
            throw new StringIndexOutOfBoundsException("prefix missing");
        }
        // check for suffix
        int suffixIndex = (suffix.isEmpty()) ? string.length() :
                          nextIndexOf(string, suffix, string.length() - suffix.length(),
                                      escapeChars, escapeSymbol);
        if(suffixIndex < 0) {
            throw new StringIndexOutOfBoundsException("suffix missing");
        }
        String name = string.substring(0, prefixIndex);
        if(map != null) {
            string = string.substring(prefixIndex + prefix.length(), suffixIndex);
            // only process if the string contains a map between the prefix and suffix
            if(!string.isEmpty()) {
                int prev = 0;
                for(int index = 0; (index = nextIndexOf(string, entrySeparator, prev, escapeChars, escapeSymbol)) >= prev;
                    prev = index + entrySeparator.length()) {
                    addMapEntry(string.substring(prev, index), map, keyValueSeparator, escapeChars, escapeSymbol);
                }
                addMapEntry(string.substring(prev), map, keyValueSeparator, escapeChars, escapeSymbol);
            }
        }
        return unescapeString(name, escapeChars, escapeSymbol);
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
     * @since 1.0.0.0
     */
    @SuppressWarnings("AssignmentToMethodParameter")
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
     * @since 1.0.0.0
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
     * @since 1.0.0.0
     */
    @SuppressWarnings({"AssignmentToMethodParameter",
                       "AssignmentToForLoopParameter"})
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
     * @since 1.0.0.0
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
     * @since 1.0.0.0
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
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
     * @since 1.0.0.0
     */
    @SuppressWarnings("AssignmentToMethodParameter")
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
     * @since 1.0.0.0
     */
    @SuppressWarnings("AssignmentToMethodParameter")
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
     * <p>
     * Returns a string representation of the current date and time. The string
     * is formatted as
     * <pre>
     *      YYYY-MM-DD hh:mm:ss.lll
     * </pre>
     * where {@code YYYY} is a four-digit year, {@code MM} is a two digit month
     * number, {@code DD} is the two-digit day of the month, {@code hh} is the
     * two-digit hour of the day, {@code mm} is the two digit minute of the hour,
     * {@code ss} is the two digit second and {@code lll} is the fractional part
     * of the second in milliseconds.
     * </p>
     * <p>
     * This method is equivalent to {@link #timeString(java.util.Calendar)
     * timeString(new GregorianCalendar())}.
     * </p>
     *
     * @return a string representing the date and time of the method invocation.
     * @see GregorianCalendar#GregorianCalendar()
     * @since 1.0.0.0
     */
    public static String nowString()
    {
        return timeString(new GregorianCalendar());
    }

    /**
     * <p>
     * Returns a simplified string representation of the current date and time.
     * The string is formatted as
     * <pre>
     *      YYYYMMDD hhmmss.lll
     * </pre>
     * where {@code YYYY} is a four-digit year, {@code MM} is a two digit month
     * number, {@code DD} is the two-digit day of the month, {@code hh} is the
     * two-digit hour of the day, {@code mm} is the two digit minute of the hour,
     * {@code ss} is the two digit second and {@code lll} is the fractional part
     * of the second in milliseconds.
     * </p>
     * <p>
     * This method is equivalent to {@link #simpleTimeString(java.util.Calendar)
     * simpleTimeString(new GregorianCalendar())}.
     * </p>
     *
     * @return a string representing the date and time of the method invocation.
     * @see GregorianCalendar#GregorianCalendar()
     * @since 1.0.0.0
     */
    public static String simpleNowString()
    {
        return simpleTimeString(new GregorianCalendar());
    }

    /**
     * Returns a string representation of the selected date and time. The string
     * is formatted as
     * <pre>
     *      YYYY-MM-DD hh:mm:ss.lll
     * </pre>
     * where {@code YYYY} is a four-digit year, {@code MM} is a two digit month
     * number, {@code DD} is the two-digit day of the month, {@code hh} is the
     * two-digit hour of the day, {@code mm} is the two digit minute of the hour,
     * {@code ss} is the two digit second and {@code lll} is the fractional part
     * of the second in milliseconds.
     *
     * @param cal a calendar representing the date and time.
     * @return a string representing the specified date and time.
     * @since 1.0.0.0
     */
    public static String timeString(Calendar cal)
    {
        return String.format("%02d-%02d-%02d %02d:%02d:%02d.%03d",
                             cal.get(Calendar.YEAR),
                             cal.get(Calendar.MONTH) + 1,
                             cal.get(Calendar.DAY_OF_MONTH),
                             cal.get(Calendar.HOUR_OF_DAY),
                             cal.get(Calendar.MINUTE),
                             cal.get(Calendar.SECOND),
                             cal.get(Calendar.MILLISECOND));
    }

    /**
     * Returns a simplified string representation of the selected date and time.
     * The string is formatted as
     * <pre>
     *      YYYYMMDD hhmmss.lll
     * </pre>
     * where {@code YYYY} is a four-digit year, {@code MM} is a two digit month
     * number, {@code DD} is the two-digit day of the month, {@code hh} is the
     * two-digit hour of the day, {@code mm} is the two digit minute of the hour,
     * {@code ss} is the two digit second and {@code lll} is the fractional part
     * of the second in milliseconds.
     *
     * @param cal a calendar representing the date and time.
     * @return a string representing the specified date and time.
     * @since 1.0.0.0
     */
    public static String simpleTimeString(Calendar cal)
    {
        return String.format("%02d%02d%02d %02d%02d%02d.%03d",
                             cal.get(Calendar.YEAR),
                             cal.get(Calendar.MONTH) + 1,
                             cal.get(Calendar.DAY_OF_MONTH),
                             cal.get(Calendar.HOUR_OF_DAY),
                             cal.get(Calendar.MINUTE),
                             cal.get(Calendar.SECOND),
                             cal.get(Calendar.MILLISECOND));
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
     * Adds an entry to a map by splitting the entry string around the
     * separator. If an non-escaped separator string is not found, an exception
     * is thrown. Only the first non-escaped instance of the separator string
     * will be processed. This means that the value may contain non-escaped
     * instances of the separator string.
     *
     * @param entry a string representation of the map entry consisting of a
     * key-value pair delimited by a separator
     * @param map the map to add the entry to
     * @param separator the separator that delimits the boundary between keys
     * and values. At least one non-escaped occurrence of this string must
     * appear in the entry.
     * @param escapeChars the characters that can be escaped by the escape
     * symbol. Null indicates that all characters preceded by the symbol are
     * escaped. Empty indicates that there are no escape sequences present, and
     * the escape symbol will be ignored.
     * @param escapeSymbol a symbol that indicates the start of escape sequences
     * @throws StringIndexOutOfBoundsException if separator does not appear in
     * entry
     * @since 1.0.0.0
     */
    private static void addMapEntry(String entry, Map<String, String> map, String separator,
                                    String escapeChars, char escapeSymbol)
    {
        int separatorIndex = nextIndexOf(entry, separator, 0, escapeChars, escapeSymbol);
        if(separatorIndex < 0) {
            throw new StringIndexOutOfBoundsException("missing key-value separator");
        }
        map.put(unescapeString(entry.substring(0, separatorIndex), escapeChars, escapeSymbol),
                unescapeString(entry.substring(separatorIndex + separator.length()), escapeChars, escapeSymbol));
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
     * @since 1.0.0.0
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
