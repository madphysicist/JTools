/*
 * FileParseException.java (Class: com.madphysicist.tools.io.FileParseException)
 *
 * Mad Physicist JTools Project (I/O Tools)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Joseph Fox-Rabinovitz
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
package com.madphysicist.tools.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p>
 * Indicates that a formatting error was found while parsing a text stream. The file name as well as the line and column
 * numbers may be supplied in the exception. Although the line and column numbers are completely application-dependent,
 * only positive integers will be mentioned in the default message, implying a one-based system.
 * </p>
 * <p>
 * The exception message for this class is always auto generated. The only way to specify a totally custom message is to
 * invoke the full constructor with the {@code file} and {@code cause} parameters set to {@code null}, the {@code line}
 * and {@code column} parameters to numbers less than one, and the {@code type} parameter to the empty string. The
 * message will then default to the description.
 * </p>
 * <p>
 * There are two other quirks in the way input parameters tweak the exception message. The default exception type of
 * {@code null} actually redirects to {@value #DEFAULT_TYPE}. To omit the type entirely, set it to an empty string.
 * Constructors that have a cause but no description will use the message of the cause as the description. Constructors
 * that have both a cause and a description explicitly specified will not replace a {@code null} description.
 * </p>
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 24 Jul 2014 - J. Fox-Rabinovitz - Initial coding.
 * @version 1.1.0, 04 Aug 2014 - J. Fox-Rabinovitz - Added type parameter and renamed from FileFormatException.
 * @since 1.0
 */
