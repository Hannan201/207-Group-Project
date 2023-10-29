package cypher.enforcers.data.security;

import cypher.enforcers.models.Account;

import java.util.function.Function;

/**
 * Converts an account object to an account data-transfer-object.
 */
public class AccountDTOMapper implements Function<Account, AccountDTO> {
    @Override
    public AccountDTO apply(Account account) {
        return new AccountDTO(account.getID(), account.getName(), account.getSocialMediaType());
    }
}
