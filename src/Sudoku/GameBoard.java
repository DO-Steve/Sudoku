package Sudoku;

public class GameBoard {

    private int[][] board;
    private int[][] solvedBoard;

        //** Constructeur de base **//
        public GameBoard() {
            board = new int[9][9];
            solvedBoard = new int[9][9];
            this.generate();
        }

        //** @param tableau de copie **//
    public GameBoard(int[][] board){
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

        //** @return le tableau resolu**//
    public int[][] getSolvedBoard(){
            return this.solvedBoard;
    }

        //** @param de la ligne, colonne et la valeur de la cellule **//
    public int getCell(int row, int col){
            return board[row][col];
    }

    //** @param de la ligne, colonne et la valeur de la cellule **//
    public int getSolvedCell(int row, int col){
            return solvedBoard[row][col];
    }
        //** @param de la nouvelle valeur de la cellule **//
    public void setCell(int num, int row, int col){
            this.board[row][col] = num;
    }

        //** @return vrai si toutes les cases sont pleines **//
    public boolean isFull(){
            boolean res = true;
            for (int i = 0; i < 9; i++){
                for (int j = 0; j < 9; j++){
                    if(board[i][j] == 0){
                        res = false;
                        break;
                    }
                }
            }
            return res;
    }

        //** @return vrai si le tableau correspond au tableau corrige**//
    public boolean isComplete() {
        boolean res = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j] != solvedBoard[i][j]) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

        /** Une fonction recursive pour la generation de la grille
          *  - On remplit les diagonales des sous tableau car ils sont indépendant
          *  - On remplit le reste des valeurs
          *  - On recupere le nombre de valeur a enleve en fonction de la difficulte
          *  - On retire des valeurs des cases du tableau (+- 5)
          *  - Si c est reussi c est bon
          *  - Sinon on recommence
          *  - Si ca echoue alors on reprend le tableau dans l etat initial
        **/

    public void generate() {
        fillDiagonal();
        fillRemaining(0, 3);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.solvedBoard[i][j] = board[i][j];
            }
        }
        int n = 50;
        removeNumbers(n);
    }

        //** @return le tableau comme un String**//
    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                res += board[i][j] + " ";
            }
            res += "\n";
        }
        res += "";
        return res;
    }

        //** Rempli les 3 diagonales des sous tableaux**//
    private void fillDiagonal() {
        for (int i = 0; i <9; i+=3) {
            fillBox(i,i);
        }
    }

        //** Rempli les boites 3*3 **//
    private void fillBox(int row, int col) {
        int num = getRandomNumber();
        for (int i = 0; i <3; i++) {
            for (int j = 0; j <3; j++) {
                while (isInBox(num, row, col)){
                    num = getRandomNumber();
                }
                board[row+i][col+j] = num;
            }
        }
    }

        //** Fonction recursive pour remplir tout les valeurs vides**//
    private boolean fillRemaining(int row, int col) {

        if (col >= 9 && row<8){
            row+=1; col=0;
        }

        if(col >=9 && row >=9) {
            return true;
        }

        if (row < 3) {
            if (col <3) col=3;
        } else if (row < 6) {
            if (col >=3 && col <6) col =6;
        } else {
            if (col >= 6) {
                row+= 1; col=0;
                if (row >= 9) return true;
            }
        }

        for (int num=1; num <=9; num++) {
            if (canPlace(num, row, col)){
                board[row][col] = num;
                if (fillRemaining(row, col+1))
                    return true;
                board[row][col] = 0;
            }
        }

        return false;
    }

        //** @return une case aleatoire du tableau en point 2D**//
    public Point randomIndice() {
        int i = getRandomNumber()-1;
        int j = getRandomNumber()-1;
        Point initialCell = new Point(i,j);
        while(board[i][j] == solvedBoard[i][j]) {
            j++;
            if (j > 8) {
                j=0; i++;
            }
            if (i > 8) {
                i=0;
            }
            //** Exception dans le cas de pas de case vide **//
            if (i== initialCell.getX() && j == initialCell.getY()){
                return null;
            }
        }
        return new Point(i,j);
    }

        //** verification de la colonne **//
    private boolean isInRow(int num, int row) {
        boolean res = false;
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                res = true;
                break;
            }
        }
        return res;
    }

        //** verification de la ligne **//
    private boolean isInCol(int num, int col) {
        boolean res = false;
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                res = true;
                break;
            }
        }
        return res;
    }

        //** test pour placer un nombre **//
    public boolean canPlace(int num, int row, int col) {
        boolean res = true;
        if(isInRow(num, row) || isInCol(num, col) || isInBox(num, row-row%3, col-col%3)){
            res = false;
        }

        return res;
    }

        //** test pour verifier si le nombre est dans le tableau**//
    private boolean isInBox(int num, int row, int col) {
        boolean res = false;
        for (int i = 0; i <3; i++) {
            for (int j = 0; j <3; j++) {
                if (board[row+i][col+j] == num) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

        //** @return un nombre aleatoire entre 1 et 9 **//
    private int getRandomNumber() {
        return (int) Math.floor((Math.random()*9 +1));
    }

    /**
     * Principe :
     *     - On enleve un nombre
     *     - On verifie que la solution est tjrs unique
     *     - Si c'est le cas, on en supprime un autre
     *     - Sinon on arrete et on remet le nombre supprime
     * @param nb le nombre de valeur a enleve
     * @return le nombre de valeur enleve
     */
    private int removeNumbers(int nb) {
        boolean canContinue = true;
        while(canContinue) {
            int col = getRandomNumber()-1;
            int row = getRandomNumber()-1;
            if (board[row][col] != 0) {
                int value = board[row][col];
                board[row][col] = 0;
                if (isUniqueSolution()) {
                    nb--;
                    if (nb <=0) {
                        canContinue = false;
                    }
                } else {
                    board[row][col] = value;
                    canContinue = false;
                }
            }
        }
        return nb;
    }

    /**
     * methode statique pour comparer 2 tableaux
     * @param b1 le premier tableau
     * @param b2 le deuxieme tableau
     * @return true si les tableaux sont egaux
     */
    public static boolean areEquals(GameBoard b1, GameBoard b2) {
        for (int i=0; i<9; i++) {
            for (int j =0; j<9; j++) {
                if (b1.getCell(i,j) != b2.getCell(i,j)){
                    return false;
                }
            }
        }
        return true;
    }

        //** retourne vrai si il existe une unique solution**//
    public boolean isUniqueSolution() {
        SudokuSolver solver = new SudokuSolver(new GameBoard(board));

        solver.solve(true);
        GameBoard firstSolution = solver.getBoard();

        solver = new SudokuSolver(new GameBoard(board));
        solver.solve(false);

        GameBoard secondSolution = solver.getBoard();

        return areEquals(firstSolution, secondSolution);
    }
}