package com.rois.happy_shopping.controller;

import com.rois.happy_shopping.common.Result;
import com.rois.happy_shopping.dto.OrderRequestDTO;
import com.rois.happy_shopping.entity.Order;
import com.rois.happy_shopping.service.OrderService;
import com.rois.happy_shopping.util.JwtUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/api/order"})
@RestController
@CrossOrigin(origins = {"*"})
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/controller/OrderController.class */
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping({"/create"})
    public Result<?> createOrder(@Valid @RequestBody OrderRequestDTO orderRequest, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        Map<String, Object> result = this.orderService.createOrder(userId, orderRequest);
        if (((Boolean) result.get("success")).booleanValue()) {
            return Result.success("Order created successfully", result);
        }
        return Result.error((String) result.get(ConstraintHelper.MESSAGE));
    }

    @GetMapping({"/my"})
    public Result<List<Order>> getMyOrders(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        List<Order> orders = this.orderService.getUserOrders(userId);
        return Result.success("Get order list successfully", orders);
    }

    @GetMapping({"/{id}"})
    public Result<Order> getOrderById(@PathVariable String id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        Order order = this.orderService.getOrderById(id);
        if (order != null) {
            String userId = this.jwtUtil.getUserIdFromToken(token);
            if (!order.getUserId().equals(userId)) {
                return Result.error(403, "No permission to access this order");
            }
            return Result.success("Get order details successfully", order);
        }
        return Result.error("Order not found");
    }

    @PostMapping({"/refund/{id}"})
    public Result<?> refundOrder(@PathVariable String id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        Map<String, Object> result = this.orderService.refundOrder(id, userId);
        if (((Boolean) result.get("success")).booleanValue()) {
            return Result.success("Refund successful", result);
        }
        return Result.error((String) result.get(ConstraintHelper.MESSAGE));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
