package com.rcircle.service.stream.services;

import com.rcircle.service.stream.events.source.HLSBean;
import com.rcircle.service.stream.utils.core.CommandCallback;
import com.rcircle.service.stream.utils.core.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class HLSService implements CommandCallback<HLSService.MateData> {
    private static final int FLAG_CREATE_HLS_FILES = 0;
    @Autowired
    private HLSBean hlsBean;
    @Autowired
    CommandExecutor commandExecutor;

    @Value("${ffmpeg-path}")
    private String ffmpeg_bin_path;

    public void createHLSFiles(int id, String srcfile, String dstpath, String baseurl) {
        MateData md = new MateData(FLAG_CREATE_HLS_FILES);
        File dstFile = new File(dstpath);
        File srcFile = new File(srcfile);
        if (!dstFile.exists()) {
            dstFile.mkdirs();
        }
        md.filename = srcFile.getName();
        md.id = id;
        List<String> cmd = new ArrayList<>();
        cmd.add(assembleAbsoluteFilePath(ffmpeg_bin_path, "ffmpeg"));
        cmd.add("-i");
        cmd.add(srcfile);
        cmd.add("-c:a");
        cmd.add("aac");
        cmd.add("-f");
        cmd.add("hls");
        if (baseurl != null) {
            md.url = assembleBaseUrl(baseurl, id, md.filename);
            cmd.add("-hls_base_url");
            cmd.add(md.url);
        }
        cmd.add("-bsf:v");
        cmd.add("h264_mp4toannexb");
        cmd.add("-hls_list_size");
        cmd.add("0");
        cmd.add("-vf");
        cmd.add("scale=1280:-1");
        cmd.add("-r");
        cmd.add("25");
        md.filepath = assembleAbsoluteFilePath(dstpath, "index.m3u8");
        cmd.add(md.filepath);
        commandExecutor.setCmd(cmd).asyncProcess(md, this);
    }

    public void sendHLSSplitFinished(int logid, String filename, boolean ret) {
        hlsBean.sendHLSSplitFinished(logid, filename, ret);
    }


    @Override
    public void preProcess(MateData flag) {

    }

    @Override
    public void processing(MateData flag, String ret) {

    }

    @Override
    public void afterProcess(MateData flag) {
        boolean result = false;
        switch (flag.type) {
            case FLAG_CREATE_HLS_FILES:
                result = checkHLSFilesCreatedResult(flag.filepath);
                sendHLSSplitFinished(flag.id, flag.filename, result);
                break;
        }
    }

    private boolean checkHLSFilesCreatedResult(String filepath) {
        File indexFile = new File(filepath);
        return indexFile.exists();
    }

    private String assembleBaseUrl(String baseurl, int id, String name) {
        if (baseurl.endsWith("/")) {
            baseurl = baseurl.substring(0, baseurl.length() - 1);
        }
        return String.format("%s/%d/%s", baseurl, id, name);
    }

    private String assembleAbsoluteFilePath(String filepath, String filename) {
        if (filepath.lastIndexOf(File.separatorChar) == filepath.length() - 1) {
            return filepath + filename;
        }
        return filepath + File.separatorChar + filename;
    }

    public class MateData {
        public int type;
        public String filename;
        public String url;
        public int id;
        public String filepath;

        public MateData(int type) {
            this.type = type;
        }
    }
}
