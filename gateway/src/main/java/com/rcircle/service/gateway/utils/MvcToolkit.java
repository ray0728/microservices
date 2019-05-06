package com.rcircle.service.gateway.utils;

import com.rcircle.service.gateway.services.ResourceService;
import org.springframework.ui.ModelMap;

public class MvcToolkit {
    public static ModelMap autoLoadTopMenuData(ResourceService resourceService, ModelMap mm){
        mm.addAttribute("categories", resourceService.getAllCategoryForCurrentUser());
        return mm;
    }

    public static ModelMap autoLoadSideBarData(ResourceService resourceService, ModelMap mm){
        mm.addAttribute("tags", resourceService.getAllTags());
        mm.addAttribute("top", resourceService.getTopBlogs());
        return mm;
    }
}
