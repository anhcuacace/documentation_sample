package tunanh.documentation.xs.pdf;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class RepaintAreaInfo {
    public Bitmap bm;
    public Rect repaintArea;
    public int viewHeight;
    public int viewWidth;

    public RepaintAreaInfo(Bitmap bitmap, int i, int i2, Rect rect) {
        this.bm = bitmap;
        this.viewWidth = i;
        this.viewHeight = i2;
        this.repaintArea = rect;
    }
}