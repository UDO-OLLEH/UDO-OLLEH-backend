package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.BoardService;
import com.udoolleh.backend.web.dto.RequestBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/udo/board/register")
    public Long create(@RequestBody RequestBoard requestBoard){
        return boardService.create(requestBoard);
    }

}
