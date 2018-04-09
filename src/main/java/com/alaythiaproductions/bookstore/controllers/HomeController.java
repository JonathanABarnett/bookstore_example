package com.alaythiaproductions.bookstore.controllers;

import com.alaythiaproductions.bookstore.models.*;
import com.alaythiaproductions.bookstore.models.security.PasswordResetToken;
import com.alaythiaproductions.bookstore.models.security.Role;
import com.alaythiaproductions.bookstore.models.security.UserRole;
import com.alaythiaproductions.bookstore.services.*;
import com.alaythiaproductions.bookstore.utility.MailConstructor;
import com.alaythiaproductions.bookstore.utility.SecurityUtility;
import com.alaythiaproductions.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;


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

    @Autowired
    private BookService bookService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("title", "Bookstore Home");
        return "views/index";
    }

    @GetMapping(value = "/hours")
    public String hours(Model model) {
        model.addAttribute("title", "Hours and Directions");
        return "views/hours";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("classActiveLogin", true);
        return "views/myAccount";
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

        model.addAttribute("user", user);
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
            model.addAttribute("emailExists", true);

            return "views/myAccount";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

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

    @PostMapping(value = "/updateUserInfo")
    public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
                                 Model model) throws Exception {

        User currentUser = userService.findById(user.getId());

        if (currentUser == null) {
            throw new Exception("User Not Found!");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
                model.addAttribute("emailExists", true);
                model.addAttribute("classActiveEdit", true);
                return "views/myProfile";
            }
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            if(userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
                model.addAttribute("usernameExists", true);
                model.addAttribute("classActiveEdit", true);
                return "views/myProfile";
            }
        }

        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);
                model.addAttribute("classActiveEdit", true);
                return "views/myProfile";
            }
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        userService.save(currentUser);

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "views/myProfile";

    }

    @GetMapping(value = "/myProfile")
    public String loggedIn(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddresses", true);
        List<String> stateList = USConstants.listOfUSStateCode;
        Collections.sort(stateList);

        model.addAttribute("stateList", stateList);


        return "views/myProfile";
    }

    @RequestMapping(value = "/bookshelf")
    public String bookshelf(Model model, Principal principal) {

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("activeAll", true);
        return "views/bookshelf";
    }

    @RequestMapping(value = "/bookDetail")
    public String bookDetail(@PathParam("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Book book = bookService.findOne(id);

        model.addAttribute("book", book);

        List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        model.addAttribute("qtyList", qtyList);

        model.addAttribute("qty", 1);

        return "views/bookDetail";
    }

    @RequestMapping(value = "/listOfCreditCards")
    public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {
        model.addAttribute("title", "List of Credit Cards");

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "views/myProfile";
    }

    @GetMapping(value = "/addNewCreditCard")
    public String addNewCreditCard(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddress", true);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();

        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);

        List<String> stateList = USConstants.listOfUSStateCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "views/myProfile";
    }

    @PostMapping(value = "/addNewCreditCard")
    public String processAddNewCreditCard(Model model, @ModelAttribute("userPayment") UserPayment userPayment,
                                          @ModelAttribute("userBilling") UserBilling userBilling, Principal principal) {
        model.addAttribute("title", "Add New Credit Card");

        User user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        model.addAttribute("user", user);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "views/myProfile";

    }

    @RequestMapping(value = "/updateCreditCard")
    public String updateCreditCard(Model model, @ModelAttribute("id") long creditCardId, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if (user.getId() != userPayment.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userPayment", userPayment);
            model.addAttribute("userBilling", userBilling);

            List<String> stateList = USConstants.listOfUSStateCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            return "views/myProfile";
        }
    }

    @RequestMapping(value = "/removeCreditCard")
    public String removeCreditCard(Model model, @ModelAttribute("id") long creditCardId, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if (user.getId() != userPayment.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);

            userPaymentService.removeById(creditCardId);

            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            return "views/myProfile";
        }
    }

    @PostMapping(value = "/setDefaultPayment")
    public String setDefaultPayment(Model model, @ModelAttribute("defaultUserPaymentId") long userPaymentId, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(userPaymentId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        return "views/myProfile";
    }

    @RequestMapping(value = "/listOfShippingAddresses")
    public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
        model.addAttribute("title", "List of Shipping Addresses");

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("aNSA", true);

        return "views/myProfile";
    }

    @GetMapping(value = "/addNewShippingAddress")
    public String addNewShippingAddress(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);

        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        List<String> stateList = USConstants.listOfUSStateCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);

        model.addAttribute("user", user);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("orderList", user.getOrderList());

        return "views/myProfile";
    }

    @PostMapping(value = "/addNewShippingAddress")
    public String processAddNewShippingAddress(Model model, @ModelAttribute("userShipping") UserShipping userShipping,
                                               Principal principal) {
        model.addAttribute("title", "Add New Shipping Address");

        User user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        model.addAttribute("user", user);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);

        return "views/myProfile";

    }

    @RequestMapping(value = "/updateUserShipping")
    public String updateUserShipping(Model model, @ModelAttribute("id") long shippingAddressId, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if (user.getId() != userShipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);

            model.addAttribute("userShipping", userShipping);

            List<String> stateList = USConstants.listOfUSStateCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("orderList", user.getOrderList());

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            return "views/myProfile";
        }
    }

    @RequestMapping(value = "/removeUserShipping")
    public String removeUserShipping(Model model, @ModelAttribute("id") long userShippingId, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(userShippingId);

        if (user.getId() != userShipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);

            userShippingService.removeById(userShippingId);

            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());

            return "views/myProfile";
        }
    }

    @PostMapping(value = "/setDefaultShippingAddress")
    public String setDefaultShippingAddress(Model model, @ModelAttribute("defaultShippingAddressId") long defaultShippingtId, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingtId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());

        return "views/myProfile";
    }

    @RequestMapping(value = "/orderDetail")
    public String orderDetail(@RequestParam("id") long orderId, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        Order order = orderService.findOne(orderId);

        if (order.getUser().getId() != user.getId()) {
            return "badRequestPage";
        } else {
            List<CartItem> cartItemList = cartItemService.findByOrder(order);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("user", user);
            model.addAttribute("order", order);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            model.addAttribute("orderList", user.getOrderList());

            UserShipping userShipping = new UserShipping();

            model.addAttribute("userShipping", userShipping);

            List<String> stateList = USConstants.listOfUSStateCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveOrders", true);
            model.addAttribute("displayOrderDetail", true);
            model.addAttribute("listOfCreditCards", true);

            return "views/myProfile";
        }
    }

    @RequestMapping(value = "/forgotPassword")
    public String forgotPassword(HttpServletRequest request, @ModelAttribute("recoverEmail") String userEmail,
                                 @ModelAttribute("fName") String username, Model model) {
        model.addAttribute("title", "Forgot Password");
        model.addAttribute("classActiveForgot", true);

        User user = userService.findByEmail(userEmail);
        System.out.println(userEmail);


        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "views/myAccount";
        }

        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokeForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        javaMailSender.send(newEmail);

        model.addAttribute("forgetPasswordEmailSent", true);

        return "views/myAccount";
    }


}
//H3z3kiahCliveB@rnett
