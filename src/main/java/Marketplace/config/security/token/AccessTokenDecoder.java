package Marketplace.config.security.token;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
