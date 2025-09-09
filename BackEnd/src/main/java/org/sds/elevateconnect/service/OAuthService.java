package org.sds.elevateconnect.service;

import org.sds.elevateconnect.model.Token;

public interface OAuthService {
    public Token refreshMiroToken();
    public Token refreshGoogleDocsToken();
}
