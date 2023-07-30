package com.reminiscence.domain;

import com.reminiscence.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;
import java.util.Optional;

@Entity(name = "Member")
@NoArgsConstructor
@Getter
@Table(name="MEMBER", uniqueConstraints = {@UniqueConstraint(name="Unique_nickname",columnNames = {"nickname"}),
        @UniqueConstraint(name="Unique_email",columnNames = {"email"})})
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="email", nullable = false)
    private String email;
    @Column(name="password", nullable = false)
    private String password;
    @Column(name="nickname", nullable = false)
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;
    @Column(name="gender_fm")
    private char genderFm;
    @Column(name="age")
    private int age;
    @Column(name="name")
    private String name;

    @Column(name="profile")
    private String profile;

    @Column(name="del_yn")
    private char delYn;

    @Column(name="sns_id")
    private String snsId;

    @Column(name="sns_type")
    private String snsType;

    @Column(name="personal_agreement_yn")
    private char personalAgreement;

    @Builder
    public Member(String email, String password, String nickname, Role role, char genderFm, int age, String name, String profile, char delYn ,String snsId, String snsType, char personalAgreement) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
        this.delYn = delYn;
        this.snsId = snsId;
        this.snsType = snsType;
        this.personalAgreement = personalAgreement;
    }


    public void modifyPassword(String password) {
        this.password=password;
    }
    public void modifyNickname(String nickname) {
        this.nickname=nickname;
    }
    public void modifyGenderFm(char genderFm) {
        this.genderFm=genderFm;
    }
    public void modifyAge(int age) {
        this.age=age;
    }
    public void modifyName(String name) {
        this.name=name;
    }
    public void modifyProfile(String profile) {
        this.profile=profile;
    }
    public void modifyDelYn(char delYn) {
        this.delYn=delYn;
    }
    public void modifySnsId(String snsId) {
        this.snsId=snsId;
    }
    public void modifySnsType(String snsType) {
        this.snsType=snsType;
    }
    public void modifyPersonalAgreement(char personalAgreement) {
        this.personalAgreement=personalAgreement;
    }
}
