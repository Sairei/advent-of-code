const { data_test, data_test_2 } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

class Pos {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    move(direction) {
        switch (direction) {
            case 'R':
                this.y++
                break;
            case 'L':
                this.y--
                break;
            case 'U':
                this.x++
                break;
            case 'D':
                this.x--
                break;
            default:
                console.log(`Wrong direction`);
        }
    }

    isAdjacentOrSame(p) {
        return Math.abs(p.x - this.x) <= 1 && Math.abs(p.y - this.y) <= 1
    }

    toString() {
        return (`[${this.x},${this.y}]`)
    }
}

function compute_data(data) {
    let map = []
    let lines = data.split("\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i]
        map.push(line.split(" "))
    }
    return map
}

function move_tail(T, H) {
    if (T.x == H.x) {
        T.y += H.y < T.y ? -1 : 1
    } else if (T.y == H.y) {
        T.x += H.x < T.x ? -1 : 1
    } else {
        T.x += H.x < T.x ? -1 : 1
        T.y += H.y < T.y ? -1 : 1
    }
}

function part1(data) {
    let H = new Pos(0, 0)
    let T = new Pos(0, 0)
    let tailsPos = []
    tailsPos.push(T.toString())

    for (let i=0; i<data.length; i++) {
        let move = data[i]
        for (let m=0; m<Number(move[1]); m++) {
            H.move(move[0])
            if (!H.isAdjacentOrSame(T)) {
                move_tail(T, H)
                if (!tailsPos.includes(T.toString())) {
                    tailsPos.push(T.toString())
                }
            }
        }
    }

    return tailsPos.length
}

function part2(data) {
    let nbKnot = 10
    let knots = []
    for (let i=0; i<nbKnot; i++) {
        knots.push(new Pos(0, 0))
    }

    let tailsPos = []
    tailsPos.push(knots[nbKnot-1].toString())

    for (let i=0; i<data.length; i++) {
        let move = data[i]
        for (let m=0; m<Number(move[1]); m++) {
            knots[0].move(move[0])
            for (let i=1; i<nbKnot; i++) {
                if (!knots[i-1].isAdjacentOrSame(knots[i])) {
                    move_tail(knots[i], knots[i-1])
                }
            }
            if (!tailsPos.includes(knots[nbKnot-1].toString())) {
                tailsPos.push(knots[nbKnot-1].toString())
            }
        }
    }

    return tailsPos.length
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))