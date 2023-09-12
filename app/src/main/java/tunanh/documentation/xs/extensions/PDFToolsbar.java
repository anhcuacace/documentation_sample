package tunanh.documentation.xs.extensions;

import android.content.Context;
import android.view.View;

import tunanh.documentation.R;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.officereader.beans.AImageButton;
import tunanh.documentation.xs.system.IControl;

public class PDFToolsbar extends AToolsbar {
    @Override
    public void updateStatus() {
    }

    public PDFToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, 536870912, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void onClick(View view) {
        if (view instanceof AImageButton) {
            this.control.actionEvent(((AImageButton) view).getActionID(), null);
        }
    }
}
