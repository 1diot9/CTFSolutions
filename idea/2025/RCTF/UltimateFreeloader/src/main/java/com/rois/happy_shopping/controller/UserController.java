package com.rois.happy_shopping.controller;

import com.rois.happy_shopping.common.Result;
import com.rois.happy_shopping.dto.UserLoginDTO;
import com.rois.happy_shopping.dto.UserRegisterDTO;
import com.rois.happy_shopping.entity.User;
import com.rois.happy_shopping.service.UserService;
import com.rois.happy_shopping.util.JwtUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/api/user"})
@RestController
@CrossOrigin(origins = {"*"})
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/controller/UserController.class */
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping({"/register"})
    public Result<?> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        Map<String, Object> result = this.userService.register(registerDTO);
        if (((Boolean) result.get("success")).booleanValue()) {
            return Result.success("Registration successful", result);
        }
        return Result.error((String) result.get(ConstraintHelper.MESSAGE));
    }

    @PostMapping({DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL})
    public Result<?> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        Map<String, Object> result = this.userService.login(loginDTO);
        if (((Boolean) result.get("success")).booleanValue()) {
            return Result.success("Login successful", result);
        }
        return Result.error((String) result.get(ConstraintHelper.MESSAGE));
    }

    @GetMapping({"/info"})
    public Result<User> getUserInfo(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        User user = this.userService.findById(userId);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error("User not found");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
