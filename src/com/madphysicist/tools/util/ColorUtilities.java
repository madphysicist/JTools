/*
 * ColorUtilities.java
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

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility library for manipulating colors.
 * <p>
 * This class can not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 05 June 2013
 * @since 1.0.0
 */
public class ColorUtilities
{
    /**
     * A mask for the red, green and blue of an integer color value. The color
     * is assumed to be encoded such that bits 24-31 are alpha, 16-23 are red,
     * 8-15 are green, 0-7 are blue. Applying this mask with binary AND sets the
     * alpha to zero. Applying the inverse of this mask with binary OR sets the
     * alpha to 255.
     *
     * @see Color#Color(int)
     * @see Color#getRGB()
     * @since 1.0.0
     */
    public static final int RGB_MASK = 0x00FFFFFF;

    /**
     * A map containins the names of the HTML colors for parsing. The contents
     * of this map were obtained from {@link
     * http://www.w3schools.com/html/html_colornames.asp}.
     *
     * @since 1.0.0
     */
    private static final Map<String, Color> HTML_COLORS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        HTML_COLORS.put("AliceBlue", new Color(0xF0F8FF));
        HTML_COLORS.put("AntiqueWhite", new Color(0xFAEBD7));
        HTML_COLORS.put("Aqua", new Color(0x00FFFF));
        HTML_COLORS.put("Aquamarine", new Color(0x7FFFD4));
        HTML_COLORS.put("Azure", new Color(0xF0FFFF));
        HTML_COLORS.put("Beige", new Color(0xF5F5DC));
        HTML_COLORS.put("Bisque", new Color(0xFFE4C4));
        HTML_COLORS.put("Black", new Color(0x000000));
        HTML_COLORS.put("BlanchedAlmond", new Color(0xFFEBCD));
        HTML_COLORS.put("Blue", new Color(0x0000FF));
        HTML_COLORS.put("BlueViolet", new Color(0x8A2BE2));
        HTML_COLORS.put("Brown", new Color(0xA52A2A));
        HTML_COLORS.put("BurlyWood", new Color(0xDEB887));
        HTML_COLORS.put("CadetBlue", new Color(0x5F9EA0));
        HTML_COLORS.put("Chartreuse", new Color(0x7FFF00));
        HTML_COLORS.put("Chocolate", new Color(0xD2691E));
        HTML_COLORS.put("Coral", new Color(0xFF7F50));
        HTML_COLORS.put("CornflowerBlue", new Color(0x6495ED));
        HTML_COLORS.put("Cornsilk", new Color(0xFFF8DC));
        HTML_COLORS.put("Crimson", new Color(0xDC143C));
        HTML_COLORS.put("Cyan", new Color(0x00FFFF));
        HTML_COLORS.put("DarkBlue", new Color(0x00008B));
        HTML_COLORS.put("DarkCyan", new Color(0x008B8B));
        HTML_COLORS.put("DarkGoldenRod", new Color(0xB8860B));
        HTML_COLORS.put("DarkGray", new Color(0xA9A9A9));
        HTML_COLORS.put("DarkGreen", new Color(0x006400));
        HTML_COLORS.put("DarkKhaki", new Color(0xBDB76B));
        HTML_COLORS.put("DarkMagenta", new Color(0x8B008B));
        HTML_COLORS.put("DarkOliveGreen", new Color(0x556B2F));
        HTML_COLORS.put("Darkorange", new Color(0xFF8C00));
        HTML_COLORS.put("DarkOrchid", new Color(0x9932CC));
        HTML_COLORS.put("DarkRed", new Color(0x8B0000));
        HTML_COLORS.put("DarkSalmon", new Color(0xE9967A));
        HTML_COLORS.put("DarkSeaGreen", new Color(0x8FBC8F));
        HTML_COLORS.put("DarkSlateBlue", new Color(0x483D8B));
        HTML_COLORS.put("DarkSlateGray", new Color(0x2F4F4F));
        HTML_COLORS.put("DarkTurquoise", new Color(0x00CED1));
        HTML_COLORS.put("DarkViolet", new Color(0x9400D3));
        HTML_COLORS.put("DeepPink", new Color(0xFF1493));
        HTML_COLORS.put("DeepSkyBlue", new Color(0x00BFFF));
        HTML_COLORS.put("DimGray", new Color(0x696969));
        HTML_COLORS.put("DimGrey", new Color(0x696969));
        HTML_COLORS.put("DodgerBlue", new Color(0x1E90FF));
        HTML_COLORS.put("FireBrick", new Color(0xB22222));
        HTML_COLORS.put("FloralWhite", new Color(0xFFFAF0));
        HTML_COLORS.put("ForestGreen", new Color(0x228B22));
        HTML_COLORS.put("Fuchsia", new Color(0xFF00FF));
        HTML_COLORS.put("Gainsboro", new Color(0xDCDCDC));
        HTML_COLORS.put("GhostWhite", new Color(0xF8F8FF));
        HTML_COLORS.put("Gold", new Color(0xFFD700));
        HTML_COLORS.put("GoldenRod", new Color(0xDAA520));
        HTML_COLORS.put("Gray", new Color(0x808080));
        HTML_COLORS.put("Green", new Color(0x008000));
        HTML_COLORS.put("GreenYellow", new Color(0xADFF2F));
        HTML_COLORS.put("HoneyDew", new Color(0xF0FFF0));
        HTML_COLORS.put("HotPink", new Color(0xFF69B4));
        HTML_COLORS.put("IndianRed ", new Color(0xCD5C5C));
        HTML_COLORS.put("Indigo ", new Color(0x4B0082));
        HTML_COLORS.put("Ivory", new Color(0xFFFFF0));
        HTML_COLORS.put("Khaki", new Color(0xF0E68C));
        HTML_COLORS.put("Lavender", new Color(0xE6E6FA));
        HTML_COLORS.put("LavenderBlush", new Color(0xFFF0F5));
        HTML_COLORS.put("LawnGreen", new Color(0x7CFC00));
        HTML_COLORS.put("LemonChiffon", new Color(0xFFFACD));
        HTML_COLORS.put("LightBlue", new Color(0xADD8E6));
        HTML_COLORS.put("LightCoral", new Color(0xF08080));
        HTML_COLORS.put("LightCyan", new Color(0xE0FFFF));
        HTML_COLORS.put("LightGoldenRodYellow", new Color(0xFAFAD2));
        HTML_COLORS.put("LightGray", new Color(0xD3D3D3));
        HTML_COLORS.put("LightGreen", new Color(0x90EE90));
        HTML_COLORS.put("LightPink", new Color(0xFFB6C1));
        HTML_COLORS.put("LightSalmon", new Color(0xFFA07A));
        HTML_COLORS.put("LightSeaGreen", new Color(0x20B2AA));
        HTML_COLORS.put("LightSkyBlue", new Color(0x87CEFA));
        HTML_COLORS.put("LightSlateGray", new Color(0x778899));
        HTML_COLORS.put("LightSteelBlue", new Color(0xB0C4DE));
        HTML_COLORS.put("LightYellow", new Color(0xFFFFE0));
        HTML_COLORS.put("Lime", new Color(0x00FF00));
        HTML_COLORS.put("LimeGreen", new Color(0x32CD32));
        HTML_COLORS.put("Linen", new Color(0xFAF0E6));
        HTML_COLORS.put("Magenta", new Color(0xFF00FF));
        HTML_COLORS.put("Maroon", new Color(0x800000));
        HTML_COLORS.put("MediumAquaMarine", new Color(0x66CDAA));
        HTML_COLORS.put("MediumBlue", new Color(0x0000CD));
        HTML_COLORS.put("MediumOrchid", new Color(0xBA55D3));
        HTML_COLORS.put("MediumPurple", new Color(0x9370DB));
        HTML_COLORS.put("MediumSeaGreen", new Color(0x3CB371));
        HTML_COLORS.put("MediumSlateBlue", new Color(0x7B68EE));
        HTML_COLORS.put("MediumSpringGreen", new Color(0x00FA9A));
        HTML_COLORS.put("MediumTurquoise", new Color(0x48D1CC));
        HTML_COLORS.put("MediumVioletRed", new Color(0xC71585));
        HTML_COLORS.put("MidnightBlue", new Color(0x191970));
        HTML_COLORS.put("MintCream", new Color(0xF5FFFA));
        HTML_COLORS.put("MistyRose", new Color(0xFFE4E1));
        HTML_COLORS.put("Moccasin", new Color(0xFFE4B5));
        HTML_COLORS.put("NavajoWhite", new Color(0xFFDEAD));
        HTML_COLORS.put("Navy", new Color(0x000080));
        HTML_COLORS.put("OldLace", new Color(0xFDF5E6));
        HTML_COLORS.put("Olive", new Color(0x808000));
        HTML_COLORS.put("OliveDrab", new Color(0x6B8E23));
        HTML_COLORS.put("Orange", new Color(0xFFA500));
        HTML_COLORS.put("OrangeRed", new Color(0xFF4500));
        HTML_COLORS.put("Orchid", new Color(0xDA70D6));
        HTML_COLORS.put("PaleGoldenRod", new Color(0xEEE8AA));
        HTML_COLORS.put("PaleGreen", new Color(0x98FB98));
        HTML_COLORS.put("PaleTurquoise", new Color(0xAFEEEE));
        HTML_COLORS.put("PaleVioletRed", new Color(0xDB7093));
        HTML_COLORS.put("PapayaWhip", new Color(0xFFEFD5));
        HTML_COLORS.put("PeachPuff", new Color(0xFFDAB9));
        HTML_COLORS.put("Peru", new Color(0xCD853F));
        HTML_COLORS.put("Pink", new Color(0xFFC0CB));
        HTML_COLORS.put("Plum", new Color(0xDDA0DD));
        HTML_COLORS.put("PowderBlue", new Color(0xB0E0E6));
        HTML_COLORS.put("Purple", new Color(0x800080));
        HTML_COLORS.put("Red", new Color(0xFF0000));
        HTML_COLORS.put("RosyBrown", new Color(0xBC8F8F));
        HTML_COLORS.put("RoyalBlue", new Color(0x4169E1));
        HTML_COLORS.put("SaddleBrown", new Color(0x8B4513));
        HTML_COLORS.put("Salmon", new Color(0xFA8072));
        HTML_COLORS.put("SandyBrown", new Color(0xF4A460));
        HTML_COLORS.put("SeaGreen", new Color(0x2E8B57));
        HTML_COLORS.put("SeaShell", new Color(0xFFF5EE));
        HTML_COLORS.put("Sienna", new Color(0xA0522D));
        HTML_COLORS.put("Silver", new Color(0xC0C0C0));
        HTML_COLORS.put("SkyBlue", new Color(0x87CEEB));
        HTML_COLORS.put("SlateBlue", new Color(0x6A5ACD));
        HTML_COLORS.put("SlateGray", new Color(0x708090));
        HTML_COLORS.put("Snow", new Color(0xFFFAFA));
        HTML_COLORS.put("SpringGreen", new Color(0x00FF7F));
        HTML_COLORS.put("SteelBlue", new Color(0x4682B4));
        HTML_COLORS.put("Tan", new Color(0xD2B48C));
        HTML_COLORS.put("Teal", new Color(0x008080));
        HTML_COLORS.put("Thistle", new Color(0xD8BFD8));
        HTML_COLORS.put("Tomato", new Color(0xFF6347));
        HTML_COLORS.put("Turquoise", new Color(0x40E0D0));
        HTML_COLORS.put("Violet", new Color(0xEE82EE));
        HTML_COLORS.put("Wheat", new Color(0xF5DEB3));
        HTML_COLORS.put("White", new Color(0xFFFFFF));
        HTML_COLORS.put("WhiteSmoke", new Color(0xF5F5F5));
        HTML_COLORS.put("Yellow", new Color(0xFFFF00));
        HTML_COLORS.put("YellowGreen", new Color(0x9ACD32));
    }

    /**
     * The pattern used to match hexadecimal color values. It contains four
     * named capture groups, each containing two hex digits: r, g, b, a. The
     * last group is optional.
     *
     * @since 1.0.0
     */
    private static final Pattern HEX_COLOR = Pattern.compile("^#" +
            "(?<r>[0-9a-fA-F]{2})(?<g>[0-9a-fA-F]{2})" +
            "(?<b>[0-9a-fA-F]{2})(?<a>[0-9a-fA-F]{2})?$");

    /**
     * Private constructor to prevent the class from being instantiated.
     *
     * @since 1.0.0
     */
    private ColorUtilities() {}

    /**
     * Parses a color string. Colors strings may have one of the following
     * formats:
     * <dl>
     * <dt>#rrggbb[aa]</dt><dd>The value is either six or eight case
     * insensitive hexadecimal digits.</dd>
     * <dt>Name</dt><dd>The value is the literal name of one of the HTML
     * colors.</dd>
     *
     * @param colorString the string to parse.
     * @return the color represented by the input string.
     * @throws IllegalArgumentException if the string does not match one of the
     * specified formats.
     */
    public static Color parseColor(String colorString) throws IllegalArgumentException
    {
        Matcher matcher = HEX_COLOR.matcher(colorString);
        if(matcher.matches()) {
            int r = Integer.parseInt(matcher.group("r"), 16);
            int g = Integer.parseInt(matcher.group("g"), 16);
            int b = Integer.parseInt(matcher.group("b"), 16);
            String aGroup = matcher.group("a");
            int a = (aGroup == null) ? 0xFF : Integer.parseInt(aGroup, 16);
            return new Color(r, g, b, a);
        }
        if(HTML_COLORS.containsKey(colorString))
            return HTML_COLORS.get(colorString);
        throw new IllegalArgumentException("No such color: " + colorString);
    }

    /**
     * Returns an HTML color for the specified name. Search is case insensitive.
     *
     * @param name the name to search for.
     * @return the color for the specified name, or {@code null} if {@code name}
     * does not correspond to an HTML color.
     * @since 1.0.0
     */
    public Color getHTMLColor(String name)
    {
        return HTML_COLORS.get(name);
    }

    /**
     * Finds the HTML color closest to the specified color. Distance between
     * colors is computed using a second degree norm across the RGB components.
     * The alpha value is ignored.
     *
     * @param color the color to compare.
     * @return the name of the HTML color closest to the specified color.
     * @see #getHTMLColor(String)
     * @see #getHTMLName(Color)
     * @since 1.0.0
     */
    public static String getNearestHTMLName(Color color)
    {
        double minDist = 0.0;
        String minName = null;

        double rDelta, gDelta, bDelta, norm;

        for(Map.Entry<String, Color> html : HTML_COLORS.entrySet()) {
            Color htmlColor = html.getValue();
            rDelta = (double)(color.getRed() - htmlColor.getRed());
            gDelta = (double)(color.getGreen() - htmlColor.getGreen());
            bDelta = (double)(color.getBlue() - htmlColor.getBlue());

            norm = Math.sqrt(rDelta * rDelta + gDelta * gDelta + bDelta * bDelta);

            if(norm == 0.0) {
                return html.getKey();
            } else if(minName == null || norm < minDist) {
                minDist = norm;
                minName = html.getKey();
            }
        }

        return minName;
    }

    /**
     * Finds the HTML color name of the specified color. The color must be an
     * exact HTML color, otherwise the return value is {@code null}. The alpha
     * value is ignored.
     *
     * @param color the color to search for.
     * @return the HTML name of the specified color, or {@code null} if it is
     * not an HTML color.
     * @see #getNearestHTMLName(Color)
     * @since 1.0.0
     */
    public static String getHTMLName(Color color)
    {
        for(Map.Entry<String, Color> html : HTML_COLORS.entrySet()) {
            if(equalsIgnoreAlpha(html.getValue(), color))
                return html.getKey();
        }
        return null;
    }

    /**
     * Returns a hexadecimal string representation of the specified color. The
     * string is guaranteed to be parseable by {@code parseColor()}.
     *
     * @param color the color to convert into a string.
     * @return a string representation of the color.
     * @see #parseColor(String)
     * @since 1.0.0
     */
    public static String toString(Color color)
    {
        String format = (color.getAlpha() == 0xFF) ? "#%02X%02X%02X" : "#%02X%02X%02X%02X";
        return String.format(format, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static String toString(Color color, boolean findHTML)
    {

        if(findHTML) {
            String name = getHTMLName(color);
            if(name != null)
                return name;
        }
        return toString(color);
    }

    /**
     * Returns the inverse of a color, preserving the opacity. Each of the RGB
     * components of the color are inverted individually. The alpha value
     * remains intact.
     *
     * @param color the color to invert.
     * @return the inverse of the color. The alpha of the return value is the
     * same as the alpha of the input.
     * @since 1.0.0
     */
    public static Color inverse(Color color)
    {
        return new Color(0xFF - color.getRed(),
                         0xFF - color.getGreen(),
                         0xFF - color.getBlue(),
                         color.getAlpha());
    }

    /**
     * Tests if two colors are equal neglecting transparancy. Two colors are
     * considered equal if and only if their red, green and blue components are
     * all identical.
     *
     * @param c1 the first color.
     * @param c2 the second color.
     * @return {@code true} if the colors are equal ignoring transparency,
     * {@code false} otherwise.
     * @see Color#equals(Object)
     * @since 1.0.0
     */
    public static boolean equalsIgnoreAlpha(Color c1, Color c2)
    {
        return (c1.getRGB() & RGB_MASK) == (c2.getRGB() & RGB_MASK);
    }
}
