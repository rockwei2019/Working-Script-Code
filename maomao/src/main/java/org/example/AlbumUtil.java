package org.example;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.*;

public class AlbumUtil {

    public static void createMusicDirectroy() throws Exception {
        System.out.println("  ");
        System.out.println(">>   创建专辑目录   <<");
        System.out.println("  ");
        String path = FunctionView.getPath();
        String directroyPath = path + "\\" + "专辑" + "\\" ;
        File directroy = new File(directroyPath);
        FunctionView.existsAndMkdir(directroy);
        System.out.println("请输入歌手名称（回车确认）：");
        String geshou = new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("请输入歌曲名称（回车确认）：");
        String gequ = new BufferedReader(new InputStreamReader(System.in)).readLine();
        String mulu = directroyPath + geshou + "-" +gequ + "-EP资料\\" ;
        String directroyQz = mulu + geshou + "-" +gequ + "-" ;
        String directroy1 = directroyQz + "歌曲&歌词" ;
        String directroy2 = directroyQz + "歌手写真" ;
        String directroy3 = directroyQz +  "专辑介绍&封面" ;
        FunctionView.existsAndMkdir(new File(directroy1));
        FunctionView.existsAndMkdir(new File(directroy2));
        FunctionView.existsAndMkdir(new File(directroy3));
        System.out.print("专辑目录已经生成，速去\"专辑\"目录中查看!  （按回车返回功能列表）");
    }

    public static void autoGroup() throws Exception {
        System.out.println("  ");
        System.out.println(">>   扫描相关专辑文件并复制到对应的目录中   <<");
        System.out.println("  ");
//        System.out.println("请输入要扫描的目录（复制路径，使用鼠标右键可以黏贴，会自动扫描所有目录噢，回车确认）");
//        String scanning = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("正在分类...");
        String directroyPath = FunctionView.getPath() + "\\" + "专辑" + "\\" ;
        File directroy = new File(directroyPath);
        for (String epDirName: directroy.list()) {
            if(epDirName.indexOf("EP资料") >= 0){
                sacnZjFile(new File(directroyPath + epDirName), true);
            }
        }
        System.out.print("自动分类完成，速去\"专辑\"目录中查看!  （按回车返回功能列表）");
    }

    public static void sacnZjFile(File directory, boolean ifEP) throws Exception{
        String path = directory.getPath();
        String[] fileNames = directory.list();
        if(ifEP){
            String geshou = directory.getName().split("-")[0] ;
            String gequ = directory.getName().split("-")[1] ;
            String[] photoTypeArr = {".bmp", ".jpg", ".png", ".tif", ".gif", ".pcx", ".tga", ".exif", ".fpx", ".svg", ".psd", ".cdr", ".pcd", ".dxf", ".ufo", ".eps", ".ai", ".raw", ".WMF", ".webp"} ;
            String[] muiscTypeArr = {".mpeg", ".mp3", ".mpeg-4", ".map4", ".midi", ".wma", ".vqf", ".amr", ".wav"} ;
            List<String> photoTypeList = Arrays.asList(photoTypeArr);
            List<String> muiscTypeList = Arrays.asList(muiscTypeArr);
            String directroyPath = FunctionView.getPath() + "\\" + "专辑" + "\\" + geshou + "-" + gequ + "-EP资料\\" + geshou + "-" + gequ + "-" ;
            for (String fileName: fileNames) {
                try{
                    File file = new File(directory.getPath() + "\\" + fileName) ;
                    if(!file.isDirectory()){
                        String fileHouZui = fileName.substring(fileName.lastIndexOf(".")) ;
                        if(fileName.indexOf("专辑介绍") >= 0 || fileName.indexOf("封面") >= 0){
                            Files.copy(file.toPath(), new File(directroyPath + "专辑介绍&封面\\" + fileName).toPath());
                        }else if(fileName.indexOf("歌词") >= 0){
                            Files.copy(file.toPath(), new File(directroyPath + "歌曲&歌词\\" + fileName).toPath());
                        }else if(fileName.indexOf("个人简介") >= 0){
                            Files.copy(file.toPath(), new File(directroyPath + "歌手写真\\" + fileName).toPath());
                        }else if(photoTypeList.contains(fileHouZui)){
                            Files.copy(file.toPath(), new File(directroyPath + "歌手写真\\" + fileName).toPath());
                        }else if(muiscTypeList.contains(fileHouZui)){
                            Files.copy(file.toPath(), new File(directroyPath + "歌曲&歌词\\" + fileName).toPath());
                        }
                    }
                }catch (FileAlreadyExistsException e){
                    
                }catch (Exception e){
                    System.out.println("拷贝文件（"+fileName+"）出错："+e.getMessage());
                }
            }
        }else{
            for (String fileName: fileNames) {
                File file = new File(path + "\\" + fileName) ;
                if(file.isDirectory()){
                    if(fileName.indexOf("EP资料") >= 0){
                        sacnZjFile(file, true);
                    }else{
                        sacnZjFile(file, false);
                    }
                }else if(file.isFile()){

                }
            }
        }

    }

