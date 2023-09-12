/*
 * 文件名称:          WPControl.java
 *
 * 编译器:            android2.2
 * 时间:              下午1:57:55
 */
package tunanh.documentation.xs.wp.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.ClipboardManager;
import android.view.View;

import java.util.List;
import java.util.Vector;

import tunanh.documentation.xs.common.ICustomDialog;
import tunanh.documentation.xs.common.IOfficeToPicture;
import tunanh.documentation.xs.common.bookmark.Bookmark;
import tunanh.documentation.xs.common.hyperlink.Hyperlink;
import tunanh.documentation.xs.constant.EventConstant;
import tunanh.documentation.xs.simpletext.model.IDocument;
import tunanh.documentation.xs.system.AbstractControl;
import tunanh.documentation.xs.system.IControl;
import tunanh.documentation.xs.system.IFind;
import tunanh.documentation.xs.system.IMainFrame;
import tunanh.documentation.xs.system.SysKit;
import tunanh.documentation.xs.wp.dialog.TXTEncodingDialog;


public class WPControl extends AbstractControl {
    private boolean isDispose;
    private IControl mainControl;
    private Word wpView;

    @Override
    public byte getApplicationType() {
        return (byte) 0;
    }

    @Override
    public void layoutView(int i, int i2, int i3, int i4) {
    }

    public WPControl(IControl iControl, IDocument iDocument, String str) {
        this.mainControl = iControl;
        this.wpView = new Word(iControl.getMainFrame().getActivity().getApplicationContext(), iDocument, str, this);
    }

    public int getPageCount() {
        Word word = this.wpView;
        if (word != null) {
            return word.getPageCount();
        }
        return 0;
    }

    public void showPageView(int i) {
        Word word = this.wpView;
        if (word != null) {
            word.showPage(i, EventConstant.WP_LAYOUT_NORMAL_VIEW);
        }
    }

