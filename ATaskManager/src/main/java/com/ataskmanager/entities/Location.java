package com.ataskmanager.entities;

import javax.persistence.*;

/** Represents a Location
 *        Annotations for Hibernate
 * @author Adam Combs
 * @author Seth Wampole
 */
@Entity
@Table(name="ams_locations")
public class Location {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          @Column(name="id")
          private Integer id;
          @Column(name="location_code")
          private String locationCode;
          private String name;
          private Float latitude;
          private Float longitude;
          private String street;
          private String city;
          private String state;
          private Integer zip;

          public Location(Integer id, String locationCode, String name, Float latitude, Float longitude, String street, String city, String state, Integer zip) {
                    this.id = id;
                    this.locationCode = locationCode;
                    this.name = name;
                    this.latitude = latitude;
                    this.longitude = longitude;
                    this.street = street;
                    this.city = city;
                    this.state = state;
                    this.zip = zip;
          }

          public Location(String locationCode, String name, Float latitude, Float longitude, String street, String city, String state, Integer zip) {
                    this.locationCode = locationCode;
                    this.name = name;
                    this.latitude = latitude;
                    this.longitude = longitude;
                    this.street = street;
                    this.city = city;
                    this.state = state;
                    this.zip = zip;
          }

          public Location() {
          }

          public Integer getId() {
                    return id;
          }

          public void setId(Integer id) {
                    this.id = id;
          }

          public String getLocationCode() {
                    return locationCode;
          }

          public void setLocationCode(String location_code) {
                    this.locationCode = location_code;
          }

          public String getName() {
                    return name;
          }

          public void setName(String name) {
                    this.name = name;
          }

          public Float getLatitude() {
                    return latitude;
          }

          public void setLatitude(Float latitude) {
                    this.latitude = latitude;
          }

          public Float getLongitude() {
                    return longitude;
          }

          public void setLongitude(Float longitude) {
                    this.longitude = longitude;
          }

          public String getStreet() {
                    return street;
          }

          public void setStreet(String street) {
                    this.street = street;
          }

          public String getCity() {
                    return city;
          }

          public void setCity(String city) {
                    this.city = city;
          }

          public String getState() {
                    return state;
          }

          public void setState(String state) {
                    this.state = state;
          }

          public Integer getZip() {
                    return zip;
          }

          public void setZip(Integer zip) {
                    this.zip = zip;
          }
}

