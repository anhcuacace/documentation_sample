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
import tunanh.documentation.xs.fc.util.LittleEndianOutput;


/**
 * preceeds and identifies a frame as belonging to the plot area.<p/>
 * 
 * @author Andrew C. Oliver (acoliver at apache.org)
 */
public final class PlotAreaRecord extends StandardRecord {
    public final static short      sid                             = 0x1035;


    public PlotAreaRecord()
    {

    }

    /**
     * @param in unused (since this record has no data)
     */
    public PlotAreaRecord(RecordInputStream in)
    {

    }

    public String toString()
    {

        String buffer = "[PLOTAREA]\n" +
                "[/PLOTAREA]\n";
        return buffer;
    }

    public void serialize(LittleEndianOutput out) {
    }

    protected int getDataSize() {
        return 0;
    }

    public short getSid()
    {
        return sid;
    }

    public Object clone() {
        PlotAreaRecord rec = new PlotAreaRecord();
    
        return rec;
    }
}
