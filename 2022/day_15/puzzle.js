var fs = require('fs');
require.extensions['.txt'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

const data_test = require('./data_test.txt');
const puzzle_data = require('./puzzle_data.txt');

function compute_data(data) {
    let map = []

    let lines = data.split("\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i].split(": ")
        let sensorCoord = line[0].slice(line[0].indexOf("x")).split(", ").map(s => Number(s.slice(2)))
        let beaconCoord = line[1].slice(line[1].indexOf("x")).split(", ").map(s => Number(s.slice(2)))
        map.push([sensorCoord, beaconCoord])
    }
    
    let minX = null
    let maxX = null
    let minY = null
    let maxY = null
    for (let elt of map) {
        for (let c of elt) {
            minX = minX==null ? c[0] : Math.min(minX, c[0])
            maxX = maxX==null ? c[0] : Math.max(maxX, c[0])
            minY = minY==null ? c[1] : Math.min(minY, c[1])
            maxY = maxY==null ? c[1] : Math.max(maxY, c[1])
        }
    }

    return [map, [minX, maxX, minY, maxY]]
}

function distance([x1, y1], [x2, y2]) {
    return Math.abs(x2 - x1) + Math.abs(y2 - y1)
}

function check(pos, map) {
    for (let couple of map) {
        if (pos[0] === couple[0][0] && pos[1] === couple[0][1]) {
            return false
        } else if (pos[0] === couple[1][0] && pos[1] === couple[1][1]) {
            return false
        }


        let sensorBeaconDist = distance(couple[0], couple[1])
        let sensorPosDist = distance(couple[0], pos)
        if (sensorBeaconDist >= sensorPosDist) {
            return true
        }
    }

    return false
}

function part1(data) {
    let lineToCheck = data[1]
    let map = data[0][0]
    let mapSize = data[0][1]

    let res = 0
    for (let x=mapSize[0]-mapSize[1]; x<=mapSize[1]*2; x++) {
        let pos = [x, lineToCheck]
        
        if (check(pos, map)) {
            res++
        }
    }

    return res
}

function part2(data) {
    let res = 0
    let limits = data[2]
    let map = data[0][0]
    
    for (let l=limits[0]; l<=limits[1]; l++) {
        let range = []
        for (let elt of map) {
            let radius = distance(elt[0], elt[1])
            let dist = Math.abs(elt[0][1] - l)
            if (dist <= radius) {
                let w = radius - dist
                range.push([elt[0][0]-w, elt[0][0]+w]);
            }
        }

        range.sort((a,b) => a[0] - b[0])
        let mergedRange = []
        let prev = range[0];

        for (let i = 0; i < range.length; i++) {
            let curr = range[i];
            if (prev[1] >= curr[0] - 1) {
                prev = [prev[0], Math.max(prev[1], curr[1])];
            } else {
                mergedRange.push(prev);
                prev = curr;
            }
        }
        mergedRange.push(prev)

        if (mergedRange.length > 1) {
            res = (mergedRange[0][1]+1) * 4000000 + l
        }
    }

    return res
}


let data = [compute_data(puzzle_data), 2000000, [0, 4000000]]
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))