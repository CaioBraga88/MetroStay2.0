package com.trabalho.crud.mapper;

import com.trabalho.crud.core.dto.ReservaDto;
import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.mapper.ReservaMapper; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe ReservaMapper.
 * Garante que a conversão entre Entity e DTO funcione em ambas as direções e que 
 * todos os construtores e métodos das classes DTO e Entity sejam exercitados para 
 * fins de cobertura (JaCoCo).
 */
@ExtendWith(MockitoExtension.class) // Usando Mockito para inicialização mais rápida
public class ReservaMapperTest {

    // Instancia automaticamente o Mapper. Se o Mapper for uma interface (ex: MapStruct), 
    // isto garante que a implementação gerada seja usada.
    @InjectMocks 
    private ReservaMapper reservaMapper;

    private Reserva reserva;
    private ReservaDto reservaDto;
    private LocalDate dataInicio;
    private LocalDate dataFinal;

    @BeforeEach
    void setUp() {
        dataInicio = LocalDate.of(2025, 11, 10);
        dataFinal = LocalDate.of(2025, 11, 15);

        // Configuração da Entity (Reserva)
        reserva = new Reserva();
        reserva.setId(5L);
        reserva.setNumeroDoQuarto("305");
        reserva.setDataInicioReserva(dataInicio);
        reserva.setDataFinalReserva(dataFinal);
        reserva.setHospedeId(20L);

        // Configuração do DTO (ReservaDto)
        reservaDto = new ReservaDto();
        reservaDto.setId(5L);
        reservaDto.setNumeroDoQuarto("305");
        reservaDto.setDataInicioReserva(dataInicio);
        reservaDto.setDataFinalReserva(dataFinal);
        reservaDto.setHospedeId(20L);
    }

    // -------------------------------------------------------------------------
    // TESTES DE CONVERSÃO: ENTITY -> DTO
    // -------------------------------------------------------------------------
    
    @Test
    void toDto_DeveConverterEntityParaDtoCorretamente() {
        ReservaDto resultadoDto = reservaMapper.toDto(reserva);

        assertNotNull(resultadoDto);
        assertEquals(reserva.getId(), resultadoDto.getId());
        assertEquals(reserva.getNumeroDoQuarto(), resultadoDto.getNumeroDoQuarto());
        assertEquals(reserva.getDataInicioReserva(), resultadoDto.getDataInicioReserva());
        assertEquals(reserva.getDataFinalReserva(), resultadoDto.getDataFinalReserva());
        assertEquals(reserva.getHospedeId(), resultadoDto.getHospedeId());
    }

    @Test
    void toDto_DeveLidarComEntityNula() {
        assertNull(reservaMapper.toDto(null));
    }

    // -------------------------------------------------------------------------
    // TESTES DE CONVERSÃO: DTO -> ENTITY
    // -------------------------------------------------------------------------
    
    @Test
    void toEntity_DeveConverterDtoParaEntityCorretamente() {
        Reserva resultadoEntity = reservaMapper.toEntity(reservaDto);

        assertNotNull(resultadoEntity);
        assertEquals(reservaDto.getId(), resultadoEntity.getId());
        assertEquals(reservaDto.getNumeroDoQuarto(), resultadoEntity.getNumeroDoQuarto());
        assertEquals(reservaDto.getDataInicioReserva(), resultadoEntity.getDataInicioReserva());
        assertEquals(reservaDto.getDataFinalReserva(), resultadoEntity.getDataFinalReserva());
        assertEquals(reservaDto.getHospedeId(), resultadoEntity.getHospedeId());
    }

    @Test
    void toEntity_DeveLidarComDtoNulo() {
        assertNull(reservaMapper.toEntity(null));
    }

    // -------------------------------------------------------------------------
    // TESTES DE CONVERSÃO: LISTAS
    // -------------------------------------------------------------------------
    
    @Test
    void toDtoList_DeveConverterListaDeEntitiesParaListaDeDtos() {
        List<Reserva> entities = List.of(reserva, new Reserva());
        
        List<ReservaDto> dtos = reservaMapper.toDtoList(entities);
        
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(entities.get(0).getNumeroDoQuarto(), dtos.get(0).getNumeroDoQuarto());
    }

    @Test
    void toDtoList_DeveLidarComListaNula() {
        assertNull(reservaMapper.toDtoList(null));
    }
    
    // -------------------------------------------------------------------------
    // TESTE DE COBERTURA EXTRA (Para garantir 100% dos getters/setters/etc.)
    // -------------------------------------------------------------------------
    
    @Test
    void gettersAndSetters_EntityENullConstructor_CoberturaCompleta() {
        // Testa construtor vazio e setters para cobertura da Entity
        Reserva r = new Reserva();
        r.setId(10L);
        r.setNumeroDoQuarto("100");
        r.setDataInicioReserva(dataInicio);
        r.setDataFinalReserva(dataFinal);
        r.setHospedeId(5L);
        
        assertEquals(10L, r.getId());
        assertEquals("100", r.getNumeroDoQuarto());
        assertEquals(dataInicio, r.getDataInicioReserva());
        assertEquals(dataFinal, r.getDataFinalReserva());
        assertEquals(5L, r.getHospedeId());

        // Testa construtor vazio e setters para cobertura do DTO
        ReservaDto d = new ReservaDto();
        d.setId(10L);
        d.setNumeroDoQuarto("100");
        d.setDataInicioReserva(dataInicio);
        d.setDataFinalReserva(dataFinal);
        d.setHospedeId(5L);
        
        assertEquals(10L, d.getId());
        assertEquals("100", d.getNumeroDoQuarto());
        assertEquals(dataInicio, d.getDataInicioReserva());
        assertEquals(dataFinal, d.getDataFinalReserva());
        assertEquals(5L, d.getHospedeId());
    }
}
