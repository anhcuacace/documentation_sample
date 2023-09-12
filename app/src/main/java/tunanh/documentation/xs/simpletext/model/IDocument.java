/*
 * 文件名称:          IDocument.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:49:19
 */
package tunanh.documentation.xs.simpletext.model;


/**
 * 文本model接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-11
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IDocument
{
    
    /**
     * 得到指定的offset的区域
     * @see tunanh.documentation.xs.constant.wp.WPModelConstant
     */
    long getArea(long offset);
    
    /**
     * 得到区域开始位置
     * @see tunanh.documentation.xs.constant.wp.WPModelConstant
     */
    long getAreaStart(long offset);
    
    /**
     * 得到区域结束位置
     * @see tunanh.documentation.xs.constant.wp.WPModelConstant
     */
    long getAreaEnd(long offset);
    
    /**
     * 得到区域文本长度
     */
    long getLength(long offset);
    
    /**
     * 得到章节元素
     */
    IElement getSection(long offset);
    
    /**
     * 得到段落元素 
     */
    IElement getParagraph(long offset);
    /**
     * 
     */
    IElement getParagraphForIndex(int index, long area);
    
    /**
     * 添加段落
     */
    void appendParagraph(IElement element, long offset);
    
    /**
     * 
     */
    void appendSection(IElement elem);
    
    /**
     * 
     */
    void appendElement(IElement elem, long offset);
    
    /**
     * 得到leaf元素
     */
    IElement getLeaf(long offset);
    
    /**
     * 得到页眉、页脚元素
     * @param area 区域
     * @param type Element类型，首页、奇数页、偶数页
     */
    IElement getHFElement(long area, byte type);
    
    /**
     * 得到脚注、尾注元素
     */
    IElement getFEElement(long offset);
    
    /**
     * 插入文本
     * 
     * @param str
     * @param attr
     * @param offset
     */
    void insertString(String str, IAttributeSet attr, long offset);
    
    /**
     * 设置章节属性
     * @param start 开始Offset
     * @param len   长度
     * @param attr  属性集
     */
    void setSectionAttr(long start, int len, IAttributeSet attr);
    
    /**
     * 设置段落属性
     * 
     * @param start 开始Offset
     * @param len   长度
     * @param attr  属性集
     */
    void setParagraphAttr(long start, int len, IAttributeSet attr);
    
    /**
     * 设置leaf属性
     * @param start 开始Offset
     * @param len   长度
     * @param attr  属性集
     */
    void setLeafAttr(long start, int len, IAttributeSet attr);
    
    /**
     * get 段落总数
     */
    int getParaCount(long area);
    
    /**
     * get 字符串
     */
    String getText(long start, long end);
    
    /**
     * 
     */
    void dispose();
    
}
