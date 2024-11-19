const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

function compute_data(data) {
    let map = []
    let lines = data.split("\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i]
        map.push(line.split(" "))
    }
    return map
}

function compute_crt(nbCycle, X) {
    let res = ""

    let pos = (nbCycle - 1) % 40
    if (nbCycle != 1 && pos == 0) {
        res += "\n"
    }

    if (X-1 <= pos && pos <= X+1) {
        res += "#"
    } else {
        res += " "
    }

    return res
}

function part1(data) {
    let X = 1
    let cycleWanted = [20, 60, 100, 140, 180, 220]
    let res = []

    let nbCycle = 1
    for (let i=0; i<data.length; i++) {
        let cmd = data[i]

        if (cycleWanted.includes(nbCycle)) {
            res.push(X)
        }
        nbCycle++

        if (cmd.length == 2) {
            if (cycleWanted.includes(nbCycle)) {
                res.push(X)
            }
            nbCycle++
            X += Number(cmd[1])
        }
    }

    let sum = 0
    for (let i=0; i<res.length; i++) {
        sum += res[i] * cycleWanted[i]
    }
    return sum
}

function part2(data) {
    let X = 1
    let res = ""

    let nbCycle = 1
    for (let i=0; i<data.length; i++) {
        let cmd = data[i]

        res += compute_crt(nbCycle, X)
        nbCycle++

        if (cmd.length == 2) {
            res += compute_crt(nbCycle, X)
            nbCycle++
            X += Number(cmd[1])
        }
    }

    return res
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 =')
console.log(part2(data))