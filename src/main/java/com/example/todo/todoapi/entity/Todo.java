package com.example.todo.todoapi.entity;

import com.example.todo.userapi.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter @Getter
@ToString @EqualsAndHashCode(of = "todoId")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_todo")
public class Todo {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String todoId;

    @Column(nullable = false, length = 30)
    private String title;   //제목

    private boolean done;   //일정 완료 여부

    @CreationTimestamp
    private LocalDateTime createDate;   //등록 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")   //todo테이블에 user_id 컬럼 추가됨
    private User user;
}
