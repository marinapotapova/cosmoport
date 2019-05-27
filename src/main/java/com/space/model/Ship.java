package com.space.model;

import com.space.model.ShipType;


import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;// ID корабля
     private String name; //Название корабля (до 50 знаков включительно)
     private String planet;//Планета пребывания (до 50 знаков включительно)
     @Enumerated(EnumType.STRING)
     private ShipType shipType; //Тип корабля
     private Date prodDate; //Дата выпуска.Диапазон значений года 2800..3019 включительно
     private Boolean isUsed; //Использованный / новый
     private Double speed; //Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно. Используй математическое округление до сотых.
     private Integer crewSize; //Количество членов экипажа. Диапазон значений 1..9999 включительно.
     private Double rating; // Рейтинг корабля. Используй математическое округление до сотых.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {

        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = calculateRating();
    }

    public Double calculateRating() {
        double k;
        if (isUsed){
            k = 0.5;
        }
        else {
            k = 1;
        }
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(prodDate);
        int year = cal.get(Calendar.YEAR);

       return  (double) Math.round(80 * speed * k / (3019 - year + 1) *100)/100;
    }
}
