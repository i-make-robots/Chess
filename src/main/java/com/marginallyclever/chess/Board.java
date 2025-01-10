package com.marginallyclever.chess;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Board contains the state of play for a game of chess and the sequence of moves that have been made.
 */
public class Board {
    public static final String ANSI_BACKGROUND_BLACK  = "\033[48:2:0:0:0m";
    public static final String ANSI_BACKGROUND_WHITE  = "\033[48:2:64:64:64m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String EM_SPACE = "\u2003";
    public static final String ANSI_BACKGROUND_MOVE = "\033[48:2:0:128:0m";

    private final Piece[][] spaces = new Piece[8][8];
    private final List<PieceMove> moves = new ArrayList<>();

    /**
     * remove all pieces from board.
     */
    public void clear() {
        for(int y=0;y<8;y++) {
            for(int x=0;x<8;x++) {
                spaces[y][x] = null;
            }
        }
    }

    public record PieceMove(Piece piece, Point from, Point to) {}

    public Board() {}

    public Piece getPiece(Point p) {
        if(outOfBounds(p)) throw new IllegalArgumentException("Point out of bounds");
        return spaces[p.y][p.x];
    }

    public Piece getPiece(String name) {
        return getPiece(nameToPoint(name));
    }

    public void setPiece(Point p, Piece piece) {
        if(outOfBounds(p)) throw new IllegalArgumentException("Point out of bounds");
        spaces[p.y][p.x] = piece;
        if(piece!=null) piece.setPosition(p);
    }

    public void addMove(Piece p,Point from,Point to) {
        moves.add(new PieceMove(p,from,to));
    }

    public PieceMove getLastMove() {
        if(moves.isEmpty()) return null;
        return moves.get(moves.size()-1);
    }

    public String getTile(int x, int y) {
        return (x+y)%2 == 0 ? ANSI_BACKGROUND_BLACK : ANSI_BACKGROUND_WHITE;
    }

    public List<Piece> getTeamPieces(Team team) {
        var list = new ArrayList<Piece>();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if (spaces[y][x] != null && spaces[y][x].getTeam() == team) {
                    list.add(spaces[y][x]);
                }
            }
        }
        return list;
    }

    public String getPieceLocations(List<Piece> list) {
        var list2 = new ArrayList<Point>();
        for( var p : list ) {
            list2.add(p.getPosition());
        }
        return pointToName(list2);
    }

    public String pointToName(Point p) {
        if(outOfBounds(p)) throw new IllegalArgumentException("Point out of bounds");
        return "" + (char)('a'+p.x) + (char)('1'+7-p.y);
    }

    /**
     * @param list the list of points to convert to names
     * @return the names of the points in the list, separated by spaces
     */
    public String pointToName(List<Point> list) {
        StringBuilder builder = new StringBuilder();
        String add ="";
        for( var p : list ) {
            builder.append(add);
            builder.append(pointToName(p));
            add=" ";
        }
        return builder.toString();
    }

    /**
     * @param name the name of the point on the board
     * @return the point on the board that corresponds to the given name, or null
     */
    public Point nameToPoint(String name) {
        if(name.length()!=2) return null;
        int x = name.charAt(0)-'a';
        int y = 7-(name.charAt(1)-'1');
        var p = new Point(x,y);
        if(outOfBounds(p)) {
            return null;
        }
        return p;
    }

    public boolean isOccupied(Point p) {
        if(outOfBounds(p)) return false;
        return spaces[p.y][p.x] != null;
    }

    public void printBoard() {
        showMoves(new ArrayList<>());
    }

    public void showMoves(List<Point> moves) {
        var HS = "\u200A";
        var NBS = "\u2009"+HS;
        System.out.println(NBS+NBS+HS+"a"+NBS+"b"+NBS+"c"+NBS+"d"+NBS+"e"+NBS+"f"+NBS+"g"+NBS+"h"+ANSI_RESET);
        for(int y=0;y<8;y++) {
            System.out.print((char)('1'+7-y)+HS);
            for(int x=0;x<8;x++) {
                var p = spaces[y][x];
                System.out.print(inPointList(moves,x,y)?ANSI_BACKGROUND_MOVE:getTile(x,y));

                System.out.print(p == null
                        ? EM_SPACE
                        : p.getSymbol());
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
    }

    /**
     * @param points the list of points to check
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return true if the given point is in the list of moves
     */
    public boolean inPointList(List<Point> points, int x, int y) {
        for(var p : points) {
            if(p.x==x && p.y==y) return true;
        }
        return false;
    }

    public boolean outOfBounds(Point p) {
        return p.x<0 || p.x>=8 || p.y<0 || p.y>=8;
    }

    /**
     * @param team
     * @return the king of the given team, or null
     */
    public Piece getKing(Team team) {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                var p = spaces[y][x];
                if(p instanceof King && p.getTeam() == team) {
                    return p;
                }
            }
        }
        return null;
    }
}
