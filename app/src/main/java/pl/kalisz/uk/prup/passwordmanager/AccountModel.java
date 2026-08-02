package pl.kalisz.uk.prup.passwordmanager;

import pl.kalisz.uk.prup.passwordmanager.security.CryptoUtils;

public class AccountModel {
    private int id;
    private String platform, login, password;

    public AccountModel(int id, String platform, String login, String password) {
        this.id = id;
        this.platform = platform;
        this.login = login;
        setPassword(password);
    }

    public AccountModel(String platform, String login, String password) {
        this.platform = platform;
        this.login = login;
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
