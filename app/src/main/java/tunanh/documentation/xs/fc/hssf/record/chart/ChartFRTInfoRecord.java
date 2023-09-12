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
import tunanh.documentation.xs.fc.util.LittleEndianInput;
import tunanh.documentation.xs.fc.util.LittleEndianOutput;


/**
 * CHARTFRTINFO - Chart Future Record Type Info (0x0850)<br/>
 * 
 * @author Patrick Cheng
 */
public final class ChartFRTInfoRecord extends StandardRecord {
	public static final short sid = 0x850;

	private final short rt;
	private final short grbitFrt;
	private final byte verOriginator;
	private final byte verWriter;
	private final CFRTID[] rgCFRTID;

	private static final class CFRTID {
		public static final int ENCODED_SIZE = 4;
		private final int rtFirst;
		private final int rtLast;

		public CFRTID(LittleEndianInput in) {
			rtFirst = in.readShort();
			rtLast = in.readShort();
		}

		public void serialize(LittleEndianOutput out) {
			out.writeShort(rtFirst);
			out.writeShort(rtLast);
		}
	}

	public ChartFRTInfoRecord(RecordInputStream in) {
		rt = in.readShort();
		grbitFrt = in.readShort();
		verOriginator = in.readByte();
		verWriter = in.readByte();
		int cCFRTID = in.readShort();

		rgCFRTID = new CFRTID[cCFRTID];
		for (int i = 0; i < cCFRTID; i++) {
			rgCFRTID[i] = new CFRTID(in);
		}
	}

	@Override
	protected int getDataSize() {
		return 2 + 2 + 1 + 1 + 2 + rgCFRTID.length * CFRTID.ENCODED_SIZE;
	}

	@Override
	public short getSid() {
		return sid;
	}

	@Override
	public void serialize(LittleEndianOutput out) {

		out.writeShort(rt);
		out.writeShort(grbitFrt);
		out.writeByte(verOriginator);
		out.writeByte(verWriter);
		int nCFRTIDs = rgCFRTID.length;
		out.writeShort(nCFRTIDs);

		for (int i = 0; i < nCFRTIDs; i++) {
			rgCFRTID[i].serialize(out);
		}
	}

	@Override
	public String toString() {

        String buffer = "[CHARTFRTINFO]\n" +
                "    .rt           =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt     =" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .verOriginator=" + String.valueOf(HexDump.byteToHex(verOriginator)) + '\n' +
                "    .verWriter    =" + String.valueOf(HexDump.byteToHex(verOriginator)) + '\n' +
                "    .nCFRTIDs     =" + String.valueOf(HexDump.shortToHex(rgCFRTID.length)) + '\n' +
                "[/CHARTFRTINFO]\n";
		return buffer;
	}
}
