package me.frostythedev.bungeeutils.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Programmed by Tevin on 12/10/2015.
 */
public class FlatFile {

    private String name;
    private String dir;
    private Configuration config;
    private ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    private File file;

    public FlatFile(String name, String dir) {
        this.name = name;
        this.dir = dir;
        this.file = new File(dir, name);
        createFile();
        loadFile();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public ConfigurationProvider getProvider() {
        return provider;
    }

    public void setProvider(ConfigurationProvider provider) {
        this.provider = provider;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Configuration getSection(String path) {
        return this.config.getSection(path);
    }

    public void set(String key, Object value) {
        this.config.set(key, value);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public boolean isString(String path) {
        return this.getString(path) != null;
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public List<Integer> getIntegerList(String path) {
        return this.config.getIntList(path);
    }

    public boolean isInt(String path) {
        return this.getInt(path) != -1;
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public List<Short> getShortList(String path) {
        return this.config.getShortList(path);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public void createFile(){
        if(!this.file.exists()){
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFile(){
        try {
            this.config = this.provider.load(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(){
        try {
            this.provider.save(getConfig(), getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
