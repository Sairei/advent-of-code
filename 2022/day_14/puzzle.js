const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

function compute_data(data) {
    let map = []
    
    let minX = null
    let maxX = null
    let maxY = null

    let lines = data.split("\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i].split(" -> ")
        line.forEach(e => {
            let tmp = e.split(",")
            minX = minX ? Math.min(minX, Number(tmp[0])) : Number(tmp[0])
            maxX = Math.max(maxX, Number(tmp[0]))
            maxY = Math.max(maxY, Number(tmp[1]))
        });
        map.push(line)
    }
    map = draw_line(map, minX, maxX, maxY)

    return [map, [minX, maxX, maxY]]
}

function draw_line(lines, minX, maxX, maxY) {
    let map = []
    for (let i=0; i<=maxY+1; i++) {
        map.push(Array.from(".".repeat(maxX - minX + 3)))
    }
    
    for (let line of lines) {
        for (let p=0; p<line.length-1; p++) {
            let point1 = line[p].split(",").map(e => Number(e))
            let point2 = line[p+1].split(",").map(e => Number(e))

            if (point1[0] != point2[0]) {
                let xInf = Math.min(point1[0], point2[0])
                let xSup = Math.max(point1[0], point2[0])
                let y = point1[1]
                for (let x=xInf; x<=xSup; x++) {
                    map[y][x-minX+1] = "#"
                }
            } else if (point1[1] != point2[1]) {
                let yInf = Math.min(point1[1], point2[1])
                let ySup = Math.max(point1[1], point2[1])
                let x = point1[0]
                for (let y=yInf; y<=ySup; y++) {
                    map[y][x-minX+1] = "#"
                }
            }
        }
    }
    return map
}

function extend(data, minX) {
    let map = []
    for (let i=0; i<data.length; i++) {
        let tmp = Array.from(".".repeat(minX)).concat(data[i])
        tmp = tmp.concat(Array.from(".".repeat(minX)))
        map.push(tmp)
    }
    map.push(Array.from("#".repeat(map[0].length)))
    return map
}

function next_pos_sand(map, xSand, ySand) {
    if (map[ySand+1][xSand] === ".") {
        return [xSand, ySand+1]
    } else if (map[ySand+1][xSand-1] == ".") {
        return [xSand-1, ySand+1]
    } else if (map[ySand+1][xSand+1] == ".") {
        return [xSand+1, ySand+1]
    } else {
        return [xSand, ySand]
    }
}

function part1(data) {
    let minX = data[1][0]
    let maxY = data[1][2]

    let queue = []
    queue.push([500, 0])

    let nbSandStop = 0
    let isInVoid = false
    while (queue.length && !isInVoid) {
        let isFalling = true
        
        while (isFalling && !isInVoid) {
            let sandPos = queue.pop()
            let next = next_pos_sand(data[0], sandPos[0]-minX+1, sandPos[1])

            if (next[1] > maxY || next[0] === 0 || next[0] === data[0].length) {
                isInVoid = true
            } else if (next[1] === sandPos[1] && next[0] === sandPos[0]-minX+1) {
                isFalling = false
                data[0][sandPos[1]][sandPos[0]-minX+1] = "o"
                nbSandStop++
            } else {
                queue.push(sandPos)
                queue.push([next[0]+minX-1, next[1]])
            }
        }
    }

    return nbSandStop
}

function part2(data) {
    let minX = data[1][0]
    let maxY = data[1][2]

    let map = extend(data[0], minX-1)

    let queue = []
    queue.push([500, 0])

    let nbSandStop = 0
    while (queue.length) {
        let isFalling = true
        
        while (isFalling) {
            let sandPos = queue.pop()
            let next = next_pos_sand(map, sandPos[0], sandPos[1])

            if (next[1] === sandPos[1] && next[0] === sandPos[0]) {
                isFalling = false
                map[sandPos[1]][sandPos[0]] = "o"
                nbSandStop++
            } else {
                queue.push(sandPos)
                queue.push([next[0], next[1]])
            }
        }
    }

    return nbSandStop
}


let data = compute_data(puzzle_data)
let resPart1 = part1(data)
console.log('Result for puzzle 1 = ', resPart1)
console.log('Result for puzzle 2 = ', part2(data) + resPart1)