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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tunanh.documentation.xs.fc.hssf.record.chart.AreaFormatRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.AreaRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.AxisLineFormatRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.AxisOptionsRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.AxisParentRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.AxisRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.AxisUsedRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.BarRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.BeginRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.CatLabRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.CategorySeriesAxisRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartEndBlockRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartEndObjectRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartFRTInfoRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartStartBlockRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartStartObjectRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ChartTitleFormatRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.DatRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.DataFormatRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.DataLabelExtensionRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.DefaultDataLabelTextPropertiesRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.EndRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.FontBasisRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.FontIndexRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.FrameRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.LegendRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.LineFormatRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.LinkedDataRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.NumberFormatIndexRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ObjectLinkRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.PlotAreaRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.PlotGrowthRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.SeriesLabelsRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.SeriesListRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.SeriesRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.SeriesTextRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.SeriesToChartGroupRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.SheetPropertiesRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.TextRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.TickRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.UnitsRecord;
import tunanh.documentation.xs.fc.hssf.record.chart.ValueRangeRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.DataItemRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.PageItemRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.StreamIDRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.ViewDefinitionRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.ViewFieldsRecord;
import tunanh.documentation.xs.fc.hssf.record.pivottable.ViewSourceRecord;
import tunanh.documentation.xs.system.AbortReaderError;
import tunanh.documentation.xs.system.AbstractReader;


/**
 * Title:  Record Factory<P>
 * Description:  Takes a stream and outputs an array of Record objects.<P>
 *
 * @author Andrew C. Oliver (acoliver at apache dot org)
 * @author Marc Johnson (mjohnson at apache dot org)
 * @author Glen Stampoultzis (glens at apache.org)
 * @author Csaba Nagy (ncsaba at yahoo dot com)
 * @see tunanh.documentation.xs.fc.hssf.eventmodel.EventRecordFactory
 */
public final class RecordFactory {
    private static final Class<?>[] CONSTRUCTOR_ARGS = {RecordInputStream.class};
    private static final int NUM_RECORDS = 512;
    private static short[] _allKnownRecordSIDs;
    private static final Map<Integer, I_RecordCreator> _recordCreatorsById;
    private static final Class<? extends Record>[] recordClasses;


    public interface I_RecordCreator {
        Record create(RecordInputStream recordInputStream);

        Class<? extends Record> getRecordClass();
    }

    public static final class ReflectionConstructorRecordCreator implements I_RecordCreator {
        private final Constructor<? extends Record> _c;

        public ReflectionConstructorRecordCreator(Constructor<? extends Record> constructor) {
            this._c = constructor;
        }

        @Override
        public Record create(RecordInputStream recordInputStream) {
            try {
                return this._c.newInstance(recordInputStream);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e2) {
                throw new RuntimeException(e2);
            } catch (InstantiationException e3) {
                throw new RuntimeException(e3);
            } catch (InvocationTargetException e4) {
                throw new RecordFormatException("Unable to construct record instance", e4.getTargetException());
            }
        }

        @Override
        public Class<? extends Record> getRecordClass() {
            return this._c.getDeclaringClass();
        }
    }

    /**
     * A "create" method is used instead of the usual constructor if the created record might
     * be of a different class to the declaring class.
     */
    private static final class ReflectionMethodRecordCreator implements I_RecordCreator
    {

        private final Method _m;

        public ReflectionMethodRecordCreator(Method m)
        {
            _m = m;
        }

