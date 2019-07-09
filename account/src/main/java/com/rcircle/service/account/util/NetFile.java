package com.rcircle.service.account.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class NetFile {

    public static int saveFileFromNet(String root, String filename, String checksum, MultipartFile srcfile) throws IOException {
        int errcode = 0;
        FileInputStream fis = null;
        File targetDir = new File(root);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File dstFile = new File(targetDir, filename);
        dstFile.deleteOnExit();
        try{
            srcfile.transferTo(dstFile);
            fis = new FileInputStream(dstFile);
            if (!DigestUtils.md5Hex(fis).equals(checksum)) {
                errcode = ResultInfo.CODE_CHECK_SUM;
            }
        }catch (IOException e){
            errcode = ResultInfo.CODE_SAVE_FILE;
        }finally {
            if (fis != null) {
                fis.close();
            }
        }
        return errcode;
    }

    public static String getDirAbsolutePath(String... dirs) {
        String absolutePath = "";
        for (String dir : dirs) {
            if (absolutePath.isEmpty()) {
                absolutePath = dir;
            } else if (dir.indexOf(File.separatorChar) != 0) {
                absolutePath += File.separatorChar + dir;
            } else {
                absolutePath += dir;
            }

            if (absolutePath.lastIndexOf(File.separatorChar) == absolutePath.length() - 1) {
                absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
            }
        }
        return absolutePath;
    }

}
