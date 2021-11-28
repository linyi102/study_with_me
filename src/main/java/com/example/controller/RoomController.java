package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/room")
public class RoomController {
    // http://localhost:8080/study_with_me/room/leaveRoom
    @RequestMapping("/leaveRoom")
    @ResponseBody
    public String leaveRoom(HttpServletRequest request, int roomId) {
        System.out.println(">---------离开自习室" + roomId + "---------<");
        return "";
    }

    // http://localhost:8080/study_with_me/room/enterRoom?roomId=1
    @RequestMapping("/enterRoom")
    public ModelAndView toRoom(HttpServletRequest request, int roomId) {
        System.out.println(">---------进入自习室" + roomId + "---------<");
        // 创建模型视图对象，并设置视图名为room
        ModelAndView mv = new ModelAndView("room");

        // 模型中添加当前的自习室id，便于视图获取数据
        mv.addObject("roomId", roomId);
        return mv;
    }
}
