package com.example.ecommerce.service;

import com.example.ecommerce.configuration.JwtRequestFilter;
import com.example.ecommerce.dao.OrderDetailsDAO;
import com.example.ecommerce.dao.ProductDAO;
import com.example.ecommerce.dao.UserDao;
import com.example.ecommerce.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailsDAO orderDetailsDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private UserDao userDao;

    private static final String ORDER_PLACE = "Placed";

    public void placeOrder(OrderInput orderInput) {
        List<CountProductQuantity> countProductQuantityList = orderInput.getCountProductQuantityList();

        for (CountProductQuantity o : countProductQuantityList) {
            Product product = productDAO.findById(o.getProductId()).get();

            String currentUser = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(currentUser).get();

            OrderDetail orderDetail = new OrderDetail(
                    orderInput.getFullName(),
                    orderInput.getAddress(),
                    orderInput.getContactNumber(),
                    orderInput.getEmail(),
                    ORDER_PLACE,
                    product.getProductDiscountPrice() * o.getQuantity(),
                    product,
                    user
            );
            orderDetailsDAO.save(orderDetail);
        }
    }
}