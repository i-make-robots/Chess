package com.marginallyclever.chess;

public enum Team {
    WHITE,
    BLACK;

    public static Team get(int turn) {
        return turn % 2 == 0 ? WHITE : BLACK;
    }

    public Team other() {
        return this == WHITE ? BLACK : WHITE;
    }

    public String colorize(String input) {
        return (this == WHITE ? Piece.ANSI_FOREGROUND_YELLOW : Piece.ANSI_FOREGROUND_RED) + input + Board.ANSI_RESET;
    }
};
