package com.rcircle.service.resource.service;

import com.rcircle.service.resource.mapper.QuotationMapper;
import com.rcircle.service.resource.model.Quotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class QuotationService {
    public static final int TYPE_MINI_ID = 0;
    public static final int TYPE_MAX_ID = 1;
    public static final int TYPE_RAND_ID = 2;
    @Autowired
    private QuotationMapper quotationMapper;

    public int getId(int type) {
        int ret = 1;
        int max = quotationMapper.getMaxId();
        int min = quotationMapper.getMiniId();
        switch (type) {
            case TYPE_MAX_ID:
                ret = max;
                break;
            case TYPE_MINI_ID:
                ret = min;
                break;
            case TYPE_RAND_ID:
                Random random = new Random();
                ret = min + random.nextInt(max - min);
                break;
        }
        return ret;
    }

    public Quotation getQuotationById(int id) {
        return quotationMapper.getQuotation(id);
    }

}
