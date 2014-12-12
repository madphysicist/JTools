/*
 * SyntaxException.java (Exception: com.madphysicist.tools.io.FilePathManager)
 *
 * Mad Physicist JTools Project (I/O Tools)
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
package com.madphysicist.tools.io;

/**
 * @brief A generic exception class for reporting syntax errors.
 *
 * The purpose of this class is to generate a nice default exception message. There are two types of constructors for
 * this exception: ones that take an explicit message and ones that do not.
 *
 * There are no constructors that accept a cause. Naturally many syntax errors will be caused by other exceptions such
 * as `NumberFormatException`. In this case, the user should invoke `initCause(Throwable)` immediately after
 * constructing this exception.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 11 Dec 2014 - J. Fox-Rabinovitz - Created
 * @since 1.1
 */
public class SyntaxException extends Exception //IOException
{
    /**
     * The version ID for serialization.
     *
     * @serial Increment the least significant three digits when compatibility
     * is not compromised by a structural change (e.g. adding a new field with
     * a sensible default value), and the upper digits when the change makes
     * serialized versions of of the class incompatible with previous releases.
     * @since 1.0.0
     */
    private static final long serialVersionUID = 1000L;

    /**
     * @brief The source of the exception.
     *
     * The contents of this field are entirely user-dependent. However, it may be meaningless if `#sourceSet` is
     * `false`.
     *
     * @since 1.0.0
     */
    private String source;

    /**
     * @brief Determines whether or not the `source` string for this exception has been set.
     *
     * @since 1.0.0
     */
    private boolean sourceSet;

    /**
     * @brief The line number on which this exception occurred.
     *
     * The contents of this field are entirely user-dependent. However, it may be meaningless if `#lineSet` is `false`.
     *
     * @since 1.0.0
     */
    private int line;

    /**
     * @brief Determines whether or not the `line` number for this exception has been set.
     *
     * @since 1.0.0
     */
    private boolean lineSet;

    /**
     * @brief The column number at which this exception occurred.
     *
     * The contents of this field are entirely user-dependent. However, it may be meaningless if `#columnSet` is
     * `false`.
     *
     * @since 1.0.0
     */
    private int column;

    /**
     * @brief Determines whether or not the `column` number for this exception has been set.
     *
     * @since 1.0.0
     */
    private boolean columnSet;

    /**
     * @brief Constructs an exception with no message, source or place.
     *
     * A message will not be generated with this constructor.
     *
     * @since 1.0.0
     */
    public SyntaxException()
    {
        super();
        clearSource();
        clearLine();
        clearColumn();
    }

    /**
     * @brief Constructs an exception with the specified line or column number and no source.
     *
     * The default message will read <b>SyntaxException at \<line|column\> {place}</b>.
     *
     * @param place The line or column at which the exception occurred. Which one it is is determined by the `isColumn`
     * parameter. The place that is not set will be set to zero.
     * @param isColumn Determines if the `place` is a line or column number. If `true`, it will be interpreted as a
     * column. If `false`, it will be a line.
     * @since 1.0.0
     */
    public SyntaxException(int place, boolean isColumn)
    {
        super("SyntaxException at " + (isColumn ? "column " : "line ") + place);
        clearSource();
        setPlace(place, isColumn);
    }

    /**
     * @brief Constructs an exception with the specified line and column numbers and no source.
     *
     * The default message will read <b>SyntaxException at line {line}, column {column}</b>.
     *
     * @param line The line at which the exception occurred.
     * @param column The column at which the exception occurred.
     * @since 1.0.0
     */
    public SyntaxException(int line, int column)
    {
        super("SyntaxException at line " + line + ", column " + column);
        clearSource();
        setLine(line);
        setColumn(column);
    }

    /**
     * @brief Constructs an exception with only the source specified.
     *
     * The default message will read <b>SyntaxException in {source}</b>.
     *
     * @param source The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @since 1.0.0
     */
    public SyntaxException(String source)
    {
        super("SyntaxException in " + source);
        setSource(source);
        clearLine();
        clearColumn();
    }

    /**
     * @brief Constructs an exception with the source and a line or column number specified.
     *
     * The default message will read <b>SyntaxException in {source} at <line|column> {place}</b>.
     *
     * @param source The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @param place The line or column at which the exception occurred. Which one it is is determined by the `isColumn`
     * parameter. The place that is not set will be set to zero.
     * @param isColumn Determines if the `place` is a line or column number. If `true`, it will be interpreted as a
     * column. If `false`, it will be a line.
     * @since 1.0.0
     */
    public SyntaxException(String source, int place, boolean isColumn)
    {
        super("SyntaxException in " + source + " at " + (isColumn ? "column " : "line ") + place);
        setSource(source);
        setPlace(place, isColumn);
    }

    /**
     * @brief Constructs an exception with the specified source, line and column.
     *
     * The default message will read <b>SyntaxException in {source} at line {line}, column {column}</b>.
     *
     * @param source The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @param line The line at which the exception occurred.
     * @param column The column at which the exception occurred.
     * @since 1.0.0
     */
    public SyntaxException(String source, int line, int column)
    {
        super("SyntaxException in " + source + " at line " + line + ", column " + column);
        setSource(source);
        setLine(line);
        setColumn(column);
    }

    /**
     * @brief Constructs an exception with only the source and a non-default message specified.
     *
     * @param message The message to set for this exception. A default message will not be generated.
     * @param source The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @since 1.0.0
     */
    public SyntaxException(String message, String source)
    {
        super(message);
        setSource(source);
        clearLine();
        clearColumn();
    }

