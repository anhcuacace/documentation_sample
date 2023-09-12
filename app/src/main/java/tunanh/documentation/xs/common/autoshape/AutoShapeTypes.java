/*
 * 文件名称:           AutoShapeTypes.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:31:41
 */
package tunanh.documentation.xs.common.autoshape;

import tunanh.documentation.xs.common.shape.ShapeTypes;

/**
 * autoShape types
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-9-17
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class AutoShapeTypes
{
    //
    private static final AutoShapeTypes kit = new AutoShapeTypes();

    /**
     * 
     * @return
     */
    public static AutoShapeTypes instance()
    {
        return kit;
    }
    
    /**
     * 
     */
    public AutoShapeTypes()
    {
        
    }
    
    public int getAutoShapeType(String name)
    {
        if (name != null)
        {
            // line
            switch (name) {
                case "line" -> {
                    return ShapeTypes.Line;
                }
                case "straightConnector1" -> {
                    return ShapeTypes.StraightConnector1;
                }
                case "bentConnector2" -> {
                    return ShapeTypes.BentConnector2;
                }
                case "bentConnector3" -> {
                    return ShapeTypes.BentConnector3;
                }
                case "curvedConnector2" -> {
                    return ShapeTypes.CurvedConnector2;
                }
                case "curvedConnector3" -> {
                    return ShapeTypes.CurvedConnector3;
                }
                case "curvedConnector4" -> {
                    return ShapeTypes.CurvedConnector4;
                }
                case "curvedConnector5" -> {
                    return ShapeTypes.CurvedConnector5;
                }

                //
                case "rect", "Rect" -> {
                    return ShapeTypes.Rectangle;
                }
                case "roundRect" -> {
                    return ShapeTypes.RoundRectangle;
                }
                case "round1Rect" -> {
                    return ShapeTypes.Round1Rect;
                }
                case "round2SameRect" -> {
                    return ShapeTypes.Round2SameRect;
                }
                case "round2DiagRect" -> {
                    return ShapeTypes.Round2DiagRect;
                }
                case "snip1Rect" -> {
                    return ShapeTypes.Snip1Rect;
                }
                case "snip2SameRect" -> {
                    return ShapeTypes.Snip2SameRect;
                }
                case "snip2DiagRect" -> {
                    return ShapeTypes.Snip2DiagRect;
                }
                case "snipRoundRect" -> {
                    return ShapeTypes.SnipRoundRect;
                }
                case "ellipse" -> {
                    return ShapeTypes.Ellipse;
                }
                case "triangle" -> {
                    return ShapeTypes.Triangle;
                }
                case "rtTriangle" -> {
                    return ShapeTypes.RtTriangle;
                }
                case "parallelogram" -> {
                    return ShapeTypes.Parallelogram;
                }
                case "trapezoid" -> {
                    return ShapeTypes.Trapezoid;
                }
                case "diamond" -> {
                    return ShapeTypes.Diamond;
                }
                case "pentagon" -> {
                    return ShapeTypes.Pentagon;
                }
                case "hexagon" -> {
                    return ShapeTypes.Hexagon;
                }
                case "heptagon" -> {
                    return ShapeTypes.Heptagon;
                }
                case "octagon" -> {
                    return ShapeTypes.Octagon;
                }
                case "decagon" -> {
                    return ShapeTypes.Decagon;
                }
                case "dodecagon" -> {
                    return ShapeTypes.Dodecagon;
                }
                case "pie" -> {
                    return ShapeTypes.Pie;
                }
                case "chord" -> {
                    return ShapeTypes.Chord;
                }
                case "teardrop" -> {
                    return ShapeTypes.Teardrop;
                }
                case "frame" -> {
                    return ShapeTypes.Frame;
                }
                case "halfFrame" -> {
                    return ShapeTypes.HalfFrame;
                }
                case "corner" -> {
                    return ShapeTypes.Corner;
                }
                case "diagStripe" -> {
                    return ShapeTypes.DiagStripe;
                }
                case "plus" -> {
                    return ShapeTypes.Plus;
                }
                case "plaque" -> {
                    return ShapeTypes.Plaque;
                }
                case "can" -> {
                    return ShapeTypes.Can;
                }
                case "cube" -> {
                    return ShapeTypes.Cube;
                }
                case "bevel" -> {
                    return ShapeTypes.Bevel;
                }
                case "donut" -> {
                    return ShapeTypes.Donut;
                }
                case "noSmoking" -> {
                    return ShapeTypes.NoSmoking;
                }
                case "blockArc" -> {
                    return ShapeTypes.BlockArc;
                }
                case "foldedCorner" -> {
                    return ShapeTypes.FoldedCorner;
                }
                case "smileyFace" -> {
                    return ShapeTypes.SmileyFace;
                }
                case "heart" -> {
                    return ShapeTypes.Heart;
                }
                case "lightningBolt" -> {
                    return ShapeTypes.LightningBolt;
                }
                case "sun" -> {
                    return ShapeTypes.Sun;
                }
                case "moon" -> {
                    return ShapeTypes.Moon;
                }
                case "cloud" -> {
                    return ShapeTypes.Cloud;
                }
                case "arc" -> {
                    return ShapeTypes.Arc;
                }
                case "bracketPair" -> {
                    return ShapeTypes.BracketPair;
                }
                case "bracePair" -> {
                    return ShapeTypes.BracePair;
                }
                case "leftBracket" -> {
                    return ShapeTypes.LeftBracket;
                }
                case "rightBracket" -> {
                    return ShapeTypes.RightBracket;
                }
                case "leftBrace" -> {
                    return ShapeTypes.LeftBrace;
                }
                case "rightBrace" -> {
                    return ShapeTypes.RightBrace;
                }
                case "mathPlus" -> {
                    return ShapeTypes.MathPlus;
                }
                case "mathMinus" -> {
                    return ShapeTypes.MathMinus;
                }
                case "mathMultiply" -> {
                    return ShapeTypes.MathMultiply;
                }
                case "mathDivide" -> {
                    return ShapeTypes.MathDivide;
                }
                case "mathEqual" -> {
                    return ShapeTypes.MathEqual;
                }
                case "mathNotEqual" -> {
                    return ShapeTypes.MathNotEqual;
                }
                case "rightArrow" -> {
////////////////arrow

                    return ShapeTypes.RightArrow;
                }
                case "leftArrow" -> {
                    return ShapeTypes.LeftArrow;
                }
                case "upArrow" -> {
                    return ShapeTypes.UpArrow;
                }
                case "downArrow" -> {
                    return ShapeTypes.DownArrow;
                }
                case "leftRightArrow" -> {
                    return ShapeTypes.LeftRightArrow;
                }
                case "upDownArrow" -> {
                    return ShapeTypes.UpDownArrow;
                }
                case "quadArrow" -> {
//十字箭头

                    return ShapeTypes.QuadArrow;
                }
                case "leftRightUpArrow" -> {
//丁字箭头

                    return ShapeTypes.LeftRightUpArrow;
                }
                case "bentArrow" -> {
//圆角右箭头

                    return ShapeTypes.BentArrow;
                }
                case "uturnArrow" -> {
//手杖形箭头

                    return ShapeTypes.UturnArrow;
                }
                case "leftUpArrow" -> {
//直角双向箭头

                    return ShapeTypes.LeftUpArrow;
                }
                case "bentUpArrow" -> {
//直角上箭头

                    return ShapeTypes.BentUpArrow;
                }
                case "curvedRightArrow" -> {
//左弧形箭头

                    return ShapeTypes.CurvedRightArrow;
                }
                case "curvedLeftArrow" -> {
//右弧形箭头

                    return ShapeTypes.CurvedLeftArrow;
                }
                case "curvedUpArrow" -> {
//下弧形箭头

                    return ShapeTypes.CurvedUpArrow;
                }
                case "curvedDownArrow" -> {
//上弧形箭头

                    return ShapeTypes.CurvedDownArrow;
                }
                case "stripedRightArrow" -> {
//虚尾箭头

                    return ShapeTypes.StripedRightArrow;
                }
                case "notchedRightArrow" -> {
//燕尾形箭头

                    return ShapeTypes.NotchedRightArrow;
                }
                case "homePlate" -> {
//五边形

                    return ShapeTypes.HomePlate;
                }
                case "chevron" -> {
//燕尾形

                    return ShapeTypes.Chevron;
                }
                case "rightArrowCallout" -> {
//右箭头标注

                    return ShapeTypes.RightArrowCallout;
                }
                case "leftArrowCallout" -> {
//

                    return ShapeTypes.LeftArrowCallout;
                }
                case "downArrowCallout" -> {
//

                    return ShapeTypes.DownArrowCallout;
                }
                case "upArrowCallout" -> {
//

                    return ShapeTypes.UpArrowCallout;
                }
                case "leftRightArrowCallout" -> {
//左右箭头标注

                    return ShapeTypes.LeftRightArrowCallout;
                }
                case "quadArrowCallout" -> {
//十字箭头标注

                    return ShapeTypes.QuadArrowCallout;
                }
                case "circularArrow" -> {
//环形箭头

                    return ShapeTypes.CircularArrow;
                }

                // flowChart
                case "flowChartProcess" -> {
                    return ShapeTypes.FlowChartProcess;
                }
                case "flowChartAlternateProcess" -> {
                    return ShapeTypes.FlowChartAlternateProcess;
                }
                case "flowChartDecision" -> {
                    return ShapeTypes.FlowChartDecision;
                }
                case "flowChartInputOutput" -> {
                    return ShapeTypes.FlowChartInputOutput;
                }
                case "flowChartPredefinedProcess" -> {
                    return ShapeTypes.FlowChartPredefinedProcess;
                }
                case "flowChartInternalStorage" -> {
                    return ShapeTypes.FlowChartInternalStorage;
                }
                case "flowChartDocument" -> {
                    return ShapeTypes.FlowChartDocument;
                }
                case "flowChartMultidocument" -> {
                    return ShapeTypes.FlowChartMultidocument;
                }
                case "flowChartTerminator" -> {
                    return ShapeTypes.FlowChartTerminator;
                }
                case "flowChartPreparation" -> {
                    return ShapeTypes.FlowChartPreparation;
                }
                case "flowChartManualInput" -> {
                    return ShapeTypes.FlowChartManualInput;
                }
                case "flowChartManualOperation" -> {
                    return ShapeTypes.FlowChartManualOperation;
                }
                case "flowChartConnector" -> {
                    return ShapeTypes.FlowChartConnector;
                }
                case "flowChartOffpageConnector" -> {
                    return ShapeTypes.FlowChartOffpageConnector;
                }
                case "flowChartPunchedCard" -> {
                    return ShapeTypes.FlowChartPunchedCard;
                }
                case "flowChartPunchedTape" -> {
                    return ShapeTypes.FlowChartPunchedTape;
                }
                case "flowChartSummingJunction" -> {
                    return ShapeTypes.FlowChartSummingJunction;
                }
                case "flowChartOr" -> {
                    return ShapeTypes.FlowChartOr;
                }
                case "flowChartCollate" -> {
                    return ShapeTypes.FlowChartCollate;
                }
                case "flowChartSort" -> {
                    return ShapeTypes.FlowChartSort;
                }
                case "flowChartExtract" -> {
                    return ShapeTypes.FlowChartExtract;
                }
                case "flowChartMerge" -> {
                    return ShapeTypes.FlowChartMerge;
                }
                case "flowChartOnlineStorage" -> {
                    return ShapeTypes.FlowChartOnlineStorage;
                }
                case "flowChartDelay" -> {
                    return ShapeTypes.FlowChartDelay;
                }
                case "flowChartMagneticTape" -> {
                    return ShapeTypes.FlowChartMagneticTape;
                }
                case "flowChartMagneticDisk" -> {
                    return ShapeTypes.FlowChartMagneticDisk;
                }
                case "flowChartMagneticDrum" -> {
                    return ShapeTypes.FlowChartMagneticDrum;
                }
                case "flowChartDisplay" -> {
                    return ShapeTypes.FlowChartDisplay;
                }

                // wedgecallout
                case "wedgeRectCallout" -> {
                    return ShapeTypes.WedgeRectCallout;
                }
                case "wedgeRoundRectCallout" -> {
                    return ShapeTypes.WedgeRoundRectCallout;
                }
                case "wedgeEllipseCallout" -> {
                    return ShapeTypes.WedgeEllipseCallout;
                }
                case "cloudCallout" -> {
                    return ShapeTypes.CloudCallout;
                }
                case "borderCallout1" -> {
                    return ShapeTypes.BorderCallout1;
                }
                case "borderCallout2" -> {
                    return ShapeTypes.BorderCallout2;
                }
                case "borderCallout3" -> {
                    return ShapeTypes.BorderCallout3;
                }
                case "accentCallout1" -> {
                    return ShapeTypes.AccentCallout1;
                }
                case "accentCallout2" -> {
                    return ShapeTypes.AccentCallout2;
                }
                case "accentCallout3" -> {
                    return ShapeTypes.AccentCallout3;
                }
                case "callout1" -> {
                    return ShapeTypes.Callout1;
                }
                case "callout2" -> {
                    return ShapeTypes.Callout2;
                }
                case "callout3" -> {
                    return ShapeTypes.Callout3;
                }
                case "accentBorderCallout1" -> {
                    return ShapeTypes.AccentBorderCallout1;
                }
                case "accentBorderCallout2" -> {
                    return ShapeTypes.AccentBorderCallout2;
                }
                case "accentBorderCallout3" -> {
                    return ShapeTypes.AccentBorderCallout3;
                }
                case "actionButtonBackPrevious" -> {
//actionButton

                    return ShapeTypes.ActionButtonBackPrevious;
                }
                case "actionButtonForwardNext" -> {
                    return ShapeTypes.ActionButtonForwardNext;
                }
                case "actionButtonBeginning" -> {
                    return ShapeTypes.ActionButtonBeginning;
                }
                case "actionButtonEnd" -> {
                    return ShapeTypes.ActionButtonEnd;
                }
                case "actionButtonHome" -> {
                    return ShapeTypes.ActionButtonHome;
                }
                case "actionButtonInformation" -> {
                    return ShapeTypes.ActionButtonInformation;
                }
                case "actionButtonReturn" -> {
                    return ShapeTypes.ActionButtonReturn;
                }
                case "actionButtonMovie" -> {
                    return ShapeTypes.ActionButtonMovie;
                }
                case "actionButtonDocument" -> {
                    return ShapeTypes.ActionButtonDocument;
                }
                case "actionButtonSound" -> {
                    return ShapeTypes.ActionButtonSound;
                }
                case "actionButtonHelp" -> {
                    return ShapeTypes.ActionButtonHelp;
                }
                case "actionButtonBlank" -> {
                    return ShapeTypes.ActionButtonBlank;
                }
                case "irregularSeal1" -> {
//star and flag

                    return ShapeTypes.IrregularSeal1;
                }
                case "irregularSeal2" -> {
                    return ShapeTypes.IrregularSeal2;
                }
                case "star4" -> {
                    return ShapeTypes.Star4;
                }
                case "star5" -> {
                    return ShapeTypes.Star5;
                }
                case "star6" -> {
                    return ShapeTypes.Star6;
                }
                case "star7" -> {
                    return ShapeTypes.Star7;
                }
                case "star8" -> {
                    return ShapeTypes.Star8;
                }
                case "star10" -> {
                    return ShapeTypes.Star10;
                }
                case "star12" -> {
                    return ShapeTypes.Star12;
                }
                case "star16" -> {
                    return ShapeTypes.Star16;
                }
                case "star24" -> {
                    return ShapeTypes.Star24;
                }
                case "star32" -> {
                    return ShapeTypes.Star32;
                }
                case "ribbon2" -> {
                    return ShapeTypes.Ribbon2;
                }
                case "ribbon" -> {
                    return ShapeTypes.Ribbon;
                }
                case "ellipseRibbon2" -> {
                    return ShapeTypes.EllipseRibbon2;
                }
                case "ellipseRibbon" -> {
                    return ShapeTypes.EllipseRibbon;
                }
                case "verticalScroll" -> {
                    return ShapeTypes.VerticalScroll;
                }
                case "horizontalScroll" -> {
                    return ShapeTypes.HorizontalScroll;
                }
                case "wave" -> {
                    return ShapeTypes.Wave;
                }
                case "doubleWave" -> {
                    return ShapeTypes.DoubleWave;
                }
                case "funnel" -> {
                    return ShapeTypes.Funnel;
                }
                case "gear6" -> {
                    return ShapeTypes.Gear6;
                }
                case "gear9" -> {
                    return ShapeTypes.Gear9;
                }
                case "leftCircularArrow" -> {
                    return ShapeTypes.LeftCircularArrow;
                }
                case "leftRightRibbon" -> {
                    return ShapeTypes.LeftRightRibbon;
                }
                case "pieWedge" -> {
                    return ShapeTypes.PieWedge;
                }
                case "swooshArrow" -> {
                    return ShapeTypes.SwooshArrow;
                }
            }
        }
        return ShapeTypes.NotPrimitive;
    }
}
