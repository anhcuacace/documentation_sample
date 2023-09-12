/*
 * 文件名称:           ShapeManage.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:25:43
 */
package tunanh.documentation.xs.fc.ppt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tunanh.documentation.xs.common.autoshape.ArbitraryPolygonShapePath;
import tunanh.documentation.xs.common.autoshape.AutoShapeTypes;
import tunanh.documentation.xs.common.bg.BackgroundAndFill;
import tunanh.documentation.xs.common.borders.Line;
import tunanh.documentation.xs.common.pictureefftect.PictureEffectInfo;
import tunanh.documentation.xs.common.pictureefftect.PictureEffectInfoFactory;
import tunanh.documentation.xs.common.shape.AChart;
import tunanh.documentation.xs.common.shape.AbstractShape;
import tunanh.documentation.xs.common.shape.ArbitraryPolygonShape;
import tunanh.documentation.xs.common.shape.Arrow;
import tunanh.documentation.xs.common.shape.AutoShape;
import tunanh.documentation.xs.common.shape.GroupShape;
import tunanh.documentation.xs.common.shape.IShape;
import tunanh.documentation.xs.common.shape.LineShape;
import tunanh.documentation.xs.common.shape.PictureShape;
import tunanh.documentation.xs.common.shape.ShapeTypes;
import tunanh.documentation.xs.common.shape.SmartArt;
import tunanh.documentation.xs.common.shape.TableShape;
import tunanh.documentation.xs.common.shape.TextBox;
import tunanh.documentation.xs.constant.MainConstant;
import tunanh.documentation.xs.fc.LineKit;
import tunanh.documentation.xs.fc.dom4j.Element;
import tunanh.documentation.xs.fc.openxml4j.opc.PackagePart;
import tunanh.documentation.xs.fc.openxml4j.opc.PackageRelationship;
import tunanh.documentation.xs.fc.openxml4j.opc.PackageRelationshipTypes;
import tunanh.documentation.xs.fc.openxml4j.opc.ZipPackage;
import tunanh.documentation.xs.fc.ppt.attribute.ParaAttr;
import tunanh.documentation.xs.fc.ppt.attribute.RunAttr;
import tunanh.documentation.xs.fc.ppt.attribute.SectionAttr;
import tunanh.documentation.xs.fc.ppt.reader.BackgroundReader;
import tunanh.documentation.xs.fc.ppt.reader.PictureReader;
import tunanh.documentation.xs.fc.ppt.reader.ReaderKit;
import tunanh.documentation.xs.fc.ppt.reader.TableReader;
import tunanh.documentation.xs.fc.xls.Reader.drawing.ChartReader;
import tunanh.documentation.xs.java.awt.Rectangle;
import tunanh.documentation.xs.pg.model.PGLayout;
import tunanh.documentation.xs.pg.model.PGMaster;
import tunanh.documentation.xs.pg.model.PGModel;
import tunanh.documentation.xs.pg.model.PGPlaceholderUtil;
import tunanh.documentation.xs.pg.model.PGSlide;
import tunanh.documentation.xs.pg.model.PGStyle;
import tunanh.documentation.xs.simpletext.model.AttrManage;
import tunanh.documentation.xs.simpletext.model.IAttributeSet;
import tunanh.documentation.xs.simpletext.model.SectionElement;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.thirdpart.achartengine.chart.AbstractChart;

