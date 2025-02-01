package com.iiitg.election.allocation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.student.Student;

@Component
public class Allocation {
    
    public List<Slot> generateSlots(LocalDateTime startTime, int requiredSlots, int slotDuration, int gapDuration) {
        List<Slot> slots = new ArrayList<>();
        LocalDateTime slotStart = startTime;
        
        for (int i = 0; i < requiredSlots; i++) {
            LocalDateTime slotEnd = slotStart.plusMinutes(slotDuration);
            slots.add(new Slot(slotStart, slotEnd));
            slotStart = slotEnd.plusMinutes(gapDuration);
        }
        
        return slots;
    }

    public List<SlotClassroom> assignStudentsToClassrooms(List<Student> students, List<Classroom> classrooms, List<Slot> slots) {
        students.sort(Comparator.comparing(Student::getRollNumber));
        classrooms.sort((c1, c2) -> Integer.compare(c2.getCapacity(), c1.getCapacity()));

        List<SlotClassroom> slotClassrooms = new ArrayList<>();
        int studentIndex = 0;

        for (Slot slot : slots) {
            for (Classroom classroom : classrooms) {
                int remainingStudents = students.size() - studentIndex;
                
                // Only create SlotClassroom if there are students to assign
                if (remainingStudents > 0) {
                    SlotClassroom slotClassroom = new SlotClassroom(classroom, slot);
                    slotClassrooms.add(slotClassroom);
                    
                    // Fill this classroom to its maximum capacity
                    int studentsToAssign = Math.min(classroom.getCapacity(), remainingStudents);
                    
                    // Assign students to this classroom
                    for (int i = 0; i < studentsToAssign; i++) {
                        students.get(studentIndex).setSlotClassroom(slotClassroom);
                        studentIndex++;
                    }
                }
                
                if (studentIndex >= students.size()) {
                    break;
                }
            }
            
            if (studentIndex >= students.size()) {
                break;
            }
        }

        return slotClassrooms;
    }

    public void assignFaculties(List<Classroom> activeClassrooms, List<Faculty> faculties) {
        if (activeClassrooms.isEmpty()) return;
        
        Collections.shuffle(faculties);
        int facultyIndex = 0;
        
        for (Classroom classroom : activeClassrooms) {
            if (facultyIndex < faculties.size()) {
                classroom.setAssignedFaculty(faculties.get(facultyIndex++));
            } else {
                // Handle case where there aren't enough faculties
                break;
            }
        }
    }
    
    public Map<Student, SlotClassroom> mapStudentsToSlotClassroom(List<Student> students, List<SlotClassroom> slotClassrooms) {
        Map<Student, SlotClassroom> mapping = new HashMap<>();
        
        // Sort slotClassrooms by classroom capacity (descending)
        slotClassrooms.sort((sc1, sc2) -> 
            Integer.compare(sc2.getClassroom().getCapacity(), sc1.getClassroom().getCapacity()));
        
        int studentIndex = 0;
        
        for (SlotClassroom slotClassroom : slotClassrooms) {
            int classroomCapacity = slotClassroom.getClassroom().getCapacity();
            int remainingStudents = students.size() - studentIndex;
            
            // Calculate how many students to assign to this classroom
            int studentsToAssign = Math.min(classroomCapacity, remainingStudents);
            
            // Assign students up to the classroom capacity or remaining students
            for (int i = 0; i < studentsToAssign; i++) {
                if (studentIndex < students.size()) {
                    Student student = students.get(studentIndex);
                    mapping.put(student, slotClassroom);
                    studentIndex++;
                }
            }
            
            // Break if all students have been assigned
            if (studentIndex >= students.size()) {
                break;
            }
        }
        
        return mapping;
    }
}
