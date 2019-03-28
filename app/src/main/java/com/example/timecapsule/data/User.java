package com.example.timecapsule.data;

public class User implements Comparable<User> {
    private int id;
    private String name;
    private String password;
    private byte[] salt;

    @Override
    public int compareTo(User o) {
        return this.getId() - o.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}