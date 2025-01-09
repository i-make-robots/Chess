package com.marginallyclever.chess;

import org.junit.jupiter.api.Test;

public class ChessTest {
    @Test
    public void testCheckmate() {
        // shortest game of chess
        Chess chess = new Chess();
        var board = chess.getBoard();
        chess.makeMove(board.getPiece("g2"), board.nameToPoint("g4"));
        chess.makeMove(board.getPiece("e7"), board.nameToPoint("e5"));
        chess.makeMove(board.getPiece("f2"), board.nameToPoint("f3"));
        board.showMoves(board.getPiece("d8").getAllPossibleMoves(board));
        chess.makeMove(board.getPiece("d8"), board.nameToPoint("h4"));
    }
}
