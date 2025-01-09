package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Team team) {
        super( '♛', "queen", team,9);
    }

    @Override
    public List<Point> getAllPossibleMoves(Board board) {
        List<Point> moves = new ArrayList<>();
        var x = position.x;
        var y = position.y;
        // all bishop moves
        doLine(moves,board,x,y,1,1);
        doLine(moves,board,x,y,1,-1);
        doLine(moves,board,x,y,-1,-1);
        doLine(moves,board,x,y,-1,1);
        // all rook moves
        doLine(moves,board,x,y,1,0);
        doLine(moves,board,x,y,-1,0);
        doLine(moves,board,x,y,0,1);
        doLine(moves,board,x,y,0,-1);

        return moves;
    }
}
