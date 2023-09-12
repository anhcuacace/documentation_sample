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

package tunanh.documentation.xs.fc.poifs.storage;

import com.itextpdf.text.html.HtmlTags;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import tunanh.documentation.xs.fc.poifs.common.POIFSBigBlockSize;
import tunanh.documentation.xs.fc.poifs.common.POIFSConstants;
import tunanh.documentation.xs.fc.util.HexDump;
import tunanh.documentation.xs.fc.util.IOUtils;
import tunanh.documentation.xs.fc.util.IntegerField;
import tunanh.documentation.xs.fc.util.LittleEndian;
import tunanh.documentation.xs.fc.util.LongField;
import tunanh.documentation.xs.fc.util.POILogFactory;
import tunanh.documentation.xs.fc.util.POILogger;
import tunanh.documentation.xs.fc.util.ShortField;


/**
 * The block containing the archive header
 */
public final class HeaderBlock implements HeaderBlockConstants {
    private static final byte _default_value = -1;
    private static final POILogger _logger = POILogFactory.getLogger(HeaderBlock.class);
    private int _bat_count;
    private final byte[] _data;
    private int _property_start;
    private int _sbat_count;
    private int _sbat_start;
    private int _xbat_count;
    private int _xbat_start;
    private final POIFSBigBlockSize bigBlockSize;

    public HeaderBlock(InputStream inputStream) throws IOException {
        this(readFirst512(inputStream));
        if (this.bigBlockSize.getBigBlockSize() != 512) {
            IOUtils.readFully(inputStream, new byte[this.bigBlockSize.getBigBlockSize() - 512]);
        }
    }

    public HeaderBlock(ByteBuffer byteBuffer) throws IOException {
        this(IOUtils.toByteArray(byteBuffer, 512));
    }

    private HeaderBlock(byte[] bArr) throws IOException {
        this._data = bArr;
        long j = LittleEndian.getLong(bArr, 0);
        if (j == -2226271756974174256L) {
            if (bArr[30] == 12) {
                this.bigBlockSize = POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS;
            } else if (bArr[30] == 9) {
                this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
            } else {
                throw new IOException("Unsupported blocksize  (2^" + ((int) bArr[30]) + "). Expected 2^9 or 2^12.");
            }
            this._bat_count = new IntegerField(44, bArr).get();
            this._property_start = new IntegerField(48, bArr).get();
            this._sbat_start = new IntegerField(60, bArr).get();
            this._sbat_count = new IntegerField(64, bArr).get();
            this._xbat_start = new IntegerField(68, bArr).get();
            this._xbat_count = new IntegerField(72, bArr).get();
            return;
        }
        throw new IOException("Invalid header signature; read " + longToHex(j) + ", expected " + longToHex(-2226271756974174256L));
    }

    public HeaderBlock(POIFSBigBlockSize pOIFSBigBlockSize) {
        this.bigBlockSize = pOIFSBigBlockSize;
        byte[] bArr = new byte[512];
        this._data = bArr;
        Arrays.fill(bArr, (byte) -1);
        new LongField(0, -2226271756974174256L, bArr);
        new IntegerField(8, 0, bArr);
        new IntegerField(12, 0, bArr);
        new IntegerField(16, 0, bArr);
        new IntegerField(20, 0, bArr);
        new ShortField(24, (short) 59, bArr);
        new ShortField(26, (short) 3, bArr);
        new ShortField(28, (short) -2, bArr);
        new ShortField(30, pOIFSBigBlockSize.getHeaderValue(), bArr);
        new IntegerField(32, 6, bArr);
        new IntegerField(36, 0, bArr);
        new IntegerField(40, 0, bArr);
        new IntegerField(52, 0, bArr);
        new IntegerField(56, 4096, bArr);
        this._bat_count = 0;
        this._sbat_count = 0;
        this._xbat_count = 0;
        this._property_start = -2;
        this._sbat_start = -2;
        this._xbat_start = -2;
    }

    private static byte[] readFirst512(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[512];
        int readFully = IOUtils.readFully(inputStream, bArr);
        if (readFully == 512) {
            return bArr;
        }
        throw alertShortRead(readFully, 512);
    }

    private static String longToHex(long j) {
        return new String(HexDump.longToHex(j));
    }

    private static IOException alertShortRead(int i, int i2) {
        if (i < 0) {
            i = 0;
        }
        String sb2 = " byte" +
                (i == 1 ? "" : HtmlTags.S);
        return new IOException("Unable to read entire header; " + i + sb2 + " read; expected " + i2 + " bytes");
    }

    public int getPropertyStart() {
        return this._property_start;
    }

    public void setPropertyStart(int i) {
        this._property_start = i;
    }

    public int getSBATStart() {
        return this._sbat_start;
    }

    public int getSBATCount() {
        return this._sbat_count;
    }

    public void setSBATStart(int i) {
        this._sbat_start = i;
    }

    public void setSBATBlockCount(int i) {
        this._sbat_count = i;
    }

    public int getBATCount() {
        return this._bat_count;
    }

    public void setBATCount(int i) {
        this._bat_count = i;
    }

    public int[] getBATArray() {
        int min = Math.min(this._bat_count, 109);
        int[] iArr = new int[min];
        int i = 76;
        for (int i2 = 0; i2 < min; i2++) {
            iArr[i2] = LittleEndian.getInt(this._data, i);
            i += 4;
        }
        return iArr;
    }

    public void setBATArray(int[] iArr) {
        int min = Math.min(iArr.length, 109);
        int i = 109 - min;
        int i2 = 76;
        for (int i3 = 0; i3 < min; i3++) {
            LittleEndian.putInt(this._data, i2, iArr[i3]);
            i2 += 4;
        }
        for (int i4 = 0; i4 < i; i4++) {
            LittleEndian.putInt(this._data, i2, -1);
            i2 += 4;
        }
    }

    public int getXBATCount() {
        return this._xbat_count;
    }

    public void setXBATCount(int i) {
        this._xbat_count = i;
    }

    public int getXBATIndex() {
        return this._xbat_start;
    }

    public void setXBATStart(int i) {
        this._xbat_start = i;
    }

    public POIFSBigBlockSize getBigBlockSize() {
        return this.bigBlockSize;
    }

    public void writeData(OutputStream outputStream) throws IOException {
        new IntegerField(44, this._bat_count, this._data);
        new IntegerField(48, this._property_start, this._data);
        new IntegerField(60, this._sbat_start, this._data);
        new IntegerField(64, this._sbat_count, this._data);
        new IntegerField(68, this._xbat_start, this._data);
        new IntegerField(72, this._xbat_count, this._data);
        outputStream.write(this._data, 0, 512);
        for (int i = 512; i < this.bigBlockSize.getBigBlockSize(); i++) {
            outputStream.write(0);
        }
    }
}