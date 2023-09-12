
package tunanh.documentation.xs.system;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.itextpdf.text.pdf.PdfBoolean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.common.ISlideShow;
import tunanh.documentation.xs.common.picture.PictureKit;
import tunanh.documentation.xs.constant.MainConstant;
import tunanh.documentation.xs.fc.EncryptedDocumentException;
import tunanh.documentation.xs.fc.doc.TXTKit;
import tunanh.documentation.xs.fc.pdf.PDFLib;
import tunanh.documentation.xs.pdf.PDFControl;
import tunanh.documentation.xs.pg.control.PGControl;
import tunanh.documentation.xs.pg.model.PGModel;
import tunanh.documentation.xs.res.ResKit;
import tunanh.documentation.xs.simpletext.model.IDocument;
import tunanh.documentation.xs.ss.control.SSControl;
import tunanh.documentation.xs.ss.model.baseModel.Workbook;
import tunanh.documentation.xs.wp.control.WPControl;


public class MainControl extends AbstractControl {
    public static String filePathForOpen = "";
    private IControl appControl;
    private ICustomDialog customDialog;
    public boolean defalutProgressDismiss;
    private String filePath;
    private IMainFrame frame;
    private Handler handler;
    private boolean isAutoTest;
    private boolean isCancel;
    private boolean isDispose;
    private IOfficeToPicture officeToPicture;
    private DialogInterface.OnKeyListener onKeyListener;
    private ProgressDialog progressDialog;
    private IReader reader;
    private ISlideShow slideShow;
    private Toast toast;
    private AUncaughtExceptionHandler uncaught;
    private byte applicationType = -1;
    public SysKit sysKit = new SysKit(this);

    @Override
    public Dialog getDialog(Activity activity, int i) {
        return null;
    }

    @Override
    public void layoutView(int i, int i2, int i3, int i4) {
    }

    public MainControl(IMainFrame iMainFrame, boolean z) {
        this.defalutProgressDismiss = true;
        this.frame = iMainFrame;
        AUncaughtExceptionHandler aUncaughtExceptionHandler = new AUncaughtExceptionHandler(this);
        this.uncaught = aUncaughtExceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(aUncaughtExceptionHandler);
        this.defalutProgressDismiss = z;
        init();
    }

    private void init() {
        initListener();
        String stringExtra = getActivity().getIntent().getStringExtra("autoTest");
        this.isAutoTest = stringExtra != null && stringExtra.equals(PdfBoolean.TRUE);
    }

