package com.marginallyclever.chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class BoardTest {
    @Test
    void testToFromPosition() {
        Board board = new Board();
        Assertions.assertEquals("a1", board.pointToName(new Point(0, 7)));
        Assertions.assertEquals("h8", board.pointToName(new Point(7, 0)));
        Assertions.assertEquals(new Point(0, 7), board.nameToPoint("a1"));
        Assertions.assertEquals(new Point(7, 0), board.nameToPoint("h8"));
    }

    @Test
    void testOutOfBounds() {
        Board board = new Board();
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.pointToName(new Point(8,0)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.pointToName(new Point(0,8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.pointToName(new Point(0,-1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.pointToName(new Point(-1,0)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getPiece(new Point(8,0)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getPiece(new Point(0,8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getPiece(new Point(0,-1)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getPiece(new Point(-1,0)));
    }
}