public class FileParseException extends IOException
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when
     * compatibility is not compromised by a structural change (e.g. adding
     * a new field with a sensible default value), and the upper digits when
     * the change makes serialized versions of of the class incompatible
     * with previous releases.
     * @since 1.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * The default type of the exception.
     *
     * @since 1.1.0
     */
    private static final String DEFAULT_TYPE = "Error";

    /**
     * The file in which this exception occurred. May be {@code null} since not all text streams are necessarily files.
     *
     * @serial
     * @since 1.0.0
     */
    private final File file;

    /**
     * The line number on which this exception occurred. May be zero or negative to indicate missing information.
     *
     * @serial
     * @since 1.0.0
     */
    private final int line;

    /**
     * The number of the column on which this exception occurred. May be zero or negative to indicate missing
     * information.
     *
     * @serial
     * @since 1.0.0
     */
    private final int column;

    /**
     * The type of condition that caused this exception describes. This reference will never be {@code null}, but it may
     * be an empty string. {@code null} inputs redirect to {@link #DEFAULT_TYPE}.
     *
     * @serial
     * @since 1.1.0
     */
    private final String type;

    /**
     * A description of the condition that caused the exception. Usually either a useful comment or a description of the
     * expected value. A {@code null} value indicates a missing description.
     *
     * @serial
     * @since 1.1.0
     */
    private final String description;

    /**
     * Constructs an exception with the specified file and line number. The condition type will be set to the default
     * value of {@value #DEFAULT_TYPE}. This exception will not have a column number, description or cause.
     *
     * @param file the file in which the exception occurred. May be {@code null}, in which case it will not appear in
     * the message.
     * @param line the number of the line on which the exception occurred. The line number is assumed to be one-based,
     * and will not appear in the message if it is zero or negative.
     * @since 1.0.0
     */
    public FileParseException(File file, int line)
    {
        this(file, line, 0);
    }

    /**
     * Constructs an exception with the specified file, line and column. The condition type will be set to the default
     * value of {@value #DEFAULT_TYPE}. This exception will not have a description or cause.
     *
     * @param file the file in which the exception occurred. May be {@code null}, in which case it will not appear in
     * the message.
     * @param line the number of the line on which the exception occurred. The line number is assumed to be one-based,
     * and will not appear in the message if it is zero or negative.
     * @param column the number of the column on which the exception occurred. The column number is assumed to be
     * one-based, and will not appear in the message if it is zero or negative.
     * @since 1.0.0
     */
    public FileParseException(File file, int line, int column)
    {
        this(file, line, column, null, null, null);
    }

    /**
     * Constructs an exception with the specified file, line, column and cause. No column number is associated with this
     * exception. The condition type will be set to the default value of {@value #DEFAULT_TYPE}. The description of the
     * exception will be taken from the message of the cause, if the cause is not {@code null}.
     *
     * @param file the file in which the exception occurred. May be {@code null}, in which case it will not appear in
     * the message.
     * @param line the number of the line on which the exception occurred. The line number is assumed to be one-based,
     * and will not appear in the message if it is zero or negative.
     * @param cause the cause of the exception. May be {@code null}.
     * @since 1.0.0
     */
    public FileParseException(File file, int line, Throwable cause)
    {
        this(file, line, 0, cause);
    }

    /**
     * Constructs an exception with the specified file, line, column and cause. The condition type will be set to the
     * default value of {@value #DEFAULT_TYPE}. The description of the exception will be taken from the message of the
     * cause, if the cause is not {@code null}.
     *
     * @param file the file in which the exception occurred. May be {@code null}, in which case it will not appear in
     * the message.
     * @param line the number of the line on which the exception occurred. The line number is assumed to be one-based,
     * and will not appear in the message if it is zero or negative.
     * @param column the number of the column on which the exception occurred. The column number is assumed to be
     * one-based, and will not appear in the message if it is zero or negative.
     * @param cause the cause of the exception. May be {@code null}.
     * @since 1.0.0
     */
    public FileParseException(File file, int line, int column, Throwable cause)
    {
        this(file, line, column, null, (cause == null) ? null : cause.getMessage(), cause);
    }

    /**
     * Constructs an exception with the specified file, line number, description and cause. No column number is
     * associated with this exception. The condition type will be set to the default value of {@value #DEFAULT_TYPE}. 
     *
     * @param file the file in which the exception occurred. May be {@code null}, in which case it will not appear in
     * the message.
     * @param line the number of the line on which the exception occurred. The line number is assumed to be one-based,
     * and will not appear in the message if it is zero or negative.
     * @param description the description of the condition that caused this exception. May be {@code null}, in which
     * case it will not appear in the message.
     * @param cause the cause of the exception. This parameter will not appear directly in the message. May be {@code
     * null}.
     * @since 1.0.0
     */
    public FileParseException(File file, int line, String description, Throwable cause)
    {
        this(file, line, 0, null, description, cause);
    }

    /**
     * The full constructor. This constructor allows all the fields to be configured. The message of the exception is
     * generated automatically based on the input parameters.
     *
     * @param file the file in which the exception occurred. May be {@code null}, in which case it will not appear in
     * the message.
     * @param line the number of the line on which the exception occurred. The line number is assumed to be one-based,
     * and will not appear in the message if it is zero or negative.
     * @param column the number of the column on which the exception occurred. The column number is assumed to be
     * one-based, and will not appear in the message if it is zero or negative.
     * @param type the type of condition that caused this exception. May be {@code null} to indicate the default value
     * of {@value #DEFAULT_TYPE}. To omit from the message entirely, specify an empty string.
     * @param description the description of the condition that caused this exception. May be {@code null}, in which
     * case it will not appear in the message.
     * @param cause the cause of the exception. This parameter will not appear directly in the message. May be {@code
     * null}.
     * @since 1.1.0
     */
    public FileParseException(File file, int line, int column, String type, String description, Throwable cause)
    {
        super(makeMessage(file, line, column, type, description), cause);
        this.file = file;
        this.line = line;
        this.column = column;
        this.type = type;
        this.description = description;
    }

    /**
     * Returns the file in which this exception occurred. This reference may be {@code null} if the reference is not
     * available, for example, when parsing a text stream that is not a file.
     *
     * @return the file in which the exception occurred.
     * @since 1.0.0
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Returns the line on which this exception occurred. Line numbers are assumed to be one-based. Zero or negative
     * line numbers indicate unavailable information.
     *
     * @return the one-based line number of this exception.
     * @since 1.0.0
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Returns the column in which this exception occurred. Column numbers are assumed to be one-based. Zero or negative
     * column numbers indicate unavailable information.
     *
     * @return the one-based column number of this exception.
     * @since 1.0.0
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * Returns the type of the exception. The type classifies the error that caused this exception. The default is
     * {@value #DEFAULT_TYPE}. Common values include "Error", "Warning", "Syntax error". This reference will never be
     * {@code null}, but it may be an empty string.
     *
     * @return the type of condition described by this exception.
     * @since 1.1.0
     */
    public String getType()
    {
        return type;
    }

    /**
     * Retrieves the description of the condition that caused the exception, or {@code null} if not available. The
     * description is usually either a useful comment or a description of the expected value. This string can be
     * equivalent to the exception's message in certain circumstances (specifically, if none of the other fields are
     * set).
     *
     * @return the description of the condition that caused the exception. May be {@code null}.
     * @since 1.1.0
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Displays sample messages from exceptions of this type for different combinations of input parameters.
     *
     * @param args an array of command-line arguments, always ignored.
     * @since 1.0.0
     */
    public static void main(String[] args)
    {
        File theFile = new File("/etc/bashrc");
        int theLine = 25;
        int theColumn = 32;
        String theType = "Warning";
        String theDescription = "Illegal syntax, expecting )";
        Throwable theCause = new NumberFormatException("\"ABC\" is not a number!");

        char[] filler = new char[80];
        Arrays.fill(filler, '━');
        String top = "┏━┳━┳━┳━┳━┳━┳" + new String(filler) + "┓";
        String middle = top.replace('┏', '┣').replace('┓', '┫').replace('┳', '╇');
        String bottom = top.replace('┏', '┗').replace('┓', '┛').replace('┳', '┷');
        String sep = top.replace('┏', '┠').replace('┓', '┨').replace('┳', '┼').replace('━', '─');
        System.out.println(top);
        Arrays.fill(filler, ' ');
        System.out.println("┃F┃L┃C┃T┃D┃C┃Message" + new String(filler, 0, filler.length - "Message".length()) + "┃");
        System.out.println(middle);
        for(int i = 0; i < 128; i++) {
            File file  = (i & 1) == 0 ? null : theFile;
            int  line  = (i & 2) == 0 ?    0 : theLine;
            int column = (i & 4) == 0 ?    0 : theColumn;
            String type;
            if((i & 8) == 0) {
                if((i & 16) == 0) {
                    continue;
                } else {
                    type = null;
                }
            } else {
                if((i & 16) == 0) {
                    type = "";
                } else {
                    type = theType;
                }
            }
            String description = (i & 32) == 0 ? null : theDescription;
            Throwable cause = (i & 64) == 0 ? null : theCause;
            String message = new FileParseException(file, line, column, type, description, cause).getMessage();

            System.out.print('┃');
            System.out.print((i & 1) == 0 ? ' ' : '∙');
            System.out.print('│');
            System.out.print((i & 2) == 0 ? ' ' : '∙');
            System.out.print('│');
            System.out.print((i & 4) == 0 ? ' ' : '∙');
            System.out.print('│');
            if((i & 8) == 0) {
                System.out.print((i & 16) == 0 ? ' ' : '∘');
            } else  {
                System.out.print('•');
            }
            System.out.print('│');
            System.out.print((i & 32) == 0 ? ' ' : '∙');
            System.out.print('│');
            System.out.print((i & 64) == 0 ? ' ' : '∙');
            System.out.print('│');
            System.out.print(message + new String(filler, 0, filler.length - message.length()));
            System.out.println('┃');
            System.out.println((i < 127) ? sep : bottom);
        }
    }

    /**
     * Generates a message string based on the input parameters. Any of the parameters may be omitted by setting it to a
     * value that gets ignored. To create a message string that is just the description, set {@code file} to {@code
     * null}, {@code line} and {@code column} to zero, and {@code type} to the empty string. A sample output string for
     * every possible combination of parameters can be viewed by running the {@code main()} method of this class.
     *
     * @param file the file in which this exception occurred. Set to {@code null} to omit.
     * @param line the line number on which this exception occurred. Set to {@code 0} or negative to omit. 
     * @param column the column number in which this exception occurred. Set to {@code 0} or negative to omit.
     * @param type the type of error, used as a prefix to the message. Set to {@code null} to use the default value of
     * {@code "Error"}. Set to empty to omit entirely.
     * @param description a description of the error that caused this exception. Set to {@code null} to omit.
     * @return a string containing all of the input parameters as an informative message.
     * @since 1.0.0
     */
    private static String makeMessage(File file, int line, int column, String type, String description)
    {
        if(type == null)
            type = DEFAULT_TYPE;

        // Add prefix
        StringBuilder sb = new StringBuilder(type);

        // Add the file name
        if(file != null) {
            if(sb.length() > 0)
                sb.append(" in ");
            else
                sb.append("In ");
            sb.append(file.getPath());
        }

        // Add the line number
        if(line > 0) {
            if(file == null) {
                if(sb.length() > 0)
                    sb.append(" on line ");
                else
                    sb.append("On line ");
            } else {
                sb.append(':');
            }
            sb.append(line);
        }

        // Add the column number
        if(column > 0) {
            if(line > 0) {
                sb.append(':');
            } else {
                if(sb.length() > 0)
                    sb.append(" in column ");
                else
                    sb.append("In column ");
            }
            sb.append(column);
        }

        // Add the description
        if(description != null) {
            if(sb.length() > 0)
                sb.append(": ");
            sb.append(description);
        }

        // Return the complete message
        return sb.toString();
    }
}
