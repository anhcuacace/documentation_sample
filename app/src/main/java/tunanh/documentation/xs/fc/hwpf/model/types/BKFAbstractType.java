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

import tunanh.documentation.xs.fc.util.BitField;
import tunanh.documentation.xs.fc.util.Internal;
import tunanh.documentation.xs.fc.util.LittleEndian;

/**
 * BooKmark First descriptor (BKF).
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
@ Internal
public abstract class BKFAbstractType
{

    protected short field_1_ibkl;
    protected short field_2_bkf_flags;
    /**/private static final BitField itcFirst = new BitField(0x007F);
    /**/private static final BitField fPub = new BitField(0x0080);
    /**/private static final BitField itcLim = new BitField(0x7F00);
    /**/private static final BitField fCol = new BitField(0x8000);

    protected BKFAbstractType()
    {
    }

    protected void fillFields(byte[] data, int offset)
    {
        field_1_ibkl = LittleEndian.getShort(data, offset);
        field_2_bkf_flags = LittleEndian.getShort(data, 0x2 + offset);
    }

    public void serialize(byte[] data, int offset)
    {
        LittleEndian.putShort(data, offset, field_1_ibkl);
        LittleEndian.putShort(data, 0x2 + offset, field_2_bkf_flags);
    }

    /**
     * Size of record
     */
    public static int getSize()
    {
        return 2 + 2;
    }

    public String toString()
    {

        String builder = "[BKF]\n" +
                "    .ibkl                 = " +
                " (" + getIbkl() + " )\n" +
                "    .bkf_flags            = " +
                " (" + getBkf_flags() + " )\n" +
                "         .itcFirst                 = " + getItcFirst() + '\n' +
                "         .fPub                     = " + isFPub() + '\n' +
                "         .itcLim                   = " + getItcLim() + '\n' +
                "         .fCol                     = " + isFCol() + '\n' +
                "[/BKF]\n";
        return builder;
    }

    /**
     * Index to BKL entry in plcfbkl that describes the ending position of this bookmark in the CP stream.
     */
    public short getIbkl()
    {
        return field_1_ibkl;
    }

    /**
     * Index to BKL entry in plcfbkl that describes the ending position of this bookmark in the CP stream.
     */
    public void setIbkl(short field_1_ibkl)
    {
        this.field_1_ibkl = field_1_ibkl;
    }

    /**
     * Get the bkf_flags field for the BKF record.
     */
    public short getBkf_flags()
    {
        return field_2_bkf_flags;
    }

    /**
     * Set the bkf_flags field for the BKF record.
     */
    public void setBkf_flags(short field_2_bkf_flags)
    {
        this.field_2_bkf_flags = field_2_bkf_flags;
    }

    /**
     * Sets the itcFirst field value.
     * When bkf.fCol==1, this is the index to the first column of a table column bookmark
     */
    public void setItcFirst(byte value)
    {
        field_2_bkf_flags = (short)itcFirst.setValue(field_2_bkf_flags, value);
    }

    /**
     * When bkf.fCol==1, this is the index to the first column of a table column bookmark
     * @return  the itcFirst field value.
     */
    public byte getItcFirst()
    {
        return (byte)itcFirst.getValue(field_2_bkf_flags);
    }

    /**
     * Sets the fPub field value.
     * When 1, this indicates that this bookmark is marking the range of a Macintosh Publisher section
     */
    public void setFPub(boolean value)
    {
        field_2_bkf_flags = (short)fPub.setBoolean(field_2_bkf_flags, value);
    }

    /**
     * When 1, this indicates that this bookmark is marking the range of a Macintosh Publisher section
     * @return  the fPub field value.
     */
    public boolean isFPub()
    {
        return fPub.isSet(field_2_bkf_flags);
    }

    /**
     * Sets the itcLim field value.
     * When bkf.fCol==1, this is the index to limit column of a table column bookmark
     */
    public void setItcLim(byte value)
    {
        field_2_bkf_flags = (short)itcLim.setValue(field_2_bkf_flags, value);
    }

    /**
     * When bkf.fCol==1, this is the index to limit column of a table column bookmark
     * @return  the itcLim field value.
     */
    public byte getItcLim()
    {
        return (byte)itcLim.getValue(field_2_bkf_flags);
    }

    /**
     * Sets the fCol field value.
     * When 1, this bookmark marks a range of columns in a table specified by (bkf.itcFirst, bkf.itcLim)
     */
    public void setFCol(boolean value)
    {
        field_2_bkf_flags = (short)fCol.setBoolean(field_2_bkf_flags, value);
    }

    /**
     * When 1, this bookmark marks a range of columns in a table specified by (bkf.itcFirst, bkf.itcLim)
     * @return  the fCol field value.
     */
    public boolean isFCol()
    {
        return fCol.isSet(field_2_bkf_flags);
    }

} // END OF CLASS
