package com.reminiscence.domain;

import com.reminiscence.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "Member")
@NoArgsConstructor
@Getter
@Table(name="MEMBER", uniqueConstraints = {@UniqueConstraint(name="Unique_nickname",columnNames = {"nickname"}),
        @UniqueConstraint(name="Unique_email",columnNames = {"email"})})
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="email", nullable = false, length = 40)
    private String email;
    @Column(name="password", nullable = false)
    private String password;
    @Column(name="nickname", nullable = false)
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private RoleType roleType;
    @Column(name="gender_fm")
    private char genderFm;
    @Column(name="age")
    private int age;
    @Column(name="name")
    private String name;

    @Column(name="profile")
    private String profile;

    @Column(name="sns_id")
    private String snsId;

    @Column(name="sns_type")
    private String snsType;

    @Column(name="personal_agreement_yn")
    private char personalAgreement;

    @Builder
    public Member(String email, String password, String nickname, RoleType roleType, Character genderFm, int age, String name, String profile, String snsId, String snsType, char personalAgreement) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roleType = roleType;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
        this.snsId = snsId;
        this.snsType = snsType;
        this.personalAgreement = personalAgreement;
    }


}
