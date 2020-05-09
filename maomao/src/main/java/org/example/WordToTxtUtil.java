package org.example;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class WordToTxtUtil {

    public static void wordToTxt() throws Exception {
        System.out.println("  ");
        System.out.println(">>   歌词Word 转 歌词txt   <<");
        System.out.println("  ");
        String path = FunctionView.getPath();
        String txtPath = path + "\\" + "歌词文本" + "\\" ;
        File txtDirectory = new File(txtPath);
        if(!txtDirectory.exists()){
            txtDirectory.mkdir();
        }
        File directory = new File(path + "\\word");
        if(!directory.exists()){
            System.out.println("word目录不存在!");
            return ;
        }
        String wordFilePath = path + "\\word\\" ;
        if(directory.isDirectory()){
            String[] filelist = directory.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(wordFilePath + filelist[i]);
                StringBuffer context = new StringBuffer() ;
                String fileName = null ;
                if(filelist[i].endsWith("docx")){
                    context.append(docx2String(new FileInputStream(readfile)));
                    fileName = readfile.getName().replace(".docx", "");
                }else if(filelist[i].endsWith("doc")){
                    context.append(doc2String(new FileInputStream(readfile)));
                    fileName = readfile.getName().replace(".doc", "");
                }
                if(context.length() > 0){
                    File out = new File(txtPath + fileName + ".txt");
                    try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(out))){
                        outputStream.write(context.toString().getBytes("UTF-8"));
                        outputStream.flush();
                    }catch (Exception e){
                        System.out.println("复制出错：" + e.getMessage());
                    }
                }
            }
        }
        System.out.print("已生成txt文件, 速去txt目录下查看! （按回车返回功能列表）");
    }

    /**
     * 读取doc文件内容
     * @return 返回文件内容
     * @throws IOException
     */
    public static String doc2String(FileInputStream fs) throws IOException {
        WordExtractor re = new WordExtractor(fs);
        StringBuilder result = new StringBuilder();
        result.append(re.getText());
        re.close();
        return result.toString();
    }

    public static String docx2String(FileInputStream fs) throws IOException {
        XWPFDocument xdoc = new XWPFDocument(fs);
        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
        String doc1 = extractor.getText();
        extractor.close();
        return doc1;
    }

}
