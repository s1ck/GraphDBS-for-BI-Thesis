#!/usr/bin/env python

'''
This script is used to select a sample of size N from a given file. It uses the
Reservoir Sampling algorithm
(http://en.wikipedia.org/wiki/Reservoir_sampling)
'''

import sys
import random

if __name__ == '__main__':
    if len(sys.argv) == 3:
        N = int(sys.argv[1])
        in_f = open(sys.argv[2], 'r')
    else:
        sys.exit('Usage: create_node_list.py <sample-size> <node-file>')

    sample = []

    for i, line in enumerate(in_f):
        if i < N:
            sample.append(line)
        elif i >= N and random.random() < N/float(i+1):
            replace = random.randint(0, len(sample) - 1)
            sample[replace] = line

    for line in sample:
        sys.stdout.write(line)
