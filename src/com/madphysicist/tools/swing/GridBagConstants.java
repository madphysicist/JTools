/*
 * GridBagConstants.java (Class: com.madphysicist.tools.swing.GridBagConstants)
 *
 * Mad Physicist JTools Project (Swing Utilities)
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
package com.madphysicist.tools.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Constants for facilitating work with {@link GridBagLayout} and {@link GridBagConstraints}.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0 18 Nov 2013 - J. Fox-Rabinovitz - Initial Coding.
 * @version 1.0.1 27 May 2014 - J. Fox-Rabinovitz - Added NONE.
 * @since 1.0
 */
public interface GridBagConstants
{
    /**
     * Size to use for the left inset of a component on the west edge of the
     * layout.
     *
     * @since 1.0.0
     */
    public int WEST_INSET = 8;

    /**
     * Size to use for the right inset of a component on the east edge of the
     * layout.
     *
     * @since 1.0.0
     */
    public int EAST_INSET = 8;

    /**
     * Size to use for the top inset of a component on the north edge of the
     * layout.
     *
     * @since 1.0.0
     */
    public int NORTH_INSET = 5;

    /**
     * Size to use for the bottom inset of a component on the south edge of the
     * layout.
     *
     * @since 1.0.0
     */
    public int SOUTH_INSET = 5;

    /**
     * Size to use for the vertical insets of a component not on the north or
     * south edge of the layout.
     *
     * @since 1.0.0
     */
    public int VERTICAL_INSET = 2;

    /**
     * Size to use for the horizonatal insets of a component not on the east or
     * west edge of the layout.
     *
     * @since 1.0.0
     */
    public int HORIZONTAL_INSET = 5;

    /**
     * Empty insets.
     *
     * @since 1.0.1
     */
    public Insets NONE = new Insets(0, 0, 0, 0);

    /**
     * Insets for a component that fills the entire layout in both the vertical
     * and horizontal directions.
     *
     * @since 1.0.0
     */
    public Insets FILL_BOTH = new Insets(NORTH_INSET, WEST_INSET, SOUTH_INSET, EAST_INSET);

    /**
     * Insets for a component that fills the entire layout in the horizontal
     * direction and is on the north edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets FILL_HORIZONTAL_NORTH = new Insets(NORTH_INSET, WEST_INSET, VERTICAL_INSET, EAST_INSET);

    /**
     * Insets for a component that fills the entire layout in the horizontal
     * direction and is neither on the north nor the south edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets FILL_HORIZONTAL_CENTER = new Insets(VERTICAL_INSET, WEST_INSET, VERTICAL_INSET, EAST_INSET);

    /**
     * Insets for a component that fills the entire layout in the horizontal
     * direction and is on the south edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets FILL_HORIZONTAL_SOUTH = new Insets(VERTICAL_INSET, WEST_INSET, SOUTH_INSET, EAST_INSET);

    /**
     * Insets for a component that fills the entire layout in the vertical
     * direction and is on the west edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets FILL_VERTICAL_WEST = new Insets(NORTH_INSET, WEST_INSET, SOUTH_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that fills the entire layout in the vertical
     * direction and is neither on the east nor the west edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets FILL_VERTICAL_CENTER = new Insets(NORTH_INSET, HORIZONTAL_INSET, SOUTH_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that fills the entire layout in the vertical
     * direction and is on the east edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets FILL_VERTICAL_EAST = new Insets(NORTH_INSET, HORIZONTAL_INSET, SOUTH_INSET, EAST_INSET);

    /**
     * Insets for a component that is on the northwest (top-left) corner of the
     * layout.
     *
     * @since 1.0.0
     */
    public Insets NORTHWEST = new Insets(NORTH_INSET, WEST_INSET, VERTICAL_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that is on the north (top) edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets NORTH = new Insets(NORTH_INSET, HORIZONTAL_INSET, VERTICAL_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that is on the northeast (top-right) corner of the
     * layout.
     *
     * @since 1.0.0
     */
    public Insets NORTHEAST = new Insets(NORTH_INSET, HORIZONTAL_INSET, VERTICAL_INSET, EAST_INSET);

    /**
     * Insets for a component that is on the west (left) edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets WEST = new Insets(VERTICAL_INSET, WEST_INSET, VERTICAL_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that is not on any edge or corner of the layout.
     *
     * @since 1.0.0
     */
    public Insets CENTER = new Insets(VERTICAL_INSET, HORIZONTAL_INSET, VERTICAL_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that is on the east (right) edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets EAST = new Insets(VERTICAL_INSET, HORIZONTAL_INSET, VERTICAL_INSET, EAST_INSET);

    /**
     * Insets for a component that is on the southwest (bottom-left) corner of
     * the layout.
     *
     * @since 1.0.0
     */
    public Insets SOUTHWEST = new Insets(VERTICAL_INSET, WEST_INSET, SOUTH_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that is on the south (bottom) edge of the layout.
     *
     * @since 1.0.0
     */
    public Insets SOUTH = new Insets(VERTICAL_INSET, HORIZONTAL_INSET, SOUTH_INSET, HORIZONTAL_INSET);

    /**
     * Insets for a component that is on the southeast (bottom-right) corner of
     * the layout.
     *
     * @since 1.0.0
     */
    public Insets SOUTHEAST = new Insets(VERTICAL_INSET, HORIZONTAL_INSET, SOUTH_INSET, EAST_INSET);
}
