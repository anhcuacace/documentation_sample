/*
 * 文件名称:          WPPageListItem.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:24:57
 */
package tunanh.documentation.xs.pg.control;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import tunanh.documentation.xs.pg.model.PGModel;
import tunanh.documentation.xs.pg.model.PGSlide;
import tunanh.documentation.xs.pg.view.SlideDrawKit;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.beans.pagelist.APageListItem;
import tunanh.documentation.xs.system.beans.pagelist.APageListView;

/**
 * word engine "PageListView" component item
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2013-1-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */

public class PGPageListItem extends APageListItem {
    public static final int BUSY_SIZE = 60;
    private final PGEditor editor;
    private ProgressBar mBusyIndicator;
    private PGModel pgModel;

     @Override
     public void removeRepaintImageView() {
    }

    public PGPageListItem(APageListView aPageListView, IControl iControl, PGEditor pGEditor, int i, int i2) {
        super(aPageListView, i, i2);
        this.control = iControl;
        this.pgModel = (PGModel) aPageListView.getModel();
        this.editor = pGEditor;
    }

    @Override
     public void onDraw(Canvas canvas) {
        PGSlide slide = this.pgModel.getSlide(this.pageIndex);
        drawPageNubmer(canvas);
        if (slide != null) {
            SlideDrawKit.instance().drawSlide(canvas, this.pgModel, this.editor, slide, this.listView.getZoom());
        }
    }

    private void drawPageNubmer(Canvas canvas) {
        this.control.getMainFrame().isDrawPageNumber();
        if (this.pageIndex != this.listView.getCurrentPageNumber()) {
            try {
                this.control.getMainFrame().changePage(this.listView.getCurrentPageNumber(), this.pgModel.getSlideCount());
            } catch (Exception unused) {
            }
        }
    }

    @Override
     public void setPageItemRawData(int i, int i2, int i3) {
        super.setPageItemRawData(i, i2, i3);
        if (this.pageIndex >= this.pgModel.getRealSlideCount()) {
            ProgressBar progressBar = this.mBusyIndicator;
            if (progressBar == null) {
                ProgressBar progressBar2 = new ProgressBar(getContext());
                this.mBusyIndicator = progressBar2;
                progressBar2.setIndeterminate(true);
                this.mBusyIndicator.setBackgroundResource(17301612);
                addView(this.mBusyIndicator);
                this.mBusyIndicator.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            new Handler(new HandlerThread("PageIndex").getLooper()) {
                @Override
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    while (PGPageListItem.this.pgModel != null && PGPageListItem.this.pageIndex >= PGPageListItem.this.pgModel.getRealSlideCount()) {
                    }
                    if (PGPageListItem.this.mBusyIndicator != null) {
                        PGPageListItem.this.mBusyIndicator.setVisibility(View.INVISIBLE);
                    }
                    PGPageListItem.this.postInvalidate();
                    if (PGPageListItem.this.listView != null) {
                        if (PGPageListItem.this.pageIndex == PGPageListItem.this.listView.getCurrentPageNumber() - 1) {
                            PGPageListItem.this.listView.exportImage(PGPageListItem.this.listView.getCurrentPageView(), null);
                        }
                        PGPageListItem.this.isInit = false;
                    }
                }
            }.sendEmptyMessage(0);
            return;
        }
        if (((int) (this.listView.getZoom() * 100.0f)) == 100 || (this.isInit && i == 0)) {
            this.listView.exportImage(this, null);
        }
        this.isInit = false;
        ProgressBar progressBar3 = this.mBusyIndicator;
        if (progressBar3 != null) {
            progressBar3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
        SlideDrawKit instance = SlideDrawKit.instance();
        PGModel pGModel = this.pgModel;
        instance.disposeOldSlideView(pGModel, pGModel.getSlide(this.pageIndex));
    }

    @Override
    public void blank(int i) {
        super.blank(i);
    }

    @Override
    public void addRepaintImageView(Bitmap bitmap) {
        postInvalidate();
        this.listView.exportImage(this, bitmap);
    }

     @Override
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        super.onLayout(z, i, i2, i3, i4);
        int i7 = i3 - i;
        int i8 = i4 - i2;
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

    @Override
     public void dispose() {
        super.dispose();
        this.control = null;
        this.pgModel = null;
    }
}
