var fs = require('fs');
require.extensions['.txt'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

const data_test = require('./data_test.txt');
const puzzle_data = require('./puzzle_data.txt');

function compute_data(data) {
    var split = data.split("\n");
    let nbCrane = (split[1].length / 4) + 0.25
    let isMove = false
    let crane = [], moves = []
    for (let i=0; i<split.length; i++) {
        let line = split[i]
        if (line == "" || line.charAt(1) == '1') {
            isMove = true
            continue
        }
        if (!isMove) {
            for (let c=0; c<nbCrane; c++) {
                if (i == 0) {
                    crane.push([])
                }

                let toAdd = line[c*4 + 1]
                if (toAdd != " ") {
                    crane[c].unshift(toAdd)
                }
            }
        } else {
            moves.push(line)
        }
    }
    
    return [crane, moves]
}

function move(cranes, from, to, nb) {
    let eltsToMove = []
    for (let i=0; i<nb; i++) {
        eltsToMove.unshift(cranes[from-1].pop())
    }
    cranes[to-1] = cranes[to-1].concat(eltsToMove)
}

function part1(data) {
    let cranes = data[0]
    let moves = data[1]
    for (let i=0; i<moves.length; i++) {
        let tmp = moves[i].split(" ")
        let nb = tmp[1]
        let from = tmp[3]
        let to = tmp[5]

        for (let n=0; n<nb; n++) {
            move(cranes, from, to, 1)
        }
    }

    let res = ""
    for (let i=0; i<cranes.length; i++) {
        res += cranes[i].pop()
    }
    return res
}

function part2(data) {
    let cranes = data[0]
    let moves = data[1]
    for (let i=0; i<moves.length; i++) {
        let tmp = moves[i].split(" ")
        let nb = tmp[1]
        let from = tmp[3]
        let to = tmp[5]

        move(cranes, from, to, nb)
    }

    let res = ""
    for (let i=0; i<cranes.length; i++) {
        res += cranes[i].pop()
    }
    return res
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
data = compute_data(puzzle_data)
console.log('Result for puzzle 2 = ', part2(data))