const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

function compute_data(data) {
    let tmpData = data.split("\n")
    let res = new Map()
    res.set("/", [])

    let pwd = []
    for (let i in tmpData) {
        let line = tmpData[i]
        let split = line.split(" ")
        if (is_command_line(line)) {
            children = []
            if (split[1] == "cd") {
                pwd = move(pwd, split[2])
            }
        } else {
            if (split[0] != "dir") {
                res.set(cmd_pwd(pwd, split[1]), Number(split[0]))
            } else {
                res.set(cmd_pwd(pwd, split[1]), [])
            }
            res.get(get_pwd_parent(pwd)).push(split[1])
        }
    }
    return res
}

function move(pwd, to) {
    if (to == "..") {
        pwd.pop()
    } else {
        pwd.push(to)
    }
    return pwd
}

function is_command_line(line) {
    return line.startsWith('$')
}

function cmd_pwd(pwd, elt) {
    let command = "/"
    for (let i=1; i<pwd.length; i++) {
        command += pwd[i] + "/"
    }
    return command + elt
}

function get_pwd_parent(pwd) {
    return pwd.length == 1 ? "/" : cmd_pwd(pwd, '').slice(0, -1)
}

function compute_directory_size(d, map) {
    let size = 0
    let children = map.get(d)
    d = d == "/" ? "" : d
    for (let i=0; i<children.length; i++) {
        let child = d + "/" + children[i]
        if (map.get(child) instanceof Array) {
            size += compute_directory_size(child, map)
        } else {
            size += map.get(child)
        }
    }
    return size
}

function part1(data) {
    let dirSize = []
    data.forEach((value, key, map) => {
        if (value instanceof Array) {
            let size = compute_directory_size(key, map)
            dirSize.push(size)
        }
    });

    dirSize = dirSize.filter(v => v <= 100000)

    let res = 0
    for (let i=0; i<dirSize.length; i++) {
        res += dirSize[i]
    }
    return res
}

function part2(data) {
    let dirSize = []
    data.forEach((value, key, map) => {
        if (value instanceof Array) {
            let size = compute_directory_size(key, map)
            dirSize.push(size)
        }
    });

    let totalSize = 70000000
    let sizeNeed = 30000000
    let usedSize = compute_directory_size('/', data)
    dirSize = dirSize.filter(v => v >= sizeNeed - (totalSize - usedSize)).sort((a, b) => a-b)
    
    return dirSize[0]
}


let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
console.log('Result for puzzle 2 = ', part2(data))