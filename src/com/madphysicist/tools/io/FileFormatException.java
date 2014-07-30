/*
 * FileFormatException.java (Class: com.madphysicist.tools.io.FileFormatException)
 *
 * Mad Physicist JTools Project (I/O Tools)
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
package com.madphysicist.tools.io;

import java.io.File;
import java.io.IOException;

/**
 * Indicates that a formatting error was found while parsing a text file. The file name as well as the line and column
 * numbers may be supplied in the exception. Although the line and column numbers are completely application-dependent,
 * only positive integers will be mentioned in the default message.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 24 Jul 2014 - J. Fox-Rabinovitz - Initial coding.
 * @since 1.0.0
 */
public class FileFormatException extends IOException
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
     * The file in which this exception occurred. May be {@code null}.
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

    public FileFormatException(File file, int line)
    {
        this(file, line, 0);
    }

    public FileFormatException(File file, int line, int column)
    {
        super(makeMessage(file, line, column, null));
        this.file = file;
        this.line = line;
        this.column = column;
    }

    public FileFormatException(File file, int line, Throwable cause)
    {
        this(file, line, 0, cause);
    }

    public FileFormatException(File file, int line, int column, Throwable cause)
    {
        super(makeMessage(file, line, column, cause.getMessage()), cause);
        this.file = file;
        this.line = line;
        this.column = column;
    }

    public FileFormatException(File file, int line, String description, Throwable cause)
    {
        this(file, line, 0, description, cause);
    }

    public FileFormatException(File file, int line, int column, String description, Throwable cause)
    {
        super(makeMessage(file, line, column, description), cause);
        this.file = file;
        this.line = line;
        this.column = column;
    }

    public File getFile()
    {
        return file;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    /**
     * Displays sample messages from exceptions of this type for different combinations of input parameters.
     *
     * @param args an array of command-line arguments, always ignored.
     * @since 1.0.0
     */
    public static void main(String[] args)
    {
        File file = new File("/etc/bashrc");
        int line = 10;
        int column = 5;
        String description = "Illegal syntax, expecting )";
        Throwable cause = new NumberFormatException("Expecting a number");
        System.out.println(new FileFormatException(new File("/etc/bashrc"), 20).getMessage());
    }

    /**
     * Generates a message string based on the input parameters. Any of the parameters may be omitted by setting it to a
     * value that gets ignored. A sample output string for every possible combination of parameters can be viewed by
     * running the {@code main()} method of this class.
     *
     * @param file the file in which this exception occurred. Set to {@code null} to omit.
     * @param line the line number on which this exception occurred. Set to {@code 0} or negative to omit. 
     * @param column the column number in which this exception occurred. Set to {@code 0} or negative to omit.
     * @param description a description of the error that caused this exception. Set to {@code null} to omit.
     * @return a string containing all of the input parameters as an informative message.
     * @since 1.0.0
     */
    private static String makeMessage(File file, int line, int column, String description)
    {
        StringBuilder sb = new StringBuilder("Error");

        if(file != null)
            sb.append(" in ").append(file.getPath());

        if(line > 0) {
            if(file == null)
                sb.append(" on line ");
            else
                sb.append(':');
            sb.append(line);
        }

        if(column > 0) {
            if(line > 0)
                sb.append(':');
            else
                sb.append(" in column ");
            sb.append(column);
        }

        if(description != null)
            sb.append(": ").append(description);

        return sb.toString();
    }
}
