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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tunanh.documentation.xs.fc.EncryptedDocumentException;
import tunanh.documentation.xs.fc.hssf.record.crypto.Biff8EncryptionKey;
import tunanh.documentation.xs.system.IControl;


/* loaded from: classes3.dex */
public final class RecordFactoryInputStream {
    public static String userPassword;
    private int _bofDepth;
    private DrawingRecord _lastDrawingRecord = new DrawingRecord();
    private Record _lastRecord;
    private boolean _lastRecordWasEOFLevelZero;
    private RecordInputStream _recStream;
    private final boolean _shouldIncludeContinueRecords;
    private Record[] _unreadRecordBuffer;
    private int _unreadRecordIndex;

    /* loaded from: classes3.dex */
    private static final class StreamEncryptionInfo {
        private final FilePassRecord _filePassRec;
        private final boolean _hasBOFRecord;
        private final int _initialRecordsSize;
        private final Record _lastRecord;

        public StreamEncryptionInfo(RecordInputStream recordInputStream, List<Record> list) {
            recordInputStream.nextRecord();
            int remaining = recordInputStream.remaining() + 4;
            Record createSingleRecord = RecordFactory.createSingleRecord(recordInputStream);
            list.add(createSingleRecord);
            FilePassRecord filePassRecord = null;
            if (createSingleRecord instanceof BOFRecord) {
                this._hasBOFRecord = true;
                if (recordInputStream.hasNextRecord()) {
                    recordInputStream.nextRecord();
                    createSingleRecord = RecordFactory.createSingleRecord(recordInputStream);
                    remaining += createSingleRecord.getRecordSize();
                    list.add(createSingleRecord);
                    if (createSingleRecord instanceof FilePassRecord) {
                        filePassRecord = (FilePassRecord) createSingleRecord;
                        list.remove(list.size() - 1);
                        createSingleRecord = list.get(0);
                    } else if (createSingleRecord instanceof EOFRecord) {
                        throw new IllegalStateException("Nothing between BOF and EOF");
                    }
                }
            } else {
                this._hasBOFRecord = false;
            }
            this._initialRecordsSize = remaining;
            this._filePassRec = filePassRecord;
            this._lastRecord = createSingleRecord;
        }

        public RecordInputStream createDecryptingStream(InputStream original) {
            FilePassRecord fpr = _filePassRec;
            String userPassword = Biff8EncryptionKey.getCurrentUserPassword();

            Biff8EncryptionKey key;
            if (userPassword == null) {
                key = Biff8EncryptionKey.create(fpr.getDocId());
            } else {
                key = Biff8EncryptionKey.create(userPassword, fpr.getDocId());
            }
            if (!key.validate(fpr.getSaltData(), fpr.getSaltHash())) {
				/*throw new EncryptedDocumentException(
						(userPassword == null ? "Default" : "Supplied")
						+ " password is invalid for docId/saltData/saltHash");*/
                throw new EncryptedDocumentException("Cannot process encrypted office files!");
            }
            return new RecordInputStream(original, key, _initialRecordsSize);
        }

        public RecordInputStream createDecryptingStream(InputStream inputStream, IControl iControl) {
            Biff8EncryptionKey biff8EncryptionKey;
            FilePassRecord filePassRecord = this._filePassRec;
            RecordFactoryInputStream.userPassword = Biff8EncryptionKey.getCurrentUserPassword();
            if (RecordFactoryInputStream.userPassword == null) {
                biff8EncryptionKey = Biff8EncryptionKey.create(filePassRecord.getDocId());
            } else {
                biff8EncryptionKey = Biff8EncryptionKey.create(RecordFactoryInputStream.userPassword, filePassRecord.getDocId());
            }
            if (biff8EncryptionKey.validate(filePassRecord.getSaltData(), filePassRecord.getSaltHash())) {
                return new RecordInputStream(inputStream, biff8EncryptionKey, this._initialRecordsSize);
            }
            throw new EncryptedDocumentException("Cannot process encrypted office files!");
        }

