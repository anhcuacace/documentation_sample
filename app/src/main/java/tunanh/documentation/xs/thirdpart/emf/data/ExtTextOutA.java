// Copyright 2002, FreeHEP.
package tunanh.documentation.xs.thirdpart.emf.data;

import java.io.IOException;

import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.thirdpart.emf.EMFConstants;
import tunanh.documentation.xs.thirdpart.emf.EMFInputStream;
import tunanh.documentation.xs.thirdpart.emf.EMFTag;

/**
 * ExtTextOutA TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ExtTextOutA.java 10377 2007-01-23 15:44:34Z duns $
 */
public class ExtTextOutA extends AbstractExtTextOut implements EMFConstants {

    private TextA text;

    public ExtTextOutA() {
        super(83, 1, null, 0, 1, 1);
    }

    public ExtTextOutA(
        Rectangle bounds,
        int mode,
        float xScale,
        float yScale,
        TextA text) {

        super(83, 1, bounds, mode, xScale, yScale);
        this.text = text;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ExtTextOutA(
            emf.readRECTL(),
            emf.readDWORD(),
            emf.readFLOAT(),
            emf.readFLOAT(),
            TextA.read(emf));
    }

    public Text getText() {
        return text;
    }
}
