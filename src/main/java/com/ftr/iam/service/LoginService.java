package com.ftr.iam.service;

import com.ftr.iam.entities.Users;
import com.ftr.iam.entities.UsersInfo;
import com.ftr.iam.repositories.UsersInfoRepository;
import com.ftr.iam.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final UsersInfoRepository usersInfoRepository;

    @Autowired
    public LoginService(final UsersRepository usersRepository, UsersInfoRepository usersInfoRepository) {
        this.usersRepository = usersRepository;
        this.usersInfoRepository = usersInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findByUsername(username);
        Optional<UsersInfo> userInfo = usersInfoRepository.findByUsername(username);

        if (user.isPresent() && userInfo.isPresent()) {
            Users users = user.get();
            UsersInfo usersInfo = userInfo.get();

            return User.builder()
                    .username(users.getUsername())
                    .password(usersInfo.getPasswordHash())
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
