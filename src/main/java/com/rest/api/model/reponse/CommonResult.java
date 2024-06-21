package com.rest.api.model.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    @Schema(description = "응답 성공여부 : true/false")
    private boolean success;

    @Schema(description = "응답 코드 번호 : >= 0 정상, < 0 비정상")
    private int code;

    @Schema(description = "응답 메세지")
    private String msg;
}
