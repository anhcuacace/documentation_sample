package tunanh.documentation.xs.pdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import tunanh.documentation.xs.fc.pdf.PDFLib;
import tunanh.documentation.xs.simpletext.control.SafeAsyncTask;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.beans.pagelist.APageListItem;
import tunanh.documentation.xs.system.beans.pagelist.APageListView;


public class PDFPageListItem extends APageListItem {
    private SafeAsyncTask<Void, Void, Bitmap> darwOriginalPageTask;
    private final boolean isAutoTest;
    private boolean isOriginalBitmapValid;
    private final PDFLib lib;
    private ProgressBar mBusyIndicator;
    private Bitmap originalBitmap;
    private ImageView originalImageView;
    private Rect repaintArea;
    private ImageView repaintImageView;
    private SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> repaintSyncTask;
    private View searchView;
    private int viewHeight;
    private int viewWidth;

    @Override    public void setLinkHighlighting(boolean z) {
    }

    public PDFPageListItem(APageListView aPageListView, IControl iControl, int i, int i2) {
        super(aPageListView, i, i2);
        this.listView = aPageListView;
        this.control = iControl;
        this.lib = (PDFLib) aPageListView.getModel();
        this.isAutoTest = iControl.isAutoTest();
        setBackgroundColor(-1);
    }


