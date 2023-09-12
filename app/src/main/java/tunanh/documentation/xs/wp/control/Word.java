/*
 * 文件名称:          WPRead.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:11:03
 */

package tunanh.documentation.xs.wp.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.common.picture.PictureKit;
import tunanh.documentation.xs.common.shape.IShape;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.constant.MainConstant;
import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.pg.animate.FadeAnimation;
import tunanh.documentation.xs.simpletext.control.Highlight;
import tunanh.documentation.xs.simpletext.control.IHighlight;
import tunanh.documentation.xs.simpletext.control.IWord;
import tunanh.documentation.xs.simpletext.model.AttrManage;
import tunanh.documentation.xs.simpletext.model.IAttributeSet;
import tunanh.documentation.xs.simpletext.model.IDocument;
import tunanh.documentation.xs.simpletext.view.IView;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.IDialogAction;
import tunanh.documentation.xs.system.SysKit;
import tunanh.documentation.xs.system.beans.pagelist.APageListView;
import tunanh.documentation.xs.wp.view.LayoutKit;
import tunanh.documentation.xs.wp.view.NormalRoot;
import tunanh.documentation.xs.wp.view.PageRoot;
import tunanh.documentation.xs.wp.view.PageView;
import tunanh.documentation.xs.wp.view.WPViewKit;


public class Word extends LinearLayout implements IWord {
    public static final float DEFAULT_MAX_SCALE = 3.0f;
    public static final float DEFAULT_MID_SCALE = 1.75f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;
    protected IControl control;
    private int currentRootType;
    private IDialogAction dialogAction;
    protected IDocument doc;
    protected WPEventManage eventManage;
    private String filePath;
    protected IHighlight highlight;
    private boolean initFinish;
    private boolean isExportImageAfterZoom;
    protected int mHeight;
    protected int mWidth;
    private NormalRoot normalRoot;
    private PageRoot pageRoot;
    private Paint paint;
    private PrintWord printWord;
    protected StatusManage status;
    private Rectangle visibleRect;
    private WPFind wpFind;
    protected float DEFAULT_THUMB_SCALE = 1.0f;
    protected float zoom = 1.0f;
    private int preShowPageIndex = -1;
    private int prePageCount = -1;
    private float normalZoom = 1.0f;

    @Override
    public byte getEditType() {
        return (byte) 2;
    }

    @Override
    public FadeAnimation getParagraphAnimation(int i) {
        return null;
    }

    @Override
    public IShape getTextBox() {
        return null;
    }