    public static void scanZJtxtFile() throws Exception {
        System.out.println("  ");
        System.out.println(">>   扫描专辑txt文件并复制到专辑目录   <<");
        System.out.println("  ");

        FunctionView.existsAndMkdir(new File(FunctionView.getPath() + "\\专辑文本")) ;

        System.out.println("请输入要扫描的目录（复制路径，使用鼠标右键可以黏贴，会自动扫描所有目录噢，回车确认）");
        String scanning = new BufferedReader(new InputStreamReader(System.in)).readLine();

        try{
            File file = new File(scanning);
            if(file.exists()){
                if(file.isDirectory()){
                    sacnTxtFile(file);

                    System.out.print("扫描完成，速去专辑目录中查看哦。  （按回车返回功能列表）");
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
                    if(fileName.indexOf("专辑介绍") >= 0 && fileName.indexOf(".txt") >= 0){
                        String txtDirectory = FunctionView.getPath() + "\\专辑文本" ;
                        Files.copy(file.toPath(), new File(txtDirectory + "\\" + fileName).toPath());
                    }
                }catch (FileAlreadyExistsException e){

                }catch (Exception e){
                    System.out.println("复制文件（" + fileName + "）失败!："+e.getMessage());
                }
            }
        }
    }

    public static void txtToExcel() throws Exception{
        System.out.println("  ");
        System.out.println(">>   专辑txt文件转excel文件   <<");
        System.out.println("  ");

        System.out.println("请输入Excel文件的名称（回车确认）：");
        String excelFileName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        String path = FunctionView.getPath();
        FunctionView.existsAndMkdir(new File(path + "\\" + "excel"));

        File copyFile = new File(path + "\\excel\\" + excelFileName + ".xlsx");

        try(InputStream inputStream = AlbumUtil.class.getResourceAsStream("/A.xlsx")){
            Files.copy(inputStream, copyFile.toPath());
        }catch (FileAlreadyExistsException e){
            System.out.print("文件（" + excelFileName + "）已经存在，快去检查!（按回车返回功能列表）");
            return ;
        }

        String txtPath = path + "\\" + "专辑文本" + "\\" ;
        System.out.println("开始扫描txt文件.....");
        //文本文件目录
        File txtDirectory = new File(txtPath);

        if(txtDirectory.isDirectory()) {
            String[] fileNameArray = txtDirectory.list();
            System.out.println(" ");
            List<Map<String, String>> musicInfoList = new ArrayList<>(fileNameArray.length);
            for (int i = 0; i < fileNameArray.length; i++) {
                Map content = readTxtFileContent(txtPath + fileNameArray[i]);
                if (content != null) {
                    musicInfoList.add(content);
                }
            }
            writeExcel(copyFile, musicInfoList);
        }
        System.out.println(" ");
        System.out.print("Excel文件已经生成，速去excel目录中查看!记得核对一下噢(⊙o⊙)。  （按回车返回功能列表）");
    }

