package tunanh.documentation.xs.extensions;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.res.ResKit;
import tunanh.documentation.xs.simpletext.control.IWord;
import tunanh.documentation.xs.ss.control.ExcelView;
import tunanh.documentation.xs.system.IMainFrame;
import tunanh.documentation.xs.system.MainControl;

public abstract class  IOffice implements IMainFrame {
    public static final String TAG = IOffice.class.getSimpleName();
    private MainControl control;
    public boolean isThumbnail;
    private String tempFilePath;
    private boolean writeLog = true;
    private final Object bg = -1;

    @Override
    public void changePage() {
    }

    @Override
    public void changeZoom() {
    }

    @Override
    public void completeLayout() {
    }

    @Override
    public abstract void error(int i);

    @Override
    public void fullScreen(boolean z) {
    }

    @Override
    public abstract Activity getActivity();

    @Override
    public abstract String getAppName();

    @Override
    public int getBottomBarHeight() {
        return 0;
    }

    @Override
    public byte getPageListViewMovingPosition() {
        return (byte) 0;
    }

    @Override
    public String getTXTDefaultEncode() {
        return "UTF-8";
    }

    @Override
    public abstract File getTemporaryDirectory();

    @Override
    public int getTopBarHeight() {
        return 0;
    }

    @Override
    public byte getWordDefaultView() {
        return (byte) 0;
    }

    @Override
    public boolean isChangePage() {
        return true;
    }

    @Override
    public boolean isDrawPageNumber() {
        return true;
    }

    @Override
    public boolean isIgnoreOriginalSize() {
        return false;
    }

    @Override
    public abstract boolean isPopUpErrorDlg();

    @Override
    public boolean isShowFindDlg() {
        return true;
    }

    @Override
    public boolean isShowPasswordDlg() {
        return true;
    }

    @Override
    public boolean isShowProgressBar() {
        return true;
    }

    @Override
    public boolean isShowTXTEncodeDlg() {
        return true;
    }

    @Override
    public boolean isShowZoomingMsg() {
        return true;
    }

    @Override
    public boolean isTouchZoom() {
        return true;
    }

    @Override
    public boolean isZoomAfterLayoutForWord() {
        return true;
    }

    @Override
    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return false;
    }

    @Override
    public void setFindBackForwardState(boolean z) {
    }

    @Override
    public void setIgnoreOriginalSize(boolean z) {
    }

    @Override
    public void updateToolsbarStatus() {
    }

    @Override
    public void updateViewImages(List<Integer> list) {
    }

    public IOffice() {
        initControl();
    }

    private void initControl() {
        MainControl mainControl = new MainControl(this, true);
        this.control = mainControl;
        mainControl.setOffictToPicture(new IOfficeToPicture() {
            private Bitmap bitmap;

            @Override
            public void dispose() {
            }

            @Override
            public byte getModeType() {
                return (byte) 1;
            }

            @Override
            public boolean isZoom() {
                return false;
            }

            @Override
            public void setModeType(byte b) {
            }

            @Override
            public Bitmap getBitmap(int i, int i2) {
                if (i == 0 || i2 == 0) {
                    return null;
                }
                Bitmap bitmap = this.bitmap;
                if (!(bitmap != null && bitmap.getWidth() == i && this.bitmap.getHeight() == i2)) {
                    Bitmap bitmap2 = this.bitmap;
                    if (bitmap2 != null) {
                        bitmap2.recycle();
                    }
                    this.bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                }
                return this.bitmap;
            }

            @Override
            public void callBack(Bitmap bitmap) {
                IOffice.this.saveBitmapToFile(bitmap);
            }
        });
    }

    private void saveBitmapToFile(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (this.tempFilePath == null) {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                this.tempFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            String stringBuilder = this.tempFilePath +
                    File.separatorChar +
                    "tempPic";
            File file = new File(stringBuilder);
            if (!file.exists()) {
                file.mkdir();
            }
            this.tempFilePath = file.getAbsolutePath();
        }
        String stringBuilder = this.tempFilePath +
                File.separatorChar +
                "export_image.jpg";
        File file = new File(stringBuilder);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException ex) {
            Log.e(TAG, "saveBitmapToFile: ", ex);

        }
    }

    public View getView() {
        MainControl mainControl = this.control;
        if (mainControl == null) {
            return null;
        }
        return mainControl.getView();
    }

    public ExcelView getExcelView() {
        return (ExcelView) this.control.getView();
    }

    public IWord getExcelEditor() {
        return getExcelView().getSpreadsheet().getEditor();
    }

    public void openFile(String str) {
        getControl().openFile(str);
    }

    @Override
    public boolean doActionEvent(int i, Object obj) {
        if (i != 20) {
            if (!(i == 1073741828 || i == 1358954496)) {
                switch (i) {
                    case EventConstant.APP_FINDING:
                        String trim = ((String) obj).trim();
                        if (!trim.isEmpty() && this.control.getFind().find(trim)) {
                            setFindBackForwardState(true);
                            break;
                        }
                        break;
                    case EventConstant.APP_FIND_BACKWARD:
                        this.control.getFind().findBackward();
                        break;
                    case EventConstant.APP_FIND_FORWARD:
                        this.control.getFind().findForward();
                        break;
                    default:
                        return false;
                }
            }
            return true;
        }
        updateToolsbarStatus();
        return true;
    }

    public MainControl getControl() {
        return this.control;
    }

    @Override
    public void showProgressBar(boolean z) {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    public void destroyEngine() {
        getActivity().onBackPressed();
    }

    @Override
    public String getLocalString(String str) {
        return ResKit.instance().getLocalString(str);
    }

    @Override
    public boolean isWriteLog() {
        return this.writeLog;
    }

    @Override
    public void setWriteLog(boolean z) {
        this.writeLog = z;
    }

    @Override
    public boolean isThumbnail() {
        return this.isThumbnail;
    }

    @Override
    public void setThumbnail(boolean z) {
        this.isThumbnail = z;
    }

    @Override
    public Object getViewBackground() {
        return this.bg;
    }

    @Override
    public void dispose() {
        MainControl mainControl = this.control;
        if (mainControl != null) {
            mainControl.dispose();
            this.control = null;
        }
    }
}