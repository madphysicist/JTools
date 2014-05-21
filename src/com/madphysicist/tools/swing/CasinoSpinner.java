/*
 * CasinoSpinner.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Implements an casino-type spinner that can be spun vertically or horizontally
 * by any number of increments. The spin is performed in response to updates to
 * the spinner's model. Multiple spinners can be registered on the same model.
 * </p>
 * <p>
 * The UI consists of a set of vertically or horizontally stacked strings. The
 * top and bottom (or left and right) thirds are covered with colored gradients
 * that fade toward the center of the component. The currently selected item is
 * outlined by a beveled border.
 * </p>
 * <p>
 * The spinner has direction and orientation attributes. The direction
 * determines which way the spinner can spin. This can be positive, negative, or
 * both. Orientation determines whether the items in the spinner move in a
 * vertical or horizontal direction. A positive vertical spin will move elements
 * upward in the display. A positive horizontal spin will move elements towards
 * the left.
 * </p>
 * <p>
 * At the moment, model values can only be strings. Like most Swing components,
 * this class is not thread safe. This component's action can not be triggered
 * through graphical user interaction. It can only be triggered programatically
 * through the {@code spin()} methods.
 * </p>
 * <p>
 * Most of the properties in this class fire changes when they are altered. The
 * names of the properties of this class which can be listened to are provided
 * as constants named {@code *_PROPERTY}. This does not include the properties
 * of any parent classes. These constants should be used in favor of
 * user-defined strings to avoid confusion. Addtional properties appear in the
 * model.
 * </p>
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0, 11 Feb 2013
 * @version 1.0.1, 10 Apr 2013 - Added orientation property.
 * @since 1.0.0
 */
