package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.entity.User;
import com.rest.api.model.reponse.CommonResult;
import com.rest.api.model.reponse.ListResult;
import com.rest.api.model.reponse.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "1. User", description = "User Controller")
@RequiredArgsConstructor
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1")
public class UserController {
    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service

    @Operation(summary = "회원 조회", description = "모든 회원을 조회한다")
    @GetMapping(value = "/user")
    public ListResult<User> findAllUser() {
        // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
        return responseService.getListResult(userJpaRepo.findAll());
    }

    @Operation(summary = "회원 단건 조회", description = "userId로 회원을 조회한다")
    @GetMapping(value = "/user/{msrl}")
    public SingleResult<User> findUserById(@Parameter(name = "msrl",description = "회원Id", required = true)
                                               @PathVariable("msrl") long msrl) {
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
    }

    @Operation(summary = "회원 입력", description = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@Parameter(name = "uid", description = "회원아이디",required = true)
                         @RequestParam("uid") String uid,
                                   @Parameter(name = "name", description = "회원이름",required = true)
                        @RequestParam("name") String name) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Operation(summary = "회원 수정", description = "회원정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @Parameter(description = "회원번호", required = true) @RequestParam("msrl") long msrl,
            @Parameter(description = "회원아이디", required = true) @RequestParam("uid") String uid,
            @Parameter(description = "회원이름", required = true) @RequestParam("name") String name
    ) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Operation(summary = "회원 삭제", description = "userId로 회원정보를 삭제한다.")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @Parameter(description = "회원번호", required = true)
            @PathVariable("msrl") long msrl
    ) {
        userJpaRepo.deleteById(msrl);
        // 성공 결과 정보만 필요한 경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}
