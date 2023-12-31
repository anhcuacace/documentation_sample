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
import tunanh.documentation.xs.fc.util.StringUtil;


/**
 * SERIESTEXT (0x100D)</p> 
 * Defines a series name</p>
 * 
 * @author Andrew C. Oliver (acoliver at apache.org)
 */
public final class SeriesTextRecord extends StandardRecord {
	public final static short sid = 0x100D;

	/** the actual text cannot be longer than 255 characters */
	private static final int MAX_LEN = 0xFF;
	private int field_1_id;
	private boolean is16bit;
	private String field_4_text;

	public SeriesTextRecord() {
		field_4_text = "";
		is16bit = false;
	}

	public SeriesTextRecord(RecordInputStream in) {
		field_1_id = in.readUShort();
		int field_2_textLength = in.readUByte();
		is16bit = (in.readUByte() & 0x01) != 0;
		if (is16bit) {
			field_4_text = in.readUnicodeLEString(field_2_textLength);
		} else {
			field_4_text = in.readCompressedUnicode(field_2_textLength);
		}
	}

	public String toString() {

        String sb = "[SERIESTEXT]\n" +
                "  .id     =" + String.valueOf(HexDump.shortToHex(getId())) + '\n' +
                "  .textLen=" + field_4_text.length() + '\n' +
                "  .is16bit=" + is16bit + '\n' +
                "  .text   =" + " (" + getText() + " )" + '\n' +
                "[/SERIESTEXT]\n";
		return sb;
	}

	public void serialize(LittleEndianOutput out) {

		out.writeShort(field_1_id);
		out.writeByte(field_4_text.length());
		if (is16bit) {
			// Excel (2007) seems to choose 16bit regardless of whether it is needed
			out.writeByte(0x01);
			StringUtil.putUnicodeLE(field_4_text, out);
		} else {
			// Excel can read this OK
			out.writeByte(0x00);
			StringUtil.putCompressedUnicode(field_4_text, out);
		}
	}

	protected int getDataSize() {
		return 2 + 1 + 1 + field_4_text.length() * (is16bit ? 2 : 1);
	}

	public short getSid() {
		return sid;
	}

	public Object clone() {
		SeriesTextRecord rec = new SeriesTextRecord();

		rec.field_1_id = field_1_id;
		rec.is16bit = is16bit;
		rec.field_4_text = field_4_text;
		return rec;
	}

	/**
	 * Get the id field for the SeriesText record.
	 */
	public int getId() {
		return field_1_id;
	}

	/**
	 * Set the id field for the SeriesText record.
	 */
	public void setId(int id) {
		field_1_id = id;
	}

	/**
	 * Get the text field for the SeriesText record.
	 */
	public String getText() {
		return field_4_text;
	}

	/**
	 * Set the text field for the SeriesText record.
	 */
	public void setText(String text) {
		if (text.length() > MAX_LEN) {
			throw new IllegalArgumentException("Text is too long ("
					+ text.length() + ">" + MAX_LEN + ")");
		}
		field_4_text = text;
		is16bit = StringUtil.hasMultibyte(text);
	}
}
