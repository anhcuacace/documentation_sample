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


import tunanh.documentation.xs.constant.fc.ConstantValueParser;
import tunanh.documentation.xs.fc.util.LittleEndianOutput;


/**
 * Title:       CRN(0x005A) <p/>
 * Description: This record stores the contents of an external cell or cell range <p/>
 * REFERENCE:   OOO 5.23<p/>
 *
 * @author josh micich
 */
public final class CRNRecord extends StandardRecord {
	public final static short sid = 0x005A;

	private final int	 field_1_last_column_index;
	private final int	 field_2_first_column_index;
	private final int	 field_3_row_index;
	private final Object[] field_4_constant_values;

	public CRNRecord() {
		throw new RuntimeException("incomplete code");
	}

	public int getNumberOfCRNs() {
		return field_1_last_column_index;
	}


	public CRNRecord(RecordInputStream in) {
		field_1_last_column_index = in.readUByte();
		field_2_first_column_index = in.readUByte();
		field_3_row_index = in.readShort();
		int nValues = field_1_last_column_index - field_2_first_column_index + 1;
		field_4_constant_values = ConstantValueParser.parse(in, nValues);
	}


	public String toString() {
        String sb = getClass().getName() + " [CRN" +
                " rowIx=" + field_3_row_index +
                " firstColIx=" + field_2_first_column_index +
                " lastColIx=" + field_1_last_column_index +
                "]";
		return sb;
	}
	protected int getDataSize() {
		return 4 + ConstantValueParser.getEncodedSize(field_4_constant_values);
	}

	public void serialize(LittleEndianOutput out) {
		out.writeByte(field_1_last_column_index);
		out.writeByte(field_2_first_column_index);
		out.writeShort(field_3_row_index);
		ConstantValueParser.encode(out, field_4_constant_values);
	}

	/**
	 * return the non static version of the id for this record.
	 */
	public short getSid() {
		return sid;
	}
}
