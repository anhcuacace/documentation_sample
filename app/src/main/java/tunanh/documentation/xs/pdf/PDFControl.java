package tunanh.documentation.xs.pdf;


import android.app.Activity;
import android.view.View;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.fc.pdf.PDFLib;
import tunanh.documentation.xs.system.AbstractControl;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.IFind;
import tunanh.documentation.xs.system.IMainFrame;
import tunanh.documentation.xs.system.SysKit;


public class PDFControl extends AbstractControl {
    private boolean isDispose;
    private IControl mainControl;
    private PDFView pdfView;

    @Override
    public byte getApplicationType() {
        return (byte) 3;
    }

    public PDFControl(IControl iControl, PDFLib pDFLib, String str) {
        this.mainControl = iControl;
        this.pdfView = new PDFView(iControl.getMainFrame().getActivity().getApplicationContext(), pDFLib, this);
    }

    @Override
    public void actionEvent(int i, final Object obj) {
        switch (i) {
            case 19:
                this.pdfView.init();
                return;
            case 20:
                this.pdfView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!PDFControl.this.isDispose) {
                            PDFControl.this.getMainFrame().updateToolsbarStatus();
                        }
                    }
                });
                return;
            case 22:
                if (isAutoTest()) {
                    getMainFrame().getActivity().onBackPressed();
                    return;
                }
                return;
            case 26:
                this.pdfView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!PDFControl.this.isDispose) {
                            PDFControl.this.mainControl.getMainFrame().showProgressBar(((Boolean) obj).booleanValue());
                        }
                    }
                });
                return;
            case EventConstant.APP_ZOOM_ID /* 536870917 */:
                int[] iArr = (int[]) obj;
                this.pdfView.setZoom(iArr[0] / 10000.0f, iArr[1], iArr[2]);
                return;
            case EventConstant.APP_PAGE_UP_ID /* 536870925 */:
                this.pdfView.previousPageview();
                return;
            case EventConstant.APP_PAGE_DOWN_ID /* 536870926 */:
                this.pdfView.nextPageView();
                return;
            case EventConstant.APP_PASSWORD_OK_INIT /* 536870930 */:
                this.pdfView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!PDFControl.this.isDispose) {
                            PDFControl.this.pdfView.passwordVerified();
                        }
                    }
                });
                return;
            case EventConstant.APP_SET_FIT_SIZE_ID /* 536870933 */:
                this.pdfView.setFitSize(((Integer) obj).intValue());
                return;
            case EventConstant.APP_INIT_CALLOUTVIEW_ID /* 536870942 */:
                this.pdfView.getListView().getCurrentPageView().initCalloutView();
                return;
            case EventConstant.PDF_SHOW_PAGE /* 1610612736 */:
                int intValue = ((Integer) obj).intValue();
                if (intValue >= 0 && intValue < this.pdfView.getPageCount()) {
                    this.pdfView.showPDFPageForIndex(intValue);
                    return;
                }
                return;
            default:
        }
    }


    @Override
    public Object getActionValue(int r14, Object r15) {

        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public View getView() {
        return this.pdfView;
    }

    @Override
    public IMainFrame getMainFrame() {
        return this.mainControl.getMainFrame();
    }

    @Override
    public Activity getActivity() {
        return getMainFrame().getActivity();
    }

    @Override
    public IFind getFind() {
        return this.pdfView.getFind();
    }

    @Override
    public boolean isAutoTest() {
        return this.mainControl.isAutoTest();
    }

    @Override
    public IOfficeToPicture getOfficeToPicture() {
        return this.mainControl.getOfficeToPicture();
    }

    @Override
    public ICustomDialog getCustomDialog() {
        return this.mainControl.getCustomDialog();
    }

    @Override
    public SysKit getSysKit() {
        return this.mainControl.getSysKit();
    }

    @Override
    public void dispose() {
        this.isDispose = true;
        this.pdfView.dispose();
        this.pdfView = null;
        this.mainControl = null;
    }
}