package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Team team) {
        super('♟', "pawn", team,1);
    }

    @Override
    public List<Point> getAllPossibleMoves(Board board) {
        List<Point> moves = new ArrayList<>();
        var dir = getTeam() == Team.WHITE ? 1 : -1;
        var x = position.x;
        var y = position.y;
        var maxSteps = hasMoved ? 1 : 2;
        for(int i=0;i < maxSteps;++i) {
            if(!addMove(moves,board,new Point(x, y+dir*(1+i)))) break;
        }
        if(canAttack(board,new Point(x+1, y+dir))) {
            moves.add(new Point(x+1, y+dir));
        }
        if(canAttack(board,new Point(x-1, y+dir))) {
            moves.add(new Point(x-1, y+dir));
        }
        return moves;
    }

    private boolean canAttack(Board board, Point point) {
        if(board.outOfBounds(point)) return false;
        var piece = board.getPiece(point);
        return piece != null && piece.getTeam() != getTeam();
    }

    @Override
    protected boolean addMove(List<Point> moves, Board board, Point p) {
        if(board.outOfBounds(p)) return false;
        if(board.isOccupied(p)) return false;
        moves.add(p);
        return true;
    }
}
