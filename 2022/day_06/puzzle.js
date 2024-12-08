var fs = require('fs');
require.extensions['.txt'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

const data_test = require('./data_test_1.txt');
const data_test_2 = require('./data_test_2.txt')
const data_test_3 = require('./data_test_3.txt')
const data_test_4 = require('./data_test_4.txt')
const data_test_5 = require('./data_test_5.txt')
const puzzle_data = require('./puzzle_data.txt');

function compute_data(data) {
    return data
}

function find_subrouting(data, size) {
    let isOk = false
    let i = -1
    while (!isOk) {
        i += 1
        let sub = data.slice(i, i+size)
        let tmp = true
        for (let c=0; c<size; c++) {
            tmp = tmp && sub.split(sub[c]).length == 2
        }
        isOk = tmp
    }
    return i+size
}

function part1(data) {
    return find_subrouting(data, 4)
}

function part2(data) {
    return find_subrouting(data, 14)
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))