package io.github.k_gregory.insurance;

import io.github.k_gregory.insurance.entity.User;
import io.github.k_gregory.insurance.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
class Some {
    private final UserService userService;
    private final RememberMeServices rememberMeServices;
    @Value("${app.remember-me.parameter-name}")
    private String rememberMeParameter;

    @Autowired
    public Some(UserService userService, RememberMeServices rememberMeServices) {
        this.userService = userService;
        this.rememberMeServices = rememberMeServices;
    }

    @GetMapping("/register/check")
    @ResponseBody
    public String some(Principal principal, HttpServletRequest request) {
        return principal.getName();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register/{login}/{pass}")
    @ResponseBody
    public String register(
            @PathVariable String login,
            @PathVariable String pass,
            HttpServletRequest request,
            HttpServletResponse response) {
        List<String> roles = Collections.singletonList("USER");
        List<GrantedAuthority> grantedAuthorities = roles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User user = userService.register(
                login, pass, roles
        );
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                login, null, grantedAuthorities
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        rememberMeServices.loginSuccess(new RequestWithRememberMe(request), response, token);
        return user.toString();
    }

    class RequestWithRememberMe extends HttpServletRequestWrapper {
        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        public RequestWithRememberMe(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            if (rememberMeParameter.equals(name))
                return "true";
            else
                return super.getParameter(name);
        }
    }
}

@SpringBootApplication
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
