package cypher.enforcers.data.security;

import cypher.enforcers.data.entities.AccountEntity;

import java.util.function.Function;

/**
 * Converts an account object to an account data-transfer-object.
 */
public class AccountDTOMapper implements Function<AccountEntity, Account> {
    @Override
    public Account apply(AccountEntity account) {
        return new Account(account.getID(), account.getName(), account.getSocialMediaType());
    }
}
