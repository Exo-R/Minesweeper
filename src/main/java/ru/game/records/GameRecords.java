package ru.game.records;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import ru.game.gametype.GameType;

@Log4j2
public class GameRecords {

    private static final String PATH_DIRECTORY_RECORDS = System.getProperty("user.dir") + "\\data\\";
    private static final String FILENAME_RECORDS = "records.json";
    private static final String EXAMPLE_USERNAME = "unknownPlayer";
    private static final int EXAMPLE_TIME = 9999;

    private List<Record> records;
    private final File recordsFile;

    public GameRecords() {
        recordsFile = new File(PATH_DIRECTORY_RECORDS + FILENAME_RECORDS);
        if (!recordsFile.isFile()) {
            createJSONFile(recordsFile);
        }
        try {
            Record[] recordsArray = new ObjectMapper().readValue(recordsFile, Record[].class);
            records = new ArrayList<>(Arrays.asList(recordsArray));

            log.info("The 'GameRecords' has been initialized!");

        } catch (IOException e) {
            log.error("Error reading file data with records! Underlying cause was [{}]", e.getMessage());
        }
    }

    private void createJSONFile(File file) {
        try {
            file.createNewFile();
        } catch (Exception e) {
            log.error("Error creating file data with records! Underlying cause was [{}]", e.getMessage());
        }
        Record[] records = new Record[]{
                new Record(GameType.NOVICE, EXAMPLE_USERNAME, EXAMPLE_TIME),
                new Record(GameType.MEDIUM, EXAMPLE_USERNAME, EXAMPLE_TIME),
                new Record(GameType.EXPERT, EXAMPLE_USERNAME, EXAMPLE_TIME)
        };
        try {
            new ObjectMapper().writeValue(file, records);
        } catch (IOException e) {
            log.error("Error writing file data with records! Underlying cause was [{}]", e.getMessage());
        }
    }

    public void addNewRecord(GameType gameType, String username, int time) {
        for (Record record : records) {
            if (record.getDifficult().equals(gameType) && record.getTime() > time) {
                record.setUsername(username);
                record.setTime(time);
                updateJSONFile();
                return;
            }
        }
    }

    private void updateJSONFile() {
        try {
            new ObjectMapper().writeValue(recordsFile, records);
        } catch (IOException e) {
            log.error("Error updating file data with records! Underlying cause was [{}]", e.getMessage());
        }
    }

    public Record getRecordByGameType(GameType gameType) {
        for (Record record : records) {
            if (record.getDifficult().equals(gameType)) {
                return record;
            }
        }
        log.error("Error getting data from file with records!");
        return null;
    }

}
