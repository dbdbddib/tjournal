package com.example.tjournal.board;

import com.example.tjournal.category.CategoryEnum;
import com.example.tjournal.member.IMember;
import com.example.tjournal.member.MemberDto;
import com.example.tjournal.member.MemberServiceImpl;
import com.example.tjournal.security.config.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.tjournal.category.regionEnum;
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

    @Autowired
    private MemberServiceImpl memberService;


    @GetMapping("/board_ajx_list/{category}")
    private String boardAjxList(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            @PathVariable String category
    ) {
        IMember loginUser = (IMember)model.getAttribute(SecurityConfig.LOGINUSER);
        if ( loginUser == null ) {
            return "redirect:/";
        }
        try {
            CategoryEnum.valueOf(category);
        } catch (IllegalArgumentException e) {
            return "error/400"; // 유효하지 않은 값에 대한 처리
        }
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        model.addAttribute("region", "");
        model.addAttribute("category", category);
        return "board/board_ajx_list";
    }

    @GetMapping("/board_ajx_list/{region}/{category}")
    private String boardAjxRegionList(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            @Validated @PathVariable String region,
            @PathVariable String category
    ) {
        IMember loginUser = (IMember)model.getAttribute(SecurityConfig.LOGINUSER);
        if ( loginUser == null ) {
            return "redirect:/";
        }
        try {
            regionEnum.valueOf(region); // 예외 발생 시 유효하지 않은 값
            CategoryEnum.valueOf(category);
        } catch (IllegalArgumentException e) {
            return "error/400"; // 유효하지 않은 값에 대한 처리
        }
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        model.addAttribute("region", region);
        model.addAttribute("category", category);
        return "board/board_ajx_list";
    }

    @GetMapping("/board_ajx_id_list/{category}/{id}")
    private String boardAjxIdList(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            @PathVariable String category,
            @PathVariable Long id
    ) {
        IMember loginUser = (IMember)model.getAttribute(SecurityConfig.LOGINUSER);
        if ( loginUser == null ) {
            return "redirect:/";
        }
        try {
            CategoryEnum.valueOf(category);
        } catch (IllegalArgumentException e) {
            return "error/400"; // 유효하지 않은 값에 대한 처리
        }
        IMember memberDto = this.memberService.findById(id);
        model.addAttribute("boardTbl", new BoardDto().getTbl());
        model.addAttribute("region", "");
        model.addAttribute("category", category);
        model.addAttribute("loginId", loginUser.getId());
        model.addAttribute("memberDto", memberDto);
        return "board/board_ajx_Id_list";
    }

    @GetMapping("/board_ajx_my_list/{category}")
    private String myBoard(Model model
                           , @PathVariable String category) {
        IMember loginUser = (IMember)model.getAttribute(SecurityConfig.LOGINUSER);
        if ( loginUser == null ) {
            return "redirect:/";
        }
        try {
            CategoryEnum.valueOf(category);
        } catch (IllegalArgumentException e) {
            return "error/400"; // 유효하지 않은 값에 대한 처리
        }
        IMember memberDto = this.memberService.findById(loginUser.getId());
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("region", "");
        model.addAttribute("loginId", loginUser.getId());
        model.addAttribute("category", category);

        return "board/board_ajx_Id_list";
    }

    @GetMapping("/board_add")
    private String allNoticeBoardAdd(Model model) {
        model.addAttribute("region", "");
        return "board/board_add";
    }

    @GetMapping("/board_add/{region}")
    private String regionBoardAdd(Model model, @PathVariable String region) {
        model.addAttribute("region", region);
        try {
            regionEnum.valueOf(region); // 예외 발생 시 유효하지 않은 값
        } catch (IllegalArgumentException e) {
            return "error/400"; // 유효하지 않은 값에 대한 처리
        }
        return "board/board_add";
    }

    @GetMapping("/board_aws_add")
    private String allAwsBoardAdd(Model model) {
        model.addAttribute("region", "");
        return "board/board_aws_add";
    }

    @GetMapping("/board_aws_add/{region}")
    private String regionAwsBoardAdd(Model model, @PathVariable String region) {
        model.addAttribute("region", region);
        try {
            regionEnum.valueOf(region); // 예외 발생 시 유효하지 않은 값
        } catch (IllegalArgumentException e) {
            return "error/400"; // 유효하지 않은 값에 대한 처리
        }
        return "board/board_aws_add";
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
