package tunanh.documentation.xs.pdf;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.widget.Toast;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.constant.PDFConstant;
import tunanh.documentation.xs.simpletext.control.SafeAsyncTask;
import tunanh.documentation.xs.system.IFind;
import tunanh.documentation.xs.system.beans.pagelist.APageListItem;


public class PDFFind implements IFind {
    private boolean isCancel;
    private boolean isSetPointToVisible;
    private boolean isStartSearch;
    private int pageIndex;
    protected Paint paint;
    private PDFView pdfView;
    private String query;
    private SafeAsyncTask safeSearchTask;
    private RectF[] searchResult;
    protected Toast toast;

    static int access$212(PDFFind pDFFind, int i) {
        int i2 = pDFFind.pageIndex + i;
        pDFFind.pageIndex = i2;
        return i2;
    }

    public PDFFind(PDFView pDFView) {
        this.pdfView = pDFView;
        this.toast = Toast.makeText(pDFView.getContext(), "", Toast.LENGTH_SHORT);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setColor(PDFConstant.HIGHLIGHT_COLOR);
    }

    @Override
    public boolean find(String str) {
        if (str == null) {
            return false;
        }
        this.isStartSearch = true;
        this.query = str;
        this.pageIndex = this.pdfView.getCurrentPageNumber() - 1;
        search(1);
        return true;
    }

