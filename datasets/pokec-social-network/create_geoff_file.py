#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
This script transforms a set of Pokec nodes and relationships into the GEOFF
format. Additionaly, it maps given Amazon User-ids to pokec users, so that
these two networks can be merged. There is _no_ relation between a user's
network in Pokec and what that user is doing in the Amazon network.
'''

import json
import sys
import cPickle as pickle

ID_KEY = '__id__'
TYPE_KEY = '__type__'
TYPE_USER = 'u'


def load_nodes(node_list_in, node_map):
    ''' Loads the nodes and some properties from the file, maps the amazon user
    id to the pokec user

    properties are:
        - id
        - gender (optional)
        - age (optional)
        - region (optional)
        - eye color (optional)
    '''
    print 'loading nodes'
    nodes = {}
    i = 0
    with open(node_list_in, 'r') as f_in:
        for line in f_in:
            items = line.split('\t')
            # node_map[i] is the amazon user id
            nodes[int(items[0])] = [node_map[i].strip(), items[1], items[4],
                    items[7], items[16]]
            i += 1

    return nodes

def load_edges(edge_list_in):
    ''' loads the dictionary {node-id : [node-ids]} from the pickle file'''
    print 'loading edges'
    with open(edge_list_in, 'rb') as f_in:
        return pickle.load(f_in)

def store_graph(nodes, edges, node_list_out, edge_list_out):
    ''' iterates all nodes and stores there properties in GEOFF format. After
    that the relationships to all other nodes which are stores. The end node of
    an edge must be present in the nodes dictionary. Any link points to an
    existing node!'''
    with open(node_list_out, 'w') as f_n_out, \
            open(edge_list_out, 'w') as f_e_out:
        node_cnt = 0
        n = 1555124
        for i, info in nodes.iteritems():
            # store node info
            data = {TYPE_KEY: TYPE_USER, ID_KEY: info[0]}
            if info[1] != 'null':
                data['gender'] = info[1]
            if info[2] != 'null' and len(info[2]) > 0:
                data['region'] = info[2]
            if info[3].isdigit() and int(info[3]) > 0:
                data['age'] = int(info[3])
            if info[4] != 'null' and len(info[4]) > 0:
                data['eye_color'] = info[4]
            # store node identified by amazon user id
            save_geoff_node(f_n_out, info[0], data if len(data) > 0 else None)
            node_cnt += 1
            # store edges
            if i in edges:
                for j in edges[i]:
                    if j in nodes:
                        # if the end-node exists, store an edge using the
                        # amazon ids of start and end node
                        save_geoff_edge(f_e_out, info[0], nodes[j][0],
                                'FRIEND_OF')
            if node_cnt % 1000 == 0:
                sys.stdout.write('\r%2.2f%%' % (node_cnt * 100.0 / n))
                sys.stdout.flush()
                f_n_out.flush()
                f_e_out.flush()


def save_geoff_node(f_nodes, descriptor, data=None):
    ''' stores a node in GEOFF format'''
    string = '(%s)' % descriptor
    if data:
        string = '%s %s' % (string, json.dumps(data))
    f_nodes.write(string + '\n')

def save_geoff_edge(f_edges, from_descriptor, to_descriptor, rel_type,
        data=None):
    ''' stores an edge in GEOFF format'''
    string = '(%s)-[:%s]->(%s)' % (from_descriptor, rel_type.upper(),
            to_descriptor)
    if data:
        string = '%s %s' % (string, json.dumps(data))
    f_edges.write(string + '\n')

if __name__ == '__main__':
    if len(sys.argv) == 6:
        node_list_in = sys.argv[1]
        edge_list_in = sys.argv[2]
        node_list_out = sys.argv[3]
        edge_list_out = sys.argv[4]
        node_map_file = sys.argv[5]
    else:
        sys.exit('Usage: python create_geoff_list.py <nodes_in> <edges_in>' +
                '<nodes_out> <edges_out> <node_map>')

    with open(node_map_file, 'r') as f:
        node_map = f.readlines()

    nodes = load_nodes(sys.argv[1], node_map)
    edges = load_edges(sys.argv[2])


    store_graph(nodes, edges, sys.argv[3], sys.argv[4])
