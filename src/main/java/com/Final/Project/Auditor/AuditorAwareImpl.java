package com.Final.Project.Auditor;

import com.Final.Project.entity.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth==null || auth instanceof AnonymousAuthenticationToken){
            return Optional.of(-1);
        }
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        return Optional.of(userPrincipal.getId());
    }
}
