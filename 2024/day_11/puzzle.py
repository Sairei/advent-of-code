from functools import cache

def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    m = []
    for num in data.split(" "):
        m.append(int(num))
    return m


###########
## UTILS ##
###########
def blink(num):
    res = []
    s = f'{num}'
    l = len(s)
    if num == 0:
        res.append(1)
    elif l%2 == 0:
        res.append(int(s[0:l//2]))
        res.append(int(s[l//2:]))
    else:
        res.append(num * 2024)
    return res

@cache
def count(num, nb):
    if nb == 0:
        return 1
    return sum(count(n, nb - 1) for n in blink(num))


###################
## Solver part 1 ##
###################
def part1(data):
    res = sum(count(num, 25) for num in data)
    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = sum(count(num, 75) for num in data)
    return res


##########
## MAIN ##
##########
# data = read_data("data_test.txt")
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))