# Copyright (c) 2017 Capital One Financial Corporation All Rights Reserved.
#
# This software contains valuable trade secrets and proprietary information of
# Capital One and is protected by law. It may not be copied or distributed in
# any form or medium, disclosed to third parties, reverse engineered or used in
# any manner without prior written authorization from Capital One.

#Author: Ali Bhagat
#This script helps to build a simple stopwatch application using Python's time module.

import time

print('Press ENTER to begin, Press Ctrl + C to stop')
while True:
    try:
        input() # ---
        starttime = time.time()
        print('Started')
    except KeyboardInterrupt:
        print('Stopped')
        endtime = time.time()
        print('Total Time')
        break

# Define ----
def again():

    #---
    calc_again = input('''
                        ---
                        ---
                        ''')

    #---
    if calc_again == 'Y':
        calculate() #---

    #---
    elif calc_again == 'N':
        print('See you later.')

    #---
    else:
        again()


def calculate():
    # TODO: ---

def input():
    # TODO: ---
    return 'Dummy'

def realpath(path):
    '''
    ---
    ---
    '''
    # TODO: ---
    # ---
    return os.path.normpath(normcase(os.path.realpath(path)))