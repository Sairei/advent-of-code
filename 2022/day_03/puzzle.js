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
        res.push([s, s.slice(0, s.length/2), s.slice(s.length/2, s.length)])
    }
    return res;
}

function convert_char_to_int(c) {
    if (c == c.toLowerCase()) {
        return c.charCodeAt(0) - 96
    } else {
        return c.charCodeAt(0) - 38
    }
}

function part1(data) {
    let res = 0
    for (let i=0; i<data.length; i++) {
        let compartment1 = data[i][1]
        let compartment2 = data[i][2]

        for (let c=0; c<compartment1.length; c++) {
            if (compartment2.includes(compartment1.charAt(c))) {
                res += convert_char_to_int(compartment1.charAt(c))
                break
            }
        }
    }
    return res
}

function part2(data) {
    let res = 0
    for (let i=0; i<data.length; i+=3) {
        let line1 = data[i][0]
        let line2 = data[i+1][0]
        let line3 = data[i+2][0]

        for (let c=0; c<line1.length; c++) {
            let caracter = line1.charAt(c)
            if (line2.includes(caracter) && line3.includes(caracter)) {
                res += convert_char_to_int(caracter)
                break
            }
        }
    }
    return res
}


// let data = compute_data(data_test)
let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))