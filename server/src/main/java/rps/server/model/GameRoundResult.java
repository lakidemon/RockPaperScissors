package rps.server.model;

import rps.server.game.Item;
import rps.server.game.Result;

public record GameRoundResult(Result roundResult, Item opponentChoice, boolean timedOut, boolean lastRound) {}
