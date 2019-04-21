package com.rcircle.service.resource.utils;

import com.rcircle.service.resource.model.FileInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NetFile {

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
                errcode = ErrInfo.CODE_SAVE_NETFILE;
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
            errcode = ErrInfo.CODE_CREATE_FILE_STREAM;
        } catch (IOException e) {
            errcode = ErrInfo.CODE_SAVE_NETFILE;
            ;
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

    public static void deleteFiles(String root, List<String> files) {
        if (files != null) {
            for (String path : files) {
                File file = new File(root, path);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }
        }
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

    public static List<FileInfo> getFilesInfo(String dir) throws IOException {
        List<FileInfo> fileInfoList = null;
        FileInfo info = null;
        FileInputStream fis = null;
        File root = new File(dir);
        File[] files = root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        if (files != null) {
            for (File file : files) {
                if (fileInfoList == null) {
                    fileInfoList = new ArrayList<>();
                }
                info = new FileInfo();
                info.setName(file.getName());
                info.setSize(file.length());
                try {
                    fis = new FileInputStream(file);
                    info.setChecksum(DigestUtils.md2Hex(fis));
                    fis.close();
                } catch (IOException e) {
                    info.setErrinfo(e.getMessage());
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                    fis = null;
                }
                fileInfoList.add(info);
            }
        }
        return fileInfoList;
    }


}
