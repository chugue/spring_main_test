package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserRepository userRepository;
    private final HttpSession session;


    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        // 1. 유효성 검사 - 사용자id가 3자 이하인경우 400번 에러메시지 전달
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        User user = userRepository.findByUsernameAndPassword(requestDTO);
        if (user == null) { // 조회 안됨 (401)
            return "error/401";
        } else { // 조회 됐음 (인증됨)
            session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)
        }

        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {

        // 1. 유효성 검사 - 사용자id가 3자 이하인경우 400번 에러메시지 전달
//        if (requestDTO.getUsername().length() < 3) {
//            return "error/400";
//        }
//        // 2. Model에게 위임하기 - DB와 상호작용할 객체와 메소드 구현 필요!!
//        userRepository.save(requestDTO);
        System.out.println(requestDTO);
        return "redirect:/loginForm"; // 요청이 완료되면 loginForm으로 전달
    }


    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
