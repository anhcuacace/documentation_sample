/*
 * 文件名称:          TXTKit.java
 *
 * 编译器:            android2.2
 * 时间:              下午5:16:41
 */
package tunanh.documentation.xs.fc.doc;

import android.os.Handler;

import java.io.FileInputStream;
import java.util.Vector;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.fc.fs.storage.LittleEndian;
import tunanh.documentation.xs.system.FileReaderThread;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.IDialogAction;
import tunanh.documentation.xs.thirdpart.mozilla.intl.chardet.CharsetDetector;
import tunanh.documentation.xs.wp.dialog.TXTEncodingDialog;


public class TXTKit {
    private static final TXTKit kit = new TXTKit();

    public static TXTKit instance() {
        return kit;
    }

    public void readText(final IControl iControl, final Handler handler, final String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[16];
            fileInputStream.read(bArr);
            long j = LittleEndian.getLong(bArr, 0);
            if (j == -2226271756974174256L || j == 1688935826934608L) {
                fileInputStream.close();
                iControl.getSysKit().getErrorKit().writerLog(new Exception("Format error"), true);
            } else if ((j & 72057594037927935L) == 13001919450861605L) {
                fileInputStream.close();
                iControl.getSysKit().getErrorKit().writerLog(new Exception("Format error"), true);
            } else {
                fileInputStream.close();
                String detect = iControl.isAutoTest() ? "GBK" : CharsetDetector.detect(str);
                if (detect != null) {
                    new FileReaderThread(iControl, handler, str, detect).ReadFile("");
                } else if (iControl.getMainFrame().isShowTXTEncodeDlg()) {
                    Vector vector = new Vector();
                    vector.add(str);
                    new TXTEncodingDialog(iControl, iControl.getMainFrame().getActivity(), new IDialogAction() {
                         @Override
                         public void dispose() {
                        }

                        @Override // com.office.system.IDialogAction
                        public IControl getControl() {
                            return iControl;
                        }

                        @Override // com.office.system.IDialogAction
                        public void doAction(int i, Vector<Object> vector2) {
                            if (TXTEncodingDialog.BACK_PRESSED.equals(vector2.get(0))) {
                                iControl.getMainFrame().getActivity().onBackPressed();
                            } else {
                                new FileReaderThread(iControl, handler, str, vector2.get(0).toString()).ReadFile("");
                            }
                        }
                    }, vector, 1).show();
                } else {
                    String tXTDefaultEncode = iControl.getMainFrame().getTXTDefaultEncode();
                    if (tXTDefaultEncode == null) {
                        ICustomDialog customDialog = iControl.getCustomDialog();
                        if (customDialog != null) {
                            customDialog.showDialog((byte) 1);
                        } else {
                            new FileReaderThread(iControl, handler, str, "UTF-8").ReadFile("");
                        }
                    } else {
                        new FileReaderThread(iControl, handler, str, tXTDefaultEncode).ReadFile("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reopenFile(IControl iControl, Handler handler, String str, String str2) {
        new FileReaderThread(iControl, handler, str, str2).ReadFile("");
    }
}