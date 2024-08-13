package com.rest.api.model.board;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
@NoArgsConstructor
public class ParamsPost {
    @NotEmpty
    @Size(min = 2, max = 50)
    @Parameter(name = "author", description = "작성자명",required = true)
    private String author;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Parameter(name = "title", description = "제목",required = true)
    private String title;

    @NotEmpty
    @Size(min = 2, max = 500)
    @Parameter(name = "content", description = "내용",required = true)
    private String content;
}
