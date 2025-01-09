package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Team team) {
        super( '♚', "king", team,0);
    }

    @Override
    public List<Point> getAllPossibleMoves(Board board) {
        List<Point> moves = new ArrayList<>();
        // TODO cannot move into check
        addMove(moves,board,new Point(0,1));
        addMove(moves,board,new Point(0,-1));
        addMove(moves,board,new Point(1,0));
        addMove(moves,board,new Point(-1,0));
        addMove(moves,board,new Point(1,1));
        addMove(moves,board,new Point(1,-1));
        addMove(moves,board,new Point(-1,1));
        addMove(moves,board,new Point(-1,-1));

        return moves;
    }
}
