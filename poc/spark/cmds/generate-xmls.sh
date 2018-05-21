#!/bin/bash -xe

rm -rf gen/data/xmls || true
mkdir -p gen/data/xmls

for k in $(seq $1); do
  echo -n '<?xml version="1.0"?>' > "gen/data/xmls/x-$k.xml"
  echo -n "<testxml>" >> "gen/data/xmls/x-$k.xml"
  for j in $(seq $2); do
    echo -n "<cli id=\"c$1-$2\"><cad nome=\"n$1-$2\"/></cli>" >> "gen/data/xmls/x-$k.xml"
  done
  echo -n "</testxml>" >> "gen/data/xmls/x-$k.xml"
done
