import reader
import argparse
import sys

class Error(Exception):
	"""Exceptions"""

def load_kinds(dataset, namespace=None):
	max_kinds = 1000
	results = reader.query(dataset, 'select __key__ from __kind__ order by __key__', namespace = namespace, limit = max_kinds)
	l = len(results['entities'])
	if l >= max_kinds:
		raise Error('Too many kinds: %i' % (max_kinds))
	ret = []
	for ent in results['entities']:
		path = ent['key']['path']
		name = path[0]['name']
		if not name.startswith('__'):
			ret.append(name)
	return ret

def print_iterate(dataset, kinds=[], namespace=None):
	if not kinds:
		kinds = load_kinds(dataset, namespace)
	for kind in kinds:
		reader.print_iterate(dataset, 'select * from ' + kind + ' order by __key__', namespace=namespace, msg=kind)

def __main():
	parser = argparse.ArgumentParser(description='Exporter')
	parser.add_argument('-d', '--dataset', required=True, help='dataset')
	parser.add_argument('-n', '--namespace', required=True, help='namespace')
	parser.add_argument('-k', '--kinds', nargs='+', help='kinds')
	args = parser.parse_args()
	print_iterate(args.dataset, args.kinds, args.namespace)

if __name__ == '__main__':
	__main()
