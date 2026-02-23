package challenge;

import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
/* loaded from: EasyDB.jar:BOOT-INF/classes/challenge/UserController.class */
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping({"/"})
    public String index(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return BeanDefinitionParserDelegate.INDEX_ATTRIBUTE;
        }
        return "redirect:/login";
    }

    @GetMapping({"/login"})
    public String login() {
        return "login";
    }

    @PostMapping({"/login"})
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) throws SQLException {
        if (this.userService.validateUser(username, password)) {
            session.setAttribute("username", username);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping({"/logout"})
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
