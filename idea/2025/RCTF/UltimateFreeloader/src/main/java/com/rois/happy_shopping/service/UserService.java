package com.rois.happy_shopping.service;

import ch.qos.logback.classic.ClassicConstants;
import com.rois.happy_shopping.dto.UserLoginDTO;
import com.rois.happy_shopping.dto.UserRegisterDTO;
import com.rois.happy_shopping.entity.Coupon;
import com.rois.happy_shopping.entity.User;
import com.rois.happy_shopping.mapper.CouponMapper;
import com.rois.happy_shopping.mapper.UserMapper;
import com.rois.happy_shopping.util.JwtUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/service/UserService.class */
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Map<String, Object> register(UserRegisterDTO registerDTO) {
        Map<String, Object> result = new HashMap<>();
        if (this.userMapper.countByUsername(registerDTO.getUsername()) > 0) {
            result.put("success", false);
            result.put(ConstraintHelper.MESSAGE, "Username already exists");
            return result;
        }
        if (this.userMapper.countByEmail(registerDTO.getEmail()) > 0) {
            result.put("success", false);
            result.put(ConstraintHelper.MESSAGE, "Email already registered");
            return result;
        }
        User user = new User(registerDTO.getUsername(), this.passwordEncoder.encode(registerDTO.getPassword()), registerDTO.getEmail());
        this.userMapper.insert(user);
        Coupon welcomeCoupon = new Coupon(user.getId(), "Welcome Coupon", new BigDecimal("10.00"), LocalDateTime.now().plusDays(30L));
        this.couponMapper.insert(welcomeCoupon);
        String token = this.jwtUtil.generateToken(user.getUsername(), user.getId());
        result.put("success", true);
        result.put(ConstraintHelper.MESSAGE, "Registration successful");
        result.put("token", token);
        result.put(ClassicConstants.USER_MDC_KEY, user);
        return result;
    }

    public Map<String, Object> login(UserLoginDTO loginDTO) {
        Map<String, Object> result = new HashMap<>();
        User user = this.userMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            result.put("success", false);
            result.put(ConstraintHelper.MESSAGE, "Invalid username or password");
            return result;
        }
        if (!this.passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            result.put("success", false);
            result.put(ConstraintHelper.MESSAGE, "Invalid username or password");
            return result;
        }
        String token = this.jwtUtil.generateToken(user.getUsername(), user.getId());
        result.put("success", true);
        result.put(ConstraintHelper.MESSAGE, "Login successful");
        result.put("token", token);
        result.put(ClassicConstants.USER_MDC_KEY, user);
        return result;
    }

    public User findById(String id) {
        return this.userMapper.findById(id);
    }

    public boolean updateBalance(String userId, BigDecimal balance) {
        return this.userMapper.updateBalance(userId, balance) > 0;
    }
}
