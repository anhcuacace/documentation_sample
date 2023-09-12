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

package tunanh.documentation.xs.fc.hssf.record;

import tunanh.documentation.xs.fc.util.HexDump;
import tunanh.documentation.xs.fc.util.LittleEndianOutput;
import tunanh.documentation.xs.fc.util.StringUtil;

/**
 * TABLESTYLES (0x088E)<br/>
 * 
 * @author Patrick Cheng
 */
public final class TableStylesRecord extends StandardRecord {
	public static final short sid = 0x088E;
	
	private final int rt;
	private final int grbitFrt;
	private final byte[] unused = new byte[8];
	private final int cts;
	
	private final String rgchDefListStyle;
	private final String rgchDefPivotStyle;
	
	
	public TableStylesRecord(RecordInputStream in) {
		rt = in.readUShort();
		grbitFrt = in.readUShort();
		in.readFully(unused);
		cts = in.readInt();
		int cchDefListStyle = in.readUShort();
		int cchDefPivotStyle = in.readUShort();
		
		rgchDefListStyle = in.readUnicodeLEString(cchDefListStyle);
		rgchDefPivotStyle = in.readUnicodeLEString(cchDefPivotStyle);
	}
	
	@Override
	protected void serialize(LittleEndianOutput out) {
		out.writeShort(rt);
		out.writeShort(grbitFrt);
		out.write(unused);
		out.writeInt(cts);
		
		out.writeShort(rgchDefListStyle.length());
		out.writeShort(rgchDefPivotStyle.length());
		
		StringUtil.putUnicodeLE(rgchDefListStyle, out);
		StringUtil.putUnicodeLE(rgchDefPivotStyle, out);
	}

	@Override
	protected int getDataSize() {
		return 2 + 2 + 8 + 4 + 2 + 2
			+ (2*rgchDefListStyle.length()) + (2*rgchDefPivotStyle.length());
	}

	@Override
	public short getSid() {
		return sid;
	}


	@Override
	public String toString() {

        String buffer = "[TABLESTYLES]\n" +
                "    .rt      =" + String.valueOf(HexDump.shortToHex(rt)) + '\n' +
                "    .grbitFrt=" + String.valueOf(HexDump.shortToHex(grbitFrt)) + '\n' +
                "    .unused  =" + HexDump.toHex(unused) + '\n' +
                "    .cts=" + String.valueOf(HexDump.intToHex(cts)) + '\n' +
                "    .rgchDefListStyle=" + rgchDefListStyle + '\n' +
                "    .rgchDefPivotStyle=" + rgchDefPivotStyle + '\n' +
                "[/TABLESTYLES]\n";
		return buffer;
	}
}
