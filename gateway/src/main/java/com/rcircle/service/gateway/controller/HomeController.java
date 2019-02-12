package com.rcircle.service.gateway.controller;

import com.rcircle.service.gateway.model.NavMenu;
import com.rcircle.service.gateway.model.NavSubMenu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("securedPage")
    public String showSecuredPage(){return  "securePage";}

    @GetMapping("join")
    public String signup(ModelMap mm){
        List<NavMenu> menuList = new LinkedList();
        String[] navmenunames = {"HOME", "SVN", "IOS", "VB.NET", "JAVA", "PHP"};
        String[] subdropnames = {"Swing", "JMeter", "EJB", null, "分离的链接"};
        NavMenu navMenu;
        for(int i = 0; i < navmenunames.length; i++) {
            navMenu = new NavMenu();
            navMenu.setName(navmenunames[i]);
            navMenu.setUrl("#");
            if(i == 4){
                navMenu.setType(NavMenu.TYPE_DROPDOWN);
                for(int j = 0; j < subdropnames.length; j++) {
                    navMenu.addSubItem(subdropnames[i], "#");
                }
            }
            menuList.add(navMenu);
        }
        mm.addAttribute("title", "Join RC");
        mm.addAttribute("menuList", menuList);
        return "sign_up";
    }
}