        public boolean hasEncryption() {
            return this._filePassRec != null;
        }

        public Record getLastRecord() {
            return this._lastRecord;
        }

        public boolean hasBOFRecord() {
            return this._hasBOFRecord;
        }
    }

    public RecordFactoryInputStream(InputStream inputStream, boolean z, IControl iControl) {
        this._unreadRecordIndex = -1;
        this._lastRecord = null;
        RecordInputStream recordInputStream = new RecordInputStream(inputStream);
        ArrayList arrayList = new ArrayList();
        StreamEncryptionInfo streamEncryptionInfo = new StreamEncryptionInfo(recordInputStream, arrayList);
        recordInputStream = streamEncryptionInfo.hasEncryption() ? streamEncryptionInfo.createDecryptingStream(inputStream, iControl) : recordInputStream;
        if (!arrayList.isEmpty()) {
            Record[] recordArr = new Record[arrayList.size()];
            this._unreadRecordBuffer = recordArr;
            arrayList.toArray(recordArr);
            this._unreadRecordIndex = 0;
        }
        this._recStream = recordInputStream;
        this._shouldIncludeContinueRecords = z;
        this._lastRecord = streamEncryptionInfo.getLastRecord();
        this._bofDepth = streamEncryptionInfo.hasBOFRecord() ? 1 : 0;
        this._lastRecordWasEOFLevelZero = false;
    }

    public Record nextRecord() {
        Record nextUnreadRecord = getNextUnreadRecord();
        if (nextUnreadRecord != null) {
            return nextUnreadRecord;
        }
        while (this._recStream.hasNextRecord()) {
            if (this._lastRecordWasEOFLevelZero && this._recStream.getNextSid() != 2057) {
                return null;
            }
            this._recStream.nextRecord();
            Record readNextRecord = readNextRecord();
            if (readNextRecord != null) {
                return readNextRecord;
            }
        }
        return null;
    }

    private Record getNextUnreadRecord() {
        Record[] recordArr = this._unreadRecordBuffer;
        if (recordArr != null) {
            int i = this._unreadRecordIndex;
            if (i < recordArr.length) {
                Record record = recordArr[i];
                this._unreadRecordIndex = i + 1;
                return record;
            }
            this._unreadRecordIndex = -1;
            this._unreadRecordBuffer = null;
        }
        return null;
    }

