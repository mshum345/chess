package model;

import java.util.Collection;

public record ResponseData(int status, String message, String username, String authToken, Collection<GameData> games, int gameID) {
}
