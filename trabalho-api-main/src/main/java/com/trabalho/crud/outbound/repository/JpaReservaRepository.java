package com.trabalho.crud.outbound.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.repository.ReservaRepository;

@Profile("!test")
public interface JpaReservaRepository extends ReservaRepository, JpaRepository <Reserva, Long> {

}
