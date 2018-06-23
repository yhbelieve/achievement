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
     *
     * @param mv
     * @return
     */
    @GetMapping(value = {"/adminLogin", "/userLogin", "/login"})
    public ModelAndView index_get(ModelAndView mv) {
//        System.out.println("跳转登陆界面");
        mv.setViewName("admin/login");
        return mv;
    }

    @PostMapping(value = {"/adminLogin", "/userLogin", "/login"})
    public ModelAndView index_post(ModelAndView mv, HttpSession session, @ModelAttribute User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println(username + "::" + password);
        User user1 = userService.findByUsernameAndPassword(username, password);
//        userService.
//        System.out.println(user1);
        if (user1 == null) {
            mv.addObject("user", user);
            mv.addObject("msg", "用户名或者密码错误！！！如有问题，请联系管理员");
            mv.setViewName("admin/login");
        } else {
            session.setAttribute("user", user1);
            mv.setViewName("redirect:/showAdminIndex");
        }
        return mv;
    }

    /**
     * 用户退出登陆
     *
     * @param mv
     * @param session
     * @return
     */
    @GetMapping(value = "/logout")
    public ModelAndView logout(ModelAndView mv, HttpSession session) {
        mv.setViewName("admin/login");
        session.removeAttribute("user");
        return mv;
    }

    @GetMapping(value = "/showUserList")
    public ModelAndView showUserList(ModelAndView mv,String msg) {
        List<User> list = userService.findAll();
        mv.addObject("msg",msg);
        mv.addObject("list", list);
        mv.setViewName("admin/userlist");
        return mv;
    }

    /**
     * 添加测试管理员
     *
     * @return
     */
    @RequestMapping("/adduser")
    public String addUser() {

        User user1 = new User("1", "admin", "123456", 1, "系统管理员");
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setLevel(1);
        user.setNickname("系统管理员");
        userService.InsertUser(user);
//
        return "用户添加成功";
    }


    @GetMapping(value = "/editPassword")
    public ModelAndView showeditPassword(ModelAndView mv) {
        mv.setViewName("admin/editPassword");
        return mv;
    }

    @PostMapping(value = "/editPassword")
    public ModelAndView editPassword(ModelAndView mv, HttpSession session, String newpassword, String oldpassword, String checkpassword) {
        User user = (User) session.getAttribute("user");
        System.out.println(newpassword + ":" + oldpassword);
        if (!user.getPassword().equals(oldpassword)) {
            mv.addObject("error1", "旧密码数据错误，请重新输入");
            mv.setViewName("admin/editPassword");
            return mv;
        } else if (!newpassword.equals(checkpassword)) {

            mv.addObject("error3", "两次密码输入不一致");
            mv.setViewName("admin/editPassword");
            return mv;
        } else if (newpassword.length() < 6) {
            mv.addObject("error2", "密码长度至少为6位");
            mv.setViewName("admin/editPassword");
            return mv;
        } else {
            user.setPassword(newpassword);
            userService.InsertUser(user);
            mv.addObject("msg", "密码修改成功，退出系统后请用新密码登录");
            mv.setViewName("redirect:/showAdminIndex");
            return mv;
        }
    }

    @GetMapping(value = "/addUser")
    public ModelAndView getaddUser(ModelAndView mv) {
        mv.setViewName("admin/addUser");
        return mv;
    }

    @PostMapping(value = "/addUser")
    public ModelAndView addUser(ModelAndView mv, String username, String userid) {
        User user = new User();

        if (username.trim().isEmpty() || userid.trim().isEmpty()) {
            mv.addObject("msg", "姓名或者学号不能为空！！！");
            mv.setViewName("admin/addUser");
            return mv;
        } else {
            List<User> userList=userService.findByUsername(userid);
            if(userList.size()==0){
            user.setPassword(userid);
            user.setUsername(userid);
            user.setNickname(username);
            user.setLevel(0);
            userService.InsertUser(user);
            mv.addObject("msg", "新用户添加成功");
            mv.setViewName("redirect:/showUserList");

            }else {
                mv.addObject("msg", "学号已经添加过了，请不要重复添加！！！");
                mv.setViewName("admin/addUser");
            }
            return mv;
        }
    }


    @GetMapping("/restPassword/{userid}")
    public ModelAndView restPassword(ModelAndView mv, @PathVariable String userid) {
        User user=userService.findById(userid);
        user.setPassword(user.getUsername());
        userService.InsertUser(user);
        mv.setViewName("redirect:/showUserList");
        mv.addObject("msg","重置密码成功");
        return mv;
    }
}
