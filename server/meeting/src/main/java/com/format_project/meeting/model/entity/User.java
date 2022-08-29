package com.format_project.meeting.model.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {
    private String userId;
    private String username;
    private String truename;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String department;
    private String position;
    private Collection<? extends GrantedAuthority> authorities;


    public User(){}
    public User(String userId,String username,String truename,String email,
    String phone,String password,String avatar,String department,String position,
    Collection<? extends GrantedAuthority> authorities){
        this.userId = userId;
        this.username = username;
        this.truename = truename;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.avatar = avatar;
        this.department = department;
        this.position = position;
        this.authorities = authorities;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    @Override 
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return phone;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String getPassword(){
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public void setAuthorities(ArrayList<SimpleGrantedAuthority> authorityList){
        this.authorities = authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }
    
    @Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
    }



    
}
