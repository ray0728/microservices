package com.ray.service.store.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ray.service.store.clients.RemoteAccountFeignClient;
import com.ray.service.store.model.Account;
import com.ray.service.store.model.FileInfo;
import com.ray.service.store.model.LogFile;
import com.ray.service.store.model.ReplyLog;
import com.ray.service.store.service.ResourceService;
import com.ray.service.store.util.ErrInfo;
import com.ray.service.store.util.NetFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class ResourceController {
    @Value("${rource.upload.dir.root}")
    private String saveDirRoot;

    @Value("#{'${spring.servlet.multipart.max-file-size}'.replace('MB','')}")
    private int chunkSize;

    @Value("${label.upload.files}")
    private String uploadFileLabel;

    @Resource
    private ResourceService resourceService;

    @Autowired
    private RemoteAccountFeignClient remoteAccountFeignClient;

    private Account getCurrentAccount(Principal principal) {
        String accountinfo = remoteAccountFeignClient.getInfo(principal.getName());
        return JSON.parseArray(accountinfo, Account.class).get(0);
    }

    @PostMapping("new")
    public String commitNewLog(Principal principal,
                               @RequestParam(name = "title", required = false) String title,
                               @RequestParam(name = "desc", required = false) String desc,
                               @RequestParam(name = "extend", required = false) String extend) {
        LogFile log = resourceService.createLog(getCurrentAccount(principal), title, desc, extend);
        return JSONObject.toJSONString(log);
    }

    @PutMapping("update")
    public String updateLog(Principal principal,
                            @RequestParam(name = "id", required = true) long id,
                            @RequestParam(name = "title", required = false) String title,
                            @RequestParam(name = "desc", required = false) String desc,
                            @RequestParam(name = "extend", required = false) String extend) {
        Account account = getCurrentAccount(principal);
        List<LogFile> logFileList = resourceService.getAllCreatedLogFileBy(account.getUid(), id);
        if (logFileList == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_LOGFILE, "there are no logfile existes.");
        }
        LogFile logFile = logFileList.get(0);
        if (title != null) {
            logFile.setTitle(title);
        }
        if (desc != null) {
            logFile.getDetial().setShortDesc(desc);
        }
        if (extend != null) {
            logFile.getDetial().setExtendUrl(extend);
        }
        if (resourceService.updateLog(logFile) == 0) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_UPDATE_LOGFILE, "update failed.");
        } else {
            return JSONObject.toJSONString(logFile);
        }
    }

    @DeleteMapping("delete")
    public String deleteLog(Principal principal,
                            @RequestParam(name = "id", required = true) long id) {
        Account account = getCurrentAccount(principal);
        List<LogFile> logFileList = resourceService.getAllCreatedLogFileBy(account.getUid(), id);
        if (logFileList == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_LOGFILE, "there are no logfile existes.");
        }
        resourceService.deleteLog(logFileList.get(0));
        return "";
    }

    @PostMapping("/files/upload")
    public String uploadSplitFile(Principal principal, HttpServletRequest request,
                                  @RequestParam(name = "id", required = true) long id,
                                  @RequestParam(name = "name", required = true) String filename,
                                  @RequestParam(name = "index", required = true) long index,
                                  @RequestParam(name = "total", required = true) long total,
                                  @RequestParam(name = "checksum", required = true) String checksum) {
        String result = "";
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(uploadFileLabel);
        if (files.isEmpty()) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_SAVE_NETFILE, "there are no files here.");
        } else if (files.size() > 1) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_SAVE_NETFILE, "there are too many files here.");
        }
        Account account = getCurrentAccount(principal);
        String saveDir = NetFile.getDirAbsolutePath(saveDirRoot, String.valueOf(account.getUid()), String.valueOf(id));
        try {
            int err = NetFile.saveSplitFile(saveDir, filename, index, total, checksum, chunkSize * 1024 * 1024, files.get(0));
            if (err == 0) {
                resourceService.updateResourceFiles(account, id, filename);
            } else {
                result = ErrInfo.assembleJson(ErrInfo.ErrType.MISMATCH, err, "checksum mismatch.");
            }
        } catch (IOException e) {
            result = ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_SAVE_NETFILE, e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/files/delete")
    public String deleteFile(Principal principal,
                             @RequestParam(name = "id", required = true) long logid,
                             @RequestParam(name = "files", required = false, defaultValue = "all") String filesname) {
        Account account = getCurrentAccount(principal);
        List<LogFile> logFileList = resourceService.getAllCreatedLogFileBy(account.getUid(), logid);
        if (logFileList == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_LOGFILE, "there are no logfile existes.");
        }
        NetFile.deleteFiles(logFileList.get(0).getDetial().getMultMediaDirRoot(), Arrays.asList(filesname.split(";")));
        return "";
    }

    @GetMapping("/files/info")
    public String getAllFileInfo(Principal principal,
                                 @RequestParam(name = "id", required = true) long logid) {
        List<FileInfo> fileInfoList = null;
        Account account = getCurrentAccount(principal);
        List<LogFile> logFileList = resourceService.getAllCreatedLogFileBy(account.getUid(), logid);
        if (logFileList == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_LOGFILE, "there are no logfile existes.");
        }
        try {
            fileInfoList = NetFile.getFilesInfo(logFileList.get(0).getDetial().getMultMediaDirRoot());
        } catch (IOException e) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_GET_FILES, e.getMessage());
        }
        return JSONObject.toJSONString(fileInfoList);
    }

    @PostMapping("reply")
    public String createNewReply(Principal principal,
                                 @RequestParam(name = "id", required = true) long logid,
                                 @RequestParam(name = "msg", required = true) String msg) {
        ReplyLog replyLog = resourceService.createReply(getCurrentAccount(principal), logid, msg);
        return JSONObject.toJSONString(replyLog);
    }

    @DeleteMapping("reply")
    public String deleteReply(Principal principal, long logid, long rid) {
        Account account = getCurrentAccount(principal);
        List<LogFile> logFileList = resourceService.getAllCreatedLogFileBy(0, logid);
        if (logFileList == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_LOGFILE, "there are no logfile existes.");
        }
        resourceService.deleteReply(logFileList.get(0), account.getUid(), rid);
        return "";
    }

    @GetMapping("reply")
    public String getAllReply(@RequestParam(name = "id", required = true) long logid) {
        List<ReplyLog> replyLogList = resourceService.queryLogReplyByLogId(logid);
        return JSONObject.toJSONString(replyLogList);
    }

    @GetMapping("list")
    public String getAllResource(Principal principal) {
        List<LogFile> logFileList = resourceService.getAllCreatedLogFileBy(getCurrentAccount(principal).getUid(), 0);
        return JSONObject.toJSONString(logFileList);
    }

}
