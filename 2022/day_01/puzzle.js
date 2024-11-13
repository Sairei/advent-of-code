const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

function compute_data(data) {
    var res = []
    var elfBag = []

    var split = data.split("\n");
    for (var i=0; i<split.length; i++) {
        let s = split[i]
        if (s === "") {
            res.push(elfBag)
            elfBag = []
        } else {
            elfBag.push(s)
        }
    }
    res.push(elfBag)
    return res;
}

function part1(data) {
    var sumBag = []
    for (var i=0; i<data.length; i++) {
        let bag = data[i]

        var sum = 0
        for (var j=0; j<bag.length; j++) {
            sum += Number(bag[j])
        }
        sumBag.push(sum)
    }
    return Math.max(...sumBag)
}

function part2(data) {
    var sumBag = []
    for (var i=0; i<data.length; i++) {
        let bag = data[i]

        var sum = 0
        for (var j=0; j<bag.length; j++) {
            sum += Number(bag[j])
        }
        sumBag.push(sum)
    }
    sumBag.sort(function(a,b) { return a - b; })

    return sumBag.slice(-3).reduce((partialSum, a) => partialSum + a, 0)
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))