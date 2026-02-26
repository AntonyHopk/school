package org.example.courseservice.security;

import org.springframework.stereotype.Component;

@Component
public class RequestPrincipalResolver {

    public RequestPrincipal fromHeaders(String userIdHeader, String roleHeader) {
        if (userIdHeader == null || roleHeader == null) {
            throw new IllegalArgumentException("Missing userId or roleHeader");
        }
        return new RequestPrincipal(Long.parseLong(userIdHeader), roleHeader);
    }
}
