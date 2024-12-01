def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    data = data.split("\n")

    list1 = []
    list2 = []
    for i in range(len(data)):
        [col1, col2] = data[i].split("   ")
        list1.append(int(col1))
        list2.append(int(col2))
        # print(data[i])
    
    return [list1, list2]


###################
## Solver part 1 ##
###################
def part1(data):
    res = 0

    list1 = data[0].copy()
    list1.sort()
    list2 = data[1].copy()
    list2.sort()
    
    for i in range(len(list1)):
        res += abs(list2[i] - list1[i])

    return res


###################
## Solver part 2 ##
###################
def part2(data):
    res = 0

    memory = {}
    list1 = data[0].copy()
    list2 = data[1].copy()

    for i in range(len(list1)):
        if not(list1[i] in memory):
            count = list2.count(list1[i])
            memory[str(list1[i])] = count

        res += (list1[i] * memory[str(list1[i])])


    return res


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))