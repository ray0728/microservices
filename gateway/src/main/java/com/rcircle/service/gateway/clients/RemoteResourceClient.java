package com.rcircle.service.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "service-resource", configuration = RemoteRequestWithTokenConfiguration.class)
public interface RemoteResourceClient {
    @GetMapping("/cate/list")
    public String getAllCategorys();

    @PostMapping("/cate/new")
    public String addNewCategory(@RequestParam(name = "desc", required = true) String desc);

    @GetMapping("/res/list")
    public String getAllDiaries(@RequestParam(name = "type") int type,
                                @RequestParam(name = "gid") int gid,
                                @RequestParam(name = "title") String title,
                                @RequestParam(name = "status") int status,
                                @RequestParam(name = "offset") int offset,
                                @RequestParam(name = "count") int count);

    @GetMapping("/res/top")
    public String getTopResource();

    @GetMapping("/res/files")
    public String getAllFileInfo(@RequestParam(name = "id") int id);

    @GetMapping("/res/blog")
    public String getBLog(@RequestParam(name = "id")int id);

    @GetMapping("/tag/list")
    public String getAllTags();

    @GetMapping("/reply/list")
    public String getAllReplies(@RequestParam(name = "id", required = true) int id);

    @GetMapping("/ref/quot")
    public String getQuotation(@RequestParam(name = "type") int type, @RequestParam(name = "id") int id);
}
