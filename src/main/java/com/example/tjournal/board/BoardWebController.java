package com.example.tjournal.board;

import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.member.IMember;
import com.example.tjournal.security.config.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("category", "");
        return "board/board_ajx_list";
    }

    @GetMapping("/board_ajx_list/{category}")
    private String boardAjxCategoryList(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            @Validated @PathVariable String category
    ) {
        IMember loginUser = (IMember)model.getAttribute(SecurityConfig.LOGINUSER);
        if ( loginUser == null ) {
            return "redirect:/";
        }
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        model.addAttribute("category", category);
        return "board/board_ajx_list";
    }

    @GetMapping("/board_add")
    private String allNoticeBoardAdd(Model model) {
        model.addAttribute("category", "");
        return "board/board_add";
    }

    @GetMapping("/board_add/{category}")
    private String categoryBoardAdd(Model model, @PathVariable String category) {
        model.addAttribute("category", category);
        return "board/board_add";
    }

    @GetMapping("/board_view")
    private String allNoticeBoardView(@RequestParam("id") Long id, Model model) {
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        model.addAttribute("boardDto", boardDto);
        return "board/board_view";
    }

    @GetMapping("/board_update")
    private String freeBoardUpdate(Model model) {
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        return "board/board_update";
    }
}
