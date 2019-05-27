package com.space.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ship.class)
public class Ship_ {
    public static volatile SingularAttribute<Ship, String> name;
    public static volatile SingularAttribute<Ship, String> planet;
    public static volatile SingularAttribute<Ship, ShipType> shipType;
    public static volatile SingularAttribute<Ship, Long> id;
    public static volatile SingularAttribute<Ship, Date> prodDate;
    public static volatile SingularAttribute<Ship, Boolean> isUsed;
    public static volatile SingularAttribute<Ship, Double> speed;
    public static volatile SingularAttribute<Ship, Integer> crewSize;
    public static volatile SingularAttribute<Ship, Double> rating;


}
