/*
 * ExtendedProperties.java
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Provides extended functionality to the {@link java.util.Properties
 * Properties} class, including convenience loaders and methods to retreive
 * values as primitive, array, and other types. Note that the search in default
 * properties will be done regardless of whether the default property set is
 * {@code ExtendedProperties} or not.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 2 Nov 2012
 * @since 1.0.0.0
 */
public class ExtendedProperties extends Properties
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when compatibility
     * is not compromised by a structural change (e.g. adding a new field with
     * a sensible default value), and the upper digits when the change makes
     * serialized versions of of the class incompatible with previous releases.
     * @since 1.0.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * Initializes a property set with no parent properties. If a property is
     * not found in this set, no other set will be searched.
     * @since 1.0.0.0
     */
    public ExtendedProperties()
    {
        super();
    }

    /**
     * Initializes a property set with the specified parent properties. If a
     * property is not found in this set, the parent property sets will be
     * searched recursively.
     *
     * @param defaults the default values to use if a property is not found in
     * this property set.
     * @since 1.0.0.0
     */
    public ExtendedProperties(Properties defaults)
    {
        super(defaults);
    }

    /**
     * Loads properties from a property resource. The rules for searching for
     * resources and loading property files can be found in the documentation
     * of {@link ClassLoader#getResourceAsStream(java.lang.String)} and {@link
     * Properties#load(java.io.InputStream)}, respectively.
     *
     * @param baseName the name of the resource to load.
     * @throws IOException if the resource can not be found or loaded for any
     * reason.
     * @since 1.0.0.0
     */
    public void loadFromResource(String baseName) throws IOException
    {
        try(InputStream input = ClassLoader.getSystemResourceAsStream(baseName)) {
            load(input);
        }
    }

    /**
     * Loads a property file into this property set.
     *
     * @param fileName the name of the file to load.
     * @throws IOException if an error occurrs while searching, opening or
     * reading the file.
     * @since 1.0.0.0
     */
    public void loadFromFile(String fileName) throws IOException
    {
        try(InputStream input = new FileInputStream(fileName)) {
            load(input);
        }
    }

    /**
     * Imports the contents of a property resource bundle into this porperty
     * set. This is similar to {@link #loadFromResource(java.lang.String)},
     * except that the search functionality used with resource bundles is used.
     *
     * @param basename the name of the property resource bundle to import.
     * @throws NullPointerException if {@code baseName} is {@code null}.
     * @throws ClassCastException if the resource contains non-string values for
     * any of its keys.
     * @throws MissingResourceException if the specified bundle can not be
     * found.
     * @see ResourceBundle#getBundle(java.lang.String)
     * @since 1.0.0.0
     */
    public void importResourceBundle(String basename) throws NullPointerException, ClassCastException, MissingResourceException
    {
        ResourceBundle bundle = ResourceBundle.getBundle(basename);
        importResourceBundle(bundle);
    }

    /**
     * Imports all of the keys of a loaded property resource bundle into this
     * set. The resource bundle must contain only string values for all of its
     * keys.
     *
     * @param bundle the bundle to import.
     * @throws NullPointerException if {@code bundle} is {@code null}.
     * @throws ClassCastException if the bundle contains non-string values for
     * any of its keys.
     * @since 1.0.0.0
     */
    public void importResourceBundle(ResourceBundle bundle) throws NullPointerException, ClassCastException
    {
        for(String key : bundle.keySet()) {
            try {
                setProperty(key, bundle.getString(key));
            } catch(ClassCastException cce) {}
        }
    }

    /**
     * Imports the contents of a map into this property set.
     *
     * @param map the map to import.
     * @throws NullPointerException if {@code map} is {@code null}.
     * @since 1.0.0.0
     */
    public void importMap(Map<String, String> map) throws NullPointerException
    {
        for(Map.Entry<String, String> entry : map.entrySet())
            setProperty(entry.getKey(), entry.getValue());
    }

    /**
     * Sets a property using a {@link Property} object.
     *
     * @param property the key and value for the property to set.
     * @return the previous value for the specified key.
     * @since 1.0.0.0
     */
    public String setProperty(Property property)
    {
        // using cast instead of *.toString() so that null case does not require special treatment
        return (String)setProperty(property.getKey(), property.getValue());
    }

    /**
     * Retreives a property as an integer value. If the property can not be
     * found or can not be parsed as an integer, no exception is thrown and the
     * default value is returned.
     *
     * @param key the property to search for.
     * @param defaultValue the value to return if the key could not be found or
     * the value could not be parsed as an integer.
     * @return the value for {@code key} as an {@code int}, or {@code
     * defaultValue}.
     * @since 1.0.0.0
     */
    public int getIntegerProperty(String key, int defaultValue)
    {
        String property = getProperty(key);
        if(property == null)
            return defaultValue;
        int value;
        try {
            value = Integer.parseInt(property);
        } catch(NumberFormatException nfe) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Retreives a property as an integer value. If the property can not be
     * found or can not be parsed as an integer, an exception is thrown.
     *
     * @param key the property to search for.
     * @return the value for {@code key} as an {@code int}.
     * @throws MissingResourceException if {@code key} can not be found in this
     * property set or one of its parents.
     * @throws NumberFormatException if the specified value can not be parsed as
     * an integer.
     * @since 1.0.0.0
     */
    public int getIntegerProperty(String key) throws MissingResourceException, NumberFormatException
    {
        String property = getProperty(key);
        if(property == null)
            throw new MissingResourceException("null", getClass().getSimpleName(), key);
        return Integer.parseInt(property);
    }

    /**
     * Retreives a property as a double value. If the property can not be found
     * or can not be parsed as an integer, no exception is thrown and the
     * default value is returned.
     *
     * @param key the property to search for.
     * @param defaultValue the value to return if the key could not be found or
     * the value could not be parsed as a a double.
     * @return the value for {@code key} as an {@code double}, or {@code
     * defaultValue}.
     * @since 1.0.0.0
     */
    public double getDoubleProperty(String key, double defaultValue)
    {
        String property = getProperty(key);
        if(property == null)
            return defaultValue;
        double value;
        try {
            value = Double.parseDouble(property);
        } catch(NumberFormatException nfe) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Retreives a property as a double value. If the property can not be found
     * or can not be parsed as a double, an exception is thrown.
     *
     * @param key the property to search for.
     * @return the value for {@code key} as a {@code double}.
     * @throws MissingResourceException if {@code key} can not be found in this
     * property set or one of its parents.
     * @throws NumberFormatException if the specified value can not be parsed as
     * a double.
     * @since 1.0.0.0
     */
    public double getDoubleProperty(String key) throws MissingResourceException, NumberFormatException
    {
        String property = getProperty(key);
        if(property == null)
            throw new MissingResourceException("null", getClass().getSimpleName(), key);
        return Double.parseDouble(property);
    }

    /**
     * Encapsulates a single key-value pair. This class also implements {@code
     * Map.Entry} as a convenience.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0.0, 9 Nov 2012
     * @serial include
     * @since 1.0.0.0
     */
    @SuppressWarnings("PublicInnerClass")
    public static class Property implements Serializable, Map.Entry<String, String>
    {
        /**
         * The version ID for serialization.
         *
         * @serial Increment the least significant three digits when
         * compatibility is not compromised by a structural change (e.g. adding
         * a new field with a sensible default value), and the upper digits when
         * the change makes serialized versions of of the class incompatible
         * with previous releases.
         * @since 1.0.0.0
         */
        private static final long serialVersionUID = 1000L;

        /**
         * The key of this property.
         *
         * @serial include
         * @since 1.0.0.0
         */
        private String key;

        /**
         * The value of this property.
         *
         * @serial include
         * @since 1.0.0.0
         */
        private String value;
        
        /**
         * Initializes a property with the specified key and an empty value.
         * The value will <b>no</b> be set to {@code null}.
         *
         * @param key the key of this property.
         * @since 1.0.0.0
         */
        public Property(String key)
        {
            this(key, "");
        }

        /**
         * Initializes a property with the specified key and value.
         *
         * @param key the key of this property.
         * @param value the value of this property.
         */
        public Property(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key of this porperty.
         *
         * @return the key of this property.
         * @since 1.0.0.0
         */
        @Override public String getKey()
        {
            return key;
        }

        /**
         * Returns the value of this porperty.
         *
         * @return the value of this property.
         * @since 1.0.0.0
         */
        @Override public String getValue()
        {
            return value;
        }

        /**
         * Sets the value of this porperty.
         *
         * @param value the new value to use for this property.
         * @return the previous value of this property.
         * @since 1.0.0.0
         */
        @Override public String setValue(String value)
        {
            String prevValue = this.value;
            this.value = value;
            return prevValue;
        }

    }
}
