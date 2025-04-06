package com.insta_downloader_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("/reel")
public class InstaController {

    @PostMapping("/download")
    public ResponseEntity<String> downloadVideo(@RequestBody String urlJson) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "scripts/scrape_instagram.py", urlJson);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return ResponseEntity.ok(output.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
