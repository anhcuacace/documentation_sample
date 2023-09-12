// Copyright 2002, FreeHEP.

package tunanh.documentation.xs.thirdpart.emf.data;

import java.io.IOException;

import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.thirdpart.emf.EMFInputStream;
import tunanh.documentation.xs.thirdpart.emf.EMFTag;

/**
 * ExcludeClipRect TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ExcludeClipRect.java 10367 2007-01-22 19:26:48Z duns $
 */
public class ExcludeClipRect extends EMFTag
{

    private Rectangle bounds;

    public ExcludeClipRect()
    {
        super(29, 1);
    }

    public ExcludeClipRect(Rectangle bounds)
    {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new ExcludeClipRect(emf.readRECTL());
    }
    
    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds;
    }
}