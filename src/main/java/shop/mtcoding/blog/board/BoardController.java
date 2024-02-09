package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardRepository boardRepository;
    private final HttpSession session;

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {

        // 모델에게 위임하고 상세데이터를 가져온다.
        BoardResponse.DetailDTO responseDTO = boardRepository.findById(id);

        // 현재 사용자가 로그인된 사용자인지 체크 (세션과 매칭하는지 체크)
        User sessionUser = (User) session.getAttribute("sessionUser");
        boolean pageOwner;
        if (sessionUser == null){
            //sessionUser에 값을 찾을 수 없다면 로그인하지 않은 사람
            pageOwner = false;
        } else {
            // 게시물 주인 id와 session 사용자 id를 비교해서, 게시물의 수정 권한이 있는지 체크.
            int postOwnerId = responseDTO.getUserId();
            int sessionUserId = sessionUser.getId();
            pageOwner = postOwnerId == sessionUserId;
        }
        // mustache에서 아래 키워드와 매핑된 곳에 뿌려줌.
        request.setAttribute("board", responseDTO);
        request.setAttribute("pageOwner", pageOwner);

        // request가방에 담아서 화면에 렌더링할 수 있게해준다.
        // 아래 코드 예시로는 mustache에서 {{board}} 같이 문법을 쓰면 정보를 가져다 쓸 수 있다.
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

        request.setAttribute("boardList", boardList);
        return "index";
    }



}
