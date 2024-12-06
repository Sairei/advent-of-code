def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    m = []
    startX, startY = 0, 0
    for row in data.split("\n"):
        m.append(list(row))
        if "^" in row:
            startX = data.split("\n").index(row)
            startY = list(row).index("^")
    return [m, PosWithDirection(startX, startY, "U")]


###########
## CLASS ##
###########
DIRECTION = {
    "U": [-1, 0],
    "R": [0, 1],
    "D": [1, 0],
    "L": [0, -1]
}

class PosWithDirection:
    def __init__(self, x, y, d):
        self.x = x
        self.y = y
        self.direction = d

    def __str__(self):
        return "[" + str(self.x) + "," + str(self.y) + "]"

    def move_forward(self):
        self.x += DIRECTION[self.direction][0]
        self.y += DIRECTION[self.direction][1]

    def turn_right(self):
        indexDir = list(DIRECTION).index(self.direction)
        self.direction = list(DIRECTION)[(indexDir+1) % 4]

    def copy(self):
        return PosWithDirection(self.x, self.y, self.direction)


###########
## UTILS ##
###########
def is_in_grid(m, pos):
    return (0 <= pos.x < len(m)) and (0 <= pos.y < len(m[0]))

###################
## Solver part 1 ##
###################
def part1(data):
    mapTile = data[0]
    startPos = data[1]

    res = []
    p = startPos.copy()
    while is_in_grid(mapTile, p):
        if not p.__str__() in res:
            res.append(p.__str__())
        newP = p.copy()
        newP.move_forward()
        if is_in_grid(mapTile, newP) and mapTile[newP.x][newP.y] == "#":
            p.turn_right()
        p.move_forward()
    
    return len(res)


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0
    return res


##########
## MAIN ##
##########
# data = read_data("data_test.txt")
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
# print('Result for puzzle 2 = ', part2(constructData(data)))