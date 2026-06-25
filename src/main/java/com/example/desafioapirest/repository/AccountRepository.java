package com.example.desafioapirest.repository;

import com.example.desafioapirest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {


}
