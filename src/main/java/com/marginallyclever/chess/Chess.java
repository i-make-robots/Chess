package com.marginallyclever.chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Chess is a game of chess.  It fills a {@link Board} with {@link Piece}s and allows the user to play the game.
 * The game will continue until the user quits, resigns, or checkmate.
 */
public class Chess {
    private final Scanner scanner = new Scanner(System.in);
    private final Board board = new Board();
    private int turn;
    private final ArrayList<Piece> removedPieces = new ArrayList<>();
    private int whiteScore;
    private int blackScore;

    public static void main(String[] args) {
        var game = new Chess();
        game.reset();
        while(true) {
            game.nextTurn();
        }
    }

    public Chess() {}

    public void reset() {
        whiteScore=0;
        blackScore=0;
        removedPieces.clear();

        // fill in board
        board.setPiece(new Point(0, 0),new Rook(Team.WHITE));
        board.setPiece(new Point(1, 0),new Knight(Team.WHITE));
        board.setPiece(new Point(2, 0),new Bishop(Team.WHITE));
        board.setPiece(new Point(3, 0),new Queen(Team.WHITE));
        board.setPiece(new Point(4, 0),new King(Team.WHITE));
        board.setPiece(new Point(5, 0),new Bishop(Team.WHITE));
        board.setPiece(new Point(6, 0),new Knight(Team.WHITE));
        board.setPiece(new Point(7, 0),new Rook(Team.WHITE));

        for(int i=0;i<8;i++) {
            board.setPiece(new Point(i, 1),new Pawn(Team.WHITE));
            board.setPiece(new Point(i, 6),new Pawn(Team.BLACK));
        }

        board.setPiece(new Point(0, 7),new Rook(Team.BLACK));
        board.setPiece(new Point(1, 7),new Knight(Team.BLACK));
        board.setPiece(new Point(2, 7),new Bishop(Team.BLACK));
        board.setPiece(new Point(3, 7),new Queen(Team.BLACK));
        board.setPiece(new Point(4, 7),new King(Team.BLACK));
        board.setPiece(new Point(5, 7),new Bishop(Team.BLACK));
        board.setPiece(new Point(6, 7),new Knight(Team.BLACK));
        board.setPiece(new Point(7, 7),new Rook(Team.BLACK));

        turn=0;
    }

    public void nextTurn() {
        board.printBoard();

        Piece selectedPiece = choosePiece();
        Point dest = chooseDestination(selectedPiece);
        makeMove(selectedPiece,dest);
    }

    public void makeMove(Piece selectedPiece, Point dest) {
        selectedPiece.setMoved();
        System.out.println(selectedPiece.getColorfulName()+" to "+board.pointToName(dest));
        board.setPiece(selectedPiece.getPosition(),null);
        Point oldPosition = selectedPiece.getPosition();

        if(board.getPiece(dest)!=null) {
            capture(board.getPiece(dest));
        }

        // pawn special cases
        if(selectedPiece instanceof Pawn pawn) {
            // check for promotion
            if((dest.y==0 || dest.y==7)) {
                System.out.println("Pawn promotion!");
                selectedPiece = new Queen(selectedPiece.getTeam());
            }
            // pawn en passant
            if(pawn.isEnPassant()) {
                System.out.print("en passant ");
                capture(pawn.getEnPassantPiece());
            }
        }
        board.setPiece(dest,selectedPiece);

        // king special case - in check?
        if(kingIsDead()) {
            System.out.println("Checkmate!");
            System.exit(0);
        }
        var otherTeam = Team.get(turn+1);
        if(isKingInCheck(otherTeam)) {
            if(isKingInCheckMate(otherTeam)) {
                System.out.println("Checkmate!");
                System.exit(0);
            } else {
                System.out.println("Check!");
            }
        }
        if(selectedPiece instanceof King king) {
            if(king.canCastle(board) && king.getAllPossibleMoves(board).getLast().equals(dest)) {
                // Move the rook.  The king will be moved at the end of the turn.
                if(dest.x==1) {
                    var rook = (Rook)board.getPiece(new Point(0,dest.y));
                    board.setPiece(new Point(2,dest.y),rook);
                    board.setPiece(new Point(0,dest.y),null);
                } else if(dest.x==6) {
                    var rook = (Rook)board.getPiece(new Point(7,dest.y));
                    board.setPiece(new Point(5,dest.y),rook);
                    board.setPiece(new Point(7,dest.y),null);
                }
            }
        }

        // move the piece
        board.addMove(selectedPiece,oldPosition,dest);
        turn++;
    }

