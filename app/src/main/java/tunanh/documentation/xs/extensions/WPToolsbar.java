package tunanh.documentation.xs.extensions;

import android.content.Context;
import android.view.View;

import tunanh.documentation.R;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.officereader.beans.AImageButton;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.wp.control.Word;

public class WPToolsbar extends AToolsbar {
    @Override
    public void dispose() {
    }

    public WPToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_copy, EventConstant.FILE_COPY_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, 536870912, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.wp_toolsbar_switch_view, EventConstant.WP_SWITCH_VIEW, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.wp_toolsbar_print_mode, EventConstant.WP_PRINT_MODE, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    public void onClick(View view) {
        if (view instanceof AImageButton) {
            this.control.actionEvent(((AImageButton) view).getActionID(), null);
        }
    }

    @Override
    public void updateStatus() {
        Word word = (Word) this.control.getView();
        setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, word.getHighlight().isSelectText());
        setEnabled(EventConstant.FILE_COPY_ID, word.getHighlight().isSelectText());
        setEnabled(EventConstant.APP_DRAW_ID, word != null && word.getCurrentRootType() == 2);
    }
}