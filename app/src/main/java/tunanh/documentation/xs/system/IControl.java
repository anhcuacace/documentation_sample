
package tunanh.documentation.xs.system;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.common.ISlideShow;

 public interface IControl {
    void actionEvent(int i, Object obj);

    void dispose();

    Object getActionValue(int i, Object obj);

    Activity getActivity();

    byte getApplicationType();

    int getCurrentViewIndex();

    ICustomDialog getCustomDialog();

    Dialog getDialog(Activity activity, int i);

    IFind getFind();

    IMainFrame getMainFrame();

    IOfficeToPicture getOfficeToPicture();

    IReader getReader();

    ISlideShow getSlideShow();

    SysKit getSysKit();

    View getView();

    boolean isAutoTest();

    boolean isSlideShow();

    void layoutView(int i, int i2, int i3, int i4);

    boolean openFile(String str);
}