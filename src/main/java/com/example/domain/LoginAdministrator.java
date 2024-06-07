package com.example.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * ログイン処理用の管理者のドメイン.
 *
 * @author rui.inoue
 */
public class LoginAdministrator extends User {
    private final Administrator administrator;

    public LoginAdministrator(Administrator administrator, Collection<GrantedAuthority> authorities){
        super(administrator.getMailAddress(), administrator.getPassword(), authorities);
        this.administrator = administrator;
    }

    public Administrator getAdministrator() {
        return administrator;
    }
}
