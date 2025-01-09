package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Team team) {
        super('♝', "bishop", team,3);
    }

    @Override
    public List<Point> getAllPossibleMoves(Board board) {
        var moves = new ArrayList<Point>();
        var x = position.x;
        var y = position.y;
        doLine(moves,board,x,y,1,1);
        doLine(moves,board,x,y,1,-1);
        doLine(moves,board,x,y,-1,1);
        doLine(moves,board,x,y,-1,-1);
        return moves;
    }
}
