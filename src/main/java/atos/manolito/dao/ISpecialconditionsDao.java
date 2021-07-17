package atos.manolito.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import atos.manolito.entity.Specialconditions;

@Repository
public interface ISpecialconditionsDao extends JpaRepository<Specialconditions,Integer> {

}
