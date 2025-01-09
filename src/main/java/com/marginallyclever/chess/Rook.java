package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(Team team) {
        super('♜', "rook", team,5);
    }

    @Override
    public List<Point> getAllPossibleMoves(Board board) {
        List<Point> moves = new ArrayList<>();
        var x = position.x;
        var y = position.y;
        doLine(moves,board,x,y,1,0);
        doLine(moves,board,x,y,-1,0);
        doLine(moves,board,x,y,0,1);
        doLine(moves,board,x,y,0,-1);
        return moves;
    }
}
