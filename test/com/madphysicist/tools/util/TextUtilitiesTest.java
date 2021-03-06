/*
 * TextUtilitiesTest.java (TestClass: com.madphysicist.tools.util.TextUtilitiesTest)
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

//import com.madphysicist.tools.test.ParametrizedTests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
//import org.testng.annotations.NoInjection;
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link
 * com.madphysicist.tools.util.TextUtilities}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 04 Mar 2012
 * @version 2.0.0.0, 18 Feb 2015 - Added tests for `arrayToString()` and
 *      `stringToArray()`.
 * @since 1.0.0.0
 */
public class TextUtilitiesTest
{
    /**
     * A simple map for testing. This map contains the following mappings:
     * ("a" -> "A", "b" -> "B", "c" -> "C"). This map is created using {@code
     * TextUtilities.propertiesToMap}, so it is not used for testing that
     * method.
     * <p>
     * <b>Methods of this class should not modify the contents of this map.</b>
     *
     * @since 1.0.0.0
     */
    private final SortedMap<String, String> basicMap = TextUtilities.propertiesToMap("a", "A", "b", "B", "c", "C");

    /**
     * An empty map. This map is cleared before every test method so that it can
     * be filled by the tests that need it, or used as an empty map.
     *
     * @since 1.0.0.0
     */
    private SortedMap<String, String> emptyMap = new TreeMap<>();
    
    /**
     * A sample property list used by the methods of this class. This value
     * is meant to be used in conjunction with {@link #mapName}, {@link #mapPrefix},
     * {@link #mapSuffix}, {@link #keyValueSeparator}, {@link #entrySeparator},
     * {@link #mapEscapeChars} and {@link #escapeSymbol} fields to create map
     * strings. It contains at least one escapable character in each key and
     * each value.
     * <p>
     * <b>Methods of this class should not modify the contents of this
     * array.</b>
     *
     * @since 1.0.0.0
     */
    private final String[] complexProperties = new String[] {"a~", "\"A\"", "b-", "<B>", "c\\", "\'C\'"};

    /**
     * The map version of {@link #complexProperties}. This map is created using
     * {@code TextUtilities.propertiesToMap}, so it is not used for testing that
     * method.
     * <p>
     * <b>Methods of this class should not modify the contents of this map.</b>
     * 
     * @since 1.0.0.0
     */
    private final SortedMap<String, String> complexMap = TextUtilities.propertiesToMap(complexProperties);

    /**
     * The escaped version of {@link #complexProperties}. The characters
     * from {@link #mapEscapeChars} are preceded by {@link #escapeSymbol} for all
     * keys and values.
     * <p>
     * <b>Methods of this class should not modify the contents of this
     * array.</b>
     * 
     * @since 1.0.0.0
     */
    private final String[] escapedProperties = new String[] {"a\\~", "\\\"A\\\"", "b\\-", "\\<B\\>", "c\\\\", "\\\'C\\\'"};

    /**
     * A sample name used in map strings by methods of this class. This value
     * is meant to be used in conjunction with {@link #mapPrefix}, {@link #mapSuffix},
     * {@link #keyValueSeparator}, {@link #entrySeparator}, {@link #mapEscapeChars}
     * and {@link #escapeSymbol}. It contains at least one escapable character.
     *
     * @since 1.0.0.0
     */
    private final String mapName = "<The~Map>";

    /**
     * The escaped version of {@link #mapName}. The characters from {@link
     * #mapEscapeChars} are preceded by {@link #escapeSymbol}.
     * 
     * @since 1.0.0.0
     */
    private final String escapedMapName = "\\<The\\~Map\\>";

    /**
     * A sample prefix used in map strings by methods of this class. This value
     * is meant to be used in conjunction with {@link #mapName}, {@link #mapSuffix},
     * {@link #keyValueSeparator}, {@link #entrySeparator}, {@link #mapEscapeChars}
     * and {@link #escapeSymbol}. It contains at least one escapable character.
     *
     * @since 1.0.0.0
     */
    private final String mapPrefix = " << \"";

    /**
     * A sample suffix used in map strings by methods of this class. This value
     * is meant to be used in conjunction with {@link #mapName}, {@link #mapPrefix},
     * {@link #keyValueSeparator}, {@link #entrySeparator}, {@link #mapEscapeChars}
     * and {@link #escapeSymbol}. It contains at least one escapable character.
     *
     * @since 1.0.0.0
     */
    private final String mapSuffix = "\' >>";

    /**
     * A sample key-value separator used in map strings by methods of this
     * class. This value is meant to be used in conjunction with {@link #mapName},
     * {@link #mapPrefix}, {@link #mapSuffix}, {@link #entrySeparator},
     * {@link #mapEscapeChars} and {@link #escapeSymbol}. It contains at least one
     * escapable character.
     *
     * @since 1.0.0.0
     */
    private final String keyValueSeparator = "\" -> \'";

    /**
     * A sample entry separator used in map strings by methods of this
     * class. This value is meant to be used in conjunction with {@link #mapName},
     * {@link #mapPrefix}, {@link #mapSuffix}, {@link #keyValueSeparator},
     * {@link #mapEscapeChars} and {@link #escapeSymbol}. It contains at least one
     * escapable character.
     *
     * @since 1.0.0.0
     */
    private final String entrySeparator = "\' ~ \"";

    /**
     * A sample sequence of escapeable characters used in map strings by methods of
     * this class. This value is meant to be used in conjunction with
     * {@link #mapName}, {@link #mapPrefix}, {@link #mapSuffix},
     * {@link #keyValueSeparator}, {@link #entrySeparator} and
     * {@link #escapeSymbol}. It contains at least one character from each of
     * the keys and values of {@link #complexMap}.
     *
     * @since 1.0.0.0
     */
    private final String mapEscapeChars = "<\"\'~-\\>";

    /**
     * @brief A sample suffix used in array strings by methods of this class.
     *
     * This value is meant to be used in conjunction with `arraySuffix`,
     * `arraySeparator`, `arrayEscapeChars` and `escapeSymbol`. It contains at
     * least one escapable character.
     *
     * @since 2.0.0.0
     */
    private final String arrayPrefix = "[";

    /**
     * @brief A sample suffix used in array strings by methods of this class.
     *
     * This value is meant to be used in conjunction with `arrayPrefix`,
     * `arraySeparator`, `arrayEscapeChars` and `escapeSymbol`. It contains at
     * least one escapable character.
     *
     * @since 2.0.0.0
     */
    private final String arraySuffix = "]";

    /**
     * @brief A sample element separator used in array strings by methods of
     * this class.
     *
     * This value is meant to be used in conjunction with `arrayPrefix`,
     * `arraySuffix`, `arrayEscapeChars` and `escapeSymbol`. It contains at
     * least one escapable character.
     *
     * @since 2.0.0.0
     */
    private final String arraySeparator = ", ";

    /**
     * @brief A sample sequence of escapeable characters used in array strings
     * by methods of this class.
     *
     * This value is meant to be used in conjunction with `arrayPrefix`,
     * `arraySuffix`, `arraySeparator` and `escapeSymbol`.
     *
     * @since 2.0.0.0
     */
    private final String arrayEscapeChars = "[,\\]";

    /**
     * A sample escape character used in map strings by methods of this class.
     * This value is meant to be used in conjunction with {@link #mapName},
     * {@link #mapPrefix}, {@link #mapSuffix}, {@link #keyValueSeparator},
     * {@link #entrySeparator} and {@link #escapeSymbol}. It contains at least
     * one escapable character.
     *
     * @since 1.0.0.0
     */
    private final char escapeSymbol = '\\';

    /**
     * A simple string that is guaranteed not to appear anywhere in
     * {@link #complexProperties}, {@link #escapedProperties},
     * {@link #complexMap}, {@link #complexMapString}, or in any of their
     * components.
     *
     * @since 1.0.0.0
     */
    private final String missingString = "xxx";

    /**
     * The map string created from (@link #complexMap}. This value is the
     * assembled and properly escaped combination of {@link #mapName},
     * {@link #mapPrefix}, {@link #mapSuffix}, {@link #keyValueSeparator},
     * {@link #entrySeparator}, and {@link #escapeSymbol}. This string is
     * guaranteed to be in the same order as both {@code complexMap} and {@code
     * complexProperties}.
     *
     * @see com.madphysicist.tools.util.TextUtilities#mapToString
     * TextUtilities.mapToString()
     * @since 1.0.0.0
     */    
    private final String complexMapString = escapedMapName + mapPrefix
            + escapedProperties[0] + keyValueSeparator + escapedProperties[1] + entrySeparator
            + escapedProperties[2] + keyValueSeparator + escapedProperties[3] + entrySeparator
            + escapedProperties[4] + keyValueSeparator + escapedProperties[5] + mapSuffix;

    private Method mapToString;
    private Method escapeString;
    private Method unescapeString;
    private Method propertiesToString;
    private Method propertiesToMap;

    /**
     * Sets up the methods to use for parametrized testing. All of the methods
     * of TextUtilitiesTest should be reflected here.
     * 
     * @throws NoSuchMethodException if one of the methods being intialized can
     * not be found in the class being tested.
     * @since 1.0.0.0
     */
    @BeforeClass public void setUpMethods() throws NoSuchMethodException
    {
        /*
        Class<TextUtilities> base = TextUtilities.class;
        mapToString = base.getMethod("mapToString", String.class, Map.class,
                String.class, String.class, String.class, String.class,
                String.class, Character.TYPE);
        escapeString = base.getMethod("escapeString", String.class, String.class, Character.TYPE);
        unescapeString = base.getMethod("unescapeString", String.class, String.class, Character.TYPE);
        propertiesToString = base.getMethod("propertiesToString",
                String.class, String[].class, String.class, String.class, String.class,
                String.class, String.class, Character.TYPE);
        propertiesToMap = base.getMethod("propertiesToMap", String[].class);
        */
    }

