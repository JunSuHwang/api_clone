package com.rest.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder // builder를 사용할 수 있게 합니다
@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name="user_table") // 'user_table' 테이블과 매핑됨을 명시합니다.
public class User {
    @Id // primaryKey임을 알립니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // pk생성전략을 DB에 위임한다는 의미입니다.
    // mysql로 보면 pk 필드를 auto_increament로 설정해 놓은 경우로 보면 됩니다.
    private long msrl;

    // uid Column을 명시합니다. 필수이고 유니크한 필드이며 길이는 30입니다.
    @Column(nullable = false, unique = true, length = 30)
    private String uid;

    @Column(nullable = false, length = 100)
    private String name;
}
