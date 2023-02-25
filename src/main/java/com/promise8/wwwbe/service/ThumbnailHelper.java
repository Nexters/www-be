package com.promise8.wwwbe.service;

public class ThumbnailHelper {

    public static CharacterType getCharacter(long id) {
        CharacterType[] characterTypes = CharacterType.values();
        long size = characterTypes.length;
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
        CREATOR,
        USER_1,
        USER_2,
        USER_3,
        USER_4
    }

    public enum YaksokiType {
        EAT,
        WORK,
        PLAY,
        REST
    }
}
