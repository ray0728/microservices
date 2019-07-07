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
            } else {
                fis.close();
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

    public static int saveSplitFile(String root, String filename,
                                    long index, long total, String checksum,
                                    int chunksize, MultipartFile srcfile) throws IOException {
        int errcode = 0;
        FileInputStream fis = null;
        File targetDir = new File(root);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File tmpFile = new File(targetDir, filename + ".cache");
        tmpFile.deleteOnExit();
        try {
            srcfile.transferTo(tmpFile);
            fis = new FileInputStream(tmpFile);
            if (!DigestUtils.md5Hex(fis).equals(checksum)) {
                errcode = ResultInfo.CODE_CHECK_SUM;
            } else {
                fis.close();
                RandomAccessFile dstFile = new RandomAccessFile(getDirAbsolutePath(root, "Unconfirmed " + filename + ".download"), "rw");
                dstFile.seek(index * chunksize);
                fis = new FileInputStream(tmpFile);
                byte[] buffer = new byte[4096];
                int length = 0;
                while ((length = fis.read(buffer)) != -1) {
                    dstFile.write(buffer, 0, length);
                }
                dstFile.close();
            }
        } catch (FileNotFoundException e) {
            errcode = ResultInfo.CODE_OPEN_FILE;
        } catch (IOException e) {
            errcode = ResultInfo.CODE_SAVE_FILE;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        if (index + 1 == total || errcode != 0) {
            tmpFile.delete();
            File file = new File(getDirAbsolutePath(root, "Unconfirmed " + filename + ".download"));
            file.renameTo(new File(getDirAbsolutePath(root, filename)));
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
