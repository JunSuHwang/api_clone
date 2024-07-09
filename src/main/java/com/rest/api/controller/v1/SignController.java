package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CEmailSigninFailedException;
import com.rest.api.advice.exception.CUserExistException;
import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.entity.User;
import com.rest.api.model.reponse.CommonResult;
import com.rest.api.model.reponse.SingleResult;
import com.rest.api.model.social.KakaoProfile;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import com.rest.api.service.social.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

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
    private final KakaoService kakaoService;


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

    @Operation(summary = "소셜 로그인", description = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/signin/{provider}")
    public SingleResult<String> signinByProvider(
            @Parameter(name = "provider", description = "서비스 제공자 provider", required = true, in = ParameterIn.PATH, schema = @Schema(defaultValue = "kakao")) @PathVariable("provider") String provider,
            @Parameter(name = "accessToken", description = "소셜 accessToken", required = true) @RequestParam(name = "accessToken") String accessToken
    ) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        User user = userJpaRepo.findByUidAndProvider(String.valueOf((profile.getId())), provider).orElseThrow(CUserNotFoundException::new);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @Operation(summary = "소셜 계정 가입", description = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/signup/{provider}")
    public CommonResult signupByProvider(
            @Parameter(name = "provider", description = "서비스 제공자 provider", required = true, in = ParameterIn.PATH, schema = @Schema(defaultValue = "kakao")) @PathVariable("provider") String provider,
            @Parameter(name = "accessToken", description = "소셜 accessToken", required = true) @RequestParam(name = "accessToken") String accessToken,
            @Parameter(name = "name", description = "이름", required = true) @RequestParam(name = "name") String name
    ) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<User> user = userJpaRepo.findByUidAndProvider(String.valueOf((profile.getId())), provider);
        if(user.isPresent())
            throw new CUserExistException();

        userJpaRepo.save(User.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
        return responseService.getSuccessResult();
    }
}
