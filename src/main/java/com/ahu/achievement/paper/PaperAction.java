package com.ahu.achievement.paper;

import com.ahu.achievement.properties.AchievementProperties;
import com.ahu.achievement.user.User;
import com.ahu.achievement.utils.MyZipUtils;
import org.apache.tools.zip.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        User user= (User) session.getAttribute("user");
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
        mv.addObject("zipList", zipList);
        mv.addObject("dirList", dirList);
        System.out.println(zipList.toString() + "----" + dirList.toString());
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
        String file=filePath+zipname;
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
        String file=filePath+zipname;
        MyZipUtils.delete(file);
        System.out.println("删除成功");
        mv.setViewName("redirect:/showMyAchievement");
        return mv;
    }

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
}
