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

package tunanh.documentation.xs.fc.hssf.formula;


import tunanh.documentation.xs.fc.hssf.formula.eval.AreaEval;
import tunanh.documentation.xs.fc.hssf.formula.eval.AreaEvalBase;
import tunanh.documentation.xs.fc.hssf.formula.eval.ValueEval;
import tunanh.documentation.xs.fc.hssf.formula.ptg.AreaI;
import tunanh.documentation.xs.fc.hssf.formula.ptg.AreaI.OffsetArea;
import tunanh.documentation.xs.fc.ss.util.CellReference;


/**
 *
 * @author Josh Micich
 */
final class LazyAreaEval extends AreaEvalBase {

	private final SheetRefEvaluator _evaluator;

	LazyAreaEval(AreaI ptg, SheetRefEvaluator evaluator) {
		super(ptg);
		_evaluator = evaluator;
	}

	public LazyAreaEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex,
			int lastColumnIndex, SheetRefEvaluator evaluator) {
		super(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex);
		_evaluator = evaluator;
	}

	public ValueEval getRelativeValue(int relativeRowIndex, int relativeColumnIndex) {

		int rowIx = (relativeRowIndex + getFirstRow() ) & 0xFFFF;
		int colIx = (relativeColumnIndex + getFirstColumn() ) & 0x00FF;

		return _evaluator.getEvalForCell(rowIx, colIx);
	}

	public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
		AreaI area = new OffsetArea(getFirstRow(), getFirstColumn(),
				relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);

		return new LazyAreaEval(area, _evaluator);
	}
	public LazyAreaEval getRow(int rowIndex) {
		if (rowIndex >= getHeight()) {
			throw new IllegalArgumentException("Invalid rowIndex " + rowIndex
					+ ".  Allowable range is (0.." + getHeight() + ").");
		}
		int absRowIx = getFirstRow() + rowIndex;
		return new LazyAreaEval(absRowIx, getFirstColumn(), absRowIx, getLastColumn(), _evaluator);
	}
	public LazyAreaEval getColumn(int columnIndex) {
		if (columnIndex >= getWidth()) {
			throw new IllegalArgumentException("Invalid columnIndex " + columnIndex
					+ ".  Allowable range is (0.." + getWidth() + ").");
		}
		int absColIx = getFirstColumn() + columnIndex;
		return new LazyAreaEval(getFirstRow(), absColIx, getLastRow(), absColIx, _evaluator);
	}

	public String toString() {
		CellReference crA = new CellReference(getFirstRow(), getFirstColumn());
		CellReference crB = new CellReference(getLastRow(), getLastColumn());
        String sb = getClass().getName() + "[" +
                _evaluator.getSheetName() +
                '!' +
                crA.formatAsString() +
                ':' +
                crB.formatAsString() +
                "]";
		return sb;
	}

    /**
     * @return  whether cell at rowIndex and columnIndex is a subtotal
    */
    public boolean isSubTotal(int rowIndex, int columnIndex){
        // delegate the query to the sheet evaluator which has access to internal ptgs
        return _evaluator.isSubTotal(rowIndex, columnIndex);
    }
}
