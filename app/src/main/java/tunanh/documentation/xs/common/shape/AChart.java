/*
 * 文件名称:          aChartData.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:16:53
 */
package tunanh.documentation.xs.common.shape;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileOutputStream;

import tunanh.documentation.xs.common.PaintKit;
import tunanh.documentation.xs.common.picture.Picture;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.thirdpart.achartengine.chart.AbstractChart;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-1-19
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class AChart  extends AbstractShape
{
    public AChart()
    {
        super();
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see tunanh.documentation.xs.common.shape.AbstractShape#getType()
     *
     */
    public short getType()
    {
        return SHAPE_CHART;
    }
    
    /**
     * 
     * @param chart
     */
    public void setAChart(AbstractChart chart)
    {
        this.chart = chart;
    }
    
    /**
     * 
     * @return
     */
    public AbstractChart getAChart()
    {
        return chart;
    }

    private void saveChartToPicture(IControl iControl) {
        Bitmap bitmap = null;
        try {
            int zoomRate = (int) (((float) this.rect.width) * getAChart().getZoomRate());
            int zoomRate2 = (int) (((float) this.rect.height) * getAChart().getZoomRate());
            bitmap = Bitmap.createBitmap(zoomRate, zoomRate2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            this.chart.draw(canvas, iControl, 0, 0, zoomRate, zoomRate2, PaintKit.instance().getPaint());
            canvas.restore();
            Picture picture = new Picture();
            File file = new File(iControl.getSysKit().getPictureManage().getPicTempPath() + File.separator + (System.currentTimeMillis() + ".tmp"));
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            bitmap.recycle();
            fileOutputStream.close();
            picture.setTempFilePath(file.getAbsolutePath());
            this.picIndex = iControl.getSysKit().getPictureManage().addPicture(picture);
        } catch (Exception e) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            iControl.getSysKit().getErrorKit().writerLog(e);
        }
    }


    public void dispose()
    {
        super.dispose();
        chart = null;
    }
    
//    private Bitmap bmp;
    private int picIndex = -1;
    
    private AbstractChart chart;
}
