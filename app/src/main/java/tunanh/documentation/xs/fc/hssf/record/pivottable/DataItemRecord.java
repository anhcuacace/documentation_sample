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

package tunanh.documentation.xs.fc.hssf.record.pivottable;


import tunanh.documentation.xs.fc.hssf.record.RecordInputStream;
import tunanh.documentation.xs.fc.hssf.record.StandardRecord;
import tunanh.documentation.xs.fc.util.HexDump;
import tunanh.documentation.xs.fc.util.LittleEndianOutput;
import tunanh.documentation.xs.fc.util.StringUtil;


/**
 * SXDI - Data Item (0x00C5)<br/>
 * 
 * @author Patrick Cheng
 */
public final class DataItemRecord extends StandardRecord {
	public static final short sid = 0x00C5;

	private final int isxvdData;
	private final int iiftab;
	private final int df;
	private final int isxvd;
	private final int isxvi;
	private final int ifmt;
	private final String name;
	
	public DataItemRecord(RecordInputStream in) {
		isxvdData = in.readUShort();
		iiftab = in.readUShort();
		df = in.readUShort();
		isxvd = in.readUShort();
		isxvi = in.readUShort();
		ifmt = in.readUShort();
		
		name = in.readString();
	}
	
	@Override
	protected void serialize(LittleEndianOutput out) {
		
		out.writeShort(isxvdData);
		out.writeShort(iiftab);
		out.writeShort(df);
		out.writeShort(isxvd);
		out.writeShort(isxvi);
		out.writeShort(ifmt);
		
		StringUtil.writeUnicodeString(out, name);
	}

	@Override
	protected int getDataSize() {
		return 2 + 2 + 2 + 2 + 2 + 2 + StringUtil.getEncodedSize(name);
	}

	@Override
	public short getSid() {
		return sid;
	}

	@Override
	public String toString() {

        String buffer = "[SXDI]\n" +
                "  .isxvdData = " + String.valueOf(HexDump.shortToHex(isxvdData)) + "\n" +
                "  .iiftab = " + String.valueOf(HexDump.shortToHex(iiftab)) + "\n" +
                "  .df = " + String.valueOf(HexDump.shortToHex(df)) + "\n" +
                "  .isxvd = " + String.valueOf(HexDump.shortToHex(isxvd)) + "\n" +
                "  .isxvi = " + String.valueOf(HexDump.shortToHex(isxvi)) + "\n" +
                "  .ifmt = " + String.valueOf(HexDump.shortToHex(ifmt)) + "\n" +
                "[/SXDI]\n";
		return buffer;
	}
}
