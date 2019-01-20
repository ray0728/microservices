package com.ray.service.store.clients;

import com.ray.service.store.util.ErrInfo;
import org.springframework.stereotype.Component;

@Component
public class RemoteAccountFeignClientHystrix implements RemoteAccountFeignClient {
    @Override
    public String getInfo(String username) {
        return ErrInfo.assembleJson(ErrInfo.ErrType.NULLOBJ, ErrInfo.CODE_GET_ACCOUNT, "Invalid resources.");
    }
}