    @Override
    public void actionEvent(int i, final Object obj) {
        int i2 = 0;
        switch (i) {
            case EventConstant.TEST_REPAINT_ID /* -268435456 */:
                this.wpView.postInvalidate();
                return;
            case 19:
                this.wpView.init();
                return;
            case 20:
                updateStatus();
                return;
            case 22:
                this.wpView.post(new Runnable() {
                    @Override

                    public void run() {
                        if (!WPControl.this.isDispose) {
                            WPControl.this.mainControl.getMainFrame().showProgressBar(false);
                        }
                    }
                });
                if (isAutoTest()) {
                    getMainFrame().getActivity().onBackPressed();
                    return;
                }
                return;
            case 26:
                if (this.wpView.getParent() != null) {
                    this.wpView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!WPControl.this.isDispose) {
                                WPControl.this.mainControl.getMainFrame().showProgressBar(((Boolean) obj).booleanValue());
                            }
                        }
                    });
                    return;
                }
                return;
            case 27:
                if (this.wpView.getParent() != null) {
                    this.wpView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!WPControl.this.isDispose) {
                                WPControl.this.mainControl.getMainFrame().updateViewImages((List) obj);
                            }
                        }
                    });
                    return;
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            if (!WPControl.this.isDispose) {
                                WPControl.this.mainControl.getMainFrame().updateViewImages((List) obj);
                            }
                        }
                    }.start();
                    return;
                }
            case EventConstant.FILE_COPY_ID /* 268435458 */:
                ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setText(this.wpView.getHighlight().getSelectText());
                return;
            case EventConstant.APP_INTERNET_SEARCH_ID /* 536870914 */:
                ControlKit.instance().internetSearch(this.wpView);
                return;
            case EventConstant.APP_ZOOM_ID /* 536870917 */:
                int[] iArr = (int[]) obj;
                this.wpView.setZoom(iArr[0] / 10000.0f, iArr[1], iArr[2]);
                this.wpView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!WPControl.this.isDispose) {
                            WPControl.this.getMainFrame().changeZoom();
                        }
                    }
                });
                return;
            case EventConstant.APP_HYPERLINK /* 536870920 */:
                Hyperlink hyperlink = (Hyperlink) obj;
                if (hyperlink != null) {
                    try {
                        if (hyperlink.getLinkType() == 5) {
                            Bookmark bookmark = getSysKit().getBookmarkManage().getBookmark(hyperlink.getAddress());
                            if (bookmark != null) {
                                ControlKit.instance().gotoOffset(this.wpView, bookmark.getStart());
                            }
                        } else {
                            getMainFrame().getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(hyperlink.getAddress())));
                        }
                        return;
                    } catch (Exception unused) {
                        return;
                    }
                } else {
                    return;
                }
            case EventConstant.APP_GENERATED_PICTURE_ID /* 536870922 */:
                exportImage();
                return;
            case EventConstant.APP_PAGE_UP_ID /* 536870925 */:
                if (this.wpView.getCurrentRootType() != 1) {
                    Word word = this.wpView;
                    word.showPage(word.getCurrentPageNumber() - 2, EventConstant.APP_PAGE_UP_ID);
                } else if (this.wpView.getEventManage() != null) {
                    this.wpView.getEventManage().onScroll(null, null, 0.0f, (-this.wpView.getHeight()) + 10);
                }
                if (this.wpView.getCurrentRootType() != 2) {
                    updateStatus();
                    exportImage();
                    return;
                }
                return;
            case EventConstant.APP_PAGE_DOWN_ID /* 536870926 */:
                if (this.wpView.getCurrentRootType() != 1) {
                    Word word2 = this.wpView;
                    word2.showPage(word2.getCurrentPageNumber(), EventConstant.APP_PAGE_DOWN_ID);
                } else if (this.wpView.getEventManage() != null) {
                    this.wpView.getEventManage().onScroll(null, null, 0.0f, this.wpView.getHeight() + 10);
                }
                if (this.wpView.getCurrentRootType() != 2) {
                    updateStatus();
                    exportImage();
                    return;
                }
                return;
            case EventConstant.APP_SET_FIT_SIZE_ID /* 536870933 */:
                this.wpView.setFitSize(((Integer) obj).intValue());
                return;
            case EventConstant.APP_INIT_CALLOUTVIEW_ID /* 536870942 */:
                this.wpView.getPrintWord().getListView().getCurrentPageView().initCalloutView();
                return;
            case 805306368:
                this.wpView.getStatus().setSelectTextStatus(!this.wpView.getStatus().isSelectTextStatus());
                return;
            case EventConstant.WP_SWITCH_VIEW /* 805306369 */:
                if (obj != null) {
                    i2 = ((Integer) obj).intValue();
                } else if (this.wpView.getCurrentRootType() == 0) {
                    i2 = 1;
                }
                this.wpView.switchView(i2);
                updateStatus();
                if (i2 != 2) {
                    exportImage();
                    return;
                }
                return;
            case EventConstant.WP_SHOW_PAGE /* 805306370 */:
                this.wpView.showPage(((Integer) obj).intValue(), EventConstant.WP_SHOW_PAGE);
                if (this.wpView.getCurrentRootType() != 2) {
                    updateStatus();
                    exportImage();
                    return;
                }
                return;
            case EventConstant.WP_LAYOUT_NORMAL_VIEW /* 805306373 */:
                if (this.wpView.getCurrentRootType() == 1) {
                    this.wpView.setExportImageAfterZoom(true);
                    this.wpView.layoutNormal();
                    return;
                }
                return;
            case EventConstant.WP_PRINT_MODE /* 805306375 */:
                if (this.wpView.getCurrentRootType() != 2) {
                    this.wpView.switchView(2);
                    updateStatus();
                    return;
                }
                return;
            case EventConstant.WP_LAYOUT_COMPLETED /* 805306376 */:
                Word word3 = this.wpView;
                if (word3 != null) {
                    word3.updateFieldText();
                    if (this.wpView.getParent() == null) {
                        getMainFrame().completeLayout();
                        return;
                    } else {
                        this.wpView.post(new Runnable() {
                            @Override
                            public void run() {
                                WPControl.this.getMainFrame().completeLayout();
                            }
                        });
                        return;
                    }
                } else {
                    return;
                }
            default:
        }
    }

    @Override
    public Object getActionValue(int i, Object obj) {
        int[] iArr;
        switch (i) {
            case EventConstant.APP_ZOOM_ID /* 536870917 */:
                return Float.valueOf(this.wpView.getZoom());
            case EventConstant.APP_FIT_ZOOM_ID /* 536870918 */:
                return Float.valueOf(this.wpView.getFitZoom());
            case EventConstant.APP_COUNT_PAGES_ID /* 536870923 */:
                return Integer.valueOf(this.wpView.getPageCount());
            case EventConstant.APP_CURRENT_PAGE_NUMBER_ID /* 536870924 */:
                return Integer.valueOf(this.wpView.getCurrentPageNumber());
            case EventConstant.APP_THUMBNAIL_ID /* 536870928 */:
                if (obj instanceof Integer) {
                    return this.wpView.getThumbnail(((Integer) obj).intValue() / 10000.0f);
                }
                return null;
            case EventConstant.APP_PAGEAREA_TO_IMAGE /* 536870931 */:
                if (!(obj instanceof int[]) || (iArr = (int[]) obj) == null || iArr.length != 7) {
                    return null;
                }
                return this.wpView.pageAreaToImage(iArr[0], iArr[1], iArr[2], iArr[3], iArr[4], iArr[5], iArr[6]);
            case EventConstant.APP_GET_FIT_SIZE_STATE_ID /* 536870934 */:
                Word word = this.wpView;
                if (word != null) {
                    return Integer.valueOf(word.getFitSizeState());
                }
                return null;
            case EventConstant.APP_GET_SNAPSHOT_ID /* 536870936 */:
                Word word2 = this.wpView;
                if (word2 != null) {
                    return word2.getSnapshot((Bitmap) obj);
                }
                return null;
            case 805306368:
                return Boolean.valueOf(this.wpView.getStatus().isSelectTextStatus());
            case EventConstant.WP_PAGE_TO_IMAGE /* 805306371 */:
                return this.wpView.allPageToImages(((Integer) obj).intValue());
            case EventConstant.WP_GET_PAGE_SIZE /* 805306372 */:
                return this.wpView.getPageSize(((Integer) obj).intValue() - 1);
            case EventConstant.WP_GET_VIEW_MODE /* 805306374 */:
                return Integer.valueOf(this.wpView.getCurrentRootType());
            case EventConstant.WP_AS_PAGES /* 805306377 */:
                return this.wpView.allPageToImages(1);
            default:
                return null;
        }
    }

    private void exportImage() {
        this.wpView.post(new Runnable() {
            @Override
            public void run() {
                if (!WPControl.this.isDispose) {
                    WPControl.this.wpView.createPicture();
                }
            }
        });
    }

    private void updateStatus() {
        this.wpView.post(new Runnable() {
            @Override
            public void run() {
                if (!WPControl.this.isDispose) {
                    WPControl.this.getMainFrame().updateToolsbarStatus();
                }
            }
        });
    }

    @Override
    public int getCurrentViewIndex() {
        return this.wpView.getCurrentPageNumber();
    }

    @Override
    public View getView() {
        return this.wpView;
    }

    @Override
    public Dialog getDialog(Activity activity, int i) {
        if (i != 1) {
            return null;
        }
        Vector vector = new Vector();
        vector.add(this.wpView.getFilePath());
        new TXTEncodingDialog(this, activity, this.wpView.getDialogAction(), vector, i).show();
        return null;
    }

    @Override
    public IMainFrame getMainFrame() {
        return this.mainControl.getMainFrame();
    }

    @Override
    public Activity getActivity() {
        return getMainFrame().getActivity();
    }

    @Override
    public IFind getFind() {
        return this.wpView.getFind();
    }

    @Override
    public boolean isAutoTest() {
        return this.mainControl.isAutoTest();
    }

    @Override
    public IOfficeToPicture getOfficeToPicture() {
        return this.mainControl.getOfficeToPicture();
    }

    @Override
    public ICustomDialog getCustomDialog() {
        return this.mainControl.getCustomDialog();
    }

    @Override
    public SysKit getSysKit() {
        return this.mainControl.getSysKit();
    }

    @Override
    public void dispose() {
        this.isDispose = true;
        this.wpView.dispose();
        this.wpView = null;
        this.mainControl = null;
    }
}