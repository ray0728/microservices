package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "service-resource", configuration = RemoteRequestWithTokenConfiguration.class)
public interface RemoteResourceClient {
    @GetMapping("/cate/list")
    public String getAllCategorys();

    @PostMapping("/cate/new")
    public String addNewCategory(@RequestParam(name = "desc", required = true) String desc);

    @PostMapping("/res/new")
    public String createNewLog(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "type", required = true) int type,
            @RequestParam(name = "gid", required = false, defaultValue = "0") int gid);

    @GetMapping("files")
    public String getAllFileInfo(@RequestParam(name = "id", required = true) int id,
                                 @RequestParam(name = "onlyurl", required = false, defaultValue = "false") boolean isOnlyUrl);

    @PutMapping("update")
    public String updateLog(
                            @RequestParam(name = "id", required = true) int id,
                            @RequestParam(name = "title", required = false, defaultValue = "") String title,
                            @RequestParam(name = "type", required = false, defaultValue = "0") int type,
                            @RequestParam(name = "gid", required = false, defaultValue = "0") int gid,
                            @RequestParam(name = "log", required = false, defaultValue = "") String htmllog);
}
