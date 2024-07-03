package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CEmailSigninFailedException;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.entity.User;
import com.rest.api.model.reponse.CommonResult;
import com.rest.api.model.reponse.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Tag(name = "1. Sign", description = "Sign Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
@CrossOrigin
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "로그인", description = "이메일 회원 로그인")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@Parameter(name = "id", description = "회원ID : 이메일", required = true) @RequestParam(name = "id") String id,
                                       @Parameter(name = "password", description = "비밀번호", required = true) @RequestParam(name = "password") String password) {
        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @Operation(summary = "가입", description = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(
            @Parameter(name = "id", description = "회원ID : 이메일", required = true) @RequestParam(name = "id") String id,
            @Parameter(name = "password", description = "비밀번호", required = true) @RequestParam(name = "password") String password,
            @Parameter(name = "name", description = "이름", required = true) @RequestParam(name = "name") String name
    ) {
        userJpaRepo.save(User.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
        return responseService.getSuccessResult();
    }
}
