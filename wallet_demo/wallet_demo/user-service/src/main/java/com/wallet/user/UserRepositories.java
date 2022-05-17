package com.wallet.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositories extends JpaRepository<User,Integer> {
    public User findByEmail( String email);
}
