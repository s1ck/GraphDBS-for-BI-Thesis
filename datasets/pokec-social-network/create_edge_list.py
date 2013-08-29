#!/usr/bin/env python

'''
Pokec relationships are stored in the format

node_id_A   node_id_B

So it's one edge per line. This script transforms it into an dictionary
where node_id_A is the key, and an array of all associated node_id_B's is the
value. The dictionary is then stored using pickle for later use.

'''

import cPickle as pickle
import sys

def build_edge_list(edge_file_in):
    ''' iterates through the file and builds an edge list dictionary in the form 
    from_id : [to_ids]'''
    print 'building edge list'
    edge_list = {}
    items = []
    line_cnt = 0
    n = float(30622564)
    with open(edge_file_in, 'r') as f_edges:
        for line in f_edges:
            items = [int(a) for a in line.split('\t')]
            if items[0] in edge_list:
                edge_list[items[0]].append(items[1])
            else:
                edge_list[items[0]] = [items[1]]
            line_cnt += 1

            if line_cnt % 1000 == 0:
                sys.stdout.write('\r%2.2f%%' % (line_cnt / n * 100))
                sys.stdout.flush()
    return edge_list


if __name__ == '__main__':
    if len(sys.argv) == 3:
        edge_file_in = sys.argv[1]
        edge_file_out = sys.argv[2]
    else:
        sys.exit('Usage: python create_edge_list.py <in-file> <out-file>')

    edge_list = build_edge_list(edge_file_in)
    pickle.dump(edge_list, open(edge_file_out, 'wb'))
