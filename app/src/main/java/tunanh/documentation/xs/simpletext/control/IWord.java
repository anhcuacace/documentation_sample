/*
 * 文件名称:          IWord.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:56:16
 */
package tunanh.documentation.xs.simpletext.control;

import tunanh.documentation.xs.common.shape.IShape;
import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.pg.animate.IAnimation;
import tunanh.documentation.xs.simpletext.model.IDocument;
import tunanh.documentation.xs.system.IControl;

/**
 * IWord 接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-7-27
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IWord
{
    /**
     * @return Returns the highlight.
     */
    IHighlight getHighlight();
    /**
     * 
     */
    Rectangle modelToView(long offset, Rectangle rect, boolean isBack);
    /**
     * 
     */
    IDocument getDocument();
    /**
     * 
     */
    String getText(long start, long end);
    
    /**
     * @param x 为100%的值
     * @param y 为100%的值
     */
    long viewToModel(int x, int y, boolean isBack);
    /**
     * 
     */
    byte getEditType();
    
    /**
     * 
     * @return
     */
    IAnimation getParagraphAnimation(int pargraphID);
    
    /**
     * 
     */
    IShape getTextBox();
    
    /**
     * 
     */
    IControl getControl();
    
    /**
     * 
     */
    void dispose();
}
