package edu.tamu.weaver.auth.resolver;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import edu.tamu.weaver.auth.annotation.WeaverUser;
import edu.tamu.weaver.auth.model.repo.AbstractWeaverUserRepo;
import edu.tamu.weaver.user.model.AbstractWeaverUser;
import edu.tamu.weaver.utility.AnnotationUtility;

public final class WeaverUserArgumentResolver<U extends AbstractWeaverUser, R extends AbstractWeaverUserRepo<U>> implements HandlerMethodArgumentResolver {

    private R userRepo;

    public WeaverUserArgumentResolver(R userRepo) {
        this.userRepo = userRepo;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return AnnotationUtility.findMethodAnnotation(WeaverUser.class, parameter) != null;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Optional<U> user = userRepo.findByUsername(authentication.getName());
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

}