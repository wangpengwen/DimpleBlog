package com.dimple.controller;

import com.dimple.bean.server.Server;
import com.dimple.framework.enums.BlogStatus;
import com.dimple.framework.message.Result;
import com.dimple.framework.message.ResultUtil;
import com.dimple.service.BlogService;
import com.dimple.service.DashboardService;
import com.dimple.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * @author : Dimple
 * @version : 1.0
 * @class : DashboardController
 * @description : 登录显示的首页的地址信息
 * @date : 01/09/19 19:18
 */
@Controller
public class DashboardController {

    @Autowired
    BlogService blogService;

    @Autowired
    SessionService sessionService;

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/page/dashboard.html")
    public String dashboardPage(Model model) {
        model.addAttribute("published", blogService.selectBlogCountByStatus(BlogStatus.PUBLISHED));
        model.addAttribute("draft", blogService.selectBlogCountByStatus(BlogStatus.DRAFT));
        model.addAttribute("dustbin", blogService.selectBlogCountByStatus(BlogStatus.DUSTBIN));
        model.addAttribute("all", blogService.selectBlogCountByStatus(BlogStatus.ALL));
        model.addAttribute("onlineCount", sessionService.getOnlineList().size());
        return "dashboard";
    }

    @GetMapping("/api/spiderPieData")
    @ResponseBody
    public Result spiderPieData() {
        List<Map<String, Integer>> list = dashboardService.getSpiderPieData();
        return ResultUtil.success(list);
    }

    @GetMapping("/api/memJvmCpuData")
    @ResponseBody
    public Result memJvmCpuData() throws UnknownHostException {

        Server server = new Server();
        List<Double> data = server.copyToMemCpuJvm();
        return ResultUtil.success(data);
    }

    @GetMapping("/api/visitorCount")
    @ResponseBody
    public Result visitorCount() {
        return ResultUtil.success(dashboardService.getVisitorData());
    }
}
