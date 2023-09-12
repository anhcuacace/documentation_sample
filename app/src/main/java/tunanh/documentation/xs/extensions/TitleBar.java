package tunanh.documentation.xs.extensions;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import tunanh.documentation.R;

 public class TitleBar extends LinearLayout {
    public int height;
    public ProgressBar mBusyIndicator;
    public Paint paint;
    public String title;
    public float yPostion;

    public TitleBar(Context context) {
        super(context);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder, options);
        this.height = options.outHeight;
        setBackgroundResource(R.drawable.icon_folder);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setColor(-1);
        this.paint.setTextSize(24.0f);
        Paint.FontMetrics fontMetrics = this.paint.getFontMetrics();
        float f = fontMetrics.ascent;
        this.yPostion = (((this.height - fontMetrics.descent) + f) / 2.0f) - f;
        ProgressBar progressBar = new ProgressBar(getContext());
        this.mBusyIndicator = progressBar;
        progressBar.setIndeterminate(true);
        addView(this.mBusyIndicator);
        this.mBusyIndicator.setVisibility(View.GONE);
    }

    public void dispose() {
        this.paint = null;
        this.title = null;
        ProgressBar progressBar = this.mBusyIndicator;
        if (progressBar != null) {
            removeView(progressBar);
            this.mBusyIndicator = null;
        }
    }

    public int getTitleHeight() {
        return this.height;
    }

    @Override
     public void onDraw(Canvas canvas) {
        String str = this.title;
        if (str != null) {
            canvas.drawText(str, 5.0f, this.yPostion, this.paint);
        }
    }

    @Override
     public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int i5 = i3 - i;
        int i6 = i4 - i2;
        ProgressBar progressBar = this.mBusyIndicator;
        if (progressBar != null) {
            int measuredWidth = progressBar.getMeasuredWidth();
            int measuredHeight = this.mBusyIndicator.getMeasuredHeight();
            this.mBusyIndicator.layout((i5 - measuredWidth) - 5, (i6 - measuredHeight) / 2, i5 - 5, (i6 + measuredHeight) / 2);
        }
    }

    @Override
     public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mBusyIndicator != null) {
            int min = (Math.min(getWidth(), getHeight()) / 2) | Integer.MIN_VALUE;
            this.mBusyIndicator.measure(min, min);
        }
    }

    public void setTitle(String str) {
        this.title = str;
        postInvalidate();
    }

    public void showProgressBar(boolean z) {
        this.mBusyIndicator.setVisibility(z ? View.VISIBLE : View.GONE);
    }
}