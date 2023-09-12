package tunanh.documentation.xs.extensions;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import tunanh.documentation.R;
import tunanh.documentation.xs.officereader.beans.AImageButton;
import tunanh.documentation.xs.officereader.beans.AImageCheckButton;
import tunanh.documentation.xs.system.IControl;

 public class AToolsbar extends HorizontalScrollView {
    public Map<Integer, Integer> actionButtonIndex;
    public boolean animation;
    public int buttonHeight;
    public int buttonWidth;
    public IControl control;
    public LinearLayout toolsbarFrame;
    public int toolsbarWidth;

    public void updateStatus() {
    }

    public AToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AToolsbar(Context context, IControl iControl) {
        super(context);
        this.control = iControl;
        setAnimation(true);
        setVerticalFadingEdgeEnabled(false);
        setFadingEdgeLength(0);
        LinearLayout linearLayout = new LinearLayout(context);
        this.toolsbarFrame = linearLayout;
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.toolsbarFrame.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.sys_toolsbar_button_bg_normal, options);
        this.buttonWidth = options.outWidth;
        this.buttonHeight = options.outHeight;
        this.toolsbarFrame.setBackgroundResource(R.drawable.sys_toolsbar_button_bg_normal);
        addView(this.toolsbarFrame, new LayoutParams(-1, this.buttonHeight));
    }

    public AImageButton addButton(int i, int i2, int i3, int i4, boolean z) {
        Context context = getContext();
        AImageButton aImageButton = new AImageButton(context, this.control, context.getResources().getString(i3), i, i2, i4);
        aImageButton.setNormalBgResID(R.drawable.sys_toolsbar_button_bg_normal);
        aImageButton.setPushBgResID(R.drawable.sys_toolsbar_button_bg_push);
        aImageButton.setLayoutParams(new LayoutParams(this.buttonWidth, this.buttonHeight));
        this.toolsbarFrame.addView(aImageButton);
        this.toolsbarWidth += this.buttonWidth;
        if (this.actionButtonIndex == null) {
            this.actionButtonIndex = new HashMap();
        }
        this.actionButtonIndex.put(i4, this.toolsbarFrame.getChildCount() - 1);
        if (z) {
            addSeparated();
        }
        return aImageButton;
    }

    public AImageCheckButton addCheckButton(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
        Context context = getContext();
        Resources resources = context.getResources();
        AImageCheckButton aImageCheckButton = new AImageCheckButton(context, this.control, resources.getString(i4), resources.getString(i5), i, i2, i3, i6);
        aImageCheckButton.setNormalBgResID(R.drawable.sys_toolsbar_button_bg_normal);
        aImageCheckButton.setPushBgResID(R.drawable.sys_toolsbar_button_bg_push);
        aImageCheckButton.setLayoutParams(new LayoutParams(this.buttonWidth, this.buttonHeight));
        this.toolsbarFrame.addView(aImageCheckButton);
        this.toolsbarWidth += this.buttonWidth;
        if (this.actionButtonIndex == null) {
            this.actionButtonIndex = new HashMap();
        }
        this.actionButtonIndex.put(i6, this.toolsbarFrame.getChildCount() - 1);
        if (z) {
            addSeparated();
        }
        return aImageCheckButton;
    }

    public void addSeparated() {
        this.toolsbarFrame.addView(new AImageButton(getContext(), this.control, "", R.drawable.ss_sheetbar_separated_horizontal, -1, -1), new LayoutParams(1, this.buttonHeight));
        this.toolsbarWidth++;
    }

    public void dispose() {
        this.control = null;
        Map<Integer, Integer> map = this.actionButtonIndex;
        if (map != null) {
            map.clear();
            this.actionButtonIndex = null;
        }
        int childCount = this.toolsbarFrame.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.toolsbarFrame.getChildAt(i);
            if (childAt instanceof AImageButton) {
                ((AImageButton) childAt).dispose();
            }
        }
        this.toolsbarFrame = null;
    }

    public int getButtonHeight() {
        return this.buttonHeight;
    }

    public int getButtonWidth() {
        return this.buttonWidth;
    }

    public int getToolsbarWidth() {
        return this.toolsbarWidth;
    }

    public boolean isAnimation() {
        return this.animation;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        this.toolsbarFrame.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isAnimation()) {
            setAnimation(false);
            if (this.toolsbarFrame.getWidth() > getResources().getDisplayMetrics().widthPixels) {
                scrollTo(this.buttonWidth * 3, 0);
            }
            fling(-4000);
        }
        super.onDraw(canvas);
    }

    public void setAnimation(boolean z) {
        this.animation = z;
    }

    public void setButtonHeight(int i) {
        this.buttonHeight = i;
    }

    public void setButtonWidth(int i) {
        this.buttonWidth = i;
    }

    public void setCheckState(int i, short s) {
        int intValue = this.actionButtonIndex.get(Integer.valueOf(i)).intValue();
        if (intValue >= 0 && intValue < this.toolsbarFrame.getChildCount() && (this.toolsbarFrame.getChildAt(intValue) instanceof AImageCheckButton)) {
            ((AImageCheckButton) this.toolsbarFrame.getChildAt(intValue)).setState(s);
        }
    }

    public void setEnabled(int i, boolean z) {
        Integer num = this.actionButtonIndex.get(i);
        if (num != null && num.intValue() >= 0 && num.intValue() < this.toolsbarFrame.getChildCount()) {
            this.toolsbarFrame.getChildAt(num).setEnabled(z);
        }
    }

    public void setToolsbarWidth(int i) {
        this.toolsbarWidth = i;
    }
}
