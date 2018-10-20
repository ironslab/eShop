package com.hercule.eshop.services;

import com.hercule.eshop.models.Cart;
import com.hercule.eshop.models.User;
import com.hercule.eshop.repositories.RoleRepository;
import com.hercule.eshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;


public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user)
    {
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Cart userCart = new Cart();
        userCart.setUser(user);
        cartService.saveCart(userCart);
    }

    @Override
    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUserId(long id)
    {
        return userRepository.findOneById(id);
    }

    @Override
    public void deleteUserById(long id)
    {
        User user = findByUserId(id);
        Cart cart = cartService.findCartByUserId(user);
        cartService.deleteCart(cart);
        userRepository.delete(user);
    }

    @Override
    public List<User> searchUserByUsername(String name)
    {
        return userRepository.findByUsernameContaining(name.toLowerCase());
    }

    @Override
    public void updateUser(User user)
    {
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        User dbUser = findByUserId(user.getId());
        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(user.getPassword());
        dbUser.setRoles(user.getRoles());

        userRepository.save(dbUser);

    }


}
