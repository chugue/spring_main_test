package shop.mtcoding.blog.reply;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

// 댓글 쓰기, 댓글 삭제, 댓글 목록보기
@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final HttpSession session;
    private final ReplyRepository replyRepository;

    @PostMapping("reply/save")
    public String write(ReplyRequest.WriteDTO requestDTO) {
        System.out.println(requestDTO);

        // 현재 사용자가 세션유저인지 확인 - 로그인된 사람인지 확인
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 유효성 검사 -- 이따가 필요하다면 더 구현하기!

        // 핵심 코드 - 아직 ReplyRepository를 만들지 않았으므로 에러날 것인데 괜찮다.
        replyRepository.save(requestDTO, sessionUser.getId());

        // 핵심 로직 실행후 그 페이지에 그대로 남아있게 하기 위한 코드
        return "redirect:/board/" + requestDTO.getBoardId();
    }
}
