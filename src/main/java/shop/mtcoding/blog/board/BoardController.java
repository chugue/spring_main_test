package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardRepository boardRepository;
    private final HttpSession session;



    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();

        request.setAttribute("boardList", boardList);
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {

        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {

        // 모델에게 위임하고 반환된 값을 responseDTO에 저장
        BoardResponse.DetailDTO responseDTO = boardRepository.findById(id);

        // request가방에 담아서 화면에 렌더링할 수 있게해준다.
        // 아래 코드 예시로는 mustache에서 {{board}} 같이 문법을 쓰면 정보를 가져다 쓸 수 있다.
        request.setAttribute("board", responseDTO);
        return "board/detail";
    }

    @PostMapping("/board/save")
    public String save (BoardRequest.SaveDTO requestDTO, HttpServletRequest request) {
        //1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null){
            return "redirect:/loginForm";
        }
        //2. 바디 데이터 확인 및 유효성 검사
        if (requestDTO.getTitle().length() > 30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // 잘못된 요청
        }
        //3. 모델 위임
        //insert into board_tb(title, content, user_id, created_at) values (?, ?, ?, now());
        boardRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/";
    }

}
