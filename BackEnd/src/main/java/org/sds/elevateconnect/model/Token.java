package org.sds.elevateconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private Integer id;
    private String type;
    private String refreshToken;
    private String accessToken;
}
