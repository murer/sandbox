#!/bin/bash -xe

msg='murer'
key='7d61bb87ebe736b23e9985f76481a785'
iv='00112233445566778899AABBCCDDEEFF'
chipertext='Yd32Tkvopk1qqjdAnn+RKQ=='

echo -n "$chipertext" | base64 -d | openssl aes-128-cbc -e -K "$key" -iv "$iv" -nosalt -d -p
