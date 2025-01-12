package com.example.tjournal.board;

import com.example.tjournal.member.IMember;
import com.example.tjournal.security.config.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardWebController {
    @Autowired
    private BoardServiceImpl boardService;


    @GetMapping("/board_ajx_list")
    private String boardAjxList(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        IMember loginUser = (IMember)model.getAttribute(SecurityConfig.LOGINUSER);
        if ( loginUser == null ) {
            return "redirect:/";
        }
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        return "board/board_ajx_list";
    }

    @GetMapping("/board_add")
    private String allNoticeBoardAdd() {
        return "board/board_add";
    }

    @GetMapping("/allnoticeboard_view")
    private String allNoticeBoardView(@RequestParam("id") Long id, Model model) {
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("allNoticeBoardTbl", new BoardDto().getTbl());
        return "board/board_view";
    }
}