/**
 * process shape
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-6-12
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ShapeManage
{   
    private static final ShapeManage kit = new ShapeManage();
    
    /**
     * 
     */
    public static ShapeManage instance()
    {
        return kit;
    }
    
    /**
     * process shape
     * @param zipPackage
     * @param packagePart
     * @param pgModel TODO
     * @param pgMaster
     * @param pgLayout
     * @param defaultStyle
     * @param pgSlide
     * @param slideType TODO
     * @param sp
     * @throws Exception
     */
    public Integer processShape(IControl control, ZipPackage zipPackage, PackagePart packagePart, 
        PGModel pgModel, PGMaster pgMaster, PGLayout pgLayout, PGStyle defaultStyle, PGSlide pgSlide, byte slideType, 
        Element sp, GroupShape parent, float zoomX, float zoomY) throws Exception
    {
        if (ReaderKit.instance().isHidden(sp))
        {
            return null;
        }
        boolean addShape = packagePart.getPartName().getName().contains("/ppt/slides/");
        addShape = addShape || (!addShape && ReaderKit.instance().isUserDrawn(sp));
        RunAttr.instance().setSlide(addShape);
        String name = sp.getName();
        switch (name) {
            case "sp", "cxnSp" -> {
                // auto shape
                return processAutoShapeAndTextShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout, defaultStyle, pgSlide, slideType,
                        sp, parent, zoomX, zoomY, addShape);
            }
            case "pic" -> {
                // picture shape
                if (addShape) {
                    return processPicture(control, zipPackage, packagePart, pgMaster, pgLayout, pgSlide, sp, parent, zoomX, zoomY);
                }
            }
            case "graphicFrame" -> {
                // graphicFrame
                if (addShape) {
                    return processGraphicFrame(control, zipPackage, packagePart, pgModel,
                            pgMaster, pgLayout, pgSlide, sp, parent, zoomX, zoomY);
                }
            }
            case "grpSp" -> {
                //shape id
                Element e = sp.element("nvGrpSpPr");
                int grpShapeID = 0;
                if (e != null && (e = e.element("cNvPr")) != null) {
                    grpShapeID = Integer.parseInt(e.attributeValue("id"));
                }

                // shapeGroup
                GroupShape groupShape = null;
                float[] zoomXY = null;
                Element grpSpPr = sp.element("grpSpPr");
                if (grpSpPr != null) {
                    Rectangle rect = null;
                    Rectangle childRect = null;
                    rect = ReaderKit.instance().getShapeAnchor(grpSpPr.element("xfrm"), zoomX, zoomY);
                    rect = processGrpSpRect(parent, rect);

                    zoomXY = ReaderKit.instance().getAnchorFitZoom(grpSpPr.element("xfrm"));
                    childRect = ReaderKit.instance().getChildShapeAnchor(grpSpPr.element("xfrm"), zoomXY[0] * zoomX, zoomXY[1] * zoomY);

                    groupShape = new GroupShape();
                    groupShape.setOffPostion(rect.x - childRect.x,
                            rect.y - childRect.y);
                    groupShape.setShapeID(grpShapeID);

                    groupShape.setBounds(rect);
                    groupShape.setParent(parent);
                    processGrpRotation(parent, groupShape, grpSpPr);
                }
                List<Integer> childShapeLst = new ArrayList<Integer>();
                Integer shapeId;
                for (Iterator<?> it = sp.elementIterator(); it.hasNext(); ) {
                    shapeId = processShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout,
                            defaultStyle, pgSlide, slideType, (Element) it.next(), groupShape, zoomXY[0] * zoomX, zoomXY[1] * zoomY);
                    if (shapeId != null) {
                        childShapeLst.add(shapeId);
                    }
                }
                if (parent == null) {
                    pgSlide.appendShapes(groupShape);
                } else {
                    parent.appendShapes(groupShape);
                }
                pgSlide.addGroupShape(grpShapeID, childShapeLst);
                return grpShapeID;
            }
            case "AlternateContent" -> {
                Element choice = sp.element("Fallback");
                if (choice != null) {
                    for (Iterator<?> it = choice.elementIterator(); it.hasNext(); ) {
                        processShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout,
                                defaultStyle, pgSlide, slideType, (Element) it.next(), parent, zoomX, zoomY);
                    }
                }
            }
        }
        RunAttr.instance().setSlide(false);
        
        return null;
    }
    
    /**
     * process textbox
     * @param zipPackage
     * @param packagePart
     * @param pgMaster
     * @param pgLayout
     * @param defaultStyle
     * @param sp
     * @throws Exception
     */
    public int processAutoShapeAndTextShape(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel,
        PGMaster pgMaster, PGLayout pgLayout, PGStyle defaultStyle, PGSlide pgSlide, byte slideType, Element sp, 
        GroupShape parent, float zoomX, float zoomY, boolean addShape) throws Exception
    {
        //shape id
        int id = 0;
        Element temp = sp.element("nvSpPr");
        if(temp == null)
        {
            temp = sp.element("nvCxnSpPr");
        }
        temp = temp.element("cNvPr");
        id = Integer.parseInt(temp.attributeValue("id"));
        
        String type = ReaderKit.instance().getPlaceholderType(sp);
        int idx = ReaderKit.instance().getPlaceholderIdx(sp);
        if(slideType == PGSlide.Slide_Layout)
        {
            pgLayout.addShapeType(idx, type);
        }
        else if(type == null && pgMaster != null && idx >= 0)
        {
            type = pgLayout.getShapeType(idx);
        }
        
        int placeHolderID = -1;
        if(PGPlaceholderUtil.instance().isTitleOrBody(type))
        {
            if(slideType == PGSlide.Slide_Master)
            {
                pgMaster.addTitleBodyID(idx, idx);
            }
            else if(slideType == PGSlide.Slide_Layout)
            {
                pgLayout.addTitleBodyID(idx, id);
            } 
        }
        else if((slideType == PGSlide.Slide_Master || slideType == PGSlide.Slide_Layout) 
        		&& ReaderKit.instance().isUserDrawn(sp))
        {
        	placeHolderID = 0;
        }
        
        // get anchor
        Rectangle rect = null;
        temp = sp.element("spPr");
        if (temp != null)
        {
            rect = ReaderKit.instance().getShapeAnchor(temp.element("xfrm"), zoomX, zoomY);
        }
        if (rect == null && pgLayout != null)
        {
            rect = pgLayout.getAnchor(type, idx);
            if (rect == null && pgMaster != null)
            {
                rect = pgMaster.getAnchor(type, idx);
            }
        }
        if (rect != null)
        {
            rect = processGrpSpRect(parent, rect);
            
            AbstractShape shape = null;
            // autoshape
            if (addShape || (!addShape && !PGPlaceholderUtil.instance().isHeaderFooter(type)))
            {
                shape = processAutoShape(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout, pgSlide, sp, id, idx, rect, 
                    isRect(type, idx), parent, slideType, type, !addShape && PGPlaceholderUtil.instance().isTitleOrBody(type));
            }
            
            if(shape != null)
            {
            	if(parent == null)
                {
                    pgSlide.appendShapes(shape); 
                }
                else
                {
                    parent.appendShapes(shape);
                }
            	
            	shape.setPlaceHolderID(placeHolderID);
            	processGrpRotation(parent, shape, sp.element("spPr"));            	
            }
            
            
            
            // ======== 处理文本 ========
            temp = sp.element("txBody");
            if (temp != null && addShape)
            {
                TextBox tb = new TextBox();
                // anchor 
                tb.setBounds(rect);
                tb.setPlaceHolderID(placeHolderID);
                tb.setShapeID(id);
                // 建立章节
                SectionElement secElem = new SectionElement();
                // 开始Offset
                secElem.setStartOffset(0);
                tb.setElement(secElem);
                // 属性
                IAttributeSet attr = secElem.getAttribute();
                // 宽度
                AttrManage.instance().setPageWidth(attr, (int)(rect.width * MainConstant.PIXEL_TO_TWIPS));
                // 高度
                AttrManage.instance().setPageHeight(attr, (int)(rect.height * MainConstant.PIXEL_TO_TWIPS));
                
                IAttributeSet attrLayout = null;
                IAttributeSet attrMaster = null;
                if (pgLayout != null)
                {
                    attrLayout = pgLayout.getSectionAttr(type, idx);
                }
                if (pgMaster != null)
                {
                    attrMaster = pgMaster.getSectionAttr(type, idx);
                }
                SectionAttr.instance().setSectionAttribute(temp.element("bodyPr"), attr, attrLayout, attrMaster, false);
                int offset = ParaAttr.instance().processParagraph(control, pgMaster, pgLayout, defaultStyle, 
                    secElem, sp.element("style"), temp, type, idx);
                secElem.setEndOffset(offset);
                if (tb.getElement() != null && tb.getElement().getText(null) != null 
                    && !tb.getElement().getText(null).isEmpty()
                    && !"\n".equals(tb.getElement().getText(null)))
                {
                    processGrpRotation(parent, tb, sp.element("spPr"));
                	
                    if(parent == null)
                    {
                        pgSlide.appendShapes(tb);
                    }
                    else
                    {                        
                        parent.appendShapes(tb);
                    }
                }
                else if(shape != null)
            	{
                	//process autoshape rotation
            		processGrpRotation(parent, shape, sp.element("spPr")); 
            	}
                
                // wrap line
                Element wrap = temp.element("bodyPr");
                if (wrap != null)
                {
                    // 文本框内自动换行
                    String value = wrap.attributeValue("wrap");
                    tb.setWrapLine(value == null || "square".equalsIgnoreCase(value));
                }
            }
        }
        
        return id;
    }
    
    
    
    /**
     * process picture
     * @param zipPackage
     * @param packagePart
     * @param pgSlide
     * @param sp
     * @throws Exception
     */
    public int processPicture(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, Element sp, GroupShape parent, float zoomX, float zoomY) throws Exception
    {
        //shape id
        Element e = sp.element("nvPicPr");
        int shapeID = 0;
        if(e != null && (e = e.element("cNvPr")) != null)
        {
            shapeID = Integer.parseInt(e.attributeValue("id"));
        }
        
        Element blipFill = sp.element("blipFill");
        if (blipFill == null)
        {
            Element alternateContent = sp.element("AlternateContent");
            if (alternateContent != null)
            {
                Element fallback = alternateContent.element("Fallback");
                if (fallback != null)
                {
                    blipFill = fallback.element("blipFill");
                }
            }
        }
        if (blipFill != null)
        {
            Element blip = blipFill.element("blip");
            if (blip != null && blip.attribute("embed") != null)
            {
                String id = blip.attributeValue("embed");
                if (id != null)
                {
                    Element spPr = sp.element("spPr");
                    if (spPr != null)
                    {
                        Rectangle rect = ReaderKit.instance().getShapeAnchor(spPr.element("xfrm"), zoomX, zoomY);
                        if (rect == null && pgLayout != null)
                        {
                            //String name = ReaderKit.instance().getPlaceholderName(sp);
                            String type = ReaderKit.instance().getPlaceholderType(sp);
                            //type = PGPlaceholderUtil.instance().processType(name, type);
                            int idx = ReaderKit.instance().getPlaceholderIdx(sp);
                            rect = pgLayout.getAnchor(type, idx);
                            if (rect == null && pgMaster != null)
                            {
                                rect = pgMaster.getAnchor(type, idx);
                            }
                        }
                        if (rect != null)
                        {
                            rect = processGrpSpRect(parent, rect);
                            PackageRelationship imageShip = packagePart.getRelationship(id);
                            if (imageShip != null)
                            {
                            	BackgroundAndFill fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, spPr);
                            	Line line = LineKit.createShapeLine(control, zipPackage, packagePart, pgMaster, sp);                            	
//                            	if(fill == null)
//                            	{
//                            		//slide background fill
//                            		fill = pgSlide.getBackgroundAndFill();
//                                    if (fill == null)
//                                    {
//                                        if (pgLayout != null)
//                                        {
//                                            fill = pgLayout.getBackgroundAndFill();
//                                        }
//                                        if (fill == null && pgMaster != null)
//                                        {
//                                            fill = pgMaster.getBackgroundAndFill();
//                                        }
//                                    }
//                                    
//                                    if(fill != null)
//                                    {
//                                    	fill.setSlideBackgroundFill(true);
//                                    }
//                            	}
                            	
                                PackagePart picPart = zipPackage.getPart(imageShip.getTargetURI());
                                PictureShape picShape = addPicture(control, picPart, pgSlide, shapeID, rect, sp.element("spPr"), parent, PictureEffectInfoFactory.getPictureEffectInfor(blipFill));
                                if(picShape != null)
                                {
                                	picShape.setBackgroundAndFill(fill);
                                	picShape.setLine(line);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return shapeID;
    }
    
    /**
     * add picture to slide
     * @param picPart
     * @param pgSlide
     * @param rect
     * @throws Exception
     */
    public PictureShape addPicture(IControl control, PackagePart picPart, PGSlide pgSlide, int shapeID, Rectangle rect, Element spPr, 
        GroupShape parent, PictureEffectInfo effectInfor) throws Exception
    {
    	PictureShape picShape = null;
        if (picPart != null)
        {
            picShape = new PictureShape();
            picShape.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
            picShape.setBounds(rect);
            processGrpRotation(parent, picShape, spPr);
            picShape.setShapeID(shapeID);
            picShape.setPictureEffectInfor(effectInfor);
            if(parent == null)
            {
                pgSlide.appendShapes(picShape);
            }
            else
            {
                parent.appendShapes(picShape);
            }
        }
        
        return picShape;
    }
    
    /**
     * process grahicFrame
     * @param zipPackage
     * @param packagePart
     * @param pgMaster
     * @param pgSlide
     * @param sp
     * @throws Exception
     */
    public int processGraphicFrame(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel, PGMaster pgMaster,
        PGLayout pgLayout, PGSlide pgSlide, Element sp, GroupShape parent, float zoomX, float zoomY) throws Exception
    {
        //shape id
        Element nvGraphicFramePr = sp.element("nvGraphicFramePr");
        int shapeId = 0;
        if(nvGraphicFramePr != null && (nvGraphicFramePr = nvGraphicFramePr.element("cNvPr")) != null)
        {
            shapeId = Integer.parseInt(nvGraphicFramePr.attributeValue("id"));
        }
        
        Element xfrm = sp.element("xfrm");
        Rectangle rect = ReaderKit.instance().getShapeAnchor(xfrm, zoomX, zoomY);
        if (rect == null && pgLayout != null)
        {
            //String name = ReaderKit.instance().getPlaceholderName(sp);
            String type = ReaderKit.instance().getPlaceholderType(sp);
            //type = PGPlaceholderUtil.instance().processType(name, type);
            int idx = ReaderKit.instance().getPlaceholderIdx(sp);
            rect = pgLayout.getAnchor(type, idx);
            if (rect == null && pgMaster != null)
            {
                rect = pgMaster.getAnchor(type, idx);
            }
        }
        if (rect != null)
        {
            rect = processGrpSpRect(parent, rect);
            Element graphic = sp.element("graphic");
            if (graphic != null)
            {
                Element graphicData = graphic.element("graphicData");
                if (graphicData != null && graphicData.attribute("uri") != null)
                {
                    String uri = graphicData.attributeValue("uri");
                    switch (uri) {
                        case PackageRelationshipTypes.OLE_TYPE -> {
                            Element oleObj = graphicData.element("oleObj");
                            if (oleObj == null) {
                                Element alternateContent = graphicData.element("AlternateContent");
                                if (alternateContent != null) {
                                    Element fallback = alternateContent.element("Fallback");
                                    if (fallback != null) {
                                        oleObj = fallback.element("oleObj");
                                        if (oleObj != null) {
                                            Element pic = oleObj.element("pic");
                                            if (pic != null) {
                                                processPicture(control, zipPackage, packagePart, pgMaster, pgLayout,
                                                        pgSlide, pic, parent, zoomX, zoomY);
                                            }
                                        }
                                    }
                                }
                            } else if (oleObj.attribute("spid") != null) {
                                String spid = oleObj.attributeValue("spid");
                                PackagePart picPart = PictureReader.instance().getOLEPart(zipPackage, packagePart, spid, false);
                                addPicture(control, picPart, pgSlide, shapeId, rect, sp.element("spPr"), parent, null);
                            }
                        }
                        case PackageRelationshipTypes.CHART_TYPE -> {
                            Element chart = graphicData.element("chart");
                            if (chart != null && chart.attribute("id") != null) {
                                String id = chart.attributeValue("id");
                                PackageRelationship ship = packagePart.getRelationship(id);
                                if (ship != null) {
                                    PackagePart chartPart = zipPackage.getPart(ship.getTargetURI());
                                    AbstractChart abstrChart = ChartReader.instance().read(control, zipPackage, chartPart, pgMaster.getSchemeColor(), MainConstant.APPLICATION_TYPE_PPT);
                                    if (abstrChart != null) {
                                        AChart shape = new AChart();
                                        shape.setAChart(abstrChart);
                                        shape.setBounds(rect);
                                        shape.setShapeID(shapeId);
                                        pgSlide.appendShapes(shape);
                                    }
                                }
                            }
                        }
                        case PackageRelationshipTypes.TABLE_TYPE -> {
                            Element tbl = graphicData.element("tbl");
                            if (tbl != null) {
                                Element temp = tbl.element("tblPr");
                                if (temp != null) {
                                    TableShape table = TableReader.instance().getTable(control, zipPackage, packagePart, pgModel,
                                            pgMaster, tbl, rect);
                                    if (table != null) {
                                        table.setBounds(rect);
                                        table.setShapeID(shapeId);
                                        pgSlide.appendShapes(table);
                                    }
                                }
                            }
                        }
                        case PackageRelationshipTypes.DIAGRAM_TYPE ->
                                processSmartArt(pgSlide, graphicData, rect);
                    }
                }
            }
        }
        
        return shapeId;
    }
    
    /**
     * 重新计算group shape中的child shape的位置
     * @param rect
     * @return
     */
    private Rectangle processGrpSpRect(GroupShape parent, Rectangle rect)
    {
        if (parent != null)
        {
            rect.x += parent.getOffX();
            rect.y += parent.getOffY();
        }
        return rect;
    }
    
    /**
     * process group rotate
     * @param parent
     * @param shape
     * @return
     */
    private void processGrpRotation(IShape parent, IShape shape, Element spPr)
    {
        ReaderKit.instance().processRotation(spPr, shape);
        /*if (shape != null && parent != null)
        {
            shape.setRotation(shape.getRotation() + parent.getRotation());
        }*/
    }
    
    /**
     * check the type of a shape is or not a rectangle
     * @return
     */
    private boolean isRect(String type, int idx)
    {
        if (type != null && (type.contains("Title") || type.contains("title") 
            || type.contains("ctrTitle") || type.contains("subTitle") || type.contains("body")
            || type.contains("body") || type.contains("half") || type.contains("dt")
            || type.contains("ftr") || type.contains("sldNum")))
        {
            return true;
        }
        else return idx > 0;
    }

    
    /**
     * 
     * @param zipPackage
     * @param packagePart
     * @param pgModel
     * @param pgMaster
     * @param pgLayout
     * @param pgSlide
     * @param sp
     * @param slideType Normal, layout, master
     * @param phType title, body, ft, dt and so on
     * @param shapeType auto shape type
     * @return
     * @throws Exception
     */
    private BackgroundAndFill getBackgrouond(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, Element sp, int shapeIDX, byte slideType, String phType, int shapeType) throws Exception
    {
        // fill
        BackgroundAndFill fill = null;
        if (sp.attribute("useBgFill") != null)
        {
            String val = sp.attributeValue("useBgFill");
            if (val != null && !val.isEmpty() && "1".equals(val))
            {
                fill = pgSlide.getBackgroundAndFill();
                if (fill == null)
                {
                    if (pgLayout != null)
                    {
                        fill = pgLayout.getBackgroundAndFill();
                    }
                    if (fill == null && pgMaster != null)
                    {
                        fill = pgMaster.getBackgroundAndFill();
                    }
                }
                
                if(fill != null)
                {
                	fill.setSlideBackgroundFill(true);
                }
                
                return fill;
            }
        }
        
        Element spPr = sp.element("spPr");
        String spName = sp.getName();
        if (fill == null && spPr.element("noFill") == null && !spName.equals("cxnSp"))
        {
            fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, spPr);
            if (fill == null && shapeType != ShapeTypes.Arc && shapeType != ShapeTypes.BracketPair 
                && shapeType != ShapeTypes.LeftBracket && shapeType != ShapeTypes.RightBracket
                && shapeType != ShapeTypes.BracePair && shapeType != ShapeTypes.LeftBrace
                && shapeType != ShapeTypes.RightBrace && shapeType != ShapeTypes.ArbitraryPolygon)
            {
                fill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, sp.element("style"));
                if(fill != null && fill.getFillType() == BackgroundAndFill.FILL_SOLID && (fill.getForegroundColor() & 0xFFFFFF) == 0)
                {
                	fill = null;
                }
            }
        }
        
        Integer shapeID = null;
        
        //get bg from layout slide
        if(fill == null && slideType == PGSlide.Slide_Normal && PGPlaceholderUtil.instance().isTitleOrBody(phType)
            && pgLayout != null && pgLayout.getSlideMasterIndex() >= 0
            && shapeIDX >= 0)
        {
            PGSlide layoutSlide = pgModel.getSlideMaster(pgLayout.getSlideMasterIndex());
            shapeID = pgLayout.getTitleBodyID(shapeIDX);
            if (shapeID != null)
            {
                IShape[] shapes = layoutSlide.getShapes();
                for(int i = 0; i < shapes.length; i++)
                {
                    if(shapeID == shapes[i].getShapeID() && shapes[i] instanceof AutoShape)
                    {
                         fill = ((AutoShape)shapes[i]).getBackgroundAndFill();
                         break;
                    }
                }
            }
        }
        
      //get bg from master slide
        if(fill == null && slideType == PGSlide.Slide_Normal 
            && pgMaster != null 
            && pgMaster.getSlideMasterIndex() >= 0
            && shapeIDX >= 0)
        {
            PGSlide masterSlide = pgModel.getSlideMaster(pgMaster.getSlideMasterIndex());
            IShape[] shapes = masterSlide.getShapes();
            if(pgMaster.getTitleBodyID(shapeIDX) != null)
            {
                shapeID = pgMaster.getTitleBodyID(shapeIDX);
                if (shapeID != null)
                {
                    for(int i = 0; i < shapes.length; i++)
                    {
                        if(shapeID == shapes[i].getShapeID() && shapes[i] instanceof AutoShape)
                        {
                             fill = ((AutoShape)shapes[i]).getBackgroundAndFill();
                             break;
                        }
                    }
                }
            }
        }
        
        return fill;
    }
    /**
     * 
     * @param zipPackage
     * @param packagePart
     * @param pgModel
     * @param pgMaster
     * @param pgLayout
     * @param pgSlide
     * @param sp
     * @param id
     * @param rect
     * @param isRect
     * @param parent
     * @param hidden
     * @return shapetype
     * @throws Exception
     */
    public AbstractShape processAutoShape(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGModel pgModel, PGMaster pgMaster, 
        PGLayout pgLayout, PGSlide pgSlide, Element sp, int id, int idx, Rectangle rect, boolean isRect, 
        GroupShape parent, byte slideType, String phType, boolean hidden) throws Exception
    {
    	AbstractShape shape =  null;
        int shapeType = ShapeTypes.NotPrimitive;
        Element spPr = sp.element("spPr");
        if (spPr != null)
        {
            String val;
            Float[] values = null;
            boolean border = true;
            String name = ReaderKit.instance().getPlaceholderName(sp);
            String spName = sp.getName();
            if (spName.equals("cxnSp"))
            {
                border = true;
                shapeType = ShapeTypes.StraightConnector1;
            }
            else if (isRect || name.contains("Text Box") || name.contains("TextBox"))
            {
                shapeType = ShapeTypes.Rectangle;
            }
            
            // type
            Element prstGeom = spPr.element("prstGeom");
            if (prstGeom != null)
            {
                if (prstGeom.attribute("prst") != null)
                {
                    val = prstGeom.attributeValue("prst");
                    if (val != null && !val.isEmpty())
                    {
                        shapeType = AutoShapeTypes.instance().getAutoShapeType(val);
                    }
                }
                
                // adjust data
                Element avLst = prstGeom.element("avLst");
                if (avLst != null)
                {
                    List<Element> gds = avLst.elements("gd");
                    if (!gds.isEmpty())
                    {
                        values = new Float[gds.size()];
                        for (int i = 0; i < gds.size(); i++)
                        {
                            Element gd = gds.get(i);
                            val = gd.attributeValue("fmla");
                            val = val.substring(4);
                            values[i] = Float.parseFloat(val) / 100000;
                        }
                    }
                }
            }
            else if(spPr.element("custGeom") != null)
            {
                //beizer line or direct line
                shapeType = ShapeTypes.ArbitraryPolygon;                
            }
            else if (isRect)
            {
                shapeType = ShapeTypes.Rectangle;
            }
            
            BackgroundAndFill fill = getBackgrouond(control, zipPackage, packagePart, pgModel, pgMaster, pgLayout, pgSlide, sp, idx, slideType, phType, shapeType);
            Line line = LineKit.createShapeLine(control, zipPackage, packagePart, pgMaster, sp);
            
            // border
            Element ln = spPr.element("ln");
            Element style = sp.element("style");
            if (ln != null)
            {
                //border                
                if (ln.element("noFill") != null)
                {
                    border = false;
                }
            }
            else if (border)
            {
                if (style == null || style.element("lnRef") == null)
                {
                    border = false;
                }
            }
            
            // lineShape or autoShape
            if (shapeType == ShapeTypes.Line 
            		|| shapeType == ShapeTypes.StraightConnector1 
            		|| shapeType == ShapeTypes.BentConnector2
            		|| shapeType == ShapeTypes.BentConnector3 
            		|| shapeType == ShapeTypes.CurvedConnector2
            		|| shapeType == ShapeTypes.CurvedConnector3
            		|| shapeType == ShapeTypes.CurvedConnector4
            		|| shapeType == ShapeTypes.CurvedConnector5)
            {
                if(!border)
                {
                    return shape;
                }
                LineShape lineShape = new LineShape();
                lineShape.setShapeType(shapeType);
                lineShape.setBounds(rect);                
                lineShape.setShapeID(id);
                lineShape.setHidden(hidden);
                lineShape.setAdjustData(values);                
                lineShape.setLine(line);
                
                if (ln != null)
                {
                    Element temp = ln.element("headEnd");
                    if (temp != null && temp.attribute("type") != null)
                    {
                        byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                        if (arrowType != Arrow.Arrow_None)
                        {
                            lineShape.createStartArrow(arrowType, 
                                Arrow.getArrowSize(temp.attributeValue("w")), 
                                Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                    temp = ln.element("tailEnd");
                    if (temp != null && temp.attribute("type") != null)
                    {
                        byte arrowType = Arrow.getArrowType(temp.attributeValue("type"));
                        if (arrowType != Arrow.Arrow_None)
                        {
                            lineShape.createEndArrow(arrowType, 
                                Arrow.getArrowSize(temp.attributeValue("w")), 
                                Arrow.getArrowSize(temp.attributeValue("len")));
                        }
                    }
                }              
                
                return shape = lineShape;
            }
            else if(shapeType == ShapeTypes.ArbitraryPolygon)
            {
                ArbitraryPolygonShape arbitraryPolygonShape = new ArbitraryPolygonShape();
                BackgroundAndFill lineFill = null;
                if(line != null)
                {
                	lineFill = line.getBackgroundAndFill();
                }
                ArbitraryPolygonShapePath.processArbitraryPolygonShape(arbitraryPolygonShape, sp, fill, border, lineFill, ln, rect);

                arbitraryPolygonShape.setShapeType(shapeType);
                arbitraryPolygonShape.setShapeID(id);
                processGrpRotation(parent, arbitraryPolygonShape, spPr);
                arbitraryPolygonShape.setHidden(hidden);
                arbitraryPolygonShape.setLine(line);                
                
                return shape = arbitraryPolygonShape;
            }
            else if (fill != null || line != null)
            {
                AutoShape autoShape = new AutoShape(shapeType);
                autoShape.setBounds(rect);
                autoShape.setShapeID(id);
                autoShape.setHidden(hidden);
               
                if (fill != null)
                {
                    autoShape.setBackgroundAndFill(fill);
                }
                if (line != null)
                {
                	autoShape.setLine(line);
                }
                autoShape.setAdjustData(values);
                
                return shape =  autoShape;
            }
        }
        return shape;
    }
    
    private void processSmartArt(PGSlide pgslide, Element graphicData, Rectangle rect)
    {   
        try
        {
            if (graphicData != null)
            {
                Element relIds = graphicData.element("relIds");
                String cs = relIds.attributeValue("dm");
                int id = Integer.parseInt(cs.substring("rId".length()));
                if (cs != null)
                {
                    SmartArt smartArt = pgslide.getSmartArt(cs);
                    if(smartArt != null)
                    {
                        smartArt.setBounds(rect);
                        IShape[] shapes = smartArt.getShapes();
                        for(IShape shape : shapes)
                        {
                            shape.setShapeID(id);
                        }
                        
                        pgslide.appendShapes(smartArt);
                    }                    
                }
            }
        }
        catch(Exception e)
        {
            
        }
    }
}