    private Record readNextRecord() {
        Record createSingleRecord = RecordFactory.createSingleRecord(this._recStream);
        this._lastRecordWasEOFLevelZero = false;
        if (!(this._lastDrawingRecord == null || createSingleRecord.getSid() == 60 || createSingleRecord.getSid() == 93 || createSingleRecord.getSid() == 438)) {
            this._lastDrawingRecord = null;
        }
        if (createSingleRecord instanceof BOFRecord) {
            this._bofDepth++;
            return createSingleRecord;
        } else if (createSingleRecord instanceof EOFRecord) {
            int i = this._bofDepth - 1;
            this._bofDepth = i;
            if (i < 1) {
                this._lastRecordWasEOFLevelZero = true;
            }
            return createSingleRecord;
        } else if (createSingleRecord instanceof DBCellRecord) {
            return null;
        } else {
            if (createSingleRecord instanceof RKRecord) {
                return RecordFactory.convertToNumberRecord((RKRecord) createSingleRecord);
            }
            if (createSingleRecord instanceof MulRKRecord) {
                NumberRecord[] convertRKRecords = RecordFactory.convertRKRecords((MulRKRecord) createSingleRecord);
                this._unreadRecordBuffer = convertRKRecords;
                this._unreadRecordIndex = 1;
                return convertRKRecords[0];
            }
            if (createSingleRecord.getSid() == 235) {
                Record record = this._lastRecord;
                if (record instanceof DrawingGroupRecord) {
                    ((DrawingGroupRecord) record).join((AbstractEscherHolderRecord) createSingleRecord);
                    return null;
                }
            }
            if (createSingleRecord.getSid() == 60) {
                ContinueRecord continueRecord = (ContinueRecord) createSingleRecord;
                Record record2 = this._lastRecord;
                if ((record2 instanceof ObjRecord) || (record2 instanceof TextObjectRecord)) {
                    DrawingRecord drawingRecord = this._lastDrawingRecord;
                    if (drawingRecord != null) {
                        drawingRecord.processContinueRecord(continueRecord.getData());
                        continueRecord.resetData();
                    }
                    if (this._shouldIncludeContinueRecords) {
                        return createSingleRecord;
                    }
                    return null;
                } else if (record2 instanceof DrawingGroupRecord) {
                    ((DrawingGroupRecord) record2).processContinueRecord(continueRecord.getData());
                    return null;
                } else if (record2 instanceof DrawingRecord) {
                    ((DrawingRecord) record2).processContinueRecord(continueRecord.getData());
                    return null;
                } else if ((record2 instanceof UnknownRecord) || (record2 instanceof EOFRecord)) {
                    return createSingleRecord;
                } else {
                    throw new RecordFormatException("Unhandled Continue Record followining " + this._lastRecord.getClass());
                }
            } else {
                this._lastRecord = createSingleRecord;
                if (createSingleRecord instanceof DrawingRecord) {
                    this._lastDrawingRecord = (DrawingRecord) createSingleRecord;
                }
                return createSingleRecord;
            }
        }
    }

    /**
     * @param shouldIncludeContinueRecords caller can pass <code>false</code> if loose
     *                                     {@link ContinueRecord}s should be skipped (this is sometimes useful in event based
     *                                     processing).
     */
    public RecordFactoryInputStream(InputStream in, boolean shouldIncludeContinueRecords) {
        RecordInputStream rs = new RecordInputStream(in);
        List<Record> records = new ArrayList<Record>();
        StreamEncryptionInfo sei = new StreamEncryptionInfo(rs, records);
        if (sei.hasEncryption()) {
            rs = sei.createDecryptingStream(in);
        } else {
            // typical case - non-encrypted stream
        }

        if (!records.isEmpty()) {
            _unreadRecordBuffer = new Record[records.size()];
            records.toArray(_unreadRecordBuffer);
            _unreadRecordIndex = 0;
        }
        _recStream = rs;
        _shouldIncludeContinueRecords = shouldIncludeContinueRecords;
        _lastRecord = sei.getLastRecord();

        /*
         * How to recognise end of stream?
         * In the best case, the underlying input stream (in) ends just after the last EOF record
         * Usually however, the stream is padded with an arbitrary byte count.  Excel and most apps
         * reliably use zeros for padding and if this were always the case, this code could just
         * skip all the (zero sized) records with sid==0.  However, bug 46987 shows a file with
         * non-zero padding that is read OK by Excel (Excel also fixes the padding).
         *
         * So to properly detect the workbook end of stream, this code has to identify the last
         * EOF record.  This is not so easy because the worbook bof+eof pair do not bracket the
         * whole stream.  The worksheets follow the workbook, but it is not easy to tell how many
         * sheet sub-streams should be present.  Hence we are looking for an EOF record that is not
         * immediately followed by a BOF record.  One extra complication is that bof+eof sub-
         * streams can be nested within worksheet streams and it's not clear in these cases what
         * record might follow any EOF record.  So we also need to keep track of the bof/eof
         * nesting level.
         */
        _bofDepth = sei.hasBOFRecord() ? 1 : 0;
        _lastRecordWasEOFLevelZero = false;
    }


    public void dispose() {
        this._recStream = null;
        this._unreadRecordBuffer = null;
        this._lastRecord = null;
        this._lastDrawingRecord = null;
    }
}