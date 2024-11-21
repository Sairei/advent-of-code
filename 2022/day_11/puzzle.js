const { data_test } = require('./data_test.js');
const { puzzle_data } = require('./puzzle_data.js');

function compute_data(data) {
    let map = new Map()
    let lines = data.split("\n\n")
    for (let i=0; i<lines.length; i++) {
        let line = lines[i]
        let monkey = line.split(":\n")
        let number = monkey[0].split(" ")[1]
        
        let tmp = monkey[1].split("\n")
        let items = tmp[0].split(": ")[1].split(", ")
        let op = tmp[1].split(" = ")[1]
        let testValue = Number(tmp[2].split(" ")[tmp[2].split(" ").length-1])
        let isTrue = tmp[3].split(" ")[tmp[3].split(" ").length-1]
        let isFalse = tmp[4].split(" ")[tmp[4].split(" ").length-1]
        map.set(number, [items, op, testValue, [isTrue, isFalse]])
    }
    return map
}

function computation(a, sign, b) {
    switch (sign) {
        case '-':
            return a - b
        case '+':
            return a + b
        case '*':
            return a * b
        case '/':
            return a / b
        default:
            console.log(`Wrong opération`);
    }
}

function play_with_item(item, monkeyProps) {
    // play
    let operation = monkeyProps[1].split(" ")
    let op1 = operation[0] == "old" ? item : Number(operation[0])
    let op2 = operation[2] == "old" ? item : Number(operation[2])
    let c = computation(op1, operation[1], op2)

    // get borred
    return Math.floor(c / 3)
}

function test_worred_item(item, testValue) {
    // test
    return (item % testValue) == 0
}

function part1(data) {
    let itemsUse = []
    for (let i=0; i<data.size; i++) {
        itemsUse.push(0)
    }

    for (let cycle=0; cycle<20; cycle++) {
        data.forEach((value, key, map) => {
            while (value[0].length != 0) {
                itemsUse[Number(key)] += 1

                let item = Number(value[0].shift())

                let newItem = play_with_item(item, value);
                if (test_worred_item(newItem, value[2])) {
                    map.get(value[3][0])[0].push(newItem.toString())
                } else {
                    map.get(value[3][1])[0].push(newItem.toString())
                }
            }
        })
    }

    itemsUse = itemsUse.sort((a, b) => b - a)
    return (itemsUse[0] * itemsUse[1])
}

function part2(data) {
    return 0
}


// let data = compute_data(data_test)
let data = compute_data(puzzle_data)
console.log('Result for puzzle 1 = ', part1(data))
// console.log('Result for puzzle 2 = ', part1(data))