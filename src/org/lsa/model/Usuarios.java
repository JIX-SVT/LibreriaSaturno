package org.lsa.model;


public class Usuarios  {
    private String username;
    private String passwordHash;
    private String confirmarPassword;
    
    public Usuarios(){
        
    }
    public Usuarios(String username,String passwordHash, String confirmarPassword){
        this.username = username;
        this.passwordHash = passwordHash;
        this.confirmarPassword = confirmarPassword;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
}
