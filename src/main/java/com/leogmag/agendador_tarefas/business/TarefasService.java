package com.leogmag.agendador_tarefas.business;

import com.leogmag.agendador_tarefas.business.dto.TarefasDTO;
import com.leogmag.agendador_tarefas.business.mapper.TarefasConverter;
import com.leogmag.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.leogmag.agendador_tarefas.infrastructure.enums.StatusNotificacao;
import com.leogmag.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.leogmag.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extractEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacao(StatusNotificacao.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefasConverter.paraTarefasEntity(dto);
        return tarefasConverter.paraTarefasDTO(
                tarefasRepository.save(entity)
        );
    }
}
