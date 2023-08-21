package com.peertutor.TuitionOrderMgr.util.mapper;

import com.peertutor.TuitionOrderMgr.util.dto.AccountDTO;
import com.peertutor.TuitionOrderMgr.util.model.Account;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AccountMapper extends EntityMapper<AccountDTO, Account> {

    Account toEntity(AccountDTO accountDTO);

    AccountDTO toDto(Account account);

    default Account fromId(Long id) {
        if (id == null) {
            return null;
        }
        Account account = new Account();
        account.setId(id);
        return account;
    }
}
