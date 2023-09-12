package tunanh.documentation.xs.pdf;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.fc.pdf.PDFLib;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.IFind;
import tunanh.documentation.xs.system.SysKit;
import tunanh.documentation.xs.system.beans.pagelist.APageListItem;
import tunanh.documentation.xs.system.beans.pagelist.APageListView;
import tunanh.documentation.xs.system.beans.pagelist.IPageListViewListener;

public class PDFView extends FrameLayout implements IPageListViewListener {
    private IControl control;
    private AsyncTask<Void, Object, Bitmap> exportTask;
    private PDFFind find;
    private APageListView listView;
    private Rect[] pagesSize;
    private Paint paint;
    private PDFLib pdfLib;
    private int preShowPageIndex;

    @Override
    public void setDrawPictrue(boolean z) {
    }

    public PDFView(Context context) {
        super(context);
        this.preShowPageIndex = -1;
    }

    public PDFView(Context context, PDFLib pDFLib, IControl iControl) {
        super(context);
        this.preShowPageIndex = -1;
        this.control = iControl;
        this.pdfLib = pDFLib;
        APageListView aPageListView = new APageListView(context, this);
        this.listView = aPageListView;
        addView(aPageListView, new LayoutParams(-1, -1));
        this.find = new PDFFind(this);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setTypeface(Typeface.SANS_SERIF);
        this.paint.setTextSize(24.0f);
        if (!pDFLib.hasPasswordSync()) {
            this.pagesSize = pDFLib.getAllPagesSize();
        }
    }

