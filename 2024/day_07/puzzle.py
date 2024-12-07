def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    m = []
    for row in data.split("\n"):
        row = row.replace(":", "")
        m.append([int(x) for x in row.split(" ")])
    return m


###########
## UTILS ##
###########
def compute(cur_res, numbers, total, concat=False):
    if cur_res > total:
        return False
    if len(numbers) == 0:
        return cur_res == total
    if compute(cur_res+numbers[0], numbers[1:], total, concat):
        return True
    if compute(cur_res*numbers[0], numbers[1:], total, concat):
        return True
    if concat:
        concatNum = int(str(cur_res) + str(numbers[0]))
        return compute(concatNum, numbers[1:], total, concat)
    return False
        

###################
## Solver part 1 ##
###################
def part1(data):
    res = 0
    for l in data:
        total, numbers = l[0], l[1:]
        if compute(numbers[0], numbers[1:], total):
            res += total
    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0
    for l in data:
        total, numbers = l[0], l[1:]
        if compute(numbers[0], numbers[1:], total, True):
            res += total
    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))