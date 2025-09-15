package com.assignment.jackpot.controller;

import com.assignment.jackpot.model.Jackpot;
import com.assignment.jackpot.service.JackpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jackpots")
@RequiredArgsConstructor
public class JackpotController {

    private final JackpotService jackpotService;

    @PostMapping()
    public ResponseEntity<String> addJackpots(@RequestBody List<Jackpot> jackpots) {
        jackpotService.saveJackpots(jackpots);
        return ResponseEntity.ok("New jackpots are saved");
    }

    @PostMapping("add")
    public ResponseEntity<String> addJackpot(@RequestBody Jackpot jackpot) {
        jackpotService.saveJackpot(jackpot);
        return ResponseEntity.accepted().body("jackpot is added");
    }

    @GetMapping
    public List<Jackpot> getAllJackpots(){
        return jackpotService.getAllJackpots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jackpot> getJackpot(@PathVariable Long id){
        return jackpotService.getJackpot(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
