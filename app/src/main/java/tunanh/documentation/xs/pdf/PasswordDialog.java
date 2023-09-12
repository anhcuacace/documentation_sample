package tunanh.documentation.xs.pdf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.widget.EditText;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.fc.pdf.PDFLib;
import tunanh.documentation.xs.res.ResKit;
import tunanh.documentation.xs.system.IControl;

public class PasswordDialog {
    private AlertDialog.Builder alertBuilder;
    private final IControl control;
    private final PDFLib lib;
    private EditText pwView;

    public PasswordDialog(IControl iControl, PDFLib pDFLib) {
        this.control = iControl;
        this.lib = pDFLib;
    }

    public void show() {
        if (this.control.getMainFrame().isShowPasswordDlg()) {
            this.alertBuilder = new AlertDialog.Builder(this.control.getActivity());
            requestPassword(this.lib);
            return;
        }
        ICustomDialog customDialog = this.control.getCustomDialog();
        if (customDialog != null) {
            customDialog.showDialog((byte) 0);
        }
    }

    public void requestPassword(final PDFLib pDFLib) {
        EditText editText = new EditText(this.control.getActivity());
        this.pwView = editText;
        editText.setInputType(128);
        this.pwView.setTransformationMethod(new PasswordTransformationMethod());
        AlertDialog create = this.alertBuilder.create();
        create.setTitle(ResKit.instance().getLocalString("DIALOG_ENTER_PASSWORD"));
        create.setView(this.pwView);
        create.setButton(-1, ResKit.instance().getLocalString("BUTTON_OK"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (pDFLib.authenticatePasswordSync(PasswordDialog.this.pwView.getText().toString())) {
                    PasswordDialog.this.control.actionEvent(EventConstant.APP_PASSWORD_OK_INIT, null);
                } else {
                    PasswordDialog.this.requestPassword(pDFLib);
                }
            }
        });
        create.setButton(-2, ResKit.instance().getLocalString("BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PasswordDialog.this.control.getActivity().onBackPressed();
            }
        });
        create.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                dialogInterface.dismiss();
                PasswordDialog.this.control.getActivity().onBackPressed();
                return true;
            }
        });
        create.show();
    }
}