    public Word(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Word(Context context, IDocument iDocument, String str, IControl iControl) {
        super(context);
        this.control = iControl;
        this.doc = iDocument;
        int wordDefaultView = iControl.getMainFrame().getWordDefaultView();
        setCurrentRootType(wordDefaultView);
        if (wordDefaultView == 1) {
            this.normalRoot = new NormalRoot(this);
        } else if (wordDefaultView == 0) {
            this.pageRoot = new PageRoot(this);
        } else if (wordDefaultView == 2) {
            this.pageRoot = new PageRoot(this);
            PrintWord printWord = new PrintWord(context, iControl, this.pageRoot);
            this.printWord = printWord;
            addView(printWord);
        }
        this.dialogAction = new WPDialogAction(iControl);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setTypeface(Typeface.SANS_SERIF);
        this.paint.setTextSize(24.0f);
        this.visibleRect = new Rectangle();
        initManage();
        if (wordDefaultView == 2) {
            setOnClickListener(null);
        }
    }

    private void initManage() {
        WPEventManage wPEventManage = new WPEventManage(this, this.control);
        this.eventManage = wPEventManage;
        setOnTouchListener(wPEventManage);
        setLongClickable(true);
        this.wpFind = new WPFind(this);
        this.status = new StatusManage();
        this.highlight = new Highlight(this);
    }

    public void init() {
        NormalRoot normalRoot = this.normalRoot;
        if (normalRoot != null) {
            normalRoot.doLayout(0, 0, this.mWidth, this.mHeight, Integer.MAX_VALUE, 0);
        } else {
            this.pageRoot.doLayout(0, 0, this.mWidth, this.mHeight, Integer.MAX_VALUE, 0);
        }
        this.initFinish = true;
        PrintWord printWord = this.printWord;
        if (printWord != null) {
            printWord.init();
        }
        if (getCurrentRootType() != 2) {
            post(new Runnable() {
                @Override
                public void run() {
                    Word.this.control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                }
            });
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (this.initFinish && this.currentRootType != 2) {
            try {
                if (getCurrentRootType() == 0) {
                    this.pageRoot.draw(canvas, 0, 0, this.zoom);
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDraw Word");
                    int i = this.prePageCount;
                    this.prePageCount = i + 1;
                    sb.append(i);
                    Log.e("TAG==", sb.toString());
                    drawPageNubmer(canvas, this.zoom);
                } else if (getCurrentRootType() == 1) {
                    this.normalRoot.draw(canvas, 0, 0, this.normalZoom);
                }
                IOfficeToPicture officeToPicture = this.control.getOfficeToPicture();
                if (officeToPicture != null && officeToPicture.getModeType() == 0) {
                    toPicture(officeToPicture);
                }
            } catch (Exception e) {
                this.control.getSysKit().getErrorKit().writerLog(e);
                Log.e("Exception==", e.getMessage());
            }
        }
    }

    public void createPicture() {
        IOfficeToPicture officeToPicture = this.control.getOfficeToPicture();
        if (officeToPicture != null && officeToPicture.getModeType() == 1) {
            try {
                toPicture(officeToPicture);
            } catch (Exception e) {
                Log.e("Exception==", e.getMessage());
            }
        }
    }

    private void toPicture(IOfficeToPicture iOfficeToPicture) {
        if (getCurrentRootType() == 2) {
            ((WPPageListItem) this.printWord.getListView().getCurrentPageView()).addRepaintImageView(null);
            return;
        }
        boolean isDrawPictrue = PictureKit.instance().isDrawPictrue();
        PictureKit.instance().setDrawPictrue(true);
        Bitmap bitmap = iOfficeToPicture.getBitmap(getWidth(), getHeight());
        if (bitmap != null) {
            float zoom = getZoom();
            float f = -getScrollX();
            float f2 = -getScrollY();
            if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
                float min = Math.min(bitmap.getWidth() / getWidth(), bitmap.getHeight() / getHeight()) * getZoom();
                PageRoot pageRoot = this.pageRoot;
                float min2 = ((pageRoot != null ? ((float) pageRoot.getChildView().getWidth()) * min : 0.0f) > ((float) bitmap.getWidth()) || getCurrentRootType() == 1) ? Math.min((getScrollX() / zoom) * min, (getWordWidth() * min) - bitmap.getWidth()) : 0.0f;
                float min3 = Math.min((getScrollY() / zoom) * min, (getWordHeight() * min) - getHeight());
                f = -Math.max(0.0f, min2);
                f2 = -Math.max(0.0f, min3);
                zoom = min;
            }
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(f, f2);
            canvas.drawColor(-7829368);
            if (getCurrentRootType() == 0) {
                this.pageRoot.draw(canvas, 0, 0, zoom);
            } else if (getCurrentRootType() == 1) {
                this.normalRoot.draw(canvas, 0, 0, zoom);
            }
            iOfficeToPicture.callBack(bitmap);
            PictureKit.instance().setDrawPictrue(isDrawPictrue);
        }
    }

    public Bitmap getSnapshot(Bitmap bitmap) {
        PrintWord printWord;
        if (bitmap == null) {
            return null;
        }
        if (getCurrentRootType() == 2 && (printWord = this.printWord) != null) {
            return printWord.getSnapshot(bitmap);
        }
        boolean isDrawPictrue = PictureKit.instance().isDrawPictrue();
        PictureKit.instance().setDrawPictrue(true);
        float zoom = getZoom();
        float f = -getScrollX();
        float f2 = -getScrollY();
        if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
            float min = Math.min(bitmap.getWidth() / getWidth(), bitmap.getHeight() / getHeight()) * getZoom();
            PageRoot pageRoot = this.pageRoot;
            float min2 = ((pageRoot != null ? ((float) pageRoot.getChildView().getWidth()) * min : 0.0f) > ((float) bitmap.getWidth()) || getCurrentRootType() == 1) ? Math.min((getScrollX() / zoom) * min, (getWordWidth() * min) - bitmap.getWidth()) : 0.0f;
            float min3 = Math.min((getScrollY() / zoom) * min, (getWordHeight() * min) - getHeight());
            f = -Math.max(0.0f, min2);
            f2 = -Math.max(0.0f, min3);
            zoom = min;
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(f, f2);
        canvas.drawColor(-7829368);
        if (getCurrentRootType() == 0) {
            this.pageRoot.draw(canvas, 0, 0, zoom);
        } else if (getCurrentRootType() == 1) {
            this.normalRoot.draw(canvas, 0, 0, zoom);
        }
        PictureKit.instance().setDrawPictrue(isDrawPictrue);
        return bitmap;
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.initFinish) {
            this.eventManage.stopFling();
            LayoutKit.instance().layoutAllPage(this.pageRoot, this.zoom);
            if (this.currentRootType == 0) {
                Rectangle visibleRect = getVisibleRect();
                int i5 = visibleRect.x;
                int i6 = visibleRect.y;
                int wordWidth = (int) (getWordWidth() * this.zoom);
                int wordHeight = (int) (getWordHeight() * this.zoom);
                if (visibleRect.x + visibleRect.width > wordWidth) {
                    i5 = wordWidth - visibleRect.width;
                }
                if (visibleRect.y + visibleRect.height > wordHeight) {
                    i6 = wordHeight - visibleRect.height;
                }
                if (!(i5 == visibleRect.x && i6 == visibleRect.y)) {
                    scrollTo(Math.max(0, i5), Math.max(0, i6));
                }
            }
            if (i != i3 && this.control.getMainFrame().isZoomAfterLayoutForWord()) {
                layoutNormal();
                setExportImageAfterZoom(true);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    Word.this.control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
                }
            });
        }
    }

    public void layoutNormal() {
        NormalRoot normalRoot = this.normalRoot;
        if (normalRoot != null) {
            normalRoot.stopBackLayout();
            post(new Runnable() {
                @Override
                public void run() {
                    if (Word.this.currentRootType == 1) {
                        Word word = Word.this;
                        word.scrollTo(0, word.getScrollY());
                    }
                    Word.this.normalRoot.layoutAll();
                    Word.this.postInvalidate();
                }
            });
        }
    }

    public void layoutPrintMode() {
        post(new Runnable() {
            @Override
            public void run() {
                APageListView listView;
                if (Word.this.currentRootType == 2 && Word.this.printWord != null && (listView = Word.this.printWord.getListView()) != null && listView.getChildCount() == 1) {
                    listView.requestLayout();
                }
            }
        });
    }

    @Override
    public void computeScroll() {
        if (getCurrentRootType() != 2) {
            this.eventManage.computeScroll();
        }
    }

    public void switchView(int i) {
        if (i != getCurrentRootType()) {
            this.eventManage.stopFling();
            setCurrentRootType(i);
            PictureKit.instance().setDrawPictrue(true);
            if (getCurrentRootType() == 1) {
                if (this.normalRoot == null) {
                    NormalRoot normalRoot = new NormalRoot(this);
                    this.normalRoot = normalRoot;
                    normalRoot.doLayout(0, 0, this.mWidth, this.mHeight, Integer.MAX_VALUE, 0);
                }
                setOnTouchListener(this.eventManage);
                PrintWord printWord = this.printWord;
                if (printWord != null) {
                    printWord.setVisibility(View.INVISIBLE);
                }
            } else if (getCurrentRootType() == 0) {
                if (this.pageRoot == null) {
                    PageRoot pageRoot = new PageRoot(this);
                    this.pageRoot = pageRoot;
                    pageRoot.doLayout(0, 0, this.mWidth, this.mHeight, Integer.MAX_VALUE, 0);
                } else {
                    LayoutKit.instance().layoutAllPage(this.pageRoot, this.zoom);
                }
                setOnTouchListener(this.eventManage);
                PrintWord printWord2 = this.printWord;
                if (printWord2 != null) {
                    printWord2.setVisibility(View.INVISIBLE);
                }
            } else if (getCurrentRootType() == 2) {
                if (this.pageRoot == null) {
                    PageRoot pageRoot2 = new PageRoot(this);
                    this.pageRoot = pageRoot2;
                    pageRoot2.doLayout(0, 0, this.mWidth, this.mHeight, Integer.MAX_VALUE, 0);
                }
                PrintWord printWord3 = this.printWord;
                if (printWord3 == null) {
                    this.printWord = new PrintWord(getContext(), this.control, this.pageRoot);
                    Object viewBackground = this.control.getMainFrame().getViewBackground();
                    if (viewBackground != null) {
                        if (viewBackground instanceof Integer) {
                            this.printWord.setBackgroundColor(((Integer) viewBackground).intValue());
                        } else if (viewBackground instanceof Drawable) {
                            this.printWord.setBackgroundDrawable((Drawable) viewBackground);
                        }
                    }
                    addView(this.printWord);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            Word.this.printWord.init();
                            Word.this.printWord.postInvalidate();
                        }
                    });
                } else {
                    printWord3.setVisibility(View.VISIBLE);
                }
                scrollTo(0, 0);
                setOnClickListener(null);
                return;
            }
            post(new Runnable() {
                @Override
                public void run() {
                    Word word = Word.this;
                    word.scrollTo(0, word.getScrollY());
                    Word.this.postInvalidate();
                }
            });
        }
    }

    public Rectangle getVisibleRect() {
        this.visibleRect.x = getScrollX();
        this.visibleRect.y = getScrollY();
        this.visibleRect.width = getWidth();
        this.visibleRect.height = getHeight();
        return this.visibleRect;
    }

    public void setZoom(float f, int i, int i2) {
        float f2;
        int i3 = this.currentRootType;
        if (i3 == 0) {
            f2 = this.zoom;
            this.zoom = f;
            LayoutKit.instance().layoutAllPage(this.pageRoot, f);
        } else if (i3 == 2) {
            this.printWord.setZoom(f, i, i2);
            return;
        } else if (i3 == 1) {
            f2 = this.normalZoom;
            this.normalZoom = f;
        } else {
            f2 = 1.0f;
        }
        scrollToFocusXY(f, f2, i, i2);
    }

    public void setFitSize(int i) {
        if (this.currentRootType == 2) {
            this.printWord.setFitSize(i);
        }
    }

    public int getFitSizeState() {
        if (this.currentRootType == 2) {
            return this.printWord.getFitSizeState();
        }
        return 0;
    }

    private void scrollToFocusXY(float f, float f2, int i, int i2) {
        int i3;
        float f3;
        float f4 = 0;
        int i4 = 0;
        PageRoot pageRoot;
        if (i == Integer.MIN_VALUE && i2 == Integer.MIN_VALUE) {
            i = getWidth() / 2;
            i2 = getHeight() / 2;
        }
        if (getCurrentRootType() != 0 || (pageRoot = this.pageRoot) == null || pageRoot.getChildView() == null) {
            f3 = getWidth();
            i3 = getHeight();
        } else {
            f3 = this.pageRoot.getChildView().getWidth();
            i3 = this.pageRoot.getChildView().getHeight();
        }
        int i5 = (int) (i3 * f2);
        scrollBy((int) ((((int) (f3 * f)) - i4) * (((getScrollX() + i) * 1.0f) / ((int) (f2 * f3)))), (int) ((((int) (f4 * f)) - i5) * (((getScrollY() + i2) * 1.0f) / i5)));
    }

    @Override
    public void scrollTo(int i, int i2) {
        super.scrollTo(Math.max(Math.min(Math.max(i, 0), (int) ((getWordWidth() * getZoom()) - getWidth())), 0), Math.max(Math.min(Math.max(i2, 0), (int) ((getWordHeight() * getZoom()) - getHeight())), 0));
    }

    public int getCurrentPageNumber() {
        if (this.currentRootType == 1 || this.pageRoot == null) {
            return 1;
        }
        if (getCurrentRootType() == 2) {
            return this.printWord.getCurrentPageNumber();
        }
        PageView pageView = WPViewKit.instance().getPageView(this.pageRoot, (int) (getScrollX() / this.zoom), ((int) (getScrollY() / this.zoom)) + (getHeight() / 3));
        if (pageView == null) {
            return 1;
        }
        return pageView.getPageNumber();
    }

    public Rectangle getPageSize(int i) {
        PageRoot pageRoot = this.pageRoot;
        if (pageRoot == null || this.currentRootType == 1) {
            return new Rectangle(0, 0, getWidth(), getHeight());
        }
        if (i < 0 || i > pageRoot.getChildCount()) {
            return null;
        }
        PageView pageView = WPViewKit.instance().getPageView(this.pageRoot, (int) (getScrollX() / this.zoom), ((int) (getScrollY() / this.zoom)) + (getHeight() / 5));
        if (pageView != null) {
            return new Rectangle(0, 0, pageView.getWidth(), pageView.getHeight());
        }
        IAttributeSet attribute = this.doc.getSection(0L).getAttribute();
        return new Rectangle(0, 0, (int) (AttrManage.instance().getPageWidth(attribute) * MainConstant.TWIPS_TO_PIXEL), (int) (AttrManage.instance().getPageHeight(attribute) * MainConstant.TWIPS_TO_PIXEL));
    }

    private void drawPageNubmer(Canvas canvas, float f) {
        int i;
        int currentPageNumber = getCurrentPageNumber();
        if (!this.control.getMainFrame().isDrawPageNumber() || this.pageRoot == null) {
            i = 0;
        } else {
            Rect clipBounds = canvas.getClipBounds();
            if (clipBounds.width() == getWidth() && clipBounds.height() == getHeight()) {
                i = this.pageRoot.getPageCount();
                Log.e("page==", i + "");
            } else {
                return;
            }
        }
        if (this.preShowPageIndex != currentPageNumber || this.prePageCount != getPageCount()) {
            this.control.getMainFrame().changePage();
            this.control.getMainFrame().changePage(currentPageNumber, i);
            this.preShowPageIndex = currentPageNumber;
            this.prePageCount = getPageCount();
        }
    }

    @Override
    public long viewToModel(int i, int i2, boolean z) {
        if (getCurrentRootType() == 0) {
            return this.pageRoot.viewToModel(i, i2, z);
        }
        if (getCurrentRootType() == 1) {
            return this.normalRoot.viewToModel(i, i2, z);
        }
        if (getCurrentRootType() == 2) {
            return this.printWord.viewToModel(i, i2, z);
        }
        return 0L;
    }

    @Override
    public Rectangle modelToView(long j, Rectangle rectangle, boolean z) {
        if (getCurrentRootType() == 0) {
            return this.pageRoot.modelToView(j, rectangle, z);
        }
        if (getCurrentRootType() == 1) {
            return this.normalRoot.modelToView(j, rectangle, z);
        }
        return getCurrentRootType() == 2 ? this.printWord.modelToView(j, rectangle, z) : rectangle;
    }

    public IView getRoot(int i) {
        if (i == 0) {
            return this.pageRoot;
        }
        if (i == 1) {
            return this.normalRoot;
        }
        return null;
    }

    @Override
    public String getText(long j, long j2) {
        return this.doc.getText(j, j2);
    }

    public IDialogAction getDialogAction() {
        return this.dialogAction;
    }

    public WPFind getFind() {
        return this.wpFind;
    }

    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public IHighlight getHighlight() {
        return this.highlight;
    }

    @Override
    public IDocument getDocument() {
        return this.doc;
    }

    @Override
    public IControl getControl() {
        return this.control;
    }

    public StatusManage getStatus() {
        return this.status;
    }

    public WPEventManage getEventManage() {
        return this.eventManage;
    }

    public void setSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public int getWordHeight() {
        if (getCurrentRootType() == 0) {
            return this.mHeight;
        }
        if (getCurrentRootType() == 1) {
            return this.normalRoot.getHeight();
        }
        return getHeight();
    }

    public void setWordHeight(int i) {
        this.mHeight = i;
    }

    public int getWordWidth() {
        if (getCurrentRootType() == 0) {
            return this.mWidth;
        }
        if (getCurrentRootType() == 1) {
            return this.normalRoot.getWidth();
        }
        return getWidth();
    }

    public void setWordWidth(int i) {
        this.mWidth = i;
    }

    public void showPage(int i, int i2) {
        if (i >= 0 && i < getPageCount() && getCurrentRootType() != 1) {
            if (getCurrentRootType() != 2) {
                PageView pageView = this.pageRoot.getPageView(i);
                if (pageView != null) {
                    scrollTo(getScrollX(), (int) (pageView.getY() * this.zoom));
                }
            } else if (i2 == 536870925) {
                this.printWord.previousPageview();
            } else if (i2 == 536870926) {
                this.printWord.nextPageView();
            } else {
                this.printWord.showPDFPageForIndex(i);
            }
        }
    }

    public Bitmap pageToImage(int i) {
        PageRoot pageRoot;
        PageView pageView;
        if (i <= 0 || i > getPageCount() || (pageRoot = this.pageRoot) == null || pageRoot.getChildView() == null || getCurrentRootType() == 1 || (pageView = this.pageRoot.getPageView(i - 1)) == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(pageView.getWidth(), pageView.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.translate(-pageView.getX(), -pageView.getY());
        canvas.drawColor(-1);
        pageView.draw(canvas, 0, 0, 1.0f);
        return createBitmap;
    }

    public Bitmap allPageToImages(int i) {
        try {
            Rectangle pageSize = getPageSize(i);
            Log.d("zzz==", "width:" + pageSize.getWidth() + "  -- height:" + pageSize.getHeight());
            if (pageSize == null) {
                return null;
            }
            return pageAreaToImage(i, 0, 0, pageSize.width, pageSize.height, Math.round(pageSize.width * this.DEFAULT_THUMB_SCALE), Math.round(pageSize.height * this.DEFAULT_THUMB_SCALE));
        } catch (Exception e) {
            Log.d("TAG==", "Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap pageAreaToImage(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        PageRoot pageRoot;
        PageView pageView;
        if (!(i <= 0 || i > getPageCount() || (pageRoot = this.pageRoot) == null || pageRoot.getChildView() == null || getCurrentRootType() == 1 || (pageView = this.pageRoot.getPageView(i - 1)) == null || !SysKit.isValidateRect(pageView.getWidth(), pageView.getHeight(), i2, i3, i4, i5))) {
            boolean isDrawPictrue = PictureKit.instance().isDrawPictrue();
            PictureKit.instance().setDrawPictrue(true);
            float f = i4;
            float f2 = i5;
            float min = Math.min(i6 / f, i7 / f2);
            try {
                Bitmap createBitmap = Bitmap.createBitmap((int) (f * min), (int) (f2 * min), Config.ARGB_8888);
                if (createBitmap == null) {
                    return null;
                }
                Canvas canvas = new Canvas(createBitmap);
                canvas.translate((-(pageView.getX() + i2)) * min, (-(pageView.getY() + i3)) * min);
                canvas.drawColor(-1);
                pageView.draw(canvas, 0, 0, min);
                PictureKit.instance().setDrawPictrue(isDrawPictrue);
                return createBitmap;
            } catch (OutOfMemoryError unused) {
            }
        }
        return null;
    }

    public Bitmap getThumbnail(float f) {
        Rectangle pageSize = getPageSize(1);
        if (pageSize == null) {
            return null;
        }
        return pageAreaToImage(1, 0, 0, pageSize.width, pageSize.height, Math.round(pageSize.width * f), Math.round(pageSize.height * f));
    }

    public int getPageCount() {
        PageRoot pageRoot;
        if (this.currentRootType == 1 || (pageRoot = this.pageRoot) == null) {
            return 1;
        }
        return pageRoot.getPageCount();
    }

    public int getCurrentRootType() {
        return this.currentRootType;
    }

    public void setCurrentRootType(int i) {
        this.currentRootType = i;
    }

    public float getZoom() {
        int i = this.currentRootType;
        if (i == 1) {
            return this.normalZoom;
        }
        if (i == 0) {
            return this.zoom;
        }
        if (i != 2) {
            return this.zoom;
        }
        PrintWord printWord = this.printWord;
        if (printWord != null) {
            return printWord.getZoom();
        }
        return this.zoom;
    }

    public float getFitZoom() {
        float f;
        int i = this.currentRootType;
        if (i == 1) {
            return 0.5f;
        }
        PageRoot pageRoot = this.pageRoot;
        if (pageRoot == null) {
            return 1.0f;
        }
        if (i == 2) {
            return this.printWord.getFitZoom();
        }
        if (i == 0) {
            IView childView = pageRoot.getChildView();
            int width = childView == null ? 0 : childView.getWidth();
            if (width == 0) {
                width = (int) (AttrManage.instance().getPageWidth(this.doc.getSection(0L).getAttribute()) * MainConstant.TWIPS_TO_PIXEL);
            }
            int width2 = getWidth();
            if (width2 == 0) {
                width2 = ((View) getParent()).getWidth();
            }
            f = (float) (width2 - 5) / width;
        } else {
            f = 1.0f;
        }
        return Math.min(f, 1.0f);
    }

    public boolean isExportImageAfterZoom() {
        return this.isExportImageAfterZoom;
    }

    public void setExportImageAfterZoom(boolean z) {
        this.isExportImageAfterZoom = z;
    }

    @Override
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        PrintWord printWord = this.printWord;
        if (printWord != null) {
            printWord.setBackgroundColor(i);
        }
    }

    @Override
    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        PrintWord printWord = this.printWord;
        if (printWord != null) {
            printWord.setBackgroundResource(i);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        PrintWord printWord = this.printWord;
        if (printWord != null) {
            printWord.setBackgroundDrawable(drawable);
        }
    }

    public PrintWord getPrintWord() {
        return this.printWord;
    }

    public void updateFieldText() {
        PageRoot pageRoot = this.pageRoot;
        if (pageRoot != null && pageRoot.checkUpdateHeaderFooterFieldText()) {
            this.control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
        }
    }

    @Override
    public void dispose() {
        this.control = null;
        StatusManage statusManage = this.status;
        if (statusManage != null) {
            statusManage.dispose();
            this.status = null;
        }
        IHighlight iHighlight = this.highlight;
        if (iHighlight != null) {
            iHighlight.dispose();
            this.highlight = null;
        }
        WPEventManage wPEventManage = this.eventManage;
        if (wPEventManage != null) {
            wPEventManage.dispose();
            this.eventManage = null;
        }
        PageRoot pageRoot = this.pageRoot;
        if (pageRoot != null) {
            pageRoot.dispose();
            this.pageRoot = null;
        }
        NormalRoot normalRoot = this.normalRoot;
        if (normalRoot != null) {
            normalRoot.dispose();
            this.normalRoot = null;
        }
        IDialogAction iDialogAction = this.dialogAction;
        if (iDialogAction != null) {
            iDialogAction.dispose();
            this.dialogAction = null;
        }
        WPFind wPFind = this.wpFind;
        if (wPFind != null) {
            wPFind.dispose();
            this.wpFind = null;
        }
        IDocument iDocument = this.doc;
        if (iDocument != null) {
            iDocument.dispose();
            this.doc = null;
        }
        PrintWord printWord = this.printWord;
        if (printWord != null) {
            printWord.dispose();
        }
        setOnClickListener(null);
        this.doc = null;
        this.paint = null;
        this.visibleRect = null;
    }

    public void resetZoomWithAnimation() {
        setZoom(this.normalZoom, 0, 0);
    }
}