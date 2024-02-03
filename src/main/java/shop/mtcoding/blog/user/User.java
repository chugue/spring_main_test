package shop.mtcoding.blog.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/*
 * @Tabled은 현재 객체가 어떤 테이블이랑 연결될 것인지 구체적 정의,
 * user로 테이블 명을 만들면, 키워드이기 때문에 안만들어 질수 있다.
 * _tb 컨벤션을 지키도록 하자.
 */
@Table(name = "user_tb")
@Data //getter,setter,toString 생성
@Entity // 현재 클래스가 Entity임을 선언
public class User {

    @Id // 해당 필드를 PK로 만들어주는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment기능을 만들어주는 어노테이션
    private int id;

    private String username;
    private String password;
    private String email;
    //여기서 카멜표기법을 지키면 자동으로 JPA가 created_at과 매핑시킨다.
    private LocalDateTime createdAt;
}
