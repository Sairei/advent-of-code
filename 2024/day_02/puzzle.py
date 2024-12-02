def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    data = data.split("\n")

    map = []
    for i in range(len(data)):
        line = [int(x) for x in data[i].split(" ")] 
        map.append(line)
        
    return map


###########
## Utils ##
###########
def isSafe(line):
    safe = line[0] != line[1]
    if not(safe):
        return 1
    
    checkType = "ASC" if line[0] < line[1] else "DESC"
    
    for i in range(1, len(line)):
        if checkType == "ASC":
            safe = safe & ((line[i] - line[i-1]) > 0) & ((line[i] - line[i-1]) < 4)
        else:
            safe = safe & ((line[i-1] - line[i]) > 0) & ((line[i-1] - line[i]) < 4)

        if not(safe):
            return i

    return 0

###################
## Solver part 1 ##
###################
def part1(data):
    res = 0

    for l in range(len(data)):
        res += 1 if isSafe(data[l]) == 0 else 0

    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0

    for l in range(len(data)):
        if isSafe(data[l]) == 0:
            res += 1
        else:
            safe = False
            for i in range(len(data[l])):
                cp = data[l].copy()
                cp.pop(i)
                safe = safe or isSafe(cp)==0
            if safe:
                res += 1
    
    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))