    public static void writeExcel(File copyFile, List<Map<String, String>> maps) {
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
            style.setWrapText(true);

            for (Map<String, String> musicInfo : maps) {
                XSSFRow row = sheet.getRow(rowNum) ;
                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(rowNum + "");
                row.getCell(0).setCellStyle(style);

                if(musicInfo.get("musicName") != null){
                    row.createCell(1).setCellValue(musicInfo.get("musicName"));
                    row.getCell(1).setCellStyle(style);
                }else{
                    row.createCell(1).setCellStyle(style);
                }

                if(musicInfo.get("geshou") != null){
                    row.createCell(2).setCellValue(musicInfo.get("geshou"));
                    row.getCell(2).setCellStyle(style);
                }else{
                    row.createCell(2).setCellStyle(style);
                }
                if(musicInfo.get("schools") != null){
                    row.createCell(3).setCellValue(musicInfo.get("schools"));
                    row.getCell(3).setCellStyle(style);
                }else{
                    row.createCell(3).setCellStyle(style);
                }
                if(musicInfo.get("language") != null){
                    row.createCell(4).setCellValue(musicInfo.get("language"));
                    row.getCell(4).setCellStyle(style);
                }else{
                    row.createCell(4).setCellStyle(style);
                }
                if(musicInfo.get("time") != null){
                    row.createCell(5).setCellValue(musicInfo.get("time"));
                    row.getCell(5).setCellStyle(style);
                }else{
                    row.createCell(5).setCellStyle(style);
                }
                if(musicInfo.get("gongsi") != null){
                    row.createCell(6).setCellValue(musicInfo.get("gongsi"));
                    row.getCell(6).setCellStyle(style);
                }else{
                    row.createCell(6).setCellStyle(style);
                }
                if(musicInfo.get("type") != null){
                    row.createCell(7).setCellValue(musicInfo.get("type"));
                    row.getCell(7).setCellStyle(style);
                }else{
                    row.createCell(7).setCellStyle(style);
                }
                if(musicInfo.get("jieshao") != null){
                    row.createCell(8).setCellValue(new XSSFRichTextString(musicInfo.get("jieshao")));
                    row.getCell(8).setCellStyle(style);
                }else{
                    row.createCell(8).setCellStyle(style);
                }
                if(musicInfo.get("qumu") != null){
                    row.createCell(9).setCellValue(new XSSFRichTextString(musicInfo.get("qumu")));
                    row.getCell(9).setCellStyle(style);
                }else{
                    row.createCell(9).setCellStyle(style);
                }
                if( musicInfo.get("jieshao").split("\n").length >  musicInfo.get("qumu").split("\n").length){
                    row.setHeight((short) (musicInfo.get("jieshao").split("\n").length * 333));
                }else{
                    row.setHeight((short) (musicInfo.get("qumu").split("\n").length * 333));
                }
                rowNum ++ ;
            }
            workbook.write(out);
        }catch (Exception e){
            System.out.println("第"+rowNum+"行出错!:");
            e.printStackTrace();
        }
    }

    public static Map readTxtFileContent(String txtFilepath) throws Exception {
        String musicName = null ;  String schools = null ; String language = null ; String type = null ;
        String time = null ; String geshou = null ; String gongsi = null ;
        StringBuffer jieshao = new StringBuffer() ; StringBuffer qumu = new StringBuffer()  ;
        File txtfile = new File(txtFilepath);
        String fileName = txtfile.getName().replace(".txt", ""); ;
        String code = null ;
        code = FunctionView.getCode(txtfile);
        if("UTF-8_BOM".equals(code)){
            code = "UTF-8" ;
        }
        String fenduan = null ;
        try(BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtfile), code))){
            String line = null ;
            while ((line = bfReader.readLine()) != null){
                //冒号的位置
                if(line.trim().length() <= 0){
                    continue ;
                }
                if(musicName != null){
                    if(fenduan == null) {
                        if(line.indexOf("歌手：") >= 0){
                            geshou = line.substring(line.indexOf("歌手：") + 3) ;
                        }else if(line.indexOf("歌手:") >= 0){
                            geshou = line.substring(line.indexOf("歌手:") + 3) ;
                        }

                        if(line.indexOf("流派：") >= 0){
                            schools = line.substring(line.indexOf("流派：") + 3) ;
                        }else if(line.indexOf("流派:") >= 0){
                            schools = line.substring(line.indexOf("流派:") + 3) ;
                        }

                        if(line.indexOf("语言：") >= 0){
                            language = line.substring(line.indexOf("语言：") + 3) ;
                        }else if(line.indexOf("语言:") >= 0){
                            language = line.substring(line.indexOf("语言:") + 3) ;
                        }

                        if(line.indexOf("发行时间：") >= 0){
                            time = line.substring(line.indexOf("发行时间：") + 5) ;
                        }else if(line.indexOf("发行时间:") >= 0){
                            time = line.substring(line.indexOf("发行时间:") + 5) ;
                        }

                        if(line.indexOf("唱片公司：") >= 0){
                            gongsi = line.substring(line.indexOf("唱片公司：") + 5) ;
                        }else if(line.indexOf("唱片公司:") >= 0){
                            gongsi = line.substring(line.indexOf("唱片公司:") + 5) ;
                        }

                        if(line.indexOf("类型：") >= 0){
                            type = line.substring(line.indexOf("类型：") + 3) ;
                        }else if(line.indexOf("类型:") >= 0){
                            type = line.substring(line.indexOf("类型:") + 3) ;
                        }
                        if(line.indexOf("专辑介绍：") >= 0){
                            fenduan = "js" ;
                        }else if(line.indexOf("专辑介绍:") >= 0){
                            fenduan = "js" ;
                        }

                        if(line.indexOf("曲目：") >= 0){
                            fenduan = "qm" ;
                        }else if(line.indexOf("曲目:") >= 0){
                            fenduan = "qm" ;
                        }

                    }else{
                        if(line.indexOf("专辑介绍：") >= 0){
                            fenduan = "js" ;continue;
                        }else if(line.indexOf("专辑介绍:") >= 0){
                            fenduan = "js" ;continue;
                        }
                        if(line.indexOf("曲目：") >= 0){
                            fenduan = "qm" ;continue;
                        }else if(line.indexOf("曲目:") >= 0){
                            fenduan = "qm" ;continue;
                        }
                        if("js".equals(fenduan)){
                            jieshao.append(line).append("\r\n");
                        }else if("qm".equals(fenduan)){
                            qumu.append(line).append("\r\n");
                        }
                    }

                }else{
                    if(line.trim().length() > 0){
                        musicName = line ;
                    }
                }
            }
            Map<String, String> musicMap = new HashMap<>();
            musicMap.put("musicName", musicName) ;
            musicMap.put("geshou", geshou) ;
            musicMap.put("schools", schools) ;
            musicMap.put("language", language) ;
            musicMap.put("time", time) ;
            musicMap.put("gongsi", gongsi) ;
            musicMap.put("type", type) ;
            musicMap.put("jieshao", "=clean("+jieshao.toString()+")") ;
            musicMap.put("qumu", "=clean("+qumu.toString()+")") ;
            return musicMap ;
        }catch (Exception e){
            System.out.println("读取文件<" + fileName + ">出错：" + e.getMessage());
            e.printStackTrace();
        }
        return null ;
    }

}
