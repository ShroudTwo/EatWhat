package com.wei.cookbook.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Profile {
    @Id(autoincrement = true)
    private Long id;     //电话号码

    String username;    //用户名

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCharacter() {
        return Character;
    }

    public void setCharacter(String character) {
        Character = character;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    String gender;      //性别
    String age;         //年龄
    String Character;   //个性签名

    @Generated(hash = 843877233)
    public Profile(Long id, String username, String gender, String age,
            String Character) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.Character = Character;
    }

    @Generated(hash = 782787822)
    public Profile() {
    }
}
