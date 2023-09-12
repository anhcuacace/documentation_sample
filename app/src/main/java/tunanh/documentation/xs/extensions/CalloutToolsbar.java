package tunanh.documentation.xs.extensions;


import android.content.Context;
import android.util.AttributeSet;

import tunanh.documentation.R;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.system.IControl;

public class CalloutToolsbar extends AToolsbar {
    public CalloutToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CalloutToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
        this.toolsbarFrame.setBackgroundResource(R.drawable.icon_folder);
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_back, EventConstant.APP_BACK_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_pen_check, R.string.app_toolsbar_pen, EventConstant.APP_PEN_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_eraser_check, R.string.app_toolsbar_eraser, EventConstant.APP_ERASER_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_color, EventConstant.APP_COLOR_ID, true);
    }
}