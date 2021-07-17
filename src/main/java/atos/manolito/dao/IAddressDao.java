package atos.manolito.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import atos.manolito.entity.Address;

//t2007-DCS-21/11/2019
public interface IAddressDao extends JpaRepository<Address, Integer>{

}
