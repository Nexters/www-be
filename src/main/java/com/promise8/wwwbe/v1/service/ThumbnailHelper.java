package com.promise8.wwwbe.v1.service;

public class ThumbnailHelper {

    public static CharacterType getCharacter(long id, boolean isHost) {
        if (isHost) {
            return CharacterType.CREATOR;
        }

        CharacterType[] characterTypes = CharacterType.values();
        long size = characterTypes.length - 1;
        int index = (int) (id % size);
        return characterTypes[index];
    }

    public static YaksokiType getYaksoki(long id) {
        YaksokiType[] yaksokiTypes = YaksokiType.values();
        long size = yaksokiTypes.length;
        int index = (int) (id % size);
        return yaksokiTypes[index];
    }

    public enum CharacterType {
        USER_1,
        USER_2,
        USER_3,
        CREATOR
    }

    public enum YaksokiType {
        EAT,
        WORK,
        PLAY,
        REST
    }
}
