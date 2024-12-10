package project.org.fitnessprogresstracker.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.org.fitnessprogresstracker.dto.ProgressDto;
import project.org.fitnessprogresstracker.entities.Progress;
import project.org.fitnessprogresstracker.service.ProgressService;

@CrossOrigin(origins = "*")
@RestController("/progress")
@AllArgsConstructor
public class ProgressController {
    private final ProgressService progressService;


    @PostMapping("/add")
    public ResponseEntity<?> addProgress(@RequestBody ProgressDto progressDto){
        return progressService.addProgress(progressDto);
    }

    @GetMapping()
    public ResponseEntity<?> getProgresses(){
        return progressService.getProgresses();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgress(@PathVariable Long id){
        return progressService.deleteProgress(id);
    }


}
