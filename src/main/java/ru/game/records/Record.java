package ru.game.records;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.game.gametype.GameType;

@Getter
@Setter
@NoArgsConstructor
public class Record {

    private GameType difficult;
    private String username;
    private int time;

    public Record(GameType difficult, String username, int time) {
        this.difficult = difficult;
        this.username = username;
        this.time = time;
    }

}