    /**
     * Common set up for each test method. This method ensures that
     * {@link #emptyMap} is cleared before each test.
     * <p>
     * <b>Test methods does not reset the values of the map and array fields
     * besides {@code emptyMap}. Test methods should not modify those
     * values.</b>
     *
     * @since 1.0.0.0
     */
    @BeforeMethod public void setUpMethod()
    {
        emptyMap.clear();
    }

    /**
     * Input test of keyList method of class TextUtilities. This test method
     * is parametrized with values from the {@link #testKeyListDataProvider()}
     * array. It verifies that key lists for maps of different types and
     * contents are generated correctly.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param testMap the input map.
     * @param expectedValue the expected list of keys.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testKeyListDataProvider")
    public void testKeyList(String label, Map<String, String> testMap, String[] expectedValue)
    {
        System.out.println("keyList (" + label + ")");
        String[] result = TextUtilities.keyList(testMap);
        ArrayUtilities.sort(result, true);
        ArrayUtilities.sort(expectedValue, true);
        Assert.assertEquals(result, expectedValue);
    }

    /**
     * Exception test of stringToMap method of class TextUtilities. This test
     * method is parametrized with values from the
     * {@link #testStringToMapExceptionDataProvider} array. It verifies that an
     * exception of the expected type is thrown in places where it is expected.
     * The exact class of the exception must be specified. Specifically,
     * {@code IndexOutOfBounds} will not work because {@code stringToMap} throws
     * the subclass {@code StringIndexOutOfBounds}.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param testString the string to parse.
     * @param prefix the prefix to find before the map keys and values.
     * @param suffix the suffix to find after the map keys and values.
     * @param keyValueSeparator the string used to separate keys from their
     * values.
     * @param entrySeparator the string used to split map entries.
     * @param mapEscapeChars the characters in the string that have been escaped.
     * @param escapeSymbol the symbol used to escape elements of {@code
     * mapEscapeChars} found in the string.
     * @param expectedExceptionClass the class of the exception that must be
     * thrown for this method to succeed. The exception type must be exact, not
     * a generic supertype.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testStringToMapExceptionDataProvider")
    public void testStringToMapException(String label, String testString,
                                         String prefix, String suffix,
                                         String keyValueSeparator, String entrySeparator,
                                         String escapeChars, char escapeSymbol,
                                         Class<? extends Exception> expectedExceptionClass)
    {
        System.out.println("stringToMap (" + label + ")");
        try {
            TextUtilities.stringToMap(testString, emptyMap, prefix, suffix,
                                      keyValueSeparator, entrySeparator,
                                      escapeChars, escapeSymbol);
            // This should be unreachable due to an exception on the previous line
            Assert.fail();
        } catch(Exception exception) {
            Assert.assertEquals(exception.getClass(), expectedExceptionClass);
        }
    }

    /**
     * Input test of stringToMap method of class TextUtilities. This test method
     * is parametrized with values from the
     * {@link #testStringToMapDataProvider} array. It verifies that strings with
     * different properties are parsed into the correct maps.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param testString the string to parse.
     * @param testMap the map to fill.
     * @param prefix the prefix to find before the map keys and values.
     * @param suffix the suffix to find after the map keys and values.
     * @param keyValueSeparator the string used to separate keys from their
     * values.
     * @param entrySeparator the string used to split map entries.
     * @param mapEscapeChars the characters in the string that have been escaped.
     * @param escapeSymbol the symbol used to escape elements of {@code
     * mapEscapeChars} found in the string.
     * @param expectedName the expected map name.
     * @param expectedMap the expected map.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testStringToMapDataProvider")
    public void testStringToMap(String label, String testString,
                                Map<String, String> testMap,
                                String prefix, String suffix,
                                String keyValueSeparator, String entrySeparator,
                                String escapeChars, char escapeSymbol,
                                String expectedName, Map<String, String> expectedMap)
    {
        System.out.println("stringToMap (" + label + ")");
        String result = TextUtilities.stringToMap(testString, testMap, prefix, suffix,
                                                  keyValueSeparator, entrySeparator,
                                                  escapeChars, escapeSymbol);
        Assert.assertEquals(result, expectedName);
        Assert.assertEquals(testMap, expectedMap);
    }

    /**
     * Exception test of the `TextUtilities.stringToArray()` method. This test
     * method is parametrized with values from the
     * `testStringToArrayExceptionDataProvider` array. It verifies that an
     * exception of the expected type is thrown in places where it is expected.
     * The exact class of the exception must be specified. Specifically,
     * `IndexOutOfBounds` will not work because `TextUtilities.stringToArray()`
     * throws the subclass `StringIndexOutOfBounds`.
     *
     * @param aLabel the label of the current parameter set. This is used for
     * output to the command line.
     * @param aTestString The string to parse.
     * @param aPrefix The prefix to find before the map keys and values.
     * @param aSuffix The suffix to find after the map keys and values.
     * @param aSeparator The string used to separate array elements.
     * @param anEscapeChars The characters in the string that have been escaped.
     * @param anEscapeSymbol The symbol used to escape elements of `anEscapeChars`
     * found in the string.
     * @param anExpectedExceptionClass The class of the exception that must be
     * thrown for this method to succeed. The exception type must be exact, not
     * a generic supertype.
     * @since 1.0.0
     */
    @Test(dataProvider = "testStringToArrayExceptionDataProvider")
    public void testStringToArrayException(String aLabel, String aTestString,
                                           String aPrefix, String aSeparator, String aSuffix,
                                           String anEscapeChars, char anEscapeSymbol,
                                           Class<? extends Exception> anExpectedExceptionClass)
    {
        System.out.println("stringToArray (" + aLabel + ")");
        try {
            TextUtilities.stringToArray(aTestString, aPrefix, aSeparator, aSuffix,
                                        anEscapeChars, anEscapeSymbol);
            // This should be unreachable due to an exception on the previous line
            Assert.fail();
        } catch(Exception exception) {
            Assert.assertEquals(exception.getClass(), anExpectedExceptionClass);
        }
    }

    /**
     * @brief Input test of the `TextUtilities.stringToArray()` method.
     *
     * This test method is parametrized with values from the
     * `testStringToMapDataProvider()` array. It verifies that strings with
     * different properties are parsed into the correct maps.
     *
     * @param aLabel The label of the current parameter set. This is used for
     * output to the command line.
     * @param aTestString The string to parse.
     * @param aPrefix The prefix to find before the array elements.
     * @param aSeparator The string used to separate array elements.
     * @param aSuffix The suffix to find after the array elements.
     * @param anEscapeChars The characters in the string that have been escaped.
     * @param anEscapeSymbol The symbol used to escape elements of
     * `anEscapeChars` found in the string.
     * @param expectedArray The expected array.
     * @since 2.0.0.0
     */
    @Test(dataProvider = "testStringToArrayDataProvider")
    public void testStringToArray(String aLabel, String aTestString,
                                  String aPrefix, String seprator, String aSuffix,
                                  String anEscapeChars, char anEscapeSymbol,
                                  String[] expectedArray)
    {
        System.out.println("stringToMap (" + aLabel + ")");
        String[] result = TextUtilities.stringToArray(aTestString, aPrefix, seprator, aSuffix,
                                                      anEscapeChars, anEscapeSymbol);
        Assert.assertEquals(result, expectedArray);
    }

