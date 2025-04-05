package com.sparsh.insta_downloader_backend.controller;

import com.sparsh.insta_downloader_backend.model.UrlRequest;
import com.sparsh.insta_downloader_backend.service.PythonBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reel")
public class InstaController {

    @Autowired
    private PythonBridgeService pythonBridgeService;

    @PostMapping("/download")
    public ResponseEntity<String> downloadVideo(@RequestBody UrlRequest request) {
        String url = request.getUrl();
        if (url == null || !url.startsWith("https://")) {
            return ResponseEntity.badRequest().body("Invalid URL format.");
        }

        String result = pythonBridgeService.sendUrlAndGetVideo(url);
        return ResponseEntity.ok(result);
    }
}
