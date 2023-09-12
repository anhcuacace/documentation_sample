/*
 * 文件名称:          FileReaderThread.java
 *
 * 编译器:            android2.2
 * 时间:              下午8:11:02
 */
package tunanh.documentation.xs.system;

import android.os.Handler;
import android.os.Message;

import tunanh.documentation.xs.constant.MainConstant;
import tunanh.documentation.xs.fc.doc.DOCReader;
import tunanh.documentation.xs.fc.doc.DOCXReader;
import tunanh.documentation.xs.fc.doc.TXTReader;
import tunanh.documentation.xs.fc.pdf.PDFReader;
import tunanh.documentation.xs.fc.ppt.PPTReader;
import tunanh.documentation.xs.fc.ppt.PPTXReader;
import tunanh.documentation.xs.fc.xls.XLSReader;
import tunanh.documentation.xs.fc.xls.XLSXReader;


public class FileReaderThread implements Runnable {
    private IControl control;
    private String encoding;
    private String filePath;
    private Handler handler;
    private String password = "";
    private Thread thread;

    public FileReaderThread(IControl iControl, Handler handler, String str, String str2) {
        this.control = iControl;
        this.handler = handler;
        this.filePath = str;
        this.encoding = str2;
    }

    public void ReadFile(String str) {
        Thread thread = new Thread(this);
        this.thread = thread;
        thread.start();
        this.password = str;
    }

     @Override
     public void run()
     {
         // show progress
         Message msg = new Message();
         msg.what = MainConstant.HANDLER_MESSAGE_SHOW_PROGRESS;
         handler.handleMessage(msg);

         msg = new Message();
         msg.what = MainConstant.HANDLER_MESSAGE_DISMISS_PROGRESS;
         try
         {
             IReader reader = null;
             String fileName = filePath.toLowerCase();
             // doc
             if (fileName.endsWith(MainConstant.FILE_TYPE_DOC)
                     || fileName.endsWith(MainConstant.FILE_TYPE_DOT))
             {
                 reader = new DOCReader(control, filePath);
             }
             // docx
             else if (fileName.endsWith(MainConstant.FILE_TYPE_DOCX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_DOTX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_DOTM))
             {
                 reader = new DOCXReader(control, filePath);
             }
             //
             else if (fileName.endsWith(MainConstant.FILE_TYPE_TXT))
             {
                 reader = new TXTReader(control, filePath, encoding);
             }
             // xls
             else if (fileName.endsWith(MainConstant.FILE_TYPE_XLS)
                     || fileName.endsWith(MainConstant.FILE_TYPE_XLT))
             {
                 reader = new XLSReader(control, filePath);
             }
             // xlsx
             else if (fileName.endsWith(MainConstant.FILE_TYPE_XLSX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_XLTX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_XLTM)
                     || fileName.endsWith(MainConstant.FILE_TYPE_XLSM))
             {
                 reader = new XLSXReader(control, filePath);
             }
             // ppt
             else if (fileName.endsWith(MainConstant.FILE_TYPE_PPT)
                     || fileName.endsWith(MainConstant.FILE_TYPE_POT))
             {
                 reader = new PPTReader(control, filePath);
             }
             // pptx
             else if (fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
                     || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
                     || fileName.endsWith(MainConstant.FILE_TYPE_POTM))
             {
                 reader = new PPTXReader(control, filePath);
             }
             // PDF document
             else if (fileName.endsWith(MainConstant.FILE_TYPE_PDF))
             {
                 reader = new PDFReader(control, filePath);
             }
             // other
             else
             {
                 reader = new TXTReader(control, filePath, encoding);
             }
             // 把IReader实例传出
             Message mesReader = new Message();
             mesReader.obj = reader;
             mesReader.what = MainConstant.HANDLER_MESSAGE_SEND_READER_INSTANCE;
             handler.handleMessage(mesReader);


             msg.obj = reader.getModel();
             reader.dispose();
             msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;
             //handler.handleMessage(msg);
         }
         catch (OutOfMemoryError eee)
         {
             msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
             msg.obj = eee;
         }
         catch (Exception ee)
         {
             msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
             msg.obj = ee;
         }
         catch (AbortReaderError ee)
         {
             msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
             msg.obj = ee;
         }
         finally
         {
             handler.handleMessage(msg);
             control = null;
             handler= null;
             encoding = null;
             filePath = null;
         }
     }
 }