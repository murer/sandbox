#!/bin/bash -xe

docker rm -f couchdb || true

docker run -d -p 0.0.0.0:5984:5984 --name couchdb couchdb

while ! docker logs couchdb 2>&1 | grep "Apache CouchDB has started on"; do
    sleep 1;
done
