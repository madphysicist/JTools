/*
 * LogTagletTest.java
 *
 * Mad Physicist JTools Project (Javadoc Utilities)
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

package com.madphysicist.tools.javadoc;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;
import java.util.Map;
import java.util.TreeMap;
import com.madphysicist.tools.util.TextUtilities;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests each of the methods of {@link
 * com.madphysicist.tools.javadoc.LogTaglet}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 21 Sept 2012
 * @since 1.0.0.0
 */
public class LogTagletTest
{
    /**
     * A sample taglet used to run tests on.
     * @since 1.0.0.0
     */
    private LogTaglet testTaglet;

    /**
     * Common set up for the entire class. This method initializes the taglet
     * to test.
     *
     * @since 1.0.0.0
     */
    @BeforeClass public void setUpClass()
    {
        testTaglet = new LogTaglet();
    }

    /**
     * New taglet test of register method of class LogTaglet.
     * This checks that the taglet is added correctly to a map in which there is
     * no mapping to the taglet's name.
     *
     * @since 1.0.0.0
     */
    @Test public void testRegister_newTaglet()
    {
        System.out.println("register (new taglet)");
        Map<String, Taglet> testMap = new TreeMap<>();
        LogTaglet.register(testMap);
        Assert.assertTrue(testMap.containsKey(testTaglet.getName()));
        Assert.assertTrue(testMap.get(testTaglet.getName()) instanceof LogTaglet);
    }

    /**
     * Replace taglet test of register method of class LogTaglet.
     * This checks that the taglet is added correctly to a map in which the
     * taglet's name is already present. The previous entry should be
     * overwritten.
     *
     * @since 1.0.0.0
     */
    @Test public void testRegister_replaceTaglet()
    {
        System.out.println("register (replace taglet)");
        Map<String, Taglet> testMap = new TreeMap<>();
        testMap.put(testTaglet.getName(), null);
        Assert.assertTrue(testMap.containsKey(testTaglet.getName()));
        Assert.assertFalse(testMap.get(testTaglet.getName()) instanceof LogTaglet);
        LogTaglet.register(testMap);
        Assert.assertTrue(testMap.containsKey(testTaglet.getName()));
        Assert.assertTrue(testMap.get(testTaglet.getName()) instanceof LogTaglet);
    }

    /**
     * Test of inField method of class LogTaglet.
     * Checks that the method always returns {@code false}.
     *
     * @since 1.0.0.0
     */
    @Test public void testInField()
    {
        System.out.println("inField");
        Assert.assertFalse(testTaglet.inField());
    }

    /**
     * Test of inConstructor method of class LogTaglet.
     * Checks that the method always returns {@code true}.
     *
     * @since 1.0.0.0
     */
    @Test public void testInConstructor()
    {
        System.out.println("inConstructor");
        Assert.assertTrue(testTaglet.inConstructor());
    }

    /**
     * Test of inMethod method of class LogTaglet.
     * Checks that the method always returns {@code true}.
     *
     * @since 1.0.0.0
     */
    @Test public void testInMethod()
    {
        System.out.println("inMethod");
        Assert.assertTrue(testTaglet.inMethod());
    }

    /**
     * Test of inOverview method of class LogTaglet.
     * Checks that the method always returns {@code false}.
     *
     * @since 1.0.0.0
     */
    @Test public void testInOverview()
    {
        System.out.println("inOverview");
        Assert.assertFalse(testTaglet.inOverview());
    }

    /**
     * Test of inPackage method of class LogTaglet.
     * Checks that the method always returns {@code false}.
     *
     * @since 1.0.0.0
     */
    @Test public void testInPackage()
    {
        System.out.println("inPackage");
        Assert.assertFalse(testTaglet.inPackage());
    }

    /**
     * Test of inType method of class LogTaglet.
     * Checks that the method always returns {@code false}.
     *
     * @since 1.0.0.0
     */
    @Test public void testInType()
    {
        System.out.println("inType");
        Assert.assertFalse(testTaglet.inType());
    }

    /**
     * Test of isInlineTag method of class LogTaglet.
     * Checks that the method always returns {@code false}.
     *
     * @since 1.0.0.0
     */
    @Test public void testIsInlineTag()
    {
        System.out.println("isInlineTag");
        Assert.assertFalse(testTaglet.isInlineTag());
    }

    /**
     * Test of getName method of class LogTaglet.
     * Checks that the method always returns the string "{@code joe.log}".
     *
     * @since 1.0.0.0
     */
    @Test public void testGetName()
    {
        System.out.println("getName");
        Assert.assertEquals(testTaglet.getName(), "joe.log");
    }

