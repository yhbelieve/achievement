package com.ahu.achievement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class AdminAction {
    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     * @param mv
     * @return
     */
    @GetMapping(value = {"/adminLogin","/userLogin","/login"})
    public ModelAndView index_get(ModelAndView mv){
        System.out.println("跳转登陆界面");
        mv.setViewName("admin/login");
        return mv;
    }
    @PostMapping(value = {"/adminLogin","/userLogin","/login"})
    public ModelAndView index_post(ModelAndView mv,HttpSession session, @ModelAttribute User user){
        String username=user.getUsername();
        String password=user.getPassword();
        System.out.println(username+"::"+password);
        User user1=userService.findByUsernameAndPassword(username,password);
//        userService.
        System.out.println(user1);
        if (user1==null){
            mv.addObject("user",user);
            mv.setViewName("admin/login");
        }else {
            session.setAttribute("user",user1);
            mv.setViewName("admin/index");
        }
        return mv;
    }

    /**
     * 用户退出登陆
     * @param mv
     * @param session
     * @return
     */
    @GetMapping(value = "/logout")
    public ModelAndView logout(ModelAndView mv,HttpSession session){
        mv.setViewName("admin/login");
        session.removeAttribute("user");
        return mv;
    }

    @GetMapping(value = "/showUserList")
    public ModelAndView showUserList(ModelAndView mv){
        List<User> list=userService.findAll();
        mv.addObject("list",list);
        mv.setViewName("admin/userlist");
        return mv;
    }
    /**
     * 添加测试管理员
     * @return
     */
    @RequestMapping("/adduser")
    public String addUser(){

        User user1=new User(1L,"admin","123456",1,"系统管理员");
        userService.InsertUser(user1);
        User user=new User(2L,"user","123456",0,"测试用户1");
        userService.InsertUser(user);
        return "用户添加成功";
    }

}
