package com.iiitg.election.allocation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.Slot;

public class ClassroomDtoMapper {
	public static ClassroomDto toDto(Classroom classroom) {
        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setClassroomName(classroom.getClassroomName());
        dto.setCapacity(classroom.getCapacity());
        dto.setIsAvailable(classroom.isAvailable());

        if (classroom.getClassroomSlots() != null) {
            List<ClassroomDto.SlotInfo> slotInfos = classroom.getClassroomSlots().stream()
                .map(slotClassroom -> {
                    ClassroomDto.SlotInfo slotInfo = new ClassroomDto.SlotInfo();
                    // Map the slot data
                    Slot slot = slotClassroom.getSlot();  // Assuming SlotClassroom has getSlot()
                    if (slot != null) {
                        slotInfo.setId(slot.getId());
                        slotInfo.setSlotStartTime(slot.getSlotStartTime());
                        slotInfo.setSlotEndTime(slot.getSlotEndTime());
                    }
                    return slotInfo;
                }).collect(Collectors.toList());
            dto.setSlots(slotInfos);
        }

        return dto;
    }
}
