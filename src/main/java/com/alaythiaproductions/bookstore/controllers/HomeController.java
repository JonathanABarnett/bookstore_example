package com.alaythiaproductions.bookstore.controllers;

import com.alaythiaproductions.bookstore.models.User;
import com.alaythiaproductions.bookstore.models.security.PasswordResetToken;
import com.alaythiaproductions.bookstore.models.security.Role;
import com.alaythiaproductions.bookstore.models.security.UserRole;
import com.alaythiaproductions.bookstore.services.UserSecurityService;
import com.alaythiaproductions.bookstore.services.UserService;
import com.alaythiaproductions.bookstore.utility.MailConstructor;
import com.alaythiaproductions.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


@Controller
public class HomeController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("title", "Bookstore Home");
        return "views/index";
    }

    @GetMapping(value = "/createAccount")
    public String createAccount(Locale locale, @RequestParam("token") String token, Model model) {
        model.addAttribute("title", "Create Account");

        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

        if (passwordResetToken == null) {
            String message = "Invalid Token";
            model.addAttribute("message", message);
            return "redirect:/views/badRequest";
        }

        User user = passwordResetToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("classActiveEdit", true);


        return "views/myProfile";
    }

    @PostMapping(value = "/createAccount")
    public String newUserProcess(HttpServletRequest request, @ModelAttribute("email") String userEmail,
                                 @ModelAttribute("username") String username, Model model) throws Exception {
        model.addAttribute("classActiveCreate", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        if (userService.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);

            return "views/myAccount";
        }

        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("email", true);

            return "views/myAccount";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokeForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        javaMailSender.send(email);

        model.addAttribute("emailSent", true);

        return "views/myAccount";

    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("classActiveLogin", true);
        return "views/myAccount";
    }

    @GetMapping(value = "/forgotPassword")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        model.addAttribute("classActiveForgot", true);
        return "views/myAccount";
    }

}
