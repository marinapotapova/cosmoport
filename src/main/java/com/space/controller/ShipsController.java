package com.space.controller;


import com.space.model.Ship;
import com.space.model.ShipSpecifications;
import com.space.repository.ShipRepository;
import com.space.service.BadRequest;
import com.space.service.NotFound;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(path = "/rest/ships")
public class ShipsController {

    private ShipRepository shipRepository;

    @Autowired
    public void ShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Ship getShip(@PathVariable(name = "id") Long id) {
        if (id !=  null) {
            if (id != 0) {
                if(shipRepository.existsById(id)) {
                    return shipRepository.findById(id).get();
                } else  throw new NotFound();
            } else throw new BadRequest();
        }
       else return null;

    }


    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Ship> getAllShips(@RequestParam(defaultValue = "id",required = false) String order,
                                  @RequestParam(defaultValue = "0", value = "pageNumber") String pageNumber,
                                  @RequestParam(defaultValue = "3", value = "pageSize") String pageSize,
                                  @RequestParam(defaultValue = "",value = "name",required = false) String name ,
                                  @RequestParam(defaultValue = "", value = "planet", required = false) String planet ,
                                  @RequestParam(value = "after", required = false) Long after,
                                  @RequestParam(value = "before", required = false) Long before,
                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam(value = "maxCrewSize",required = false) Integer maxCrewSize ,
                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam( value = "minRating", required = false) Double minRating,
                                  @RequestParam( value = "maxRating", required = false) Double maxRating,
                                  @RequestParam(defaultValue = "Any", value = "shipType", required = false) String shipType ,
                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed){

       Specification <Ship> spec= specificationBuilder( name, planet, after,  before, minCrewSize,  maxCrewSize ,minSpeed,
                                                        maxSpeed, minRating,  maxRating, shipType ,  isUsed);
        if (order.equals("DATE")) {
                order = "prodDate";
            Pageable p = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize) ,
                    Sort.by(order));
            return shipRepository.findAll( spec,p).getContent();
        }
        else {
            Pageable p = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize) ,
                    Sort.by(order.toLowerCase()));
            return shipRepository.findAll(spec, p).getContent();

        }


    }

    public Specification<Ship> specificationBuilder(String name , String planet , Long after, Long before,
                                                    Integer minCrewSize, Integer maxCrewSize , Double minSpeed,
                                                    Double maxSpeed, Double minRating, Double maxRating,
                                                    String shipType , Boolean isUsed) {
        Specification <Ship> spec= ShipSpecifications.getShipByNameSpec(name).
                and(ShipSpecifications.getShipByPlanetSpec(planet));
        if (minCrewSize != null && maxCrewSize != null) {
            spec = spec.and(ShipSpecifications.getShipByCrewSpec(minCrewSize,maxCrewSize));
        }
        if (minCrewSize == null && maxCrewSize != null) {
            spec = spec.and(ShipSpecifications.getShipByCrewSpec(0,maxCrewSize));
        }
        if (minCrewSize != null && maxCrewSize == null) {
            spec = spec.and(ShipSpecifications.getShipByCrewSpec(minCrewSize,9999));
        }
        if (minSpeed != null && maxSpeed != null) {
            spec = spec.and(ShipSpecifications.getShipBySpeedSpec(minSpeed, maxSpeed));
        }
        if (minSpeed == null && maxSpeed != null) {
            spec = spec.and(ShipSpecifications.getShipBySpeedSpec(0.0, maxSpeed));
        }
        if (minSpeed != null && maxSpeed == null) {
            spec = spec.and(ShipSpecifications.getShipBySpeedSpec(minSpeed, 0.99));
        }
        if (minRating != null && maxRating != null) {
            spec = spec.and(ShipSpecifications.getShipByRatingSpec(minRating, maxRating));
        }
        if (minRating == null && maxRating != null) {
            spec = spec.and(ShipSpecifications.getShipByRatingSpec(0.0, maxRating));
        }
        if (minRating != null && maxRating == null) {
            spec = spec.and(ShipSpecifications.getShipByRatingSpec(minRating, 1000.0));
        }
        if (!shipType.equals("Any")) {
            spec = spec.and(ShipSpecifications.getShipByTypeSpec(shipType));
        }
        if (isUsed != null) {
            spec = spec.and(ShipSpecifications.getShipByUsedSpec(isUsed));
        }
        if (after != null && before != null) {
            spec = spec.and(ShipSpecifications.getShipByYearSpec(after, before));
        }
        return spec;
    }


    @RequestMapping(path = "/count", method = RequestMethod.GET)
    public long getQuantityOfShips(@RequestParam(defaultValue = "",value = "name",required = false) String name ,
                                   @RequestParam(defaultValue = "", value = "planet", required = false) String planet ,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                   @RequestParam(value = "maxCrewSize",required = false) Integer maxCrewSize ,
                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                   @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                   @RequestParam( value = "minRating", required = false) Double minRating,
                                   @RequestParam( value = "maxRating", required = false) Double maxRating,
                                   @RequestParam(defaultValue = "Any", value = "shipType", required = false) String shipType ,
                                   @RequestParam(value = "isUsed", required = false) Boolean isUsed) {
        return shipRepository.findAll(specificationBuilder(name, planet, after,  before, minCrewSize,  maxCrewSize ,minSpeed,
                                      maxSpeed, minRating,  maxRating, shipType ,  isUsed)).size();
    }


    @RequestMapping(path = "/{id}",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Ship updateShip(@RequestBody Ship shipToUpdate, @PathVariable(name = "id") Long id) throws Exception {

        Ship foundShip = getShip(id);

        if (shipToUpdate.getName() != null) {
            if (shipToUpdate.getName().length() <= 50 && shipToUpdate.getName().length() > 0) {
                foundShip.setName(shipToUpdate.getName());
            } else {
                throw new BadRequest();
            }
        }
        if (shipToUpdate.getPlanet() != null) {
            if (shipToUpdate.getPlanet().length() > 0 && shipToUpdate.getPlanet().length() <= 50) {
                    foundShip.setPlanet(shipToUpdate.getPlanet());
            } else {
                    throw new BadRequest();
            }
        }
        if (shipToUpdate.getProdDate() != null) {
            if (compareYears(shipToUpdate.getProdDate())) {
                foundShip.setProdDate(shipToUpdate.getProdDate());
            } else {
                throw new BadRequest();
            }
            }
        if (shipToUpdate.getSpeed() != null) {
            if (shipToUpdate.getSpeed() >= 0.01 && shipToUpdate.getSpeed() <= 0.99) {
                foundShip.setSpeed(shipToUpdate.getSpeed());
            } else {
                throw new BadRequest();
            }
            }
        if (shipToUpdate.getCrewSize() != null) {
            if (shipToUpdate.getCrewSize() >= 1 && shipToUpdate.getCrewSize() <= 9999) {
                foundShip.setCrewSize(shipToUpdate.getCrewSize());
            } else {
                throw new BadRequest();
            }
            }
        if (shipToUpdate.getUsed() != null) {
            foundShip.setUsed(shipToUpdate.getUsed());
            }
            if (shipToUpdate.getShipType() != null) {
                foundShip.setShipType(shipToUpdate.getShipType());
            }
        foundShip.setRating(shipToUpdate.getRating());

        return shipRepository.save(foundShip);




    }
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteShip(@PathVariable(name = "id") Long id) {
        Ship foundShip = getShip(id);
        shipRepository.delete(foundShip);

    }



    @RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Ship createShip(@RequestBody(required = false) Ship shipToSave ) {
      if (shipToSave.getName() == null){
          throw new BadRequest();
      }
      if (!(shipToSave.getName().length() <=50 && shipToSave.getName().length() >0)) {
          throw new BadRequest();
        }
      if (!(shipToSave.getPlanet().length() >0 && shipToSave.getPlanet().length() <=50 )) {
          throw new BadRequest();
        }
      if (!(compareYears(shipToSave.getProdDate()))) {
          throw new BadRequest();
        }
      if (!(shipToSave.getCrewSize() >= 1 && shipToSave.getCrewSize()  <= 9999) ){
            throw new BadRequest();
        }
      if ((shipToSave.getSpeed() == null) || !(shipToSave.getSpeed() >=0.01 && shipToSave.getSpeed()<= 0.99)) {
            throw new BadRequest();
        }
      if (shipToSave.getUsed() == null) {
             shipToSave.setUsed(false);
         }
      shipToSave.setRating(shipToSave.getRating());
        return shipRepository.save(shipToSave);


    }

  public Boolean compareYears (Date date){
      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      Date date1 = new Date();
      Date date2 = new Date();
      try {
          date1 = simpleDateFormat.parse("2800-01-01");
          date2 = simpleDateFormat.parse("3019-01-01");
      }
      catch (Exception e){
          e.getStackTrace();
      }
     return date.after(date1) && date.before(date2);
  }

}