    /**
     * Null tag test of toString(Tag) method of class LogTaglet.
     * Checks that {@code null} is returned if the input tag is {@code null}.
     *
     * @since 1.0.0.0
     */
    @Test public void testToStringTag_null()
    {
        System.out.println("toString(Tag) (null)");
        Assert.assertNull(testTaglet.toString((Tag)null));
    }

    /**
     * A data provider for {@link #testToStringTag}. Returns an array of input
     * parameters that exercise different scenarios.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null Text: Checks that a null string is returned if the input tag's
     * text is null.</li>
     * <li>Empty Text: Checks that an empty string is returned if the input
     * tag's text is empty.</li>
     * <li>All Blanks: Checks that an empty string is returned if the input
     * tag's text consists entirely of spaces.</li>
     * <li>Type Only: Checks that missing message and reason sections are
     * omitted from the output when only the type of the log entry is specified
     * while the message and reason sections are omitted.</li>
     * <li>Type Only (With Spaces): Checks that missing message and reason
     * sections are omitted from the output when only a type is specified while
     * the message and reason are missing. The type contains escaped
     * spaces.</li>
     * <li>No Reason: Checks that the reason section if omitted from the output
     * when only a type and a one word message are present.</li>
     * <li>Missing Quote: Checks that the reason section is omitted from the
     * output when a type and message are present, but the message is missing
     * a closing quotation mark (because it has been escaped).</li>
     * <li>Quote Inside: Checks that the opening quote character is not
     * recognized when it appears inside the message body rather than at the
     * beginning.</li>
     * <li>Quote End: Checks that the reason section is omitted from the output
     * when the end quote character of the message is at the end of the tag, but
     * padded with extra spaces.</li>
     * <li>Quote No Space: Checks that the end of the message is recognized
     * even when there is no space between the closing quote character of the
     * message and the beginning of the reason.</li>
     * <li>Extra Spaces: Checks that extra spaces between the type, message and
     * reason sections are removed.</li>
     * <li>Normal Escape: Checks that the {@code toString(Tag)} method behaves
     * correctly when the tag contains all three elements, and the spaces in the
     * message are escaped rather than quoted. Both the message and the reason
     * have HTML markup on input, but only the reason should retain unencoded
     * HTML.</li>
     * <li>Normal Quote: Checks that the {@code toString(Tag)} method behaves
     * correctly when the tag contains all three elements, and the spaces in the
     * message are quoted rather than escaped. Both the message and the reason
     * have HTML markup on input, but only the reason should retain unencoded
     * HTML.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, the input tag text and the
     * expected result.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testTagDataProvider")
    private Object[][] testTagDataProvider()
    {
        String lessThan = TextUtilities.htmlEncode("<");
        String greaterThan = TextUtilities.htmlEncode(">");
        String quotation = TextUtilities.htmlEncode("\"");

        String fullExpectedResult = "<dd><dl><dt><b>error:</b>&nbsp;<code>"
                + lessThan + "b" + greaterThan + "do not repeat this at home"
                + lessThan + "/b" + greaterThan + "</code></dt>"
                + "<dd>you <b>will</b> die!</dd></dl></dd>";

        return new Object[][] {
            {"null text", null, null},
            {"empty text", "", ""},
            {"all blanks", "        ", ""},
            {"type only", "error", "<dd><dl><dt><b>error</b></dt></dl></dd>"},
            {"type only with spaces", "oh\\ no!",
                    "<dd><dl><dt><b>oh no!</b></dt></dl></dd>"},
            {"no reason", "error run",
                    "<dd><dl><dt><b>error:</b>&nbsp;<code>run</code></dt></dl></dd>"},
            {"missing quote", "error \"do not repeat\\\"",
                    "<dd><dl><dt><b>error:</b>&nbsp;<code>do not repeat"
                    + quotation + "</code></dt></dl></dd>"},
            {"quote inside", "error d\"o not repeat\" this at home",
                    "<dd><dl><dt><b>error:</b>&nbsp;<code>d" + quotation
                    + "o</code></dt><dd>not repeat\" this at home</dd></dl></dd>"},
            {"quote end", "error \"do not repeat this at home\"  ",
                    "<dd><dl><dt><b>error:</b>&nbsp;"
                    + "<code>do not repeat this at home</code></dt></dl></dd>"},
            {"quote no space", "error \"<b>do not repeat this at home"
                    + "</b>\"you <b>will</b> die!", fullExpectedResult},
            {"extra spaces", "         error            <b>do\\ not\\ repeat\\ "
                    + "this\\ at\\ home</b>            you <b>will</b> die!      ",
                    fullExpectedResult},
            {"normal escape", "error <b>do\\ not\\ repeat\\ this\\ at\\ home</b> "
                    + "you <b>will</b> die!", fullExpectedResult},
            {"normal quote", "error \"<b>do not repeat this at home</b>\" you "
                    + "<b>will</b> die!", fullExpectedResult}
        };
    }

    /**
     * Input test of toString(Tag) method of class LogTaglet. This test method
     * is parametrized with values from the {@link #testTagDataProvider()}
     * array. It ensures that tags with different unusual strings are parsed as
     * specified.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param tagText the input tag text.
     * @param expectedValue the expected HTML code for the input tag text.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testTagDataProvider")
    public void testToStringTag(final String label, final String tagText, final String expectedValue)
    {
        System.out.println("toString(Tag) (" + label + ")");
        Tag testTag = new TagAdapter(tagText);
        Assert.assertEquals(testTaglet.toString(testTag), expectedValue);
    }

    /**
     * A data provider for {@link #testToStringTagArray}. Returns an array of
     * input parameters that exercise different scenarios.
     * <p>
     * The tests made available by this data provider are:
     * <ul>
     * <li>Null: Checks that a null string is returned if the input array is
     * null.</li>
     * <li>Empty: Checks that a null string is returned if the input array is
     * empty</li>
     * <li>All Null and Empty: Checks that the return is just the header when
     * the input array consists of all nulls and empty tags.</li>
     * <li>Contains Null and Empty: Checks that null and empty elements are
     * omitted from the output if the input array has non-empty elements.</li>
     * <li>Normal: Checks that an array of ordinary tags gets parsed and
     * assembled correctly.</li>
     * </ul>
     *
     * @return a two-dimensional array of objects. The outer array represents
     * distinct runs of the test method. The inner Object arrays are parameter
     * lists representing the scenario label, an array of input tags, and the
     * expected result.
     * @since 1.0.0.0
     */
    @DataProvider(name = "testTagArrayDataProvider")
    private Object[][] testTagArrayDataProvider()
    {
        return new Object[][] {
            {"null", null, null},
            {"empty", new Tag[0], null},
            {"all null/empty", new Tag[] {null,
                        new TagAdapter(""), new TagAdapter(null)},
                    "<dt><b>Log Messages:</b></dt>"},
            {"contains null/empty", new Tag[] {new TagAdapter(null),
                        new TagAdapter("error \"something terrible\" "
                                     + "when it hits the fan"), null,
                        new TagAdapter("warning \"something bad\" "
                                     + "when the bricks are made"),
                        new TagAdapter("")},
                    "<dt><b>Log Messages:</b></dt>"
                        + "<dd><dl><dt><b>error:</b>&nbsp;"
                        + "<code>something terrible</code></dt>"
                        + "<dd>when it hits the fan</dd></dl></dd>"
                        + "<dd><dl><dt><b>warning:</b>&nbsp;"
                        + "<code>something bad</code></dt>"
                        + "<dd>when the bricks are made</dd></dl></dd>"},
            {"normal", new Tag[] {
                        new TagAdapter("error \"something terrible\" "
                                     + "when it hits the fan"),
                        new TagAdapter("error something\\ horrible "
                                     + "when it explodes"),
                        new TagAdapter("warning \"something bad\" "
                                     + "when the bricks are made"),
                        new TagAdapter("warning something\\ icky "
                                     + "when the bricks are laid")},
                    "<dt><b>Log Messages:</b></dt>"
                        + "<dd><dl><dt><b>error:</b>&nbsp;"
                        + "<code>something terrible</code></dt>"
                        + "<dd>when it hits the fan</dd></dl></dd>"
                        + "<dd><dl><dt><b>error:</b>&nbsp;"
                        + "<code>something horrible</code></dt>"
                        + "<dd>when it explodes</dd></dl></dd>"
                        + "<dd><dl><dt><b>warning:</b>&nbsp;"
                        + "<code>something bad</code></dt>"
                        + "<dd>when the bricks are made</dd></dl></dd>"
                        + "<dd><dl><dt><b>warning:</b>&nbsp;"
                        + "<code>something icky</code></dt>"
                        + "<dd>when the bricks are laid</dd></dl></dd>"}
        };
    }

    /**
     * Input test of toString(Tag[]) method of class LogTaglet. This test method
     * is parametrized with values from the {@link #testTagArrayDataProvider()}
     * array. It ensures that tag arrays with different unusual combinations of
     * tags are parsed and assembled as specified.
     *
     * @param label the label of the current parameter set. This is used for
     * output to the command line.
     * @param tags an array of input tags. This may be null or empty or it may
     * contain null and empty elements.
     * @param expectedValue the expected HTML code for the input tag text.
     * @since 1.0.0.0
     */
    @Test(dataProvider = "testTagArrayDataProvider")
    public void testToStringTagArray(final String label, final Tag[] tags, final String expectedValue)
    {
        System.out.println("toString(Tag[] (" + label + ")");
        Assert.assertEquals(testTaglet.toString(tags), expectedValue);
    }
}
