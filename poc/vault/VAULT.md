# dsa vault


```shell
# Login

dsa vault login
dsa vault login --service service-account.json
dsa vault login --gce

# Create secret version

dsa vault create dsa 'any/secret' --secret 'mysecret' \
  --reader 'user:juquinha@dextra-sw.com' 'user:mariazinha@dextra-sw.com' \
  --writer 'group:dsa@dextra-sw.com'

dsa vault create juquinha 'any/secret' --secret 'mysecret' \
  --share 'group:dsa@dextra-sw.com'

# Read secret

dsa vault get dsa 'any/secret'
dsa vault get dsa 'any/secret' --rev 18253
dsa vault get juquinha 'any/secret'

# Delete secret

dsa vault delete dsa 'any/secret'
dsa vault delete dsa 'any/secret' --rev 18253
dsa vault delete juquinha 'any/secret'

# List secrets
dsa vault ls dsa
# -> any/secret
# -> other/xpto

# List versions
dsa vault ls dsa 'any/secret'
# -> 1232
# -> 1345
