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
        ProcessBuilder pb = new ProcessBuilder("python3", "./scripts/persistent_scraper.py");
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
            // Construct JSON safely using org.json
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            String json = jsonObject.toString();

            writer.write(json);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            System.out.println("Response from Python: " + response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error communicating with Python";
        }
    }
}
