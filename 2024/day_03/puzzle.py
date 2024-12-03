import re

def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    return data

###################
## Solver part 1 ##
###################
def part1(data):
    res = 0

    l = re.findall(r'mul\(\d{1,3},\d{1,3}\)', data)
    for i in range(len(l)):
        [a, b] = l[i][4:-1].split(",")
        res += (int(a) * int(b))

    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0

    mapList = {}

    mulIndex = re.finditer(r'mul\(\d{1,3},\d{1,3}\)', data)
    for x in mulIndex:
        mapList[x.start()] = x.group()

    doIndex = re.finditer(r'do\(\)', data)
    for x in doIndex:
        mapList[x.start()] = "yes"

    dontIndex = re.finditer(r'don\'t\(\)', data)
    for x in dontIndex:
        mapList[x.start()] = "no"

    mapList = dict(sorted(mapList.items()))
    
    enabled = True
    for key in mapList:
        val = mapList[key]
        if val == "yes":
            enabled = True
        elif val == "no":
            enabled = False
        elif enabled:
            [a, b] = val[4:-1].split(",")
            res += (int(a) * int(b))

    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))