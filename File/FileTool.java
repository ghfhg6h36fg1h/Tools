package com.liangxin.platform.common.tools;

import com.liangxin.platform.common.entity.tax.outputParam.IsUpload;
import com.liangxin.platform.common.entity.tax.outputParam.IsUploadThumb;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

public class FileTool {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * 02前缀为个税上传的图片标识
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName,int sid,String empno){
        return "02" + "_" + String.valueOf(System.currentTimeMillis()) + "_" + empno + "_" + sid + getSuffix(fileOriginName);
    }

    /**
     *
     * @param file 文件
     * @param path   文件存放路径
     * @param fileName 原文件名
     * @return
     */
    public static IsUpload upload(MultipartFile file, String path, String fakePath, String fileName , int sid, String empno){

        IsUpload isUpload = new IsUpload();
        // 生成新的文件名
        String randFileName = getFileName(fileName,sid,empno);
        String realPath = path + "/" + randFileName;
        String storagePath = fakePath + randFileName;
        isUpload.setPicRealPath(realPath);
        isUpload.setPicFakePath(storagePath);
        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            isUpload.setSuccess(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            isUpload.setSuccess(false);
        } catch (IOException e) {
            e.printStackTrace();
            isUpload.setSuccess(false);
        }
        return isUpload;
    }


    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 依据图片路径生成缩略图 </p>
     * @param imagePath    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制依照宽高生成缩略图(假设为false，则生成最佳比例缩略图)
     */
    public static IsUploadThumb thumbnailImage(String imagePath, String fakePath, int w, int h, String prevfix, boolean force){
        IsUploadThumb isUploadThumb = new IsUploadThumb();
        File imgFile = new File(imagePath);
        if(imgFile.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }
                // 类型和图片后缀所有小写，然后推断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                    isUploadThumb.setSuccess(false);
                }
                Image img = ImageIO.read(imgFile);
                if(!force){
                    // 依据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                String pathName = p.substring(0,p.lastIndexOf(File.separator)) + File.separator + prevfix +imgFile.getName();
                // 将图片保存在原文件夹并加上前缀
                ImageIO.write(bi, suffix, new File(pathName));
                isUploadThumb.setSuccess(true);
                isUploadThumb.setThumbnailPath(fakePath + prevfix +imgFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
                isUploadThumb.setSuccess(false);
            }
        }else{
            isUploadThumb.setSuccess(false);
        }
        return isUploadThumb;
    }

    }


