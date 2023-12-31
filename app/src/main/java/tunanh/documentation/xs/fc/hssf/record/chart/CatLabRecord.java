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
 * CATLAB - Category Labels (0x0856)<br/>
 * 
 * @author Patrick Cheng
 */
public final class CatLabRecord extends StandardRecord {
	public static final short sid = 0x0856;
	
	private final short rt;
	private final short grbitFrt;
	private final short wOffset;
	private final short at;
	private final short grbit;
	private final Short unused;
	
	public CatLabRecord(RecordInputStream in) {
		rt = in.readShort();
		grbitFrt = in.readShort();
		wOffset = in.readShort();
		at = in.readShort();
		grbit = in.readShort();
		
		// Often, but not always has an unused short at the end
		if(in.available() == 0) {
			unused = null;
		} else {
			unused = in.readShort();
		}
	}
	
	@Override
	protected int getDataSize() {
		return 2 + 2 + 2 + 2 + 2 + (unused==null? 0:2);
	}

	@Override
	public short getSid() {
		return sid;
	}

	@Override
	public void serialize(LittleEndianOutput out) {
		out.writeShort(rt);
		out.writeShort(grbitFrt);
		out.writeShort(wOffset);
		out.writeShort(at);
		out.writeShort(grbit);
		if(unused != null)
			out.writeShort(unused);
	}

	@Override
	public String toString() {

        String buffer = "[CATLAB]\n" +
                "    .rt      =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt=" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .wOffset =" + String.valueOf(HexDump.shortToHex(wOffset)) + '\n' +
                "    .at      =" + String.valueOf(HexDump.shortToHex(at)) + '\n' +
                "    .grbit   =" + String.valueOf(HexDump.shortToHex(grbit)) + '\n' +
                "    .unused  =" + String.valueOf(HexDump.shortToHex(unused)) + '\n' +
                "[/CATLAB]\n";
		return buffer;
	}
}