    private void capture(Piece toRemove) {
        System.out.println("Captured "+toRemove.getColorfulName());
        // take it off the board
        board.setPiece(toRemove.getPosition(),null);
        // put it in the box
        removedPieces.add(toRemove);
        // add to score
        if(toRemove.getTeam()==Team.WHITE)
            whiteScore += toRemove.getValue();
        else blackScore += toRemove.getValue();
    }

    private boolean kingIsDead() {
        return removedPieces.stream().anyMatch(e->e instanceof King);
    }

    /**
     * Calculates if the king can escape check.
     * @param team the team currently in check.
     * @return true if the king cannot escape check.
     */
    private boolean isKingInCheckMate(Team team) {
        if(!isKingInCheck(team)) return false;

        // for every piece on the board on the king's team
        for(var piece : board.getTeamPieces(team)) {
            var oldPosition = new Point(piece.getPosition());
            // for every possible move
            for(var destination : piece.getAllPossibleMoves(board)) {
                // save the piece at the destination
                var oldPiece = board.getPiece(destination);
                // move the piece to the destination
                board.setPiece(oldPosition,null);
                board.setPiece(destination,piece);
                // does the move gets the king out of check?
                boolean inCheck = isKingInCheck(team);
                // undo the move
                board.setPiece(oldPosition,piece);
                board.setPiece(destination,oldPiece);
                // if the king is not in check, then the king can escape checkmate
                if(!inCheck) {
                    System.out.println("King can escape: "+piece.getColorfulName()+" "+board.pointToName(oldPosition)+" to "+board.pointToName(destination));
                    return false;
                }
            }
        }
        return true;
    }

    public Point chooseDestination(Piece selectedPiece) {
        var moves = selectedPiece.getAllPossibleMoves(board);
        board.showMoves(moves);

        Point destination;
        do {
            System.out.print("Choose a destination (" + board.pointToName(moves) + "):");
            String input = scanner.nextLine();
            quitOrResigned(input);
            destination = board.nameToPoint(input);
            Point finalDestination = destination;
            if(moves.stream().noneMatch(e->e.equals(finalDestination))) destination=null;
        } while(destination==null);
        return destination;
    }

    /**
     * @return the piece the user has chosen to move
     */
    public Piece choosePiece() {
        Piece selectedPiece = null;
        do {
            var currentTeam = Team.get(turn);
            var teamPieces = board.getTeamPieces(currentTeam);
            var canMove = filterCanMove(teamPieces);
            System.out.print("Choose a piece to move (" + board.getPieceLocations(canMove)+"): ");
            // get + parse user input
            String input = scanner.nextLine();
            quitOrResigned(input);
            selectedPiece = getPieceOrNull(canMove, input);
        } while(selectedPiece==null);
        return selectedPiece;
    }

    /**
     * @param teamPieces a list of pieces on the board
     * @return only pieces that have at least one valid move.
     */
    public List<Piece> filterCanMove(List<Piece> teamPieces) {
        var toKeep = new ArrayList<Piece>();
        for(var p : teamPieces) {
            if(!p.getAllPossibleMoves(board).isEmpty()) {
                toKeep.add(p);
            }
        }
        return toKeep;
    }

    /**
     * @return true if any enemy piece can move to the king's position.
     */
    public boolean isKingInCheck(Team team) {
        var kp = board.getKing(team).getPosition();
        // for every enemy piece
        var enemyPieces = board.getTeamPieces(team.other());
        for(var p : enemyPieces) {
            // find all possible moves
            var moves = p.getAllPossibleMoves(board);
            if(moves.isEmpty()) continue;
            // if any move is on our king
            if(moves.contains(kp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pieceList a list of pieces
     * @param input the user's input
     * @return the piece whose position matches the user's input, or null
     */
    public Piece getPieceOrNull(List<Piece> pieceList, String input) {
        input = input.toLowerCase();
        for(var p : pieceList) {
            if(board.pointToName(p.getPosition()).equals(input)) {
                return p;
            }
        }
        return null;
    }

    private void quitOrResigned(String str) {
        if(str.equalsIgnoreCase("q")) {
            System.out.println("Quitting.");
            System.exit(0);
        }
        if(str.equalsIgnoreCase("r")) {
            System.out.println("Resigned.");
            System.exit(0);
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }
}
