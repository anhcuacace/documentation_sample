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
 * High level representation of a Excel worksheet.
 *
 * <p>
 * Sheets are the central structures within a workbook, and are where a user does most of his spreadsheet work.
 * The most common type of sheet is the worksheet, which is represented as a grid of cells. Worksheet cells can
 * contain text, numbers, dates, and formulas. Cells can also be formatted.
 * </p>
 */
public interface Sheet/* extends Iterable<IRow> */{

    /* Constants for margins */
    short LeftMargin = 0;

    short RightMargin = 1;

    short TopMargin = 2;

    short BottomMargin = 3;

    short HeaderMargin = 4;

    short FooterMargin = 5;

    byte PANE_LOWER_RIGHT = (byte) 0;

    byte PANE_UPPER_RIGHT = (byte) 1;

    byte PANE_LOWER_LEFT = (byte) 2;

    byte PANE_UPPER_LEFT = (byte) 3;

    //IRow createRow(int rownum);

    //void removeRow(IRow row);

    //IRow getRow(int rownum);

    //int getPhysicalNumberOfRows();

    //int getFirstRowNum();

    //int getLastRowNum();

    //void setColumnHidden(int columnIndex, boolean hidden);

    //boolean isColumnHidden(int columnIndex);

    //public void setRightToLeft(boolean value);

    //public boolean isRightToLeft();

    //void setColumnWidth(int columnIndex, int width);

    //int getColumnWidth(int columnIndex);

    //void setDefaultColumnWidth(int width);

    //int getDefaultColumnWidth();

    //short getDefaultRowHeight();

    //float getDefaultRowHeightInPoints();

    //void setDefaultRowHeight(short height);

    //void setDefaultRowHeightInPoints(float height);

    //public ICellStyle getColumnStyle(int column);

    //    public CellStyle setColumnStyle(int column, CellStyle style);

    //int addMergedRegion(HSSFCellRangeAddress region);

    //void setVerticallyCenter(boolean value);

    //void setHorizontallyCenter(boolean value);

    //boolean getHorizontallyCenter();

    //boolean getVerticallyCenter();

    //void removeMergedRegion(int index);

    //int getNumMergedRegions();

    //public HSSFCellRangeAddress getMergedRegion(int index);

    //Iterator<IRow> rowIterator();

    //void setForceFormulaRecalculation(boolean value);

    //boolean getForceFormulaRecalculation();

    //void setAutobreaks(boolean value);

    //void setDisplayGuts(boolean value);

    //void setDisplayZeros(boolean value);


    //boolean isDisplayZeros();

    //void setFitToPage(boolean value);

    //void setRowSumsBelow(boolean value);

    //void setRowSumsRight(boolean value);

    //boolean getAutobreaks();

    //boolean getDisplayGuts();

    //boolean getFitToPage();

    //boolean getRowSumsBelow();

    //boolean getRowSumsRight();

    //boolean isPrintGridlines();

    //void setPrintGridlines(boolean show);

    //PrintSetup getPrintSetup();

    //Header getHeader();

    //Footer getFooter();

    //void setSelected(boolean value);

    //double getMargin(short margin);

    //void setMargin(short margin, double size);

    //boolean getProtect();

    //public void protectSheet(String password);

    //boolean getScenarioProtect();

    //void setZoom(int numerator, int denominator);

    //short getTopRow();

    //short getLeftCol();

    //void showInPane(short toprow, short leftcol);

    //void shiftRows(int startRow, int endRow, int n);

    //void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight);

    //void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow);

    //void createFreezePane(int colSplit, int rowSplit);

    //void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane);

    //HSSFPaneInformation getPaneInformation();

    //void setDisplayGridlines(boolean show);

    //boolean isDisplayGridlines();

    //void setDisplayFormulas(boolean show);

    //boolean isDisplayFormulas();

    //void setDisplayRowColHeadings(boolean show);

    //boolean isDisplayRowColHeadings();

    //void setRowBreak(int row);

    //boolean isRowBroken(int row);

    //void removeRowBreak(int row);

    //int[] getRowBreaks();

    //int[] getColumnBreaks();

    //void setColumnBreak(int column);

    //boolean isColumnBroken(int column);

    //void removeColumnBreak(int column);

    //void setColumnGroupCollapsed(int columnNumber, boolean collapsed);

    //void groupColumn(int fromColumn, int toColumn);

    //void ungroupColumn(int fromColumn, int toColumn);

    //void groupRow(int fromRow, int toRow);

    //void ungroupRow(int fromRow, int toRow);

    //void setRowGroupCollapsed(int row, boolean collapse);

    //void setDefaultColumnStyle(int column, ICellStyle style);

    //void autoSizeColumn(int column);

    //void autoSizeColumn(int column, boolean useMergedCells);

    //Comment getCellComment(int row, int column);

    //Drawing createDrawingPatriarch();


    //Workbook getWorkbook();

    //String getSheetName();

    //boolean isSelected();


    //CellRange<? extends ICell> setArrayFormula(String formula, HSSFCellRangeAddress range);

    //CellRange<? extends ICell> removeArrayFormula(ICell cell);
    
    //public DataValidationHelper getDataValidationHelper();

    //public void addValidationData(DataValidation dataValidation);

    //AutoFilter setAutoFilter(HSSFCellRangeAddress range);

    //SheetConditionalFormatting getSheetConditionalFormatting();

}
