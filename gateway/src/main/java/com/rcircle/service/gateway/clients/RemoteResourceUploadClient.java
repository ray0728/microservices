package com.rcircle.service.gateway.clients;


import feign.RequestInterceptor;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;

import feign.codec.Encoder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "service-resource", configuration = RemoteResourceUploadClient.MultipartSupportConfig.class)
public interface RemoteResourceUploadClient {

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadSplitFile(@RequestPart(value = "file") MultipartFile file,
                                  @RequestParam(name = "id", required = true) int id,
                                  @RequestParam(name = "name", required = true) String filename,
                                  @RequestParam(name = "index", required = true) int index,
                                  @RequestParam(name = "total", required = true) int total,
                                  @RequestParam(name = "chunksize") int chunkSize,
                                  @RequestParam(name = "checksum", required = true) String checksum);

    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }

        @Bean
        public RequestInterceptor appendTokenInterceptor() {
            return new RemoteRequestWithTokenInterceptor();
        }
    }
}
