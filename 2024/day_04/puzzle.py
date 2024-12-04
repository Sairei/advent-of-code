def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    lines = data.split("\n")
    res = []
    for line in lines:
        res.append(list(line))
    return res


###########
## UTILS ##
###########
DIRECTIONS = {
    "U": [-1, 0],
    "UR": [-1, 1],
    "R": [0, 1],
    "DR": [1, 1],
    "D": [1, 0],
    "DL": [1, -1],
    "L": [0, -1],
    "UL": [-1, -1]
}

WORD = "XMAS"

## Function to find all possible positions ##
def possible_direction(pos, maxX, maxY):
    dirs = []
    for key in DIRECTIONS:
        xMove = pos[0] + DIRECTIONS[key][0]*3
        yMove = pos[1] + DIRECTIONS[key][1]*3
        if (xMove >= 0) and (xMove < maxX) and (yMove >= 0) and (yMove < maxY):
            dirs.append(key)
    return dirs

## Function to check if XMAS is writen in the direction ##
def find_xmas_by_direction(data, pos, d):
    for m in range(1, 4):
        xTest = pos[0] + DIRECTIONS[d][0]*m
        yTest = pos[1] + DIRECTIONS[d][1]*m
        if data[xTest][yTest] != WORD[m]:
            return False
    return True

## function to check if MAS is on position ##
def is_mas(data, p1, p3):
    if p1[0] < 0 or p1[0] >= len(data):
        return False
    if p1[1] < 0 or p1[1] >= len(data[0]):
        return False
    if p3[0] < 0 or p3[0] >= len(data):
        return False
    if p3[1] < 0 or p3[1] >= len(data[0]):
        return False

    return (data[p1[0]][p1[1]] == "M" and data[p3[0]][p3[1]] == "S") or (data[p1[0]][p1[1]] == "S" and data[p3[0]][p3[1]] == "M")

## function to check the x-mas at the position of "A" ##
def find_x_mas(data, pos):
    posUL = [pos[0]+DIRECTIONS["UL"][0], pos[1]+DIRECTIONS["UL"][1]]
    posUR = [pos[0]+DIRECTIONS["UR"][0], pos[1]+DIRECTIONS["UR"][1]]
    posDL = [pos[0]+DIRECTIONS["DL"][0], pos[1]+DIRECTIONS["DL"][1]]
    posDR = [pos[0]+DIRECTIONS["DR"][0], pos[1]+DIRECTIONS["DR"][1]]

    if is_mas(data, posUL, posDR) and is_mas(data, posUR, posDL):
        return True

    return False


###################
## Solver part 1 ##
###################
def part1(data):
    res = 0
    for x in range(len(data)):
        for y in range(len(data[0])):
            if data[x][y] == 'X':
                dirs = possible_direction([x,y], len(data), len(data[0]))
                for d in dirs:
                    if find_xmas_by_direction(data, [x,y], d):
                        res += 1

    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0
    for x in range(len(data)):
        for y in range(len(data[0])):
            if data[x][y] == 'A':
                if find_x_mas(data, [x,y]):
                    res += 1

    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))