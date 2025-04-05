package com.sparsh.insta_downloader_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.InitializingBean;

import java.io.*;

@Service
public class PythonBridgeService implements InitializingBean {

    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;

    @Override
    public void afterPropertiesSet() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python3", "scripts/persistent_scraper.py");
        pb.redirectErrorStream(true);
        process = pb.start();
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // wait for "ready"
        String ready = reader.readLine();
        System.out.println("Python started: " + ready);
    }

    public String sendUrlAndGetVideo(String url) {
        try {
            String json = "{\"url\": \"" + url + "\"}";
            writer.write(json);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error communicating with Python";
        }
    }
}
