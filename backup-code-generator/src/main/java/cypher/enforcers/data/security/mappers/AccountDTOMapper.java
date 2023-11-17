package cypher.enforcers.data.security.mappers;

import cypher.enforcers.data.entities.AccountEntity;
import cypher.enforcers.data.security.dtos.Account;

import java.util.function.Function;

/**
 * Converts an account object to an account data-transfer-object.
 */
public class AccountDTOMapper implements Function<AccountEntity, Account> {

    /**
     * Create a data transfer mapper for the account entity.
     * <br>
     * Mainly here to avoid warnings.
     */
    public AccountDTOMapper() {

    }

    @Override
    public Account apply(AccountEntity account) {
        return new Account(account.getID(), account.getName(), account.getSocialMediaType());
    }
}
