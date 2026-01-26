package com.modulith.auctionsystem.tasks.internal.mapper;

import com.modulith.auctionsystem.tasks.domain.entity.Task;
import com.modulith.auctionsystem.tasks.domain.entity.TaskDependency;
import com.modulith.auctionsystem.tasks.domain.enums.TaskPriority;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import com.modulith.auctionsystem.tasks.shared.dto.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface TaskMapper {

    TaskResponse toTaskResponse(Task task);

    @Mapping(target = "status", expression = "java(mapStatus(createTaskRequest.status()))")
    @Mapping(target = "priority", expression = "java(mapPriority(createTaskRequest.priority()))")
    Task toTask(CreateTaskRequest createTaskRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(UpdateTaskRequest updateTaskRequest, @MappingTarget Task task);

    @Mapping(target = "predecessorTaskId", source = "predecessorTask.taskId")
    @Mapping(target = "predecessorTaskTitle", source = "predecessorTask.title")
    @Mapping(target = "successorTaskId", source = "successorTask.taskId")
    @Mapping(target = "successorTaskTitle", source = "successorTask.title")
    TaskDependencyResponse toTaskDependencyResponse(TaskDependency taskDependency);

    default TaskStatus mapStatus(TaskStatus status) {
        return status != null ? status : TaskStatus.TO_DO;
    }

    default TaskPriority mapPriority(TaskPriority priority) {
        return priority != null ? priority : TaskPriority.MEDIUM;
    }
}
