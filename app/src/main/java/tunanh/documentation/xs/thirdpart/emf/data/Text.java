// Copyright 2002, FreeHEP.

package tunanh.documentation.xs.thirdpart.emf.data;

import android.graphics.Point;

import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.thirdpart.emf.EMFConstants;

/**
 * EMF Text
 * 
 * @author Mark Donszelmann
 * @version $Id: Text.java 10313 2007-01-15 16:30:03Z duns $
 */
public abstract class Text implements EMFConstants
{

    Point pos;

    String string;

    int options;

    Rectangle bounds;

    int[] widths;

    protected Text(Point pos, String string, int options, Rectangle bounds, int[] widths)
    {
        this.pos = pos;
        this.string = string;
        this.options = options;
        this.bounds = bounds;
        this.widths = widths;
    }

    public Point getPos()
    {
        return pos;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public String getString()
    {
        return string;
    }
}