    @Override    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        super.onLayout(z, i, i2, i3, i4);
        int i7 = i3 - i;
        int i8 = i4 - i2;
        ImageView imageView = this.originalImageView;
        if (imageView != null) {
            imageView.layout(0, 0, i7, i8);
        }
        View view = this.searchView;
        if (view != null) {
            view.layout(0, 0, i7, i8);
        }
        if (this.viewWidth == i7 && this.viewHeight == i8) {
            ImageView imageView2 = this.repaintImageView;
            if (imageView2 != null) {
                imageView2.layout(this.repaintArea.left, this.repaintArea.top, this.repaintArea.right, this.repaintArea.bottom);
            }
        } else {
            this.viewHeight = 0;
            this.viewWidth = 0;
            this.repaintArea = null;
            ImageView imageView3 = this.repaintImageView;
            if (imageView3 != null) {
                imageView3.setImageBitmap(null);
            }
        }
        if (this.mBusyIndicator != null) {
            if (i7 > this.listView.getWidth()) {
                i5 = ((this.listView.getWidth() - 60) / 2) - i;
            } else {
                i5 = (i7 - 60) / 2;
            }
            if (i8 > this.listView.getHeight()) {
                i6 = ((this.listView.getHeight() - 60) / 2) - i2;
            } else {
                i6 = (i8 - 60) / 2;
            }
            this.mBusyIndicator.layout(i5, i6, i5 + 60, i6 + 60);
        }
    }

    @Override    public void setPageItemRawData(final int i, int i2, int i3) {
        super.setPageItemRawData(i, i2, i3);
        this.isOriginalBitmapValid = false;
        SafeAsyncTask<Void, Void, Bitmap> safeAsyncTask = this.darwOriginalPageTask;
        if (safeAsyncTask != null) {
            safeAsyncTask.cancel(true);
            this.darwOriginalPageTask = null;
        }
        if (this.originalImageView == null) {
            ImageView imageView = new androidx.appcompat.widget.AppCompatImageView(this.listView.getContext()) {                @Override                public boolean isOpaque() {
                    return true;
                }

                @Override                public void onDraw(Canvas canvas) {
                    try {
                        super.onDraw(canvas);
                    } catch (Exception unused) {
                    }
                }
            };
            this.originalImageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(this.originalImageView);
        }
        if (this.pageWidth > 0 && this.pageHeight > 0) {
            this.originalImageView.setImageBitmap(null);
            float fitZoom = this.listView.getFitZoom();
            Bitmap bitmap = this.originalBitmap;
            if (!(bitmap != null && bitmap.getWidth() == ((int) (this.pageWidth * fitZoom)) && this.originalBitmap.getHeight() == ((int) (this.pageHeight * fitZoom)))) {
                int i4 = (int) (this.pageWidth * fitZoom);
                int i5 = (int) (this.pageHeight * fitZoom);
                try {
                    if (!this.listView.isInitZoom()) {
                        this.listView.setZoom(fitZoom, false);
                    }
                    if (this.originalBitmap != null) {
                        while (!this.lib.isDrawPageSyncFinished()) {
                            try {
                                Thread.sleep(100L);
                            } catch (Exception unused) {
                            }
                        }
                        this.originalBitmap.recycle();
                    }
                    this.originalBitmap = Bitmap.createBitmap(i4, i5, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError unused2) {
                    System.gc();
                    try {
                        Thread.sleep(50L);
                        this.originalBitmap = Bitmap.createBitmap(i4, i5, Bitmap.Config.ARGB_8888);
                    } catch (Exception unused3) {
                        return;
                    }
                }
            }
            SafeAsyncTask<Void, Void, Bitmap> safeAsyncTask2 = new SafeAsyncTask<Void, Void, Bitmap>() {                private boolean isCancel = false;

                public Bitmap doInBackground(Void... voidArr) {
                    try {
                        if (PDFPageListItem.this.originalBitmap == null) {
                            return null;
                        }
                        Thread.sleep(PDFPageListItem.this.pageIndex == PDFPageListItem.this.listView.getCurrentPageNumber() + (-1) ? 500L : 1000L);
                        if (this.isCancel) {
                            return null;
                        }
                        PDFPageListItem.this.lib.drawPageSync(PDFPageListItem.this.originalBitmap, PDFPageListItem.this.pageIndex, PDFPageListItem.this.originalBitmap.getWidth(), PDFPageListItem.this.originalBitmap.getHeight(), 0, 0, PDFPageListItem.this.originalBitmap.getWidth(), PDFPageListItem.this.originalBitmap.getHeight(), 1);
                        return PDFPageListItem.this.originalBitmap;
                    } catch (Exception unused4) {
                        return null;
                    }
                }

                @Override                protected void onPreExecute() {
                    PDFPageListItem.this.originalImageView.setImageBitmap(null);
                    if (PDFPageListItem.this.mBusyIndicator == null) {
                        PDFPageListItem.this.mBusyIndicator = new ProgressBar(PDFPageListItem.this.getContext());
                        PDFPageListItem.this.mBusyIndicator.setIndeterminate(true);
                        PDFPageListItem.this.mBusyIndicator.setBackgroundResource(17301612);
                        PDFPageListItem pDFPageListItem = PDFPageListItem.this;
                        pDFPageListItem.addView(pDFPageListItem.mBusyIndicator);
                        PDFPageListItem.this.mBusyIndicator.setVisibility(View.VISIBLE);
                        return;
                    }
                    PDFPageListItem.this.mBusyIndicator.setVisibility(View.VISIBLE);
                }

                @Override                protected void onCancelled() {
                    this.isCancel = true;
                }

                 public void onPostExecute(Bitmap bitmap2) {
                    try {
                        PDFPageListItem.this.mIsBlank = false;
                        PDFPageListItem.this.isOriginalBitmapValid = true;
                        if (!(PDFPageListItem.this.listView == null || PDFPageListItem.this.mBusyIndicator == null)) {
                            PDFPageListItem.this.mBusyIndicator.setVisibility(View.INVISIBLE);
                        }
                        PDFPageListItem.this.listView.setDoRequstLayout(false);
                        PDFPageListItem.this.originalImageView.setImageBitmap(PDFPageListItem.this.originalBitmap);
                        PDFPageListItem.this.listView.setDoRequstLayout(true);
                        PDFPageListItem.this.invalidate();
                        if (PDFPageListItem.this.listView != null) {
                            if ((((int) (PDFPageListItem.this.listView.getZoom() * 100.0f)) == 100 || (PDFPageListItem.this.isInit && i == 0)) && bitmap2 != null) {
                                if (!PDFPageListItem.this.isInit || i != 0) {
                                    PDFPageListItem.this.listView.exportImage(PDFPageListItem.this, PDFPageListItem.this.originalBitmap);
                                } else {
                                    PDFPageListItem.this.listView.postRepaint(PDFPageListItem.this.listView.getCurrentPageView());
                                }
                            }
                            PDFPageListItem.this.isInit = false;
                            if (PDFPageListItem.this.isAutoTest) {
                                PDFPageListItem.this.control.actionEvent(22, true);
                            }
                        }
                    } catch (NullPointerException unused4) {
                    }
                }
            };
            this.darwOriginalPageTask = safeAsyncTask2;
            safeAsyncTask2.safeExecute();
            if (this.searchView == null) {
                View view = new View(getContext()) {                    @Override                    protected void onDraw(Canvas canvas) {
                        super.onDraw(canvas);
                        PDFFind pDFFind = (PDFFind) PDFPageListItem.this.control.getFind();
                        if (pDFFind != null && !PDFPageListItem.this.mIsBlank) {
                            pDFFind.drawHighlight(canvas, 0, 0, PDFPageListItem.this);
                        }
                    }
                };
                this.searchView = view;
                addView(view, new LayoutParams(-1, -1));
            }
        }
    }

    @Override    public void releaseResources() {
        super.releaseResources();
        this.isOriginalBitmapValid = false;
        SafeAsyncTask<Void, Void, Bitmap> safeAsyncTask = this.darwOriginalPageTask;
        if (safeAsyncTask != null) {
            safeAsyncTask.cancel(true);
            this.darwOriginalPageTask = null;
        }
        SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> safeAsyncTask2 = this.repaintSyncTask;
        if (safeAsyncTask2 != null) {
            safeAsyncTask2.cancel(true);
            this.repaintSyncTask = null;
        }
        ImageView imageView = this.originalImageView;
        if (imageView != null) {
            imageView.setImageBitmap(null);
        }
        ImageView imageView2 = this.repaintImageView;
        if (imageView2 != null) {
            imageView2.setImageBitmap(null);
        }
        this.control.getMainFrame().isShowProgressBar();
        ProgressBar progressBar = this.mBusyIndicator;
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override    public void blank(int i) {
        super.blank(i);
        this.isOriginalBitmapValid = false;
        SafeAsyncTask<Void, Void, Bitmap> safeAsyncTask = this.darwOriginalPageTask;
        if (safeAsyncTask != null) {
            safeAsyncTask.cancel(true);
            this.darwOriginalPageTask = null;
        }
        SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> safeAsyncTask2 = this.repaintSyncTask;
        if (safeAsyncTask2 != null) {
            safeAsyncTask2.cancel(true);
            this.repaintSyncTask = null;
        }
        ImageView imageView = this.originalImageView;
        if (imageView != null) {
            imageView.setImageBitmap(null);
        }
        ImageView imageView2 = this.repaintImageView;
        if (imageView2 != null) {
            imageView2.setImageBitmap(null);
        }
        ProgressBar progressBar = this.mBusyIndicator;
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public int getHyperlinkCount(float f, float f2) {
        float width = (float) getWidth() / this.pageWidth;
        return this.lib.getHyperlinkCountSync(this.pageIndex, (f - getLeft()) / width, (f2 - getTop()) / width);
    }


    @Override    public void addRepaintImageView(Bitmap bitmap) {
        Rect rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        if (rect.width() != this.pageWidth || rect.height() != this.pageHeight || (this.originalBitmap != null && ((int) this.listView.getZoom()) * 100 == 100 && (this.originalBitmap.getWidth() != this.pageWidth || this.originalBitmap.getHeight() != this.pageHeight))) {
            Rect rect2 = new Rect(0, 0, this.listView.getWidth(), this.listView.getHeight());
            if (rect2.intersect(rect)) {
                rect2.offset(-rect.left, -rect.top);
                if (!rect2.equals(this.repaintArea) || this.viewHeight != rect.width() || this.viewHeight != rect.height()) {
                    SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> safeAsyncTask = this.repaintSyncTask;
                    if (safeAsyncTask != null) {
                        safeAsyncTask.cancel(true);
                        this.repaintSyncTask = null;
                    }
                    if (this.repaintImageView == null) {
                        ImageView imageView = new androidx.appcompat.widget.AppCompatImageView(this.listView.getContext()) {                            @Override                            public boolean isOpaque() {
                                return true;
                            }

                            @Override                            public void onDraw(Canvas canvas) {
                                try {
                                    super.onDraw(canvas);
                                } catch (Exception unused) {
                                }
                            }
                        };
                        this.repaintImageView = imageView;
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        this.repaintImageView.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
                    }
                    this.repaintSyncTask = new SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo>() {
                        public RepaintAreaInfo doInBackground(RepaintAreaInfo... repaintAreaInfoArr) {
                            try {
                                PDFPageListItem.this.lib.drawPageSync(repaintAreaInfoArr[0].bm, PDFPageListItem.this.pageIndex, repaintAreaInfoArr[0].viewWidth, repaintAreaInfoArr[0].viewHeight, repaintAreaInfoArr[0].repaintArea.left, repaintAreaInfoArr[0].repaintArea.top, repaintAreaInfoArr[0].repaintArea.width(), repaintAreaInfoArr[0].repaintArea.height(), 1);
                                return repaintAreaInfoArr[0];
                            } catch (Exception unused) {
                                return null;
                            }
                        }

                        public void onPostExecute(RepaintAreaInfo repaintAreaInfo) {
                            try {
                                PDFPageListItem.this.viewWidth = repaintAreaInfo.viewWidth;
                                PDFPageListItem.this.viewHeight = repaintAreaInfo.viewHeight;
                                PDFPageListItem.this.repaintArea = repaintAreaInfo.repaintArea;
                                Drawable drawable = PDFPageListItem.this.repaintImageView.getDrawable();
                                if (drawable instanceof BitmapDrawable) {
                                    if (((BitmapDrawable) drawable).getBitmap() != null) {
                                        while (!PDFPageListItem.this.lib.isDrawPageSyncFinished()) {
                                            try {
                                                Thread.sleep(100L);
                                            } catch (Exception unused) {
                                            }
                                        }
                                        ((BitmapDrawable) drawable).getBitmap().recycle();
                                    }
                                    PDFPageListItem.this.listView.setDoRequstLayout(false);
                                    PDFPageListItem.this.repaintImageView.setImageBitmap(null);
                                    PDFPageListItem.this.repaintImageView.setImageBitmap(repaintAreaInfo.bm);
                                    PDFPageListItem.this.listView.setDoRequstLayout(true);
                                }
                                PDFPageListItem.this.repaintImageView.layout(PDFPageListItem.this.repaintArea.left, PDFPageListItem.this.repaintArea.top, PDFPageListItem.this.repaintArea.right, PDFPageListItem.this.repaintArea.bottom);
                                if (PDFPageListItem.this.repaintImageView.getParent() == null) {
                                    PDFPageListItem pDFPageListItem = PDFPageListItem.this;
                                    pDFPageListItem.addView(pDFPageListItem.repaintImageView);
                                    if (PDFPageListItem.this.searchView != null) {
                                        PDFPageListItem.this.searchView.bringToFront();
                                    }
                                }
                                PDFPageListItem.this.invalidate();
                                if (PDFPageListItem.this.listView != null) {
                                    PDFPageListItem.this.listView.exportImage(PDFPageListItem.this, repaintAreaInfo.bm);
                                }
                            } catch (Exception unused2) {
                            }
                        }
                    };
                    try {
                        this.repaintSyncTask.safeExecute(new RepaintAreaInfo(Bitmap.createBitmap(rect2.width(), rect2.height(), Bitmap.Config.ARGB_8888), rect.width(), rect.height(), rect2));
                    } catch (OutOfMemoryError unused) {
                        ImageView imageView2 = this.repaintImageView;
                        if (imageView2 != null) {
                            Drawable drawable = imageView2.getDrawable();
                            if (drawable instanceof BitmapDrawable bitmapDrawable) {
                                if (bitmapDrawable.getBitmap() != null) {
                                    while (!this.lib.isDrawPageSyncFinished()) {
                                        try {
                                            Thread.sleep(100L);
                                        } catch (Exception unused2) {
                                        }
                                    }
                                    bitmapDrawable.getBitmap().recycle();
                                }
                            }
                        }
                        System.gc();
                        try {
                            Thread.sleep(50L);
                            this.repaintSyncTask.safeExecute(new RepaintAreaInfo(Bitmap.createBitmap(rect2.width(), rect2.height(), Bitmap.Config.ARGB_8888), rect.width(), rect.height(), rect2));
                        } catch (Exception | OutOfMemoryError unused3) {
                        }
                    }
                }
            }
        } else if (!this.mIsBlank && this.isOriginalBitmapValid) {
            this.listView.exportImage(this, this.originalBitmap);
        }
    }

    @Override    public void removeRepaintImageView() {
        SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> safeAsyncTask = this.repaintSyncTask;
        if (safeAsyncTask != null) {
            safeAsyncTask.cancel(true);
            this.repaintSyncTask = null;
        }
        this.viewHeight = 0;
        this.viewWidth = 0;
        this.repaintArea = null;
        ImageView imageView = this.repaintImageView;
        if (imageView != null) {
            imageView.setImageBitmap(null);
        }
    }

    protected void drawSerachView(Canvas canvas) {
        View view = this.searchView;
        if (view != null) {
            view.draw(canvas);
        }
    }

    @Override    public void dispose() {
        this.listView = null;
        SafeAsyncTask<Void, Void, Bitmap> safeAsyncTask = this.darwOriginalPageTask;
        if (safeAsyncTask != null) {
            safeAsyncTask.cancel(true);
            this.darwOriginalPageTask = null;
        }
        SafeAsyncTask<RepaintAreaInfo, Void, RepaintAreaInfo> safeAsyncTask2 = this.repaintSyncTask;
        if (safeAsyncTask2 != null) {
            safeAsyncTask2.cancel(true);
            this.repaintSyncTask = null;
        }
        ImageView imageView = this.originalImageView;
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof BitmapDrawable bitmapDrawable) {
                if (bitmapDrawable.getBitmap() != null) {
                    while (!this.lib.isDrawPageSyncFinished()) {
                        try {
                            Thread.sleep(100L);
                        } catch (Exception unused) {
                        }
                    }
                    bitmapDrawable.getBitmap().recycle();
                }
            }
            this.originalImageView.setImageBitmap(null);
        }
        ImageView imageView2 = this.repaintImageView;
        if (imageView2 != null) {
            Drawable drawable2 = imageView2.getDrawable();
            if (drawable2 instanceof BitmapDrawable bitmapDrawable2) {
                if (bitmapDrawable2.getBitmap() != null) {
                    while (!this.lib.isDrawPageSyncFinished()) {
                        try {
                            Thread.sleep(100L);
                        } catch (Exception unused2) {
                        }
                    }
                    bitmapDrawable2.getBitmap().recycle();
                }
            }
            this.repaintImageView.setImageBitmap(null);
        }
    }
}