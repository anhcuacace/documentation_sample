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

package tunanh.documentation.xs.fc.hssf.eventusermodel;

import java.io.IOException;
import java.io.InputStream;

import tunanh.documentation.xs.fc.hssf.record.Record;
import tunanh.documentation.xs.fc.hssf.record.RecordFactoryInputStream;
import tunanh.documentation.xs.fc.poifs.filesystem.DirectoryNode;
import tunanh.documentation.xs.fc.poifs.filesystem.POIFSFileSystem;

/* loaded from: classes3.dex */
public class HSSFEventFactory {
	public void processWorkbookEvents(HSSFRequest hSSFRequest, POIFSFileSystem pOIFSFileSystem) throws IOException {
		processWorkbookEvents(hSSFRequest, pOIFSFileSystem.getRoot());
	}

	public void processWorkbookEvents(HSSFRequest hSSFRequest, DirectoryNode directoryNode) throws IOException {
		processEvents(hSSFRequest, directoryNode.createDocumentInputStream("Workbook"));
	}

	public short abortableProcessWorkbookEvents(HSSFRequest hSSFRequest, POIFSFileSystem pOIFSFileSystem) throws IOException, HSSFUserException {
		return abortableProcessWorkbookEvents(hSSFRequest, pOIFSFileSystem.getRoot());
	}

	public short abortableProcessWorkbookEvents(HSSFRequest hSSFRequest, DirectoryNode directoryNode) throws IOException, HSSFUserException {
		return abortableProcessEvents(hSSFRequest, directoryNode.createDocumentInputStream("Workbook"));
	}

	public void processEvents(HSSFRequest hSSFRequest, InputStream inputStream) {
		try {
			genericProcessEvents(hSSFRequest, inputStream);
		} catch (HSSFUserException unused) {
		}
	}

	public short abortableProcessEvents(HSSFRequest hSSFRequest, InputStream inputStream) throws HSSFUserException {
		return genericProcessEvents(hSSFRequest, inputStream);
	}

	private short genericProcessEvents(HSSFRequest hSSFRequest, InputStream inputStream) throws HSSFUserException {
		short s = 0;
		RecordFactoryInputStream recordFactoryInputStream = new RecordFactoryInputStream(inputStream, false, null);
		do {
			Record nextRecord = recordFactoryInputStream.nextRecord();
			if (nextRecord == null) {
				break;
			}
			s = hSSFRequest.processRecord(nextRecord);
		} while (s == 0);
		return s;
	}
}