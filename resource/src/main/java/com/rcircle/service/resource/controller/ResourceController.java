package com.rcircle.service.resource.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.resource.service.AccountService;
import com.rcircle.service.resource.service.ResourceService;
import com.rcircle.service.resource.utils.ErrInfo;
import com.rcircle.service.resource.utils.NetFile;
import com.rcircle.service.resource.utils.SimpleDate;
import com.rcircle.service.resource.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/res")
public class ResourceController {
    @Value("${rource.upload.dir.root}")
    private String saveDirRoot;

    @Value("${label.upload.files}")
    private String uploadFileLabel;

    @Resource
    private ResourceService resourceService;
    @Resource
    private AccountService accountService;

    private Account getOpAccount(Principal principal) {
        return accountService.loadUserByUsername(principal.getName());
    }

    private List<Group> getAllGroupBelongOpAccount() {
        String info = accountService.getGroupList();
        if (info.equals("Invalid resources.")) {
            return null;
        }
        List<Group> groupList = JSON.parseArray(info, Group.class);
        if (groupList == null || groupList.isEmpty()) {
            return null;
        }
        return groupList;
    }

    @PostMapping("new")
    public String createNewLog(Principal principal,
                               @RequestParam(name = "title", required = true) String title,
                               @RequestParam(name = "type", required = true) String type,
                               @RequestParam(name = "gid", required = false, defaultValue = "0") int gid,
                               @RequestParam(name = "tags", required = false, defaultValue = "") String[] tags) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CREATE_NEW, "Invalid request parameters.");
        }
        Category category = resourceService.createNewCategory(account.getUid(), type);
        Log log = new Log();
        log.setTitle(title);
        log.setType(category.getCid());
        log.setGid(gid);
        log.setUid(account.getUid());
        log.setDate(SimpleDate.getUTCTime());
        for (String desc : tags) {
            Tag tag = new Tag();
            tag.setDesc(desc);
            log.addTag(tag);
        }
        resourceService.createLog(log);
        return String.valueOf(log.getId());
    }

    @PostMapping("update")
    public String updateLog(Principal principal,
                            @RequestParam(name = "id", required = true) int id,
                            @RequestParam(name = "title", required = false, defaultValue = "") String title,
                            @RequestParam(name = "type", required = false, defaultValue = "0") int type,
                            @RequestParam(name = "gid", required = false, defaultValue = "0") int gid,
                            @RequestParam(name = "log", required = false, defaultValue = "") String htmllog) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_UPDATE_RES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_UPDATE_RES, "You don't have permission to access.");
        }
        if (!title.isEmpty()) {
            log.setTitle(title);
        }

        if (type != 0) {
            log.setType(type);
        }
        if (gid != 0) {
            log.setGid(gid);
        }
        resourceService.changeLog(log);
        if (!htmllog.isEmpty()) {
            log.getDetial().setLog(htmllog);
            resourceService.changeLogDetail(log);
        }
        return JSONObject.toJSONString(log);
    }

    @DeleteMapping("delete")
    public String deleteLog(Principal principal,
                            @RequestParam(name = "id", required = true) int id) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_RES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_DELETE_RES, "You don't have permission to access.");
        }
        resourceService.deleteLog(log);
        return "";
    }


    @PostMapping("upload")
    public String uploadSplitFile(Principal principal, MultipartFile file,
                                  @RequestParam(name = "id", required = true) int id,
                                  @RequestParam(name = "name", required = true) String filename,
                                  @RequestParam(name = "index", required = true) int index,
                                  @RequestParam(name = "total", required = true) int total,
                                  @RequestParam(name = "chunksize") int chunkSize,
                                  @RequestParam(name = "checksum", required = true) String checksum) {
        String result = "";
        Account account = getOpAccount(principal);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_UPLOAD_RES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_UPLOAD_RES, "You don't have permission to access.");
        }

