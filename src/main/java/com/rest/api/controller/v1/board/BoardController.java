package com.rest.api.controller.v1.board;

import com.rest.api.entity.board.Board;
import com.rest.api.entity.board.Post;
import com.rest.api.model.board.ParamsPost;
import com.rest.api.model.reponse.CommonResult;
import com.rest.api.model.reponse.ListResult;
import com.rest.api.model.reponse.SingleResult;
import com.rest.api.service.ResponseService;
import com.rest.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "3. Board", description = "Board Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    @Operation(summary = "게시판 정보 조회", description = "게시판 정보를 조회한다.")
    @GetMapping(value = "/{boardName}")
    public SingleResult<Board> boardInfo(@Parameter(name = "boardName", required = true ,in = ParameterIn.PATH) @PathVariable(value = "boardName") String boardName) {
        return responseService.getSingleResult(boardService.findBoard(boardName));
    }

    @Operation(summary = "게시판 글 리스트", description = "게시판 게시글 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public ListResult<Post> posts(@Parameter(name = "boardName", required = true ,in = ParameterIn.PATH) @PathVariable(value = "boardName") String boardName) {
        return responseService.getListResult(boardService.findPosts(boardName));
    }

    @Operation(summary = "게시판 글 작성", description = "게시판에 글을 작성한다.")
    @PostMapping(value = "/{boardName}")
    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token",required = true, in = ParameterIn.HEADER)
    })
    public SingleResult<Post> post(
            @Valid @ModelAttribute ParamsPost post,
            @Parameter(name = "boardName", required = true, in = ParameterIn.PATH) @PathVariable(value = "boardName") String boardName
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        return responseService.getSingleResult(boardService.writePost(uid, boardName, post));
    }

    @Operation(summary = "게시판 글 상세", description = "게시판 글 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public SingleResult<Post> post(@Parameter(name = "postId", required = true, in = ParameterIn.PATH) @PathVariable(value = "postId") long postId) {
        return responseService.getSingleResult(boardService.getPost(postId));
    }

    @Operation(summary = "게시판 글 수정", description = "게시판의 글을 수정한다.")
    @PutMapping(value = "/post/{postId}")
    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true, in = ParameterIn.HEADER)
    })
    public SingleResult<Post> post(
            @Parameter(name = "postId", required = true, in = ParameterIn.PATH) @PathVariable(value = "postId") long postId,
            @Valid @ModelAttribute ParamsPost post
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        return responseService.getSingleResult(boardService.updatePost(postId, uid, post));
    }

    @Operation(summary = "게시판 글 삭제", description = "게시판의 글을 삭제한다.")
    @DeleteMapping(value = "/post/{postId}")
    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true, in = ParameterIn.HEADER)
    })
    public CommonResult deletePost(@Parameter(name = "postId", required = true, in = ParameterIn.PATH) @PathVariable(value = "postId") long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        boardService.deletePost(postId, uid);
        return responseService.getSuccessResult();
    }
}
