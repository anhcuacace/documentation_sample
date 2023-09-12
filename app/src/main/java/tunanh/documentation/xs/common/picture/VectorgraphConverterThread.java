/*
 * 文件名称:          PictureConverterThread.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:33:03
 */
package tunanh.documentation.xs.common.picture;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-25
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class VectorgraphConverterThread extends Thread
{
    public  VectorgraphConverterThread(PictureConverterMgr converterMgr, byte type , String srcPath, String dstPath, int width, int height)
    {
        this.converterMgr = converterMgr;
        this.type = type;
        
        this.sourPath = srcPath;
        this.destPath = dstPath;
        this.picWidth = width;
        this.picHeight = height;
    }
    
    public void run() 
    {
    	converterMgr.convertWMF_EMF(type, sourPath, destPath, picWidth, picHeight, false);
    }   
    
    
    private final PictureConverterMgr converterMgr;
    private final byte type;
    private final String sourPath;
    private final String destPath;
    private final int picWidth;
    private final int picHeight;
}
