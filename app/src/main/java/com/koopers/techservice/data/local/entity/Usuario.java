package com.koopers.techservice.data.local.entity;

//No ROOM
public class Usuario {

    private int id;
    private String usuario;
    private String mail;
    private String password;
    private String image;
    private String role;


    public Usuario(int id_usuario, String usuario, String mail, String password, String image, String role) {
        this.id = id_usuario;
        this.usuario = usuario;
        this.mail = mail;
        this.password = password;
        this.image = image;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id_usuario) {
        this.id = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
