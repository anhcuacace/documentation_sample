// Copyright 2002, FreeHEP.
package tunanh.documentation.xs.thirdpart.emf.data;

import java.io.IOException;

import tunanh.documentation.xs.thirdpart.emf.EMFInputStream;
import tunanh.documentation.xs.thirdpart.emf.EMFTag;

/**
 * RealizePalette TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: RealizePalette.java 10140 2006-12-07 07:50:41Z duns $
 */
public class RealizePalette extends EMFTag {

    public RealizePalette() {
        super(52, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

}
