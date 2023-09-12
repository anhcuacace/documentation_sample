/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package tunanh.documentation.xs.fc.hwpf.model.types;

import tunanh.documentation.xs.fc.util.Internal;
import tunanh.documentation.xs.fc.util.LittleEndian;

/**
 * Footnote Reference Descriptor (FRD).
 * <p>
 * Class and fields descriptions are quoted from Microsoft Office Word 97-2007
 * Binary File Format (.doc) Specification
 * <p>
 * NOTE: This source is automatically generated please do not modify this file.
 * Either subclass or remove the record in src/types/definitions.
 * 
 * @author Sergey Vladimirov; according to Microsoft Office Word 97-2007 Binary
 *         File Format (.doc) Specification
 */
@Internal
public abstract class FRDAbstractType
{

    protected short field_1_nAuto;

    protected FRDAbstractType()
    {
    }

    protected void fillFields( byte[] data, int offset )
    {
        field_1_nAuto = LittleEndian.getShort( data, offset);
    }

    public void serialize( byte[] data, int offset )
    {
        LittleEndian.putShort( data, offset, field_1_nAuto );
    }

    /**
     * Size of record
     */
    public static int getSize()
    {
        return 2;
    }

    public String toString()
    {

        String builder = "[FRD]\n" +
                "    .nAuto                = " +
                " (" + getNAuto() + " )\n" +
                "[/FRD]\n";
        return builder;
    }

    /**
     * If > 0, the note is an automatically numbered note, otherwise it has a
     * custom mark.
     */
    public short getNAuto()
    {
        return field_1_nAuto;
    }

    /**
     * If > 0, the note is an automatically numbered note, otherwise it has a
     * custom mark.
     */
    public void setNAuto( short field_1_nAuto )
    {
        this.field_1_nAuto = field_1_nAuto;
    }

} // END OF CLASS
