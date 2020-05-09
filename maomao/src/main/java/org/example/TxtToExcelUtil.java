package org.example;

import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.*;

public class TxtToExcelUtil {

    public static void txtToExcel() throws Exception{
        System.out.println(" ");
        System.out.println(">>   歌词txt 转 歌词excel  <<");
        System.out.println(" ");

        System.out.println("请输入Excel文件的名称（回车确认）：");
        String excelFileName = new BufferedReader(new InputStreamReader(System.in)).readLine();
        String path = FunctionView.getPath();

        //Excel文件目录
        FunctionView.existsAndMkdir(new File(path + "\\" + "excel"));
        File copyFile = new File(path + "\\excel\\" + excelFileName + ".xlsx");
        try(InputStream inputStream = AlbumUtil.class.getResourceAsStream("/template.xlsx")){
            Files.copy(inputStream, copyFile.toPath());
//            Files.copy(new File(path + "\\" + "template.xlsx").toPath(), copyFile.toPath());
        }catch (FileAlreadyExistsException e){
            System.out.print("文件（" + excelFileName + "）已经存在，快去检查!（按回车返回功能列表）");
            return ;
        }
        String txtPath = path + "\\" + "歌词文本" + "\\" ;
        System.out.println("开始扫描txt文件.....");
        //文本文件目录
        File txtDirectory = new File(txtPath);
        if(txtDirectory.isDirectory()){
            String[] fileNameArray = txtDirectory.list() ;
            System.out.println(" ");
            List<Map<String, String>> musicInfoList = new ArrayList<>(fileNameArray.length);
            for (int i = 0; i < fileNameArray.length; i++) {
                Map content = readTxtFileContent(txtPath + fileNameArray[i]);
                if(content != null){
                    musicInfoList.add(content);
                }
            }
            System.out.println("文件扫描完成，开始生成Excel文件.....");
            int rowNum = 1;
            try(XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(copyFile));
                FileOutputStream out = new FileOutputStream(copyFile)) {
                XSSFSheet sheet = workbook.getSheetAt(0);
                XSSFFont font = workbook.createFont();
                font.setFontName("宋体");
                font.setFontHeightInPoints((short) 10);//设置字体大小
                font.setColor((short)0);
                XSSFCellStyle style = workbook.getCellStyleAt(1);
                style.setFont(font);
                for (Map<String, String> musicInfo : musicInfoList) {
                    XSSFRow row = sheet.getRow(rowNum) ;
                    if(row == null){
                        row = sheet.createRow(rowNum);
                        row.createCell(0).setCellValue(rowNum + "");
                        row.getCell(0).setCellStyle(style);
                        if(musicInfo.get("musicName") != null){
                            row.createCell(2).setCellValue(musicInfo.get("musicName"));
                            row.getCell(2).setCellStyle(style);
                        }else{
                            row.createCell(2).setCellStyle(style);
                        }
                        if(musicInfo.get("authorName") != null){
                            row.createCell(3).setCellValue(musicInfo.get("authorName"));
                            row.getCell(3).setCellStyle(style);
                        }else{
                            row.createCell(3).setCellStyle(style);
                        }
                        if(musicInfo.get("lyrics") != null){
                            row.createCell(4).setCellValue(musicInfo.get("lyrics"));
                            row.getCell(4).setCellStyle(style);
                        }else{
                            row.createCell(4).setCellStyle(style);
                        }
                        if(musicInfo.get("writeMusic") != null){
                            row.createCell(5).setCellValue(musicInfo.get("writeMusic"));
                            row.getCell(5).setCellStyle(style);
                        }else{
                            row.createCell(5).setCellStyle(style);
                        }
                        row.createCell(1).setCellStyle(style);
                        row.createCell(6).setCellStyle(style);
                        row.createCell(7).setCellStyle(style);
                        row.createCell(8).setCellStyle(style);
                    }else{
                        row.getCell(0).setCellValue(rowNum);
                        if(musicInfo.get("musicName") != null){
                            row.getCell(2).setCellValue(musicInfo.get("musicName"));
                        }
                        if(musicInfo.get("authorName") != null){
                            row.getCell(3).setCellValue(musicInfo.get("authorName"));
                        }
                        if(musicInfo.get("lyrics") != null){
                            row.getCell(4).setCellValue(musicInfo.get("lyrics"));
                        }
                        if(musicInfo.get("writeMusic") != null){
                            row.getCell(5).setCellValue(musicInfo.get("writeMusic"));
                        }
                    }
                    rowNum ++ ;
                }
                workbook.write(out);
            }catch (Exception e){
                System.out.println("第"+rowNum+"行出错!:");
                e.printStackTrace();
            }
            System.out.println(" ");
            System.out.print("Excel文件已经生成，速去excel目录中查看!记得核对一下噢(⊙o⊙)。  （按回车返回功能列表）");
        }
    }

    public static Map readTxtFileContent(String txtFilepath) throws Exception {
        String authorName = null ;  String musicName = null ; String lyrics = null ; String writeMusic = null ;
        File txtfile = new File(txtFilepath);
        String fileName = txtfile.getName().replace(".txt", ""); ;
        String code = null ;
        code = FunctionView.getCode(txtfile);
        if("UTF-8_BOM".equals(code)){
            code = "UTF-8" ;
        }
        try(BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtfile), code))){
            try{
                String[] fileSplitArray = fileName.split("-");
                musicName = fileSplitArray[1];
                if(fileSplitArray[0].split("\\.").length > 1){
                    authorName = fileSplitArray[0].split("\\.")[1] ;
                }else{
                    authorName = fileSplitArray[0] ;
                }
            }catch (Exception e){
                System.out.println("文件名不符合规范格式<" + fileName + ">：" + e.getMessage());
                return null ;
            }
            String line = null ;
            boolean isZC = false ; //是否出现过作词
            boolean isZQ = false ; //是否出现过作曲
            while ((line = bfReader.readLine()) != null){
                //冒号的位置
                String t[] = {"作词", "词", "作曲", "曲", "词曲"} ;
                for (int i = 0 ; i < t.length ; i ++ ) {
                    int tIndex = line.indexOf(t[i]) ;
                    if(tIndex >= 0){
                        if((tIndex + t[i].length() + 1) <= line.length()){
                            //0 1
                            String colon = line.substring(tIndex + t[i].length(), tIndex + t[i].length() + 1);
                            if(":".equals(colon) || "：".equals(colon)){
                                if(t[i].indexOf("词") >= 0){
                                    if(!isZC){
                                        lyrics = line.substring(tIndex + t[i].length() + 1);
                                    }
                                    if(i == 0){ //作词
                                        isZC = true ;
                                    }
                                }
                                if(t[i].indexOf("曲") >= 0){
                                    if(!isZQ){
                                        writeMusic = line.substring(tIndex + t[i].length() + 1);
                                    }
                                    if(i == 2){ //作曲
                                        isZQ = true ;
                                    }
                                }
                                if(i == 4){ //词曲
                                    isZC = true ;
                                    isZQ = true ;
                                }
                            }
                        }
                    }
                }
            }
            Map<String, String> musicMap = new HashMap<>();
            musicMap.put("authorName", authorName) ;
            musicMap.put("musicName", musicName) ;
            musicMap.put("lyrics", lyrics) ;
            musicMap.put("writeMusic", writeMusic) ;
            return musicMap ;
        }catch (Exception e){
            System.out.println("读取文件<" + fileName + ">出错：" + e.getMessage());
            e.printStackTrace();
        }
        return null ;
    }

    public static void copyTxtFile() throws Exception {
        System.out.println("  ");
        System.out.println(">>   扫描歌词txt文件并复制到txt目录   <<");
        System.out.println("  ");
        System.out.println("请输入要扫描的目录（复制路径，使用鼠标右键可以黏贴，会自动扫描所有目录噢，回车确认）");
        String scanning = new BufferedReader(new InputStreamReader(System.in)).readLine();

        try{
            File file = new File(scanning);
            if(file.exists()){
                if(file.isDirectory()){
                    sacnTxtFile(file);

                    System.out.print("扫描完成，速去txt目录中查看哦。  （按回车返回功能列表）");
                }else{
                    System.out.println("你是猪吗？输入路径不是一个目录路径");
                }
            }else{
                System.out.println("你是猪吗？目录不存在啊！请输入正确的目录路径！");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("你是猪吗？请输入正确的目录路径！");
        }
    }


    public static void sacnTxtFile(File directory) throws Exception{
        String path = directory.getPath();
        String[] fileNames = directory.list();
        for (String fileName: fileNames) {
            File file = new File(path + "\\" + fileName) ;
            if(file.isDirectory()){
                sacnTxtFile(file);
            }else if(file.isFile()){
                try{
                    if(fileName.indexOf("歌词") >= 0 && fileName.indexOf(".txt") >= 0){
                        String txtDirectory = FunctionView.getPath() + "\\歌词文本" ;
                        Files.copy(file.toPath(), new File(txtDirectory + "\\" + fileName).toPath());
                    }
                }catch (FileAlreadyExistsException e){

                }catch (Exception e){
                    System.out.println("复制文件（" + fileName + "）失败!："+e.getMessage());
                }
            }
        }
    }

}
