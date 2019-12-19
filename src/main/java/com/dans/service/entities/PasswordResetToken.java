package com.dans.service.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "password_tokens", uniqueConstraints = {@UniqueConstraint(columnNames = {"token"})})
public class PasswordResetToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @NotNull
    private Date expiryDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public void setExpiryDate(int hours) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, hours);
        this.expiryDate = now.getTime();
    }
}
