#!/usr/bin/env python

'''
This script parses the amazon-meta dataset, extracts nodes and relationships
and stores them in the GEOFF format.

Nodes are:
    - Products (542.684)
    - Groups (10)
    - Users (optional, they are taken from Pokec) (1.555.124)
Relationships are:
    - Product-[:SIMILAR_TO]->Product (1.788.725)
    - Product-[:BELONGS_TO]->Group (542.684)
    - Product-[:REVIEWED_BY]->User (7.593.109)
'''

import json
import os
import re
import sys

# needs trailing slash!
OUT_DIR = 'out/'

# input file
AMAZON_FILE = 'amazon-meta.txt'

# output files
NODES_FILE = 'nodes-amazon.geoff'
EDGES_FILE = 'edges-amazon.geoff'

GROUPS = {}
GROUP_ID = 0 # initial id (will be incremented)

# need to avoid duplicates
USERS = set()

REVIEW_ID = 0 # initial id (will be incremented)

# needed to store the type as a property at a node
ID_KEY = '__id__'
TYPE_KEY = '__type__'
TYPE_PRODUCT = 'p'
TYPE_GROUP = 'g'
TYPE_USER = 'u'

# statistics
STATS = {'n_product_cnt': 0,
        'n_user_cnt': 0,
        'n_group_cnt': 0,
        'e_review_cnt': 0,
        'e_similar_cnt': 0,
        'e_belongs_cnt': 0}


def parse_line(line, index, sep=':'):
    '''
    Splits the given line by sep, strips the parts and returns the value at
    the given idx.
    '''
    return [a.strip() for a in line.split(sep)][index]

def parse_group(line):
    '''
    Parses the group information and returns the corresponding group id.
    '''
    group_name = parse_line(line, 1)
    global GROUP_ID
    if group_name not in GROUPS:
        GROUPS[group_name] = GROUP_ID
        save_geoff_node(f_nodes, GROUP_ID, {"name": group_name, TYPE_KEY:
            TYPE_GROUP, ID_KEY: GROUP_ID})
        GROUP_ID += 1
        STATS['n_group_cnt'] += 1
    return GROUPS[group_name]

def parse_reviews(f_nodes, f_edges, reviews, product_asin):
    '''
    Parse all reviews, create users and corresponding edges.
    '''
    for review in reviews:
        items = re.sub(r"\s+", ' ', review).split(' ')
        data = {'date': items[0],
                'rating': int(items[4]),
                'votes': int(items[6]),
                'helpful': int(items[8]),
                }
        save_geoff_edge(f_edges, product_asin, items[2], 'REVIEWED_BY', data)
        STATS['e_review_cnt'] += 1

        parse_user(f_nodes, items[2])

def parse_user(f_nodes, user_id):
    '''
    Makes sure that there are no duplicate user ids and stores them.
    '''
    if user_id not in USERS:
        USERS.add(user_id)
        #save_geoff_node(f_nodes, user_id, {TYPE_KEY: TYPE_USER})
        STATS['n_user_cnt'] += 1

def parse_product_meta(product_meta, f_nodes, f_edges):
    '''
    product_meta contains all lines regarding to a single amazon product. This
    function parses each line and calls the appropriate functions to parse the
    content.
    '''
    product = {TYPE_KEY: 'p'}
    line_field = []
    for idx, line in enumerate(product_meta):
        # product id
        #if line.startswith('Id:'):
        #    product['id'] = int(parse_line(line, 1))
        # amazon standard identification number (ASIN)
        if line.startswith('ASIN'):
            product[ID_KEY] = parse_line(line, 1)
        elif line.startswith('discontinued'):
            return None
        # title
        elif line.startswith('title'):
            title = parse_line(line, 1)
            if len(title) > 0:
                product['title'] = title
            else:
                product['title'] = 'untitled'
        # group (resulting in an edge)
        elif line.startswith('group'):
            group_id = int(parse_group(line))
            # instantly store the edge
            save_geoff_edge(f_edges, product[ID_KEY], group_id, 'BELONGS_TO')
            STATS['e_belongs_cnt'] += 1
        # salesrank
        elif line.startswith('salesrank'):
            product['salesrank'] = int(parse_line(line, 1))
        elif line.startswith('similar'):
            parse_similar_edges(f_edges, line, product[ID_KEY])
        # categories (stored unedited as array)
        elif line.startswith('categories'):
            # strip the number of categories to strip the whole block
            cat_cnt = int(parse_line(line, 1))
            product['categories'] = product_meta[idx+1:idx+cat_cnt+1]
        elif line.startswith('reviews'):
            # strip the number of reviews to extract the whole block
            review_cnt = int(line.split(': ')[2].split(' ')[0].strip())
            reviews = parse_reviews(f_nodes, f_edges, \
                    product_meta[idx+1:idx+review_cnt+1], product[ID_KEY])

    return product

def parse_similar_edges(f_edges, line, product_asin):
    '''
    Splits the ASINs of similar products and writes the edges into the file.
    '''
    for sim_asin in line.split('  ')[1:]:
        save_geoff_edge(f_edges, product_asin, sim_asin, 'SIMILAR_TO')
        STATS['e_similar_cnt'] += 1

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
    if not os.path.exists(OUT_DIR):
        os.makedirs(OUT_DIR)
    new_product = False
    n = 1
    product_meta = []
    with    open(AMAZON_FILE) as f_in, \
            open(OUT_DIR + NODES_FILE, 'w') as f_nodes, \
            open(OUT_DIR + EDGES_FILE, 'w') as f_edges:
        for line in f_in:
            line = line.strip()
            if line.startswith('#'):
                continue
            if line.startswith('Total items:'):
                n = float(line.split(':')[1].strip())
            if line.startswith('Id:'):
                product_meta = []
            elif line == ''and len(product_meta) > 1:
                p = parse_product_meta(product_meta,
                        f_nodes,
                        f_edges)
                if p is not None:
                    save_geoff_node(f_nodes, p[ID_KEY], p)
                    STATS['n_product_cnt'] += 1

            product_meta += [line]

            # logging
            if STATS['n_product_cnt'] % 1000 == 0:
                sys.stdout.write('\r%2.2f%%' % (STATS['n_product_cnt'] / n * 100))
                sys.stdout.flush()
                f_nodes.flush()
                f_edges.flush()


    print '\n'
    for (k,v) in STATS.iteritems():
        print '%s: %d' % (k,v)
