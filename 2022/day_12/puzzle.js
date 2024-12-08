var fs = require('fs');
require.extensions['.txt'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

const data_test = require('./data_test.txt');
const puzzle_data = require('./puzzle_data.txt');

class Pos {
    constructor(x, y, elevation) {
        this.x = x;
        this.y = y;
        this.h = elevation
    }

    toString() {
        return (`[${this.x},${this.y}] ${this.h}`)
    }
}

function compute_data(data) {
    let res = []
    let S = []
    let E = null
    let map = []
    
    let lines = data.split("\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i]

        if (line.includes("S")) {
            S.push(new Pos(i, line.indexOf("S"), "a".charCodeAt(0)))
            line = line.replace("S", "a")
        }
        
        if (line.includes("E")) {
            E = new Pos(i, line.indexOf("E"), "z".charCodeAt(0))
            line = line.replace('E', 'z')
        }

        map.push(line.split(""))
    }
    res.push(S)
    res.push(E)
    res.push(map)
    return res
}

function moves_ok(pos, map) {
    let possibles = []
    let adjacent = [
        [1, 0], [-1, 0],
        [0, 1], [0, -1]
    ]

    for (let i=0; i<4; i++) {
        let m = adjacent[i]
        let tmpPos = new Pos(pos.x + m[0], pos.y + m[1], 0) 
        if (is_in_grid(tmpPos, map)){
            tmpPos.h = map[pos.x + m[0]][pos.y + m[1]].charCodeAt(0)
            if (tmpPos.h <= pos.h+1) {
                possibles.push(tmpPos)
             }
        }
    }

    return possibles
}

function is_in_grid(p, map) {
    return 0 <= p.x  && p.x < map.length &&
        0 <= p.y && p.y < map[0].length
}

function part1(data) {
    const seen = [];
    let minStep = 0
    
    const queue = data[0].map((start) => ({ p: start, steps: 0 }));
    const end = data[1]
    while (queue.length) {
        const {
            p,
            steps,
        } = queue.shift();

        if (seen[p.x]?.[p.y]) {
            continue;
        }
        if (p.x === end.x && p.y === end.y) {
            minStep = steps
            break;
        }

        for (const move of moves_ok(p, data[2])) {
            if (!seen[move.x]?.[move.y]) {
                queue.push({ p: move, steps: steps + 1 });
            }
        }
        seen[p.x] = seen[p.x] ?? [];
        seen[p.x][p.y] = 1;
    }

    return minStep
}

function part2(data) {
    let starts = []
    const map = data[2]
    for (let x=0; x<map.length; x++) {
        for (let y=0; y<map[x].length; y++) {
            let p = new Pos(x, y, map[x][y].charCodeAt(0))
            if (map[x][y] === 'a' && !data[0].some(s => s.toString() === p.toString())) {
                data[0].push(p)
            }
        }
    }
    return part1(data)
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))