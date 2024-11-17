const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

function compute_data(data) {
    let map = []
    let lines = data.split("\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i]
        let row = []
        for (let j=0; j<line.length; j++) {
            row.push(line[j])
        }
        map.push(row)
    }
    return map
}

function is_visible(x, y, map) {
    let upOk = true;
    let downOk = true;
    let leftOk = true;
    let rightOk = true;

    for (let i=0; i < x; i++) {
        if (map[i][y] >= map[x][y]) {
            upOk = false
            break
        }
    }
    
    for (let i=map.length-1; i > x; i--) {
        if (map[i][y] >= map[x][y]) {
            downOk = false
            break
        }
    }
    
    for (let i=0; i < y; i++) {
        if (map[x][i] >= map[x][y]) {
            rightOk = false
            break
        }
    }
    
    for (let i=map[x].length-1; i > y; i--) {
        if (map[x][i] >= map[x][y]) {
            leftOk = false
            break
        }
    }

    return upOk || downOk || leftOk || rightOk
}

function get_scenic_score(x, y, map) {
    let upOk = 0;
    let downOk = 0;
    let leftOk = 0;
    let rightOk = 0;

    for (let i=x+1; i < map.length; i++) {
        downOk++
        if (map[i][y] >= map[x][y]) {
            break
        }
    }
    
    for (let i=x-1; i >= 0; i--) {
        upOk++
        if (map[i][y] >= map[x][y]) {
            break
        }
    }
    
    for (let i=y+1; i < map[x].length; i++) {
        leftOk++
        if (map[x][i] >= map[x][y]) {
            break
        }
    }
    
    for (let i=y-1; i >= 0; i--) {
        rightOk++
        if (map[x][i] >= map[x][y]) {
            break
        }
    }

    return upOk * downOk * leftOk * rightOk
}

function part1(data) {
    let res = (data.length-1) * 4
    for (let x=1; x<data.length-1; x++) {
        for (let y=1; y<data[x].length-1; y++) {
            if (is_visible(x, y, data)) {
                res += 1
            }
        }
    }
    return res
}

function part2(data) {
    let res = 0
    for (let x=0; x<data.length; x++) {
        for (let y=0; y<data[x].length; y++) {
            res = Math.max(res, get_scenic_score(x, y, data))
        }
    }
    return res
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))