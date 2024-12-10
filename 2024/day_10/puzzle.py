def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    m = []
    for row in data.split("\n"):
        m.append(list(row))
    return m


###########
## UTILS ##
###########
DIRECTIONS = {
    "U": [-1, 0],
    "R": [0, 1],
    "D": [1, 0],
    "L": [0, -1]
}

def is_in_grid(m, x, y):
    return (0 <= x < len(m)) and (0 <= y < len(m[0]))

def find_possibles_moves(m, x, y, step):
    res = []
    for key in DIRECTIONS:
        newX = x+DIRECTIONS[key][0]
        newY = y+DIRECTIONS[key][1]
        if is_in_grid(m, newX, newY) and int(m[newX][newY]) == step+1:
            res.append([newX,newY])
    return res

def find_nb_highest(m, x, y):
    step = int(m[x][y])

    high = []
    moves = find_possibles_moves(m, x, y, step)
    if step == 9:
        high = ["[" + str(x) + "," + str(y) + "]"]
    else:
        for move in moves:
            for find in find_nb_highest(m, move[0], move[1]):
                high.append(find)
    return high


###################
## Solver part 1 ##
###################
def part1(data):
    res = 0
    for x in range(len(data)):
        for y in range(len(data[x])):
            if data[x][y] == '0':
                highestList = find_nb_highest(data, x, y)
                res += len(set(list(highestList)))
    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0
    for x in range(len(data)):
        for y in range(len(data[x])):
            if data[x][y] == '0':
                res += len(find_nb_highest(data, x, y))
    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))