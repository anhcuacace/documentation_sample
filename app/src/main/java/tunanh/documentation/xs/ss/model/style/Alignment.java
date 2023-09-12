/*
 * 文件名称:          Alignment.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:26:28
 */
package tunanh.documentation.xs.ss.model.style;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-22
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class Alignment
{
    /*

    public final static short ALIGN_GENERAL = 0x0;

    *//*

    public final static short ALIGN_LEFT = 0x1;

    *//*

    public final static short ALIGN_CENTER = 0x2;

    *//*

    public final static short ALIGN_RIGHT = 0x3;

    *//*

    public final static short ALIGN_JUSTIFY = 0x4;

    *//*

    public final static short ALIGN_FILL = 0x5;

    *//*

    public final static short ALIGN_CENTER_SELECTION = 0x6;

    *//*

    public final static short VERTICAL_TOP = 0x1;

    *//*

    public final static short VERTICAL_CENTER = 0x2;

    *//*

    public final static short VERTICAL_BOTTOM = 0x3;

    *//*

    public final static short VERTICAL_JUSTIFY = 0x4;

    *//**
     * vertically Distributed vertical alignment
     *//*

    public final static short VERTICAL_DISTRIBUTED = 0x5;*/
     
    public Alignment()
    {
        
    }
    
    /**
     * 
     * @param horizontal
     */
    public void setHorizontalAlign(short horizontal)
    {
        this.horizontal = horizontal;
    }
    
    /**
     * 
     * @return
     */
    public short getHorizontalAlign()
    {
        return horizontal;
    }
    
    /**
     *
     */
    public void setVerticalAlign(short v)
    {
        this.vertival = v;
    }
    
    /**
     * 
     * @return
     */
    public short getVerticalAlign()
    {
        return vertival;
    }
    
    /**
     * 
     * @param rotation
     */
    
    public void setRotation(short rotation)
    {
        this.rotation = rotation;
    }
    
    /**
     * 
     * @return
     */
    public short getRotaion()
    {
        return rotation;
    }
    
    /**
     * 
     * @param wrapText
     */
    public void setWrapText(boolean wrapText)
    {
        this.wrapText = wrapText;
    }
    
    /**
     * 
     * @return
     */
    public boolean isWrapText()
    {
        return wrapText;
    }
    
    /**
     * 
     * @param indent
     */
    public void setIndent(short indent)
    {
        this.indent = indent;
    }
    
    /**
     * 
     * @return
     */
    public short getIndent()
    {
        return indent;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }
    
    private short horizontal;
    private short vertival = CellStyle.VERTICAL_BOTTOM;
    private short rotation = 0;
    private boolean wrapText = false;
    private short indent = 0;
}