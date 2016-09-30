package ru.energodata.Model;

/**
 * Created by Aristarkhov-VM on 21.09.2016.
 */
public class Otpravlenie {
    private String barcode;
    private String reliability;
    private String zip;
    private String region;
    private String region_socr;
    private String area;
    private String area_socr;
    private String city;
    private String city_socr;
    private String subcity;
    private String subcity_socr;
    private String street;
    private String street_socr;
    private String building;
    private String building_korp;
    private String apartment;
    /*private String name;
    private String phone;*/

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getReliability() {
        return reliability;
    }

    public void setReliability(String reliability) {
        this.reliability = reliability;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubcity() {
        return subcity;
    }

    public void setSubcity(String subcity) {
        this.subcity = subcity;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
    public String getBuilding_korp() {
        return building_korp;
    }

    public void setBuilding_korp(String building_korp) {
        this.building_korp = building_korp;
    }
    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    /*public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }*/

    public String getRegion_socr() {
        return region_socr;
    }

    public void setRegion_socr(String region_socr) {
        this.region_socr = region_socr;
    }

    public String getArea_socr() {
        return area_socr;
    }

    public void setArea_socr(String area_socr) {
        this.area_socr = area_socr;
    }

    public String getCity_socr() {
        return city_socr;
    }

    public void setCity_socr(String city_socr) {
        this.city_socr = city_socr;
    }

    public String getSubcity_socr() {
        return subcity_socr;
    }

    public void setSubcity_socr(String subcity_socr) {
        this.subcity_socr = subcity_socr;
    }

    public String getStreet_socr() {
        return street_socr;
    }

    public void setStreet_socr(String street_socr) {
        this.street_socr = street_socr;
    }
}
