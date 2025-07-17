package com.career.testtaskmegacom.model.additional;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUser {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;
}