//        if (files.isEmpty()) {
//            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_UPLOAD_RES, "There are no files here.");
//        } else if (files.size() > 1) {
//            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_UPLOAD_RES, "There are too many files here.");
//        }
        String saveDir = log.getDetial().getRes_url();
        //NetFile.getDirAbsolutePath(saveDirRoot, String.valueOf(account.getUid()), String.valueOf(id));
        try {
            int err = NetFile.saveSplitFile(saveDir, filename, index, total, checksum, chunkSize, file);
            if (err != 0) {
                result = ErrInfo.assembleJson(ErrInfo.ErrType.MISMATCH, err, "checksum mismatch.");
            }
        } catch (IOException e) {
            result = ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_UPLOAD_RES, e.getMessage());
        }
        return result;
    }

    @DeleteMapping("files")
    public String deleteFile(Principal principal,
                             @RequestParam(name = "id", required = true) int id,
                             @RequestParam(name = "name", required = false, defaultValue = "all") String filesname) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_RES_FILES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_DELETE_RES_FILES, "You don't have permission to access.");
        }
        if (log == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_RES_FILES, "There are no logfile existes.");
        }
        NetFile.deleteFiles(log.getDetial().getRes_url(), Arrays.asList(filesname.split(";")));
        return "";
    }

    @PostMapping("reply")
    public String createNewReply(Principal principal,
                                 @RequestParam(name = "id", required = true) int id,
                                 @RequestParam(name = "msg", required = true) String msg) {
        Account account = getOpAccount(principal);
        Log log = resourceService.getLog(id);
        if (account == null || log == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_CREATE_REPLY, "Invalid request parameters.");
        }
        Reply reply = new Reply();
        reply.setDate(SimpleDate.getUTCTime());
        reply.setDesc(msg);
        reply.setLid(id);
        reply.setUid(account.getUid());
        resourceService.createReply(reply);
        return JSONObject.toJSONString(reply);
    }

    @DeleteMapping("reply")
    public String deleteReply(Principal principal, int lid, int rid) {
        Account account = getOpAccount(principal);
        Log log = resourceService.getLog(lid);
        if (account == null || log == null) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_DELETE_REPLY, "Invalid request parameters.");
        }
        for (Reply reply : log.getReplyList()) {
            if (reply.getId() != rid) {
                continue;
            }
            if (reply.getUid() == account.getUid() || log.getUid() == account.getUid()) {
                resourceService.deleteReply(reply);
            }
            break;
        }
        return "";
    }

    @GetMapping("replies")
    public String getAllReply(@RequestParam(name = "id", required = true) int id) {
        List<Reply> replies = resourceService.getReplies(id);
        return JSONObject.toJSONString(replies);
    }

    @GetMapping("list")
    public String getResource(Principal principal,
                              @RequestParam(name = "type", required = false, defaultValue = "0") int type,
                              @RequestParam(name = "gid", required = false, defaultValue = "0") int gid,
                              @RequestParam(name = "title", required = false, defaultValue = "") String title,
                              @RequestParam(name = "status", required = false, defaultValue = "0") int status,
                              @RequestParam(name = "offset", required = false, defaultValue = "0")int offset,
                              @RequestParam(name="count", required = false, defaultValue = "5")int count
    ) {
        int uid = 0;
        if (principal != null) {
            Account account = getOpAccount(principal);
            if (account != null) {
                uid = account.getUid();
            }
        }
        List<Log> logs = resourceService.getLogs(uid, type, gid, title, status, offset, count);
        return JSONObject.toJSONString(logs);
    }

    @GetMapping("img/{lid}/{name}")
    @ResponseBody
    public ResponseEntity getImageFile(Principal principal, @PathVariable("lid") Integer logid, @PathVariable("name") String name) {
        int uid = 0;
        String errinfo = null;
        Account account;
        if (principal != null) {
            account = getOpAccount(principal);
            if (account != null) {
                uid = account.getUid();
            }
        }
        Log log = resourceService.getLog(logid);
        if(log.getGid()!=0 && uid != 0 && !isBelongGid(log.getGid())){
            errinfo = ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_GET_RES_FILES, "You don't have permission to access.");
        }
        if(log.getGid() != 0 &&  uid == 0){
            errinfo =  ErrInfo.assembleJson(ErrInfo.ErrType.INVALID, ErrInfo.CODE_GET_RES_FILES, "You don't have permission to access.");
        }
        if(errinfo == null) {
            try {
                Iterator<FileInfo> iter = log.getDetial().getFiles().iterator();
                while(iter.hasNext()){
                    FileInfo fileInfo = iter.next();
                    if(fileInfo.getMime() == null || !fileInfo.getMime().toLowerCase().startsWith("image")){
                        continue;
                    }
                    if(fileInfo.getName().equals(name)){
                        MediaType mediaType = MediaType.parseMediaType(fileInfo.getMime());
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(mediaType);
                        File file = new File(fileInfo.getPath());
                        FileInputStream inputStream = new FileInputStream(file);
                        byte[] bytes = new byte[inputStream.available()];
                        inputStream.read(bytes, 0, inputStream.available());
                        inputStream.close();
                        return new ResponseEntity(bytes, headers, HttpStatus.OK);
                    }
                }
            } catch (Exception e) {
                errinfo = e.getMessage();
            }
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    private boolean isBelongGid(int gid) {
        List<Group> groupList = getAllGroupBelongOpAccount();
        if (groupList == null || gid == 0) {
            return false;
        }
        for (Group group : groupList) {
            if (group.getGid() == gid) {
                return true;
            }
        }
        return false;
    }

}
