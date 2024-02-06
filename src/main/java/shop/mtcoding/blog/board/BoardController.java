package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
