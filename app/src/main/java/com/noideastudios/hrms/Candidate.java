package com.noideastudios.hrms;

public class Candidate {

    private int id;
    private String name;
    private long phone;
    private String position;
    private String status;

    Candidate() {
    }

    Candidate(String name, long phone, String position, String status) {
        this.name = name;
        this.phone = phone;
        this.position = position;
        this.status = status;
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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
