package com.trip.adviser.data.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "geonames")
public class Geoname {

    @Id
    @Column(name = "geonameid")
    private Integer geonameId;

    @Column(name = "name", length = 10000)
    private String name;

    @Column(name = "asciiname", length = 10000)
    private String asciiName;

    @Column(name = "alternatenames", length = 10000)
    private String alternateNames;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "feature_class", length = 1)
    private String featureClass;

    @Column(name = "feature_code", length = 10)
    private String featureCode;

    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "cc2")
    private String cc2;

    @Column(name = "admin1_code", length = 20)
    private String admin1Code;

    @Column(name = "admin2_code", length = 80)
    private String admin2Code;

    @Column(name = "admin3_code", length = 20)
    private String admin3Code;

    @Column(name = "admin4_code", length = 20)
    private String admin4Code;

    @Column(name = "population")
    private Long population;

    @Column(name = "elevation")
    private Integer elevation;

    @Column(name = "dem")
    private Integer dem;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "modification_date")
    private LocalDate modificationDate;

    // Getters and Setters
    public Integer getGeonameId() { return geonameId; }
    public void setGeonameId(Integer geonameId) { this.geonameId = geonameId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAsciiName() { return asciiName; }
    public void setAsciiName(String asciiName) { this.asciiName = asciiName; }
    public String getAlternateNames() { return alternateNames; }
    public void setAlternateNames(String alternateNames) { this.alternateNames = alternateNames; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getFeatureClass() { return featureClass; }
    public void setFeatureClass(String featureClass) { this.featureClass = featureClass; }
    public String getFeatureCode() { return featureCode; }
    public void setFeatureCode(String featureCode) { this.featureCode = featureCode; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getCc2() { return cc2; }
    public void setCc2(String cc2) { this.cc2 = cc2; }
    public String getAdmin1Code() { return admin1Code; }
    public void setAdmin1Code(String admin1Code) { this.admin1Code = admin1Code; }
    public String getAdmin2Code() { return admin2Code; }
    public void setAdmin2Code(String admin2Code) { this.admin2Code = admin2Code; }
    public String getAdmin3Code() { return admin3Code; }
    public void setAdmin3Code(String admin3Code) { this.admin3Code = admin3Code; }
    public String getAdmin4Code() { return admin4Code; }
    public void setAdmin4Code(String admin4Code) { this.admin4Code = admin4Code; }
    public Long getPopulation() { return population; }
    public void setPopulation(Long population) { this.population = population; }
    public Integer getElevation() { return elevation; }
    public void setElevation(Integer elevation) { this.elevation = elevation; }
    public Integer getDem() { return dem; }
    public void setDem(Integer dem) { this.dem = dem; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public LocalDate getModificationDate() { return modificationDate; }
    public void setModificationDate(LocalDate modificationDate) { this.modificationDate = modificationDate; }
}
