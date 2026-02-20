package com.modulith.auctionsystem.tasks.internal;

import com.modulith.auctionsystem.tasks.domain.entity.Kanban;
import com.modulith.auctionsystem.tasks.shared.dto.CreateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.dto.KanbanResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateKanbanRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface KanbanMapper {

    KanbanResponse toKanbanResponse(Kanban kanban);

    Kanban toKanban(CreateKanbanRequest createKanbanRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateKanbanFromDto(UpdateKanbanRequest updateKanbanRequest, @MappingTarget Kanban kanban);
}