    /**
     * @brief Constructs an exception with a non-default message and the source and a line or column number specified.
     *
     * @param message The message to set for this exception. A default message will not be generated.
     * @param source The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @param place The line or column at which the exception occurred. Which one it is is determined by the `isColumn`
     * parameter. The place that is not set will be set to zero.
     * @param isColumn Determines if the `place` is a line or column number. If `true`, it will be interpreted as a
     * column. If `false`, it will be a line.
     * @since 1.0.0
     */
    public SyntaxException(String message, String source, int place, boolean isColumn)
    {
        super(message);
        setSource(source);
        setPlace(place, isColumn);
    }

    /**
     * @brief Constructs an exception with a non-default message and the specified source, line and column.
     *
     * @param message The message to set for this exception. A default message will not be generated.
     * @param source The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @param line The line at which the exception occurred.
     * @param column The column at which the exception occurred.
     * @since 1.0.0
     */
    public SyntaxException(String message, String source, int line, int cloumn)
    {
        super(message);
        setSource(source);
        setLine(line);
        setColumn(cloumn);
    }

    /**
     * @brief Retrieves the source of the exception.
     *
     * The source may be `null`, especially if `isSourceSet()` returns `false`.
     *
     * @return The source of the exception. This can be a file name, the expression being parsed, or any other
     * user-determined identifier.
     * @since 1.0.0
     */
    public String source() { return source; }

    /**
     * @brief Determines if the source of the exception contains meaningful information.
     *
     * @return `true` if the value returned by `source()` has been set explicitly, `false` if it is garbage. The value
     * of `source()` will be `null` in the latter case.
     * @since 1.0.0
     */
    public boolean isSourceSet() { return sourceSet; }

    /**
     * @brief Retrieves the line number on which the exception occurred.
     *
     * The line number may be zero, especially if `isLineSet()` returns `false`.
     *
     * @return The thrower-defined line number at which the exception occurred.
     * @since 1.0.0
     */
    public int lineNumber() { return line; }

    /**
     * @brief Determines if the line number of the exception contains meaningful information.
     *
     * @return `true` if the value returned by `lineNumber()` has been set explicitly, `false` if it is garbage. The
     * value of `lineNumber()` will be zero in the latter case.
     * @since 1.0.0
     */
    public boolean isLineSet() { return lineSet; } 

    /**
     * @brief Retrieves the column number on which the exception occurred.
     *
     * The column number may be zero, especially if `isColumnSet()` returns `false`.
     *
     * @return The thrower-defined column number at which the exception occurred.
     * @since 1.0.0
     */
    public int columnNumber() { return column; }

    /**
     * @brief Determines if the column number of the exception contains meaningful information.
     *
     * @return `true` if the value returned by `columnNumber()` has been set explicitly, `false` if it is garbage. The
     * value of `columnNumber()` will be zero in the latter case.
     * @since 1.0.0
     */
    public boolean isColumnSet() { return columnSet; }

    /**
     * @brief Sets the source of this exception and marks it as set.
     *
     * The field `sourceSet` is set to `true` even if `source` is `null`.
     *
     * @param source The source of this exception.
     * @since 1.0.0
     */
    private void setSource(String source)
    {
        this.source = source;
        this.sourceSet = true;
    }

    /**
     * @brief Ensures that the source is set to a trash value and marked as unset.
     *
     * The field `sourceSet` is always marked `false`.
     *
     * @since 1.0.0
     */
    private void clearSource()
    {
        this.source = null;
        this.sourceSet = false;
    }

    /**
     * @brief Sets the line number of this exception and marks it as set.
     *
     * The field `lineSet` is set to `true` even if `line` is zero.
     *
     * @param line The line number of this exception.
     * @since 1.0.0
     */
    private void setLine(int line)
    {
        this.line = line;
        this.lineSet = true;
    }

    /**
     * @brief Ensures that the line number is set to a trash value and marked as unset.
     *
     * The field `lineSet` is always marked `false`.
     *
     * @since 1.0.0
     */
    private void clearLine()
    {
        this.line = 0;
        this.lineSet = false;
    }

    /**
     * @brief Sets the column number of this exception and marks it as set.
     *
     * The field `columnSet` is set to `true` even if `column` is zero.
     *
     * @param column The column number of this exception.
     * @since 1.0.0
     */
    private void setColumn(int column)
    {
        this.column = column;
        this.columnSet = true;
    }

    /**
     * @brief Ensures that the column number is set to a trash value and marked as unset.
     *
     * The field `columnSet` is always marked `false`.
     *
     * @since 1.0.0
     */
    private void clearColumn()
    {
        this.column = 0;
        this.columnSet = false;
    }

    /**
     * @brief Sets either the line or column number based on the specified place.
     *
     * The other element is marked as unset;
     *
     * @param place The line or column at which the exception occurred. Which one it is is determined by the `isColumn`
     * parameter. The place that is not set will be set to zero.
     * @param isColumn Determines if the `place` is a line or column number. If `true`, it will be interpreted as a
     * column. If `false`, it will be a line.
     * @since 1.0.0
     */
    private void setPlace(int place, boolean isColumn)
    {
        if(isColumn) {
            clearLine();
            setColumn(place);
        } else {
            setLine(place);
            clearColumn();
        }
    }
}
