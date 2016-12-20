#!/bin/bash -xe

mkdir target || true

key='7d61bb87ebe736b23e9985f76481a785'
iv='00112233445566778899AABBCCDDEEFF'

(head -c '1001' img/test.bmp && \
   (tail -c '+1001' img/test.bmp | \
   openssl enc -aes-128-ecb -K "$key" -nosalt) \
) > target/test.ecb.bmp

(head -c '1001' img/test.bmp && \
  (tail -c '+1001' img/test.bmp | \
  openssl enc -aes-128-cbc -K "$key" -nosalt -iv "$iv") \
) > target/test.cbc.bmp
