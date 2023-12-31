/*
 * 文件名称:          I.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:44:20
 */
package tunanh.documentation.xs.simpletext.model;

/**
 * 属性集接口
 * <p>
 * 属性以ID，Value方式记录，ID类型是short，value类型是int
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-28
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IAttributeSet
{

    /**
     * 得到属性集ID
     */
    int getID();
    /**
     * 添加属性
     * @param attrID
     * @param value
     */
    void setAttribute(short attrID, int value);
    
    /**
     * 删除属性
     * 
     * @param attrID
     */
    void removeAttribute(short attrID);
    
    /**
     * 得到属性
     * @param attrID
     */
    int getAttribute(short attrID);
    /**
     * 合并属性
     */
    void mergeAttribute(IAttributeSet attr);
    /**
     * 
     */
    IAttributeSet clone();
    /**
     * 
     */
    void dispose();
    
}
