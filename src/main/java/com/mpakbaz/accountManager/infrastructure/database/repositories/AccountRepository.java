package com.mpakbaz.accountManager.infrastructure.database.repositories;

import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