    @Override
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        APageListView aPageListView = this.listView;
        if (aPageListView != null) {
            aPageListView.setBackgroundColor(i);
        }
    }

    @Override
    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        APageListView aPageListView = this.listView;
        if (aPageListView != null) {
            aPageListView.setBackgroundResource(i);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        APageListView aPageListView = this.listView;
        if (aPageListView != null) {
            aPageListView.setBackgroundDrawable(drawable);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawPageNubmer(canvas);
    }

    public void init() {
        if (this.pdfLib.hasPasswordSync()) {
            new PasswordDialog(this.control, this.pdfLib).show();
        }
    }

    public void setZoom(float f, int i, int i2) {
        this.listView.setZoom(f, i, i2);
    }

    public void setFitSize(int i) {
        this.listView.setFitSize(i);
    }

    public int getFitSizeState() {
        return this.listView.getFitSizeState();
    }

    public float getZoom() {
        return this.listView.getZoom();
    }

    public float getFitZoom() {
        return this.listView.getFitZoom();
    }

    public int getCurrentPageNumber() {
        return this.listView.getCurrentPageNumber();
    }

    public IFind getFind() {
        return this.find;
    }

    public PDFLib getPDFLib() {
        return this.pdfLib;
    }

    public APageListView getListView() {
        return this.listView;
    }

    public void nextPageView() {
        this.listView.nextPageView();
    }

    public void previousPageview() {
        this.listView.previousPageview();
    }

    public void showPDFPageForIndex(int i) {
        this.listView.showPDFPageForIndex(i);
    }

    public Bitmap pageToImage(int i) {
        if (i <= 0 || i > getPageCount()) {
            return null;
        }
        int i2 = i - 1;
        Rect pageSize = getPageSize(i2);
        Bitmap createBitmap = Bitmap.createBitmap(pageSize.width(), pageSize.height(), Bitmap.Config.ARGB_8888);
        this.pdfLib.drawPageSync(createBitmap, i2, pageSize.width(), pageSize.height(), 0, 0, pageSize.width(), pageSize.height(), 1);
        return createBitmap;
    }

    public Bitmap getThumbnail(int i, float f) {
        Bitmap bitmap = null;
        if (i <= 0 || i > getPageCount()) {
            return null;
        }
        int i2 = i - 1;
        Rect pageSize = getPageSize(i2);
        int width = (int) (pageSize.width() * f);
        int height = (int) (pageSize.height() * f);
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.pdfLib.drawPageSync(bitmap, i2, width, height, 0, 0, width, height, 1);
            return bitmap;
        } catch (OutOfMemoryError e) {
            this.control.getSysKit().getErrorKit().writerLog(e);
            return bitmap;
        }
    }

    public Bitmap pageAreaToImage(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (i > 0 && i <= getPageCount()) {
            int i8 = i - 1;
            Rect pageSize = getPageSize(i8);
            if (!SysKit.isValidateRect(pageSize.width(), pageSize.height(), i2, i3, i4, i5)) {
                return null;
            }
            float f = i4;
            float f2 = i5;
            float min = Math.min(i6 / f, i7 / f2);
            int i9 = (int) (f * min);
            int i10 = (int) (f2 * min);
            try {
                Bitmap createBitmap = Bitmap.createBitmap(i9, i10, Bitmap.Config.ARGB_8888);
                if (createBitmap == null) {
                    return null;
                }
                this.pdfLib.drawPageSync(createBitmap, i8, (int) (pageSize.width() * min), (int) (pageSize.height() * min), (int) (i2 * min), (int) (i3 * min), i9, i10, 1);
                return createBitmap;
            } catch (OutOfMemoryError unused) {
            }
        }
        return null;
    }

    public void passwordVerified() {
        if (this.listView != null) {
            this.pagesSize = this.pdfLib.getAllPagesSize();
            this.control.getMainFrame().openFileFinish();
            this.listView.init();
        }
    }

    @Override
    public int getPageCount() {
        return this.pdfLib.getPageCountSync();
    }

    @Override
    public APageListItem getPageListItem(int i, View view, ViewGroup viewGroup) {
        Rect pageSize = getPageSize(i);
        return new PDFPageListItem(this.listView, this.control, pageSize.width(), pageSize.height());
    }

    @Override
    public Rect getPageSize(int i) {
        if (i < 0) {
            return null;
        }
        Rect[] rectArr = this.pagesSize;
        if (i >= rectArr.length) {
            return null;
        }
        return rectArr[i];
    }

    @Override
    public void exportImage(final APageListItem aPageListItem, final Bitmap bitmap) {
        if (getControl() != null && bitmap != null) {
            if (this.find.isSetPointToVisible()) {
                this.find.setSetPointToVisible(false);
                RectF[] searchResult = this.find.getSearchResult();
                if (searchResult != null && searchResult.length > 0 && !this.listView.isPointVisibleOnScreen((int) searchResult[0].left, (int) searchResult[0].top)) {
                    this.listView.setItemPointVisibleOnScreen((int) searchResult[0].left, (int) searchResult[0].top);
                    return;
                }
            }
            AsyncTask<Void, Object, Bitmap> asyncTask = this.exportTask;
            if (asyncTask != null) {
                asyncTask.cancel(true);
                this.exportTask = null;
            }
            this.exportTask = new AsyncTask<Void, Object, Bitmap>() {
                private boolean isCancal;

                @Override
                protected void onPreExecute() {
                }


                public Bitmap doInBackground(Void... voidArr) {
                    int min;
                    int min2;
                    Bitmap bitmap2;
                    int i;
                    int i2;
                    int i3;
                    int i4;
                    if (!(PDFView.this.control == null || PDFView.this.pdfLib == null)) {
                        try {
                            IOfficeToPicture officeToPicture = PDFView.this.control.getOfficeToPicture();
                            if (officeToPicture == null || officeToPicture.getModeType() != 1 || (bitmap2 = officeToPicture.getBitmap((min = Math.min(PDFView.this.getWidth(), bitmap.getWidth())), (min2 = Math.min(PDFView.this.getHeight(), bitmap.getHeight())))) == null) {
                                return null;
                            }
                            Canvas canvas = new Canvas(bitmap2);
                            int left = aPageListItem.getLeft();
                            int top = aPageListItem.getTop();
                            if (bitmap2.getWidth() == min && bitmap2.getHeight() == min2) {
                                if (bitmap.getWidth() == min && bitmap.getHeight() == min2) {
                                    i4 = 0;
                                    i3 = 0;
                                    canvas.drawBitmap(bitmap, i4, i3, PDFView.this.paint);
                                    canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                    PDFView.this.control.getSysKit().getCalloutManager().drawPath(canvas, aPageListItem.getPageIndex(), PDFView.this.getZoom());
                                }
                                i4 = Math.min(0, aPageListItem.getLeft());
                                i3 = Math.min(0, aPageListItem.getTop());
                                canvas.drawBitmap(bitmap, i4, i3, PDFView.this.paint);
                                canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                PDFView.this.control.getSysKit().getCalloutManager().drawPath(canvas, aPageListItem.getPageIndex(), PDFView.this.getZoom());
                            } else {
                                Matrix matrix = new Matrix();
                                float width = (float) bitmap2.getWidth() / min;
                                float height = (float) bitmap2.getHeight() / min2;
                                matrix.postScale(width, height);
                                if (((int) (PDFView.this.getZoom() * 1000000.0f)) == 1000000) {
                                    matrix.postTranslate(Math.min(aPageListItem.getLeft(), 0), Math.min(aPageListItem.getTop(), 0));
                                    i = Math.min(0, (int) (aPageListItem.getLeft() * width));
                                    i2 = Math.min(0, (int) (aPageListItem.getTop() * height));
                                } else {
                                    i2 = 0;
                                    i = 0;
                                }
                                try {
                                    Bitmap bitmap3 = bitmap;
                                    canvas.drawBitmap(Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap.getHeight(), matrix, true), i, i2, PDFView.this.paint);
                                } catch (OutOfMemoryError unused) {
                                    canvas.drawBitmap(bitmap, matrix, PDFView.this.paint);
                                }
                                canvas.translate(-(Math.max(left, 0) - left), -(Math.max(top, 0) - top));
                                PDFView.this.control.getSysKit().getCalloutManager().drawPath(canvas, aPageListItem.getPageIndex(), PDFView.this.getZoom());
                            }
                            return bitmap2;
                        } catch (Exception unused2) {
                        }
                    }
                    return null;
                }

                @Override
                protected void onCancelled() {
                    this.isCancal = true;
                }

                public void onPostExecute(Bitmap bitmap2) {
                    IOfficeToPicture officeToPicture;
                    if (bitmap2 != null) {
                        try {
                            if (PDFView.this.control != null && !this.isCancal && (officeToPicture = PDFView.this.control.getOfficeToPicture()) != null && officeToPicture.getModeType() == 1) {
                                officeToPicture.callBack(bitmap2);
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
            };
        }
    }

    public Bitmap getSanpshot(Bitmap bitmap) {
        APageListItem currentPageView;
        if (bitmap == null || (currentPageView = this.listView.getCurrentPageView()) == null) {
            return null;
        }
        int min = Math.min(getWidth(), currentPageView.getWidth());
        int min2 = Math.min(getHeight(), currentPageView.getHeight());
        float width = (float) bitmap.getWidth() / min;
        float height = (float) bitmap.getHeight() / min2;
        int left = (int) (currentPageView.getLeft() * width);
        int top = (int) (currentPageView.getTop() * height);
        int max = Math.max(left, 0) - left;
        int max2 = Math.max(top, 0) - top;
        float pageWidth = currentPageView.getPageWidth() * width * getZoom();
        this.pdfLib.drawPageSync(bitmap, currentPageView.getPageIndex(), pageWidth, currentPageView.getPageHeight() * height * getZoom(), max, max2, bitmap.getWidth(), bitmap.getHeight(), 1);
        if (max == 0 && pageWidth < bitmap.getWidth() && min == currentPageView.getWidth()) {
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(-16777216);
            new Canvas(bitmap).drawRect(bitmap.getWidth() - (bitmap.getWidth() - pageWidth), 0.0f, bitmap.getWidth(), bitmap.getHeight(), this.paint);
        }
        return bitmap;
    }

    @Override
    public boolean isInit() {
        return !this.pdfLib.hasPasswordSync();
    }

    @Override
    public boolean isIgnoreOriginalSize() {
        return this.control.getMainFrame().isIgnoreOriginalSize();
    }

    @Override
    public byte getPageListViewMovingPosition() {
        return this.control.getMainFrame().getPageListViewMovingPosition();
    }

    @Override
    public Object getModel() {
        return this.pdfLib;
    }

    public IControl getControl() {
        return this.control;
    }

    @Override
    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return this.control.getMainFrame().onEventMethod(view, motionEvent, motionEvent2, f, f2, b);
    }

    @Override
    public void updateStutus(Object obj) {
        this.control.actionEvent(20, obj);
    }

    @Override
    public void resetSearchResult(APageListItem aPageListItem) {
        if (this.find != null && aPageListItem.getPageIndex() != this.find.getPageIndex()) {
            this.find.resetSearchResult();
        }
    }

    @Override
    public boolean isTouchZoom() {
        return this.control.getMainFrame().isTouchZoom();
    }

    @Override
    public boolean isShowZoomingMsg() {
        return this.control.getMainFrame().isShowZoomingMsg();
    }

    @Override
    public void changeZoom() {
        this.control.getMainFrame().changeZoom();
    }

    @Override
    public boolean isChangePage() {
        return this.control.getMainFrame().isChangePage();
    }

    private void drawPageNubmer(Canvas canvas) {
        if (this.control.getMainFrame().isDrawPageNumber()) {
            String valueOf = this.listView.getCurrentPageNumber() + " / " + this.pdfLib.getPageCountSync();
            int measureText = (int) this.paint.measureText(valueOf);
            int descent = (int) (this.paint.descent() - this.paint.ascent());
            int width = (getWidth() - measureText) / 2;
            int height = (getHeight() - descent) + (-20);
            Drawable pageNubmerDrawable = SysKit.getPageNubmerDrawable();
            pageNubmerDrawable.setBounds(width - 10, height - 10, measureText + width + 10, descent + height + 10);
            pageNubmerDrawable.draw(canvas);
            canvas.drawText(valueOf, width, (int) (height - this.paint.ascent()), this.paint);
        }
        if (this.listView.isInit() && this.preShowPageIndex != this.listView.getCurrentPageNumber()) {
            this.control.getMainFrame().changePage();
            this.preShowPageIndex = this.listView.getCurrentPageNumber();
        }
    }

    public void dispose() {
        PDFFind pDFFind = this.find;
        if (pDFFind != null) {
            pDFFind.dispose();
        }
        PDFFind pDFFind2 = this.find;
        if (pDFFind2 != null) {
            pDFFind2.dispose();
            this.find = null;
        }
        PDFLib pDFLib = this.pdfLib;
        if (pDFLib != null) {
            pDFLib.setStopFlagSync(1);
            this.pdfLib = null;
        }
        APageListView aPageListView = this.listView;
        if (aPageListView != null) {
            aPageListView.dispose();
        }
        this.control = null;
    }
}