package com.promise8.wwwbe.model.dto;

import java.util.Arrays;

public enum ActionType {
    END_VOTE("endVote"),
    END_MEETING("endMeeting");


    ActionType(String name) {
        this.name = name;
    }

    private String name;

    public static ActionType of(String actionTypeStr) {
        if (actionTypeStr == null) {
            throw new IllegalArgumentException("not valid argument. " + actionTypeStr);
        }

        return Arrays.asList(ActionType.values()).stream()
                .filter(actionType -> actionType.name.equals(actionTypeStr))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("not valid argument. " + actionTypeStr);
                });
    }
}
