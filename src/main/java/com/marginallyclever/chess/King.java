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
        // must be last move for castling.
        // TODO cannot move through check.
        if(canCastle(board)) {
            if(getTeam()==Team.WHITE) {
                moves.add(new Point(6,position.y));
            } else {
                moves.add(new Point(1,position.y));
            }
        }
        return moves;
    }

    public boolean canCastle(Board board) {
        if(getMoved()) return false;
        if(getTeam()==Team.WHITE) {
            // the squares between the king and the rook must be empty
            if (board.getPiece(new Point(5, position.y)) != null) return false;
            if (board.getPiece(new Point(6, position.y)) != null) return false;
            // the rook at the corner cannot have moved
            if (!(board.getPiece(new Point(7, position.y)) instanceof Rook rook)) return false;
            return !rook.getMoved();
        } else {
            // the squares between the king and the rook must be empty
            if (board.getPiece(new Point(3, position.y)) != null) return false;
            if (board.getPiece(new Point(2, position.y)) != null) return false;
            // the rook at the corner cannot have moved
            if (!(board.getPiece(new Point(1, position.y)) instanceof Rook rook)) return false;
            return !rook.getMoved();
        }
    }
}
