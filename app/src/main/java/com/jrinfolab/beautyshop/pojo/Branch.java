package com.jrinfolab.beautyshop.pojo;

public class Branch {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String[] getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String[] photoList) {
        this.photoList = photoList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name : " + name);
        sb.append("\nAddress : " + address);
        for(String data : photoList) {
            sb.append("\nImage : " + data);
        }
        return sb.toString();
    }

    String id;
    String name;
    String address;
    double lat;
    double lng;
    String[] photoList;
}