public class CasinoSpinner extends JPanel
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
    private static final long serialVersionUID = 1001L;

    /**
     * A flag indicating vertical orientation. A vertically oriented spinner
     * will display values that scroll top to bottom or bottom to top. The
     * shades will be drawn at the top and bottom of the component.
     *
     * @see #getOrientation()
     * @see #setOrientation(boolean)
     * @since 1.0.1
     */
    public static final boolean VERTICAL = false;

    /**
     * A flag indicating horizontal orientation. A horizontally oriented spinner
     * will display values that scroll left to right or right to left. The
     * shades will be drawn at the sides of the component.
     *
     * @see #getOrientation()
     * @see #setOrientation(boolean)
     * @since 1.0.1
     */
    public static final boolean HORIZONTAL = true;

    /**
     * The name of the model property. This can be used to register a {@code
     * PropertyChangeListener} on changes to the model. Such a listener would
     * be notified when a different model is set, not when the properties of the
     * model change.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getModel()
     * @see #setModel(CasinoSpinnerModel)
     * @since 1.0.0
     */
    public static final String MODEL_PROPERTY = "model";

    /**
     * The name of the shade color property. This can be used to register a
     * {@code PropertyChangeListener} on changes to the shade color. The shade
     * color is the color of the darker, outer side of the shade. It may be
     * totally or only partially opaque. The transparent side of the shade will
     * be set to the same RGB value with zero alpha, to ensure a smooth
     * transition.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getShadeColor()
     * @see #setShadeColor(Color)
     * @since 1.0.0
     */
    public static final String SHADE_COLOR_PROPERTY = "shadeColor";

    /**
     * The name of the vertical padding property. This can be used to register a
     * {@code PropertyChangeListener} on changes to the vertical padding. The
     * vertical padding is the separation between objects in the display. The
     * bounding box of each of the objects is defined by the largest width and
     * the largest height among all of the objects. The vertical padding is the
     * separation between the bounding boxes of successive objects.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getVerticalPadding()
     * @see #setVerticalPadding(int)
     * @since 1.0.0
     */
    public static final String VERTICAL_PADDING_PROPERTY = "verticalPadding";

    /**
     * The name of the orientation property. This can be used to register a
     * {@code PropertyChangeListener} on changes to the orientation. The
     * orientation determines if the spinner spins vertically or horizontally.
     * Vertical spinner have shades at the top and bottom while horizontal
     * spinners have shades on the sides.
     *
     * @see PropertyChangeListener
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getOrientation()
     * @see #setOrientation(boolean)
     * @since 1.0.1
     */
    public static final String ORIENTATION_PROPERTY = "orientation";

    /**
     * The default color of the opaque side of the shades. The transparent side
     * of the shades defaults to the same color but with a zero alpha value.
     *
     * @since 1.0.0
     */
    private static final Color DEFAULT_SHADE_COLOR = new Color(0.0f, 0.0f, 0.0f, 1.0f);

    /**
     * The default number of padding pixels between objects shown on the screen.
     *
     * @since 1.0.0
     */
    private static final int DEFAULT_VERTICAL_PADDING = 5;

    /**
     * The default orientation of the spinner.
     *
     * @since 1.0.1
     */
    private static final boolean DEFAULT_ORIENTATION = VERTICAL;

    /**
     * An array of the bounds of the current data model. This array is reset
     * when the data changes. The bounds are recalculated whenever this
     * component's font or model changes, as well as when the model's data
     * changes. If the model's data is set to {@code null}, this array is set to
     * {@code null}.
     *
     * @serial
     * @since 1.0.0
     */
    private StringBounds[] dataBounds;

    /**
     * The color of the dark outer side of the shades at the top and bottom of
     * the spinner. This color does not necessarily have to be opaque. In fact,
     * if it has an alpha value of zero, the shades will not be drawn at all.
     * Changes to this field are reported through {@code PropertyChangeEvents}s
     * to listeners on the property named by {@link #SHADE_COLOR_PROPERTY}.
     *
     * @serial
     * @since 1.0.0
     */
    private Color shadeDark;

    /**
     * The translucent version of {@link #shadeDark}. This color preserves the
     * red, green and blue components of the dark color, but sets the alpha to
     * zero. This ensures that the shades are drawn with a consistent color
     * gradient.
     *
     * @serial
     * @since 1.0.0
     */
    private Color shadeLight;

    /**
     * The paint used to color the top shade. This paint is defined by a
     * gradient from {@link #shadeDark} at the top of the shade to {@link
     * #shadeLight} at the bottom of the shade. The paint is recomputed any time
     * the bounds of the component change or the color of the paint changes.
     *
     * @serial
     * @since 1.0.0
     */
    private final PaintedShape shadeTop;

    /**
     * The paint used to color the bottom shade. This paint is defined by a
     * gradient from {@link #shadeLight} at the top of the shade to {@link
     * #shadeDark} at the bottom of the shade. The paint is recomputed any time
     * the bounds of the component change or the color of the paint changes.
     *
     * @serial
     * @since 1.0.0
     */
    private final PaintedShape shadeBottom;

    /**
     * The vertical spacing between objects in the spinner. Changes to this
     * field are reported through {@code PropertyChangeEvents}s to listeners on
     * the property named by {@link #VERTICAL_PADDING_PROPERTY}.
     *
     * @serial
     * @since 1.0.0
     */
    private int verticalPadding;

    /**
     * The maximum vertical size of an object, including the internal padding
     * between successive objects. All objects are allocated this size in the
     * spinner. This is recomputed as necessary as a byproduct of the optimal
     * display size calculation.
     *
     * @serial
     * @since 1.0.0
     */
    private float objectHeight;

    /**
     * The maximum horizontal size of an object. All objects are allocated this
     * size in the spinner. This is recomputed as necessary as a byproduct of
     * the optimal display size calculation.
     *
     * @serial
     * @since 1.0.0
     */
    private float objectWidth;

    /**
     * The orientation of the spinner. Vertical orientation means values
     * scrolling up and down in the spinner, while horizontal orientation means
     * values scrolling left and right.
     *
     * @serial
     * @since 1.0.1
     */
    private boolean orientation;

    /**
     * An internal buffer holding the size of the component, used in layout
     * calculations. This buffer is updated only as necessary whenever the
     * component is resized so that extra calls to {@code getSize()} do not have
     * to be made.
     *
     * @serial
     * @since 1.0.0
     */
    private final Dimension sizeBuffer;

    /**
     * An internal buffer holding the dimensions of the component's border, used
     * in layout calculations. This buffer is updated only as necessary whenever
     * the component's border changes so that extra calls to {@code getInsets()}
     * do not have to be made.
     *
     * @serial
     * @since 1.0.0
     */
    private final Insets insetsBuffer;

    /**
     * An internal buffer holding the drawable bounds of the component, used in
     * layout calculations. This buffer is updated only as necessary whenever
     * the component's size or border properties change. This way, superfluous
     * calls to {@code getSize} and {@code getInsets()} do not need to be made.
     *
     * @serial
     * @since 1.0.0
     */
    private final Rectangle boundsBuffer;

    /**
     * The model that this spinner listens to. When a model is added, this
     * spinner registers a {@code ChangeListener} on the state of the model and
     * a {@code PropertyChangeListener} on its data property. When the model is
     * removed, the listeners are removed from it. The {@code
     * PropertyChangeListener} ensures that the spinner's data display is up to
     * date with the model's data. Setting the model to {@code null} removes
     * all data from the spinner. The model determines the state that is
     * displayed by this component.
     *
     * @see #modelListener
     * @serial
     * @since 1.0.0
     */
    private CasinoSpinnerModel model;

    /**
     * A listener that provides the interface between the model and the spinner.
     * The listener is registered whenever a non-{@code null} model is set, and
     * removed from the old model when the model is changed. The listener is
     * responsible for requesting view updates whenever the model's state
     * changes and a recomputation of the data bounds and view dimensions
     * whenever the model's data changes.
     *
     * @serial
     * @since 1.0.0
     */
    private final ModelListener modelListener;

    /**
     * The listener that is registered to buttons which are set as triggers to
     * this component. The listener triggers a spin on this component if it is
     * not already spinning.
     *
     * @serial
     * @since 1.0.0
     */
    private final ActionListener buttonListener;

    /**
     * The component used to draw a window around the current selection. This
     * component is totally translucent and contains no children. Only its
     * border is drawn to highlight an area around the current selection of the
     * spinner.
     * 
     * @serial
     * @since 1.0.0
     */
    private final JComponent selectionWindow;

    /**
     * An internal buffer holding the dimensions of the {@linkplain
     * #selectionWindow highlighting component}'s border, used in layout
     * calculations. This buffer should not need to be updated after
     * construction since the type of the border does not change, only the
     * component's size.
     *
     * @serial
     * @since 1.0.0
     */
    private final Insets selectionWindowInsets;

    /**
     * Constructs a default spinner with a default model containing no data.
     * This method is provided so that the spinner class can be used as a Java
     * Bean in visual GUI builders and similar tools.
     *
     * @since 1.0.0
     */
    public CasinoSpinner()
    {
        this((List<String>)null);
    }

    /**
     * Constructs a default spinner with the specified data. The data will be
     * copied into a default model. Initializing multiple spinners with the
     * same data will not allow them to share the same model.
     *
     * @param data the list of strings to initialize this spinner model with.
     * This list will be copied into the model. The model will not be {@code
     * null} even if this parameter is.
     * @since 1.0.0
     */
    public CasinoSpinner(List<String> data)
    {
        this(new CasinoSpinnerModel(data));
    }

    /**
     * Constructs a default spinner with the specified model.
     *
     * @param model the model to initialize this spinner with. The model may be
     * shared across multiple spinners. This parameter may be {@code null}
     * @since 1.0.0
     */
    public CasinoSpinner(CasinoSpinnerModel model)
    {
        super(null, true);
        setOpaque(true);

        this.insetsBuffer = new Insets(0, 0, 0, 0);
        this.sizeBuffer = new Dimension();
        this.boundsBuffer = new Rectangle();
        this.shadeTop = new PaintedShape();
        this.shadeBottom = new PaintedShape();
        this.modelListener = new ModelListener();
        this.buttonListener = new ButtonListener();
        this.selectionWindow = new JPanel();
        this.selectionWindowInsets = new Insets(0, 0, 0, 0);

        setDefaults();
        initSelectionWindow();
        addPropertyChangeListeners();
        setModelInternal(model);
        computeShades();
    }

    /**
     * Sets default values for the shades and the vertical padding. This method
     * is intended for use in the constructor.
     *
     * @since 1.0.0
     */
    private void setDefaults()
    {
        setShadeColorInternal(DEFAULT_SHADE_COLOR);
        setOrientation(DEFAULT_ORIENTATION);
        setVerticalPaddingInternal(DEFAULT_VERTICAL_PADDING);
    }

    /**
     * Initializes the {@linkplain #selectionWindow} component. The component is
     * set to be translucent with a {@code BevelBorder}. Its {@linkplain
     * #selectionWindowInsets insets} are initialized as well. The insets will not be
     * changed at any point once they are set because the border type will not
     * change. This method is intended for use in the constructor.
     *
     * @since 1.0.0
     */
    private void initSelectionWindow()
    {
        this.selectionWindow.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.selectionWindow.setOpaque(false);
        add(this.selectionWindow);
        selectionWindow.getInsets(selectionWindowInsets);
    }

    /**
     * Sets up the listeners necessary for this component to react properly to
     * changes to itself. The following is the list of relevant properties:
     * <dl>
     * <dt>font</dt><dd>The entire layout of data elements is recomputed using
     * {@link #setOptimalSize()}. The component is repainted.</dd>
     * <dt>border, insets</dt><dd>The component's {@link #insetsBuffer} is
     * reset. The entire layout of data elements is then recomputed. Once the
     * shades and selection window have been reset as well, {@link
     * #boundsBuffer} is computed and the component is repainted.</dd>
     * <dt>size</dt><dd>The component's {@link #sizeBuffer} is reset. Once the
     * shades and selection window have been reset as well, {@link
     * #boundsBuffer} is computed and the component is repainted. The entire
     * layout of data components is not recomputed in this case. Part of the
     * reason for this is that {@link #setOptimalSize()} usually triggers a
     * change of the size.</dd>
     * </dl>
     * This method is intended for use in the constructor.
     *
     * @since 1.0.0
     */
    private void addPropertyChangeListeners()
    {
        PropertyChangeListener sizePropertyChangeListener = new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent evt) {
                setOptimalSize();
                repaint();
            }
        };
        PropertyChangeListener borderPropertyChangeListener = new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent evt) {
                getInsets(insetsBuffer);
                setOptimalSize();
                getInternalBounds(); // gets bounds, sets shades, highlight, repaints
            }
        };
        ComponentListener componentSizeListener = new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                getSize(sizeBuffer);
                getInternalBounds(); // gets bounds, sets shades, repaints
            }
        };

        addPropertyChangeListener("font", sizePropertyChangeListener);
        addPropertyChangeListener("border", borderPropertyChangeListener);
        addPropertyChangeListener("insets", borderPropertyChangeListener);

        addComponentListener(componentSizeListener);
    }

    /**
     * Returns the current model.
     *
     * @return the current model of this spinner.
     * @since 1.0.0
     */
    public CasinoSpinnerModel getModel()
    {
        return this.model;
    }

    /**
     * Sets the model of the current spinner. If the model is {@code null}, this
     * component will not display anything. This method will fire a property
     * change to all listeners registered to the model property named by {@link
     * #MODEL_PROPERTY}.
     *
     * @param model the new model to set.
     * @since 1.0.0
     */
    public void setModel(CasinoSpinnerModel model)
    {
        CasinoSpinnerModel oldModel = setModelInternal(model);
        firePropertyChange(MODEL_PROPERTY, oldModel, model);
        repaint();
    }

    /**
     * Sets the model of the current spinner. This method is responsible for the
     * internal workings of the model set up without triggering property change
     * events or repainting the component. It is used both in the constructor
     * and in {@link #setModel(CasinoSpinnerModel)}. The {@link #modelListener}
     * is unregistered from the previous model if it was not {@code null}, and
     * registered to the new model if it is not {@code null}. The data of the
     * spinner is reset with {@link #createData()} to reflect the new model.
     *
     * @param model the new model to set. This reference may be {@code null}.
     * @return the previous model. This is used to construct {@code
     * PropertyChangeEvent}s. This reference may be {@code null}.
     * @since 1.0.0
     */
    private CasinoSpinnerModel setModelInternal(CasinoSpinnerModel model)
    {
        CasinoSpinnerModel oldModel = this.model;
        this.model = model;
        if(oldModel != null) {
            oldModel.removeChangeListener(modelListener);
            oldModel.removePropertyChangeListener(modelListener);
        }
        if(model != null) {
            model.addPropertyChangeListener(CasinoSpinnerModel.DATA_PROPERTY, modelListener);
            model.addChangeListener(modelListener);
        }
        createData();
        return oldModel;
    }

    /**
     * Returns the model's data. This method is a convenience for {@code
     * getModel().getData()}.
     *
     * @return the data of the current model, or {@code null} if the model is
     * {@code null}.
     * @see CasinoSpinnerModel#getData()
     * @since 1.0.0
     */
    public List<String> getData()
    {
        return (this.model == null) ? null : this.model.getData();
    }

    /**
     * Sets the model's data. This method is a convenience for {@code
     * getModel.setData(List<String>)}. This method is a no-op if the current
     * model is {@code null}. It does not attempt to create a new model or throw
     * an exception.
     *
     * @param data the data to set for the current model, if it is not {@code
     * null}.
     * @see CasinoSpinnerModel#setData(List)
     * @since 1.0.0
     */
    public void setData(List<String> data)
    {
        if(this.model != null) {
            this.model.setData(data);
            repaint();
        }
    }

    /**
     * Returns the vertical padding between successive objects in the spinner.
     *
     * @return the padding between successive objects in the spinner.
     * @since 1.0.0
     */
    public int getVerticalPadding()
    {
        return this.verticalPadding;
    }

    /**
     * Sets the vertical padding between successive objects in the spinner. This
     * method will fire a property change to all listeners registered to the
     * vertical padding property named by {@link #VERTICAL_PADDING_PROPERTY}.
     *
     * @param verticalPadding the vertical padding to set.
     * @since 1.0.0
     */
    public void setVerticalPadding(int verticalPadding)
    {
        int oldPadding = setVerticalPaddingInternal(verticalPadding);
        setOptimalSize();
        firePropertyChange(VERTICAL_PADDING_PROPERTY, oldPadding, verticalPadding);
        repaint();
    }

    /**
     * Sets the vertical padding between successive objects in the spinner. This
     * method is responsible for the internal workings of the vertical padding
     * setup without recomputing the layout, triggering property change events
     * or repainting the component. It is used both in the constructor via
     * {@link #setDefaults()} and in {@link #setVerticalPadding(int)}.
     *
     * @param verticalPadding the vertical padding to set.
     * @return the previous vertical padding. This is used to construct {@code
     * PropertyChangeEvent}s.
     * @since 1.0.0
     */
    private int setVerticalPaddingInternal(int verticalPadding)
    {
        int oldPadding = this.verticalPadding;
        this.verticalPadding = verticalPadding;
        return oldPadding;
    }

    /**
     * Returns the shade color. The shades fade from this color at the top and
     * bottom edges of the component to the same color with zero alpha towards
     * the center.
     *
     * @return the shade color of the outer edges. This may be totally
     * translucent or {@code null}, in both of which cases, the shades will not
     * be displayed.
     * @since 1.0.0
     */
    public Color getShadeColor()
    {
        return this.shadeDark;
    }

    /**
     * Sets the color of the shades at the top and bottom of the spinner to the
     * specified value. If the color is either {@code null} or completely
     * translucent (zero alpha value), the shades will not be displayed. The
     * color may be totally opaque or partially translucent. This method will
     * fire a property change to all listeners registered to the shade color
     * property named by {@link #SHADE_COLOR_PROPERTY}.
     *
     * @param shadeColor the new shade color to set. May be {@code null}.
     * @since 1.0.0
     */
    public void setShadeColor(Color shadeColor)
    {
        Color oldColor = setShadeColorInternal(shadeColor);
        computeShades();
        firePropertyChange(SHADE_COLOR_PROPERTY, oldColor, shadeColor);
        repaint();
    }

    /**
     * Sets the color of the shades at the top and bottom of the spinner. This
     * method is responsible for the internal workings of the shade color setup
     * without recomputing the shades themselves, triggering property change
     * events or repainting the component. It is used both in the constructor
     * via {@link #setDefaults()} and in {@link #setShadeColor(Color)}.
     *
     * @param shadeColor the new shade color to set. May be {@code null}.
     * @return the previous shade color. This is used to construct {@code
     * PropertyChangeEvent}s.
     * @since 1.0.0
     */
    private Color setShadeColorInternal(Color shadeColor)
    {
        Color oldColor = this.shadeDark;
        this.shadeDark = shadeColor;
        this.shadeLight = (this.shadeDark == null) ? null :
                           new Color(shadeColor.getRed(), shadeColor.getGreen(), shadeColor.getBlue(), 0);
        return oldColor;
    }

    /**
     * Returns the orienation of the spinner. The return value will be either
     * {@link #VERTICAL} or {@link #HORIZONTAL}.
     *
     * @return the orientation of the spinner.
     * @since 1.0.1
     */
    public boolean getOrientation()
    {
        return this.orientation;
    }

    /**
     * Sets the orientation of the the spinner. The orientation parameter should
     * be one of the constants {@link #VERTICAL} or {@link #HORIZONTAL}. This
     * method will fire a property change to all listeners registered to the
     * orientation property named by {@link #ORIENTATION_PROPERTY}.
     *
     * @param orientation the orientation flag to set.
     * @since 1.0.1
     */
    public void setOrientation(boolean orientation)
    {
        boolean oldOrientation = setOrientationInternal(orientation);
        setOptimalSize();
        firePropertyChange(ORIENTATION_PROPERTY, oldOrientation, orientation);
        doInternalLayout();
    }

    /**
     * Sets the orientation of the the spinner. The orientation parameter should
     * be one of the constants {@link #VERTICAL} or {@link #HORIZONTAL}. This
     * method is responsible for the internal workings of the orientation setup
     * without recomputing the layout, triggering property change events or
     * repainting the component. It is used both in the constructor via {@link
     * #setDefaults()} and in {@link #setOrientation(boolean)}.
     *
     * @param orientation the orientation flag to set.
     * @return the previous orientation. This is used to construct {@code
     * PropertyChangeEvent}s.
     * @since 1.0.0
     */
    private boolean setOrientationInternal(boolean orientation)
    {
        boolean oldOrientation = this.orientation;
        this.orientation = orientation;
        return oldOrientation;
    }

    /**
     * Spins the spinner by a random amount if it is not already spinning. This
     * method is a convenience for {@code getModel().spin()}. Since multiple
     * spinners may share the same model, they will all spin when this method is
     * invoked. Note that there is no {@code spin(int)} method to rotate through
     * a specified number of data objects. That method has to be invoked
     * directly through the model.
     *
     * @see CasinoSpinnerModel#spin()
     * @since 1.0.0
     */
    public void spin()
    {
        model.spin();
    }

    /**
     * Adds an action listener that will trigger this spinner to the specified
     * button. The action listener can be removed using {@link
     * #removeFromButton(AbstractButton)}. All spinners sharing a model with
     * this one will be triggered simultaneously by actions on the button.
     *
     * @param button a button that will trigger this spinner.
     * @since 1.0.0
     */
    public void addToButton(AbstractButton button)
    {
        button.addActionListener(buttonListener);
    }

    /**
     * Removes the action listener that triggers this spinner from the specified
     * button. This method is a no-op if the action listener was not first
     * registered using {@link #addToButton(AbstractButton)}.
     *
     * @param button the button to remove this spinner's listener from.
     * @since 1.0.0
     */
    public void removeFromButton(AbstractButton button)
    {
        button.removeActionListener(buttonListener);
    }

    /**
     * Returns an informative string representation of this model. The resulting
     * string will contain information about all of the object's properties.
     * This method is useful for debugging and should not be used as a formal
     * description of this object since the contents of the string may change at
     * any time.
     *
     * @return a {@code String} representation of this object.
     * @since 1.0.0
     */
    @Override public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [dataBounds=").append(Arrays.toString(this.dataBounds));
        sb.append(", shadeDark=").append(this.shadeDark);
        sb.append(", shadeLight=").append(this.shadeLight);
        sb.append(", shadeTop=").append(this.shadeTop);
        sb.append(", shadeBottom=").append(this.shadeBottom);
        sb.append(", verticalPadding=").append(this.verticalPadding);
        sb.append(", objectHeight=").append(this.objectHeight);
        sb.append(", objectWidth=").append(this.objectWidth);
        sb.append(", model=").append(this.model);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Draws the various parts of the component in the drawable region. The
     * strings are rendered first, at a position offset by the current state of
     * the model. The shades are drawn last to overlay the top and bottom edges
     * of the component. This method is not responsible for drawing the window
     * that surrounds the current selection because it is a child of this
     * component.
     *
     * @param g the graphics onto which this component is to be drawn.
     * @since 1.0.0
     */
    @Override protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g.create();
        g2.clipRect(boundsBuffer.x, boundsBuffer.y,
                    boundsBuffer.width - 1, boundsBuffer.height - 1);

        float position = (float)model.getState();

        // draw strings top to bottom at the current spin position
        float deltaH = (0.5f * boundsBuffer.height) / objectHeight;

        int top = (int)Math.floor(position - deltaH + 0.5);
        int bottom = (int)Math.ceil(position + deltaH - 0.5);

        float x = (float)boundsBuffer.x;
        float width = (float)boundsBuffer.width;
        float height = (float)boundsBuffer.height;

        for(int index = top; index <= bottom; index++) {
            int normIndex = model.normalize(index);

            /*
             * The formula for x position is just x + half of the difference
             * between the effective width of the component and the width of the
             * string The formula for y position is a bit more complex: The
             * distance from the position to the top of the topmost displayed
             * string is given by deltaTop = (position - top + 0.5f) *
             * objectHeight. The 0.5f is there because the position is counted
             * from the middle, not the top. objectHeight converts the units
             * from objects to pixels. Substracting this from the effective
             * center of the component (where the "position" is located), yields
             * 0.5f * height - deltaTop. The string indexed by index starting
             * from top is drawn deltaOffset = (index - top) * objectHeight
             * pixels below this. This would be the top of the bounding box of
             * the whole string if it were objectHeight pixels high. Adding half
             * of the string height substracted from objectHeight centers the
             * string vertically in its bounding box. Since the y-coordinate
             * passed to drawString is that of the baseline, the offset between
             * the top of the string and the baseline must be added in last.
             * Note that top can be cancelled out from deltaTop and deltaOffset.
             * This has the advantage of eliminating not only two operations,
             * but also two casts from int to float. Further, there are two
             * additional summands of 0.5f * objectHeight which can be cancelled
             * out. The simplified formula appears below:
             */

            // y is defined this way mostly for symmetry
            float y = (index - position) * objectHeight + this.dataBounds[normIndex].baseLine;
            g2.drawString(this.dataBounds[normIndex].str,
                          x + 0.5f * (width - this.dataBounds[normIndex].width),
                          y + 0.5f * (height - this.dataBounds[normIndex].height));
        }

        // draw shades if they are not totally translucent
        if(shadeDark != null && shadeDark.getAlpha() > 0) {
            g2.setPaint(shadeTop.paint);
            g2.fill(shadeTop.bounds);
            g2.setPaint(shadeBottom.paint);
            g2.fill(shadeBottom.bounds);
        }
    }

    /**
     * Recreates the {@link #dataBounds} array when either the model is changed
     * or the current model's data property changes. The array is set to {@code
     * null} if the model or its data are set to {@code null}. This method is
     * triggered by {@link #setModelInternal(CasinoSpinnerModel)} as well as
     * {@link ModelListener#propertyChange(PropertyChangeEvent)}. The bounds of
     * the component and the data are set based on the model and the current set
     * of properties via {@link #setOptimalSize()} once the array has been
     * recomputed.
     *
     * @since 1.0.0
     */
    private void createData()
    {
        if(this.model == null || this.model.getData() == null) {
            this.dataBounds = null;
        } else {
            this.dataBounds = new StringBounds[this.model.getDataSize()];
            List<String> data = this.model.getData();
            for(int index = 0; index < dataBounds.length; index++)
                this.dataBounds[index] = new StringBounds(data.get(index));
        }
        setOptimalSize();
    }

    /**
     * Computes the paintable bounds of this component, and resets everything
     * that depends on those bounds. Changes to either the insets or the size of
     * the spinner trigger this method. When the drawable area changes, the
     * shades and the selection window must be reset and a repaint requested via
     * {@link #doInternalLayout()}.
     *
     * @since 1.0.0
     */
    private void getInternalBounds()
    {
        this.boundsBuffer.x = this.insetsBuffer.left;
        this.boundsBuffer.y = this.insetsBuffer.top;
        this.boundsBuffer.width = this.sizeBuffer.width - this.insetsBuffer.left - this.insetsBuffer.right;
        this.boundsBuffer.height = this.sizeBuffer.height - this.insetsBuffer.left - this.insetsBuffer.right;
        doInternalLayout();
    }

    /**
     * Resets the internal components for the current bounds and insets. The
     * components are the shades (reset by {@link #computeShades()}) and the
     * selection window (reset by {@link #resetSelectionWindow()}).A repaint is
     * requeseted explicitly by this method. This method is invoked by {@link
     * #getInternalBounds()} and in response to orientation changes.
     *
     * @since 1.0.1
     */
    private void doInternalLayout()
    {
        computeShades();
        resetSelectionWindow();
        repaint();
    }

    /**
     * Computes the maximum dimensions of a data string with the current font
     * and sets the spinner's size accordingly. The {@link #objectWidth} field
     * is set to the largest string width in the data. The {@link #objectHeight}
     * field is set to the largest height found, with {@link #verticalPadding}
     * added to it. The size of the component is set to try to completely
     * include three strings stacked vertically within the insets. This method
     * does not request a repaint of the component. Repainting must be invoked
     * externally.
     *
     * @since 1.0.0
     */
    private void setOptimalSize()
    {
        Dimension computedSize = new Dimension(0, 0);

        Font currentFont = getFont();
        // This will only give an approximation of the real font sizes,
        // but usually a good one
        FontRenderContext rendererContext =
                new FontRenderContext(currentFont.getTransform(), true, true);

        if(this.dataBounds != null) {
            // find the biggest width and height in the list
            for(StringBounds bounds : this.dataBounds) {
                bounds.setBounds(currentFont, rendererContext);
                if(bounds.width > computedSize.width)
                    computedSize.width = (int)Math.ceil(bounds.width);
                if(bounds.height > computedSize.height)
                    computedSize.height = (int)Math.ceil(bounds.height);
            }
        }

        // Include three rows of stuff with internal padding between them.
        computedSize.height += this.verticalPadding;
        this.objectHeight = computedSize.height;
        this.objectWidth = computedSize.width;
        computedSize.height *= 3;

        // Allow extra room necessary for border
        computedSize.width += insetsBuffer.left + insetsBuffer.right;
        computedSize.height += insetsBuffer.top + insetsBuffer.bottom;

        // Set the sizes for all possible layout managers. Do not restrict maximum.
        setMinimumSize(computedSize);
        setPreferredSize(computedSize);
        setSize(computedSize);
    }

    /**
     * Computes the shading gradient paints that are used to cover the top and
     * bottom of the spinner. This method is invoked through {@link
     * #getInternalBounds()} whenever this component is resized and through
     * {@link #setShadeColor(Color)} whenever the color changes. This method
     * does not request a repaint of the component. Repainting must be invoked
     * externally.
     *
     * @see #shadeTop
     * @see #shadeBottom
     * @since 1.0.0
     */
    private void computeShades()
    {
        float effectiveHeight = (float)this.boundsBuffer.height;
        float shadeHeight = (float)this.boundsBuffer.height / 3.0f;

        // compute the paints based on the object height
        shadeTop.paint = new GradientPaint(0.0f, 0.0f, this.shadeDark,
                                           0.0f, shadeHeight, this.shadeLight, false);
        shadeBottom.paint = new GradientPaint(0.0f, effectiveHeight, this.shadeDark,
                                              0.0f, effectiveHeight - shadeHeight, this.shadeLight, false);
        // compute the bounds based on the bounds
        shadeTop.bounds.setRect(boundsBuffer.x, boundsBuffer.y,
                                boundsBuffer.width, shadeHeight);
        shadeBottom.bounds.setRect(boundsBuffer.x, boundsBuffer.y + effectiveHeight - shadeHeight,
                                   boundsBuffer.width, shadeHeight);
    }

    /**
     * Resets the size and position of the {@link #selectionWindow} component.
     * This method is triggered by any change on the bounds of the component via
     * {@link #getInternalBounds()}. It does not request a repaint of the
     * component. Repainting must be invoked externally.
     *
     * @since 1.0.0
     */
    private void resetSelectionWindow()
    {
        float height = objectHeight + selectionWindowInsets.top + selectionWindowInsets.bottom - 0.5f * verticalPadding;
        float dx = 0.5f * (boundsBuffer.width - (selectionWindowInsets.left + selectionWindowInsets.right + objectWidth));
        float dy = 0.5f * (boundsBuffer.height - height);
        selectionWindow.setBounds(boundsBuffer.x + Math.round(0.5f * dx) - 1,
                                  boundsBuffer.y + Math.round(dy) - 1,
                                  Math.round(boundsBuffer.width - dx), Math.round(height) - 1);
    }

    /**
     * Listens to events on the model and performs the necessary updates to the
     * graphic as the model changes.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 25 Feb 2013
     * @since 1.0.0
     */
    private class ModelListener implements ChangeListener, PropertyChangeListener, Serializable
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
         * Responds to model updates when the model is spinning. This method
         * delegates directly to {@code repaint()}. This method ensures that
         * multiple spinners can be registered to the same model.
         *
         * @param event the change notification event.
         * @since 1.0.0
         */
        @Override public void stateChanged(ChangeEvent event)
        {
            repaint();
        }

        /**
         * Responds to property changes that need to be reflected in the
         * display. When the data is altered, a new set of bounds must be
         * created before the display bounds and alignment are recomputed.
         *
         * @param evt the notification event. The event is only processed if it
         * pertains to the model's {@link CasinoSpinnerModel#DATA_PROPERTY}.
         * @since 1.0.0
         */
        @Override public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals(CasinoSpinnerModel.DATA_PROPERTY))
                createData();
        }
    }

    /**
     * A listener for {@code ActionEvent}s that triggers random spin. A single
     * instance of this listener will be registered on all buttons supplied to
     * the {@link #addToButton(AbstractButton)} method, so it does not care
     * about the source of the action.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 12 Mar 2013
     * @since 1.0.0
     */
    private class ButtonListener implements ActionListener, Serializable
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
         * Reacts to {@code ActionEvent}s by tirggering a random spin on the
         * parent spinner. If the spinner is already spinning, this method is a
         * no-op.
         *
         * @param event the event that triggered this method.
         * @since 1.0.0
         */
        @Override public void actionPerformed(ActionEvent event)
        {
            CasinoSpinner.this.spin();
        }
    }

    /**
     * Stores the bounds and paint for the shades at the top and bottom of the
     * spinner. These objects are size-dependent since both the bounds and the
     * gradient paint require the dimensions of the components.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 7 Mar 2013
     * @since 1.0.0
     */
    private static class PaintedShape implements Serializable
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
         * The paint used to fill the shape.
         *
         * @since 1.0.0
         */
        public GradientPaint paint;

        /**
         * The bounds of the shape being filled in.
         *
         * @since 1.0.0
         */
        public Rectangle2D.Float bounds;

        /**
         * Constructs an empty shape. The paint is set to {@code null} and the
         * bounds are an empty rectangle. It is the responsibility of the
         * spinner's update code to recompute this object whenever the drawable
         * area changes.
         *
         * @since 1.0.0
         */
        public PaintedShape()
        {
            this.paint = null;
            this.bounds = new Rectangle2D.Float();
        }

        /**
         * Returns an informative string representation of this model. The
         * resulting string will contain information about all of the task's
         * properties. This method is useful for debugging and should not be
         * used as a formal description of this object since the contents of the
         * string may change at any time.
         *
         * @return a {@code String} representation of this object.
         * @since 1.0.0
         */
        @Override public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" [paint=").append(this.paint);
            sb.append(" [bounds=").append(this.bounds);
            sb.append("]");
            return sb.toString();
        }
    }

    /**
     * Stores the dimensions of a string when rendered with a specific font.
     *
     * @author Joseph Fox-Rabinovitz
     * @version 1.0.0, 16 Feb 2013
     * @since 1.0.0
     */
    private static class StringBounds implements Serializable
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
         * The width of the string in pixels for the current font and rendering
         * context.
         * 
         * @since 1.0.0
         */
        public float width;

        /**
         * The height of the string in pixels for the current font and rendering
         * context. The height is the sum of the ascent and descent of the
         * current font.
         *
         * @since 1.0.0
         */
        public float height;

        /**
         * The base line of the string in pixels measured from the top for the
         * current font and rendering context. The base line is given by the
         * distance from the top of the ascent to the base line of the current
         * font.
         *
         * @since 1.0.0
         */
        public float baseLine;

        /**
         * The string which this object encapsulates. This is the only field set
         * by the constructor since it is font- and rendering
         * context-independent.
         *
         * @since 1.0.0
         */
        public final String str;

        /**
         * Constructs a bounds object for the specified string. The width,
         * height and base line fields are not set by the constructor. They must
         * be computed for a specific font and context using {@link
         * #setBounds(Font, FontRenderContext)}.
         *
         * @param str the string for which the bounds are to be computed.
         * @since 1.0.0
         */
        public StringBounds(String str)
        {
            this.str = str;
        }

        /**
         * Recomputes the bounds parameters of the string based on the specified
         * font and renderer context. The result is a very good, generally
         * accurate approximation of the size of the string. The baseling is
         * computed from the top of the string.
         *
         * @param font the font to compute the bounds for.
         * @param renderer the rendering context in which the string is to be
         * displayed.
         * @since 1.0.0
         */
        public void setBounds(Font font, FontRenderContext renderer)
        {
            Rectangle2D rect = font.getStringBounds(str, renderer);
            LineMetrics metrics = font.getLineMetrics(str, renderer);
            this.width = (float)rect.getWidth();
            this.height = metrics.getAscent() + metrics.getDescent();
            this.baseLine = metrics.getAscent();
        }

        /**
         * Returns an informative string representation of this model. The
         * resulting string will contain information about all of the task's
         * properties. This method is useful for debugging and should not be
         * used as a formal description of this object since the contents of the
         * string may change at any time.
         *
         * @return a {@code String} representation of this object.
         * @since 1.0.0
         */
        @Override public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" [str=").append(this.str);
            sb.append(" [width=").append(this.width);
            sb.append(" [height=").append(this.height);
            sb.append(" [baseLine=").append(this.baseLine);
            sb.append("]");
            return sb.toString();
        }
    }

    public static void main(String[] args)
    {
        System.err.println("This should display some sort of window with a little demo app!");
    }
}
