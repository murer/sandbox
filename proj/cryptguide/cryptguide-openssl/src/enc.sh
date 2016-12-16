#!/bin/bash -xe

msg='murer'
key='7d61bb87ebe736b23e9985f76481a785'
iv='00112233445566778899AABBCCDDEEFF'

echo -n "$msg" | openssl enc -aes-128-cbc -K "$key" -nosalt -iv "$iv" -p | head -n 2

echo -n "$msg" | openssl enc -aes-128-cbc -K "$key" -nosalt -iv "$iv" | base64
