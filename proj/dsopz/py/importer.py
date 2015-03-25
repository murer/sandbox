import oauth
import json
import sys
import argparse

def upload(dataset, block, namespace=None):
	for ent in block:
		ent['key']['partitionId'] = {
			'dataset': dataset,
			'namespace': namespace
		}
	params = { 
		'mode': 'NON_TRANSACTIONAL', 
		'mutation': { 
			'upsert': block
		}
	}
	resp = oauth.oauth_req_json('POST', 
		'https://www.googleapis.com/datastore/v1beta2/datasets/%s/commit' % (dataset), 
		params)

def get_kind(obj):
	path = obj['key']['path']
	i = len(path)
	last = path[i - 1]
	return last['kind']

def import_data(dataset, kinds=[], namespace=None, chunkSize=500):
	kinds = kinds or []
	kinds = [k.lower() for k in kinds]
	block = []
	count = 0
	while True:
		line = sys.stdin.readline()
		if not line:
			break
		line = line.strip()
		if not line or line.startswith('#'):
			continue
		obj = json.loads(line)
		kind = get_kind(obj)
		if kinds and kind.lower() not in kinds:
			continue
		block.append(obj)
		if len(block) >= chunkSize:
			upload(dataset, block, namespace)
			count += len(block)
			print >> sys.stderr, 'Uploaded', count
			block = []
	if block:
		upload(dataset, block, namespace)
		count += len(block)
	print >> sys.stderr, 'Done', count

def __main():
	parser = argparse.ArgumentParser(description='Exporter')
	parser.add_argument('-d', '--dataset', required=True, help='dataset')
	parser.add_argument('-n', '--namespace', required=True, help='namespace')
	parser.add_argument('-k', '--kinds', nargs='+', help='kinds')
	args = parser.parse_args()
	import_data(args.dataset, args.kinds, args.namespace)

if __name__ == '__main__':
	__main()
