// Copyright 2002, FreeHEP.

package tunanh.documentation.xs.thirdpart.emf.data;

import android.graphics.Point;

import java.io.IOException;

import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.thirdpart.emf.EMFInputStream;
import tunanh.documentation.xs.thirdpart.emf.EMFTag;

/**
 * Polyline16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: Polyline16.java 10367 2007-01-22 19:26:48Z duns $
 */
public class Polyline16 extends Polyline
{

    public Polyline16()
    {
        super(87, 1, null, 0, null);
    }

    public Polyline16(Rectangle bounds, int numberOfPoints, Point[] points)
    {
        super(87, 1, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new Polyline16(r, n, emf.readPOINTS(n));
    }

}
