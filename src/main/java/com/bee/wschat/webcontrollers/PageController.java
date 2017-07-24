package com.bee.wschat.webcontrollers;

import com.bee.wschat.entity.ChatUser;
import com.bee.wschat.service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


/**
 * Created by Chaklader on 7/24/17.
 */
@Controller
public class PageController {

    private String uploadedFolder = "./";

    @Autowired
    private ChatUserService chatUserService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main() {
        return "chat";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/sign_un", method = RequestMethod.GET)
    public String showSignUnPage() {
        return "sign_un";
    }

    @RequestMapping(value = "/sign_un", method = RequestMethod.POST)
    public String doSignUp(String username, String password, String confirm_password, HttpServletRequest request) {
        ChatUser chatUser = chatUserService.getUserByLoginName(username);
        if (chatUser == null) {
            try {
                chatUserService.registerNewUser(username, password);
                request.login(username, password);
                return "redirect:/";
            } catch (Exception e) {
                return "sign_un";
            }
        } else {
            return "sign_un";
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletResponse response, @RequestParam(name = "file") String fileName){
        try {
            Path path = Paths.get(uploadedFolder + fileName);
            byte[] bytes = Files.readAllBytes(path);
            response.setContentType(Files.probeContentType(path));
            response.setContentLength(bytes.length);

            FileCopyUtils.copy(bytes, response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String upload(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();

        MultipartFile file = request.getFile(itr.next());

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadedFolder + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(file.getOriginalFilename() + " uploaded!");

        return "{status: 'OK' }";

    }
}
