package com.marginallyclever.chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

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

    @Test
    void testEnPassant() {
        // put two pawns on an empty board
        Chess chess = new Chess();
        Board board = chess.getBoard();
        Pawn blackPawn = new Pawn(Team.BLACK);
        Pawn whitePawn = new Pawn(Team.WHITE);
        board.setPiece(new Point(3, 3), whitePawn);
        board.setPiece(new Point(4, 5), blackPawn);
        // move the white pawn two spaces forward
        board.addMove(whitePawn,new Point(3, 3), new Point(3, 5));
        // can the black pawn attack the white pawn en passant?
        var list = blackPawn.getAllPossibleMoves(board);
        Assertions.assertTrue(blackPawn.isEnPassant());
        // two forward steps and one diagonal attack
        Assertions.assertEquals(3, list.size());
        // move to the position behind the white pawn.
        Assertions.assertEquals(list.getLast(), new Point(3, 4));
        chess.makeMove(blackPawn, list.getLast());
        // assert the white pawn was removed
        Assertions.assertNull(board.getPiece(new Point(3, 5)));
        Assertions.assertEquals(1, chess.getBlackScore());
        Assertions.assertEquals(0, chess.getWhiteScore());
    }
}
