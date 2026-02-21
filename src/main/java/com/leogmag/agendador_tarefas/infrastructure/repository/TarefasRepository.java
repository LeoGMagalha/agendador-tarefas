package com.leogmag.agendador_tarefas.infrastructure.repository;

import com.leogmag.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.leogmag.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefasRepository extends MongoRepository<TarefasEntity, String> {
    List<TarefasEntity> findByDataEventoBetweenAndStatusNotificacao(LocalDateTime dataInicial, LocalDateTime dataFinal,
                                                                    StatusNotificacao statusNotificacao);
    List<TarefasEntity> findByEmailUsuario(String email);
}
