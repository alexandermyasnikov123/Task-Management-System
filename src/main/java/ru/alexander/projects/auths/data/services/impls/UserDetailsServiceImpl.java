package ru.alexander.projects.auths.data.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alexander.projects.auths.data.entities.UserDetailsDelegate;
import ru.alexander.projects.auths.data.repositories.UserDetailsRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDetailsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsername(username)
                .map(UserDetailsDelegate::new)
                .orElseThrow(() -> new UsernameNotFoundException("errors.cant-find-user.username.details"));
    }
}
