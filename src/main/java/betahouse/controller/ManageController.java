package betahouse.controller;

import betahouse.controller.Base.BaseController;
import betahouse.core.Base.BaseFile;
import betahouse.model.VO.UserInformationVO;
import betahouse.service.club.ClubService;
import betahouse.service.user.UserInfoService;
import betahouse.service.user.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static betahouse.core.constant.FolderNameConstant.FOLDER_OFFICE_EXCEL;

/**
 * Created by x1654 on 2017/7/18.
 */
@Controller
@RequestMapping(value = "/manage")
public class ManageController extends BaseController{

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userManage")
    public String userManage(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("userInfo", userInfoService.listAllUserInfo());
        return "manage/userManage";
    }
    @RequestMapping(value = "/powerManage")
    public String powerManage(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("userInfo", userInfoService.listAllUserInfo());
        return "manage/powerManage";
    }

    @RequestMapping(value = "/clubManage")
    public String clubManage(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("club", clubService.listClubAndChief());
        return "manage/clubManage";
    }

    @RequestMapping(value = "/updateUserInfo")
    public String updateUserInfo(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam String data){
        List<UserInformationVO> listDTO = JSON.parseArray(data, UserInformationVO.class);
        for(UserInformationVO u: listDTO){
            int idDTO = u.getId();
            String passwordDTO = u.getPassword();
            String tel = u.getTel();
            String eMail = u.geteMail();
            if(!"".equals(tel)||!"".equals(eMail)){
                userInfoService.updateUserInfoById(idDTO, "", "", eMail, tel);
            }
            if(!"-1".equals(passwordDTO)){
                userService.updateUserById(idDTO, "", passwordDTO);
            }
        }
        return ajaxReturn(response,null, "修改成功", 0);
    }
    @RequestMapping(value = "/selfManage")
    public String selfManage(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("selfInfo", getCurrentUser(request));
        return "manage/selfManage";
    }

    @RequestMapping(value = "/createClub", method = RequestMethod.POST)
    public String createClub(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam("file") MultipartFile file){
        BaseFile baseFile = new BaseFile();
        baseFile.upload(file, FOLDER_OFFICE_EXCEL+"importFile");
        String fileNamee = file.getOriginalFilename();
        clubService.createClub("importFile", fileNamee.substring(0, fileNamee.lastIndexOf(".")));
        return ajaxReturn(response, null);
    }
}
