package com.ahu.achievement.properties;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AchievementProperties {
    @Value("${com.ahu.achievement}")
    private String ftp_file_name;

    public String getFtp_file_name() {
        return ftp_file_name;
    }

    public void setFtp_file_name(String ftp_file_name) {
        this.ftp_file_name = ftp_file_name;
    }
}
