package com.javiles.eshop.services;

import com.javiles.eshop.models.Cart;
import com.javiles.eshop.models.CartItem;
import com.javiles.eshop.models.Order;
import com.javiles.eshop.models.OrderItem;
import com.javiles.eshop.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService
{
    private final String COMPLETE = "COMPLETED";
    private final String PENDING = "PENDING";
    private final String CANCELLED = "CANCELLED";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Override
    public void createOrder(Cart cart)
    {
        double orderTotal = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        Order order = new Order();
        order.setStatus(PENDING);
        order.setUser(cart.getUser());

        for (CartItem cartItem : cart.getCartItem())
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setPrice(orderItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotal(cartItem.getTotal());
            orderTotal += orderItem.getTotal();
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }

        order.setOrderItems(orderItemList);
        order.setTotal(orderTotal);
        order.setSize(orderItemList.size());

        orderRepository.save(order);

        cartService.emptyCart(cart);
    }

    @Override
    public Order getOrderByUserId(long userId)
    {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void completeOrderByUserId(long userId)
    {
        Order order = orderRepository.findByUserId(userId);
        if (order.getStatus().equals(PENDING))
        {
            order.setStatus(COMPLETE);
            orderRepository.save(order);
        }

    }

    @Override
    public void cancelOrderByUserId(long userId)
    {
        Order order = orderRepository.findByUserId(userId);
        if (order.getStatus().equals(PENDING))
        {
            order.setStatus(CANCELLED);
            orderRepository.save(order);
        }
    }
}
