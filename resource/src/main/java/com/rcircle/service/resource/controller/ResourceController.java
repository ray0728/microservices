package com.rcircle.service.resource.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.resource.service.AccountService;
import com.rcircle.service.resource.service.ResourceService;
import com.rcircle.service.resource.service.StreamService;
import com.rcircle.service.resource.utils.ResultInfo;
import com.rcircle.service.resource.utils.NetFile;
import com.rcircle.service.resource.utils.SimpleDate;
import com.rcircle.service.resource.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    @Resource
    private StreamService streamService;

    private Account getOpAccount(Principal principal) {
        if (principal != null) {
            return accountService.loadUser(0, principal.getName());
        }
        return null;
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
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_CREATE_NEW, "Invalid request parameters.");
        }
        Log log = new Log();
        log.setTitle(title);
        log.setCategory(type);
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
                            @RequestParam(name = "type", required = false, defaultValue = "") String category,
                            @RequestParam(name = "gid", required = false, defaultValue = "0") int gid,
                            @RequestParam(name = "log", required = false, defaultValue = "") String htmllog) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_UPDATE_RES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_UPDATE_RES, "You don't have permission to access.");
        }
        boolean shouldUpdate = false;
        if (!title.isEmpty()) {
            log.setTitle(title);
            shouldUpdate = true;
        }

        if (!category.isEmpty()) {
            log.setCategory(category);
            shouldUpdate = true;
        }
        if (gid != 0) {
            log.setGid(gid);
            shouldUpdate = true;
        }
        if (shouldUpdate) {
            resourceService.changeLog(log);
        }
        if (!htmllog.isEmpty()) {
            if (log.getDetail() == null) {
                resourceService.createLogDetail(log);
            }
            log.getDetail().setLog(htmllog);
            resourceService.changeLogDetail(log);
        }
        return JSONObject.toJSONString(log);
    }

    @DeleteMapping("delete")
    public String deleteLog(Principal principal,
                            @RequestParam(name = "id", required = true) int id) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_DELETE_RES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_DELETE_RES, "You don't have permission to access.");
        }
        resourceService.deleteLog(log);
        return "";
    }

    @PostMapping("cover/{lid}/{name}")
    public String uploadCoverFile(Principal principal, MultipartFile file,
                                  @PathVariable("lid") int id,
                                  @PathVariable("name") String filename,
                                  @RequestParam(name = "index", required = true) int index,
                                  @RequestParam(name = "total", required = true) int total,
                                  @RequestParam(name = "chunksize") int chunkSize,
                                  @RequestParam(name = "checksum", required = true) String checksum) {
        Log log = resourceService.getLog(id);
        String ret = verifyAccount(principal, log, ResultInfo.CODE_UPLOAD_RES, false);
        if (ret != null) {
            return ret;
        }
        return saveClientFile(log, "cover", filename, index, total, checksum, chunkSize, file);
    }

    @PostMapping("img/{lid}/{name}")
    public String uploadImageFile(Principal principal, MultipartFile file,
                                  @PathVariable("lid") int id,
                                  @PathVariable("name") String filename,
                                  @RequestParam(name = "index", required = true) int index,
                                  @RequestParam(name = "total", required = true) int total,
                                  @RequestParam(name = "chunksize") int chunkSize,
                                  @RequestParam(name = "checksum", required = true) String checksum) {

        Log log = resourceService.getLog(id);
        String ret = verifyAccount(principal, log, ResultInfo.CODE_UPLOAD_RES, false);
        if (ret != null) {
            return ret;
        }
        return saveClientFile(log, "img", filename, index, total, checksum, chunkSize, file);
    }


    @PostMapping("video/{lid}/{name}")
    public String uploadVideoFile(Principal principal, MultipartFile file,
                                  @PathVariable("lid") int id,
                                  @PathVariable("name") String filename,
                                  @RequestParam(name = "index", required = true) int index,
                                  @RequestParam(name = "total", required = true) int total,
                                  @RequestParam(name = "chunksize") int chunkSize,
                                  @RequestParam(name = "checksum", required = true) String checksum) {
        Log log = resourceService.getLog(id);
        String ret = verifyAccount(principal, log, ResultInfo.CODE_UPLOAD_RES, false);
        if (ret != null) {
            return ret;
        }
        return saveClientFile(log, "video", filename, index, total, checksum, chunkSize, file);
    }


    private String verifyAccount(Principal principal, Log log, int errInfo, boolean shouldCheckGroup) {
        if (log.getGid() != 0) {
            Account account = getOpAccount(principal);
            if (account == null) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, errInfo, "Invalid request parameters.");
            }
            if (log.getUid() != account.getUid()) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, errInfo, "You don't have permission to access.");
            }
            if (shouldCheckGroup && !isBelongGid(log.getGid())) {
                return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, errInfo, "You don't have permission to access.");
            }
        }
        return null;
    }

    private String saveClientFile(Log log, String savetype, String filename, int index, int total, String checksum, int chunkSize, MultipartFile file) {
        String result = "";
        if (log.getDetail() == null) {
            resourceService.createLogDetail(log);
        }
        String saveDir = NetFile.getDirAbsolutePath(log.getDetail().getRes_url(), savetype);
        try {
            int err = NetFile.saveSplitFile(saveDir, filename, index, total, checksum, chunkSize, file);
            if (err != 0) {
                result = ResultInfo.assembleJson(ResultInfo.ErrType.MISMATCH, err, "checksum mismatch.");
            } else if (index + 1 == total && savetype.toLowerCase().equals("video")) {
                streamService.asynCreateHLSFiles(log.getId(),
                        NetFile.getDirAbsolutePath(saveDir, filename),
                        NetFile.getDirAbsolutePath(saveDir, "hls", filename),
                        "/blog/api/res/video/");
            }
        } catch (IOException e) {
            result = ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_UPLOAD_RES, e.getMessage());
        }
        return result;
    }

    @DeleteMapping("files")
    public String deleteFile(Principal principal,
                             @RequestParam(name = "id", required = true) int id,
                             @RequestParam(name = "name", required = false, defaultValue = "all") String filesname) {
        Account account = getOpAccount(principal);
        if (account == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_DELETE_RES_FILES, "Invalid request parameters.");
        }
        Log log = resourceService.getLog(id);
        if (log.getUid() != account.getUid()) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.INVALID, ResultInfo.CODE_DELETE_RES_FILES, "You don't have permission to access.");
        }
        if (log == null) {
            return ResultInfo.assembleJson(ResultInfo.ErrType.NULLOBJ, ResultInfo.CODE_DELETE_RES_FILES, "There are no logfile existes.");
        }
        NetFile.deleteFiles(log.getDetail().getRes_url(), Arrays.asList(filesname.split(";")));
        return "";
    }

    @GetMapping("list")
    public String getResource(Principal principal,
                              @RequestParam(name = "type", required = false, defaultValue = "0") int type,
                              @RequestParam(name = "gid", required = false, defaultValue = "0") int gid,
                              @RequestParam(name = "title", required = false, defaultValue = "") String title,
                              @RequestParam(name = "status", required = false, defaultValue = "0") int status,
                              @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                              @RequestParam(name = "count", required = false, defaultValue = "5") int count
    ) {
        int uid = 0;
        if (principal != null) {
            Account account = getOpAccount(principal);
            if (account != null) {
                uid = account.getUid();
            }
        }
        ResultData data = new ResultData();
        List<Log> logs = resourceService.getLogs(uid, type, gid, title, status, offset, count, data.getMap());
        assembleAuthor(logs);
        data.setCode(ResultInfo.CODE_GET_RES);
        data.setType(ResultInfo.translate(ResultInfo.ErrType.SUCCESS));
        data.addToMap("logs", logs);
        return JSONObject.toJSONString(data);
    }

    private List<Log> assembleAuthor(List<Log> logs) {
        Iterator<Log> iter = logs.iterator();
        while (iter.hasNext()) {
            Log log = iter.next();
            String info = accountService.getAccountInfo(log.getUid(), null);
            if (info != null) {
                log.setAuthor(JSONObject.parseObject(info, Account.class).getUsername());
            }
        }
        return logs;
    }

    @GetMapping("top")
    public String getTopResource() {
        ResultData data = new ResultData();
        List<Log> logs = resourceService.getTopLogs();
        assembleAuthor(logs);
        data.setCode(ResultInfo.CODE_GET_RES);
        data.setType(ResultInfo.translate(ResultInfo.ErrType.SUCCESS));
        data.addToMap("logs", logs);
        return JSONObject.toJSONString(data);
    }

    @GetMapping("blog")
    public String getLog(@RequestParam(name = "id") int id) {
        ResultData data = new ResultData();
        Log log = resourceService.getLog(id);
        String info = accountService.getAccountInfo(log.getUid(), null);
        if (info != null) {
            log.setAuthor(JSONObject.parseObject(info, Account.class).getUsername());
        }
        data.setType(ResultInfo.translate(ResultInfo.ErrType.SUCCESS));
        data.addToMap("log", log);
        return JSONObject.toJSONString(data);
    }

    private ResponseEntity getImageFile(Principal principal, String dir, int logid, String name) {
        Log log = resourceService.getLog(logid);
        String errinfo = verifyAccount(principal, log, ResultInfo.CODE_GET_RES_FILES, true);
        if (errinfo == null) {
            try {
                for (Map.Entry<String, String> entry : log.getDetail().getFiles().entrySet()) {
                    if (entry.getKey().equals(name) && entry.getValue().contains(File.separatorChar + dir + File.separatorChar)) {
                        return createResponseEntity("image/jpg", entry.getValue());
                    }
                }
            } catch (Exception e) {
                errinfo = e.getMessage();
            }
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    @GetMapping("cover/{lid}/{name}")
    @ResponseBody
    public ResponseEntity getCoverFile(Principal principal, @PathVariable("lid") int logid, @PathVariable("name") String name) {
        return getImageFile(principal, "cover", logid, name);
    }

    @GetMapping("img/{lid}/{name}")
    @ResponseBody
    public ResponseEntity getImageFile(Principal principal, @PathVariable("lid") int logid, @PathVariable("name") String name) {
        return getImageFile(principal, "img", logid, name);
    }

    @GetMapping("video/{lid}/{name}/{tsfile}")
    public ResponseEntity getVideoTsFile(Principal principal,
                                         @PathVariable("lid") int logid,
                                         @PathVariable("name") String name,
                                         @PathVariable("tsfile") String tsname) {
        Log log = resourceService.getLog(logid);
        String errinfo = verifyAccount(principal, log, ResultInfo.CODE_GET_RES_FILES, true);
        if (errinfo == null) {
            try {
                for (Map.Entry<String, String> entry : log.getDetail().getFiles().entrySet()) {
                    if (entry.getKey().equals(name) && entry.getValue().contains(File.separatorChar + "video" + File.separatorChar)) {
                        return createResponseEntity("application/x-mpegURL", NetFile.translateLocalVideoFileToTsFile(entry.getValue(), tsname));
                    }
                }
            } catch (Exception e) {
                errinfo = e.getMessage();
            }
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    @GetMapping("video/{lid}/{name}")
    public ResponseEntity getVideoFile(Principal principal, @PathVariable("lid") int logid, @PathVariable("name") String name) {
        Log log = resourceService.getLog(logid);
        String errinfo = verifyAccount(principal, log, ResultInfo.CODE_GET_RES_FILES, true);
        if (errinfo == null) {
            try {
                for (Map.Entry<String, String> entry : log.getDetail().getFiles().entrySet()) {
                    if (entry.getKey().equals(name) && entry.getValue().contains(File.separatorChar + "video" + File.separatorChar)) {
                        return createResponseEntity("application/x-mpegURL", NetFile.translateLocalVideoFileToHlsFile(entry.getValue()));
                    }
                }
            } catch (Exception e) {
                errinfo = e.getMessage();
            }
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    private ResponseEntity createResponseEntity(String type, String filePath) throws IOException {
        MediaType mediaType = MediaType.parseMediaType(type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return new ResponseEntity(bytes, headers, HttpStatus.OK);
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
