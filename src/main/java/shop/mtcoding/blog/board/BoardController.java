package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardRepository boardRepository;
    private final HttpSession session;


    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        if (requestDTO.getTitle().length() > 20) {
            request.setAttribute("msg", "잘못된 요청입니다.");
            request.setAttribute("status", 400);
            return "error/40x";
        }
        boardRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/";
    }


    @PostMapping("board/{id}/update")
    public String update(@PathVariable int id, BoardResponse.UpdateDTO requestDTO, HttpServletRequest request) {
        //1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        //2. 권한 체크
        BoardResponse.DetailDTO board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            request.setAttribute("msg", "권한이 없습니다.");
            request.setAttribute("status", 403);
            return "error/40x";
        }
        //3. 핵심 로직
        // 쿼리 >> update board_tb set title=? , content=? where id=?
        boardRepository.update(id, requestDTO);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {

        BoardResponse.DetailDTO responseDTO = boardRepository.findById(id);
        User sessionUser = (User) session.getAttribute("sessionUser");
        boolean pageOwner;
        if (sessionUser == null) {
            pageOwner = false;
        } else {
            int postOwnerId = responseDTO.getUserId();
            int sessionUserId = sessionUser.getId();
            pageOwner = postOwnerId == sessionUserId;
        }
        request.setAttribute("pageOwner", pageOwner);
        request.setAttribute("board", responseDTO);
        return "board/detail";
    }

    //   /board/saveForm 요청(Get)이 온다
    @GetMapping("/board/saveForm")
    public String saveForm() {
        // 세션 영역에 sessionUser 키값에 user 객체가 있는지 비교
        User user = (User) session.getAttribute("sessionUser");

        // 값이 null이면 로그인 페이지로 리다이렉션
        // 값이 null이 아니면 /board/saveForm 로 이동
        if (user == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }


    @GetMapping("/")
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();


        request.setAttribute("boardList", boardList);

        return "index";
    }


    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request) {
        //인증 확인
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        //권한 확인
        BoardResponse.DetailDTO board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            request.setAttribute("msg", "권한이 없습니다.");
            request.setAttribute("status", 403);
            return "error/40x";
        }
        //핵심 로직
        boardRepository.delete(id);
        return "redirect:/";
    }

    @GetMapping("board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        BoardResponse.DetailDTO detailDTO = boardRepository.findById(id);
        if (sessionUser.getId() != detailDTO.getUserId()) {
            request.setAttribute("msg", "권한이 없습니다.");
            request.setAttribute("status", 403);
        }

        request.setAttribute("board", detailDTO);
        return "board/updateForm";
    }


}
