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
import tunanh.documentation.xs.fc.hssf.formula.eval.RefEvalBase;
import tunanh.documentation.xs.fc.hssf.formula.eval.ValueEval;
import tunanh.documentation.xs.fc.hssf.formula.ptg.AreaI;
import tunanh.documentation.xs.fc.hssf.formula.ptg.AreaI.OffsetArea;
import tunanh.documentation.xs.fc.ss.util.CellReference;


/**
*
* @author Josh Micich
*/
final class LazyRefEval extends RefEvalBase {

	private final SheetRefEvaluator _evaluator;

	public LazyRefEval(int rowIndex, int columnIndex, SheetRefEvaluator sre) {
		super(rowIndex, columnIndex);
		if (sre == null) {
			throw new IllegalArgumentException("sre must not be null");
		}
		_evaluator = sre;
	}

	public ValueEval getInnerValueEval() {
		return _evaluator.getEvalForCell(getRow(), getColumn());
	}

	public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {

		AreaI area = new OffsetArea(getRow(), getColumn(),
				relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);

		return new LazyAreaEval(area, _evaluator);
	}

	public String toString() {
		CellReference cr = new CellReference(getRow(), getColumn());
        String sb = getClass().getName() + "[" +
                _evaluator.getSheetName() +
                '!' +
                cr.formatAsString() +
                "]";
		return sb;
	}
}
