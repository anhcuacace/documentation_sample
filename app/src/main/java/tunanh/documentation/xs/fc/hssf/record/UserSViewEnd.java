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

/**
 * The UserSViewEnd record marks the end of the settings for a custom view associated with the sheet
 *
 * @author Yegor Kozlov
 */
public final class UserSViewEnd extends StandardRecord {

    public final static short sid = 0x01AB;
	private final byte[] _rawData;

    public UserSViewEnd(byte[] data) {
        _rawData = data;
    }

	/**
	 * construct an UserSViewEnd record.  No fields are interpreted and the record will
	 * be serialized in its original form more or less
	 * @param in the RecordInputstream to read the record from
	 */
	public UserSViewEnd(RecordInputStream in) {
		_rawData = in.readRemainder();
	}

	/**
	 * spit the record out AS IS. no interpretation or identification
	 */
	public void serialize(LittleEndianOutput out) {
		out.write(_rawData);
	}

	protected int getDataSize() {
		return _rawData.length;
	}

    public short getSid()
    {
        return sid;
    }

    public String toString() {

        String sb = "[" + "USERSVIEWEND" + "] (0x" +
                Integer.toHexString(sid).toUpperCase() + ")\n" +
                "  rawData=" + HexDump.toHex(_rawData) + "\n" +
                "[/" + "USERSVIEWEND" + "]\n";
        return sb;
    }

    //HACK: do a "cheat" clone, see Record.java for more information
    public Object clone() {
        return cloneViaReserialise();
    }

    
}
