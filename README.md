# Sudoku-Solver
> Using CSP algorithm with Forward Checking for solving Sudoku Puzzle

<img src="./assets/overview.png" width="350" align="middle">

<hr />

## Documentation

  Variable Class :
  
  - For holding data of each variable such as its Value and Domain.

  State Class:
  
  - For holding data of each state.
  
  ProblemTree Class:
  
  - Search through all available digit (the domain of variable) for the next empty spot in the table.
  
  
  **A naive approach is to only check the first empty spot in table if there is no value in its domain then it backtracks**
  
  **But with Forward Checking it checks the next empty spot after the first one, if the variable we had chosen for the first empty spot left us with no choice for the second blank spot then it backtracks**
  
<hr />

## Example

### The table that has been shown in the beginning is solvable ###

<img src="./assets/solved.png" width="350" align="middle">

### Then the example below is not solvable because there are two 7 in the first region of the table ###

<img src="./assets/unsolvable.png" width="350" align="middle">

### There is no solution ###

<img src="./assets/notsolved.png" width="350" align="middle">


<hr />

## Author

  - Soheil Changizi ( [@cocolico14](https://github.com/cocolico14) )


## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details

