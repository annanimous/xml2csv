package ru.energodata.Model;

/**
 * Created by Aristarkhov-VM on 21.09.2016.
 * Анализатор качества распознавания Excel-файлов
 */
public class Stroka {
    private String id;
    private String original_address;
    private String index;
    private String region = "";
    private String raion = "";
    private String city = "";
    private String subcity = "";
    private String street = "";
    private String building = "";
    private String drob = "";
    private String korpus = "";
    private String stroenie = "";
    private String kvartira = "";
    private String analiz_index = "";
    private String indexChdpa = "";
    private String analiz_street = "";
    private String analiz_building = "";
    private String analiz_drob = "";
    private String analiz_korpus = "";
    private String analiz_stroenie = "";
    private String analiz_kvartira = "";
    private String finalAnaliz = "";
    public Stroka(String id, String original_address, String index, String region, String raion, String city, String subcity, String street, String building, String drob, String korpus, String stroenie, String kvartira) {
        this.id = id;
        this.original_address = original_address;
        this.index = index;
        this.region = region;
        this.raion = raion;
        this.city = city;
        this.subcity = subcity;
        this.street = street;
        this.building = building;
        this.drob = drob;
        this.korpus = korpus;
        this.stroenie = stroenie;
        this.kvartira = kvartira;
        this.analiz_index = "";
        this.analiz_street = "";
        this.analiz_building = "";
        this.analiz_kvartira = "";
        this.finalAnaliz = "";

    }

    public Stroka(String id, String analiz_index, String indexChdpa, String analiz_street, String analiz_building, String analiz_drob, String analiz_korpus, String analiz_stroenie, String analiz_kvartira, String finalAnaliz) {
        this.id = id;
        this.analiz_index = analiz_index;
        this.indexChdpa = indexChdpa;
        this.analiz_street = analiz_street;
        this.analiz_building = analiz_building;
        this.analiz_drob = analiz_drob;
        this.analiz_korpus = analiz_korpus;
        this.analiz_stroenie = analiz_stroenie;
        this.analiz_kvartira = analiz_kvartira;
        this.finalAnaliz = finalAnaliz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_address() {
        return original_address;
    }

    public void setOriginal_address(String original_address) {
        this.original_address = original_address;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRaion() {
        return raion;
    }

    public void setRaion(String raion) {
        this.raion = raion;
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

    public String getBuilding() {        return building;    }

    public void setBuilding(String building) {        this.building = building;    }

    public String getDrob() {        return drob;    }

    public void setDrob(String drob) {        this.drob = drob;    }

    public String getKorpus() {        return korpus;    }

    public void setKorpus(String korpus) {        this.korpus = korpus;    }

    public String getStroenie() {        return stroenie;    }

    public void setStroenie(String stroenie) {        this.stroenie = stroenie;    }

    public String getKvartira() {        return kvartira;    }

    public void setKvartira(String kvartira) {        this.kvartira = kvartira;    }

    public String getAnaliz_index() {
        return analiz_index;
    }

    public void setAnaliz_index(String analiz_index) {
        this.analiz_index = analiz_index;
    }

    public String getIndexChdpa() { return indexChdpa; }

    public void setIndexChdpa(String indexChdpa) { this.indexChdpa = indexChdpa; }

    public String getAnaliz_street() {        return analiz_street;    }

    public void setAnaliz_street(String analiz_street) {        this.analiz_street = analiz_street;    }

    public String getAnaliz_building() {        return analiz_building;    }

    public void setAnaliz_building(String analiz_building) {        this.analiz_building = analiz_building;    }

    public String getAnaliz_drob() {        return analiz_drob;    }

    public void setAnaliz_drob(String analiz_drob) {        this.analiz_drob = analiz_drob;    }

    public String getAnaliz_korpus() {        return analiz_korpus;    }

    public void setAnaliz_korpus(String analiz_korpus) {        this.analiz_korpus = analiz_korpus;    }

    public String getAnaliz_stroenie() {        return analiz_stroenie;    }

    public void setAnaliz_stroenie(String analiz_stroenie) {        this.analiz_stroenie = analiz_stroenie;    }

    public String getAnaliz_kvartira() {
        return analiz_kvartira;
    }

    public void setAnaliz_kvartira(String analiz_kvartira) {
        this.analiz_kvartira = analiz_kvartira;
    }

    public void setFinalAnaliz(String finalAnaliz) {
        this.finalAnaliz = finalAnaliz;
    }

    public String getFinalAnaliz() {
        return finalAnaliz;
    }
}
