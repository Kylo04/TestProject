package qa.automation.models.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenResponse {
    private String token_type;
    private String scope;
    private Integer expires_in;
    private Integer ext_expires_in;
    private String access_token;
}
