package com.peertutor.TuitionOrderMgr.service.mapper;

import com.peertutor.TuitionOrderMgr.model.Account;
import com.peertutor.TuitionOrderMgr.service.dto.AccountDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link com.peertutor.AccountMgr.model.Account} and its DTO {@link AccountDTO}.
 */
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
