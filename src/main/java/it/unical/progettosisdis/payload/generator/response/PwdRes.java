package it.unical.progettosisdis.payload.generator.response;

public class PwdRes {

    private String password;

    public PwdRes(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
