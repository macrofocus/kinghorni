print('Python: Simulator initializing')

count = 0


def init():
    print('Python: Simulator initialized')


def step():
    global count
    count += 1
    print('Python: Step', count)


init()
