package com.rcircle.service.resource.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rcircle.service.resource.model.Quotation;
import com.rcircle.service.resource.service.QuotationService;
import com.rcircle.service.resource.utils.ErrInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ref/")
public class ReferenceController {
    @Resource
    private QuotationService quotationService;

    @GetMapping("quot")
    public String getQuotation(@RequestParam(name = "type") int type,
                               @RequestParam(name = "id", required = false, defaultValue = "0") int id) {
        if (id == 0 && (type > QuotationService.TYPE_RAND_ID || type < QuotationService.TYPE_MINI_ID)) {
            return ErrInfo.assembleJson(ErrInfo.ErrType.PARAMS, ErrInfo.CODE_GET_QUOT, "Invalid request parameters.");
        } else if (id == 0) {
            id = quotationService.getId(type);
        }
        Quotation quotation = quotationService.getQuotationById(id);
        return JSONObject.toJSONString(quotation);
    }
}
