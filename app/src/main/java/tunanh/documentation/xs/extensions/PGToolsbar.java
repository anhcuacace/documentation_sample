package tunanh.documentation.xs.extensions;

import android.content.Context;
import android.util.AttributeSet;

import tunanh.documentation.R;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.pg.control.Presentation;
import tunanh.documentation.xs.system.IControl;

public class PGToolsbar extends AToolsbar {
    public PGToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PGToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.pg_slideshow, EventConstant.PG_SLIDESHOW_GEGIN, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, 536870912, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.pg_toolsbar_note, EventConstant.PG_NOTE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void updateStatus() {
        Presentation presentation = (Presentation) this.control.getView();
        setEnabled(EventConstant.PG_NOTE_ID, presentation.getCurrentSlide() != null && presentation.getCurrentSlide().getNotes() != null);
        postInvalidate();
    }
}