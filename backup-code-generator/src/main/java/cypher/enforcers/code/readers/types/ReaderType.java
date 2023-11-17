package cypher.enforcers.code.readers.types;

/**
 The different platforms that support text import.
 */
public enum ReaderType {

    /** To read backup codes provided by Discord. */
    DISCORD,

    /** To read backup codes provided by GitHub. */
    GITHUB,

    /** To read backup codes provided by Google. */
    GOOGLE,

    /** To read backup codes provided by Shopify. */
    SHOPIFY
}
