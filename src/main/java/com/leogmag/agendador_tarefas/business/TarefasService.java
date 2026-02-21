package com.leogmag.agendador_tarefas.business;

import com.leogmag.agendador_tarefas.business.dto.TarefasDTO;
import com.leogmag.agendador_tarefas.business.mapper.TarefasConverter;
import com.leogmag.agendador_tarefas.business.mapper.TarefasUpdateConverter;
import com.leogmag.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.leogmag.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.leogmag.agendador_tarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.leogmag.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.leogmag.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.osgi.resource.Resource;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final JwtUtil jwtUtil;
    private final TarefasUpdateConverter tarefasUpdateConverter;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extractEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacao(StatusNotificacao.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefasConverter.paraTarefasEntity(dto);
        return tarefasConverter.paraTarefasDTO(
                tarefasRepository.save(entity)
        );
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefasConverter.paraListaTarefasDTO(tarefasRepository.findByDataEventoBetweenAndStatusNotificacao(dataInicial, dataFinal, StatusNotificacao.PENDENTE));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorEmail(String token) {
        String email = jwtUtil.extractEmailToken(token.substring(7));
        return tarefasConverter.paraListaTarefasDTO(tarefasRepository.findByEmailUsuario(email));
    }

    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente " + id, e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacao status, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(
                    () -> new ResolutionException("Tarefa não encontrada" + id)
            );
            entity.setStatusNotificacao(status);
            return tarefasConverter.paraTarefasDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar o status da tarefa" + e.getCause());
        }

    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(
                    () -> new ResolutionException("Tarefa não encontrada" + id));
            tarefasUpdateConverter.updateTarefas(dto, entity);
            return tarefasConverter.paraTarefasDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao atualizar a tarefa" + e.getCause());
        }
    }

}
