package com.marginallyclever.chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class ChessTest {
    @Test
    public void testCheckmate() {
        // shortest game of chess
        Chess chess = new Chess();
        chess.reset();
        var board = chess.getBoard();
        chess.makeMove(board.getPiece("g2"), board.nameToPoint("g4"));
        chess.makeMove(board.getPiece("e7"), board.nameToPoint("e5"));
        chess.makeMove(board.getPiece("f2"), board.nameToPoint("f3"));
        board.showMoves(board.getPiece("d8").getAllPossibleMoves(board));
        chess.makeMove(board.getPiece("d8"), board.nameToPoint("h4"));
    }

    @Test
    void testEnPassantAndScoring() {
        // put two pawns on an empty board
        Chess chess = new Chess();
        chess.reset();
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

    @Test
    void testPromotion() {
        Chess chess = new Chess();
        chess.reset();
        Board board = chess.getBoard();
        // put a pawn on the board
        Pawn whitePawn = new Pawn(Team.WHITE);
        board.setPiece(new Point(0, 6), whitePawn);
        // move the pawn to the end of the board
        chess.makeMove(whitePawn, new Point(0, 7));
        // assert the pawn was promoted
        Assertions.assertInstanceOf(Queen.class, board.getPiece(new Point(0, 7)));
    }

    @Test
    void testCastling() {
        Chess chess = new Chess();
        chess.reset();
        Board board = chess.getBoard();
        // put a king and rook on the board
        King king = (King)board.getPiece("e8");
        Rook rook = (Rook)board.getPiece("h8");
        board.setPiece(board.nameToPoint("f8"), null);
        board.setPiece(board.nameToPoint("g8"), null);
        // move the king two spaces to the right
        chess.makeMove(king, board.nameToPoint("g8"));
        // assert the rook was moved to the left of the king
        Assertions.assertEquals(rook, board.getPiece(board.nameToPoint("f8")));
    }
}
