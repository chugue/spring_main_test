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

    @PostMapping("board/{id}/update")
    public String update (@PathVariable int id, BoardResponse.UpdateDTO requestDTO, HttpServletRequest request){
        //1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null){
            return "redirect:/loginForm";
        }
        //2. 권한 체크
        BoardResponse.DetailDTO board = boardRepository.findById(id);
        if (board.getUserId()!=sessionUser.getId()){
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

        // 모델에게 위임하고 상세데이터를 가져온다.
        BoardResponse.DetailDTO responseDTO = boardRepository.findById(id);

        User sessionUser = (User) session.getAttribute("sessionUser");
        boolean pageOwner;
        if (sessionUser == null){
            pageOwner = false;
        } else {
            int postOwnerId = responseDTO.getUserId();
            int sessionUserId = sessionUser.getId();
            pageOwner = postOwnerId == sessionUserId;
        }
        // mustache에서 아래 키워드와 매핑된 곳에 뿌려줌.
        request.setAttribute("pageOwner", pageOwner);
        request.setAttribute("board", responseDTO);
        return "board/detail";
    }

    //   /board/saveForm 요청(Get)이 온다
    @GetMapping("/board/saveForm")
    public String saveForm() {
        // 세션 영역에 sessionUser 키값에 user 객체가 있는지 비교
        User user = (User)session.getAttribute("sessionUser");

        // 값이 null이면 로그인 페이지로 리다이렉션
        // 값이 null이 아니면 /board/saveForm 로 이동
        if (user == null ){
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }


    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();

        // 테스트용 코드
        for (Board a : boardList){
            System.out.println(a);
        }
        request.setAttribute("boardList", boardList);
        return "index";
    }


    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request){
        //인증 확인
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null){
            return "redirect:/loginForm";
        }
        //권한 확인
        BoardResponse.DetailDTO board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()){
            request.setAttribute("msg", "권한이 없습니다.");
            request.setAttribute("status", 403);
            return "error/40x";
        }
        //핵심 로직
        boardRepository.delete  (id);
        return "redirect:/";
    }

    @GetMapping("board/{id}/updateForm")
    public String updateForm (@PathVariable int id, HttpServletRequest request){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null){
            return "redirect:/loginForm";
        }
        BoardResponse.DetailDTO detailDTO = boardRepository.findById(id);
        if (sessionUser.getId() != detailDTO.getUserId()){
            request.setAttribute("msg", "권한이 없습니다.");
            request.setAttribute("status", 403);
        }

        request.setAttribute("board", detailDTO);
        return "board/updateForm";
    }


}
