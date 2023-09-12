package tunanh.documentation.xs.extensions;


import android.content.Context;
import android.util.AttributeSet;

import tunanh.documentation.R;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.ss.control.ExcelView;
import tunanh.documentation.xs.ss.control.Spreadsheet;
import tunanh.documentation.xs.ss.model.baseModel.Sheet;
import tunanh.documentation.xs.ss.util.ModelUtil;
import tunanh.documentation.xs.system.IControl;


public class SSToolsbar extends AToolsbar {
    public SSToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SSToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.ic_copy, R.drawable.ic_copy, R.string.file_toolsbar_copy, EventConstant.FILE_COPY_ID, true);
        addButton(R.drawable.ic_baseline_link_24, R.drawable.ic_baseline_link_24, R.string.app_toolsbar_hyperlink, EventConstant.APP_HYPERLINK, true);
        addButton(R.drawable.ic_search, R.drawable.ic_search, R.string.app_toolsbar_find, 536870912, true);
        addButton(R.drawable.share, R.drawable.share, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addButton(R.drawable.ic_web_search, R.drawable.ic_web_search, R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void updateStatus() {
        Spreadsheet spreadsheet = ((ExcelView) this.control.getView()).getSpreadsheet();
        if (spreadsheet.getSheetView() != null) {
            boolean z = true;
            setEnabled(536870912, true);
            setEnabled(EventConstant.APP_SHARE_ID, true);
            setEnabled(15, true);
            Sheet currentSheet = spreadsheet.getSheetView().getCurrentSheet();
            if (currentSheet.getActiveCellType() != 0 || currentSheet.getActiveCell() == null) {
                setEnabled(EventConstant.FILE_COPY_ID, false);
                setEnabled(EventConstant.APP_HYPERLINK, false);
                setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, false);
            } else {
                String formatContents = ModelUtil.instance().getFormatContents(currentSheet.getWorkbook(), currentSheet.getActiveCell());
                setEnabled(EventConstant.FILE_COPY_ID, formatContents != null && !formatContents.isEmpty());
                setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, formatContents != null && !formatContents.isEmpty());
                if (currentSheet.getActiveCell().getHyperLink() == null || currentSheet.getActiveCell().getHyperLink().getAddress() == null) {
                    z = false;
                }
                setEnabled(EventConstant.APP_HYPERLINK, z);
            }
            postInvalidate();
        }
    }
}
