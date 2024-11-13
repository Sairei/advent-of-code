const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

const ROCK = 1
const PAPER = 2
const SCISSORS = 3
const LOSE = 0
const DRAW = 3
const WIN = 6

function compute_data(data) {
    var res = []

    var split = data.split("\n");
    for (var i=0; i<split.length; i++) {
        res.push(split[i].split(" "))
    }
    return res;
}

function part1(data) {
    let res = 0
    for (let i=0; i<data.length; i++) {
        let game = data[i]
        let matchRes = 0
        if (game[0] === "A") {
            if (game[1] === "X") {
                matchRes = DRAW + ROCK
            } else if (game[1] === "Y") {
                matchRes = WIN + PAPER
            } else if (game[1] === "Z") {
                matchRes = LOSE + SCISSORS
            }
        } else if (game[0] === "B") {
            if (game[1] === "X") {
                matchRes = LOSE + ROCK
            } else if (game[1] === "Y") {
                matchRes = DRAW + PAPER
            } else if (game[1] === "Z") {
                matchRes = WIN + SCISSORS
            }
        } else if (game[0] === "C") {
            if (game[1] === "X") {
                matchRes = WIN + ROCK
            } else if (game[1] === "Y") {
                matchRes = LOSE + PAPER
            } else if (game[1] === "Z") {
                matchRes = DRAW + SCISSORS
            }
        }
        res += matchRes
    }
    return res
}

function part2(data) {
    let res = 0
    for (let i=0; i<data.length; i++) {
        let game = data[i]
        let matchRes = 0
        if (game[0] === "A") {
            if (game[1] === "X") {
                matchRes = LOSE + SCISSORS
            } else if (game[1] === "Y") {
                matchRes = DRAW + ROCK
            } else if (game[1] === "Z") {
                matchRes = WIN + PAPER
            }
        } else if (game[0] === "B") {
            if (game[1] === "X") {
                matchRes = LOSE + ROCK
            } else if (game[1] === "Y") {
                matchRes = DRAW + PAPER
            } else if (game[1] === "Z") {
                matchRes = WIN + SCISSORS
            }
        } else if (game[0] === "C") {
            if (game[1] === "X") {
                matchRes = LOSE + PAPER
            } else if (game[1] === "Y") {
                matchRes = DRAW + SCISSORS
            } else if (game[1] === "Z") {
                matchRes = WIN + ROCK
            }
        }
        res += matchRes
    }
    return res
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))