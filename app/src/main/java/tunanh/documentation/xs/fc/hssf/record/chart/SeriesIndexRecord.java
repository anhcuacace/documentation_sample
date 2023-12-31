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

package tunanh.documentation.xs.fc.hssf.record.chart;


import tunanh.documentation.xs.fc.hssf.record.RecordInputStream;
import tunanh.documentation.xs.fc.hssf.record.StandardRecord;
import tunanh.documentation.xs.fc.util.HexDump;
import tunanh.documentation.xs.fc.util.LittleEndianOutput;


/**
 * links a series to its position in the series list.<p/>
 * 
 * @author Andrew C. Oliver (acoliver at apache.org)
 */
public final class SeriesIndexRecord extends StandardRecord {
    public final static short      sid                             = 0x1065;
    private  short      field_1_index;


    public SeriesIndexRecord()
    {

    }

    public SeriesIndexRecord(RecordInputStream in)
    {
        field_1_index                  = in.readShort();
    }

    public String toString()
    {

        String buffer = "[SINDEX]\n" +
                "    .index                = " +
                "0x" + HexDump.toHex(getIndex()) +
                " (" + getIndex() + " )" +
                System.getProperty("line.separator") +
                "[/SINDEX]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_index);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid()
    {
        return sid;
    }

    public Object clone() {
        SeriesIndexRecord rec = new SeriesIndexRecord();
    
        rec.field_1_index = field_1_index;
        return rec;
    }




    /**
     * Get the index field for the SeriesIndex record.
     */
    public short getIndex()
    {
        return field_1_index;
    }

    /**
     * Set the index field for the SeriesIndex record.
     */
    public void setIndex(short field_1_index)
    {
        this.field_1_index = field_1_index;
    }
}
