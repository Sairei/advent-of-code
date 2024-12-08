def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    m = {}

    lines = data.split("\n")
    maxX = len(lines)
    for x in range(len(lines)):
        row = lines[x]
        maxY = len(row)
        for y in range(len(list(row))):
            c = list(row)[y]
            if c != ".":
                old = m[c] if c in m else []
                m[c] = old
                m[c].append([x,y])
    return m, maxX, maxY


###########
## UTILS ##
###########
def find_antinode(x1, y1, x2, y2, MAX):
    distX = x2 - x1
    distY = y2 - y1
    a1 = [x1-distX, y1-distY]
    a2 = [x2+distX, y2+distY]

    ok = []
    for a in [a1, a2]:
        if in_grid(a[0], a[1], MAX[0], MAX[1]):
            ok.append(a)
    return ok

def find_all_antinode(x1, y1, x2, y2, MAX):
    distX = x2 - x1
    distY = y2 - y1
    a1 = [x1-distX, y1-distY]
    a2 = [x2+distX, y2+distY]

    ok = [[x1, y1], [x2, y2]]

    while in_grid(a1[0], a1[1], MAX[0], MAX[1]):
        ok.append(a1)
        a1 = [a1[0]-distX, a1[1]-distY]

    while in_grid(a2[0], a2[1], MAX[0], MAX[1]):
        ok.append(a2)
        a2 = [a2[0]+distX, a2[1]+distY]

    return ok

def in_grid(x, y, MAX_X, MAX_Y):
    return (0 <= x < MAX_X) and (0 <= y < MAX_Y)


###################
## Solver part 1 ##
###################
def part1(data):
    data, MAX_X, MAX_Y = data
    res = []
    for key in data:
        for i in range(len(data[key])-1):
            for j in range(i+1, len(data[key])):
                p1 = data[key][i]
                p2 = data[key][j]
                antinodes = find_antinode(p1[0], p1[1], p2[0], p2[1], [MAX_X, MAX_Y])
                for a in antinodes:
                    if not str(a) in res:
                        res.append(str(a))
    return len(res)


###################
## Solver part 2 ##
###################
def part2(data):
    data, MAX_X, MAX_Y = data
    res = []
    for key in data:
        for i in range(len(data[key])-1):
            for j in range(i+1, len(data[key])):
                p1 = data[key][i]
                p2 = data[key][j]
                antinodes = find_all_antinode(p1[0], p1[1], p2[0], p2[1], [MAX_X, MAX_Y])
                for a in antinodes:
                    if not str(a) in res:
                        res.append(str(a))
    return len(res)


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))