package com.example.bolasdejairo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.db")
public class DatabaseProperties {

    private String name;
    private String username;
    private String password;
    private final Pool pool = new Pool();
    private final CloudSql cloudSql = new CloudSql();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Pool getPool() {
        return pool;
    }

    public CloudSql getCloudSql() {
        return cloudSql;
    }

    public static class Pool {

        private int initialSize = 0;
        private int maxSize = 20;
        private Duration maxIdleTime = Duration.ofMinutes(30);

        public int getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(int initialSize) {
            this.initialSize = initialSize;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public Duration getMaxIdleTime() {
            return maxIdleTime;
        }

        public void setMaxIdleTime(Duration maxIdleTime) {
            this.maxIdleTime = maxIdleTime;
        }
    }

    public static class CloudSql {

        private boolean enabled;
        private String instanceConnectionName;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getInstanceConnectionName() {
            return instanceConnectionName;
        }

        public void setInstanceConnectionName(String instanceConnectionName) {
            this.instanceConnectionName = instanceConnectionName;
        }
    }
}
