package com.ahu.achievement.paper;

import com.ahu.achievement.properties.AchievementProperties;
import com.ahu.achievement.user.User;
import com.ahu.achievement.utils.MyZipUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class PaperAction {
    @Autowired
    private PaperService paperService;
    @Autowired
    private AchievementProperties achievementProperties;

    @GetMapping("/findByAuthorname")
    public List<Paper> findByAuthorname() {
        List<Paper> paperList = paperService.findByAuthorsid("E13212312");
        return paperList;
    }

    /**
     * 查找登陆用户的成果文件以及文件夹
     *
     * @param mv
     * @param session
     * @return
     */
    @GetMapping("/showMyAchievement")
    public ModelAndView showMyAchievement(ModelAndView mv, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String filePath = achievementProperties.getFtp_file_name();
        //获取ftp目录下的所以一级目录以及文件
        List<String> fileList = MyZipUtils.traverseChildrenFolder(filePath);
//        删选出该用户自己的zip文件以及解压之后的文件
        List<String> zipList = new ArrayList<>();
        List<String> dirList = new ArrayList<>();
        for (String file : fileList) {
            if (file.endsWith(".zip") && file.contains(user.getUsername())) {
                zipList.add(file.substring(file.lastIndexOf("\\") + 1, file.length()));
            } else if (file.contains(user.getUsername())) {
                dirList.add(file.substring(file.lastIndexOf("\\") + 1, file.length()));
            }
        }

        List<Paper> paper = paperService.findByAuthorsid(user.getUsername());
        List showpaper = new ArrayList();
        for (String paperdirname : dirList) {
            boolean flag = false;
            String papername = paperdirname + ".pdf";
            Map map = new HashMap();
            for (Paper pa : paper) {
                if (pa.getPapername().equals(papername)) {
                    flag = true;
                }
            }
            map.put("dirname", paperdirname);
            if (flag) {
                map.put("isexist", 1);//数据库中已经存在
            } else {
                map.put("isexist", 0);//新数据，还未入库
            }
            showpaper.add(map);
        }

        mv.addObject("zipList", zipList);
        mv.addObject("dirList", showpaper);
//        System.out.println(zipList.toString() + "----" + dirList.toString());
        mv.setViewName("admin/achievement");
        return mv;
    }

    /**
     * 根据文件名解压缩文件
     *
     * @param mv
     * @param zipname
     * @return
     */
    @GetMapping("/unZip/{zipname}")
    public ModelAndView unZip(ModelAndView mv, @PathVariable String zipname) {

        //ftp的根目录
        String filePath = achievementProperties.getFtp_file_name();
        String file = filePath + zipname;
        MyZipUtils.unZip(file, filePath);
        System.out.println("解压成功");
        MyZipUtils.deleteFile(file);
        System.out.println("压缩包删除成功");
        mv.setViewName("redirect:/showMyAchievement");
        return mv;
    }

    /**
     * 根据文件夹或者文件名来删除
     *
     * @param mv
     * @param zipname
     * @return
     */
    @GetMapping("/deleteZip/{zipname}")
    public ModelAndView deleteZip(ModelAndView mv, @PathVariable String zipname) {

        //ftp的根目录
        String filePath = achievementProperties.getFtp_file_name();
        String file = filePath + zipname;
        MyZipUtils.delete(file);
        System.out.println("删除成功");
        mv.setViewName("redirect:/showMyAchievement");
        return mv;
    }

    @GetMapping("/check/{filename}")
    public ModelAndView check(ModelAndView mv, @PathVariable String filename, HttpSession session) {
        //获取用户的session的id，姓名与json文件进行对比，对比论文的名称和json文件中的是否一致，并且对比论文的格式是否一致
        User user = (User) session.getAttribute("user");

        //ftp的根目录
        String filePath = achievementProperties.getFtp_file_name();
        String file = filePath + filename;
        System.out.println(file);
//        获取文件目录下的文件或者文件夹，然后进行校验
        Map map = MyZipUtils.traverseFolder(file);
        List error = new ArrayList();
        List<String> er = (List) map.get("error");
        error.addAll(er);
        System.out.println("map:" + JSON.toJSONString(map));
        System.out.println(file + "/paperInfo.json");
//        读取json文件中的信息
        File jsonfile = new File(file + "/paperInfo.json");
        if (jsonfile.isFile()) {
            String content = null;
            try {
                content = FileUtils.readFileToString(jsonfile, "UTF-8");
                Paper paperJson = JSON.parseObject(content, Paper.class);
                System.out.println("paperJson:" + JSON.toJSONString(paperJson));
                //判断json文件的数据是否正确
                if (paperJson.getAuthorsid().isEmpty()) {
                    error.add("paperInfo.json中学号不能为空");
                } else if (paperJson.getAuthorsname().isEmpty()) {
                    error.add("paperInfo.json中作者姓名不能为空");
                } else if (paperJson.getPaperdesc().isEmpty()) {
                    error.add("paperInfo.json中论文描述不能为空");
                } else if (paperJson.getDegreelevel().isEmpty()) {
                    error.add("paperInfo.json中学位等级不能为空");
                } else if (paperJson.getPublish().isEmpty()) {
                    error.add("paperInfo.json中发布时间不能为空");
                } else if (paperJson.getTags().isEmpty()) {
                    error.add("paperInfo.json中论文标签不能为空");
                } else if (!paperJson.getPapername().equals(map.get("papername"))) {
                    System.out.println("1");
                    error.add("paperInfo.json中论文名称与您上传的论文名称不一致！");
                } else if (!paperJson.getPapername().equals(filename + ".pdf")) {
                    System.out.println("2");
                    error.add("上传论文的名称与文件夹名不一致");
                } else if (paperJson.getCode().isEmpty()) {
                    error.add("code文件夹不能为空");
                } else if (paperJson.getData().isEmpty()) {
                    error.add("data文件夹不能为空");
                } else if (paperJson.getReferences().isEmpty()) {
                    error.add("references文件夹不能为空");
                } else if (!paperJson.getAuthorsid().equals(user.getUsername())) {
                    error.add("paperInfo.json中的学号与您登录的账号不一致！！！");
                } else if (!paperJson.getAuthorsname().equals(user.getNickname())) {
                    error.add("paperInfo.json中的姓名与您的账户姓名不一致！！！");
                }
                List<String> data = (List<String>) map.get("data");
                List<String> code = (List<String>) map.get("code");
                List<String> references = (List<String>) map.get("references");
//                List<String> others = (List<String>) map.get("others");
                if (data != null) {
                    for (Data datajson : paperJson.getData()) {
                        if (!data.contains(datajson.getDataname())) {
                            error.add(String.format("paperInfo.json中的%s与data文件夹中的文件名不一致", datajson.getDataname()));
                        }

                    }

                }
                if (code != null) {
                    for (Code codejson : paperJson.getCode()) {
                        if (!code.contains(codejson.getCodename())) {
                            error.add(String.format("paperInfo.json中的%s与code文件夹中的文件名不一致", codejson.getCodename()));
                        }


                    }
                }
                if (references != null) {
                    for (References referencesjson : paperJson.getReferences()) {
                        if (!references.contains(referencesjson.getReferencename())) {
                            error.add(String.format("paperInfo.json中的%s与references文件夹中的文件名不一致", referencesjson.getReferencename()));
                        }
                    }
                }
//                for (Others othersjson : paperJson.getOthers()) {
//                    if (!others.contains(othersjson.getOthersname())) {
//                        error.add(String.format("others文件夹中的%s文件名与paperInfo.json的不一致", othersjson.getOthersname()));
//                    }
//                }

                if (error.size() == 0) {
                    //存入数据库，显示论文上传成功
                    paperJson.setIsshow(1);
                    paperService.savePaper(paperJson);
                    System.out.println("存入数据库");
                }
                System.out.println(JSON.toJSONString(paperJson));
            } catch (IOException e) {
                error.add("读取paperInfo.json文件异常");
            }
        } else {
            error.add("文件夹中paperInfo.json文件不存在");
        }
        mv.setViewName("admin/check");
        mv.addObject("error", error);
        return mv;
    }


    /**
     * 添加测试用例
     * @param mv
     * @return
     */
    @GetMapping("/savepaper")
    public String savePaper(ModelAndView mv) {
        Paper paper = new Paper();
        paper.setAuthorsid("E13212312");
        paper.setAuthorsname("余豪");
        paper.setDegreelevel("硕士");
        paper.setPaperdesc("论文描述");
        paper.setPapername("学位论文.pdf");
        paper.setPublish("2018");
        paper.setReadme("readme 的内容");
        List<Code> codeList = new ArrayList<>();
        Code code1 = new Code("代码1.rar", "代码1的描述");
        Code code2 = new Code("代码2.rar", "代码2的描述");
        codeList.add(code1);
        codeList.add(code2);
        paper.setCode(codeList);
        List<Others> othersList = new ArrayList<>();
        Others others1 = new Others("others文件名1.doc", "others1的描述");
        Others others2 = new Others("others文件名2.doc", "others2的描述");
        othersList.add(others1);
        othersList.add(others2);
        paper.setOthers(othersList);
        List<Data> dataList = new ArrayList<>();
        Data data1 = new Data("数据1.rar", "数据1的描述");
        Data data2 = new Data("数据2.rar", "数据2的描述");
        dataList.add(data1);
        dataList.add(data2);
        paper.setData(dataList);
        List<Tags> tagsList = new ArrayList<>();
        Tags tags1 = new Tags("论文关键字1");
        Tags tags2 = new Tags("论文关键字2");
        Tags tags3 = new Tags("论文关键字3");
        tagsList.add(tags1);
        tagsList.add(tags2);
        tagsList.add(tags3);
        paper.setTags(tagsList);
        List<References> referencesList = new ArrayList<>();
        References references1 = new References("引文1.pdf", "引文1描述", "引文1url", tagsList);
        References references2 = new References("引文2.pdf", "引文2描述", "引文1url", tagsList);
        References references3 = new References("引文3.pdf", "引文3描述", "引文1url", tagsList);
        referencesList.add(references1);
        referencesList.add(references2);
        referencesList.add(references3);
        paper.setReferences(referencesList);
        paperService.savePaper(paper);
        return "保存成功";
    }

    /**
     * 后台显示所有成果列表
     *
     * @param mv
     * @return
     */
    @GetMapping("showAllAchievement")
    public ModelAndView showAllAchievement(ModelAndView mv) {
        List<Paper> paperList = paperService.findAll();
        mv.addObject("paperList", paperList);
        mv.setViewName("admin/showAllAchievement");
        return mv;
    }

    /**
     * 首页显示所有成果列表
     *
     * @param mv
     * @return
     */
    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(ModelAndView mv) {
        List<Paper> paperList = paperService.findAll();
        List<Tags> tagsList = new ArrayList<>();
        for (Paper pa : paperList) {
            tagsList.addAll(pa.getTags());
            for (References re : pa.getReferences()) {
                tagsList.addAll(re.getTags());
            }
        }
        HashSet<String> h = new HashSet();
        for (Tags tag11 : tagsList) {
            h.add(tag11.getTag());
        }
        List list = new ArrayList();
        for (String tag : h) {
            Map map = new HashMap();
            map.put("label", tag);
            String url = "searchTags/" + tag;
            map.put("url", url);
            map.put("target", "_top");
            list.add(map);

        }
//        System.out.println(JSON.toJSONString(list));
        mv.addObject("tagList", JSON.toJSON(list));
        mv.addObject("paperList", paperList);
        mv.setViewName("index/index");
        return mv;
    }

    /**
     * 按照标签查找
     *
     * @param mv
     * @param tags
     * @return
     */
    @GetMapping("searchTags/{tags}")
    public ModelAndView searchTags(ModelAndView mv, @PathVariable String tags) {
        List<Paper> findBytagsList = new ArrayList<>();
        List<Paper> paperList = paperService.findAll();
        List<Tags> tagsList = new ArrayList<>();
        for (Paper pa : paperList) {
            List<Tags> tagsList1 = pa.getTags();
            for (Tags tag : tagsList1) {
                if (tag.getTag().equals(tags)) {
                    findBytagsList.add(pa);
                }

            }
            tagsList.addAll(pa.getTags());
            for (References re : pa.getReferences()) {
                tagsList.addAll(re.getTags());
            }
        }
        HashSet<String> h = new HashSet();
        for (Tags tag11 : tagsList) {
            h.add(tag11.getTag());
        }
        System.out.println(JSON.toJSONString(h));
        List list = new ArrayList();
        for (String tag : h) {
            Map map = new HashMap();
            map.put("label", tag);
            String url = "" + tag;
            map.put("url", url);
            map.put("target", "_top");
            list.add(map);

        }
        mv.addObject("tagList", JSON.toJSON(list));
        mv.addObject("paperList", findBytagsList);
        mv.setViewName("index/index");
        return mv;
    }


    /**
     * 成果管理
     *
     * @param mv
     * @return
     */
    @GetMapping("showEnable/{id}")
    public ModelAndView showEnable(ModelAndView mv, @PathVariable String id) {
        Paper paper = paperService.findById(id);
        paper.setIsshow(0);
        System.out.println(paper.toString());
        paperService.savePaper(paper);
//        mv.addObject("msg","修改成功");
        mv.setViewName("redirect:/showAllAchievement");
        return mv;
    }

    @GetMapping("showDisable/{id}")
    public ModelAndView showDisable(ModelAndView mv, @PathVariable String id) {
        Paper paper = paperService.findById(id);
        paper.setIsshow(1);
        paperService.savePaper(paper);
//        mv.addObject("msg","修改成功");
        mv.setViewName("redirect:/showAllAchievement");
        return mv;
    }

    /**
     * 按照id显示论文具体内容
     *
     * @param mv
     * @param id
     * @return
     */
    @GetMapping("showOnePaper/{id}")
    public ModelAndView showOnePaper(ModelAndView mv, @PathVariable String id) {
        Paper paper = paperService.findById(id);

        mv.addObject("paper", paper);

        List<Paper> paperList = paperService.findAll();
        List<Tags> tagsList = new ArrayList<>();
        for (Paper pa : paperList) {
            tagsList.addAll(pa.getTags());
            for (References re : pa.getReferences()) {
                tagsList.addAll(re.getTags());
            }
        }
        HashSet<String> h = new HashSet();
        for (Tags tag11 : tagsList) {
            h.add(tag11.getTag());
        }
        List list = new ArrayList();
        for (String tag : h) {
            Map map = new HashMap();
            map.put("label", tag);
            String url = "/searchTags/" + tag;
            map.put("url", url);
            map.put("target", "_top");
            list.add(map);

        }
        mv.addObject("tagList", JSON.toJSON(list));
        mv.setViewName("index/paper");
        return mv;
    }
    @GetMapping("showAdminIndex")
    public ModelAndView showAdminIndex(ModelAndView mv,String msg){
        List<Paper> paperList = paperService.findAll();
        List<Tags> tagsList = new ArrayList<>();
        for (Paper pa : paperList) {
            tagsList.addAll(pa.getTags());
            for (References re : pa.getReferences()) {
                tagsList.addAll(re.getTags());
            }
        }
        HashSet<String> h = new HashSet();
        for (Tags tag11 : tagsList) {
            h.add(tag11.getTag());
        }
        List list = new ArrayList();
        for (String tag : h) {
            Map map = new HashMap();
            map.put("label", tag);
            String url = "/searchTags/" + tag;
            map.put("url", url);
            map.put("target", "_top");
            list.add(map);

        }
        mv.addObject("tagList", JSON.toJSON(list));
        mv.addObject("msg", msg);
        mv.setViewName("admin/index");
        return mv;
    }



}
