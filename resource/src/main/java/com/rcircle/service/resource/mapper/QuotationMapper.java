package com.rcircle.service.resource.mapper;

import com.rcircle.service.resource.model.Quotation;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationMapper {
    public int getMiniId();

    public int getMaxId();

    public Quotation getQuotation(int id);
}
