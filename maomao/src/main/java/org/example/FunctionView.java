package org.example;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class FunctionView {

    public static void functionView() throws Exception {
        System.out.println("-----------功能列表----------");
        System.out.println("  1) 歌词word文件转歌词txt文件");
        System.out.println("  2) 歌词txt文件转excel文件");
        System.out.println("  3) 创建专辑文件夹");
        System.out.println("  4) 自动分类专辑文件");
        System.out.println("  5) 扫描复制歌词txt文件");
        System.out.println("  6) 扫描复制专辑txt文件");
        System.out.println("  7) 专辑txt文件转excel文件");
        System.out.println("  0) 帮助");
        System.out.println("  G) 结束");
        System.out.println("----------------------------");
        System.out.print(" 请输入功能编号(回车确认)：");
        String num = new BufferedReader(new InputStreamReader(System.in)).readLine();
        switch(num){
            case "1" :
                WordToTxtUtil.wordToTxt();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "2" :
                TxtToExcelUtil.txtToExcel();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "3" :
                AlbumUtil.createMusicDirectroy();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "4":
                AlbumUtil.autoGroup();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "5" :
                TxtToExcelUtil.copyTxtFile();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "6" :
                AlbumUtil.scanZJtxtFile();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "7" :
                AlbumUtil.txtToExcel();
                new BufferedReader(new InputStreamReader(System.in)).readLine();
                break ;
            case "0" :
                help();
                break ;
            case "G" :
                System.out.println("感谢使用，See you 😙 ~ ");
                Thread.sleep(1000);
                return;
            default:
                System.out.println("你是猪吗?这都能输错，快重新输入!!");
                break ;
        }
        System.out.println("                                                             ");
        functionView();
    }

    public static void help() throws Exception {
        System.out.println("功能1（Word转TXT）：");
        System.out.println("    1）先把word文件放到启动文件的word目录下(支持doc、docx的word文件。); ");
        System.out.println("    2）运行脚本选择功能1 ;");
        System.out.println("    3）生成的txt文件在\"歌词文本\"目录下。");
        System.out.println("                                                                       ");
        System.out.println("功能2（txt转excel文件）：");
        System.out.println("    1）把txt文件放到启动文件的txt目录下;");
        System.out.println("    2）运行脚本选择功能2 ;");
        System.out.println("    3）输入要生成的Excel文件名 ;");
        System.out.println("    4）生成的excel文件在\"excel\"目录下。");
        System.out.println("                                                                       ");
        System.out.println("功能3（创建专辑文件夹）：");
        System.out.println("    1）运行脚本选择功能3 ;");
        System.out.println("    2）输入歌手名和歌曲名 ;");
        System.out.println("    3）生成的文件夹在\"专辑\"目录下。");
        System.out.println("                                                                       ");
        System.out.println("功能4（自动分类专辑文件）：");
        System.out.println("    1）把需要分类的文件复制到对应的专辑目录下 ;");
        System.out.println("    2）运行脚本选择功能4，程序会自动扫描专辑目录内的所有“EP资料”目录，然后自动分类专辑文件 ;");
        System.out.println("    3）自动分类后，要记得检查一下哦。");
        System.out.println("                                                                       ");
        System.out.println("功能5（扫描歌词txt文件并复制到txt目录）：");
        System.out.println("    1）运行脚本选择功能5 ;");
        System.out.println("    2）输入要扫描的目录，程序会自动扫描输入目录内的所有文件夹和文件，复制所有带“歌词”关键字的txt文件 ;");
        System.out.println("    3）复制的txt的文件在\"歌词文本\"目录下。");
        System.out.println("        例子：txt文件的目录是：D:/maomao/m1/毛毛-小星星-歌词.txt " );
        System.out.println("              输入：D:/maomao,  也能扫描到txt文件" );
        System.out.println("                                                                       ");
        System.out.println("功能6（扫描专辑txt文件并复制到专辑txt目录）：");
        System.out.println("    1）运行脚本选择功能6 ;");
        System.out.println("    2）输入要扫描的目录，程序会自动扫描输入目录内的所有文件夹和文件，复制所有带“专辑介绍”关键字的txt文件 ;");
        System.out.println("    3）复制的txt的文件在\"专辑文本\"目录下。");
        System.out.println("        例子：txt文件的目录是：D:/maomao/m1/毛毛-小星星-专辑介绍XXX.txt " );
        System.out.println("              输入：D:/maomao,  也能扫描到txt文件" );
        System.out.println("                                                                       ");
        System.out.println("功能7（专辑txt文件转excel文件）：");
        System.out.println("    1）把专辑txt文件放到启动文件的专辑文本目录下;");
        System.out.println("    2）运行脚本选择功能7 ;");
        System.out.println("    3）输入要生成的Excel文件名 ;");
        System.out.println("    4）生成的excel文件在\"excel\"目录下。");
        System.out.println("                                                                       ");
        System.out.print(" 》》    （按回车返回功能列表）");
        new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("                                                                        ");
    }

    public static void main(String[] args) throws Exception {
        functionView();
    }

    public static String getPath() throws UnsupportedEncodingException {
        String path = URLDecoder.decode(new FunctionView().getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        if(System.getProperty("os.name").contains("dows")) {
            path = path.substring(1,path.length());
        }
        if(path.contains("jar")) {
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }

    public static void existsAndMkdir(File directroy){
        if(!directroy.exists()){
            directroy.mkdirs();
        }
    }

    public static String getCode(File file) throws Exception {
        String code = "GBK";
        try(BufferedInputStream br = new BufferedInputStream(new FileInputStream(file))){
            code = EncodeUtil.getEncode(br, false);
        }
        return code;
    }

}
