package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ConfigurationService {
    private final File SETTINGS_FILE = new File("settings.json");
    private Gson gson = new GsonBuilder().create();

    public ConfigModel getConfiguration() throws IOException {
        if(!SETTINGS_FILE.exists()) {
            createSettingsFile();
        }

        return readDataFromConfig();
    }

    private void createSettingsFile() throws IOException {
        ConfigModel configModel = new ConfigModel();
        try(Writer writer = new FileWriter(SETTINGS_FILE, false)) {
            gson.toJson(configModel, writer);
        }
    }

    private ConfigModel readDataFromConfig() throws IOException {
        ConfigModel configModel = new ConfigModel();
        try(Reader reader = new FileReader(SETTINGS_FILE)) {
            return gson.fromJson(reader, ConfigModel.class);
        }
    }
}
