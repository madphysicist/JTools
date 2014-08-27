/*
 * package-info.java (Package: com.madphysicist.tools)
 *
 * Mad Physicist JTools Project
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

/**
 * <p>
 * Contains collections of general purpose tools that are not project-specific.
 * Each of the sub-packages of this package are intended to contain a distinct
 * set of tools.
 * </p>
 * <p>
 * This package is an ongoing work. It is not intended to be tied to any single
 * project, but some of the other Mad Physicist projects will depend on this
 * one.
 * </p>
 * <p>
 * <b>A note on version numbers</b><br />
 * Packages are labeled with a major and minor version number. The major number is increased when a package becomes
 * incompatible with previous versions. The minor version is increased when content is added to the package. The {@code
 * {@literal @}since} tag of public classes and sub-packages should reflect the version of the parent package into which
 * they were introduced. Java files have major and minor version numbers as well as a release number. The major number
 * should be incremented whenever the class API changes in a way that is incompatible with previous versions, or a major
 * rewrite occurs. The minor version should be incremented when APIs are added, and the release number should be
 * incremented whenever minor code changes or bug-fixes occur under the hood. The {@code {@literal @}since} tags of all
 * class elements, including subclasses should reflect the version of the enclosing class in which they were introduced.  
 * </p>
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0,  4 Mar 2012 - J. Fox-Rabinovitz: Initial coding.
 * @version 1.1, 22 May 2014 - J. Fox-Rabinovitz: Added proc sub-package.
 * @version 1.2, 22 Jun 2014 - J. Fox-Rabinovitz: Added xml sub-package.
 * @version 1.3, 14 Jul 2014 - J. Fox-Rabinovitz: Added units sub-package.
 * @version 1.4, 24 Jul 2014 - J. Fox-Rabinovitz: Added io sub-package.
 * @version 2.0, 22 Aug 2014 - J. Fox-Rabinovitz: Moved test and javadoc packages to separate project. Also units.
 *                                                These were packages with external (non-Java) dependencies.
 * @since 1.0
 */
package com.madphysicist.tools;