    @Override
    public boolean findBackward() {
        if (this.query == null) {
            return false;
        }
        this.isStartSearch = false;
        int i = this.pageIndex;
        if (i == 0) {
            this.toast.setText(this.pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_BEGIN"));
            this.toast.show();
            return false;
        }
        this.pageIndex = i - 1;
        search(-1);
        return true;
    }

    @Override // com.office.system.IFind
    public boolean findForward() {
        if (this.query == null) {
            return false;
        }
        this.isStartSearch = false;
        if (this.pageIndex + 1 >= this.pdfView.getPageCount()) {
            this.toast.setText(this.pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_END"));
            this.toast.show();
            return false;
        }
        this.pageIndex++;
        search(1);
        return true;
    }

    private void search(final int i) {
        SafeAsyncTask safeAsyncTask = this.safeSearchTask;
        if (safeAsyncTask != null) {
            safeAsyncTask.cancel(true);
            this.safeSearchTask = null;
        }
        this.isSetPointToVisible = false;
        this.searchResult = null;
        this.isCancel = false;
        int pageCount = i > 0 ? this.pdfView.getPageCount() - this.pageIndex : this.pageIndex;
        final boolean isShowFindDlg = this.pdfView.getControl().getMainFrame().isShowFindDlg();
        final ProgressDialog progressDialog = new ProgressDialog(this.pdfView.getControl().getActivity());
        progressDialog.setProgressStyle(1);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(this.pdfView.getControl().getMainFrame().getLocalString("DIALOG_PDF_SEARCHING"));
        progressDialog.setMax(pageCount);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                if (i2 == 4) {
                    PDFFind.this.isCancel = true;
                    if (PDFFind.this.safeSearchTask != null) {
                        PDFFind.this.safeSearchTask.cancel(true);
                        PDFFind.this.safeSearchTask = null;
                    }
                }
                return true;
            }
        });
        SafeAsyncTask<Void, Integer, RectF[]> safeAsyncTask2 = new SafeAsyncTask<Void, Integer, RectF[]>() {
            public RectF[] doInBackground(Void... voidArr) {
                int i2 = 1;
                while (PDFFind.this.pageIndex >= 0 && PDFFind.this.pageIndex < PDFFind.this.pdfView.getPageCount() && !isCancelled()) {
                    try {
                        i2++;
                        publishProgress(Integer.valueOf(i2));
                        RectF[] searchContentSync = PDFFind.this.pdfView.getPDFLib().searchContentSync(PDFFind.this.pageIndex, PDFFind.this.query);
                        if (searchContentSync != null && searchContentSync.length > 0) {
                            return searchContentSync;
                        }
                        PDFFind.access$212(PDFFind.this, i);
                    } catch (Exception unused) {
                    }
                }
                return null;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if (isShowFindDlg) {
                    progressDialog.cancel();
                    return;
                }
                ICustomDialog customDialog = PDFFind.this.pdfView.getControl().getCustomDialog();
                if (customDialog != null) {
                    customDialog.dismissDialog((byte) 4);
                }
            }


            public void onProgressUpdate(Integer... numArr) {
                super.onProgressUpdate(numArr);
                if (isShowFindDlg) {
                    progressDialog.setProgress(numArr[0].intValue());
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (isShowFindDlg) {
                    PDFFind.this.pdfView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!PDFFind.this.isCancel) {
                                progressDialog.show();
                                progressDialog.setProgress(1);
                            }
                        }
                    }, 0L);
                    return;
                }
                ICustomDialog customDialog = PDFFind.this.pdfView.getControl().getCustomDialog();
                if (customDialog != null) {
                    customDialog.showDialog((byte) 4);
                }
            }

            public void onPostExecute(RectF[] rectFArr) {
                String str;
                if (isShowFindDlg) {
                    progressDialog.cancel();
                } else {
                    ICustomDialog customDialog = PDFFind.this.pdfView.getControl().getCustomDialog();
                    if (customDialog != null) {
                        customDialog.dismissDialog((byte) 2);
                    }
                }
                if (rectFArr != null) {
                    PDFFind.this.searchResult = rectFArr;
                    if (PDFFind.this.pdfView.getCurrentPageNumber() - 1 != PDFFind.this.pageIndex) {
                        PDFFind.this.pdfView.getListView().showPDFPageForIndex(PDFFind.this.pageIndex);
                        PDFFind.this.isSetPointToVisible = true;
                    } else if (PDFFind.this.pdfView.getListView().isPointVisibleOnScreen((int) PDFFind.this.searchResult[0].left, (int) PDFFind.this.searchResult[0].top)) {
                        PDFFind.this.pdfView.invalidate();
                    } else {
                        PDFFind.this.pdfView.getListView().setItemPointVisibleOnScreen((int) PDFFind.this.searchResult[0].left, (int) PDFFind.this.searchResult[0].top);
                    }
                } else if (isShowFindDlg) {
                    if (PDFFind.this.isStartSearch) {
                        PDFFind.this.pdfView.getControl().getMainFrame().setFindBackForwardState(false);
                        str = PDFFind.this.pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_NOT_FOUND");
                    } else {
                        int i2 = i;
                        str = i2 > 0 ? PDFFind.this.pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_END") : i2 < 0 ? PDFFind.this.pdfView.getControl().getMainFrame().getLocalString("DIALOG_FIND_TO_BEGIN") : "";
                    }
                    if (str != null && !str.isEmpty()) {
                        PDFFind.this.toast.setText(str);
                        PDFFind.this.toast.show();
                    }
                }
            }
        };
        this.safeSearchTask = safeAsyncTask2;
        safeAsyncTask2.safeExecute(null, null);
    }

    public void drawHighlight(Canvas canvas, int i, int i2, APageListItem aPageListItem) {
        if (this.pageIndex == aPageListItem.getPageIndex()) {
            float width = (float) aPageListItem.getWidth() / aPageListItem.getPageWidth();
            RectF[] rectFArr = this.searchResult;
            if (rectFArr != null) {
                for (RectF rectF : rectFArr) {
                    float f = i * width;
                    float f2 = i2 * width;
                    canvas.drawRect((rectF.left * width) + f, (rectF.top * width) + f2, (rectF.right * width) + f, (rectF.bottom * width) + f2, this.paint);
                }
            }
        }
    }

    public RectF[] getSearchResult() {
        return this.searchResult;
    }

    @Override
    public void resetSearchResult() {
        this.searchResult = null;
    }

    @Override
    public int getPageIndex() {
        return this.pageIndex;
    }

    public boolean isSetPointToVisible() {
        return this.isSetPointToVisible;
    }

    public void setSetPointToVisible(boolean z) {
        this.isSetPointToVisible = z;
    }

    @Override
    public void dispose() {
        this.pdfView = null;
        this.toast = null;
    }
}