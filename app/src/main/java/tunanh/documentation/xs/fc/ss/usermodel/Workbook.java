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

package tunanh.documentation.xs.fc.ss.usermodel;

/**
 * High level representation of a Excel workbook.  This is the first object most users
 * will construct whether they are reading or writing a workbook.  It is also the
 * top level object for creating new sheets/etc.
 */
public interface Workbook {

    /** Extended windows meta file */
    int PICTURE_TYPE_EMF = 2;

    /** Windows Meta File */
    int PICTURE_TYPE_WMF = 3;

    /** Mac PICT format */
    int PICTURE_TYPE_PICT = 4;

    /** JPEG format */
    int PICTURE_TYPE_JPEG = 5;

    /** PNG format */
    int PICTURE_TYPE_PNG = 6;

    /** Device independent bitmap */
    int PICTURE_TYPE_DIB = 7;


    /**
     * Indicates the sheet is visible.
     *
     */
    int SHEET_STATE_VISIBLE = 0;

    /**
     * Indicates the book window is hidden, but can be shown by the user via the user interface.
     *
     */
    int SHEET_STATE_HIDDEN = 1;

    /**
     * Indicates the sheet is hidden and cannot be shown in the user interface (UI).
     *
     * <p>
     * In Excel this state is only available programmatically in VBA:
     * <code>ThisWorkbook.Sheets("MySheetName").Visible = xlSheetVeryHidden </code>
     * </p>
     *
     */
    int SHEET_STATE_VERY_HIDDEN = 2;

    //int getActiveSheetIndex();

    //void setActiveSheet(int sheetIndex);

    //int getFirstVisibleTab();

    //void setFirstVisibleTab(int sheetIndex);

    //void setSheetOrder(String sheetname, int pos);

    //void setSelectedTab(int index);

    //void setSheetName(int sheet, String name);

    // String getSheetName(int sheet);

    //int getSheetIndex(String name);

    //int getSheetIndex(Sheet sheet);

    //Sheet createSheet();

    //Sheet createSheet(String sheetname);

    //Sheet cloneSheet(int sheetNum);


    /**
     * Get the number of spreadsheets in the workbook
     *
     * @return the number of sheets
     */
    int getNumberOfSheets();

    /**
     * Get the Sheet object at the given index.
     *
     * @param index of the sheet number (0-based physical & logical)
     * @return Sheet at the provided index
     */
    Sheet getSheetAt(int index);

    //Sheet getSheet(String name);

    //void removeSheetAt(int index);

    //void setRepeatingRowsAndColumns(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow);

    //IFont createFont();

    //IFont findFont(short boldWeight, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline);

    //short getNumberOfFonts();

    //IFont getFontAt(short idx);

    //ICellStyle createCellStyle();

    //short getNumCellStyles();

    //ICellStyle getCellStyleAt(short idx);

    //void write(OutputStream stream) throws IOException;

    //int getNumberOfNames();

    //Name getName(String name);
    //Name getNameAt(int nameIndex);

    //Name createName();

    //int getNameIndex(String name);

    //void removeName(int index);

    //void removeName(String name);

    //void setPrintArea(int sheetIndex, String reference);

    //void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow);

    //String getPrintArea(int sheetIndex);

    //void removePrintArea(int sheetIndex);

    //MissingCellPolicy getMissingCellPolicy();

    //void setMissingCellPolicy(MissingCellPolicy missingCellPolicy);

    //DataFormat createDataFormat();

    //int addPicture(byte[] pictureData, int format);

    //List<? extends PictureData> getAllPictures();

    //CreationHelper getCreationHelper();

    //boolean isHidden();

    //void setHidden(boolean hiddenFlag);

    //boolean isSheetHidden(int sheetIx);

    //boolean isSheetVeryHidden(int sheetIx);

    //void setSheetHidden(int sheetIx, boolean hidden);

    //void setSheetHidden(int sheetIx, int hidden);

    //void addToolPack(UDFFinder toopack);

    //public void setForceFormulaRecalculation(boolean value);

    //boolean getForceFormulaRecalculation();

}
