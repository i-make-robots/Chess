package com.marginallyclever.chess;

import java.awt.*;
import java.util.List;

/**
 * ChessPiece class represents any given chess piece.
 */
public abstract class Piece {
    public static final String ANSI_FOREGROUND_RED    = "\033[38:2:255:0:0m";
    public static final String ANSI_FOREGROUND_YELLOW = "\033[38:2:255:255:0m";

    protected final Point position = new Point();
    private final char symbol;
    private final String name;
    private final int value;
    private final Team team;
    boolean hasMoved = false;  // for pawns and castling

    /**
     * Constructor for ChessPiece.
     * @param symbol the symbol of the chess piece
     * @param name the name of the chess piece
     * @param team the team of the chess piece
     * @param value the value of the chess piece
     */
    public Piece(char symbol, String name, Team team, int value) {
        this.symbol = symbol;
        this.name = name;
        this.team = team;
        this.value = value;
    }

    /**
     * Get the symbol of the chess piece.
     * @return the symbol of the chess piece
     */
    public String getSymbol() {
        return team.colorize(""+symbol);
    }

    public String getName() {
        return team.colorize(name);
    }

    public Team getTeam() {
        return team;
    }

    /**
     * @return all possible legal moves for the chess piece.  Some moves may be outside the board limits.
     */
    abstract public List<Point> getAllPossibleMoves(Board board);

    public Point getPosition() {
        return position;
    }

    void setPosition(Point p) {
        position.setLocation(p);
    }

    /**
     * Move the piece along the given line until it hits a piece or the edge of the board.  Collect all valid moves in the list.
     * @param moves the list of moves
     * @param board the board
     * @param x starting x coordinate of the piece
     * @param y starting y coordinate of the piece
     * @param dx x direction
     * @param dy y direction
     */
    protected void doLine(List<Point> moves, Board board,int x,int y,int dx,int dy) {
        for(int i=1;i<8;i++) {
            var p = new Point(x+i*dx,y+i*dy);
            if(!addMove(moves,board,p)) break;
        }
    }

    /**
     * adds a move to the list if it is within the board limits
     * @param moves the list of moves
     * @param board the board
     * @param p the point to add
     * @return true if the move was added
     */
    protected boolean addMove(List<Point> moves, Board board,Point p) {
        if(board.outOfBounds(p)) return false;
        if(board.isOccupied(p) && board.getPiece(p).getTeam() == team) return false;
        moves.add(p);
        return true;
    }

    public int getValue() {
        return value;
    }

    public void setMoved() {
        hasMoved = true;
    }
}

