#Dean Kopping
#KPPDEA001

import random
import sys

def main():
    if len(sys.argv) != 2:
        print('Usage: python paging.py [number of page frames]')
    else:
        size = int(sys.argv[1])
        pages = GeneratePages(random.randint(0,100))
        print(pages)
        print('FIFO', FIFO(size,pages), 'page faults.')
        print('LRU', LRU(size,pages), 'page faults.')
        print('OPT', OPT(size,pages), 'page faults.')

def GeneratePages(length):
    pages = []
    for i in range(length):
        pages.append(random.randint(0, 9))
    return pages

def FIFO(size, pages):
    frame = []
    faults = 0
    for page in pages:
        if page not in frame:
            if len(frame) < size:
                frame.append(page)
            else:
                #removes the first page and adds the new page
                frame.pop(0)
                frame.append(page)
            faults += 1
    return faults

def LRU(size, pages):
    frame = []
    faults = 0
    for page in pages:
        if page not in frame:
            if len(frame) < size:
                frame.append(page)
                faults=faults+1
            else:
                frame.pop(0)
                frame.append(page)
                faults+=1
        else:
            #move page from current position to end of page list as it is most recently used
            index = frame.index(page)
            frame.pop(index)
            frame.append(page)
    return faults

def OPT(size, pages):
    frame = []
    faults = 0
    for page in pages:
        if page not in frame:
            if len(frame) < size:
                frame.append(page)
            else:
                max = -1
                for i in frame:
                    if i in pages[pages.index(page):]:
                        index = pages.index(i)
                        if index > max:
                            max = index
                            victim = frame.index(i)
                    else:
                        victim = frame.index(i)
                        break
                frame[victim] = page
            faults += 1
        else:
            index = frame.index(page)
            frame.pop(index)
            frame.append(page)
    return faults


if __name__ == "__main__":
    main()