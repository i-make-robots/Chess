package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Team team) {
        super( '♞', "knight", team,3);
    }

    @Override
    public List<Point> getAllPossibleMoves(Board board) {
        List<Point> moves = new ArrayList<>();
        var x = position.x;
        var y = position.y;
        addMove(moves,board,new Point(x+1,y+2));
        addMove(moves,board,new Point(x+1,y-2));
        addMove(moves,board,new Point(x-1,y+2));
        addMove(moves,board,new Point(x-1,y-2));
        addMove(moves,board,new Point(x+2,y+1));
        addMove(moves,board,new Point(x+2,y-1));
        addMove(moves,board,new Point(x-2,y-1));
        addMove(moves,board,new Point(x-2,y+1));
        return moves;
    }
}
