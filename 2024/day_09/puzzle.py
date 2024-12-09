def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    used = []
    free = []
    p = 0
    for i, ch in enumerate(data):
        if i % 2 == 0:
            used += [[*range(p,p:=p+int(ch))]]
        else:
            free += [[*range(p,p:=p+int(ch))]]
    return used, free


###################
## Solver part 1 ##
###################
def part1(data):
    used, free = data
    free = sum(free, [])
    for u in reversed(used):
        for x in reversed(range(len(u))):
            if len(free) and u[x] > free[0]:
                u[x] = free[0]
                free = free[1:]
    
    res = 0
    for i,f in enumerate(used):
        for j in f:
            res += i*j
    return res


###################
## Solver part 2 ##
###################
def part2(data):
    used, free = data
    for y in reversed(range(len(used))):
        for x in range(len(free)):
            if len(free[x]) >= len(used[y]) and used[y][0] > free[x][0]:
                used[y] = free[x][:len(used[y])]
                free[x] = free[x][len(used[y]):]
    
    res = 0
    for i,f in enumerate(used):
        for j in f:
            res += i*j
    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))