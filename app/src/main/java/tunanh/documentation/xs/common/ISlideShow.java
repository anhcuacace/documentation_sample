/*
 * 文件名称:          ISlideShow.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:58:39
 */
package tunanh.documentation.xs.common;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-12-28
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public interface ISlideShow
{
    //slideshow type
    //begin slideshow
    byte SlideShow_Begin = 0;                           //0
    //exit slideshow
    byte SlideShow_Exit = SlideShow_Begin + 1;          //1
    //previous step of animation
    byte SlideShow_PreviousStep = SlideShow_Exit + 1;   //2
    //next step of animation
    byte SlideShow_NextStep = SlideShow_PreviousStep + 1;//3
    //previous slide
    byte SlideShow_PreviousSlide = SlideShow_NextStep + 1;//4
    //next slide
    byte SlideShow_NextSlide = SlideShow_PreviousSlide + 1;//5
    
//    /**
//     * 
//     * @param actionType
//     */
//    public void slideshow(byte actionType);
    
    /**
     * exit slideshow
     */
    void exit();
}