    /**
     * Odd property list test of propertiesToString method of class
     * TextUtilities. This test checks that an exception is thrown if a property
     * list of odd size is passed in to {@code TextUtilities.propertiesToString}.
     * This method is not parametrized because it only tests for a single
     * condition.
     * 
     * @throws ArrayIndexOutOfBoundsException the exception that this methods
     * checks for.
     * @since 1.0.0.0
     */
    @Test(expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void testPropertiesToStringException()
    {
        System.out.println("propertiesToString (odd properties)");
        
        String[] oddProperties = new String[complexProperties.length + 1];
        System.arraycopy(complexProperties, 0, oddProperties, 0, complexProperties.length);
        oddProperties[complexProperties.length] = missingString;

        TextUtilities.propertiesToString(mapName, oddProperties, mapPrefix, mapSuffix,
                keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol);
        // This should be unreachable due to an exception on the previous line
        Assert.fail();
    }

    /**
     * Exception test of propertiesToMap method of class TextUtilities. This
     * test method is parametrized with values from the
     * {@link #testPropertiesToMapExceptionDataProvider} array. It verifies that
     * an exception of the expected type is thrown in places where it is
     * expected. The exact class of the exception must be specified.
     * Specifically, {@code IndexOutOfBounds} will not work because
     * {@code propertiesToMap} throws the subclass
     * {@code StringIndexOutOfBounds}.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param properties the invalid property list.
     * @param expectedExceptionClass the class of the exception that must be
     * thrown for this method to succeed. The exception type must be exact, not
     * a generic supertype.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testPropertiesToMapExceptionDataProvider")
    public void testPropertiesToMapException(String label, String[] properties,
                                             Class<? extends Exception> expectedExceptionClass)
    {
        System.out.println("propertiesToMap (" + label + ")");
        try {
            TextUtilities.propertiesToMap(properties);
            // This should be unreachable due to an exception on the previous line
            Assert.fail();
        } catch(Exception exception) {
            Assert.assertEquals(exception.getClass(), expectedExceptionClass);
        }
    }
// <<<<<<<
    /**
     * Input test of mapToProperties method of class TextUtilities. This test
     * method is parametrized with values from the
     * {@link #testMapToPropertiesDataProvider} array. It verifies that property
     * lists are created from maps as specified.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param testMap the input map.
     * @param expectedValue the expected property list.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testMapToPropertiesDataProvider")
    public void testMapToProperties(String label, Map<String, String> testMap, String[] expectedValue)
    {
        System.out.println("mapToProperties (" + label + ")");
        String[] result = TextUtilities.mapToProperties(testMap);
        if(result == null) {
            Assert.assertEquals(result, expectedValue);
        } else {
            Assert.assertEquals(result.length, expectedValue.length);
            // comparison has to be element-by-element since order is not guaranteed
            for(int keyIndex = 0; keyIndex < result.length; keyIndex += 2) {
                // search for each key among expected keys
                int searchIndex;
                for(searchIndex = 0; searchIndex < expectedValue.length; searchIndex += 2) {
                    if(result[keyIndex].equals(expectedValue[searchIndex])) {
                        Assert.assertEquals(result[keyIndex + 1], expectedValue[keyIndex + 1]);
                        break;
                    }
                }
                // if no match was found for the current key, fail
                if(searchIndex >= expectedValue.length) {
                    Assert.fail("key \"" + result[keyIndex] + "\" not found among expected");
                }
            }
        }
    }

    /**
     * Null pointer exception test of nextIndexOf(String) method of class
     * TextUtilities. This test checks that an exception is thrown if a null
     * template or key are passed in to {@code
     * TextUtilities.nextIndexOf(String)}.
     * 
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param template the template string to search  in.
     * @param key the key to search for in the template string.
     * @param start the start offset to search from.
     * @param escapeList a list of characters that may be escaped with the
     * escape symbol.
     * @param escapeSymbol the symbol used to escape characters.
     * @throws NullPointerException the exception that this methods checks for.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testNextIndexOfStringExceptionDataProvider",
          expectedExceptions = NullPointerException.class)
    public void testNextIndexOfStringException(String label, String template, String key,
                                               int start, String escapeList, char escapeSymbol)
    {
        System.out.println("nextIndexOf(String) (" + label + ")");
        TextUtilities.nextIndexOf(template, key, start, escapeList, escapeSymbol);
        // This should be unreachable due to an exception on the previous line
        Assert.fail();
    }

    /**
     * Input test of nextIndexOf(String) method of class TextUtilities. This
     * test method is parametrized with values from the
     * {@link #testNextIndexOfStringDataProvider} array. It verifies that the
     * correct position of the key is identified according to the specification.
     * 
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param template the template string to search  in.
     * @param key the key to search for in the template string.
     * @param start the start offset to search from.
     * @param escapeList a list of characters that may be escaped with the
     * escape symbol.
     * @param escapeSymbol the symbol used to escape characters.
     * @param expectedValue the expected index.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testNextIndexOfStringDataProvider")
    public void testNextIndexOfString(String label, String template, String key,
                                      int start, String escapeList, char escapeSymbol,
                                      int expectedValue)
    {
        System.out.println("nextIndexOf(String) (" + label + ")");
        int result = TextUtilities.nextIndexOf(template, key, start, escapeList, escapeSymbol);
        Assert.assertEquals(result, expectedValue);
    }

    /**
     * Null template exception test of nextIndexOf(char) method of class
     * TextUtilities. This test checks that an exception is thrown if a null
     * template or is passed in to {@code TextUtilities.nextIndexOf(char)}.
     *
     * @throws NullPointerException the exception that this methods checks for.
     * @since 1.0.0.0
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testNextIndexOfCharNullTemplate()
    {
        System.out.println("nextIndexOf(String) (null template)");
        TextUtilities.nextIndexOf(null, 'a', 0, escapeSymbol, true);
        // This should be unreachable due to an exception on the previous line
        Assert.fail();
    }

    /**
     * Input test of nextIndexOf(char) method of class TextUtilities. This
     * test method is parametrized with values from the
     * {@link #testNextIndexOfCharDataProvider} array. It verifies that the
     * correct position of the key Stringis identified according to the specification.
     * 
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param template the template string to search  in.
     * @param key the key to search for in the template string.
     * @param start the start offset to search from.
     * @param escapeSymbol the symbol used to escape characters.
     * @param symbolEscaped a flag indicating whether or not the escape symbol
     * may itself be escaped.
     * @param expectedValue the expected index.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testNextIndexOfCharDataProvider")
    public void testNextIndexOfChar(String label, String template, char key,
                                    int start, char escapeSymbol, boolean symbolEscaped,
                                    int expectedValue)
    {
        System.out.println("nextIndexOf(String) (" + label + ")");
        int result = TextUtilities.nextIndexOf(template, key, start, escapeSymbol, symbolEscaped);
        Assert.assertEquals(result, expectedValue);
    }

    //<editor-fold defaultstate="collapsed" desc="Incorporate Me!">
    ////    /**
    ////     * Null input test of nowString(print) method, of class TextUtilities.
    ////     */
    ////    @Test public void testNowStringP_null()
    ////    {
    ////        System.out.println("nowString(print) (null)");
    ////        PrintWriter writer = null;
    ////        try {
    ////            TextUtilities.nowString(writer);
    ////            Assert.fail();
    ////        } catch(NullPointerException npe) {
    ////            Assert.assertTrue(true);
    ////        }
    ////    }
    ////
    ////    /**
    ////     * Normal input test of nowString(print) method, of class TextUtilities.
    ////     */
    ////    @Test public void testNowStringP_normal()
    ////    {
    ////        System.out.println("nowString(print) (normal)");
    ////        StringWriter string = new StringWriter();
    ////        PrintWriter writer = new PrintWriter(string);
    ////        Calendar before = new GregorianCalendar();
    ////        TextUtilities.nowString(writer);
    ////        Calendar after = new GregorianCalendar();
    ////        Assert.assertTrue(before.before(after));
    ////    }
    ////
    ////    /**
    ////     * Test of nowString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testNowString_0args()
    ////    {
    ////        System.out.println("nowString");
    ////        String expResult = "";
    ////        String result = TextUtilities.nowString();
    ////        assertEquals(expResult, result);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of simpleNowString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testSimpleNowString_PrintWriter()
    ////    {
    ////        System.out.println("simpleNowString");
    ////        PrintWriter writer = null;
    ////        TextUtilities.simpleNowString(writer);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of simpleNowString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testSimpleNowString_0args()
    ////    {
    ////        System.out.println("simpleNowString");
    ////        String expResult = "";
    ////        String result = TextUtilities.simpleNowString();
    ////        assertEquals(expResult, result);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of timeString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testTimeString_PrintWriter_Calendar()
    ////    {
    ////        System.out.println("timeString");
    ////        PrintWriter writer = null;
    ////        Calendar cal = null;
    ////        TextUtilities.timeString(writer, cal);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of timeString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testTimeString_Calendar()
    ////    {
    ////        System.out.println("timeString");
    ////        Calendar cal = null;
    ////        String expResult = "";
    ////        String result = TextUtilities.timeString(cal);
    ////        assertEquals(expResult, result);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of simpleTimeString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testSimpleTimeString_PrintWriter_Calendar()
    ////    {
    ////        System.out.println("simpleTimeString");
    ////        PrintWriter writer = null;
    ////        Calendar cal = null;
    ////        TextUtilities.simpleTimeString(writer, cal);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of simpleTimeString method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testSimpleTimeString_Calendar()
    ////    {
    ////        System.out.println("simpleTimeString");
    ////        Calendar cal = null;
    ////        String expResult = "";
    ////        String result = TextUtilities.simpleTimeString(cal);
    ////        assertEquals(expResult, result);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }
    ////
    ////    /**
    ////     * Test of htmlEncode method, of class TextUtilities.
    ////     *\/
    ////    @Test
    ////    public void testHtmlEncode()
    ////    {
    ////        System.out.println("htmlEncode");
    ////        String string = "";
    ////        String expResult = "";
    ////        String result = TextUtilities.htmlEncode(string);
    ////        assertEquals(expResult, result);
    ////        // TODO review the generated test code and remove the default call to fail.
    ////        fail("The test case is a prototype.");
    ////    }*/
    //</editor-fold>

    /**
     * A data provider for {@link #testMethodsByValue}. Returns an array of
     * input parameters that exercise different scenarios.
     * <p>
     * The methods and tests run with this data provider are:
     * <dl>
     * <dt>For <b>{@link TextUtilities#mapToString mapToString}</b></dt>
     * <dd><ul>
     * <li>Null Name: Checks that a null name is omitted from the map
     * string.</li>
     * <li>Null Map: Checks that a null map is converted to just the name,
     * prefix and suffix.</li>
     * <li>Null Prefix: Checks that a null prefix is omitted from the map
     * string.</li>
     * <li>Null Suffix: Checks that a null suffix is omitted from the map
     * string.</li>
     * <li>Null Key-Value Separator: Checks that a null key-value separator
     * defaults to a single space (' ') character in the map string.</li>
     * <li>Null Entry Separator: Checks that a null entry separator defaults to
     * a single space (' ') character in the map string.</li>
     * <li>Null Escape Characters: Checks that no characters get escaped in the
     * map string when the list of characters to escape is null.</li>
     * <li>Empty Name: Checks that an empty name is omitted from the map
     * string.</li>
     * <li>Empty Map: Checks that an empty map is converted to just the name,
     * prefix and suffix.</li>
     * <li>Empty Prefix: Checks that an empty prefix is omitted from the map
     * string.</li>
     * <li>Empty Suffix: Checks that an empty suffix is omitted from the map
     * string.</li>
     * <li>Empty Key-Value Separator: Checks that keys are not separated from
     * values in the map string if an empty key-value separator is passed
     * in.</li>
     * <li>Empty Entry Separator: Checks that successive entries are not
     * separated in the map string if an empty entry separator is passed
     * in.</li>
     * <li>Empty Escape Characters: Checks that no characters get escaped in the
     * map string when the list of characters to escape is null.</li>
     * <li>Single Key: Verifies that the map string does not contain an entry
     * separator if there is only one key-value pair in the map.</li>
     * <li>Normal: Verifies that ordinary inputs produce the expected
     * results.</li>
     * </ul></dd>
     * <dt>For <b>{@link TextUtilities#escapeString escacpeString}</b></dt>
     * <dd><ul>
     * <li>Null String: Checks that the string is returned as-is if it is
     * null.</li>
     * <li>Null Escape Characters: Checks that the string is returned as-is if
     * the list of escape characters is null.</li>
     * <li>Empty String: Checks that the string is returned as-is if it is
     * empty.</li>
     * <li>Empty Escape Characters: Checks that the string is returned as-is if
     * the list of escape characters is empty.</li>
     * <li>Missing Escape Characters: Checks that the string is returned as-is
     * if none of the characters in the escape list are in it.</li>
     * <li>All: Checks that the entire string is escaped properly if it is its
     * own list of escape characters. This verifies that repeated characters
     * are allowed in {@code mapEscapeChars} because the string has adjacent
     * repeated characters in it. It also checks that the escape symbol is
     * escaped properly.</li>
     * </ul></dd>
     * <dt>For <b>{@link TextUtilities#unescapeString unescapeString}</b></dt>
     * <dd><ul>
     * <li>Null String: Checks that the string is returned as-is if it is
     * null.</li>
     * <li>Null Escape Characters: Checks that all characters in the string that
     * are preceded by the escape symbol are unescaped if the list of escape
     * characters is null.</li>
     * <li>Empty String: Checks that the string is returned as-is if it is
     * empty.</li>
     * <li>Empty Escape Characters: Checks that the string is returned as-is if
     * the list of escape characters is empty.</li>
     * <li>Missing Escape Characters: Checks that the string is returned as-is
     * if none of the characters in the escape list are present, even when the
     * escape symbol is present.</li>
     * <li>Missing Escape Symbol: Checks that the string is returned as-is if it
     * does not contain the escape symbol.</li>
     * <li>Ends With Escape Symbol: Checks that a string that ends with the
     * escape symbol retains it when unescaped.</li>
     * <li>Repeated Escape Symbol (Escape): Checks that sequences of consecutive
     * escape symbols are unescaped properly when the escape symbol is present
     * in the list of escape sequences. The test checks both the even and the
     * odd cases. Both types of sequence are followed by an escapable
     * character.</li>
     * <li>Repeated Escape Symbol (Non-Escape): Checks that sequences of
     * consecutive escape symbols are left escaped when the escape symbol is not
     * present in the list of escape sequences. The test checks both the even
     * and the odd cases. Both types of sequence are followed by an escapable
     * character.</li>
     * </ul></dd>
     * <dt>For <b>{@link TextUtilities#propertiesToString propertiesToString}</b></dt>
     * <dd><ul>
     * <li>Null Name: Checks that a null name is omitted from the property
     * string.</li>
     * <li>Null Properties: Checks that a null property list is converted to
     * just the name, prefix and suffix.</li>
     * <li>Null Prefix: Checks that a null prefix is omitted from the property
     * string.</li>
     * <li>Null Suffix: Checks that a null suffix is omitted from the property
     * string.</li>
     * <li>Null Key-Value Separator: Checks that a null key-value separator
     * defaults to a single space (' ') character in the property string.</li>
     * <li>Null Entry Separator: Checks that a null entry separator defaults to
     * a single space (' ') character in the property string.</li>
     * <li>Null Escape Characters: Checks that no characters get escaped in the
     * property string when the list of characters to escape is null.</li>
     * <li>Empty Name: Checks that an empty name is omitted from the property
     * string.</li>
     * <li>Empty Properties: Checks that an empty property list is converted to
     * just the name, prefix and suffix.</li>
     * <li>Empty Prefix: Checks that an empty prefix is omitted from the
     * property string.</li>
     * <li>Empty Suffix: Checks that an empty suffix is omitted from the
     * property string.</li>
     * <li>Empty Key-Value Separator: Checks that keys are not separated from
     * values in the property string if an empty key-value separator is passed
     * in.</li>
     * <li>Empty Entry Separator: Checks that successive entries are not
     * separated in the property string if an empty entry separator is passed
     * in.</li>
     * <li>Empty Escape Characters: Checks that no characters get escaped in the
     * property string when the list of characters to escape is null.</li>
     * <li>Single Key: Verifies that the property string does not contain an
     * entry separator if there is only one key-value pair in the map.</li>
     * <li>Normal: Verifies that ordinary inputs produce the expected
     * results.</li>
     * </ul></dd>
     * <dt>For <b>{@link TextUtilities#propertiesToMap propertiesToMap}</b></dt>
     * <dd><ul>
     * <li>Null: Checks that the map is null if the property list is null.</li>
     * <li>Empty: Checks that the map is empty if the property list is
     * empty.</li>
     * <li>Null Value: Checks that a null value (odd index) is treated normally
     * in the property list.</li>
     * <li>Repeated Key: Checks that a repeated key is overwritten silently in
     * the output map.</li>
     * <li>Normal: Checks that propertiesToMap behaves correctly with normal
     * input.</li>
     * </ul></dd>
     * </dl>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, method to invoke, the expeceted
     * result and the method arguments.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testMethodsByValueDataProvider")
    private Object[][] testMethodsByValueDataProvider()
    {
        // mapToString
        Map<String, String> singleMap = new TreeMap<>();
        singleMap.put(complexProperties[0], complexProperties[1]);
        String singleMapString = escapedMapName + mapPrefix + escapedProperties[0]
                                  + keyValueSeparator + escapedProperties[1] + mapSuffix;

        // escapeString
        String esTestString = "AaBbbccCC\\DEE\\\\LLaAaAAaa";
        String esEscapedString = "\\A\\a\\B\\b\\b\\c\\c\\C\\C\\\\\\D\\E\\E\\\\\\\\\\L\\L\\a\\A\\a\\A\\A\\a\\a";
        String esEscapesList = "DL";

        // unescapeString
        String usTestString = "\\A\\a\\B\\b\\b\\c\\c\\C\\C\\\\\\\\\\\\\\D\\E\\E\\\\\\\\\\\\\\\\\\L\\L\\a\\A\\a\\A\\A\\a\\a";
        String usUnescapedString = "AaBbbccCC\\\\\\DEE\\\\\\\\LLaAaAAaa";
        String usEscapesList = "AaBbCcDdEeLl\\";
        char escapeSymbol2 = '~';

        // propertiesToString
        String[] singleProperties = new String[] {complexProperties[0], complexProperties[1]};
        String singlePropertyString = escapedMapName + mapPrefix + escapedProperties[0]
                                    + keyValueSeparator + escapedProperties[1] + mapSuffix;

        // propertiesToMap
        SortedMap<String, String> p2mBaseMap = new TreeMap<>();
        p2mBaseMap.put("a", "A");
        p2mBaseMap.put("b", "B");
        p2mBaseMap.put("c", "C");

        String[] p2mBaseProperties = new String[] {"a", "A", "b", "B", "c", "C"};

        SortedMap<String, String> p2mNullValueMap = new TreeMap<>(p2mBaseMap);
        p2mNullValueMap.put("b", null);

        String[] p2mNullValueProperties = p2mBaseProperties.clone();
        p2mNullValueProperties[3] = null;

        String[] extendedProperties = new String[] {"a", "X", "b", "B", "c", "Y", "a", "Z", "c", "C", "a", "A"};

        return new Object[][] {
            // mapToString - BEGIN
            {"null name", mapToString, complexMapString.replace(escapedMapName, ""),
                new Object[] {null, complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null map", mapToString, escapedMapName + mapPrefix + mapSuffix,
                new Object[] {mapName, null, mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null mapPrefix", mapToString, complexMapString.replace(mapPrefix, ""),
                new Object[] {mapName, complexMap, null, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null mapSuffix", mapToString, complexMapString.replace(mapSuffix, ""),
                new Object[] {mapName, complexMap, mapPrefix, null, keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null keyValueSeparator", mapToString, complexMapString.replace(keyValueSeparator, " "),
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, null,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null entrySeparator", mapToString, complexMapString.replace(entrySeparator, " "),
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                    null, mapEscapeChars, escapeSymbol}},
            {"null mapEscapeChars", mapToString,
                TextUtilities.unescapeString(complexMapString, mapEscapeChars, escapeSymbol),
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                    entrySeparator, null, escapeSymbol}},
            {"empty mapName", mapToString, complexMapString.replace(escapedMapName, ""),
                new Object[] {"", complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty map", mapToString, escapedMapName + mapPrefix + mapSuffix,
                new Object[] {mapName, emptyMap, mapPrefix, mapSuffix, keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty mapPrefix", mapToString, complexMapString.replace(mapPrefix, ""),
                new Object[] {mapName, complexMap, "", mapSuffix, keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty mapSuffix", mapToString, complexMapString.replace(mapSuffix, ""),
                new Object[] {mapName, complexMap, mapPrefix, "", keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty keyValueSeparator", mapToString, complexMapString.replace(keyValueSeparator, ""),
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, "",
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty entrySeparator", mapToString, complexMapString.replace(entrySeparator, ""),
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                    "", mapEscapeChars, escapeSymbol}},
            {"empty mapEscapeChars", mapToString,
                TextUtilities.unescapeString(complexMapString, mapEscapeChars, escapeSymbol),
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                    entrySeparator, "", escapeSymbol}},
            {"single key", mapToString, singleMapString,
                new Object[] {mapName, singleMap, mapPrefix, mapSuffix, keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            {"normal", mapToString, complexMapString,
                new Object[] {mapName, complexMap, mapPrefix, mapSuffix, keyValueSeparator,
                    entrySeparator, mapEscapeChars, escapeSymbol}},
            // mapToString - END

            // escapeString - BEGIN
            {"null string", escapeString, null, new Object[] {null, esEscapesList, '\\'}},
            {"null mapEscapeChars", escapeString, esTestString, new Object[] {esTestString, null, '\\'}},
            {"empty string", escapeString, "", new Object[] {"", esEscapesList, '\\'}},
            {"empty mapEscapeChars", escapeString, esTestString, new Object[] {esTestString, "", '\\'}},
            {"missing mapEscapeChars", escapeString, esTestString, new Object[] {esTestString, "xXyY", '\\'}},
            {"all", escapeString, esEscapedString, new Object[] {esTestString, esTestString, '\\'}},
            // escapeString - END

            //unescapeString - BEGIN
            {"null string", unescapeString, null,
                new Object[] {null, usEscapesList, escapeSymbol}},
            {"null mapEscapeChars", unescapeString, usUnescapedString,
                new Object[] {usTestString, null, escapeSymbol}},
            {"empty string", unescapeString, "",
                new Object[] {"", usEscapesList, escapeSymbol}},
            {"empty mapEscapeChars", unescapeString, usTestString,
                new Object[] {usTestString, "", escapeSymbol}},
            {"missing escapeSymbol", unescapeString, usTestString,
                new Object[] {usTestString, usEscapesList, escapeSymbol2}},
            {"missing mapEscapeChars", unescapeString, usTestString,
                new Object[] {usTestString, "xXyYzZ*()pP{>", escapeSymbol}},
            {"ends with escapeSymbol", unescapeString, usUnescapedString + escapeSymbol,
                new Object[] {usTestString + escapeSymbol, usEscapesList, escapeSymbol}},
            {"repeated escapeSymbol [escape]", unescapeString, usUnescapedString,
                new Object[] {usTestString, usEscapesList, escapeSymbol}},
            {"repeated escapeSymbol [non-escape]", unescapeString,
                usUnescapedString.replace("\\", "\\\\"),
                new Object[] {usTestString, "AaBbCcDdEeLl", escapeSymbol}},
            // unescapeString - END

            // propertiesToString - BEGIN
            {"null mapName", propertiesToString, complexMapString.replace(escapedMapName, ""),
                new Object[] {null, complexProperties, mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null properties", propertiesToString, escapedMapName + mapPrefix + mapSuffix,
                new Object[] {mapName, null, mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null mapPrefix", propertiesToString, complexMapString.replace(mapPrefix, ""),
                new Object[] {mapName, complexProperties, null, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null mapSuffix", propertiesToString, complexMapString.replace(mapSuffix, ""),
                new Object[] {mapName, complexProperties, mapPrefix, null, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null keyValueSeparator", propertiesToString,
                complexMapString.replace(keyValueSeparator, " "),
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix, null,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"null entrySeparator", propertiesToString,
                complexMapString.replace(entrySeparator, " "),
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix,
                              keyValueSeparator, null, mapEscapeChars, escapeSymbol}},
            {"null mapEscapeChars", propertiesToString,
                TextUtilities.unescapeString(complexMapString, mapEscapeChars, escapeSymbol),
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix,
                              keyValueSeparator, entrySeparator, null, escapeSymbol}},
            {"empty mapName", propertiesToString, complexMapString.replace(escapedMapName, ""),
                new Object[] {"", complexProperties, mapPrefix, mapSuffix,
                              keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty properties", propertiesToString, escapedMapName + mapPrefix + mapSuffix,
                new Object[] {mapName, new String[0], mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty prexix", propertiesToString, complexMapString.replace(mapPrefix, ""),
                new Object[] {mapName, complexProperties, "", mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty mapSuffix", propertiesToString, complexMapString.replace(mapSuffix, ""),
                new Object[] {mapName, complexProperties, mapPrefix, "", keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty keyValueSeparator", propertiesToString,
                complexMapString.replace(keyValueSeparator, ""),
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix,
                              "", entrySeparator, mapEscapeChars, escapeSymbol}},
            {"empty entrySeparator", propertiesToString,
                complexMapString.replace(entrySeparator, ""),
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix,
                              keyValueSeparator, "", mapEscapeChars, escapeSymbol}},
            {"empty mapEscapeChars", propertiesToString,
                TextUtilities.unescapeString(complexMapString, mapEscapeChars, escapeSymbol),
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix,
                              keyValueSeparator, entrySeparator, "", escapeSymbol}},
            {"single key", propertiesToString, singlePropertyString,
                new Object[] {mapName, singleProperties, mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            {"normal", propertiesToString, complexMapString,
                new Object[] {mapName, complexProperties, mapPrefix, mapSuffix, keyValueSeparator,
                              entrySeparator, mapEscapeChars, escapeSymbol}},
            // propertiesToString - END

            // propertiesToMap - BEGIN
            {"null", propertiesToMap, null, new Object[] {null}},
            {"empty", propertiesToMap, emptyMap, new Object[] {new String[0]}},
            {"null value", propertiesToMap, p2mNullValueMap, new Object[] {p2mNullValueProperties}},
            {"repeated key", propertiesToMap, p2mBaseMap, new Object[] {extendedProperties}},
            {"normal", propertiesToMap, p2mBaseMap, new Object[] {p2mBaseProperties}},
            // propertiesToMap - END
        };
    }

    /**
     * A data provider for {@link #testKeyList}. Returns an array of input
     * parameters that exercise different scenarios.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null: A null map should generate a null key list.</li>
     * <li>Empty: Verifies that an empty list is generated from an empty
     * map.</li>
     * <li>Only Null: Verifies that a map with just a null key generates a list
     * with a single null entry.</li>
     * <li>Some Null: Verifies that a map that contains a null key generates the
     * correct key list which contains null.</li>
     * <li>Normal: Checks the key list generated from a normal non-empty map
     * with no null keys.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the test map, and the expected
     * result.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testKeyListDataProvider")
    private Object[][] testKeyListDataProvider()
    {
        // Use HashMap since TreeMap with natural ordering forbids null keys
        HashMap<String, String> onlyNullMap = new HashMap<>();
        HashMap<String, String> someNullMap = new HashMap<>(basicMap);
        onlyNullMap.put(null, null);
        someNullMap.put(null, null);

        return new Object[][] {
            {"null", null, null},
            {"empty", emptyMap, new String[0]},
            {"only null", onlyNullMap, new String[] {null}},
            {"some null", someNullMap, new String[] {null, "a", "b", "c"}},
            {"normal", basicMap, new String[] {"a", "b", "c"}}
        };
    }

    /**
     * A data provider for {@link #testStringToMapException}. Returns an array
     * of input parameters that are intended cause exceptions.
     * <p>
     * The exceptions and tests made available by this data provider are:
     * <dl>
     * <dt>{@code NullPointerException}</dt>
     * <dd><ul>
     * <li>Null String: Checks that a null input string causes an
     * exception.</li>
     * <li>Null Prefix: Checks that a null prefix causes an exception.</li>
     * <li>Null Suffix: Checks that a null suffix causes an exception.</li>
     * <li>Null Key-Value Separator: Checks that a key-value separator causes an
     * exception.</li>
     * <li>Null Entry Separator: Checks that a null entry separator causes an
     * exception.</li>
     * </ul></dd>
     * <dt>{@code IllegalArgumentException}</dt>
     * <dd><ul>
     * <li>Empty String: Checks that an empty input string causes an
     * exception.</li>
     * <li>Empty Key-Value Separator: Checks that an empty key-value separator
     * causes an exception.</li>
     * <li>Empty Entry Separator: Checks that an empty entry separator causes
     * an exception.</li>
     * </ul></dd>
     * <dt>{@code StringIndexOutOfBoundsException}</dt>
     * <dd><ul>
     * <li>Missing Prefix: Checks that an exception is thrown if the specified
     * prefix is not present in the string.</li>
     * <li>Missing Suffix: Checks that an exception is thrown if the string does
     * not end with the specified suffix.</li>
     * <li>Missing Key-Value Separator: Checks that an exceptions is thrown if
     * the specified key-value separator is not present in the string.</li>
     * <li>Escaped Prefix: Checks that an exception is thrown if the specified
     * prefix is present in the string, but it is escaped.</li>
     * <li>Escaped Suffix: Checks that an exception is thrown if the string ends
     * with the specified suffix, but it is escaped.</li>
     * <li>Escaped Key-Value Separator: Checks that an exceptions is thrown if
     * an escaped key-value separator is used to delimit a key-value pair in the
     * string.</li>
     * <li>Padded Suffix: Checks that an exception is thrown if the suffix is
     * present, but not as the last character sequence in the string.</li>
     * </ul></dd>
     * </dl>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the test string, the prefix,
     * suffix, key-value separator, entry separator, list of escape characters,
     * the escape symbol, and the class of the expected exception.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testStringToMapExceptionDataProvider")
    private Object[][] testStringToMapExceptionDataProvider()
    {
        String escapedKeyValueSeparatorMapString = escapedMapName + mapPrefix
                + escapedProperties[0]+ keyValueSeparator + escapedProperties[1] + entrySeparator
                + escapedProperties[2]+ escape(keyValueSeparator) + escapedProperties[3] + entrySeparator
                + escapedProperties[4]+ keyValueSeparator + escapedProperties[5] + mapSuffix;

        return new Object[][] {
            // NullPointerException
            {"null string", null, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    NullPointerException.class},
            {"null mapPrefix", complexMapString, null, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    NullPointerException.class},
            {"null mapSuffix", complexMapString, mapPrefix, null,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    NullPointerException.class},
            {"null keyValueSeparator", complexMapString, mapPrefix, mapSuffix,
                    null, entrySeparator, mapEscapeChars, escapeSymbol,
                    NullPointerException.class},
            {"null entrySeparator", complexMapString, mapPrefix, mapSuffix,
                    keyValueSeparator, null, mapEscapeChars, escapeSymbol,
                    NullPointerException.class},
            // IllegalAgrumentException
            {"empty string", "", mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, IllegalArgumentException.class},
            {"empty keyValueSeparator", complexMapString, mapPrefix, mapSuffix,
                    "", entrySeparator, mapEscapeChars, escapeSymbol,
                    IllegalArgumentException.class},
            {"empty entrySeparator", complexMapString, mapPrefix, mapSuffix,
                    keyValueSeparator, "", mapEscapeChars, escapeSymbol,
                    IllegalArgumentException.class},
            // StringIndexOutOfBoundsException
            {"missing mapPrefix", complexMapString, missingString, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    StringIndexOutOfBoundsException.class},
            {"missing mapSuffix", complexMapString, mapPrefix, missingString,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    StringIndexOutOfBoundsException.class},
            {"missing keyValueSeparator", complexMapString, mapPrefix, mapSuffix,
                    missingString, entrySeparator, mapEscapeChars, escapeSymbol,
                    StringIndexOutOfBoundsException.class},
            {"escaped mapPrefix", complexMapString.replace(mapPrefix, escape(mapPrefix)),
                    mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"escaped mapSuffix", complexMapString.replace(mapSuffix, escape(mapSuffix)),
                    mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"escaped keyValueSeparator", escapedKeyValueSeparatorMapString,
                    mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"padded mapSuffix", complexMapString + " ", mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    StringIndexOutOfBoundsException.class}
        };
    }

    /**
     * A data provider for {@link #testStringToMap}. Returns an array of input
     * parameters that exercise different scenarios.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null Map: Checks that a null map is ignored and the correct name is
     * returned.</li>
     * <li>Null Escape Characters: All symbols preceded by the escape symbol
     * should be treated as escaped if the list of escape characters is
     * null.</li>
     * <li>All Empty: The name should be empty if the input string is empty as
     * well as the suffix and the prefix. This is the only case where an empty
     * map string does not cause an exception.</li>
     * <li>Empty mapPrefix: If the expected prefix is empty, the map name and
     * prefix should be treated as part of the first key.</li>
     * <li>Empty Suffix: If the expected suffix is empty, the suffix in the
     * string should be treated as part of the last value.</li>
     * <li>Empty Escape Characters: An empty list of escape characters should
     * be interpreted as meaning that no characters are escaped by the escape
     * symbol.</li>
     * <li>Empty Key: An entry separator followed by a key-value separator
     * should be parsed as an empty key.</li>
     * <li>Empty Value: A key-value separator followed by an entry separator
     * should be parsed as an empty value.</li>
     * <li>Empty Entry: An entry consisting of only a key-value separator should
     * be parsed as an empty key and an empty value.</li>
     * <li>Missing Entry Separator: If the specified entry separator is not
     * found, the content of the map string should be broken up into a single
     * key and giant value.</li>
     * <li>Escaped Entry Separator: If the entry separator appears in escaped
     * form, it, and the following key-value pair should be concatenated into
     * the preceding value.</li>
     * <li>Start Prefix: The name should be empty if the map string begins with
     * the specified prefix. This test adds the escaped name to the prefix
     * rather than removing the name from the map string to verify that escape
     * characters in the prefix are not un-escaped.</li>
     * <li>Bare String (With Name): If the string consists only of a name,
     * prefix and suffix, the resulting map should be empty.</li>
     * <li>Bare String (No Name): If the string consists only of a prefix and
     * suffix, both the resulting map and its name should be empty.</li>
     * <li>Repeated Key: Repeated keys should have the value silently
     * overwritten, with only the last value in the string appearing in the
     * map.</li>
     * <li>Non-Empty Map: Checks that entries are added and overwritten
     * correctly if the map is not empty.</li>
     * <li>Normal: Normal input should be parsed correctly and added into an
     * empty map.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the test string, the test map,
     * the prefix, suffix, key-value separator, entry separator, list of escape
     * characters, escape symbol, the expected map name and the expected map.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testStringToMapDataProvider")
    private Object[][] testStringToMapDataProvider()
    {
        SortedMap<String, String> prefixMap = new TreeMap<>(complexMap);
        // replace the first key with the name and prefix as part of the key
        prefixMap.remove(complexProperties[0]);
        prefixMap.put(mapName + mapPrefix + complexProperties[0], complexProperties[1]);
        
        SortedMap<String, String> suffixMap = new TreeMap<>(complexMap);
        // append the fuffix to the last value
        suffixMap.put(suffixMap.lastKey(), suffixMap.get(suffixMap.lastKey()) + mapSuffix);

        String emptyKeyMapString = escapedMapName + mapPrefix
                + keyValueSeparator + escapedProperties[1] + entrySeparator
                + escapedProperties[2] + keyValueSeparator + escapedProperties[3] + mapSuffix;
        SortedMap<String, String> emptyKeyMap = TextUtilities.propertiesToMap(
                "", complexProperties[1], complexProperties[2], complexProperties[3]);

        String emptyValueMapString = escapedMapName + mapPrefix
                + escapedProperties[0] + keyValueSeparator + escapedProperties[1]
                + entrySeparator + escapedProperties[2] + keyValueSeparator + mapSuffix;
        SortedMap<String, String> emptyValueMap = TextUtilities.propertiesToMap(
                complexProperties[0], complexProperties[1], complexProperties[2], "");

        SortedMap<String, String> oneEntryMap1 = TextUtilities.propertiesToMap(
                complexProperties[0], complexProperties[1] + entrySeparator
              + complexProperties[2] + keyValueSeparator + complexProperties[3] + entrySeparator
              + complexProperties[4] + keyValueSeparator + complexProperties[5]);

        String escapedEntrySeparatorMapString = escapedMapName + mapPrefix
                + escapedProperties[0] + keyValueSeparator + escapedProperties[1] + entrySeparator
                + escapedProperties[2] + keyValueSeparator + escapedProperties[3] + escape(entrySeparator)
                + escapedProperties[4] + keyValueSeparator + escapedProperties[5] + mapSuffix;
        SortedMap<String, String> oneEntryMap2 = TextUtilities.propertiesToMap(
                complexProperties[0], complexProperties[1],
                complexProperties[2], complexProperties[3] + entrySeparator
                                    + complexProperties[4] + keyValueSeparator + complexProperties[5]);

        String repeatedMapString = escapedMapName + mapPrefix
                + "a" + keyValueSeparator + "B" + entrySeparator
                + "a" + keyValueSeparator + "A" + mapSuffix;
        SortedMap<String, String> repeatedMap = TextUtilities.propertiesToMap("a", "A");

        SortedMap<String, String> nonEmptyMap = TextUtilities.propertiesToMap(
                complexProperties[2], complexProperties[5], missingString, missingString);
        SortedMap<String, String> complexNonEmptyMap = new TreeMap<>(nonEmptyMap);
        // replaces prop[2]->prop[5] with prop[2]->prop[3] and adds missing->missing
        complexNonEmptyMap.putAll(complexMap);

        return new Object[][] {
            {"null map", complexMapString, null, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, null},
            {"null mapEscapeChars", complexMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, null, escapeSymbol,
                    mapName, complexMap},
            {"all empty", "", emptyMap, "", "", keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, "", new TreeMap<>()},
            {"empty mapPrefix", complexMapString, emptyMap, "", mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    "", prefixMap},
            {"empty mapSuffix", complexMapString, emptyMap, mapPrefix, "",
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, suffixMap},
            {"emptyEscapeChars", complexMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, "", escapeSymbol,
                    escapedMapName, TextUtilities.propertiesToMap(escapedProperties)},
            {"empty key", emptyKeyMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, emptyKeyMap},
            {"empty value", emptyValueMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, emptyValueMap},
            {"empty entry", escapedMapName + mapPrefix + keyValueSeparator + mapSuffix,
                    emptyMap, mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol,
                    mapName, TextUtilities.propertiesToMap("", "")},
            {"missing entrySeparator", complexMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, missingString, mapEscapeChars, escapeSymbol,
                    mapName, oneEntryMap1},
            {"escaped entrySeparator", escapedEntrySeparatorMapString, emptyMap,
                    mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, mapName, oneEntryMap2},
            {"start mapPrefix", complexMapString, emptyMap, escapedMapName + mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    "", complexMap},
            {"bare string with mapName", escapedMapName + mapPrefix + mapSuffix, emptyMap,
                    mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, mapName, new TreeMap<>(emptyMap)},
            {"bare string no mapName", mapPrefix + mapSuffix, emptyMap,
                    mapPrefix, mapSuffix, keyValueSeparator, entrySeparator,
                    mapEscapeChars, escapeSymbol, "", new TreeMap<>(emptyMap)},
            {"non-empty map", complexMapString, nonEmptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, complexNonEmptyMap},
            {"repeated key", repeatedMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, repeatedMap},
            {"normal", complexMapString, emptyMap, mapPrefix, mapSuffix,
                    keyValueSeparator, entrySeparator, mapEscapeChars, escapeSymbol,
                    mapName, complexMap}
        };
    }

    /**
     * @brief A data provider for `testStringToArrayException()`.
     *
     * Returns an array of input parameters that are intended cause exceptions.
     *
     * The exceptions and tests made available by this data provider are:
     * <dl>
     * <dt>`NullPointerException`</dt>
     * <dd>
     *  - Null String: Checks that a null input string causes an exception.
     *  - Null Prefix: Checks that a null prefix causes an exception.
     *  - Null Suffix: Checks that a null suffix causes an exception.
     *  - Null Separator: Checks that null a separator causes an exception.
     * </dd>
     * <dt>`IllegalArgumentException`</dt>
     * <dd>
     *  - Empty String: Checks that an empty input string causes an exception if
     *    the prefix and suffix are not both empty.
     *  - Empty Separator: Checks that an empty separator causes an exception.
     * </dd>
     * <dt>`StringIndexOutOfBoundsException`</dt>
     * <dd>
     *  - Missing Prefix: Checks that an exception is thrown if the specified
     *    prefix is not present in the string.
     *  - Missing Suffix: Checks that an exception is thrown if the string does
     *    not end with the specified suffix.
     *  - Escaped Prefix: Checks that an exception is thrown if the specified
     *    prefix is present in the string, but it is escaped.
     *  - Escaped Suffix: Checks that an exception is thrown if the string ends
     *    with the specified suffix, but it is escaped.
     *  - Padded Suffix: Checks that an exception is thrown if the suffix is
     *    present, but not as the last character sequence in the string.
     * </dd>
     * </dl>
     *
     * @return A two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the test string, the prefix,
     * separator, suffix, entry separator, list of escape characters, the escape
     * symbol, and the class of the expected exception.
     * @since 2.0.0.0
     */
    @DataProvider(name = "testStringToArrayExceptionDataProvider")
    private Object[][] testStringToArrayExceptionDataProvider()
    {
        String arrayString = "[a, b,c, d\\, e]";

        return new Object[][] {
            // NullPointerException
            {"null string", null, arrayPrefix, arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, NullPointerException.class},
            {"null prefix", arrayString, null, arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, NullPointerException.class},
            {"null separator", arrayString, arrayPrefix, null, arraySuffix,
                    arrayEscapeChars, escapeSymbol, NullPointerException.class},
            {"null suffix", arrayString, arrayPrefix, arraySeparator, null,
                    arrayEscapeChars, escapeSymbol, NullPointerException.class},
            // IllegalAgrumentException
            {"empty string", "", arrayPrefix, arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, IllegalArgumentException.class},
            {"empty string-prefix-only", "", arrayPrefix, arraySeparator, "",
                    arrayEscapeChars, escapeSymbol, IllegalArgumentException.class},
            {"empty string-suffix-only", "", "", arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, IllegalArgumentException.class},
            {"empty separator", arrayString, arrayPrefix, "", arraySuffix,
                    arrayEscapeChars, escapeSymbol, IllegalArgumentException.class},
            // StringIndexOutOfBoundsException
            {"missing prefix", arrayString, missingString, arraySeparator,
                    arraySuffix, arrayEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"missing suffix", arrayString, arrayPrefix, arraySeparator, missingString,
                    arrayEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"escaped prefix", "\\[a, b]", arrayPrefix, arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"escaped suffix", "[a, b\\]", arrayPrefix, arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
            {"padded suffix", arrayString + " ", arrayPrefix, arraySeparator, arraySuffix,
                    arrayEscapeChars, escapeSymbol, StringIndexOutOfBoundsException.class},
        };
    }

    /**
     * @brief A data provider for `testStringToArray`.
     * 
     * Returns an array of input parameters that exercise different scenarios.
     * <p>
     * The tests made available by this data provider are:
     *  - Null Escape Characters: All symbols preceded by the escape symbol
     *    should be treated as escaped if the list of escape characters is
     *    `null`.
     *  - All Empty: The array should be empty if the input string is empty as
     *    well as the suffix and the prefix. This is the only case where an
     *    empty input string does not cause an exception.
     *  - Empty Prefix: If the expected prefix is empty, what would normally be
     *    the prefix should be treated as part of the first key.
     *  - Empty Suffix: If the expected suffix is empty, what would normally be
     *    the suffix should be treated as part of the last value.
     *  - Empty Escape Characters: An empty list of escape characters should be
     *    interpreted to mean that no characters are escaped by the escape
     *    symbol.
     *  - Empty Element: A separator followed by another separator should be
     *    parsed as an empty element.
     *  - Empty Last Element: A separator followed by the suffix should be
     *    parsed as an empty final element.
     *  - Missing Separator: If the specified entry separator is not found, the
     *    content of the input string should be broken up into a single element.
     *  - Bare String: If the string consists only of a prefix and suffix, the
     *    resulting array should be empty.
     *  - Same Delimiters: The parser should have no trouble differentiating
     *    between the prefix, separator and suffix, even when they are
     *    identical.
     *  - Similar Delimiters: The parser should have no trouble differentiating
     *    between the prefix, separator and suffix, even when they contain each
     *    other.
     *  - Normal: Normal input should be parsed correctly.
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the test string, the prefix,
     * separator, suffix, list of escape characters, escape symbol, and the
     * expected resulting string array.
     * @since 2.0.0.0
     */
    @DataProvider(name = "testStringToArrayDataProvider")
    private Object[][] testStringToArrayDataProvider()
    {
        String arrayString = "[a, \\b,c, d\\, e]";

        return new Object[][] {
            {"null escapeChars", arrayString, arrayPrefix, arraySeparator, arraySuffix, null, escapeSymbol,
                    new String[] {"a", "b,c", "d, e" }},
            {"all empty", "", "", arraySeparator, "", arrayEscapeChars, escapeSymbol,
                    new String[0]},
            {"empty prefix", arrayString, "", arraySeparator, arraySuffix, arrayEscapeChars, escapeSymbol,
                    new String[] {"[a", "\\b,c", "d, e"}},
            {"empty suffix", arrayString, arrayPrefix, arraySeparator, "", arrayEscapeChars, escapeSymbol,
                    new String[] {"a", "\\b,c", "d, e]"}},
            {"emptyEscapeChars", arrayString, arrayPrefix, arraySeparator, arraySuffix, "", escapeSymbol,
                    new String[] {"a", "\\b,c", "d\\", "e"}},
            {"empty element", "[a, b, c, , d,]", arrayPrefix, arraySeparator, arraySuffix, "", escapeSymbol,
                    new String[] {"a", "b", "c", "", "d,"}},
            {"empty last element", "[a, b, c, d, ]", arrayPrefix, arraySeparator, arraySuffix, "", escapeSymbol,
                    new String[] {"a", "b", "c", "d", ""}},
            {"missing separator", "[a b c\\, d]", arrayPrefix, arraySeparator, arraySuffix, arrayEscapeChars, escapeSymbol,
                    new String[] {"a b c, d"}},
            {"bare string", "[]", arrayPrefix, arraySeparator, arraySuffix, arrayEscapeChars, escapeSymbol,
                    new String[0]},
            {"same delimiters", "xxxxxxxxxaxxx", "xxx", "xxx", "xxx", arrayEscapeChars, escapeSymbol,
                    new String[] {"", "", "a"}},
            {"similar delimiters", "xxxxxaxxxxxx", "xx", "xxx", "xx", arrayEscapeChars, escapeSymbol,
                    new String[] {"", "a", "x"}},
            {"normal", arrayString, arrayPrefix, arraySeparator, arraySuffix, arrayEscapeChars, escapeSymbol,
                        new String[] {"a", "\\b,c", "d, e"}}
        };
    }

    /**
     * A data provider for {@link #testPropertiesToMapException}. Returns an
     * array of input parameters that are intended cause exceptions.
     * <p>
     * The exceptions and tests made available by this data provider are:
     * <dl>
     * <dt>{@code NullPointerException}</dt>
     * <dd><ul>
     * <li>Null Key: Checks that a null key in the property list causes an
     * exception.</li>
     * </ul></dd>
     * <dt>{@code ArrayIndexOutOfBoundsException}</dt>
     * <dd><ul>
     * <li>Odd List: Checks that an odd number of elements in the property list
     * causes an exception.</li>
     * </ul></dd>
     * </dl>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the property list and the class
     * of the expected exception.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testPropertiesToMapExceptionDataProvider")
    private Object[][] testPropertiesToMapExceptionDataProvider()
    {
        return new Object[][] {
            // NullPointerException
            {"null key", new String[] {"a", "A", null, "B", "c", "C"}, NullPointerException.class},
            // ArrayIndexOutOfBoundsException
            {"odd list", new String[] {"a", "b", "c"}, ArrayIndexOutOfBoundsException.class}
        };
    }

    /**
     * A data provider for {@link #testMapToProperties}. Returns an array of
     * input parameters that exercise different scenarios. Note that this method
     * <b>never</b> uses {@link #basicMap}, {@link #complexMap} or
     * {@link #complexProperties}, just to be extra safe.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null: Checks that the property list is null if the map is null.</li>
     * <li>Empty: Checks that the property list is empty if the map is
     * empty.</li>
     * <li>Null Value: Checks that a null map value is inserted into an odd
     * index normally in the property list.</li>
     * <li>Normal: Checks that mapToProperties behaves correctly with normal
     * input.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the map, and the expected property
     * list.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testMapToPropertiesDataProvider")
    private Object[][] testMapToPropertiesDataProvider()
    {
        SortedMap<String, String> baseMap = new TreeMap<>();
        baseMap.put("a", "A");
        baseMap.put("b", "B");
        baseMap.put("c", "C");

        String[] baseProperties = new String[] {"a", "A", "b", "B", "c", "C"};

        SortedMap<String, String> nullValueMap = new TreeMap<>(baseMap);
        nullValueMap.put("b", null);

        String[] nullValueProperties = baseProperties.clone();
        nullValueProperties[3] = null;

        return new Object[][] {
            {"null", null, null},
            {"empty", emptyMap, new String[0]},
            {"null value", nullValueMap, nullValueProperties},
            {"normal", baseMap, baseProperties}
        };
    }

    /**
     * A data provider for {@link #testNextIndexOfStringException}. Returns an
     * array of input parameters that are intended cause a
     * {@code NullPointerException}.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null Template: Checks that an exception is thrown if the template is
     * null.</li>
     * <li>Null Key: Checks that an exception is thrown if the key is null.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the template string, the search
     * key, the start index, a list of escape characters, and the escape symbol.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testNextIndexOfStringExceptionDataProvider")
    private Object[][] testNextIndexOfStringExceptionDataProvider()
    {
        return new Object[][] {
            {"null template", null, "key", 0, "ABC", '\\'},
            {"null key", "template", null, 0, "XYZ", '~'}
        };
    }

    /**
     * A data provider for {@link #testNextIndexOfString}. Returns an array of
     * input parameters that exercise different scenarios. Note that this method
     * <b>never</b> uses {@link #basicMap}, {@link #complexMap} or
     * {@link #complexProperties}, just to be extra safe.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null Escape Characters: Checks that all characters are regarded as
     * being escaped when the list of escape characters is null.</li>
     * <li>Empty Escape Characters: Checks that no characters are regarded as
     * being escaped when the list of escape characters is empty.</li>
     * <li>Empty Template: Checks that the key is not found in an empty
     * template.</li>
     * <li>Empty Key: Checks that an empty key is found at index 0 in the
     * template, even when the first character is escaped.</li>
     * <li>Empty Template Empty Key: Checks that the key is found at index 0 if
     * both it and the template are empty.</li>
     * <li>Missing Key (Escaped): Checks that the key is not found if the only
     * occurrence of it in the template is escaped.</li>
     * <li>Missing Key (Non-Escaped): Checks that the key is not found if it is
     * not present in the template at all.</li>
     * <li>Negative start (Escaped): Checks that the key is found correctly when
     * the start offset is negative, even when some instances of it are escaped
     * in the template.</li>
     * <li>Negative start (Non-Escaped): Checks that the key is found correctly
     * when the start offset is negative.</li>
     * <li>Zero start (Escaped): Checks that the key is found correctly when
     * the start offset is the beginning of the template, even when some
     * instances of it are escaped.</li>
     * <li>Zero start (Non-Escaped): Checks that the key is found correctly
     * when the start offset is the beginning of the template.</li>
     * <li>Start Past Escape (Escaped): Checks that an escaped key is ignored
     * when the start offset is just past the escape symbol.</li>
     * <li>Start Past Escape (Non-Escaped): Checks that an escape character
     * immediately preceding the start offset is ignored when the first
     * character of the key is not in the list of escapable characters.</li>
     * <li>Start Past (Escaped): Checks that an escaped key is ignored
     * when the start offset is past the last occurrence of the key in the
     * template.</li>
     * <li>Start Past (Non-Escaped): Checks that the key is not found when the
     * start offset is past the last occurrence of the key in the template.</li>
     * <li>Positive start (Escaped): Checks that the key is found correctly when
     * the start offset is positive and within the string, even when some
     * instances of it are escaped in the template.</li>
     * <li>Positive start (Non-Escaped): Checks that the key is found correctly
     * when the start offset is positive and within the string.</li>
     * <li>Start Near End: Checks that the key is not found when the start
     * offset is less than the length of the key from the end of the
     * template.</li>
     * <li>Start Past End: Checks that the key is not found when the start
     * offset is past the end of the template.</li></li>
     * <li>Even Escapes (Escaped): Checks that an even number of escape symbols
     * is ignored when the escape symbol is in the list of escapable
     * characters.</li>
     * <li>Even Escapes (Non-Escaped): Checks that the last in an even number of
     * escape symbols is not ignored when the escape symbol is not in the list
     * of escapable characters.</li>
     * <li>Odd Escapes (Escaped): Checks that an odd number of escape symbols
     * is not ignored, even when the escape symbol is in the list of escapable
     * characters.</li>
     * <li>Odd Escapes (Non-Escaped): Checks that an odd number of escape
     * symbols is not ignored when the escape symbol is not in the list of
     * escapable characters.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the template string, the search
     * key, the start index, a list of escape characters, the escape symbol and
     * the expected index.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testNextIndexOfStringDataProvider")
    private Object[][] testNextIndexOfStringDataProvider()
    {
        String template = "\\ABCABC";
        String otherTemplate = "\\ABC\\ABCABC";
        String escapedTemplate = "\\\\\\ABCABC";
        String pastTemplate = "ABC\\ABCDEF";
        String key = "ABC";
        String escapes = "Aa";
        char symbol = '\\';
        char otherSymbol = '~';

        return new Object[][] {
            {"null mapEscapeChars", template, key, 0, null, symbol, 4},
            {"empty mapEscapeChars", template, key, 0, "", symbol, 1},
            {"empty template", "", key, 0, escapes, symbol, -1},
            {"empty key", template, "", 0, escapes, symbol, 0},
            {"empty template empty key", "", "", 0, escapes, symbol, 0},
            {"missing key [escaped]", "XYZ\\ABCDEF", key, 0, escapes, symbol, -1},
            {"missing key [non-escaped]", template, "abc", 0, escapes, symbol, -1},
            {"negative start [escaped]", template, key, -5, escapes, symbol, 4},
            {"negative start [non-escaped]", template, key, -5, escapes, otherSymbol, 1},
            {"zero start [escaped]", template, key, 0, escapes, symbol, 4},
            {"zero start [non-escaped]", template, key, 0, escapes, otherSymbol, 1},
            {"start past escape [escaped]", template, key, 1, escapes, symbol, 4},
            {"start past escape [non-escaped]", template, key, 1, "aB", symbol, 1},
            {"start past [escaped]", pastTemplate, key, 2, escapes, symbol, -1},
            {"start past [non-escaped]", pastTemplate, key, 5, escapes, symbol, -1},
            {"positive start [escaped]", otherTemplate, key, 2, escapes, symbol, 8},            
            {"positive start [non-escaped]", otherTemplate, key, 2, escapes, otherSymbol, 5},
            {"start near end", template, key, 5, escapes, symbol, -1},
            {"start past end", template, key, 7, escapes, symbol, -1},
            {"even escapes [escaped]", symbol + escapedTemplate, key, 0, escapes + symbol, symbol, 4},
            {"even escapes [non-escaped]", symbol + escapedTemplate, key, 0, escapes, symbol, 7},
            {"odd escapes [escaped]", escapedTemplate, key, 0, escapes + symbol, symbol, 6},
            {"odd escapes [non-escaped]", escapedTemplate, key, 0, escapes, symbol, 6}
        };
    }

    /**
     * A data provider for {@link #testNextIndexOfChar}. Returns an array of
     * input parameters that exercise different scenarios. Note that this method
     * <b>never</b> uses {@link #basicMap}, {@link #complexMap} or
     * {@link #complexProperties}, just to be extra safe.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Empty Template: Checks that the key is not found in an empty
     * template.</li>
     * <li>Missing Key (Escaped): Checks that the key is not found if the only
     * occurrence of it in the template is escaped.</li>
     * <li>Missing Key (Non-Escaped): Checks that the key is not found if it is
     * not present in the template at all.</li>
     * <li>Negative start (Escaped): Checks that the key is found correctly when
     * the start offset is negative, even when some instances of it are escaped
     * in the template.</li>
     * <li>Negative start (Non-Escaped): Checks that the key is found correctly
     * when the start offset is negative.</li>
     * <li>Zero start (Non-Escaped): Checks that the key is found correctly
     * when the start offset is the beginning of the template.</li>
     * <li>Start Past Escape: Checks that an escaped key is ignored when the
     * start offset is just past the escape symbol.</li>
     * <li>Start Past (Escaped): Checks that an escaped key is ignored
     * when the start offset is past the last occurrence of the key in the
     * template.</li>
     * <li>Start Past (Non-Escaped): Checks that the key is not found when the
     * start offset is past the last occurrence of the key in the template.</li>
     * <li>Positive start (Escaped): Checks that the key is found correctly when
     * the start offset is positive and within the string, even when some
     * instances of it are escaped in the template.</li>
     * <li>Positive start (Non-Escaped): Checks that the key is found correctly
     * when the start offset is positive and within the string.</li>
     * <li>Start Near End: Checks that the key is not found when the start
     * offset is less than the length of the key from the end of the
     * template.</li>
     * <li>Start Past End: Checks that the key is not found when the start
     * offset is past the end of the template.</li></li>
     * <li>Even Escapes (Escaped): Checks that an even number of escape symbols
     * is ignored when the escape symbol can be escaped.</li>
     * <li>Even Escapes (Non-Escaped): Checks that the last in an even number of
     * escape symbols is not ignored when the escape symbol can not be
     * escaped.</li>
     * <li>Odd Escapes (Escaped): Checks that an odd number of escape symbols
     * is not ignored, even when the escape symbol can be escaped.</li>
     * <li>Odd Escapes (Non-Escaped): Checks that an odd number of escape
     * symbols is not ignored when the escape symbol can not be escaped.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the template string, the search
     * key, the start index, the escape symbol, a flag indicating whether or not
     * the escape symbol can itself be escaped, and the expected index.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testNextIndexOfCharDataProvider")
    private Object[][] testNextIndexOfCharDataProvider()
    {
        String template = "\\ABCABC";
        String otherTemplate = "\\ABC\\ABCABC";
        String escapedTemplate = "\\\\\\ABCABC";
        String pastTemplate = "ABC\\ABCDEF";
        char key = 'A';
        char symbol = '\\';
        char otherSymbol = '~';

        return new Object[][] {
            {"empty template", "", key, 0, symbol, true, -1},
            {"missing key [escaped]", "XYZ\\ABCDEF", key, 0, symbol, true, -1},
            {"missing key [non-escaped]", template, 'z', 0, symbol, true, -1},
            {"negative start [escaped]", template, key, -5, symbol, true, 4},
            {"negative start [non-escaped]", template, key, -5, otherSymbol, true, 1},
            {"zero start [escaped]", template, key, 0, symbol, true, 4},
            {"zero start [non-escaped]", template, key, 0, otherSymbol, true, 1},
            {"start past escape", template, key, 1, symbol, true, 4},
            {"start past [escaped]", pastTemplate, key, 2, symbol, true, -1},
            {"start past [non-escaped]", pastTemplate, key, 5, symbol, true, -1},
            {"positive start [escaped]", otherTemplate, key, 2, symbol, true, 8},            
            {"positive start [non-escaped]", otherTemplate, key, 2, otherSymbol, true, 5},
            {"start past end", template, key, 7, symbol, true, -1},
            {"even escapes [escaped]", symbol + escapedTemplate, key, 0, symbol, true, 4},
            {"even escapes [non-escaped]", symbol + escapedTemplate, key, 0, symbol, false, 7},
            {"odd escapes [escaped]", escapedTemplate, key, 0, symbol, true, 6},
            {"odd escapes [non-escaped]", escapedTemplate, key, 0, symbol, false, 6}
        };
    }

    /**
     * Inserts an escape symbol before the first escapable character of a
     * string. This method can be used to insert the minimum number of escape
     * characters into strings such as {@link #mapPrefix} and {@link #mapSuffix} to
     * make them parse as something they were not intended to be parsed as. The
     * value of the escape symbol is the field {@link #escapeSymbol}.
     *
     * @param input the string to escape
     * @return the escaped string.
     */
    private String escape(String input)
    {
        for(int index = 0; index < input.length(); index++) {
            // search for the first escapable character in input
            if(mapEscapeChars.indexOf(input.charAt(index)) >= 0) {
                return input.substring(0, index) + escapeSymbol + input.substring(index);
            }
        }
        // the default case is that the string could not be escaped
        return input;
    }
}
