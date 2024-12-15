import copy
def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    m = []
    for line in data.split("\n"):
        m.append(list(line))
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
    return (0 <= x < len(m)) and (0 <= y < len(m[x]))

def count_part(cpMap, x, y, plant, part1):
    if part1:
        return count_edge(cpMap, x, y, plant)
    else:
        return count_side(cpMap, x, y, plant)

def count_edge(cpMap, x, y, plant):
    edge = 0
    for d in DIRECTIONS:
        newX, newY = x+DIRECTIONS[d][0], y+DIRECTIONS[d][1]
        if not is_in_grid(cpMap, newX, newY) or cpMap[newX][newY] != plant:
            edge += 1
    return edge

def count_side(cpMap, x, y, plant):
    side = 0
    for d in DIRECTIONS:
        prevDir = list(DIRECTIONS.keys())[(list(DIRECTIONS.keys()).index(d) - 1) % 4]
        newX, newY = x+DIRECTIONS[d][0], y+DIRECTIONS[d][1]
        if not is_in_grid(cpMap, newX, newY) or cpMap[newX][newY] != plant:
            newX_90CC, newY_90CC = x+DIRECTIONS[prevDir][0], y+DIRECTIONS[prevDir][1]
            isBeginEdge = not is_in_grid(cpMap, newX_90CC, newY_90CC) or cpMap[newX_90CC][newY_90CC] != plant

            newX_Corner, newY_Corner = newX+DIRECTIONS[prevDir][0], newY+DIRECTIONS[prevDir][1]
            isConcaveBeginEdge = is_in_grid(cpMap, newX_Corner, newY_Corner) and cpMap[newX_Corner][newY_Corner] == plant

            if isBeginEdge or isConcaveBeginEdge:
                side += 1
    return side

def compute_price(m, startX, startY, cpMap, part1):
    plant = m[startX][startY]
    queue = [(startX, startY)]
    
    area = 1
    p = count_part(cpMap, startX, startY, plant, part1)
    
    m[startX][startY] = '.'
    while queue:
        x, y = queue.pop(0)
        for d in DIRECTIONS:
            newX, newY = x+DIRECTIONS[d][0], y+DIRECTIONS[d][1]
            if is_in_grid(cpMap, newX, newY) and m[newX][newY] == plant:
                area += 1
                p += count_part(cpMap, newX, newY, plant, part1)
                m[newX][newY] = '.'
                queue.append((newX, newY))
    
    return area, p


###################
## Solver part 1 ##
###################
def part1(data):
    res = 0
    cpMap = copy.deepcopy(data)
    for i in range(len(data)):
        for j in range(len(data[i])):
            if data[i][j] != '.':
                a, p = compute_price(data, i, j, cpMap, True)
                res += a*p
    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0
    cpMap = copy.deepcopy(data)
    for i in range(len(data)):
        for j in range(len(data[i])):
            if data[i][j] != '.':
                a, p = compute_price(data, i, j, cpMap, False)
                res += a*p
    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))