        public Record create(RecordInputStream in)
        {
            Object[] args = {in,};
            try
            {
                return (Record)_m.invoke(null, args);
            }
            catch(IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch(IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch(InvocationTargetException e)
            {
                throw new RecordFormatException("Unable to construct record instance",
                        e.getTargetException());
            }
        }

        @ SuppressWarnings("unchecked")
        public Class< ? extends Record> getRecordClass()
        {
            return (Class< ? extends Record>)_m.getDeclaringClass();
        }
    }


    static {
        final Class<? extends Record>[] clsArr = new Class[]{ArrayRecord.class, AutoFilterInfoRecord.class, BackupRecord.class, BlankRecord.class, BOFRecord.class, BookBoolRecord.class, BoolErrRecord.class, BottomMarginRecord.class, BoundSheetRecord.class, CalcCountRecord.class, CalcModeRecord.class, CFHeaderRecord.class, CFRuleRecord.class, ChartRecord.class, ChartTitleFormatRecord.class, CodepageRecord.class, ColumnInfoRecord.class, ContinueRecord.class, CountryRecord.class, CRNCountRecord.class, CRNRecord.class, DateWindow1904Record.class, DBCellRecord.class, DefaultColWidthRecord.class, DefaultRowHeightRecord.class, DeltaRecord.class, DimensionsRecord.class, DrawingGroupRecord.class, DrawingRecord.class, DrawingSelectionRecord.class, DSFRecord.class, DVALRecord.class, DVRecord.class, EOFRecord.class, ExtendedFormatRecord.class, ExternalNameRecord.class, ExternSheetRecord.class, ExtSSTRecord.class, FeatRecord.class, FeatHdrRecord.class, FilePassRecord.class, FileSharingRecord.class, FnGroupCountRecord.class, FontRecord.class, FooterRecord.class, FormatRecord.class, FormulaRecord.class, GridsetRecord.class, GutsRecord.class, HCenterRecord.class, HeaderRecord.class, HeaderFooterRecord.class, HideObjRecord.class, HorizontalPageBreakRecord.class, HyperlinkRecord.class, IndexRecord.class, InterfaceEndRecord.class, InterfaceHdrRecord.class, IterationRecord.class, LabelRecord.class, LabelSSTRecord.class, LeftMarginRecord.class, LegendRecord.class, MergeCellsRecord.class, MMSRecord.class, MulBlankRecord.class, MulRKRecord.class, NameRecord.class, NameCommentRecord.class, NoteRecord.class, NumberRecord.class, ObjectProtectRecord.class, ObjRecord.class, PaletteRecord.class, PaneRecord.class, PasswordRecord.class, PasswordRev4Record.class, PrecisionRecord.class, PrintGridlinesRecord.class, PrintHeadersRecord.class, PrintSetupRecord.class, ProtectionRev4Record.class, ProtectRecord.class, RecalcIdRecord.class, RefModeRecord.class, RefreshAllRecord.class, RightMarginRecord.class, RKRecord.class, RowRecord.class, SaveRecalcRecord.class, ScenarioProtectRecord.class, SelectionRecord.class, SeriesRecord.class, SeriesTextRecord.class, SharedFormulaRecord.class, SSTRecord.class, StringRecord.class, StyleRecord.class, SupBookRecord.class, TabIdRecord.class, TableRecord.class, TableStylesRecord.class, TextObjectRecord.class, TopMarginRecord.class, UncalcedRecord.class, UseSelFSRecord.class, UserSViewBegin.class, UserSViewEnd.class, ValueRangeRecord.class, VCenterRecord.class, VerticalPageBreakRecord.class, WindowOneRecord.class, WindowProtectRecord.class, WindowTwoRecord.class, WriteAccessRecord.class, WriteProtectRecord.class, WSBoolRecord.class, BeginRecord.class, ChartFRTInfoRecord.class, ChartStartBlockRecord.class, ChartEndBlockRecord.class, ChartStartObjectRecord.class, ChartEndObjectRecord.class, CatLabRecord.class, DataFormatRecord.class, EndRecord.class, LinkedDataRecord.class, SeriesToChartGroupRecord.class, AreaFormatRecord.class, AreaRecord.class, AxisLineFormatRecord.class, AxisOptionsRecord.class, AxisParentRecord.class, AxisRecord.class, AxisUsedRecord.class, BarRecord.class, CategorySeriesAxisRecord.class, DatRecord.class, DefaultDataLabelTextPropertiesRecord.class, FontBasisRecord.class, FontIndexRecord.class, FrameRecord.class, LineFormatRecord.class, NumberFormatIndexRecord.class, PlotAreaRecord.class, PlotGrowthRecord.class, SeriesLabelsRecord.class, SeriesListRecord.class, SheetPropertiesRecord.class, TickRecord.class, UnitsRecord.class, DataItemRecord.class, ExtendedPivotTableViewFieldsRecord.class, PageItemRecord.class, StreamIDRecord.class, ViewDefinitionRecord.class, ViewFieldsRecord.class, ViewSourceRecord.class, DataLabelExtensionRecord.class, TextRecord.class, ObjectLinkRecord.class};
        recordClasses = clsArr;
        _recordCreatorsById = recordsToMap(clsArr);
    }

    public static Class<? extends Record> getRecordClass(int i) {
        I_RecordCreator i_RecordCreator = _recordCreatorsById.get(Integer.valueOf(i));
        if (i_RecordCreator == null) {
            return null;
        }
        return i_RecordCreator.getRecordClass();
    }

    public static Record[] createRecord(RecordInputStream recordInputStream) {
        Record createSingleRecord = createSingleRecord(recordInputStream);
        return createSingleRecord instanceof DBCellRecord ? new Record[]{null} : createSingleRecord instanceof RKRecord ? new Record[]{convertToNumberRecord((RKRecord) createSingleRecord)} : createSingleRecord instanceof MulRKRecord ? convertRKRecords((MulRKRecord) createSingleRecord) : new Record[]{createSingleRecord};
    }

    public static Record createSingleRecord(RecordInputStream recordInputStream) {
        I_RecordCreator i_RecordCreator = _recordCreatorsById.get(Integer.valueOf(recordInputStream.getSid()));
        if (i_RecordCreator == null) {
            return new UnknownRecord(recordInputStream);
        }
        return i_RecordCreator.create(recordInputStream);
    }

    public static NumberRecord convertToNumberRecord(RKRecord rKRecord) {
        NumberRecord numberRecord = new NumberRecord();
        numberRecord.setColumn(rKRecord.getColumn());
        numberRecord.setRow(rKRecord.getRow());
        numberRecord.setXFIndex(rKRecord.getXFIndex());
        numberRecord.setValue(rKRecord.getRKNumber());
        return numberRecord;
    }

    public static NumberRecord[] convertRKRecords(MulRKRecord mulRKRecord) {
        NumberRecord[] numberRecordArr = new NumberRecord[mulRKRecord.getNumColumns()];
        for (int i = 0; i < mulRKRecord.getNumColumns(); i++) {
            NumberRecord numberRecord = new NumberRecord();
            numberRecord.setColumn((short) (mulRKRecord.getFirstColumn() + i));
            numberRecord.setRow(mulRKRecord.getRow());
            numberRecord.setXFIndex(mulRKRecord.getXFAt(i));
            numberRecord.setValue(mulRKRecord.getRKNumberAt(i));
            numberRecordArr[i] = numberRecord;
        }
        return numberRecordArr;
    }

    public static BlankRecord[] convertBlankRecords(MulBlankRecord mulBlankRecord) {
        BlankRecord[] blankRecordArr = new BlankRecord[mulBlankRecord.getNumColumns()];
        for (int i = 0; i < mulBlankRecord.getNumColumns(); i++) {
            BlankRecord blankRecord = new BlankRecord();
            blankRecord.setColumn((short) (mulBlankRecord.getFirstColumn() + i));
            blankRecord.setRow(mulBlankRecord.getRow());
            blankRecord.setXFIndex(mulBlankRecord.getXFAt(i));
            blankRecordArr[i] = blankRecord;
        }
        return blankRecordArr;
    }

    public static short[] getAllKnownRecordSIDs() {
        if (_allKnownRecordSIDs == null) {
            Map<Integer, I_RecordCreator> map = _recordCreatorsById;
            short[] sArr = new short[map.size()];
            int i = 0;
            for (Integer num : map.keySet()) {
                i++;
                sArr[i] = num.shortValue();
            }
            Arrays.sort(sArr);
            _allKnownRecordSIDs = sArr;
        }
        return _allKnownRecordSIDs.clone();
    }


    /**
     * gets the record constructors and sticks them in the map by SID
     * @return map of SIDs to short,short,byte[] constructors for Record classes
     * most of org.apache.poi.hssf.record.*
     */
    private static Map<Integer, I_RecordCreator> recordsToMap(Class< ? extends Record>[] records)
    {
        Map<Integer, I_RecordCreator> result = new HashMap<Integer, I_RecordCreator>();
        Set<Class< ? >> uniqueRecClasses = new HashSet<Class< ? >>(records.length * 3 / 2);

        for (int i = 0; i < records.length; i++)
        {

            Class< ? extends Record> recClass = records[i];
            if (!Record.class.isAssignableFrom(recClass))
            {
                throw new RuntimeException("Invalid record sub-class (" + recClass.getName() + ")");
            }
            if (Modifier.isAbstract(recClass.getModifiers()))
            {
                throw new RuntimeException("Invalid record class (" + recClass.getName()
                        + ") - must not be abstract");
            }
            if (!uniqueRecClasses.add(recClass))
            {
                throw new RuntimeException("duplicate record class (" + recClass.getName() + ")");
            }

            int sid;
            try
            {
                sid = recClass.getField("sid").getShort(null);
            }
            catch(Exception illegalArgumentException)
            {
                throw new RecordFormatException("Unable to determine record types");
            }
            Integer key = Integer.valueOf(sid);
            if (result.containsKey(key))
            {
                Class< ? > prevClass = result.get(key).getRecordClass();
                throw new RuntimeException("duplicate record sid 0x"
                        + Integer.toHexString(sid).toUpperCase() + " for classes ("
                        + recClass.getName() + ") and (" + prevClass.getName() + ")");
            }
            result.put(key, getRecordCreator(recClass));
        }
        //		result.put(Integer.valueOf(0x0406), result.get(Integer.valueOf(0x06)));
        return result;
    }


    private static I_RecordCreator getRecordCreator(Class< ? extends Record> recClass)
    {
        try
        {
            Constructor< ? extends Record> constructor;
            constructor = recClass.getConstructor(CONSTRUCTOR_ARGS);
            return new ReflectionConstructorRecordCreator(constructor);
        }
        catch(NoSuchMethodException e)
        {
            // fall through and look for other construction methods
        }
        try
        {
            Method m = recClass.getDeclaredMethod("create", CONSTRUCTOR_ARGS);
            return new ReflectionMethodRecordCreator(m);
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException("Failed to find constructor or create method for ("
                    + recClass.getName() + ").");
        }
    }

    /**
     * Create an array of records from an input stream
     *
     * @param in the InputStream from which the records will be obtained
     *
     * @return an array of Records created from the InputStream
     *
     * @exception RecordFormatException on error processing the InputStream
     */
    public static List<Record> createRecords(InputStream in, AbstractReader iAbortListener) throws RecordFormatException
    {

        List<Record> records = new ArrayList<Record>(NUM_RECORDS);

        RecordFactoryInputStream recStream = new RecordFactoryInputStream(in, true);

        Record record;
        while ((record = recStream.nextRecord()) != null)
        {
            if(iAbortListener != null && iAbortListener.isAborted())
            {
                throw new AbortReaderError("abort Reader");
            }
            records.add(record);
        }

        recStream.dispose();
        recStream =  null;

        return records;
    }

    /**
     * Create an array of records from an input stream
     *
     * @param in the InputStream from which the records will be obtained
     *
     * @return an array of Records created from the InputStream
     *
     * @exception RecordFormatException on error processing the InputStream
     */
    public static List<Record> createRecords(InputStream in) throws RecordFormatException
    {
        return createRecords(in, null);
    }



}