    private void initListener() {
        this.onKeyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                dialogInterface.dismiss();
                MainControl.this.isCancel = true;
                if (MainControl.this.reader != null) {
                    MainControl.this.reader.abortReader();
                    MainControl.this.reader.dispose();
                }
                MainControl.this.getActivity().onBackPressed();
                return true;
            }
        };
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(final Message message) {
                if (!MainControl.this.isCancel) {
                    int i = message.what;
                    if (i == 0) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MainControl.this.createApplication(message.obj);
                                } catch (Exception e) {
                                    MainControl.this.sysKit.getErrorKit().writerLog(e, true);
                                    Log.e("TAG==", "Exception=" + e.getMessage());
                                }
                            }
                        });
                    } else if (i == 1) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                MainControl.this.dismissProgressDialog();
                            }
                        });
                    } else if (i != 2) {
                        if (i == 3) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    MainControl.this.dismissProgressDialog();
                                }
                            });
                        } else if (i == 4) {
                            MainControl.this.reader = (IReader) message.obj;
                        }
                    } else if (MainControl.this.getMainFrame().isShowProgressBar()) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                MainControl.this.progressDialog = ProgressDialog.show(MainControl.this.getActivity(), MainControl.this.frame.getLocalString("DIALOG_WAITING"), MainControl.this.frame.getLocalString("DIALOG_LOADING"), false, false, null);
                                MainControl.this.progressDialog.setOnKeyListener(MainControl.this.onKeyListener);
                            }
                        });
                    } else if (MainControl.this.customDialog != null) {
                        MainControl.this.customDialog.showDialog((byte) 2);
                    }
                }
            }
        };
    }

    public void dismissProgressDialog() {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.progressDialog = null;
        }
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

     public void createApplication(Object obj) throws Exception {
        Object viewBackground;
        if (obj != null) {
            byte b = this.applicationType;
            if (b == 0) {
                this.appControl = new WPControl(this, (IDocument) obj, this.filePath);
            } else if (b == 1) {
                this.appControl = new SSControl(this, (Workbook) obj, this.filePath);
                this.defalutProgressDismiss = true;
            } else if (b == 2) {
                this.appControl = new PGControl(this, (PGModel) obj, this.filePath);
                this.defalutProgressDismiss = true;
            } else if (b == 3) {
                this.appControl = new PDFControl(this, (PDFLib) obj, this.filePath);
                this.defalutProgressDismiss = true;
            }
            View view = this.appControl.getView();
            if (!(view == null || (viewBackground = this.frame.getViewBackground()) == null)) {
                if (viewBackground instanceof Integer) {
                    view.setBackgroundColor(((Integer) viewBackground).intValue());
                } else if (viewBackground instanceof Drawable) {
                    view.setBackgroundDrawable((Drawable) viewBackground);
                }
            }
            if (this.applicationType != 3) {
                this.frame.openFileFinish();
            }
            if (!getMainFrame().isShowProgressBar()) {
                ICustomDialog iCustomDialog = this.customDialog;
                if (iCustomDialog != null) {
                    iCustomDialog.dismissDialog((byte) 2);
                }
            } else if (this.defalutProgressDismiss) {
                dismissProgressDialog();
            }
            PictureKit.instance().setDrawPictrue(true);
            this.handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        View view2 = MainControl.this.getView();
                        Class[] clsArr = null;
                        Object[] objArr = null;
                        Object invoke = view2.getClass().getMethod("isHardwareAccelerated", null).invoke(view2, null);
                        if ((invoke instanceof Boolean) && ((Boolean) invoke).booleanValue()) {
                            view2.getClass().getMethod("setLayerType", Integer.TYPE, Paint.class).invoke(view2, Integer.valueOf(view2.getClass().getField("LAYER_TYPE_SOFTWARE").getInt(null)), null);
                        }
                        MainControl.this.actionEvent(26, Boolean.FALSE);
                        MainControl.this.actionEvent(19, null);
                        MainControl.this.frame.updateToolsbarStatus();
                        MainControl.this.getView().postInvalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Exception2==", e.getMessage());
                    }
                }
            });
            return;
        }
        throw new Exception("Document with password");
    }

    @Override
    public boolean openFile(final String str) {
        this.filePath = str;
        filePathForOpen = str;
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_DOC) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOCX) || lowerCase.endsWith(MainConstant.FILE_TYPE_TXT) || lowerCase.endsWith(MainConstant.FILE_TYPE_JSON) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_CSV)) {
            this.applicationType = (byte) 0;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM)) {
            this.applicationType = (byte) 1;
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)) {
            this.applicationType = (byte) 2;
        } else if (lowerCase.endsWith("pdf")) {
            this.applicationType = (byte) 3;
        } else {
            this.applicationType = (byte) 0;
        }
        boolean isSupport = FileKit.instance().isSupport(lowerCase);
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_TXT) || lowerCase.endsWith(MainConstant.FILE_TYPE_RTF) || !isSupport) {
            TXTKit.instance().readText(this, this.handler, str);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS)) {
            try {
                new FileInputStream(new File(str));
                new FileReaderThread(this, this.handler, str, null).ReadFile("");
            } catch (EncryptedDocumentException | FileNotFoundException unused) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getActivity());
                editText.setInputType(128);
                editText.setTransformationMethod(new PasswordTransformationMethod());
                editText.requestFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 1);
                AlertDialog create = builder.create();
                create.setTitle(ResKit.instance().getLocalString("DIALOG_ENTER_PASSWORD"));
                create.setView(editText);
                create.setButton(-1, ResKit.instance().getLocalString("BUTTON_OK"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new FileReaderThread(MainControl.this, MainControl.this.handler, str, null).ReadFile(editText.getText().toString());
                    }
                });
                create.setButton(-2, ResKit.instance().getLocalString("BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainControl.this.getActivity().onBackPressed();
                    }
                });
                create.show();
            }
        } else {
            new FileReaderThread(this, this.handler, str, null).ReadFile("");
        }
        return true;
    }

    @Override
    public void actionEvent(int i, Object obj) {
        if (i == 23 && this.reader != null) {
            IControl iControl = this.appControl;
            if (iControl != null) {
                iControl.actionEvent(i, obj);
            }
            this.reader.dispose();
            this.reader = null;
        }
        IMainFrame iMainFrame = this.frame;
        if (iMainFrame != null && !iMainFrame.doActionEvent(i, obj)) {
            if (i == -268435456) {
                getView().postInvalidate();
            } else if (i == 0) {
                try {
                    Message message = new Message();
                    message.obj = obj;
                    this.reader.dispose();
                    message.what = 0;
                    this.handler.handleMessage(message);
                } catch (Throwable th) {
                    this.sysKit.getErrorKit().writerLog(th);
                }
            } else if (i == 26) {
                Handler handler = this.handler;
                if (handler != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            boolean unused = MainControl.this.isDispose;
                        }
                    });
                }
            } else if (i == 536870919) {
                this.appControl.actionEvent(i, obj);
                this.frame.updateToolsbarStatus();
            } else if (i == 536870921) {
                IReader iReader = this.reader;
                if (iReader != null) {
                    iReader.abortReader();
                }
            } else if (i != 17) {
                if (i == 18) {
                    this.toast.cancel();
                } else if (i == 23) {
                    Handler handler2 = this.handler;
                    if (handler2 != null) {
                        handler2.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainControl.this.isDispose) {
                                    MainControl.this.frame.getActivity().setProgressBarIndeterminateVisibility(false);
                                    MainControl.this.frame.showProgressBar(false);
                                }
                            }
                        });
                    }
                } else if (i == 24) {
                    Handler handler3 = this.handler;
                    if (handler3 != null) {
                        handler3.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!MainControl.this.isDispose) {
                                    MainControl.this.frame.showProgressBar(true);
                                }
                            }
                        });
                    }
                } else if (i == 117440512) {
                    TXTKit.instance().reopenFile(this, this.handler, this.filePath, (String) obj);
                } else if (i != 117440513) {
                    IControl iControl2 = this.appControl;
                    if (iControl2 != null) {
                        iControl2.actionEvent(i, obj);
                    }
                } else {
                    String[] strArr = (String[]) obj;
                    if (strArr.length == 2) {
                        this.filePath = strArr[0];
                        this.applicationType = (byte) 0;
                        TXTKit.instance().reopenFile(this, this.handler, this.filePath, strArr[1]);
                    }
                }
            } else if (obj != null && (obj instanceof String)) {
                this.toast.setText((String) obj);
                this.toast.setGravity(17, 0, 0);
                this.toast.show();
            }
        }
    }

    @Override
    public IFind getFind() {
        return this.appControl.getFind();
    }

    @Override
    public Object getActionValue(int i, Object obj) {
        if (i == 1) {
            return this.filePath;
        }
        IControl iControl = this.appControl;
        if (iControl == null) {
            return null;
        }
        if (i != 536870928 && i != 805306371 && i != 805306377 && i != 536870931 && i != 1342177283 && i != 1073741830 && i != 1358954506) {
            return iControl.getActionValue(i, obj);
        }
        boolean isDrawPictrue = PictureKit.instance().isDrawPictrue();
        boolean isThumbnail = this.frame.isThumbnail();
        PictureKit.instance().setDrawPictrue(true);
        if (i == 536870928) {
            this.frame.setThumbnail(true);
        }
        Object actionValue = this.appControl.getActionValue(i, obj);
        if (i == 536870928) {
            this.frame.setThumbnail(isThumbnail);
        }
        PictureKit.instance().setDrawPictrue(isDrawPictrue);
        return actionValue;
    }

    @Override
    public View getView() {
        IControl iControl = this.appControl;
        if (iControl == null) {
            return null;
        }
        return iControl.getView();
    }

    @Override
    public boolean isAutoTest() {
        return this.isAutoTest;
    }

    @Override
    public IMainFrame getMainFrame() {
        return this.frame;
    }

    public IControl getAppControl() {
        return this.appControl;
    }

    @Override
    public Activity getActivity() {
        return this.frame.getActivity();
    }

    @Override
    public IOfficeToPicture getOfficeToPicture() {
        return this.officeToPicture;
    }

    @Override
    public ICustomDialog getCustomDialog() {
        return this.customDialog;
    }

    @Override
    public ISlideShow getSlideShow() {
        return this.slideShow;
    }

    @Override
    public IReader getReader() {
        return this.reader;
    }

    @Override
    public byte getApplicationType() {
        return this.applicationType;
    }

    public void setOffictToPicture(IOfficeToPicture iOfficeToPicture) {
        this.officeToPicture = iOfficeToPicture;
    }

    public void setCustomDialog(ICustomDialog iCustomDialog) {
        this.customDialog = iCustomDialog;
    }

    public void setSlideShow(ISlideShow iSlideShow) {
        this.slideShow = iSlideShow;
    }

    @Override
    public SysKit getSysKit() {
        return this.sysKit;
    }

    @Override
    public int getCurrentViewIndex() {
        return this.appControl.getCurrentViewIndex();
    }

    @Override
    public void dispose() {
        this.isDispose = true;
        IControl iControl = this.appControl;
        if (iControl != null) {
            iControl.dispose();
            this.appControl = null;
        }
        IReader iReader = this.reader;
        if (iReader != null) {
            iReader.dispose();
            this.reader = null;
        }
        IOfficeToPicture iOfficeToPicture = this.officeToPicture;
        if (iOfficeToPicture != null) {
            iOfficeToPicture.dispose();
            this.officeToPicture = null;
        }
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.progressDialog = null;
        }
        if (this.customDialog != null) {
            this.customDialog = null;
        }
        if (this.slideShow != null) {
            this.slideShow = null;
        }
        this.frame = null;
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.handler = null;
        }
        AUncaughtExceptionHandler aUncaughtExceptionHandler = this.uncaught;
        if (aUncaughtExceptionHandler != null) {
            aUncaughtExceptionHandler.dispose();
            this.uncaught = null;
        }
        this.onKeyListener = null;
        this.toast = null;
        this.filePath = null;
        System.gc();
        SysKit sysKit = this.sysKit;
        if (sysKit != null) {
            sysKit.dispose();
        }
    }
}