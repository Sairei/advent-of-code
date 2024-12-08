var fs = require('fs');
require.extensions['.txt'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

const data_test = require('./data_test.txt');
const puzzle_data = require('./puzzle_data.txt');

function compute_data(data) {
    var res = []

    var split = data.split("\n");
    for (var i=0; i<split.length; i++) {
        let s = split[i]
        res.push(s.split(","))
    }
    return res;
}

function part1(data) {
    let res = 0
    for (let i=0; i<data.length; i++) {
        let first = data[i][0].split("-")
        let min1 = Number(first[0]), max1 = Number(first[1])
        let second = data[i][1].split("-")
        let min2 = Number(second[0]), max2 = Number(second[1])
        if (min1 <= min2 && max1 >= max2) {
            res++
        } else if (min1 >= min2 && max1 <= max2) {
            res++
        }
    }
    return res
}

function part2(data) {
    let res = 0
    for (let i=0; i<data.length; i++) {
        let first = data[i][0].split("-")
        let min1 = Number(first[0]), max1 = Number(first[1])
        let second = data[i][1].split("-")
        let min2 = Number(second[0]), max2 = Number(second[1])
        if (min2 <= min1 && min1 <= max2) {
            res++
        } else if (min2 <= max1 && max1 <= max2) {
            res++
        } else if (min1 <= min2 && min2 <= max1) {
            res++
        } else if (min1 <= max2 && max2 <= max1) {
            res++
        }
    }
    return res
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))