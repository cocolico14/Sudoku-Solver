
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Label;

/**
 *
 * @author soheilchangizi
 */
public class State {
    
    public static final int BSIZE = 9;
    private int blank;
    private Variable[][] board;
    private State parent;
    private ArrayList<State> children;
    
    public State(int[][] board) {
        this.board = new Variable[BSIZE][BSIZE];
        this.children = new ArrayList<>();
        this.blank = 0;
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                this.board[i][j] = new Variable(board[i][j]);
                if(this.board[i][j].getValue() == 0) this.blank++;
                if(i>=0 && i<3 && j>=0 && j<3) this.board[i][j].setRegion(1);
                else if(i>=3 && i<6 && j>=0 && j<3) this.board[i][j].setRegion(2);
                else if(i>=6 && i<9 && j>=0 && j<3) this.board[i][j].setRegion(3);
                else if(i>=0 && i<3 && j>=3 && j<6) this.board[i][j].setRegion(4);
                else if(i>=3 && i<6 && j>=3 && j<6) this.board[i][j].setRegion(5);
                else if(i>=6 && i<9 && j>=3 && j<6) this.board[i][j].setRegion(6);
                else if(i>=0 && i<3 && j>=6 && j<9) this.board[i][j].setRegion(7);
                else if(i>=3 && i<6 && j>=6 && j<9) this.board[i][j].setRegion(8);
                else if(i>=6 && i<9 && j>=6 && j<9) this.board[i][j].setRegion(9);
            }
        }
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                if(this.board[i][j].getValue() == 0){
                    this.board[i][j].setDomain(evalDomain(i, j));
                }
            }
        }
    }
    
    public State(Label[][] board) {
        this.board = new Variable[BSIZE][BSIZE];
        this.children = new ArrayList<>();
        this.blank = 0;
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                this.board[i][j] = new Variable(Integer.valueOf(board[i][j].getText()));
                if(this.board[i][j].getValue() == 0) this.blank++;
                if(i>=0 && i<3 && j>=0 && j<3) this.board[i][j].setRegion(1);
                else if(i>=3 && i<6 && j>=0 && j<3) this.board[i][j].setRegion(2);
                else if(i>=6 && i<9 && j>=0 && j<3) this.board[i][j].setRegion(3);
                else if(i>=0 && i<3 && j>=3 && j<6) this.board[i][j].setRegion(4);
                else if(i>=3 && i<6 && j>=3 && j<6) this.board[i][j].setRegion(5);
                else if(i>=6 && i<9 && j>=3 && j<6) this.board[i][j].setRegion(6);
                else if(i>=0 && i<3 && j>=6 && j<9) this.board[i][j].setRegion(7);
                else if(i>=3 && i<6 && j>=6 && j<9) this.board[i][j].setRegion(8);
                else if(i>=6 && i<9 && j>=6 && j<9) this.board[i][j].setRegion(9);
            }
        }
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                if(this.board[i][j].getValue() == 0){
                    this.board[i][j].setDomain(evalDomain(i, j));
                }
            }
        }
    }
    
    public boolean isPossNode(boolean isForwardChecking){
        //forward checking
        if(isForwardChecking){
            for (int i = 0; i < BSIZE; i++) {
                for (int j = 0; j < BSIZE; j++) {
                    if(this.board[i][j].getValue() == 0){
                        if(evalDomain(i, j).isEmpty()){
                            return false;
                        }
                    }
                }
            }
        }
        if(this.blank > 1) return true;
        return false;
    }
    
    
    public ArrayList<State> nextStates(State now){
        ArrayList<State> nSs = new ArrayList<>();
        State nS = null;
        int[][] v = new int[BSIZE][BSIZE]; //copy of board
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                v[i][j] = this.board[i][j].getValue();
            }
        }
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                if(this.board[i][j].getValue() == 0){
                    this.board[i][j].setDomain(evalDomain(i, j));
                }
            }
        }
        for (int i = 0; i < BSIZE; i++) {
            for (int j = 0; j < BSIZE; j++) {
                if(this.board[i][j].getValue() == 0){
                    for (int k = 0; k < this.board[i][j].getDomain().size(); k++) {
                        nS = new State(v);
                        nS.board[i][j].setValue(this.board[i][j].getDomain().get(k));
                        nSs.add(nS);
                    }
                    i = BSIZE;
                    j = BSIZE;
                }
            }
        }
        return nSs;
    }
    
    public ArrayList<Integer> evalDomain(int i, int j){
        ArrayList<Integer> re = new ArrayList<>();
        ArrayList<Integer> ref = new ArrayList<>();
        
        for (Integer integer : colCon(j)) {
            re.add(integer);
        }
        for (Integer integer : rowCon(i)) {
            re.add(integer);
        }
        for (Integer integer : boxCon(i, j)) {
            re.add(integer);
        }
        for (Integer integer : re) {
            if(colCon(j).contains(integer)
                    && rowCon(i).contains(integer)
                    && boxCon(i, j).contains(integer)
                    && !ref.contains(integer)){
                ref.add(integer);
            }
        }
        
        return ref;
    }
    
    public ArrayList<Integer> colCon(int j){
        ArrayList<Integer> re = new ArrayList<>();
        boolean[] find = new boolean[BSIZE+1];
        for (int i = 0; i < BSIZE; i++) {
            if(this.board[i][j].getValue() != 0){
                find[this.board[i][j].getValue()] = true;
            }else{
                find[this.board[i][j].getValue()] = false;
            }
        }
        for (int i = 1; i < BSIZE+1; i++) {
            if(!find[i]){
                re.add(i);
            }
        }
        return re;
    }
    
    public ArrayList<Integer> rowCon(int i){
        ArrayList<Integer> re = new ArrayList<>();
        boolean[] find = new boolean[BSIZE+1];
        for (int j = 0; j < BSIZE; j++) {
            if(this.board[i][j].getValue() != 0){
                find[this.board[i][j].getValue()] = true;
            }else{
                find[this.board[i][j].getValue()] = false;
            }
        }
        for (int j = 1; j < BSIZE+1; j++) {
            if(!find[j]){
                re.add(j);
            }
        }
        return re;
    }
    
    public ArrayList<Integer> boxCon(int i, int j){
        ArrayList<Integer> re = new ArrayList<>();
        boolean[] find = new boolean[BSIZE+1];
        int reg = this.board[i][j].getRegion();
        for (int k = 0; k < BSIZE; k++) {
            for (int l = 0; l < BSIZE; l++) {
                if(this.board[k][l].getRegion() == reg){
                    if(this.board[k][l].getValue() != 0){
                        find[this.board[k][l].getValue()] = true;
                    }else{
                        find[this.board[i][j].getValue()] = false;
                    }
                }
            }
        }
        
        for (int k = 1; k < BSIZE+1; k++) {
            if(!find[k]){
                re.add(k);
            }
        }
        return re;
    }
    
    public boolean isSatisfied(){
        if(this.blank > 1) return false;
        return true;
    }
    
    public Variable[][] getBoard() {
        return board;
    }
    
    public void setParent(State parent) {
        this.parent = parent;
    }
    
    public State getParent() {
        return parent;
    }
    
    public void addChildren(State st){
        this.children.add(st);
    }
    
    public ArrayList<State> getChildren() {
        return children;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.blank;
        hash = 89 * hash + Arrays.deepHashCode(this.board);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (this.blank != other.blank) {
            return false;
        }
        for (int k = 0; k < BSIZE; k++) {
            for (int l = 0; l < BSIZE; l++) {
                if(this.board[k][l].getValue() != other.board[k][l].getValue()) return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        String out = "";
        for (int k = 0; k < BSIZE; k++) {
            for (int l = 0; l < BSIZE; l++) {
                out += this.board[k][l].getValue() + ", ";
            }
            out += "\n";
        }
        out += "\n---------\n";
        return out;
    }
    
    public int getBlank() {
        return blank;
    }
    
}
