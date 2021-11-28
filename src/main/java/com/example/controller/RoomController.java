package com.example.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/room")
public class RoomController {
    @RequestMapping("/peopleAllCount")
    @ResponseBody
    public String peopleAllCount(HttpServletRequest request) {
        System.out.println(">---------更新所有自习室人数---------<");
        ServletContext application = request.getServletContext();
        int[] QtyofPeopleinRoom = (int[])application.getAttribute("QtyofPeopleinRoomName");
        String str = "";
        // 注意从下标1开始，因为没有自习室00
        for (int i = 1; i < QtyofPeopleinRoom.length; ++i)
            str += QtyofPeopleinRoom[i] + " ";
        return str;
    }

    // http://localhost:8080/study_with_me/room/peopleCount?roomID=x
    @RequestMapping("/peopleCount")
    // 表明直接作为响应体，而非经过视图解析
    @ResponseBody
    public String peopleCount(HttpServletRequest request, int roomID) {
        ServletContext application = request.getServletContext();
        int[] QtyofPeopleinRoom = (int[])application.getAttribute("QtyofPeopleinRoomName");
        System.out.println(">---------更新自习室" + roomID + "人数为" + QtyofPeopleinRoom[roomID] + "---------<");
        return "" + QtyofPeopleinRoom[roomID];
    }

    // http://localhost:8080/study_with_me/room/leaveRoom
    @RequestMapping("/leaveRoom")
    @ResponseBody
    public String leaveRoom(HttpServletRequest request, int roomID) {
        System.out.println(">---------离开自习室" + roomID + "---------<");
        ServletContext application;
        // 对上下文加锁，防止多个线程影响QtyofPeopleinRoom[id]--
        synchronized (application = request.getServletContext()) {
            int[] QtyofPeopleinRoom = (int[])application.getAttribute("QtyofPeopleinRoomName");
            if (QtyofPeopleinRoom[roomID] > 0)
                QtyofPeopleinRoom[roomID]--;
            // 更新application域的自习室人数数组
            application.setAttribute("QtyofPeopleinRoomName", QtyofPeopleinRoom);
        }
        return "";
    }

    @RequestMapping("/enterRoom")
    public ModelAndView toRoom(HttpServletRequest request, int roomID) {
        System.out.println(">---------进入自习室" + roomID + "---------<");
        // 创建模型视图对象，并设置视图名为room
        ModelAndView mv = new ModelAndView("room");
        ServletContext application;
        int[] QtyofPeopleinRoom;

        // application = request.getServletContext();
        // // 从application域中获取所有自习室人数数组
        // QtyofPeopleinRoom = (int[])application.getAttribute("QtyofPeopleinRoomName");
        // if (QtyofPeopleinRoom == null)
        // return mv;
        // // 所在的自习室人数+1
        // QtyofPeopleinRoom[roomID]++;
        // // 更新application域的所有自习室人数数组
        // application.setAttribute("QtyofPeopleinRoomName", QtyofPeopleinRoom);

        synchronized (application = request.getServletContext()) {
            // 从application域中获取所有自习室人数数组
            QtyofPeopleinRoom = (int[])application.getAttribute("QtyofPeopleinRoomName");
            if (QtyofPeopleinRoom == null)
                return mv;
            // 所在的自习室人数+1
            QtyofPeopleinRoom[roomID]++;
            // 更新application域的所有自习室人数数组
            application.setAttribute("QtyofPeopleinRoomName", QtyofPeopleinRoom);
        }

        // 模型中添加当前的自习室id和人数，便于视图获取数据
        mv.addObject("roomID", roomID);
        mv.addObject("number", QtyofPeopleinRoom[roomID]);
        return mv;
    }
}
