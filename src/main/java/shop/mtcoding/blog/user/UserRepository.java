package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;


@Controller //스프링컨테이너에 현재 객체를 Bean으로 등록
public class UserRepository {
    private EntityManager em;

    // 생성자에 EnityManager의존성 주입 - 이 코드가 있어야 가져다 쓸수 있다.
    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? and password=?", User.class);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        User user = (User) query.getSingleResult();
        return user;
    }

    @Transactional // DB를 변경하는 작업을 할 때 동기화 시켜주는 어노테이션, 꼭 필요!!!
    public void save(UserRequest.JoinDTO requestDTO) {
        Query query = em.createNativeQuery("insert into user_tb(username, password, email, created_at) values (?,?,?, now())");
        System.out.println(requestDTO);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getEmail());
        query.executeUpdate(); // 쿼리 발송 flush()기능
    }


}
