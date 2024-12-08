var fs = require('fs');
require.extensions['.txt'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

const data_test = require('./data_test.txt');
const puzzle_data = require('./puzzle_data.txt');

function compute_data(data) {
    let map = []
    
    let lines = data.split("\n\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i]

        map.push(line.split("\n").map(e => eval(e)))
    }
    return map
}

function compare(left, right) {
    while (left.length && right.length) {
        const l = left.shift()
        const r = right.shift()

        if (is_number(l) && is_number(r)) {
            if (l < r) {
                return true
            } else if (l > r) {
                return false
            }
        } else {
            let tmpL = l
            let tmpR = r
            if (is_number(l)) {
                tmpL = [l]
            }
            if (is_number(r)) {
                tmpR = [r]
            }

            const res = compare(tmpL, tmpR)
            if (typeof res === "boolean") {
                return res
            }
        }
    }

    if (left.length) return false;
    if (right.length) return true;
}

function is_number(x) {
    return !Array.isArray(x) && !isNaN(x)
}

function part1(data) {
    let res = 0
    for (let i=0; i<data.length; i++) {
        const p1 = data[i][0]
        const p2 = data[i][1]

        if (compare(structuredClone(p1), structuredClone(p2))) {
            res += i+1
        }
    }
    return res
}

function part2(data) {
    let map = []
    for (let packets of data) {
        map = map.concat(packets)
    }
    map.push([[2]], [[6]])
    map = map.sort((a, b) => compare(structuredClone(a), structuredClone(b)) ? -1 : 1)
    
    const key2 = map.findIndex(x => x.length === 1 && x[0].length === 1 && x[0][0] === 2) + 1
    const key6 = map.findIndex(x => x.length === 1 && x[0].length === 1 && x[0][0] === 6) + 1

    return key2*key6
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))