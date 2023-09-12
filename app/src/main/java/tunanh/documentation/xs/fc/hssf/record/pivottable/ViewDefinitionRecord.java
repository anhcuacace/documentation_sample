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
 * SXVIEW - View Definition (0x00B0)<br/>
 * 
 * @author Patrick Cheng
 */
public final class ViewDefinitionRecord extends StandardRecord {
	public static final short sid = 0x00B0;

	private final int rwFirst;
	private final int rwLast;
	private final int colFirst;
	private final int colLast;
	private final int rwFirstHead;
	private final int rwFirstData;
	private final int colFirstData;
	private final int iCache;
	private final int reserved;
	
	private final int sxaxis4Data;
	private final int ipos4Data;
	private final int cDim;
	
	private final int cDimRw;
	
	private final int cDimCol;
	private final int cDimPg;
	
	private final int cDimData;
	private final int cRw;
	private final int cCol;
	private final int grbit;
	private final int itblAutoFmt;
	
	private final String dataField;
	private final String name;

	
	public ViewDefinitionRecord(RecordInputStream in) {
		rwFirst = in.readUShort();
		rwLast = in.readUShort();
		colFirst = in.readUShort();
		colLast = in.readUShort();
		rwFirstHead = in.readUShort();
		rwFirstData = in.readUShort();
		colFirstData = in.readUShort();
		iCache = in.readUShort();
		reserved = in.readUShort();
		sxaxis4Data = in.readUShort();
		ipos4Data = in.readUShort();
		cDim = in.readUShort();
		cDimRw = in.readUShort();
		cDimCol = in.readUShort();
		cDimPg = in.readUShort();
		cDimData = in.readUShort();
		cRw = in.readUShort();
		cCol = in.readUShort();
		grbit = in.readUShort();
		itblAutoFmt = in.readUShort();
		int cchName = in.readUShort();
		int cchData = in.readUShort();

		name = StringUtil.readUnicodeString(in, cchName);
		dataField = StringUtil.readUnicodeString(in, cchData);
	}
	
	@Override
	protected void serialize(LittleEndianOutput out) {
		out.writeShort(rwFirst);
		out.writeShort(rwLast);
		out.writeShort(colFirst);
		out.writeShort(colLast);
		out.writeShort(rwFirstHead);
		out.writeShort(rwFirstData);
		out.writeShort(colFirstData);
		out.writeShort(iCache);
		out.writeShort(reserved);
		out.writeShort(sxaxis4Data);
		out.writeShort(ipos4Data);
		out.writeShort(cDim);
		out.writeShort(cDimRw);
		out.writeShort(cDimCol);
		out.writeShort(cDimPg);
		out.writeShort(cDimData);
		out.writeShort(cRw);
		out.writeShort(cCol);
		out.writeShort(grbit);
		out.writeShort(itblAutoFmt);
		out.writeShort(name.length());
		out.writeShort(dataField.length());

		StringUtil.writeUnicodeStringFlagAndData(out, name);
		StringUtil.writeUnicodeStringFlagAndData(out, dataField);		
	}

	@Override
	protected int getDataSize() {
		return 40 + // 20 short fields (rwFirst ... itblAutoFmt)
			StringUtil.getEncodedSize(name) + StringUtil.getEncodedSize(dataField) ;
	}

	@Override
	public short getSid() {
		return sid;
	}

	@Override
	public String toString() {

        String buffer = "[SXVIEW]\n" +
                "    .rwFirst      =" + String.valueOf(HexDump.shortToHex(rwFirst)) + '\n' +
                "    .rwLast       =" + String.valueOf(HexDump.shortToHex(rwLast)) + '\n' +
                "    .colFirst     =" + String.valueOf(HexDump.shortToHex(colFirst)) + '\n' +
                "    .colLast      =" + String.valueOf(HexDump.shortToHex(colLast)) + '\n' +
                "    .rwFirstHead  =" + String.valueOf(HexDump.shortToHex(rwFirstHead)) + '\n' +
                "    .rwFirstData  =" + String.valueOf(HexDump.shortToHex(rwFirstData)) + '\n' +
                "    .colFirstData =" + String.valueOf(HexDump.shortToHex(colFirstData)) + '\n' +
                "    .iCache       =" + String.valueOf(HexDump.shortToHex(iCache)) + '\n' +
                "    .reserved     =" + String.valueOf(HexDump.shortToHex(reserved)) + '\n' +
                "    .sxaxis4Data  =" + String.valueOf(HexDump.shortToHex(sxaxis4Data)) + '\n' +
                "    .ipos4Data    =" + String.valueOf(HexDump.shortToHex(ipos4Data)) + '\n' +
                "    .cDim         =" + String.valueOf(HexDump.shortToHex(cDim)) + '\n' +
                "    .cDimRw       =" + String.valueOf(HexDump.shortToHex(cDimRw)) + '\n' +
                "    .cDimCol      =" + String.valueOf(HexDump.shortToHex(cDimCol)) + '\n' +
                "    .cDimPg       =" + String.valueOf(HexDump.shortToHex(cDimPg)) + '\n' +
                "    .cDimData     =" + String.valueOf(HexDump.shortToHex(cDimData)) + '\n' +
                "    .cRw          =" + String.valueOf(HexDump.shortToHex(cRw)) + '\n' +
                "    .cCol         =" + String.valueOf(HexDump.shortToHex(cCol)) + '\n' +
                "    .grbit        =" + String.valueOf(HexDump.shortToHex(grbit)) + '\n' +
                "    .itblAutoFmt  =" + String.valueOf(HexDump.shortToHex(itblAutoFmt)) + '\n' +
                "    .name         =" + name + '\n' +
                "    .dataField    =" + dataField + '\n' +
                "[/SXVIEW]\n";
		return buffer;
	}
}
