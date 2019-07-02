package com.rcircle.service.resource.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

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
                errcode = ResultInfo.CODE_SAVE_NETFILE;
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
            errcode = ResultInfo.CODE_CREATE_FILE_STREAM;
        } catch (IOException e) {
            errcode = ResultInfo.CODE_SAVE_NETFILE;
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

    public static void deleteDir(File root) {
        if (root.isDirectory()) {
            String[] children = root.list();
            File child;
            for (int i = 0; i < children.length; i++) {
                child = new File(root, children[i]);
                if (child.isDirectory()) {
                    deleteDir(child);
                } else {
                    child.delete();
                }
            }
        }
        root.delete();
    }

    public static void deleteFilesExist(String path) {
        File root = new File(path);
        if (root.isDirectory()) {
            File[] files = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isFile()) {
                        String filename = pathname.getName();
                        return (filename.startsWith("Unconfirmed") && filename.endsWith(".download")) || filename.endsWith(".cache");
                    }
                    return false;
                }
            });
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        } else {
            root.delete();
        }
    }

    public static String translateToUrlFormat(String str) {
        str = str.replace("+", "%2B");
        str = str.replaceAll(" ", "%20");
        str = str.replaceAll("/", "%2F");
        str = str.replace("?", "%3F");
        str = str.replaceAll("%", "%25");
        str = str.replaceAll("#", "%23");
        str = str.replaceAll("&", "%26");
        str = str.replaceAll("=", "%3D");
        return str;
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

    public static Map<String, String> getFilesInfo(String dir, String subdir) {
        Map<String, String> fileInfoList = null;
        File root = new File(dir, subdir);
        File[] files = root.listFiles(pathname -> pathname.isFile());
        if (files != null) {
            for (File file : files) {
                if (fileInfoList == null) {
                    fileInfoList = new HashMap<>();
                }
                fileInfoList.putIfAbsent(file.getName(), file.getAbsolutePath());
            }
        }
        return fileInfoList;
    }

    public static String translateLocalVideoFileToHlsFile(String path) {
        File file = new File(path);
        return String.format("%shls%sindex.m3u8", file.getParent() + File.separatorChar,
                File.separatorChar + file.getName() + File.separatorChar);
    }

    public static String translateLocalVideoFileToTsFile(String path, String tsname) {
        File file = new File(path);
        return String.format("%shls%s%s", file.getParent() + File.separatorChar,
                File.separatorChar + file.getName() + File.separatorChar, tsname);
    }


}
