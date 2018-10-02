package com.ray.service.auth.clients;

import com.ray.service.auth.util.ErrInfo;
import org.springframework.stereotype.Component;

@Component
public class RemoteAccountFeignClientHystrix implements RemoteAccountFeignClient {
    @Override
    public String getInfo(String username) {
        return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_ACCOUNT, "Invalid resources.");
    }
}
