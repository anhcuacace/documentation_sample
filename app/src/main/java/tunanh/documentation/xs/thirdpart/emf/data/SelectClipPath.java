// Copyright 2002, FreeHEP.

package tunanh.documentation.xs.thirdpart.emf.data;

import java.io.IOException;

import tunanh.documentation.xs.thirdpart.emf.EMFConstants;
import tunanh.documentation.xs.thirdpart.emf.EMFInputStream;
import tunanh.documentation.xs.thirdpart.emf.EMFRenderer;
import tunanh.documentation.xs.thirdpart.emf.EMFTag;

/**
 * SelectClipPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SelectClipPath.java 10516 2007-02-06 21:11:19Z duns $
 */
public class SelectClipPath extends AbstractClipPath
{

    public SelectClipPath()
    {
        super(67, 1, EMFConstants.RGN_AND);
    }

    public SelectClipPath(int mode)
    {
        super(67, 1, mode);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SelectClipPath(emf.readDWORD());
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        render(renderer, renderer.getPath());
    }
}
