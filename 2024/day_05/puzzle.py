def read_data(filename):
    return open(f"{filename}").read().strip()

###############################
## Construct data for solver ##
###############################
def constructData(data):
    rule_lists, page_lists = data.split("\n\n")
    rules = [ list(r.split('|')) for r in rule_lists.splitlines() ]
    rules = [ list(map(int, r)) for r in rules ]
    updates = [ list(r.split(',')) for r in page_lists.splitlines() ]
    updates = [ list(map(int, p)) for p in updates ]

    return [rules, updates]


###########
## UTILS ##
###########
def is_ok(rules, listOfPages):
    for rule in rules:
        if not set(rule).issubset(set(listOfPages)): 
            continue
            
        left, right = rule
        left_i = listOfPages.index(left)
        right_i = listOfPages.index(right)
        
        if left_i > right_i:
            return False
    
    return True


def sorted_list_of_page(rules, listOfPages):
    listOrdered = listOfPages.copy()
    
    is_update = True
    while is_update:
        is_update = False
        for rule in rules:
            if not set(rule).issubset(set(listOrdered)): 
                continue
                
            left, right = rule
            left_i = listOrdered.index(left)
            right_i = listOrdered.index(right)
            
            if left_i > right_i:
                listOrdered[left_i], listOrdered[right_i] = listOrdered[right_i], listOrdered[left_i]
                is_update = True
    
    return listOrdered


###################
## Solver part 1 ##
###################
def part1(data):
    rules = data[0]
    listToUpdate = data[1]
    
    res = []
    for l in listToUpdate:
        if is_ok(rules, l):
            mid = l[len(l)//2]
            res.append(mid)
    
    return sum(res)


###################
## Solver part 2 ##
###################
def part2(data):
    rules = data[0]
    listToUpdate = data[1]
    
    unorder = []
    for l in listToUpdate:
        if not is_ok(rules, l):
            unorder.append(l)
    
    res = []
    for l in unorder:
        mid = sorted_list_of_page(rules, l)[len(l)//2]
        res.append(mid)
    return sum(res)


##########
## MAIN ##
##########
data = read_data("puzzle_data.txt")
print('Result for puzzle 1 = ', part1(constructData(data)))
print('Result for puzzle 2 = ', part2(constructData(data)))