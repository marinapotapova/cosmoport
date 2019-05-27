package com.space.model;


import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.util.Date;

public class ShipSpecifications {

    private  static Predicate equalPredicate;

   public static Specification<Ship> getShipByNameSpec(String name){

        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                equalPredicate = criteriaBuilder.like(root.get(Ship_.name), '%'+name+'%');
                return equalPredicate;
            }
        };
    }

    public static Specification<Ship> getShipByPlanetSpec(String planet){
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                 equalPredicate = criteriaBuilder.like(root.get(Ship_.planet), '%'+planet+'%');
                return equalPredicate;
            }
        };
    }
    public static Specification<Ship> getShipByCrewSpec(Integer minCrewSize, Integer maxCrewSize){
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {


               equalPredicate = criteriaBuilder.between(root.get(Ship_.crewSize), minCrewSize, maxCrewSize);
                return equalPredicate;
            }
        };
    }
    public static Specification<Ship> getShipBySpeedSpec(Double minSpeed, Double maxSpeed){
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {


               equalPredicate = criteriaBuilder.between(root.get(Ship_.speed), minSpeed, maxSpeed);
                return equalPredicate;
            }
        };
    }
    public static Specification<Ship> getShipByRatingSpec(Double minRating, Double maxRating){
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {


               equalPredicate = criteriaBuilder.between(root.get(Ship_.rating), minRating, maxRating);
                return equalPredicate;
            }
        };
    }
    public static Specification<Ship> getShipByTypeSpec(String shipType){
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {

              String type = shipType.toString().toUpperCase();
              ShipType st = ShipType.valueOf(type);

               equalPredicate = criteriaBuilder.equal(root.get(Ship_.shipType), st);
                return equalPredicate;
            }
        };
    }
    public static Specification<Ship> getShipByUsedSpec(Boolean isUsed){
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {


                 equalPredicate = criteriaBuilder.equal(root.get(Ship_.isUsed), isUsed);
                return equalPredicate;
            }
        };
    }
    public static Specification<Ship> getShipByYearSpec(Long after, Long before)  {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {

                equalPredicate = criteriaBuilder.between(root.get(Ship_.prodDate), new Date(after), new Date(before));
                return equalPredicate;
            }
        };
    }
}
